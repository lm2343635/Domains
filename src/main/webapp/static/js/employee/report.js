var rid = request("rid");

$(document).ready(function () {
    checkEmployeeSession(function () {
        // Load the report if rid is not empty.
        if (rid != null && rid != "") {

        }
    });

    $("#report-content").summernote({
        toolbar: SUMMERNOTE_TOOLBAR_FULL,
        lang: "zh-CN",
        height: 600
    });

    $("#report-save").click(function () {
        var title = $("#report-title").val();
        var content = $("#report-content").summernote("code");
        if (title == "" || content == "") {
            $.messager.popup("标题和内容不能为空！");
            return;
        }
        $(this).text("提交中...").attr("disabled", "disabled");
        if (rid != null && rid != "") {

        } else {
            ReportManager.add(title, content, function (result) {
                if (!result.session) {
                    sessionError();
                    return;
                }
                if (result.data == null) {
                    $.messager.popup("保存失败，请刷新重试！");
                    return;
                }
                $("#report-save").text("保存报告").removeAttr("disabled");
                $.messager.popup("保存成功！");
                setTimeout(function () {
                    location.href = "reports.html";
                }, 1000);
            });
        }
    });

});