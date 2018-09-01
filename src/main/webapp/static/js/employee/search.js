GlobalSearchCustomer = 0;
GlobalSearchDomain = 1;

var keyword = decodeURIComponent(request("keyword"));

$(document).ready(function () {
    $("#keyword").text(keyword);

    checkEmployeeSession(function () {
        EmployeeManager.globalSearch(keyword, function (result) {
            if (!result.session) {
                sessionError();
                return;
            }
            fillText({
                customers: result.data[GlobalSearchCustomer].length,
                domains: result.data[GlobalSearchDomain].length
            })
            for (var i in result.data[GlobalSearchCustomer]) {
                var customer = result.data[GlobalSearchCustomer][i];
                $("#customer-list").mengular(".customer-list-template", {
                    cid: customer.id,
                    description: customer.description,
                    updateAt: customer.updateAt.format(DATE_HOUR_MINUTE_SECOND_FORMAT)
                });
            }
            for (var i in result.data[GlobalSearchDomain]) {
                var domain = result.data[GlobalSearchDomain][i];
                $("#domain-list").mengular(".domain-list-template", {
                    did: domain.id,
                    sid: domain.parentId,
                    description: domain.description,
                    updateAt: domain.updateAt.format(DATE_HOUR_MINUTE_SECOND_FORMAT)
                });
            }

        });
    });
});