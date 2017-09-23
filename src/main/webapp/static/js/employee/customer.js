var cid = request("cid");
var state;

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
            // console.log(customer);
            state = customer.state;
            fillValue({
                "customer-name": customer.name,
                "customer-capital": customer.capital,
                "customer-contact": customer.contact
            });

            if (state == CustomerStateDeveloped) {
                fillValue({
                    "customer-money": customer.money == 0 ? "" : customer.money,
                    "customer-items": customer.items,
                    "customer-remark": customer.remark
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
            } else {
                $("#customer-panel .customer-developed").remove();
            }

            if (state != CustomerStateUndeveloped || employee.role.develop == RolePrevilgeNone) {
                $("#customer-develop").remove();
            }

            if (state != CustomerStateDeveloping || employee.role.finish == RolePrevilgeNone) {
                $("#customer-finish").remove();
            } else {
                EmployeeManager.getDevelopingAssignableEmployees(function (employees) {
                    for (var i in employees) {
                        var employee = employees[i];
                        $("<option>").val(employee.eid).text(employee.name + "（" + employee.role.name + "）").appendTo("#customer-finish-manager");
                    }
                });
            }

            $("#customer-managers").mengular(".customer-managers-template", customer.managers);

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
                    $.messager.popup("改账户无权申请开发客户！");
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
        var remark = null;
        var document = null;
        if (state == CustomerStateDeveloped) {
            items = $("#customer-items").val();
            money = $("#customer-money").val();
            remark = $("#customer-remark").val();
            expireAt = $("#customer-expireAt").val();
            document = $("#customer-document").summernote("code");
            if (money != "" && !isInteger(money)) {
                $("#customer-money").parent().addClass("has-error");
                validate = false;
            } else {
                $("#customer-money").parent().removeClass("has-error");
            }
        }
        if (!validate) {
            return;
        }

        $(this).text("提交中...").attr("disabled", "disabled");
        CustomerManager.edit(cid, name, capital, contact, items, money, expireAt, remark, document, function (result) {
            if (!result.session) {
                sessionError();
                return;
            }
            if (!result.privilege) {
                $.messager.popup("改账户无权限更改该客户的详细信息！");
                return;
            }
            $("#customer-edit").text("保存客户信息").removeAttr("disabled");
            $.messager.popup("修改成功！");
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
                $.messager.popup("改账户无权限更改该客户的详细信息！");
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

});