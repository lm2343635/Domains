$(document).ready(function () {
   
    checkAdminSession(function () {
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
        if (validate) {
            ServerManager.add(name, address, remark, function (sid) {
                if (sid == null) {
                    location.href = "session.html";
                    return;
                }
                $("#add-server-modal").modal("hide");
                $.messager.popup("新建成功！");
                loadServers();
            });
        }
    });

    $("#add-server-modal").on("hidden.bs.modal", function () {
        $("#add-server-modal .input-group").removeClass("has-error");
        $("#add-server-modal input").val("");
    });
    
});

function loadServers() {
    ServerManager.getAll(function (servers) {
        if (servers == null) {
            location.href = "session.html";
            return;
        }
        $("#server-list tbody").mengularClear();
        for (var i in servers) {
            var server = servers[i];
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
                var sid = $(this).mengularId();
            });
        }
    });
}