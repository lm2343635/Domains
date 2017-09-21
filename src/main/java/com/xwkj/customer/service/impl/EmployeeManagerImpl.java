package com.xwkj.customer.service.impl;

import com.xwkj.common.util.Debug;
import com.xwkj.customer.bean.EmployeeBean;
import com.xwkj.customer.domain.Employee;
import com.xwkj.customer.domain.Role;
import com.xwkj.customer.service.EmployeeManager;
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
    public List<EmployeeBean> getAll(HttpSession session) {
        if (!checkAdminSession(session)) {
            return null;
        }
        List<EmployeeBean> employeeBeans = new ArrayList<EmployeeBean>();
        for (Employee employee : employeeDao.findAll("createAt", true)) {
            employeeBeans.add(new EmployeeBean(employee));
        }
        return employeeBeans;
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
        return new EmployeeBean(employee);
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

}
