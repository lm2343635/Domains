var wid = request("wid");

$(document).ready(function () {
    $("#add-reply-content").summernote({
        toolbar: SUMMERNOTE_TOOLBAR_FULL,
        lang: "zh-CN",
        height: 300
    });

    checkEmployeeSession(function (employee) {
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
                active: work.active ? "待完成" : "已完成",
                title: work.title
            });
            $("#work-info").fillText({
                sponsor: work.sponsor.name,
                executor: work.executor.name
            });
            $("#work-replys").fillText({
                replys: work.replys
            });
            $("#work-panel").addClass(work.active ? "panel-success" : "panel-danger");

            if (employee.eid == work.sponsor.eid && work.active) {
                $("#close-work").show();
            } else {
                $("#close-work").remove();
            }

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
        if (length > 1024 * 300) {
            $.messager.popup("回复大小不能超过300KB！当前大小：" + formatByte(length));
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

    $("#close-work").click(function () {
        $.messager.confirm("结束工作任务", "结束工作任务后，该任务将转入已完成状态，确认结束任务吗？", function () {
            WorkManager.close(wid, function (result) {
                if (!result.session) {
                    sessionError();
                    return;
                }
                if (!result.privilege) {
                    $.messager.popup("当前用户无权限结束该任务！仅该任务发起者可结束该任务。");
                    return;
                }
                $.messager.popup("该任务已结束！");
                setTimeout(function () {
                    location.reload();
                }, 1000);
            });
        });
    });

});