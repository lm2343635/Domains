var bid = request("bid");

$(document).ready(function () {

    checkEmployeeSession(function () {
        if (bid != null) {
            $("#bulletin-head").text("编辑公告");
            BulletinManager.get(bid, function (result) {
                if (!result.session) {
                    sessionError();
                    return;
                }
                if (result.data == null) {
                    $.messager.popup("链接错误，请关闭重试！");
                    return;
                }
                var bulletin = result.data;
                $("#bulletin-head").text("编辑公告：" + bulletin.employee.name + "创建于" + bulletin.createAt.format(DATE_HOUR_MINUTE_SECOND_FORMAT));
                $("#bulletin-content").summernote("code", bulletin.content);
            });
        }
    });

    $("#bulletin-content").summernote({
        toolbar: SUMMERNOTE_TOOLBAR_FULL,
        lang: "zh-CN",
        height: 600
    });

    $("#bulletin-submit").click(function () {
        var _this = $(this);
        var content = $("#bulletin-content").summernote("code");
        if (content == null || content == "<p><br></p>") {
            $.messager.popup("公告内容不能为空！");
            return;
        }
        var length = content.getBytesLength();
        if (length > 1024 * 100) {
            $.messager.popup("文档大小不能超过100KB！当前大小：" + formatByte(length));
            return;
        }
        _this.text("正在提交...").attr("disabled", "disabled");
        if (bid == null || bid == "") {

            BulletinManager.add(content, function (result) {
                if (!result.session) {
                    sessionError();
                    return;
                }
                _this.text("保存").removeAttr("disabled");
                if (result.data == null) {
                    $.messager.popup("创建失败，请重试！");
                    return;
                }
                $.messager.popup("创建成功！");
                setTimeout(function () {
                    history.back(-1);
                }, 1000);
            });
        } else {
            BulletinManager.edit(bid, content, function (result) {
                if (!result.session) {
                    sessionError();
                    return;
                }
                if (!result.privilege) {
                    $.messager.popup("当前用户无权限编辑改公告！");
                    return;
                }
                _this.text("保存").removeAttr("disabled");
                if (result.data == false) {
                    $.messager.popup("保存失败，请重试！");
                    return;
                }
                $.messager.popup("保存成功！");
                setTimeout(function () {
                    history.back(-1);
                }, 1000);
            });
        }
    });

});