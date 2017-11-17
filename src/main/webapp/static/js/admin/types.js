$(document).ready(function () {
    checkAdminSession(function () {
        loadTypes();
    });

    $("#add-type-submit").click(function () {
        var name = $("#add-type-name").val();
        if (name == null || name == "") {
            $("#add-type-name").parent().addClass("has-error");
            return;
        } else {
            $("#add-type-name").parent().addClass("has-error");
        }
        TypeManager.add(name, function (aid) {
            if (aid == null) {
                sessionError();
                return;
            }
            $.messager.popup("创建成功！");
            $("#add-type-modal").modal("hide");
            loadTypes();
        });
    });

    $("#add-type-modal").on("hidden.bs.modal", function () {
        $("#add-type-modal .input-group").removeClass("has-error");
        $("#add-type-modal input").val("");
    });
});

function loadTypes() {
    TypeManager.getAll(function (result) {
        if (!result.session) {
            sessionError();
            return;
        }

        $("#type-list tbody").mengularClear();

        for (var i in result.data) {
            var type = result.data[i];
            $("#type-list tbody").mengular(".type-list-template", {
                tid: type.tid,
                createAt: type.createAt.format(DATE_HOUR_MINUTE_SECOND_FORMAT),
                name: type.name
            });
        }
    });
}