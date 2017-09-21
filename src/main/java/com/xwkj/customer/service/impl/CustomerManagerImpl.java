package com.xwkj.customer.service.impl;

import com.xwkj.customer.domain.Customer;
import com.xwkj.customer.domain.Employee;
import com.xwkj.customer.service.CustomerManager;
import com.xwkj.customer.service.RoleManager;
import com.xwkj.customer.service.common.ManagerTemplate;
import com.xwkj.customer.bean.Result;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

@Service
@RemoteProxy(name = "CustomerManager")
public class CustomerManagerImpl extends ManagerTemplate implements CustomerManager {

    @RemoteMethod
    @Transactional
    public Result addUndeveloped(String name, int capital, String contact, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        if (employee.getRole().getUndevelopedW() != RoleManager.RolePrevilgeHold) {
            return new Result(true, false);
        }
        Customer customer = new Customer();
        customer.setState(CustomerSataeUndeveloped);
        customer.setCreateAt(System.currentTimeMillis());
        customer.setUpdateAt(customer.getCreateAt());
        customer.setName(name);
        customer.setCapital(capital);
        customer.setContact(contact);
        String cid = customerDao.save(customer);
        if (cid == null) {
            return new Result(true, false);
        }
        return new Result(true, true, cid);
    }

}
