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
    domain: "域名管理权限"
}

var previlegeSymbols = ["", "", ""];

$(document).ready(function () {

    checkAdminSession(function () {
        loadRoles();

        for (var identifier in previlegNames) {
            $("#add-role-previleges").mengular(".previlege-template", {
                identifier: identifier,
                name: previlegNames[identifier]
            });
        }
        $("#add-role-previleges").mengularClearTemplate();
    });

    $("#add-role-submit").click(function () {
        var name = $("#add-role-name").val();
        if (name == null || name == "") {
            $("#add-role-name").parent().addClass("has-error");
            return;
        } else {
            $("#add-role-name").parent().addClass("has-error");
        }
        var previleges = [];
        var index = 0;
        for (var identifier in previlegNames) {
            previleges[index] = $("input[name='" + identifier + "']:checked").val();
            index++;
        }
        RoleManager.add(name, previleges, function (rid) {
            if (rid == null) {
                sessionError();
                return;
            }
            $("#add-role-modal").modal("hide");
            loadRoles();
        });
    });

    $("#add-role-modal").on("hidden.bs.modal", function () {
        $("#add-role-modal .input-group").removeClass("has-error");
        $("#add-role-modal input").val("");
    });

});

function loadRoles() {

}