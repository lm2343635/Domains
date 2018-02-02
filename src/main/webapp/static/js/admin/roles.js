var editingRid = null;

var previlegNames = {
    undevelopedR: "待开发客户读权限",
    undevelopedW: "待开发客户写权限",
    undevelopedD: "待开发客户删权限",
    developingR: "开发中客户读权限",
    developingW: "开发中客户写权限",
    developingD: "开发中客户删权限",
    developedR: "已开发客户读权限",
    developedW: "已开发客户写权限",
    developedD: "已开发客户删权限",
    lostR: "已流失客户读权限",
    lostW: "已流失客户写权限",
    lostD: "已流失客户删权限",
    develop: "待开发->开发中",
    finish: "开发中->已开发",
    ruin: "已开发->已流失",
    recover: "已流失->开发中",
    assign: "指派负责人权限",
    server: "服务器管理权限",
    domain: "域名管理权限",
    employee: "员工管理权限",
    expiration: "到期时间查看权限",
    work: "任务管理超级权限"
}

var symbols = ["<i class='fa fa-times text-muted'></i>",
    "<i class='fa fa-circle-o text-warning'></i>",
    "<i class='fa fa-check text-success'></i>"];

$(document).ready(function () {

    checkAdminSession(function () {
        loadRoles();
    });

    $("#add-role").click(function () {
        refreshPrivilgeRadio();
        editingRid = null;
        $("#role-modal").modal("show");
    });

    $("#role-submit").click(function () {
        var name = $("#role-name").val();
        if (name == null || name == "") {
            $("#role-name").parent().addClass("has-error");
            return;
        } else {
            $("#role-name").parent().addClass("has-error");
        }
        var privileges = [];
        var index = 0;
        for (var identifier in previlegNames) {
            privileges[index] = $("input[name='" + identifier + "']:checked").val();
            index++;
        }
        if (editingRid == null) {
            RoleManager.add(name, privileges, function (rid) {
                if (rid == null) {
                    sessionError();
                    return;
                }
                $("#role-modal").modal("hide");
                loadRoles();
            });
        } else {
            RoleManager.edit(editingRid, name, privileges, function (success) {
               if (!success) {
                   sessionError();
                   return;
               }
               $.messager.popup("更改权限成功！");
                $("#role-modal").modal("hide");
                loadRoles();
            });
        }

    });

    $("#role-modal").on("hidden.bs.modal", function () {
        $("#role-modal .input-group").removeClass("has-error");
        $("#role-name").val("");
        editingRid = null;
    });

});

function loadRoles() {
    RoleManager.getAll(function (roles) {
        if (roles == null) {
            sessionError();
            return;
        }

        $("#role-list tbody").mengularClear();

        for (var i in roles) {
            var role = roles[i];

            $("#role-list tbody").mengular(".role-list-template", {
                rid: role.rid,
                name: role.name,
                undeveloped: symbols[role.undevelopedR] + "|" + symbols[role.undevelopedW] + "|" + symbols[role.undevelopedD],
                developing: symbols[role.developingR] + "|" + symbols[role.developingW] + "|" + symbols[role.developingD],
                developed: symbols[role.developedR] + "|" + symbols[role.developedW] + "|" + symbols[role.developedD],
                lost: symbols[role.lostR] + "|" + symbols[role.lostW] + "|" + symbols[role.lostD],
                develop: symbols[role.develop],
                finish: symbols[role.finish],
                ruin: symbols[role.ruin],
                recover: symbols[role.recover],
                assign: symbols[role.assign],
                server: symbols[role.server],
                domain: symbols[role.domain],
                expiration: symbols[role.expiration],
                employee: symbols[role.employee],
                work: symbols[role.work],
                employees: role.employees,
            });

            $("#" + role.rid + " .role-list-remove").click(function () {
                var rid = $(this).mengularId();
                var name = $("#" + rid + " .role-list-name").text();
                $.messager.confirm("删除角色", "确认删除角色" + name + "吗？", function () {
                    RoleManager.remove(rid, function (success) {
                        if (!success) {
                            sessionError();
                            return;
                        }
                        $("#" + rid).remove();
                        $.messager.popup("删除成功！");
                    });
                });
            });

            $("#" + role.rid + " .role-list-edit").click(function () {
                editingRid = $(this).mengularId();
                refreshPrivilgeRadio();
                RoleManager.get(editingRid, function (role) {
                    for (var attribute in role) {
                        if (attribute == "name" || attribute == "rid") {
                            continue;
                        }
                        $("input[name='" + attribute + "']").each(function () {
                            $(this).attr("checked", $(this).val() == role[attribute]);
                        });
                    }
                    $("#role-name").val(role.name);
                    $("#role-modal").modal("show");
                });
            });
        }

    });
}

function refreshPrivilgeRadio() {
    $("#role-privileges").mengularClear();
    for (var identifier in previlegNames) {
        $("#role-privileges").mengular(".privilege-template", {
            identifier: identifier,
            name: previlegNames[identifier]
        });
    }
}