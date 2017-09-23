var editingSid = null;

$(document).ready(function () {

    checkEmployeeSession(function () {
        loadServers();
    });
    
    $("#add-server-submit").click(function () {
        var name = $("#add-server-name").val();
        var address = $("#add-server-address").val();
        var remark = $("#add-server-remark").val();
        var validate = true;
        if (name == "" || name == null) {
            $("#add-server-name").parent().addClass("has-error");
            validate = false;
        } else {
            $("#add-server-name").parent().removeClass("has-error");
        }
        if (address == "" || address == null) {
            $("#add-server-address").parent().addClass("has-error");
            validate = false;
        } else {
            $("#add-server-address").parent().removeClass("has-error");
        }
        if (!validate) {
            return;
        }
        if (editingSid == null) {
            ServerManager.add(name, address, remark, function (result) {
                if (!result.session) {
                    sessionError();
                    return;
                }
                if (!result.privilege) {
                    $.messager.popup("当前用户无权限创建服务器！");
                    return;
                }
                $("#add-server-modal").modal("hide");
                $.messager.popup(result.data != null ? "新建成功！" : "新建失败，请重试！");
                loadServers();
            });
        } else {
            ServerManager.modify(editingSid, name, address, remark, function (success) {
                if (!success) {
                    location.href = "../../../admin/session.html";
                    return;
                }
                $("#add-server-modal").modal("hide");
                $.messager.popup("修改成功！");
                loadServers();
            })
        }
    });

    $("#add-server-modal").on("hidden.bs.modal", function () {
        $("#add-server-modal .input-group").removeClass("has-error");
        $("#add-server-modal input").val("");
        editingSid = null;
    });
    
});

function loadServers() {
    ServerManager.getAll(function (result) {
        if (!result.session) {
            sessionError();
            return;
        }
        if (!result.privilege) {
            $.messager.popup("当前账户无权限查看服务器！");
            return;
        }
        $("#server-list tbody").mengularClear();
        for (var i in result.data) {
            var server = result.data[i];

            $("#server-list tbody").mengular(".server-list-template", {
                sid: server.sid,
                createAt: server.createAt.format(DATE_HOUR_MINUTE_FORMAT),
                updateAt: server.updateAt.format(DATE_HOUR_MINUTE_FORMAT),
                name: server.name,
                address: server.address,
                domains: server.domains,
                remark: server.remark
            });
            
            $("#" + server.sid + " .server-list-edit").click(function () {
                editingSid = $(this).mengularId();
                ServerManager.get(editingSid, function (server) {
                    if (server == null) {
                        sessionError();
                        return;
                    }
                    fillValue({
                        "add-server-name": server.name,
                        "add-server-address": server.address,
                        "add-server-remark": server.remark
                    });
                    $("#add-server-modal").modal("show");
                });
            });

            $("#" + server.sid + " .server-list-remove").click(function () {
                var sid = $(this).mengularId();
                var name = $("#" + sid + " .server-list-name").text();
                var domains = parseInt($("#" + sid + " .server-list-customer").text());
                if (domains > 0) {
                    $.messager.popup("该服务器下有域名，无法删除！");
                    return;
                }
                $.messager.confirm("删除服务器", "确认删除服务器" + name + "吗？", function () {
                    ServerManager.remove(sid, function (success) {
                        if (!success) {
                            sessionError();
                            return;
                        }
                        $("#" + sid).remove();
                        $.messager.popup("删除成功！");
                    });
                });
            });
        }
    });
}