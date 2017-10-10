package com.xwkj.customer.service.common;

import com.xwkj.common.util.Debug;
import com.xwkj.customer.dao.*;
import com.xwkj.customer.domain.Employee;
import com.xwkj.customer.service.AdminManager;
import com.xwkj.customer.service.CustomerManager;
import com.xwkj.customer.service.EmployeeManager;
import com.xwkj.customer.service.RoleManager;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;

public class ManagerTemplate {

    @Autowired
    protected RoleDao roleDao;

    @Autowired
    protected AreaDao areaDao;

    @Autowired
    protected IndustryDao industryDao;

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

    public boolean checkEmployeeSession(HttpSession session) {
        return session.getAttribute(EmployeeManager.EmployeeFlag) != null;
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

    /**
     * Get privilge of a customer in a state for an employee.
     *
     * @param employee
     * @param state
     * @param type
     * @return
     */
    public int getPrivilegeForEmployee(Employee employee, int state, int type) {
        int privilege = RoleManager.RolePrivilgeNone;
        switch (state) {
            case CustomerManager.CustomerStateUndeveloped:
                if (type == RoleManager.PrivilegeRead) {
                    privilege = employee.getRole().getUndevelopedR();
                } else if (type == RoleManager.PrivilegeWrite) {
                    privilege = employee.getRole().getUndevelopedW();
                } else if (type == RoleManager.PrivilegeDelete) {
                    privilege = employee.getRole().getUndevelopedD();
                }
                break;
            case CustomerManager.CustomerStateDeveloping:
                if (type == RoleManager.PrivilegeRead) {
                    privilege = employee.getRole().getDevelopingR();
                } else if (type == RoleManager.PrivilegeWrite) {
                    privilege = employee.getRole().getDevelopingW();
                } else if (type == RoleManager.PrivilegeDelete) {
                    privilege = employee.getRole().getDevelopingD();
                }
                break;
            case CustomerManager.CustomerStateDeveloped:
                if (type == RoleManager.PrivilegeRead) {
                    privilege = employee.getRole().getDevelopedR();
                } else if (type == RoleManager.PrivilegeWrite) {
                    privilege = employee.getRole().getDevelopedW();
                } else if (type == RoleManager.PrivilegeDelete) {
                    privilege = employee.getRole().getDevelopedD();
                }
                break;
            case CustomerManager.CustomerStateLost:
                if (type == RoleManager.PrivilegeRead) {
                    privilege = employee.getRole().getLostR();
                } else if (type == RoleManager.PrivilegeWrite) {
                    privilege = employee.getRole().getLostW();
                } else if (type == RoleManager.PrivilegeDelete) {
                    privilege = employee.getRole().getLostD();
                }
                break;
            default:
                break;
        }
        return privilege;
    }

}
