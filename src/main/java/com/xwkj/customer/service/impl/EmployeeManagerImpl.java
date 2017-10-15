package com.xwkj.customer.service.impl;

import com.xwkj.common.util.Debug;
import com.xwkj.customer.bean.AssignBean;
import com.xwkj.customer.bean.EmployeeBean;
import com.xwkj.customer.bean.Result;
import com.xwkj.customer.domain.Assign;
import com.xwkj.customer.domain.Customer;
import com.xwkj.customer.domain.Employee;
import com.xwkj.customer.domain.Role;
import com.xwkj.customer.service.EmployeeManager;
import com.xwkj.customer.service.RoleManager;
import com.xwkj.customer.service.common.ManagerTemplate;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
@RemoteProxy(name = "EmployeeManager")
public class EmployeeManagerImpl extends ManagerTemplate implements EmployeeManager {

    // ************* For admin ****************

    @RemoteMethod
    @Transactional
    public String add(String name, String password, String rid, HttpSession session) {
        if (!checkAdminSession(session)) {
            return null;
        }
        Role role = roleDao.get(rid);
        if (role == null) {
            Debug.error("Cannot find a role by this rid.");
            return null;
        }
        Employee employee = new Employee();
        employee.setCreateAt(System.currentTimeMillis());
        employee.setUpdateAt(employee.getCreateAt());
        employee.setName(name);
        employee.setPassword(password);
        employee.setRole(role);
        employee.setEnable(true);
        String eid = employeeDao.save(employee);
        if (eid == null) {
            return null;
        }
        role.setEmployees(role.getEmployees() + 1);
        roleDao.update(role);
        return eid;
    }

    @RemoteMethod
    public EmployeeBean get(String eid, HttpSession session) {
        if (!checkAdminSession(session)) {
            return null;
        }
        Employee employee = employeeDao.get(eid);
        if (employee == null) {
            Debug.error("Cannot find a employee by this eid.");
            return null;
        }
        return new EmployeeBean(employee, true);
    }

    @RemoteMethod
    @Transactional
    public boolean edit(String eid, String name, String rid, HttpSession session) {
        if (!checkAdminSession(session)) {
            return false;
        }
        Employee employee = employeeDao.get(eid);
        if (employee == null) {
            Debug.error("Cannot find a employee by this eid.");
            return false;
        }
        Role role = roleDao.get(rid);
        if (role == null) {
            Debug.error("Cannot find a role by this rid.");
            return false;
        }
        employee.setName(name);
        employee.setRole(role);
        employeeDao.update(employee);
        return true;
    }

    @RemoteMethod
    @Transactional
    public boolean remove(String eid, HttpSession session) {
        if (!checkAdminSession(session)) {
            return false;
        }
        Employee employee = employeeDao.get(eid);
        if (employee == null) {
            Debug.error("Cannot find a employee by this eid.");
            return false;
        }
        Role role = employee.getRole();
        role.setEmployees(role.getEmployees() - 1);
        roleDao.update(role);
        // TODO cannot delete a employee who has customers.

        employeeDao.delete(employee);
        return true;
    }

    @RemoteMethod
    @Transactional
    public boolean resetPassword(String eid, String password, HttpSession session) {
        if (!checkAdminSession(session)) {
            return false;
        }
        Employee employee = employeeDao.get(eid);
        if (employee == null) {
            Debug.error("Cannot find a employee by this eid.");
            return false;
        }
        employee.setPassword(password);
        employeeDao.update(employee);
        return true;
    }

    // ************* For admin & employee *************

    @RemoteMethod
    public Result getAll(HttpSession session) {
        boolean admin = checkAdminSession(session);
        Employee employee = getEmployeeFromSession(session);
        if (!admin && employee == null) {
            return Result.NoSession();
        }
        List<Employee> employees = null;
        if (admin || employee.getRole().getEmployee() == RoleManager.RolePrivilgeHold) {
            employees = employeeDao.findAll("createAt", true);
        } else {
            employees = new ArrayList<Employee>();
            employees.add(employee);
        }
        List<EmployeeBean> employeeBeans = new ArrayList<EmployeeBean>();
        for (Employee e : employees) {
            employeeBeans.add(new EmployeeBean(e, true));
        }
        return Result.WithData(employeeBeans);
    }

