var pageSize = 20;
var eid = null;

$(document).ready(function () {

    checkEmployeeSession(function (employee) {
        eid = employee.eid;

        loadTopBulletins();
        loadUntopBulletins(1);
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
                content: bulletin.content
            });
        }
    });
}

function loadUntopBulletins(page) {
    BulletinManager.getUntopByPage(page, pageSize, function (result) {
        if (!result.session) {
            sessionError();
            return;
        }

        $("#bulletin-list").mengularClear();
        for (var i in result.data) {
            var bulletin = result.data[i];
            $("#bulletin-list").mengular(".bulletin-list-template", {
                bid: bulletin.bid,
                employee: bulletin.employee.name,
                createAt: bulletin.createAt.format(DATE_HOUR_MINUTE_SECOND_FORMAT),
                content: bulletin.content
            });

            if (bulletin.employee.eid == eid) {
                $("#" + bulletin.bid + " .bulletin-list-top").click(function () {
                    var bid = $(this).mengularId();
                    var info = $("#" + bid + " .bulletin-list-info").text();
                    $.messager.confirm("置顶公告", "确认要置顶" + info + "的公告吗？", function () {
                        BulletinManager.top(bid, true, function (result) {
                            if (!result.session) {
                                sessionError();
                                return;
                            }
                            if (!result.privilege) {
                                $.messager.popup("当前用户无权限置顶该公告！");
                                return;
                            }
                            if (!result.data) {
                                $.messager.popup("置顶失败，请重试！");
                                return;
                            }
                            loadTopBulletins();
                            $("#" + bid).remove();
                            $.messager.popup("置顶成功！");
                        });
                    });
                }).show();

                $("#" + bulletin.bid + " .bulletin-list-remove").click(function () {
                    var bid = $(this).mengularId();
                    var info = $("#" + bid + " .bulletin-list-info").text();
                    $.messager.confirm("删除公告", "确认要删除" + info + "的公告吗？", function () {
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
                    });
                }).show();
            } else {
                $("#" + bulletin.bid + " .bulletin-list-top").remove();
                $("#" + bulletin.bid + " .bulletin-list-remove").remove();
            }

        }
    });
}
