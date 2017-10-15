var cid = request("cid");
var state;

var assinableEmployees;
var editingLid = null;

$(document).ready(function () {

    checkEmployeeSession(function (employee) {
        CustomerManager.get(cid, function (result) {
            if (!result.session) {
                sessionError();
                return;
            }
            if (!result.privilege) {
                $.messager.popup("该账户无权限查看该客户的详细信息！");
                $("#customer-panel .panel-body").remove();
                $("#customer-panel .panel-footer").remove();
                return;
            }

            var customer = result.data;
            state = customer.state;

            $("#customer-panel .panel-heading").fillText({
                state: CustomerStateNames[state],
                name: customer.name
            });

            fillValue({
                "customer-name": customer.name,
                "customer-capital": customer.capital,
                "customer-contact": customer.contact,
                "customer-register": customer.register.name + "（" + customer.register.role.name + "）",
                "customer-remark": customer.remark
            });

            AreaManager.getAll(function (areas) {
                for (var i in areas) {
                    var area = areas[i];
                    $("<option>").val(area.aid).text(area.name).appendTo("#customer-area");
                }
                $("#customer-area").val(customer.area.aid);
            });

            IndustryManager.getAll(function (industries) {
                for (var i in industries) {
                    var industry = industries[i];
                    $("<option>").val(industry.iid).text(industry.name).appendTo("#customer-industry");
                }
                $("#customer-industry").val(customer.industry.iid);
            });

            if (state == CustomerStateDeveloped) {

                fillValue({
                    "customer-money": customer.money == 0 ? "" : customer.money,
                    "customer-items": customer.items
                })

                $("#customer-expireAt").datetimepicker({
                    format: "yyyy-mm-dd",
                    autoclose: true,
                    todayBtn: true,
                    startView: 2,
                    minView: 2,
                    language: "zh-CN"
                }).val(customer.expireAt == null ? "" : customer.expireAt.format(YEAR_MONTH_DATE_FORMAT));

                $("#customer-document").summernote({
                    toolbar: SUMMERNOTE_TOOLBAR_FULL,
                    lang: "zh-CN",
                    height: 400
                }).summernote("code", customer.document);

                if (customer.document != "" && customer.document != null) {
                    $("#customer-document-size").text(customer.document.getBytesLengthString());
                }

                EmployeeManager.getDevelopedAssinableEmployees(cid, function (result) {
                    if (!result.session) {
                        sessionError();
                        return;
                    }
                    if (!result.privilege) {
                        return;
                    }
                    assinableEmployees = result.data;
                    for (var i in assinableEmployees) {
                        var employee = assinableEmployees[i];
                        $("<option>").val(employee.eid).text(employee.name + "（" + employee.role.name + "）")
                            .appendTo("#add-manager-eid");
                    }
                });

                EmployeeManager.assignForCustomer(cid, function (result) {
                    if (!result.session) {
                        sessionError();
                        return;
                    }
                    if (!result.privilege) {
                        return;
                    }
                    var assign = result.data;

                    if (!assign.r) {
                        $("#add-manager-r option[value=true]").remove();
                    }
                    if (!assign.w) {
                        $("#add-manager-w option[value=true]").remove();
                        $("#customer-edit").remove();
                    }
                    if (!assign.d) {
                        $("#add-manager-d option[value=true]").remove();
                        $("#customer-remove").remove();
                    }
                    if (!assign.assign) {
                        $("#customer-managers-add").remove();
                        $("#add-manager-modal").remove();
                    }
                });
                
            } else {
                $("#customer-panel .customer-developed").remove();
            }

            if (state != CustomerStateUndeveloped || employee.role.develop == RolePrevilgeNone) {
                $("#customer-develop").remove();
            }

            if (state != CustomerStateDeveloping || employee.role.finish == RolePrevilgeNone) {
                $("#customer-finish").remove();
            } else {
                EmployeeManager.getDevelopingAssignableEmployees(function (result) {
                    if (!result.session) {
                        sessionError();
                        return;
                    }
                    if (!result.privilege) {
                        return;
                    }
                    for (var i in result.data) {
                        var employee = result.data[i];
                        $("<option>").val(employee.eid).text(employee.name + "（" + employee.role.name + "）")
                            .appendTo("#customer-finish-manager");
                    }
                });
            }

            // Show all managers.
            assinableEmployees = customer.managers;
            for (var i in assinableEmployees) {
                addManager(assinableEmployees[i]);
            }

            // Show all logs.
            loadLogs();

        });
    });
    
    $("#customer-develop").click(function () {
        $.messager.confirm("申请开发客户", "申请开发客户后该账户将自动成为该客户的负责人。", function () {
            CustomerManager.develop(cid, function (result) {
                if (!result.session) {
                    sessionError();
                    return;
                }
                if (!result.privilege) {
                    $.messager.popup("该账户无权申请开发客户！");
                    return;
                }
                if (!result.data) {
                    $.messager.popup("该客户已被开发，请勿重复操作！");
                    return;
                }
                $.messager.popup("申请开发成功！");
                setTimeout(function () {
                    location.reload();
                }, 1000);
            })
        });
    });
    
    $("#customer-edit").click(function () {
        var name = $("#customer-name").val();
        var capital = $("#customer-capital").val();
        var contact = $("#customer-contact").val();
        var aid = $("#customer-area").val();
        var iid = $("#customer-industry").val();
        var remark = $("#customer-remark").val();
        var validate = true;
        if (name == "" || name == null) {
            $("#customer-name").parent().addClass("has-error");
            validate = false;
        } else {
            $("#customer-name").parent().removeClass("has-error");
        }
        if (capital == "" || capital == null || !isInteger(capital)) {
            $("#customer-capital").parent().addClass("has-error");
            validate = false;
        } else {
            $("#customer-capital").parent().removeClass("has-error");
        }
        if (contact == "" || contact == null) {
            $("#customer-contact").parent().addClass("has-error");
            validate = false;
        } else {
            $("#customer-contact").parent().removeClass("has-error");
        }
        var items = null;
        var money = 0;
        var expireAt = null;
        var document = null;
        if (state == CustomerStateDeveloped) {
            items = $("#customer-items").val();
            money = $("#customer-money").val();
            expireAt = $("#customer-expireAt").val();
            document = $("#customer-document").summernote("code");
            if (money != "" && !isInteger(money)) {
                $("#customer-money").parent().addClass("has-error");
                validate = false;
            } else {
                $("#customer-money").parent().removeClass("has-error");
            }
            var length = document.getBytesLength();
            if (length > 1024 * 1024) {
                $.messager.popup("文档大小不能超过1024KB！当前大小：" + formatByte(length));
                return;
            }
        }
        if (!validate) {
            return;
        }

        $(this).text("提交中...").attr("disabled", "disabled");
        CustomerManager.edit(cid, name, capital, contact, aid, iid, items, money, expireAt, remark, document, function (result) {
            if (!result.session) {
                sessionError();
                return;
            }
            if (!result.privilege) {
                $.messager.popup("该账户无权限更改该客户的详细信息！");
                return;
            }
            $("#customer-edit").text("保存客户信息").removeAttr("disabled");
            $.messager.popup("修改成功！");
            $("#customer-document-size").text(formatByte(length));
        });
    });
    
    $("#customer-finish-submit").click(function () {
        var eid = $("#customer-finish-manager").val();
        if (eid == null || eid == "") {
            $("#customer-finish-manager").parent().addClass("has-error");
            return;
        } else {
            $("#customer-finish-manager").parent().removeClass("has-error");
        }
        CustomerManager.finish(cid, eid, function (result) {
            if (!result.session) {
                sessionError();
                return;
            }
            if (!result.privilege) {
                $.messager.popup("该账户无权限完成开发客户！");
                return;
            }
            if (!result.data) {
                $.messager.popup("该客户已完成开发，请勿重复操作！");
                return;
            }
            $.messager.popup("完成开发成功！");
            setTimeout(function () {
                location.reload();
            }, 1000);
        });
    });

    $("#add-manager-submit").click(function () {
        var eid = $("#add-manager-eid").val();
        if (eid == null || eid == "") {
            $("#add-manager-eid").parent().addClass("has-error");
            return;
        } else {
            $("#add-manager-eid").parent().removeClass("has-error");
        }
        var r = $("#add-manager-r").val();
        var w = $("#add-manager-w").val();
        var d = $("#add-manager-d").val();
        var assign = $("#add-manager-assign").val();
        CustomerManager.assign(cid, eid, r, w, d, assign, function (result) {
            if (!result.session) {
                sessionError();
                return;
            }
            if (!result.privilege) {
                $.messager.popup("该账户无权限添加新的负责人！");
                return;
            }
            if (!result.data) {
                $.messager.popup("该员工无权作为新的负责人！");
                return;
            }
            $.messager.popup("新的负责人已添加！");
            $("#add-manager-modal").modal("hide");

            // Remove employee from selector.
            $("#add-manager-eid option[value='" + eid + "']").remove();

            // Add employee in manager list.
            for (var i in assinableEmployees) {
                var employee = assinableEmployees[i];
                if (employee.eid == eid) {
                    addManager(employee);
                }
            }

        });
    });

    $("#save-log-submit").click(function () {
        var title = $("#log-title").val();
        var content = $("#log-content").val();
        if (title == null || title == "") {
            $.messager.popup("标题不能为空！");
            return;
        }
        if (editingLid == null) {
            // Create a new log.
            LogManager.add(cid, title, content, function (result) {
                if (!result.session) {
                    sessionError();
                    return;
                }
                if (!result.privilege) {
                    $("#log-modal").modal("hide");
                    $.messager.popup("该账户无权限添加工作日志！");
                    return;
                }
                if (result.data == null) {
                    $.messager.popup("新建日志错误，请重试！");
                    return;
                }

                $("#log-modal").modal("hide");
                loadLogs();
            });
        } else {
            // Update an existing log.
            LogManager.edit(editingLid, title, content, function (result) {
                if (!result.session) {
                    sessionError();
                    return;
                }
                if (!result.privilege) {
                    $("#log-modal").modal("hide");
                    $.messager.popup("工作日志创建者以外的员工无权修改该日志！");
                    return;
                }

                $("#log-modal").modal("hide");
                loadLogs();
            });
        }
    });

    $("#log-modal").on("hidden.bs.modal", function () {
        $("#log-modal input").val("");
        editingLid = null;
    });

});

