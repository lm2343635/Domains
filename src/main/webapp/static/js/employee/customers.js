var pageSize = 50;

var state = request("state");

if (state != CustomerStateDeveloping
    && state != CustomerStateDeveloped
    && state != CustomerStateLost) {
    state = CustomerStateUndeveloped;
}

document.title = CustomerStateNames[state] + "的客户";

$(document).ready(function () {

    if (state != CustomerStateUndeveloped) {
        $("#add-undeveloped").remove();
    }

    $("#customer-panel .panel-heading .nav li").eq(state).addClass("active");

    AreaManager.getAll(function (areas) {
        for (var i in areas) {
            var area = areas[i];
            var option = $("<option>").val(area.aid).text(area.name);
            $("#add-undeveloped-area, #search-customer-area").append(option);
        }
    });

    IndustryManager.getAll(function (industries) {
        for (var i in industries) {
            var industry = industries[i];
            var option = $("<option>").val(industry.iid).text(industry.name);
            $("#add-undeveloped-industry, #search-customer-industry").append(option);
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

    $("#search-submit").click(function () {
        var name = $("#search-customer-name").val();
        var aid = $("#search-customer-area").val();
        var iid = $("#search-customer-industry").val();
        var lower = $("#search-customer-capital-lower").val();
        var higher = $("#search-customer-capital-higher").val();
        var validate = true;
        if (lower != null && lower != "") {
            if (!isInteger(lower) || lower < 0) {
                $("#search-customer-capital-lower").parent().addClass("has-error");
                validate = false;
            } else {
                $("#search-customer-capital-lower").parent().removeClass("has-error");
            }
        }
        if (higher != null && higher != "") {
            if (!isInteger(higher) || higher < 0) {
                $("#search-customer-capital-higher").parent().addClass("has-error");
                validate = false;
            } else {
                $("#search-customer-capital-higher").parent().removeClass("has-error");
            }
        }
        if (!validate) {
            $.messager.popup("注册资金必须为正整数！");
            return;
        }
        searchCustomers(name, aid, iid, lower, higher, 1);
    });

    $("#search-reset").click(function () {
        $("#search-panel input, #search-panel select").val("");
    });

});

function loadCustomers() {
    $("#search-panel input, #search-panel select").val("");
    searchCustomers(null, null, null, 0, 0, 1);
}

function searchCustomers(name, aid, iid, lower, higher, page) {
    CustomerManager.getSearchCount(state, name, aid, iid, lower, higher, function (result) {
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
                searchCustomers(name, aid, iid, lower, higher, index + 1);
                $("html, body").animate({
                    scrollTop: 0
                }, 300);
            });
        });
    });
    
    CustomerManager.search(state, name, aid, iid, lower, higher, page, pageSize, function (result) {
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