var redirect = decodeURIComponent(request("redirect"));

$(document).ready(function () {
    $("#admin-submit-button").click(function () {
        var name = $("#admin-number-input").val();
        var password = $("#admin-password-input").val();
        var validate = true;
        if (name == "" || name == null) {
            $("#admin-number-input").parent().addClass("has-error");
            validate = false;
        } else {
            $("#admin-number-input").parent().removeClass("has-error");
        }
        if (password == "" || password == null) {
            $("#admin-password-input").parent().addClass("has-error");
            validate = false;
        } else {
            $("#admin-password-input").parent().removeClass("has-error");
        }
        if (validate) {
            EmployeeManager.login(name, md5(password), function (result) {
                var status = result.data;
                if (status != EmployeeLoginSuccess) {
                    $("#admin-number-input").parent().addClass("has-error");
                    $("#admin-password-input").parent().addClass("has-error");
                    switch (status) {
                        case EmployeeLoginNotFound:
                            $.messager.popup("用户名未找到！");
                            break;
                        case EmployeeLoginWrongPassword:
                            $.messager.popup("用户密码错误！");
                            break;
                        case EmployeeLoginNotEnable:
                            $.messager.popup("用户已被管理员禁用！");
                            break;
                        default:
                            break;
                    }
                    return;
                }

                location.href = (redirect == null || redirect == "") ? "bulletins.html" : redirect;
            });
        }
    });

    $("body").keydown(function () {
        if (event.keyCode == 13) {
            $("#admin-submit-button").click();
        }
    });

});