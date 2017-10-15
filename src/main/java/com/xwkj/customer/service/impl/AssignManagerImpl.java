package com.xwkj.customer.service.impl;

import com.xwkj.common.util.Debug;
import com.xwkj.customer.bean.AssignBean;
import com.xwkj.customer.bean.Result;
import com.xwkj.customer.domain.*;
import com.xwkj.customer.service.AssignManager;
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
@RemoteProxy(name = "AssignManager")
public class AssignManagerImpl extends ManagerTemplate implements AssignManager {

    @RemoteMethod
    @Transactional
    public Result assign(String cid, String eid, boolean r, boolean w, boolean d, boolean a, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        // Deny employees without assign privilege directly.
        if (employee.getRole().getAssign() == RoleManager.RolePrivilgeNone) {
            return Result.NoPrivilege();
        }
        Customer customer = customerDao.get(cid);
        if (customer == null) {
            Debug.error("Cannot find a customer by this cid.");
            return Result.WithData(false);
        }
        if (employee.getRole().getAssign() == RoleManager.RolePrivilgeAssign) {
            Assign assign = assignDao.getByCustomerForEmployee(customer, employee);
            if (assign == null) {
                return Result.NoPrivilege();
            }
            if (!assign.getAssign()) {
                return Result.NoPrivilege();
            }
        }
        Employee manager = employeeDao.get(eid);
        if (manager == null) {
            Debug.error("Cannot find a employee by this eid.");
            return Result.WithData(false);
        }
        // New employee do not have privilege to be a manager of developed customer.
        if (manager.getRole().getDevelopedR() == RoleManager.RolePrivilgeNone ||
                manager.getRole().getDevelopedW() == RoleManager.RolePrivilgeNone ||
                manager.getRole().getDevelopedD() == RoleManager.RolePrivilgeNone) {
            return Result.WithData(false);
        }
        Assign assign = new Assign();
        assign.setCreateAt(System.currentTimeMillis());
        assign.setCustomer(customer);
        assign.setEmployee(manager);
        assign.setR(r);
        assign.setW(w);
        assign.setD(d);
        assign.setAssign(a);
        if (assignDao.save(assign) == null) {
            Result.WithData(false);
        }
        return Result.WithData(true);
    }

    @RemoteMethod
    @Transactional
    public Result revokeAssign(String cid, String eid, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        // Deny employees without assign privilege directly.
        if (employee.getRole().getAssign() == RoleManager.RolePrivilgeNone) {
            return Result.NoPrivilege();
        }
        Customer customer = customerDao.get(cid);
        if (customer == null) {
            Debug.error("Cannot find a customer by this cid.");
            return Result.WithData(false);
        }
        if (employee.getRole().getAssign() == RoleManager.RolePrivilgeAssign) {
            Assign assign = assignDao.getByCustomerForEmployee(customer, employee);
            if (assign == null) {
                return Result.NoPrivilege();
            }
            if (!assign.getAssign()) {
                return Result.NoPrivilege();
            }
        }

        Employee manager = employeeDao.get(eid);
        if (manager == null) {
            Debug.error("Cannot find a employee by this eid.");
            return Result.WithData(false);
        }
        Assign assign = assignDao.getByCustomerForEmployee(customer, manager);
        if (assign == null) {
            return Result.WithData(false);
        }
        assignDao.delete(assign);
        return Result.WithData(true);
    }

    public Result getSearchCount(String eid, int state, String name, Area area, Industry industry, int lower, int higher, HttpSession session) {
        Employee viwer = getEmployeeFromSession(session);
        if (viwer == null) {
            return Result.NoSession();
        }
        Employee employee = employeeDao.get(eid);
        if (employee == null) {
            Debug.error("Cannot find an employee by this eid.");
            return Result.WithData(null);
        }
        if (viwer.getRole().getEmployee() != RoleManager.RolePrivilgeHold && !viwer.equals(employee)) {
            return Result.NoPrivilege();
        }
        return Result.WithData(assignDao.getCountForEmployee(employee, state, name, area, industry, lower, higher));
    }

    public Result search(String eid, int state, String name, Area area, Industry industry, int lower, int higher, int page, int pageSize, HttpSession session) {
        Employee viwer = getEmployeeFromSession(session);
        if (viwer == null) {
            return Result.NoSession();
        }
        Employee employee = employeeDao.get(eid);
        if (employee == null) {
            Debug.error("Cannot find an employee by this eid.");
            return Result.WithData(null);
        }
        if (viwer.getRole().getEmployee() != RoleManager.RolePrivilgeHold && !viwer.equals(employee)) {
            return Result.NoPrivilege();
        }
        int offset = (page - 1) * pageSize;
        List<AssignBean> assignBeans = new ArrayList<AssignBean>();
        for (Assign assign : assignDao.findByEmployee(employee, state, name, area, industry, lower, higher, offset, pageSize)) {
            assignBeans.add(new AssignBean(assign));
        }
        return Result.WithData(assignBeans);
    }
    
}
