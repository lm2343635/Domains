package com.xwkj.customer.service.common;

import com.xwkj.common.util.Debug;
import com.xwkj.customer.dao.*;
import com.xwkj.customer.domain.Employee;
import com.xwkj.customer.service.AdminManager;
import com.xwkj.customer.service.EmployeeManager;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;

public class ManagerTemplate {

    @Autowired
    protected RoleDao roleDao;

    @Autowired
    protected EmployeeDao employeeDao;

    @Autowired
    protected CustomerDao customerDao;

    @Autowired
    protected LogDao logDao;

    @Autowired
    protected  AssignDao assignDao;

    @Autowired
    protected ServerDao serverDao;

    @Autowired
    protected DomainDao domainDao;

    public boolean checkAdminSession(HttpSession session) {
        return session.getAttribute(AdminManager.AdminFlag) != null;
    }

    public Employee getEmployeeFromSession(HttpSession session) {
        if (session.getAttribute(EmployeeManager.EmployeeFlag) == null) {
            return null;
        }
        String eid = (String) session.getAttribute(EmployeeManager.EmployeeFlag);
        Employee employee = employeeDao.get(eid);
        if (employee == null) {
            Debug.error("Cannot find a employee by this eid from session.");
            return null;
        }
        return employee;
    }

}
