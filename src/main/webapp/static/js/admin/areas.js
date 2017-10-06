var editingAid = null;

$(document).ready(function () {

    checkAdminSession(function () {
        loadArea();
    });

    $("#add-area-submit").click(function () {
        var name = $("#add-area-name").val();
        if (name == null || name == "") {
            $("#add-area-name").parent().addClass("has-error");
            return;
        } else {
            $("#add-area-name").parent().addClass("has-error");
        }
        AreaManager.add(name, function (aid) {
            if (aid == null) {
                sessionError();
                return;
            }
            $.messager.popup("创建成功！");
            $("#add-area-modal").modal("hide");
            loadArea();
        });
    });

    $("#add-area-modal").on("hidden.bs.modal", function () {
        $("#add-area-modal .input-group").removeClass("has-error");
        $("#add-area-modal input").val("");
    });

});

function loadArea() {
    AreaManager.getAll(function (areas) {
        $("#area-list tbody").mengularClear();

        for (var i in areas) {
            var area = areas[i];
            $("#area-list tbody").mengular(".area-list-template", {
                aid: area.aid,
                createAt: area.createAt.format(DATE_HOUR_MINUTE_SECOND_FORMAT),
                name: area.name,
                customers: area.customers
            });
        }
    });
}