package com.xwkj.customer.service.impl;

import com.xwkj.common.util.Debug;
import com.xwkj.customer.bean.AssignBean;
import com.xwkj.customer.bean.EmployeeBean;
import com.xwkj.customer.bean.GlobalSearch;
import com.xwkj.customer.bean.Result;
import com.xwkj.customer.domain.*;
import com.xwkj.customer.service.EmployeeManager;
import com.xwkj.customer.service.RoleManager;
import com.xwkj.customer.service.common.ManagerTemplate;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.*;

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
    @Transactional
    public boolean edit(String eid, String name, String rid, HttpSession session) {
        if (!checkAdminSession(session)) {
            return false;
        }
        Employee employee = employeeDao.get(eid);
        if (employee == null) {
            Debug.error("Cannot find an employee by this eid.");
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
            Debug.error("Cannot find an employee by this eid.");
            return false;
        }
        Role role = employee.getRole();
        role.setEmployees(role.getEmployees() - 1);
        roleDao.update(role);
        // TODO cannot delete a employee who has customers.

        employeeDao.delete(employee);
        return true;
    }

    @Transactional
    @RemoteMethod
    public boolean resetPassword(String eid, String password, HttpSession session) {
        if (!checkAdminSession(session)) {
            return false;
        }
        Employee employee = employeeDao.get(eid);
        if (employee == null) {
            Debug.error("Cannot find an employee by this eid.");
            return false;
        }
        employee.setPassword(password);
        employeeDao.update(employee);
        return true;
    }

    @Transactional
    @RemoteMethod
    public Result enable(String eid, boolean enable, HttpSession session) {
        if (!checkAdminSession(session)) {
            return Result.NoSession();
        }
        Employee employee = employeeDao.get(eid);
        if (employee == null) {
            Debug.error("Cannot find an employee by this eid.");
            return Result.WithData(false);
        }
        employee.setEnable(enable);
        employeeDao.update(employee);
        return Result.WithData(true);
    }

    // ************* For admin & employee *************

    @RemoteMethod
    public Result getAll(HttpSession session) {
        boolean admin = checkAdminSession(session);
        Employee employee = getEmployeeFromSession(session);
        if (!admin && employee == null) {
            return Result.NoSession();
        }
        List<EmployeeBean> employeeBeans = new ArrayList<EmployeeBean>();
        for (Employee e : employeeDao.findAll("createAt", true)) {
            employeeBeans.add(new EmployeeBean(e, true));
        }
        return Result.WithData(employeeBeans);
    }

    @RemoteMethod
    public Result get(String eid, HttpSession session) {
        boolean admin = checkAdminSession(session);
        Employee viewer = getEmployeeFromSession(session);
        if (!admin && viewer == null) {
            return Result.NoSession();
        }
        Employee employee = employeeDao.get(eid);
        if (employee == null) {
            Debug.error("Cannot find a employee by this eid.");
            return Result.WithData(null);
        }
        if (!admin && viewer.getRole().getEmployee() != RoleManager.RolePrivilgeHold && !viewer.equals(employee)) {
            return Result.NoPrivilege();
        }
        return Result.WithData(new EmployeeBean(employee, true));
    }

    // ************* For employee ****************

    @RemoteMethod
    public Result login(String username, String password, HttpSession session) {
        Employee employee = employeeDao.getByName(username);
        if (employee == null) {
            Debug.error("Cannot find a employee by this name.");
            return Result.WithData(EmployeeLoginNotFound);
        }
        if (!employee.getPassword().equals(password)) {
            Debug.error("Password error.");
            return Result.WithData(EmployeeLoginWrongPassword);
        }
        if (!employee.getEnable()) {
            Debug.error("This user is not enable now.");
            return Result.WithData(EmployeeLoginNotEnable);
        }
        session.setAttribute(EmployeeFlag, employee.getEid());
        return Result.WithData(EmployeeLoginSuccess);
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
    public Result getEnable(HttpSession session) {
        Employee manager = getEmployeeFromSession(session);
        if (manager == null) {
            return Result.NoPrivilege();
        }
        List<EmployeeBean> employeeBeans = new ArrayList<EmployeeBean>();
        for (Employee employee : employeeDao.findByEnable(true)) {
            employeeBeans.add(new EmployeeBean(employee, true));
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

    @RemoteMethod
    public Result globalSearch(String keyword, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        final List<GlobalSearch> customers = new ArrayList<GlobalSearch>();
        for (Customer customer : customerDao.globalSearch(keyword)) {
            customers.add(new GlobalSearch(GlobalSearch.GlobalSearchCustomer,
                    customer.getName(),
                    new Date(customer.getUpdateAt()),
                    customer.getCid()));
        }
        final List<GlobalSearch> domains = new ArrayList<GlobalSearch>();
        for (Domain domain : domainDao.globalSearch(keyword)) {
            domains.add(new GlobalSearch(GlobalSearch.GlobalSearchDomain,
                    domain.getName() + "(" + domain.getDomains() + ")",
                    new Date(domain.getUpdateAt()),
                    domain.getDid(),
                    domain.getServer().getSid()));
        }
        return Result.WithData(new HashMap<Integer, List>() {{
            put(GlobalSearch.GlobalSearchCustomer, customers);
            put(GlobalSearch.GlobalSearchDomain, domains);
        }});
    }

    // For api.
    @Override
    public EmployeeBean getByName(String name) {
        Employee employee = employeeDao.getByName(name);
        if (employee == null) {
            Debug.error("Cannot find a employee by this name.");
            return null;
        }
        return new EmployeeBean(employee, true);
    }
}
