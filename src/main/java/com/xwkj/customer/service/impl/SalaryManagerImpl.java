package com.xwkj.customer.service.impl;

import com.xwkj.common.util.Debug;
import com.xwkj.customer.bean.Result;
import com.xwkj.customer.bean.SalaryBean;
import com.xwkj.customer.domain.Employee;
import com.xwkj.customer.domain.Salary;
import com.xwkj.customer.service.RoleManager;
import com.xwkj.customer.service.SalaryManager;
import com.xwkj.customer.service.common.ManagerTemplate;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
@RemoteProxy(name = "SalaryManager")
public class SalaryManagerImpl extends ManagerTemplate implements SalaryManager {

    @RemoteMethod
    @Transactional
    public Result add(String eid, String remark, int money, String detail, HttpSession session) {
        Employee creator = getEmployeeFromSession(session);
        if (creator == null) {
            return Result.NoSession();
        }
        if (creator.getRole().getEmployee() != RoleManager.RolePrivilgeHold) {
            return Result.NoPrivilege();
        }
        Employee employee = employeeDao.get(eid);
        if (employee == null) {
            Debug.error("Cannot find an employee by this eid.");
            return Result.WithData(null);
        }
        Salary salary = new Salary();
        salary.setCreateAt(System.currentTimeMillis());
        salary.setRemark(remark);
        salary.setMoney(money);
        salary.setDetail(detail);
        salary.setEmployee(employee);
        return Result.WithData(salaryDao.save(salary));
    }

    @RemoteMethod
    public Result get(String sid, HttpSession session) {
        Employee viewer = getEmployeeFromSession(session);
        if (viewer == null) {
            return Result.NoSession();
        }
        if (viewer.getRole().getEmployee() != RoleManager.RolePrivilgeHold) {
            return Result.NoPrivilege();
        }
        Salary salary = salaryDao.get(sid);
        if (salary == null) {
            Debug.error("Cannot find a salary by this sid.");
            return Result.WithData(null);
        }
        return Result.WithData(new SalaryBean(salary, true));
    }

    @RemoteMethod
    @Transactional
    public Result remove(String sid, HttpSession session) {
        Employee viewer = getEmployeeFromSession(session);
        if (viewer == null) {
            return Result.NoSession();
        }
        if (viewer.getRole().getEmployee() != RoleManager.RolePrivilgeHold) {
            return Result.NoPrivilege();
        }
        Salary salary = salaryDao.get(sid);
        if (salary == null) {
            Debug.error("Cannot find a salary by this sid.");
            return Result.WithData(false);
        }
        salaryDao.delete(salary);
        return Result.WithData(true);
    }

    @RemoteMethod
    public Result getByEid(String eid, HttpSession session) {
        Employee viewer = getEmployeeFromSession(session);
        if (viewer == null) {
            return Result.NoSession();
        }
        Employee employee = employeeDao.get(eid);
        if (employee == null) {
            Debug.error("Cannot find an employee by this eid.");
            return Result.WithData(null);
        }
        if (viewer.getRole().getEmployee() != RoleManager.RolePrivilgeHold && !viewer.equals(employee)) {
            return Result.NoPrivilege();
        }
        List<SalaryBean> salaryBeans = new ArrayList<SalaryBean>();
        for (Salary salary : salaryDao.findByEmployee(employee)) {
            salaryBeans.add(new SalaryBean(salary, false));
        }
        return Result.WithData(salaryBeans);
    }

}
