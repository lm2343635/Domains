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

            document.title = work.title
            $("#work-panel .panel-title").fillText({
                title: work.title
            });
            $("#work-info").fillText({
                sponsor: work.sponsor.name,
                executor: work.executor.name
            });
            $("#work-replys").fillText({
                replys: work.replys
            });

            ReplyManager.getByWid(wid, function (result) {
                if (!result.session) {
                    sessionError();
                    return;
                }
                if (result.data == null) {
                    return;
                }
                for (var i in result.data) {
                    var reply = result.data[i];
                    var state = "text-warning";
                    if (reply.employee.eid == work.sponsor.eid) {
                        state = "text-success";
                    } else if (reply.employee.eid == work.executor.eid) {
                        state = "text-danger";
                    }
                    $("#reply-list").mengular(".reply-list-template", {
                        rid: reply.rid,
                        state: state,
                        employee: reply.employee.name,
                        createAt: reply.createAt.format(DATE_HOUR_MINUTE_SECOND_FORMAT),
                        content: reply.content
                    });
                }
            });
        });

    });

    $("#add-reply").click(function () {
        $("html, body").animate({
            scrollTop: $(document).height() - $(window).height()
        }, 300);
    });

    $("#add-reply-submit").click(function () {
        var content = $("#add-reply-content").summernote("code");
        if (content == null || content == "") {
            $.messager.popup("回复不能为空！");
        }
        var length = content.getBytesLength();
        if (length > 1024 * 200) {
            $.messager.popup("回复大小不能超过200KB！当前大小：" + formatByte(length));
            return;
        }
        $.messager.confirm("回复", "确认回复该工作任务吗？", function () {
            ReplyManager.add(wid, content, function (result) {
                if (!result.session) {
                    sessionError();
                    return;
                }
                $.messager.popup("回复成功！");
                setTimeout(function () {
                    location.reload();
                }, 1000);
            });
        });

    });

});