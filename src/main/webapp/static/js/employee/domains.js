var sid = request("sid");
var editingDid = null;

$(document).ready(function () {

    checkEmployeeSession(function (employee) {
        ServerManager.get(sid, function (result) {
            if (!result.session) {
                sessionError();
                return;
            }
            if (!result.privilege) {
                return;
            }
            var server = result.data;
            document.title = server.name + document.title;

            $("#domain-panel .panel-title").fillText({
                name: server.name,
                address: server.address,
                domains: server.domains
            })

            loadDomains();
        });

        ServerManager.getAll(function (result) {
            if (!result.session) {
                sessionError();
                return;
            }
            if (!result.privilege) {
                return;
            }
            for (var i in result.data) {
                var server = result.data[i];
                if (server.sid != sid) {
                    $("<option>").val(server.sid).text(server.name).appendTo("#transfer-domain-server");
                }

            }
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
            DomainManager.add(sid, name, domains, language, resolution, path, remark, function (result) {
                if (!result.session) {
                    sessionError();
                    return;
                }
                if (!result.privilege) {
                    $.messager.popup("当前用户无权限创建域名！");
                    return;
                }
                $.messager.popup(result.data != null ? "新建成功！" : "创建失败，请重试！");
                $("#add-domain-modal").modal("hide");
                loadDomains();
            });
        } else {
            DomainManager.modify(editingDid, name, domains, language, resolution, path, remark, function (result) {
                if (!result.session) {
                    sessionError();
                    return;
                }
                if (!result.privilege) {
                    $.messager.popup("当前用户无权更改建域名！");
                    return;
                }
                $("#add-domain-modal").modal("hide");
                $.messager.popup(result.data ? "修改成功！" : "修改失败，请重试！");
                loadDomains();
            });
        }
    });

    $("#add-domain-modal").on("hidden.bs.modal", function () {
        $("#add-domain-modal .input-group").removeClass("has-error");
        $("#add-domain-modal input").val("");
        editingDid = null;
    });
    
    $("#transfer-domain-submit").click(function () {
        var sid = $("#transfer-domain-server").val();
        if (sid == null || sid == "") {
            $.messager.popup("请选择服务器！");
            return;
        }
        DomainManager.transfer(editingDid, sid, function (result) {
            if (!result.session) {
                sessionError();
                return;
            }
            if (!result.privilege) {
                $.messager.popup("当前用户无权更转移域名！");
                return;
            }
            if (result.data) {
                $("#" + editingDid).remove();
                $.messager.popup("转移成功！");
            } else {
                $.messager.popup("转移失败，请重试!");
            }
            $("#transfer-domain-modal").modal("hide");
            editingDid = null;
        });
    });

    $("#transfer-domain-modal").on("hidden.bs.modal", function () {
        editingDid = null;
    });

});

function loadDomains() {
    DomainManager.getBySid(sid, function (result) {
        if (!result.session) {
            sessionError();
            return;
        }
        if (!result.privilege) {
            $.messager.popup("当前用户无权限查看域名！");
            return;
        }

        $("#domain-list").mengularClear();
        for (var i in result.data) {
            var domain = result.data[i];
            var sites = domain.domains.split(",");
            var links = ""
            for (var j in sites) {
                links += "<a href='http://" + sites[j] + "' target='_blank'>" + sites[j] + "</a>";
                if (j != sites.length - 1) {
                    links += ", ";
                }
            }

            $("#domain-list").mengular(".domain-list-template", {
                did: domain.did,
                createAt: domain.createAt.format(DATE_HOUR_MINUTE_FORMAT),
                updateAt: domain.updateAt.format(DATE_HOUR_MINUTE_FORMAT),
                name: domain.name,
                domains: links,
                language: domain.language,
                resolution: domain.resolution,
                path: domain.path,
                remark: domain.remark,
                highlight: domain.highlight ? "highlight" : ""
            });

            // Set grab buttons of ungrabbed sites to muted.
            if (!domain.grabbed) {
                $("#" + domain.did + " .domain-list-grab").addClass("text-muted");
            }
            
            $("#" + domain.did + " .domain-list-edit").click(function () {
                editingDid = $(this).mengularId();
                DomainManager.get(editingDid, function (result) {
                    if (!result.session) {
                        sessionError();
                        return;
                    }
                    if (!result.privilege) {
                        return;
                    }
                    var domain = result.data;
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
            
            $("#" + domain.did + " .domain-list-transfer").click(function () {
                editingDid = $(this).mengularId();
                $("#transfer-domain-modal").modal("show");
            });

            $("#" + domain.did + " .domain-list-highlight").bootstrapSwitch({
                state: domain.highlight
            }).on("switchChange.bootstrapSwitch", function (event, state) {
                var did = $(this).mengularId();
                DomainManager.setHighlight(did, state, function(result) {
                    if (!result.session) {
                        sessionError();
                        return;
                    }
                    if (!result.privilege) {
                        $.messager.popup("当前用户无权更改该域名！");
                        return;
                    }
                    if (state) {
                        $("#" + did).addClass("highlight");
                    } else {
                        $("#" + did).removeClass("highlight");
                    }
                });
            });

            $("#" + domain.did + " .domain-list-monitoring").bootstrapSwitch({
                state: domain.highlight
            }).on("switchChange.bootstrapSwitch", function (event, state) {
                var did = $(this).mengularId();
                DomainManager.setHighlight(did, state, function(result) {
                    if (!result.session) {
                        sessionError();
                        return;
                    }
                    if (!result.privilege) {
                        $.messager.popup("当前用户无权更改建域名！");
                        return;
                    }

                });
            });

            $("#" + domain.did + " .domain-list-remove").click(function () {
                var did = $(this).mengularId();
                var name = $("#" + did + " .domain-list-name").text();
                $.messager.confirm("删除域名", "确认删除域名" + name + "吗？", function () {
                    DomainManager.remove(did, function (result) {
                        if (!result.session) {
                            sessionError();
                            return;
                        }
                        if (!result.privilege) {
                            $.messager.popup("当前用户无权限删除域名！");
                            return;
                        }
                        if (result.data) {
                            $("#" + did).remove();
                            $.messager.popup("删除成功！");
                        } else {
                            $.messager.popup("删除失败，请重试！");
                        }
                    });
                });
            });
        }
    });
}