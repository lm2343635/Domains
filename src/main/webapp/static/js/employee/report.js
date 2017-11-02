var rid = request("rid");

$(document).ready(function () {
    checkEmployeeSession(function (employee) {
        // Load the report if rid is not empty.
        if (rid != null && rid != "") {
            ReportManager.get(rid, function (result) {
                if (!result.session) {
                    sessionError();
                    return;
                }
                var report = result.data;
                $("#report-info").show().fillText({
                    createAt: report.createAt.format(DATE_HOUR_MINUTE_SECOND_FORMAT),
                    updateAt: report.updateAt.format(DATE_HOUR_MINUTE_SECOND_FORMAT),
                    employee: report.employee.name
                });
                $("#report-title").val(report.title);
                $("#report-content").html(report.content);
                if (employee.eid == report.employee.eid) {
                    $("#report-edit, #report-remove").show();
                }
            });
        } else {
            $("#report-save").show();
            editReport();
        }
    });

    $("#report-edit").click(function () {
        $("#report-edit").hide();
        $("#report-save").show();
        editReport();
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
            ReportManager.edit(rid, title, content, function (result) {
                if (!result.session) {
                    sessionError();
                    return;
                }
                if (!result.privilege) {
                    $.messager.popup("只有创建者本人有权修改该工作报告！");
                    return;
                }
                if (result.data == null) {
                    $.messager.popup("保存失败，请刷新重试！");
                    return;
                }
                $("#report-save").text("保存报告").removeAttr("disabled");
                $.messager.popup("保存成功！");
            });
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

    $("#report-remove").click(function () {
        $.messager.confirm("删除工作报告", "确认删除该工作报告吗？", function () {
            ReportManager.remove(rid, function (result) {
                if (!result.session) {
                    sessionError();
                    return;
                }
                if (!result.privilege) {
                    $.messager.popup("只有创建者本人有权删除该工作报告！");
                    return;
                }
                if (!result.data) {
                    $.messager.popup("删除失败，请刷新重试！");
                    return;
                }
                $.messager.popup("删除成功！");
                setTimeout(function () {
                    window.close();
                }, 1000);
            });
        });
    });

});

function editReport() {
    $("#report-title").removeAttr("disabled");
    var html = $("#report-content").html();
    $("#report-content").summernote({
        toolbar: SUMMERNOTE_TOOLBAR_FULL,
        lang: "zh-CN",
        height: 600
    }).summernote("code", html);
}