package com.xwkj.customer.service.impl;

import com.xwkj.common.util.Debug;
import com.xwkj.customer.domain.Employee;
import com.xwkj.customer.domain.Role;
import com.xwkj.customer.service.EmployeeManager;
import com.xwkj.customer.service.common.ManagerTemplate;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

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
        return employeeDao.save(employee);
    }

}
