$(document).ready(function () {

    checkAdminSession(function () {
        loadEmployees();

        RoleManager.getAll(function (roles) {
            for (var i in roles) {
                var role = roles[i];
                $("<option>").val(role.rid).text(role.name).appendTo("#add-employee-role");
            }
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
            });
        });
    });

    

});

function loadEmployees() {

}