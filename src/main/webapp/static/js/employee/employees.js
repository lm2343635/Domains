var limit = 50;

$(document).ready(function () {

    checkEmployeeSession(function (employee) {
        loadEmployees();

        if (employee.role.employee == RolePrevilgeHold) {
            $("#log-panel").show();
            loadLatestLogs();
        } else {
            $("#log-panel, #log-modal").remove();
        }
    });

    $("#log-limit li").click(function () {
        $("#log-limit li").removeClass("active");
        $(this).addClass("active");
        limit = parseInt($(this).attr("data-limit"));
        loadLatestLogs();
    });
    
});

function loadEmployees() {
    EmployeeManager.getAll(function (result) {
        if (!result.session) {
            sessionError();
            return;
        }

        $("#employee-list").mengularClear();

        for (var i in result.data) {
            var employee = result.data[i];
            $("#employee-list").mengular(".employee-list-template", {
                eid: employee.eid,
                createAt: employee.createAt.format(DATE_HOUR_MINUTE_FORMAT),
                updateAt: employee.updateAt.format(DATE_HOUR_MINUTE_FORMAT),
                name: employee.name,
                role: employee.role.name
            });

        }
    });
}

function loadLatestLogs() {
    LogManager.getByLimit(limit, function (result) {
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
                employee: log.employee.name,
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