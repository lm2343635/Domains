var sid = request("sid");
var did = location.hash.substring(1);
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
        var cid = $("#add-domain-customer .customer-cid").val();
        var domains = $("#add-domain-domains").val();
        var language = $("#add-domain-language").val();
        var resolution = $("#add-domain-resolution").val();
        var path = $("#add-domain-path").val();
        var remark = $("#add-domain-remark").val();
        var similarity = $("#add-domain-similarity").val();
        var frequency = $("#add-domain-frequency").val();
        var validate = true;
        if (name == "" || name == null) {
            $("#add-domain-name").parent().addClass("has-error");
            validate = false;
        } else {
            $("#add-domain-name").parent().removeClass("has-error");
        }
        if (cid == "" || cid == null) {
            $("#add-domain-customer").parent().addClass("has-error");
            $("#add-domain-customer-select").addClass("btn-danger");
            validate = false;
        } else {
            $("#add-domain-customer").parent().removeClass("has-error");
            $("#add-domain-customer-select").removeClass("btn-danger");
        }
        if (domains == "" || domains == null) {
            $("#add-domain-domains").parent().addClass("has-error");
            validate = false;
        } else {
            $("#add-domain-domains").parent().removeClass("has-error");
        }
        if (language == "" || language == null) {
            $("#add-domain-language").parent().addClass("has-error");
            validate = false;
        } else {
            $("#add-domain-language").parent().removeClass("has-error");
        }
        if (path == "" || path == null) {
            $("#add-domain-path").parent().addClass("has-error");
            validate = false;
        } else {
            $("#add-domain-path").parent().removeClass("has-error");
        }
        if (similarity == "" || similarity == null || !isInteger(similarity) || similarity < 0 || similarity > 100) {
            $("#add-domain-similarity").parent().addClass("has-error");
            validate = false;
        } else {
            $("#add-domain-similarity").parent().removeClass("has-error");
        }
        if (!validate) {
            return;
        }
        if (editingDid == null) {
            DomainManager.add(sid, name, cid, domains, language, resolution, path, remark, frequency, similarity, function (result) {
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
            did = editingDid;
            DomainManager.modify(editingDid, name, cid, domains, language, resolution, path, remark, frequency, similarity, function (result) {
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
        $("#add-domain-customer-select").removeClass("btn-danger");
        $("#add-domain-customer .customer-name").text("拉选择所属客户");
        $("#add-domain-modal input, #add-domain-customer .customer-cid").val("");
        $("#add-domain-customer ul .customer-alternative").remove();
        $("#add-domain-similarity").val(100);
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

    $("#add-domain-customer .customer-keyword").click(function (event) {
        event.stopPropagation();
    });

    $("#add-domain-customer .customer-search").click(function (event) {
        event.stopPropagation();
        var keyword = $("#add-domain-customer .customer-keyword").val();
        if (keyword == null || keyword == "") {
            $.messager.popup("请输入客户名称搜索！");
            return;
        }
        CustomerManager.searchForDomain(keyword, function (result) {
            if (!result.session) {
                sessionError();
                return;
            }
            if (!result.privilege) {
                $.messager.popup("当前用户无权搜索已开发客户！");
                return;
            }
            $("#add-domain-customer ul .customer-alternative").remove();

            for (var i in result.data) {
                var customer = result.data[i];
                $("#add-domain-customer ul").mengular(".customer-list-template", {
                    cid: customer.cid,
                    name: customer.name,
                    state: "customer-alternative"
                });

                $("#" + customer.cid).click(function (event) {
                    var cid = $(this).mengularId();
                    $("#add-domain-customer .customer-cid").val(cid);
                    $("#add-domain-customer .customer-name").text($("#" + cid + " a").text());
                });
            }
        });
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
                highlight: domain.highlight ? "highlight" : "",
                frequency: domain.frequency * 10,
                similarity: domain.similarity,
                checkAt: domain.checkAt.format(DATE_HOUR_MINUTE_SECOND_FORMAT),
                cid: domain.customer.cid,
                cname: domain.customer.name
            });

            // Set grab buttons of ungrabbed sites to muted.
            if (!domain.grabbed) {
                $("#" + domain.did + " .domain-list-grab").addClass("text-muted");
                $("#" + domain.did + " .domain-list-monitoring").remove();
            } else {
                $("#" + domain.did + " .domain-list-monitoring").bootstrapSwitch({
                    state: domain.monitoring
                }).on("switchChange.bootstrapSwitch", function (event, state) {
                    var did = $(this).mengularId();
                    DomainManager.setMonitoring(did, state, function(result) {
                        if (!result.session) {
                            sessionError();
                            return;
                        }
                        if (!result.privilege) {
                            $.messager.popup("当前用户无权更改该域名！");
                            return;
                        }

                    });
                });
            }

            if (!domain.alert) {
                $("#" + domain.did + " .domain-list-alert").remove();
            } else {
                $("#" + domain.did + " .domain-list-alert").click(function () {
                    var did = $(this).mengularId();
                    var name = $("#" + did + " .domain-list-name").text();
                    $.messager.confirm("取消报警", "确认取消域名" + name + "的报警吗？", function () {
                        DomainManager.cancelAlert(did, function (result) {
                            if (!result.session) {
                                sessionError();
                                return;
                            }
                            if (!result.privilege) {
                                $.messager.popup("当前用户无权更改该域名！");
                                return;
                            }
                            $.messager.popup("已取消报警！");
                            $("#" + did + " .domain-list-alert").remove();
                        });
                    });
                });

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
                        "add-domain-remark": domain.remark,
                        "add-domain-frequency": domain.frequency,
                        "add-domain-similarity": domain.similarity
                    });
                    $("#add-domain-customer .customer-cid").val(domain.customer.cid);
                    $("#add-domain-customer .customer-name").text(domain.customer.name);
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

        if (did != null || did != "") {
            $('html, body').animate({
                scrollTop: $("#" + did).offset().top
            }, 500);
        }
    });
}