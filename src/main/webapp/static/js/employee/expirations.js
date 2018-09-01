var pageSize = 20;

$(document).ready(function () {

    checkEmployeeSession(function (employee) {
        if (employee.role.expiration != RolePrevilgeHold) {
            $.messager.popup("当前用户无权限查看到期时间列表！");
            return;
        }

        TypeManager.getByCategory(TypeCategoryExpiration, function (result) {
            if (!result.session) {
                return;
            }
            for (var i in result.data) {
                var type = result.data[i];
                $("<option>").val(type.tid).text(type.name).appendTo("#search-expiration-type");
            }
        });

        searchExpirations(null, null, null, null, 1);

    });

    $("#search-expiration-start, #search-expiration-end").datetimepicker({
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        startView: 2,
        minView: 2,
        language: "zh-CN"
    });

    $("#search-expiration-quick-buttons button").click(function () {
        var today = new Date().format(YEAR_MONTH_DATE_FORMAT);
        switch($(this).index()) {
            case 0:
                $("#search-expiration-start").val("");
                $("#search-expiration-end").val(today);
                break;
            case 1:
                $("#search-expiration-start").val(today);
                $("#search-expiration-end").val(nextDay(7).format(YEAR_MONTH_DATE_FORMAT));
                break;
            case 2:
                $("#search-expiration-start").val(today);
                $("#search-expiration-end").val(nextDay(30).format(YEAR_MONTH_DATE_FORMAT));
                break;
            default:
                break;
        }
        $("#search-submit").click();
    });

    $("#search-submit").click(function () {
        var tid = $("#search-expiration-type").val();
        var customer = $("#search-expiration-customer").val();
        var start = $("#search-expiration-start").val();
        var end = $("#search-expiration-end").val();
        searchExpirations(tid, customer, start, end, 1);
    });

    $("#search-reset").click(function () {
        $("#search-panel input, #search-panel select").val("");
        $("#search-submit").click();
    });

});

function searchExpirations(tid, customer, start, end, page) {
    ExpirationManager.getCount(tid, customer, start, end, function (result) {
        if (!result.session) {
            sessionError();
            return;
        }
        if (!result.privilege) {
            return;
        }

        $("#page-size").text(pageSize);

        var searchCount = result.data.searchCount;
        $("#page-count").text(searchCount);
        $("#page-nav ul").empty();
        for (var i = 1; i < Math.ceil(searchCount / pageSize + 1); i++) {
            var li = $("<li>").append($("<a>").attr("href", "javascript:void(0)").text(i));
            if (page == i) {
                li.addClass("active");
            }
            $("#page-nav ul").append(li);
        }

        $("#page-nav ul li").each(function (index) {
            $(this).click(function () {
                searchExpirations(tid, customer, start, end, index + 1);
                $("html, body").animate({
                    scrollTop: 0
                }, 300);
            });
        });

        $("#expiration-money-count").text(result.data.moneyCount);
    });

    ExpirationManager.search(tid, customer, start, end, page, pageSize, function (result) {
        if (!result.session) {
            sessionError();
            return;
        }
        if (!result.privilege) {
            return;
        }
        $("#expiration-list").mengularClear();

        for (var i in result.data) {
            var expiration = result.data[i];
            $("#expiration-list").mengular(".expiration-list-template", {
                eid: expiration.eid,
                createAt: expiration.createAt.format(DATE_HOUR_MINUTE_SECOND_FORMAT),
                updateAt: expiration.updateAt.format(DATE_HOUR_MINUTE_SECOND_FORMAT),
                type: expiration.type.name,
                expireAt: expiration.expireAt.format(YEAR_MONTH_DATE_FORMAT),
                money: expiration.money,
                cid: expiration.customer.cid,
                customer: expiration.customer.name
            });
        }
    });
}