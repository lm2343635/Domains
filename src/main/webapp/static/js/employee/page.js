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

$(document).ready(function () {

    checkEmployeeSession(function (employee) {
        DomainManager.getWithGrabbedPgae(did, function (result) {
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


            $("#code").text(tidy_html5(result.data.page, options));

        });
    });

});