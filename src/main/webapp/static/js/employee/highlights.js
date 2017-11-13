

$(document).ready(function () {

    checkAdminSession(function () {
        ServerManager.getAll(function (servers) {
            if (servers == null) {
                sessionError();
                return;
            }
            for (var i in servers) {
                var server = servers[i];

            }
        });


    });

});


