var limit = 20;

$(document).ready(function () {

    checkEmployeeSession(function () {
        loadBulletins();
    });

    $("#bulletins-head .nav li").click(function () {
        $("#bulletins-head .nav li").removeClass("active");
        $(this).addClass("active");
        limit = parseInt($(this).attr("data-limit"));
        loadBulletins();
    });

});

function loadBulletins() {
    BulletinManager.getByLimit(limit, function (result) {
        if (!result.session) {
            sessionError();
            return;
        }

        $("#bulletin-list").mengularClear();
        for (var i in result.data) {
            var bulletin = result.data[i];
            $("#bulletin-list").mengular(".bulletin-list-template", {
                bid: bulletin.bid,
                employee: bulletin.employee,
                createAt: bulletin.createAt.format(DATE_HOUR_MINUTE_SECOND_FORMAT),
                content: bulletin.content
            });
        }
    });
}