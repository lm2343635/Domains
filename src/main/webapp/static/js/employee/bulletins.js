var limit = 20;

$(document).ready(function () {

    checkEmployeeSession(function () {
        loadBulletins();
    });


    $("#add-bulletin-submit").click(function () {
        var content = $("#add-bulletin-content").val();
        if (content == null || content == "") {
            $.messager.popup("公告内容不能为空！");
            return;
        }
        BulletinManager.add(content, function (result) {
            if (!result.session) {
                sessionError();
                return;
            }
            if (result.data == null) {
                $.messager.popup("创建失败，请重试！");
                return;
            }
            $.messager.popup("创建成功！");
            loadBulletins();
            $("#add-bulletin-modal").modal("hide");
        });
    });

    $("#add-bulletin-modal").on("hidden.bs.modal", function () {
        $("#add-bulletin-modal textarea").val("");
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