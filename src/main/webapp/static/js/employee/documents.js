$(document).ready(function () {

    checkEmployeeSession(function () {
        loadPublicDocuments();
    });

    $("#upload-document").fileupload({
        autoUpload: true,
        url: "/document/upload/public",
        dataType: "json",
        start: function (e) {
            $("#document-upload-modal").modal("show");
        },
        done: function (e, data) {
            if (data.result.status == 801) {
                sessionError();
                return;
            }
            if (data.result.status == 803) {
                $.messager.popup("刷新页面重试！");
                return;
            }
            loadPublicDocuments();
            setTimeout(function () {
                $("#document-upload-modal").modal("hide");
            }, 1000);
        },
        progressall: function (e, data) {
            var progress = parseInt(data.loaded / data.total * 100, 10);
            $("#document-upload-progress .progress-bar").css("width", progress + "%");
            $("#document-upload-progress .progress-bar").text(progress + "%");
        }
    });

});

function loadPublicDocuments() {
    DocumentManager.getPublic(null, function (result) {
        if (!result.session) {
            sessionError();
            return;
        }
        if (!result.privilege) {
            return;
        }
        $("#document-list").mengularClear();
        for (var i in result.data) {
            var document = result.data[i];

            $("#document-list").mengular(".document-list-template", {
                did: document.did,
                employee: document.employee,
                uploadAt: document.uploadAt.format(DATE_HOUR_MINUTE_SECOND_FORMAT),
                filename: document.filename,
                size: document.size
            });

            $("#" + document.did + " .document-list-remove").click(function () {
                var did = $(this).mengularId();
                var filename = $("#" + did + " .document-list-filename").text();
                $.messager.confirm("删除附件文件", "确定要删除" + filename + "吗？", function () {
                    DocumentManager.remove(did, function (result) {
                        if (!result.session) {
                            sessionError();
                            return;
                        }
                        if (!result.privilege) {
                            $.messager.popup("当前用户无权删除改附件！");
                            return;
                        }
                        $.messager.popup("删除成功！");
                        $("#" + did).remove();
                    });
                });
            });
        }
    });
}