var state = request("state");
if (state != CustomerStateDeveloping
    && state != CustomerStateDeveloped
    && state != CustomerStateLost) {
    state = CustomerStateUndeveloped;
}

$(document).ready(function () {

    if (state != CustomerStateUndeveloped) {
        $("#add-undeveloped").remove();
    }

    $("#customer-panel .panel-heading .nav li").eq(state).addClass("active");

    AreaManager.getAll(function (areas) {
        for (var i in areas) {
            var area = areas[i];
            $("<option>").val(area.aid).text(area.name).appendTo("#add-undeveloped-area");
        }
    });

    IndustryManager.getAll(function (industries) {
        for (var i in industries) {
            var industry = industries[i];
            $("<option>").val(industry.iid).text(industry.name).appendTo("#add-undeveloped-industry");
        }
    });

    checkEmployeeSession(function (employee) {
        var listable = true;
        switch (state) {
            case CustomerStateUndeveloped:
                if (employee.role.undevelopedR == RolePrevilgeNone) {
                    listable = false;
                }
                break;
            case CustomerStateDeveloping:
                if (employee.role.developingR == RolePrevilgeNone) {
                    listable = false;
                }
                break;
            case CustomerStateDeveloped:
                if (employee.role.developedR == RolePrevilgeNone) {
                    listable = false;
                }
                break;
            case CustomerStateLost:
                if (employee.role.lostR == RolePrevilgeNone) {
                    listable = false;
                }
                break;
            default:
                break;
        }
        if (!listable) {
            $.messager.popup("当前用户无权查看" + CustomerStateNames[state] + "客户！");
            return;
        }
        loadCustomers();
    });
    
    $("#add-undeveloped-submit").click(function () {
        var name = $("#add-undeveloped-name").val();
        var capital = $("#add-undeveloped-capital").val();
        var contact = $("#add-undeveloped-contact").val();
        var aid = $("#add-undeveloped-area").val();
        var iid = $("#add-undeveloped-industry").val();
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
        if (aid == "" || aid == null) {
            $("#add-undeveloped-area").parent().addClass("has-error");
            validate = false;
        } else {
            $("#add-undeveloped-area").parent().removeClass("has-error");
        }
        if (iid == "" || iid == null) {
            $("#add-undeveloped-industry").parent().addClass("has-error");
            validate = false;
        } else {
            $("#add-undeveloped-industry").parent().removeClass("has-error");
        }
        if (!validate) {
            return;
        }
        CustomerManager.addUndeveloped(name, capital, contact, aid, iid, function (result) {
            if (!result.session) {
                sessionError();
                return;
            }
            if (!result.privilege) {
                $.messager.popup("当前账户无权限创建未开发客户！");
            } else {
                loadCustomers();
            }
            $("#add-undeveloped-modal").modal("hide");
        });
    });

});

function loadCustomers() {
    CustomerManager.getByState(state, function (result) {
        if (!result.session) {
            sessionError();
            return;
        }
        if (!result.privilege) {
            $.messager.popup("当前账户无权限查看" + CustomerStateNames[state] + "的客户！");
            return;
        }

        $("#customer-list tbody").mengularClear();

        for (var i in result.data) {
            var customer = result.data[i];
            $("#customer-list tbody").mengular(".customer-list-template", {
                cid: customer.cid,
                createAt: customer.createAt.format(DATE_HOUR_MINUTE_FORMAT),
                updateAt: customer.updateAt.format(DATE_HOUR_MINUTE_FORMAT),
                name: customer.name,
                capital: customer.capital,
                area: customer.area.name,
                industry: customer.industry.name
            });
        }
    });
}