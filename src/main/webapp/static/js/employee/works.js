var active = request("active");
if (active != 0 && active != 1) {
    active = 0;
}

$(document).ready(function () {

    $("#work-panel .panel-heading .nav li").eq(active).addClass("active");

    $("#search-work-start, #search-work-end").datetimepicker({
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        startView: 2,
        minView: 2,
        language: "zh-CN"
    });

    checkEmployeeSession(function () {

        EmployeeManager.getAll(function (result) {
            if (!result.session) {
                sessionError();
                return;
            }
            for (var i in result.data) {
                var employee = result.data[i];
                $("<option>").val(employee.eid).text(employee.name).appendTo("#add-work-executor");
            }
        });

    });

    $("#add-work-submit").click(function () {
        var title = $("#add-work-title").val();
        var executor = $("#add-work-executor").val();
        var validate = true;
        if (title == "" || title == null) {
            $("#add-work-title").parent().addClass("has-error");
            validate = false;
        } else {
            $("#add-work-title").parent().removeClass("has-error");
        }
        if (executor == "" || executor == null) {
            $("#add-work-executor").parent().addClass("has-error");
            validate = false;
        } else {
            $("#add-work-executor").parent().removeClass("has-error");
        }
        if (!validate) {
            return;
        }
        WorkManager.add(title, executor, function (result) {
            if (!result.session) {
                sessionError();
                return;
            }
            if (result.data == null) {
                $.messager.popup("创建失败，请重试！");
                return;
            }
            $("#add-work-modal").modal("hide");
            $.messager.confirm("创建成功", "任务已创建成功，是否立即添加回复？", function () {
                location.href = "work.html?wid=" + result.data;
            });
        });
    });
    
    $("#add-work-modal").on("hidden.bs.modal", function () {
        $("#add-work-modal input").val("");
    });

});