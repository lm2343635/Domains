var editingIid = null;

$(document).ready(function () {

    checkAdminSession(function () {
        loadIndustry();
    });

    $("#add-industry-submit").click(function () {
        var name = $("#add-industry-name").val();
        if (name == null || name == "") {
            $("#add-industry-name").parent().addClass("has-error");
            return;
        } else {
            $("#add-industry-name").parent().addClass("has-error");
        }
        IndustryManager.add(name, function (iid) {
            if (iid == null) {
                sessionError();
                return;
            }
            $.messager.popup("创建成功！");
            $("#add-industry-modal").modal("hide");
            loadIndustry();
        });
    });

    $("#add-industry-modal").on("hidden.bs.modal", function () {
        $("#add-industry-modal .input-group").removeClass("has-error");
        $("#add-industry-modal input").val("");
    });

});

function loadIndustry() {
    IndustryManager.getAll(function (Industrys) {
        $("#industry-list tbody").mengularClear();

        for (var i in Industrys) {
            var Industry = Industrys[i];
            $("#industry-list tbody").mengular(".industry-list-template", {
                iid: Industry.iid,
                createAt: Industry.createAt.format(DATE_HOUR_MINUTE_SECOND_FORMAT),
                name: Industry.name,
                customers: Industry.customers
            });
        }
    });
}