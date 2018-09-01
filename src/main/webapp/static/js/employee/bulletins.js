var pageSize = 20;
var page = 1;
var end = false;
var eid = null;
var bulletinPrivilege = false;

$(document).ready(function () {

    checkEmployeeSession(function (employee) {
        eid = employee.eid;
        bulletinPrivilege = employee.role.bulletin == RolePrevilgeHold;

        loadTopBulletins();
        loadUntopBulletins(page);
    });

    $(window).scroll(function () {
        if ($(document).scrollTop() >= $(document).height() - $(window).height() && !end) {
            page++;
            loadUntopBulletins(page);
        }
    });

});

function loadTopBulletins() {
    BulletinManager.getTop(function (result) {
        if (!result.session) {
            sessionError();
            return;
        }

        $("#top-list").mengularClear();
        for (var i in result.data) {
            var bulletin = result.data[i];
            $("#top-list").mengular(".top-list-template", {
                bid: bulletin.bid,
                employee: bulletin.employee.name,
                createAt: bulletin.createAt.format(DATE_HOUR_MINUTE_SECOND_FORMAT),
                updateAt: bulletin.updateAt.format(DATE_HOUR_MINUTE_SECOND_FORMAT),
                content: bulletin.content
            });

            if (bulletin.employee.eid == eid || bulletinPrivilege) {
                $("#" + bulletin.bid + " .bulletin-list-untop").click(function () {
                    var bid = $(this).mengularId();
                    var info = $("#" + bid + " .bulletin-list-info").text();
                    $.messager.confirm("取消置顶公告", "确认要取消置顶置顶" + info + "的公告吗？", function () {
                        setTop(bid, false);
                    });
                }).show();

                $("#" + bulletin.bid + " .bulletin-list-remove").click(function () {
                    var bid = $(this).mengularId();
                    var info = $("#" + bid + " .bulletin-list-info").text();
                    $.messager.confirm("删除公告", "确认要删除" + info + "的公告吗？", function () {
                        removeBulletin(bid);
                    });
                }).show();

                $("#" + bulletin.bid + " .bulletin-list-edit").show();
            } else {
                $("#" + bulletin.bid + " .bulletin-list-untop").remove();
                $("#" + bulletin.bid + " .bulletin-list-remove").remove();
                $("#" + bulletin.bid + " .bulletin-list-edit").remove();
            }
        }
    });
}

function loadUntopBulletins(page) {
    $("#loading").show();

    BulletinManager.getUntopByPage(page, pageSize, function (result) {
        if (!result.session) {
            sessionError();
            return;
        }
        $("#loading").hide();
        if (result.data.length == 0) {
            end = true;
            return;
        }

        for (var i in result.data) {
            var bulletin = result.data[i];
            $("#bulletin-list").mengular(".bulletin-list-template", {
                bid: bulletin.bid,
                employee: bulletin.employee.name,
                createAt: bulletin.createAt.format(DATE_HOUR_MINUTE_SECOND_FORMAT),
                updateAt: bulletin.updateAt.format(DATE_HOUR_MINUTE_SECOND_FORMAT),
                content: bulletin.content
            });

            if (bulletin.employee.eid == eid || bulletinPrivilege) {
                $("#" + bulletin.bid + " .bulletin-list-top").click(function () {
                    var bid = $(this).mengularId();
                    var info = $("#" + bid + " .bulletin-list-info").text();
                    $.messager.confirm("置顶公告", "确认要置顶" + info + "的公告吗？", function () {
                        setTop(bid, true);
                    });
                }).show();

                $("#" + bulletin.bid + " .bulletin-list-remove").click(function () {
                    var bid = $(this).mengularId();
                    var info = $("#" + bid + " .bulletin-list-info").text();
                    $.messager.confirm("删除公告", "确认要删除" + info + "的公告吗？", function () {
                        removeBulletin(bid);
                    });
                }).show();

                $("#" + bulletin.bid + " .bulletin-list-edit").show();
            } else {
                $("#" + bulletin.bid + " .bulletin-list-top").remove();
                $("#" + bulletin.bid + " .bulletin-list-remove").remove();
                $("#" + bulletin.bid + " .bulletin-list-edit").remove();
            }

        }

    });
}

function setTop(bid, top) {
    var action = top ? "置顶" : "取消置顶";
    BulletinManager.top(bid, top, function (result) {
        if (!result.session) {
            sessionError();
            return;
        }
        if (!result.privilege) {
            $.messager.popup("当前用户无权限" + action + "该公告！");
            return;
        }
        if (!result.data) {
            $.messager.popup(action + "失败，请重试！");
            return;
        }
        if (top) {
            loadTopBulletins();
        } else {
            loadUntopBulletins(1);
        }
        $("#" + bid).remove();
        $.messager.popup(action + "成功！");
    });
}

function removeBulletin(bid) {
    BulletinManager.remove(bid, function (result) {
        if (!result.session) {
            sessionError();
            return;
        }
        if (!result.privilege) {
            $.messager.popup("当前用户无权限删除该公告！");
            return;
        }
        if (!result.data) {
            $.messager.popup("删除失败，请重试！");
            return;
        }
        $("#" + bid).remove();
        $.messager.popup("删除成功！");
    });
}