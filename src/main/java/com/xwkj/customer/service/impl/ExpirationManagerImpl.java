package com.xwkj.customer.service.impl;

import com.xwkj.common.util.DateTool;
import com.xwkj.common.util.Debug;
import com.xwkj.customer.bean.ExpirationBean;
import com.xwkj.customer.bean.Result;
import com.xwkj.customer.domain.*;
import com.xwkj.customer.service.ExpirationManager;
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
@RemoteProxy(name = "ExpirationManager")
public class ExpirationManagerImpl extends ManagerTemplate implements ExpirationManager {

    @RemoteMethod
    @Transactional
    public Result add(String cid, String tid, String expireAt, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        Type type = typeDao.get(tid);
        if (type == null) {
            Debug.error("Cannot find a type by this tid.");
            return Result.WithData(null);
        }
        Customer customer = customerDao.get(cid);
        if (customer == null) {
            Debug.error("Cannot find a customer by this cid.");
            return Result.WithData(null);
        }
        switch (getPrivilegeForEmployee(employee, customer.getState(), RoleManager.PrivilegeWrite)) {
            case RoleManager.RolePrivilgeAssign:
                Assign assign = assignDao.getByCustomerForEmployee(customer, employee);
                if (assign == null) {
                    return Result.NoPrivilege();
                }
                if (!assign.getW()) {
                    return Result.NoPrivilege();
                }
                break;
            case RoleManager.RolePrivilgeNone:
                return Result.NoPrivilege();
            default:
                break;
        }
        Expiration expiration = new Expiration();
        expiration.setCreateAt(System.currentTimeMillis());
        expiration.setUpdateAt(expiration.getCreateAt());
        expiration.setExpireAt(DateTool.transferDate(expireAt, DateTool.YEAR_MONTH_DATE_FORMAT).getTime());
        expiration.setCustomer(customer);
        expiration.setType(type);
        return Result.WithData(expirationDao.save(expiration));
    }

    @RemoteMethod
    public Result getByCid(String cid, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        Customer customer = customerDao.get(cid);
        if (customer == null) {
            Debug.error("Cannot find a customer by this cid.");
            return Result.WithData(null);
        }
        switch (getPrivilegeForEmployee(employee, customer.getState(), RoleManager.PrivilegeRead)) {
            case RoleManager.RolePrivilgeAssign:
                Assign assign = assignDao.getByCustomerForEmployee(customer, employee);
                if (assign == null) {
                    return Result.NoPrivilege();
                }
                if (!assign.getR()) {
                    return Result.NoPrivilege();
                }
                break;
            case RoleManager.RolePrivilgeNone:
                return Result.NoPrivilege();
            default:
                break;
        }
        List<ExpirationBean> expirationBeans = new ArrayList<ExpirationBean>();
        for (Expiration expiration : expirationDao.findByCustomer(customer)) {
            expirationBeans.add(new ExpirationBean(expiration, false));
        }
        return Result.WithData(expirationBeans);
    }

    @RemoteMethod
    @Transactional
    public Result edit(String eid, String expireAt, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        Expiration expiration = expirationDao.get(eid);
        if (expiration == null) {
            Debug.error("Cannot find an expiration by this eid.");
            return Result.WithData(false);
        }
        switch (getPrivilegeForEmployee(employee, expiration.getCustomer().getState(), RoleManager.PrivilegeWrite)) {
            case RoleManager.RolePrivilgeAssign:
                Assign assign = assignDao.getByCustomerForEmployee(expiration.getCustomer(), employee);
                if (assign == null) {
                    return Result.NoPrivilege();
                }
                if (!assign.getW()) {
                    return Result.NoPrivilege();
                }
                break;
            case RoleManager.RolePrivilgeNone:
                return Result.NoPrivilege();
            default:
                break;
        }
        expiration.setUpdateAt(System.currentTimeMillis());
        expiration.setExpireAt(DateTool.transferDate(expireAt, DateTool.YEAR_MONTH_DATE_FORMAT).getTime());
        expirationDao.update(expiration);
        return Result.WithData(true);
    }

    @RemoteMethod
    @Transactional
    public Result remove(String eid, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        Expiration expiration = expirationDao.get(eid);
        if (expiration == null) {
            Debug.error("Cannot find an expiration by this eid.");
            return Result.WithData(false);
        }
        switch (getPrivilegeForEmployee(employee, expiration.getCustomer().getState(), RoleManager.PrivilegeDelete)) {
            case RoleManager.RolePrivilgeAssign:
                Assign assign = assignDao.getByCustomerForEmployee(expiration.getCustomer(), employee);
                if (assign == null) {
                    return Result.NoPrivilege();
                }
                if (!assign.getW()) {
                    return Result.NoPrivilege();
                }
                break;
            case RoleManager.RolePrivilgeNone:
                return Result.NoPrivilege();
            default:
                break;
        }
        expirationDao.delete(expiration);
        return Result.WithData(true);
    }

}