function addManager(employee) {
    $("#customer-managers").mengular(".customer-managers-template", employee);

    $("#" + employee.eid).click(function () {
        var eid = $(this).mengularId();
        var name = $("#" + eid + " span").text();
        $.messager.confirm("移除负责人", "确认要移除负责人" + name + "吗？", function () {
            CustomerManager.revokeAssign(cid, eid, function (result) {
                if (!result.session) {
                    sessionError();
                    return;
                }
                if (!result.privilege) {
                    $.messager.popup("该账户无权限删除负责人！");
                    return;
                }
                if (!result.data) {
                    $.messager.popup("找不着该负责人，无法删除！");
                    return;
                }
                $.messager.popup("删除成功！");
                $("#" + eid).remove();
            });
        });
    });
}

function loadLogs() {

    LogManager.getByCid(cid, function (result) {
        console.log(result);
        if (!result.session) {
            sessionError();
            return;
        }
        if (!result.privilege) {
            $("#log-list").html("<h3 class='text-center'>该账户无权限查看工作日志！</h3>");
            return;
        }

        $("#log-list tbody").mengularClear();
        for (var i in result.data) {
            var log = result.data[i];
            $("#log-list tbody").mengular(".log-list-template", {
                lid: log.lid,
                createAt: log.createAt.format(DATE_HOUR_MINUTE_SECOND_FORMAT),
                updateAt: log.updateAt.format(DATE_HOUR_MINUTE_SECOND_FORMAT),
                employee: log.employee.name,
                title: log.title
            });

            $("#" + log.lid + " .log-list-edit a").click(function () {
                editingLid = $(this).mengularId();
                LogManager.get(editingLid, function (result) {
                    if (!result.session) {
                        sessionError();
                        return;
                    }
                    if (!result.privilege) {
                        $.messager.popup("该账户无权限查看工作日志！");
                        return;
                    }
                    fillValue({
                        "log-title": result.data.title,
                        "log-content": result.data.content
                    });
                    $("#log-modal").modal("show");
                });

            });
        }
    });

}