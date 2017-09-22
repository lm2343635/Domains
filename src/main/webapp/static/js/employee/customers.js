var state = request("state");
if (state != CustomerStateDeveloping
    && state != CustomerStateDeveloped
    && state != CustomerStateLost) {
    state = CustomerStateUndeveloped;
}

$(document).ready(function () {

    $("#customer-panel .panel-heading .nav li").eq(state).addClass("active");

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
            if (!result.privilege) {
                $.messager.popup("当前账户无权限创建未开发客户！");
            } else {
                loadUndeveloped();
            }
            $("#add-undeveloped-modal").modal("hide");
        });
    });

});

function loadUndeveloped() {
    CustomerManager.getByState(state, function (result) {
        if (!result.session) {
            sessionError();
            return;
        }
        if (!result.privilege) {
            $.messager.popup("当前账户无权限查看" + CustomerStateNames[state] + "的客户！");
            return;
        }

        $("#undeveloped-list tbody").mengularClear();

        for (var i in result.data) {
            var customer = result.data[i];
            $("#undeveloped-list tbody").mengular(".undeveloped-list-template", {
                cid: customer.cid,
                createAt: customer.createAt.format(DATE_HOUR_MINUTE_FORMAT),
                updateAt: customer.updateAt.format(DATE_HOUR_MINUTE_FORMAT),
                name: customer.name,
                contact: customer.contact,
                capital: customer.capital
            });
        }
    });
}