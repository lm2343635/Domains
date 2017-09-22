var names = [];

$(document).ready(function () {

    checkAdminSession(function () {
        ServerManager.getAll(function (servers) {
            if (servers == null) {
                location.href = "../../../admin/session.html";
                return;
            }
            for (var i in servers) {
                var server = servers[i];
                names[server.sid] = server.name;
            }
        });

        loadHighlightDomains();
    });

});

function loadHighlightDomains() {
    DomainManager.getHightlightDomains(function (domains) {

        if (domains == null) {
            location.href = "../../../admin/session.html";
            return;
        }
        $("#domain-list tbody").mengularClear();
        for (var i in domains) {
            var domain = domains[i];
            var sites = domain.domains.split(",");
            var links = ""
            for (var j in sites) {
                links += "<a href='http://" + sites[j] + "' target='_blank'>" + sites[j] + "</a>";
                if (j != sites.length - 1) {
                    links += "<br>";
                }
            }

            $("#domain-list tbody").mengular(".domain-list-template", {
                did: domain.did,
                createAt: domain.createAt.format(DATE_HOUR_MINUTE_FORMAT),
                updateAt: domain.updateAt.format(DATE_HOUR_MINUTE_FORMAT),
                name: domain.name,
                domains: links,
                language: domain.language,
                resolution: domain.resolution,
                path: domain.path,
                remark: domain.remark,
                sid: domain.sid,
                server: names[domain.sid]
            });

            $("#" + domain.did + " .domain-list-remove").click(function () {
                var did = $(this).mengularId();
                $.messager.confirm("移除待处理域名", "确认从待处理域名列表中移除该域名吗？", function () {
                    DomainManager.setHighlight(did, false, function(success) {
                        if (!success) {
                            location.href = "../../../admin/session.html";
                            return;
                        }
                        $("#" + did).remove();
                    });
                });
            });
        }
    });
}

