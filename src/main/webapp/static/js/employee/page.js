var did = request("did");
var options = {
    "indent":"auto",
    "indent-spaces":4,
    "wrap":80,
    "markup":true,
    "output-xml":false,
    "numeric-entities":true,
    "quote-marks":true,
    "quote-nbsp":false,
    "show-body-only":false,
    "quote-ampersand":false,
    "break-before-br":true,
    "uppercase-tags":false,
    "uppercase-attributes":false,
    "drop-font-tags":true,
    "tidy-mark":false
};

var page = null;

$(document).ready(function () {

    checkEmployeeSession(function (employee) {
        $("#charset-modal").modal("show");


    });

    $("#charset-submit").click(function () {
        $("#charset-modal").modal("hide");
        var charset = $("#charset").val();
        DomainManager.getWithGrabbedPgae(did, charset, function (result) {
            if (!result.session) {
                sessionError();
                return;
            }
            if (!result.privilege) {
                alert("当前用户无权限查看该域名！");
                return;
            }
            var domain = result.data.domain;
            document.title = document.title + domain.name;

            $("#view").attr("src", "http://" + domain.domains.split(",")[0]);

            page = result.data.page;
            $("#code pre").text(tidy_html5(page, options));
        });
    });

    $("#save-code").click(function () {
        if (page == null) {
            $.messager.popup("请等待页面完全加载！");
            return;
        }
        var charset = $("#charset").val();
        $.messager.confirm("保存为标准页面", "确认保存该页面为该网站的标准页面用于差异检测吗？", function () {
            DomainManager.savePage(did, charset, page, function (result) {
                if (!result.session) {
                    sessionError();
                    return;
                }
                if (!result.privilege) {
                    $.messager.popup("当前账户无权限保存标准页面！");
                    return;
                }
                $.messager.popup("保存成功！")
                setTimeout(function () {
                    self.close();
                }, 1000);
            });
        });
    });

});
