var sid = request("sid");

$(document).ready(function () {

    checkAdminSession(function () {
        ServerManager.get(sid, function (server) {
            if (server == null) {
                location.href = "session.html";
                return;
            }
            $("#domain-panel .panel-title").fillText({
                name: server.name,
                address: server.address,
                domains: server.domains
            })

            loadDomains();
        });
    });

    $("#add-domain-submit").click(function () {
        var name = $("#add-domain-name").val();
        var domains = $("#add-domain-domains").val();
        var language = $("#add-domain-language").val();
        var resolution = $("#add-domain-resolution").val();
        var path = $("#add-domain-path").val();
        var remark = $("#add-domain-remark").val();
        var validate = true;
        if (name == "" || name == null) {
            $("#add-domain-name").parent().addClass("has-error");
            validate = false;
        } else {
            $("#add-domain-name").parent().removeClass("has-error");
        }
        if (domains == "" || domains == null) {
            $("#add-domain-domains").parent().addClass("has-error");
            validate = false;
        } else {
            $("#add-domain-domains").parent().removeClass("has-error");
        }
        if (path == "" || path == null) {
            $("#add-domain-path").parent().addClass("has-error");
            validate = false;
        } else {
            $("#add-domain-path").parent().removeClass("has-error");
        }
        if (validate) {
            DomainManager.add(sid, name, domains, language, resolution, path, remark, function (did) {
                if (did == null) {
                    location.href = "session.html";
                    return;
                }
                $("#add-domain-modal").modal("hide");
                $.messager.popup("新建成功！");
                loadDomains();
            });
        }
    });

    $("#add-domain-modal").on("hidden.bs.modal", function () {
        $("#add-domain-modal .input-group").removeClass("has-error");
        $("#add-domain-modal input").val("");
    });

});

function loadDomains() {
    DomainManager.getBySid(sid, function (domains) {
        if (domains == null) {
            location.href = "session.html";
            return;
        }
        $("#domain-list tbody").mengularClear();
        for (var i in domains) {
            var domain = domains[i];
            $("#domain-list tbody").mengular(".domain-list-template", {
                did: domain.did,
                createAt: domain.createAt.format(DATE_HOUR_MINUTE_FORMAT),
                updateAt: domain.updateAt.format(DATE_HOUR_MINUTE_FORMAT),
                name: domain.name,
                domains: domain.domains,
                language: domain.language,
                resolution: domain.resolution,
                path: domain.path,
                remark: domain.remark
            });
        }
    });
}