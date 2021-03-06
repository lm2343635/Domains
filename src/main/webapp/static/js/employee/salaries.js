var eid = request("eid");

$(document).ready(function () {
    checkEmployeeSession(function (viewer) {
        if (viewer.role.employee != RolePrevilgeHold) {
            $("#add-salary, #add-salary-modal").remove();
            $("#salary-list .salary-list-remove").remove();
        }

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
            $("#salary-panel .panel-title").fillText({
                name: employee.name,
                role: employee.role.name
            })
            document.title = employee.name + document.title;

            loadSalaries();
        });

    });

    $("#add-salary-submit").click(function () {
        var remark = $("#add-salary-remark").val();
        var money = $("#add-salary-money").val();
        var detail = $("#add-salary-detail").val();
        var validate = true;
        if (remark == null || remark == "") {
            $("#add-salary-remark").parent().addClass("has-error");
            validate = false;
        } else {
            $("#add-salary-remark").parent().removeClass("has-error");
        }
        if (money == null || money == "" || !isInteger(money)) {
            $("#add-salary-money").parent().addClass("has-error");
            validate = false;
        } else {
            $("#add-salary-money").parent().removeClass("has-error");
        }
        if (!validate) {
            return;
        }
        SalaryManager.add(eid, remark, money, detail, function (result) {
            if (!result.session) {
                sessionError();
                return;
            }
            if (!result.privilege) {
                $.messager.popup("当前用户无权限添加工资记录！");
                return;
            }
            if (result.data == null) {
                $.messager.popup("创建失败，请刷新重试！");
                return;
            }
            loadSalaries();
            $.messager.popup("创建成功！");
            $("#add-salary-modal").modal("hide");
        });
    });

    $("#add-salary-modal").on("hidden.bs.modal", function () {
        $("#add-salary-modal .input-group").removeClass("has-error");
        $("#add-salary-modal input").val("");
    });

});

function loadSalaries() {
    SalaryManager.getByEid(eid, function (result) {
        if (!result.session) {
            sessionError();
            return;
        }
        if (!result.privilege) {
            $.messager.popup("当前用户无权查看该员工的工资记录！");
            return;
        }

        $("#salary-list").mengularClear();

        for (var i in result.data) {
            var salary = result.data[i];
            $("#salary-list").mengular(".salary-list-template", {
                sid: salary.sid,
                createAt: salary.createAt.format(DATE_HOUR_MINUTE_SECOND_FORMAT),
                remark: salary.remark,
                money: salary.money
            });

            $("#" + salary.sid + " .salary-list-remark").click(function () {
                var sid = $(this).mengularId();
                SalaryManager.get(sid, function (result) {
                    if (!result.session) {
                        sessionError();
                        return;
                    }
                    if (!result.privilege) {
                        $.messager.popup("当前用户无权查看该员工的工资记录！");
                        return;
                    }
                    $("#detail-modal-remark").text(result.data.remark);
                    $("#detail-modal-detail").text(result.data.detail);
                    $("#detail-modal").modal("show");
                });
            });

            $("#" + salary.sid + " .salary-list-remove").click(function () {
                var sid = $(this).mengularId();
                var remark = $("#" + sid + " .salary-list-remark").text();
                $.messager.confirm("删除工作记录", "确认删除工资记录" + remark + "吗？", function () {
                    SalaryManager.remove(sid, function (result) {
                        if (!result.session) {
                            sessionError();
                            return;
                        }
                        if (!result.privilege) {
                            $.messager.popup("当前用户无权删除该员工的工资记录！");
                            return;
                        }
                        $.messager.popup("删除成功！");
                        $("#" + sid).remove()
                    });;
                });
            });
        }
    });
}