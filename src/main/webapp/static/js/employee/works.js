var pageSize = 20;

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
                var option = $("<option>").val(employee.eid).text(employee.name);
                $("#add-work-executor, #search-work-sponsor, #search-work-executor").append(option);
            }
        });

        searchWorks(active == 0, null, null, null, null, null, 1);
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

            searchWorks(true, null, null, null, null, null, 1);
            $("#work-panel .panel-heading .nav li").removeClass("active");
            $("#work-panel .panel-heading .nav li").eq(0).addClass("active");

            $("#add-work-modal").modal("hide");

            $.messager.confirm("创建成功", "任务已创建成功，是否立即添加回复？", function () {
                location.href = "work.html?wid=" + result.data;
            });
        });
    });
    
    $("#add-work-modal").on("hidden.bs.modal", function () {
        $("#add-work-modal input").val("");
    });

    $("#search-submit").click(function () {
        var title = $("#search-work-title").val();
        var sponsor = $("#search-work-sponsor").val();
        var executor = $("#search-work-executor").val();
        var start = $("#search-work-start").val();
        var end = $("#search-work-end").val();
        searchWorks(active == 0, title, sponsor, executor, start, end, 1);
    });

    $("#search-reset").click(function () {
        $("#search-panel input, #search-panel select").val("");
        $("#search-submit").click();
    });

});

function searchWorks(active, title, sponsor, executor, start, end, page) {
    WorkManager.getSearchCount(active, title, sponsor, executor, start, end, function (result) {
        if (!result.session) {
            sessionError();
            return;
        }

        $("#page-size").text(pageSize);

        var searchCount = result.data;
        $("#page-count").text(searchCount);
        $("#page-nav ul").empty();
        for (var i = 1; i < Math.ceil(searchCount / pageSize + 1); i++) {
            var li = $("<li>").append($("<a>").attr("href", "javascript:void(0)").text(i));
            if (page == i) {
                li.addClass("active");
            }
            $("#page-nav ul").append(li);
        }

        $("#page-nav ul li").each(function (index) {
            $(this).click(function () {
                searchWorks(active, title, sponsor, executor, start, end, index + 1);
                $("html, body").animate({
                    scrollTop: 0
                }, 300);
            });
        });

        $("#expiration-money-count").text(result.data.moneyCount);
    });

    WorkManager.search(active, title, sponsor, executor, start, end, page, pageSize, function (result) {
        if (!result.session) {
            sessionError();
            return;
        }

        $("#work-list").mengularClear();
        for (var i in result.data) {
            var work = result.data[i];
            $("#work-list").mengular(".work-list-template", {
                wid: work.wid,
                title: work.title,
                sponsor: work.sponsor.name,
                executor: work.executor.name,
                createAt: work.createAt.format(DATE_HOUR_MINUTE_SECOND_FORMAT),
                replys: work.replys
            })
        }
    });
}