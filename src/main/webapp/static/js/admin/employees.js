var editingEid = null;

$(document).ready(function () {

    checkAdminSession(function () {
        loadEmployees();

        RoleManager.getAll(function (roles) {
            for (var i in roles) {
                var role = roles[i];
                option = $("<option>").val(role.rid).text(role.name).appendTo("#add-employee-role, #edit-employee-role");

            }
        });

    });

    $("#add-employee-submit").click(function () {
        var name = $("#add-employee-name").val();
        var password = $("#add-employee-password").val();
        var validate = true;
        if (name == "" || name == null) {
            $("#add-employee-name").parent().addClass("has-error");
            validate = false;
        } else {
            $("#add-employee-name").parent().removeClass("has-error");
        }
        if (password == "" || password == null) {
            $("#add-employee-password").parent().addClass("has-error");
            validate = false;
        } else {
            $("#add-employee-password").parent().removeClass("has-error");
        }
        if (!validate) {
            return;
        }
        var rid = $("#add-employee-role").val();
        EmployeeManager.add(name, md5(password), rid, function (eid) {
            if (eid == null) {
                sessionError();
                return;
            }
            $("#add-employee-modal").modal("hide");
            loadEmployees();
        });
    });

    $("#add-employee-modal").on("hidden.bs.modal", function () {
        $("#add-employee-modal .input-group").removeClass("has-error");
        $("#add-employee-modal input").val("");
    });
    
    $("#edit-employee-submit").click(function () {
        var name = $("#edit-employee-name").val();
        if (name == "" || name == null) {
            $("#edit-employee-name").parent().addClass("has-error");
            return;
        } else {
            $("#edit-employee-name").parent().removeClass("has-error");
        }
        var rid = $("#edit-employee-role").val();
        EmployeeManager.edit(editingEid, name, rid, function (success) {
            if (!success) {
                sessionError();
                return;
            }
            loadEmployees();
            $("#edit-employee-modal").modal("hide");
        })
    });

    $("#edit-employee-modal").on("hidden.bs.modal", function () {
        $("#edit-employee-modal .input-group").removeClass("has-error");
        $("#edit-employee-modal input").val("");
        editingEid = null;
    });

    $("#reset-password-submit").click(function () {
        var password = $("#reset-password").val();
        if (password == "" || password == null) {
            $("#reset-password").parent().addClass("has-error");
            return;
        } else {
            $("#reset-password").parent().removeClass("has-error");
        }
        EmployeeManager.resetPassword(editingEid, md5(password), function (success) {
            if (!success) {
                sessionError();
                return;
            }
            $("#reset-password-modal").modal("hide");
            $.messager.popup("重置密码成功！");
        });
    });

    $("#reset-password-modal").on("hidden.bs.modal", function () {
        $("#reset-password-modal .input-group").removeClass("has-error");
        $("#reset-password-modal input").val("");
        editingEid = null;
    });

});

function loadEmployees() {
    EmployeeManager.getAll(function (result) {
        if (!result.session) {
            sessionError();
            return;
        }

        $("#employee-list tbody").mengularClear();

        for (var i in result.data) {
            var employee = result.data[i];
            $("#employee-list tbody").mengular(".employee-list-template", {
                eid: employee.eid,
                createAt: employee.createAt.format(DATE_HOUR_MINUTE_FORMAT),
                updateAt: employee.updateAt.format(DATE_HOUR_MINUTE_FORMAT),
                name: employee.name,
                role: employee.role.name
            });

            $("#" + employee.eid + " .employee-list-password").click(function () {
                editingEid = $(this).mengularId();
                var name = $("#" + editingEid + " .employee-list-name").text();
                $("#reset-password-title").text(name);
                $("#reset-password-modal").modal("show");
            });

            $("#" + employee.eid + " .employee-list-edit").click(function () {
                editingEid = $(this).mengularId();
                EmployeeManager.get(editingEid, function (employee) {
                    if (employee == null) {
                        sessionError();
                        return;
                    }
                    fillValue({
                        "edit-employee-name": employee.name,
                        "edit-employee-role": employee.role.rid
                    })
                    $("#edit-employee-modal").modal("show");
                })

            });

            $("#" + employee.eid + " .employee-list-remove").click(function () {
                var eid = $(this).mengularId();
                var name = $("#" + eid + " .employee-list-name").text();
                $.messager.confirm("删除员工", "确认删除员工" + name + "吗？", function () {
                    EmployeeManager.remove(eid, function (success) {
                        if (!success) {
                            sessionError();
                            return;
                        }
                        $("#" + eid).remove();
                        $.messager.popup("删除成功！");
                    });
                });
            });
        }
    });
}