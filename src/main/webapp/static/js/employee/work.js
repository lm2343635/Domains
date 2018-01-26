var wid = request("wid");

$(document).ready(function () {
    $("#add-reply-content").summernote({
        toolbar: SUMMERNOTE_TOOLBAR_FULL,
        lang: "zh-CN",
        height: 300
    });

    checkEmployeeSession(function () {
        WorkManager.get(wid, function (result) {
            if (!result.session) {
                sessionError();
                return;
            }
            var work = result.data;
            if (work == null) {
                linkError();
                return;
            }

            $("#work-panel .panel-title").fillText({
                title: work.title,
                replys: work.replys
            })
        });
    });
});