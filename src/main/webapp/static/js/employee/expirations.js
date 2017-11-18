$(document).ready(function () {

    checkEmployeeSession(function (employee) {
        if (employee.role.expiration != RolePrevilgeHold) {
            $.messager.popup("当前用户无权限查看到期时间列表！");
            return;
        }

        TypeManager.getAll(function (result) {
            if (!result.session) {
                return;
            }
            for (var i in result.data) {
                var type = result.data[i];
                $("<option>").val(type.tid).text(type.name).appendTo("#search-expiration-type");
            }
        });
    });

    $("#search-expiration-start, #search-expiration-end").datetimepicker({
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        startView: 2,
        minView: 2,
        language: "zh-CN"
    });



});