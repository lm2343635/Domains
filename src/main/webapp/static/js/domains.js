var sid = request("sid");
var editingDid = null;

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
        if (!validate) {
            return;
        }
        if (editingDid == null) {
            DomainManager.add(sid, name, domains, language, resolution, path, remark, function (did) {
                if (did == null) {
                    location.href = "session.html";
                    return;
                }
                $("#add-domain-modal").modal("hide");
                $.messager.popup("新建成功！");
                loadDomains();
            });
        } else {
            DomainManager.modify(editingDid, name, domains, language, resolution, path, remark, function (success) {
                if (!success) {
                    location.href = "session.html";
                    return;
                }
                $("#add-domain-modal").modal("hide");
                $.messager.popup("修改成功！");
                loadDomains();
            });
        }
    });

    $("#add-domain-modal").on("hidden.bs.modal", function () {
        $("#add-domain-modal .input-group").removeClass("has-error");
        $("#add-domain-modal input").val("");
        editingDid = null;
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
            
            $("#" + domain.did + " .domain-list-edit").click(function () {
                editingDid = $(this).mengularId();
                DomainManager.get(editingDid, function (domain) {
                    if (domain == null) {
                        location.href = "session.html";
                        return;
                    }
                    fillValue({
                        "add-domain-name": domain.name,
                        "add-domain-domains": domain.domains,
                        "add-domain-language": domain.language,
                        "add-domain-resolution": domain.resolution,
                        "add-domain-path": domain.path,
                        "add-domain-remark": domain.remark
                    });
                    $("#add-domain-modal").modal("show");
                })
            });

            $("#" + domain.did + " .domain-list-remove").click(function () {
                var did = $(this).mengularId();
                var name = $("#" + did + " .domain-list-name").text();
                $.messager.confirm("删除域名", "确认删除域名" + name + "吗？", function () {
                    DomainManager.remove(did, function (success) {
                        if (!success) {
                            location.href = "session.html";
                            return;
                        }
                        $("#" + did).remove();
                        $.messager.popup("删除成功！");
                    });
                });
            });
        }
    });
}