    // ************* For employee ****************

    @RemoteMethod
    public String login(String username, String password, HttpSession session) {
        Employee employee = employeeDao.getByName(username);
        if (employee == null) {
            Debug.error("Cannot find a employee by this name.");
            return null;
        }
        if (!employee.getPassword().equals(password)) {
            Debug.error("Password error.");
            return null;
        }
        session.setAttribute(EmployeeFlag, employee.getEid());
        Role role = employee.getRole();
        if (role.getUndevelopedR() > RoleManager.RolePrivilgeNone) {
            return "undeveloped";
        }
        if (role.getDevelopedR() > RoleManager.RolePrivilgeNone) {
            return "developing";
        }
        if (role.getDevelopedR() > RoleManager.RolePrivilgeNone) {
            return "developed";
        }
        if (role.getLostR() > RoleManager.RolePrivilgeNone) {
            return "lost";
        }
        if (role.getServer() > RoleManager.RolePrivilgeNone) {
            return "domains";
        }
        return "";
    }

    @RemoteMethod
    public EmployeeBean checkSession(HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return null;
        }
        return new EmployeeBean(employee, true);
    }

    @RemoteMethod
    public Result getDevelopingAssignableEmployees(HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        List<EmployeeBean> employeeBeans = new ArrayList<EmployeeBean>();
        for (Employee manager : employeeDao.findByRolePrivilege("developedW", RoleManager.RolePrivilgeHold)) {
            employeeBeans.add(new EmployeeBean(manager, true));
        }
        for (Employee manager : employeeDao.findByRolePrivilege("developedW", RoleManager.RolePrivilgeAssign)) {
            employeeBeans.add(new EmployeeBean(manager, true));
        }
        return Result.WithData(employeeBeans);
    }

    @RemoteMethod
    public Result getDevelopedAssinableEmployees(String cid, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        Customer customer = customerDao.get(cid);
        if (customer == null) {
            Debug.error("Cannot find a customer by this cid.");
            return Result.WithData(null);
        }

        List<EmployeeBean> employeeBeans = new ArrayList<EmployeeBean>();
        List<Assign> assigns = assignDao.findByCustomer(customer);
        List<Employee> managers = employeeDao.findByRolePrivilege("developedW", RoleManager.RolePrivilgeHold);
        managers.addAll(employeeDao.findByRolePrivilege("developedW", RoleManager.RolePrivilgeAssign));
        for (Assign assign : assigns) {
            if (managers.contains(assign.getEmployee())) {
                managers.remove(assign.getEmployee());
            }
        }
        for (Employee manager : managers) {
            employeeBeans.add(new EmployeeBean(manager, true));
        }
        return Result.WithData(employeeBeans);
    }

    @RemoteMethod
    public Result assignForCustomer(String cid, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        Customer customer = customerDao.get(cid);
        if (customer == null) {
            Debug.error("Cannot find a customer by this cid.");
            return Result.WithData(null);
        }
        Role role = employee.getRole();
        AssignBean assignBean = new AssignBean(role.getDevelopedR() == RoleManager.RolePrivilgeHold,
                        role.getDevelopedW() == RoleManager.RolePrivilgeHold,
                        role.getDevelopedD() == RoleManager.RolePrivilgeHold,
                        role.getAssign() == RoleManager.RolePrivilgeHold);
        Assign assign = assignDao.getByCustomerForEmployee(customer, employee);
        if (assign != null) {
            if (assign.getR()) {
                assignBean.setR(true);
            }
            if (assign.getW()) {
                assignBean.setW(true);
            }
            if (assign.getD()) {
                assign.setD(true);
            }
            if (assign.getAssign()) {
                assign.setAssign(true);
            }
        }
        return Result.WithData(assignBean);
    }

}
