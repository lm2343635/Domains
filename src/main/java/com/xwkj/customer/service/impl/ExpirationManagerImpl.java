package com.xwkj.customer.service.impl;

import com.xwkj.common.util.DateTool;
import com.xwkj.common.util.Debug;
import com.xwkj.customer.bean.ExpirationBean;
import com.xwkj.customer.bean.Result;
import com.xwkj.customer.domain.*;
import com.xwkj.customer.service.ExpirationManager;
import com.xwkj.customer.service.RoleManager;
import com.xwkj.customer.service.TypeManager;
import com.xwkj.customer.service.common.ManagerTemplate;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RemoteProxy(name = "ExpirationManager")
public class ExpirationManagerImpl extends ManagerTemplate implements ExpirationManager {

    @RemoteMethod
    @Transactional
    public Result add(String cid, String tid, String expireAt, int money, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        Type type = typeDao.get(tid);
        if (type == null) {
            Debug.error("Cannot find a type by this tid.");
            return Result.WithData(null);
        }
        if (type.getCategory() != TypeManager.TypeCategoryExpiration) {
            Debug.error("Type category error!");
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
        expiration.setMoney(money);
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
    public Result edit(String eid, String expireAt, int money, HttpSession session) {
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
        expiration.setMoney(money);
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

    @RemoteMethod
    public Result getCount(String tid, String customer, String start, String end, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        if (employee.getRole().getExpiration() != RoleManager.RolePrivilgeHold) {
            return Result.NoPrivilege();
        }
        Type type = null;
        if (tid != null && !tid.equals("")) {
            type = typeDao.get(tid);
            if (type == null) {
                Debug.error("Cannot find a type by this tid.");
                return Result.WithData(0);
            }
        }
        Long startStamp = null, endStamp = null;
        if (start != null && !start.equals("")) {
            startStamp = DateTool.transferDate(start + " 00:00:00", DateTool.DATE_HOUR_MINUTE_SECOND_FORMAT).getTime();
        }
        if (end != null && !end.equals("")) {
            endStamp = DateTool.transferDate(end + " 23:59:59", DateTool.DATE_HOUR_MINUTE_SECOND_FORMAT).getTime();
        }
        final int searchCount = expirationDao.getSearchCount(type, customer, startStamp, endStamp);
        final int moneyCount = (searchCount == 0) ? 0 : expirationDao.getMoneyCount(type, customer, startStamp, endStamp);
        return Result.WithData(new HashMap<String, Integer>() {{
            put("searchCount", searchCount);
            put("moneyCount", moneyCount);
        }});
    }

    @RemoteMethod
    public Result search(String tid, String customer, String start, String end, int page, int pageSize, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        if (employee.getRole().getExpiration() != RoleManager.RolePrivilgeHold) {
            return Result.NoPrivilege();
        }
        Type type = null;
        if (tid != null && !tid.equals("")) {
            type = typeDao.get(tid);
            if (type == null) {
                Debug.error("Cannot find a type by this tid.");
                return Result.WithData(0);
            }
        }
        Long startStamp = null, endStamp = null;
        if (start != null && !start.equals("")) {
            startStamp = DateTool.transferDate(start + " 00:00:00", DateTool.DATE_HOUR_MINUTE_SECOND_FORMAT).getTime();
        }
        if (end != null && !end.equals("")) {
            endStamp = DateTool.transferDate(end + " 23:59:59", DateTool.DATE_HOUR_MINUTE_SECOND_FORMAT).getTime();
        }
        int offset = (page - 1) * pageSize;
        List<ExpirationBean> expirationBeans = new ArrayList<ExpirationBean>();
        for (Expiration expiration : expirationDao.find(type, customer, startStamp, endStamp, offset, pageSize)) {
            expirationBeans.add(new ExpirationBean(expiration, true));
        }
        return Result.WithData(expirationBeans);
    }

}
