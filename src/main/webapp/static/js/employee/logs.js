var pageSize = 20;

var eid = request("eid");

$(document).ready(function () {

    $("#search-log-start, #search-log-end").datetimepicker({
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        startView: 2,
        minView: 2,
        language: "zh-CN"
    });

    checkEmployeeSession(function () {

        EmployeeManager.get(eid, function (result) {

            if (!result.session) {
                sessionError();
                return;
            }
            if (!result.privilege) {
                $.messager.popup("当前用户无权限查看！");
                return;
            }

            var employee = result.data;
            $("#log-panel .panel-title").fillText({
                name: employee.name,
                role: employee.role.name
            })
            document.title = employee.name + document.title;

            loadLogs();
        });

    });

    $("#search-submit").click(function () {
        var customer = $("#search-log-customer").val();
        var title = $("#search-log-title").val();
        var start = $("#search-log-start").val();
        var end = $("#search-log-end").val();
        searchLogs(customer, title, start, end, 1);
    });

    $("#search-reset").click(function () {
        $("#search-panel input, #search-panel select").val("");
        $("#search-submit").click();
    });

});

function loadLogs() {
    $("#search-panel input, #search-panel select").val("");
    searchLogs(null, null, null, null, 1);
}

function searchLogs(customer, title, start, end, page) {
    LogManager.getSearchCount(eid, customer, title, start, end, function (result) {
        if (!result.session) {
            sessionError();
            return;
        }
        if (!result.privilege) {
            return;
        }

        $("#page-size").text(pageSize);

        var count = result.data;
        $("#page-count").text(count);
        $("#page-nav ul").empty();
        for (var i = 1; i < Math.ceil(count / pageSize + 1); i++) {
            var li = $("<li>").append($("<a>").attr("href", "javascript:void(0)").text(i));
            if (page == i) {
                li.addClass("active");
            }
            $("#page-nav ul").append(li);
        }

        $("#page-nav ul li").each(function (index) {
            $(this).click(function () {
                searchLogs(customer, title, start, end, index + 1);
                $("html, body").animate({
                    scrollTop: 0
                }, 300);
            });
        });
    });

    LogManager.search(eid, customer, title, start, end, page, pageSize, function (result) {
        if (!result.session) {
            sessionError();
            return;
        }
        if (!result.privilege) {
            $.messager.popup("当前账户无权限查看改员工的日志！");
            return;
        }

        $("#log-list").mengularClear();
        for (var i in result.data) {
            var log = result.data[i];
            $("#log-list").mengular(".log-list-template", {
                lid: log.lid,
                createAt: log.createAt.format(DATE_HOUR_MINUTE_SECOND_FORMAT),
                updateAt: log.updateAt.format(DATE_HOUR_MINUTE_SECOND_FORMAT),
                cid: log.customer.cid,
                customer: log.customer.name,
                title: log.title
            });

            $("#" + log.lid + " .log-list-show").click(function () {
                var lid = $(this).mengularId();
                LogManager.get(lid, function (result) {
                    if (!result.session) {
                        sessionError();
                        return;
                    }
                    if (!result.privilege) {
                        $.messager.popup("该账户无权限查看工作日志！");
                        return;
                    }
                    fillText({
                        "log-title": result.data.title,
                        "log-content": result.data.content
                    });
                    $("#log-modal").modal("show");
                });

            });
        }
    });
}