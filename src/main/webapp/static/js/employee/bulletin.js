var bid = request("bid");

$(document).ready(function () {

    checkEmployeeSession(function () {

    });

    $("#bulletin-content").summernote({
        toolbar: SUMMERNOTE_TOOLBAR_FULL,
        lang: "zh-CN",
        height: 600
    });

    $("#bulletin-submit").click(function () {
        var _this = $(this);
        if (bid == null || bid == "") {
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

        }
    });

});