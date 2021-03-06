var pageSize = 20;
var tid = request("tid");

$(document).ready(function () {

    checkEmployeeSession(function () {
        TypeManager.getByCategory(TypeCategoryReport, function(result) {
            if (!result.session) {
                return;
            }
            for (var i in result.data) {
                var type = result.data[i];
                $("#report-types").mengular(".report-type-template", {
                    tid: type.tid,
                    name: type.name
                });
            }
            $("#report-types").mengularClearTemplate();

            if (tid == null || tid == "") {
                tid = result.data[0].tid;
            }
            $("#" + tid).addClass("active");

            searchReports(null, null, null, 1);
        });
    });

    $("#search-report-start, #search-report-end").datetimepicker({
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        startView: 2,
        minView: 2,
        language: "zh-CN"
    });

    $("#search-submit").click(function () {
        var title = $("#search-report-title").val();
        var start = $("#search-report-start").val();
        var end = $("#search-report-end").val();
        searchReports(title, start, end, 1);
    });

    $("#search-reset").click(function () {
        $("#search-panel input, #search-panel select").val("");
        $("#search-submit").click();
    });

});

function searchReports(title, start, end, page) {
    ReportManager.getSearchCount(tid, title, start, end, function (result) {
        if (!result.session) {
            sessionError();
            return;
        }

        $("#page-size").text(pageSize);

        var count = result.data;
        $("#page-count").text(count);
        $("#page-nav ul").empty();
        for (var i = 1; i < Math.ceil(count / pageSize + 1); i++) {
            var li = $("<li>").append($("<a>").attr("href", "javascript:void(0)").text(i));
            if (page == i) {
                li.addClass("active");
            }
            $("#page-nav ul").append(li);
        }

        $("#page-nav ul li").each(function (index) {
            $(this).click(function () {
                searchReports(title, start, end, index + 1);
                $("html, body").animate({
                    scrollTop: 0
                }, 300);
            });
        });
    });

    ReportManager.search(tid, title, start, end, page, pageSize, function (result) {
        if (!result.session) {
            sessionError();
            return;
        }
        $("#report-list").mengularClear();
        for (var i in result.data) {
            var report = result.data[i];
            $("#report-list").mengular(".report-list-template", {
                rid: report.rid,
                createAt: report.createAt.format(DATE_HOUR_MINUTE_SECOND_FORMAT),
                updateAt: report.updateAt.format(DATE_HOUR_MINUTE_SECOND_FORMAT),
                employee: report.employee.name,
                title: report.title
            });
        }
    });
}