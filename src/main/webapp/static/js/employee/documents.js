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

}