$(document).ready(function () {

    checkEmployeeSession(function (employee) {

        loadEmployees();
    });

    
});

function loadEmployees() {
    EmployeeManager.getAll(function (result) {
        if (!result.session) {
            sessionError();
            return;
        }

        $("#employee-list tbody").mengularClear();

        for (var i in result.data) {
            var employee = result.data[i];
            $("#employee-list tbody").mengular(".employee-list-template", {
                eid: employee.eid,
                createAt: employee.createAt.format(DATE_HOUR_MINUTE_FORMAT),
                updateAt: employee.updateAt.format(DATE_HOUR_MINUTE_FORMAT),
                name: employee.name,
                role: employee.role.name
            });

        }
    });
}