$(document).ready(function () {

    checkEmployeeSession(function (employee) {
        if (employee.role.undevelopedR == RolePrevilgeNone) {
            $.messager.popup("当前用户无权查看未开发客户！");
            return;
        }
        loadUndeveloped();
    });
    
    $("#add-undeveloped-submit").click(function () {
        var name = $("#add-undeveloped-name").val();
        var capital = $("#add-undeveloped-capital").val();
        var contact = $("#add-undeveloped-contact").val();
        var validate = true;
        if (name == "" || name == null) {
            $("#add-undeveloped-name").parent().addClass("has-error");
            validate = false;
        } else {
            $("#add-undeveloped-name").parent().removeClass("has-error");
        }
        if (capital == "" || capital == null || !isInteger(capital)) {
            $("#add-undeveloped-capital").parent().addClass("has-error");
            validate = false;
        } else {
            $("#add-undeveloped-capital").parent().removeClass("has-error");
        }
        if (contact == "" || contact == null) {
            $("#add-undeveloped-contact").parent().addClass("has-error");
            validate = false;
        } else {
            $("#add-undeveloped-contact").parent().removeClass("has-error");
        }
        if (!validate) {
            return;
        }
        CustomerManager.addUndeveloped(name, capital, contact, function (result) {
            if (!result.session) {
                sessionError();
                return;
            }
            if (!result.success) {
                $.messager.popup("当前账户无权限创建未开发客户！");
            } else {
                loadUndeveloped();
            }
            $("#add-undeveloped-modal").modal("hide");
        });
    });

});

function loadUndeveloped() {

}