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

});