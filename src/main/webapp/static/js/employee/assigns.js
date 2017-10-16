var pageSize = 50;

var eid = request("eid");
var state = request("state");

if (state != CustomerStateDeveloping
    && state != CustomerStateDeveloped) {
    state = CustomerStateDeveloping;
}

$(document).ready(function () {

    $("#customer-panel .panel-heading .nav").fillText({
        eid: eid
    })
    $("#customer-panel .panel-heading .nav li").eq(state - 1).addClass("active");

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
            $("#customer-panel .panel-title").fillText({
                name: employee.name,
                role: employee.role.name,
                state: CustomerStateNames[state]
            })

            loadAssigns();
        });

    });

});

function loadAssigns() {
    $("#search-panel input, #search-panel select").val("");
    searchAssigns(null, null, null, 0, 0, 1);
}

function searchAssigns(name, aid, iid, lower, higher, page) {
    AssignManager.getSearchCount(eid, state, name, aid, iid, lower, higher, function (result) {
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
                searchAssigns(name, aid, iid, lower, higher, index + 1);
                $("html, body").animate({
                    scrollTop: 0
                }, 300);
            });
        });
    });

    AssignManager.search(eid, state, name, aid, iid, lower, higher, page, pageSize, function (result) {
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
            var customer = result.data[i].customer;
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