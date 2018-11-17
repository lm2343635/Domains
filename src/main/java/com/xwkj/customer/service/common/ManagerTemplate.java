package com.xwkj.customer.service.common;

import com.xwkj.common.util.Debug;
import com.xwkj.customer.component.AliyunOSSComponent;
import com.xwkj.customer.component.ConfigComponent;
import com.xwkj.customer.component.DomainComponent;
import com.xwkj.customer.dao.*;
import com.xwkj.customer.domain.Assign;
import com.xwkj.customer.domain.Customer;
import com.xwkj.customer.domain.Employee;
import com.xwkj.customer.service.AdminManager;
import com.xwkj.customer.service.CustomerManager;
import com.xwkj.customer.service.EmployeeManager;
import com.xwkj.customer.service.RoleManager;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;

public class ManagerTemplate {

    @Autowired
    protected ConfigComponent configComponent;

    @Autowired
    protected DomainComponent domainComponent;

    @Autowired
    protected AliyunOSSComponent aliyunOSSComponent;

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
    protected AssignDao assignDao;

    @Autowired
    protected ServerDao serverDao;

    @Autowired
    protected DomainDao domainDao;

    @Autowired
    protected BulletinDao bulletinDao;

    @Autowired
    protected DocumentDao documentDao;

    @Autowired
    protected ReportDao reportDao;

    @Autowired
    protected SalaryDao salaryDao;

    @Autowired
    protected TypeDao typeDao;

    @Autowired
    protected ExpirationDao expirationDao;

    @Autowired
    protected WorkDao workDao;

    @Autowired
    protected ReplyDao replyDao;

    @Autowired
    protected CheckDao checkDao;

    @Autowired
    protected DeviceDao deviceDao;

    private String rootPath;

    public String getRootPath() {
        if (rootPath == null) {
            rootPath = this.getClass().getClassLoader().getResource("/").getPath().split("WEB-INF")[0];
        }
        return rootPath;
    }

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

    public boolean assignReadPrivilege(Customer customer, Employee employee) {
        Assign assign = assignDao.getByCustomerForEmployee(customer, employee);
        if (assign == null) {
            return false;
        }
        if (assign.getR() == null) {
            return false;
        }
        return true;
    }

    public boolean assignWritePrivilege(Customer customer, Employee employee) {
        Assign assign = assignDao.getByCustomerForEmployee(customer, employee);
        if (assign == null) {
            return false;
        }
        if (assign.getW() == null) {
            return false;
        }
        return true;
    }

    public boolean assignDeletePrivilege(Customer customer, Employee employee) {
        Assign assign = assignDao.getByCustomerForEmployee(customer, employee);
        if (assign == null) {
            return false;
        }
        if (assign.getD() == null) {
            return false;
        }
        return true;
    }

}
