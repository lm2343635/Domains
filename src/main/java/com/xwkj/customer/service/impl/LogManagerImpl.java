package com.xwkj.customer.service.impl;

import com.xwkj.common.util.Debug;
import com.xwkj.customer.bean.Result;
import com.xwkj.customer.domain.Assign;
import com.xwkj.customer.domain.Customer;
import com.xwkj.customer.domain.Employee;
import com.xwkj.customer.domain.Log;
import com.xwkj.customer.service.LogManager;
import com.xwkj.customer.service.RoleManager;
import com.xwkj.customer.service.common.ManagerTemplate;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

@Service
@RemoteProxy(name = "LogManager")
public class LogManagerImpl extends ManagerTemplate implements LogManager {

    @RemoteMethod
    @Transactional
    public Result add(String cid, String title, String content, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        Customer customer = customerDao.get(cid);
        if (customer == null) {
            Debug.error("Cannot find a customer by this cid.");
            return Result.WithData(false);
        }
        int privilege = getPrivilegeForEmployee(employee, customer.getState(), RoleManager.PrivilegeWrite);
        if (privilege == RoleManager.RolePrivilgeNone) {
            return Result.NoPrivilege();
        } else if (privilege == RoleManager.RolePrivilgeAssign) {
            Assign assign = assignDao.getByCustomerForEmployee(customer, employee);
            if (assign == null) {
                return Result.NoPrivilege();
            }
            if (!assign.getW()) {
                return Result.NoPrivilege();
            }
        }
        // Create a new log.
        Log log = new Log();
        log.setCreateAt(System.currentTimeMillis());
        log.setUpdateAt(log.getCreateAt());
        log.setTitle(title);
        log.setContent(content);
        log.setCustomer(customer);
        log.setEmployee(employee);
        String lid = logDao.save(log);
        if (lid == null) {
            Debug.error("Internal error: save log failed.");
            return Result.WithData(null);
        }
        return Result.WithData(lid);
    }

}
