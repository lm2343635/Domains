package com.xwkj.customer.service.impl;

import com.xwkj.common.util.Debug;
import com.xwkj.customer.bean.CustomerBean;
import com.xwkj.customer.domain.Assign;
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
import java.util.ArrayList;
import java.util.List;

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
        customer.setState(CustomerStateUndeveloped);
        customer.setCreateAt(System.currentTimeMillis());
        customer.setUpdateAt(customer.getCreateAt());
        customer.setName(name);
        customer.setCapital(capital);
        customer.setContact(contact);
        String cid = customerDao.save(customer);
        if (cid == null) {
            Debug.error("Internal error: save customer failed.");
            return Result.NoPrivilege();
        }
        return Result.WithData(cid);
    }

    @RemoteMethod
    public Result getByState(int state, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        List<Customer> customers = null;
        switch (state) {
            case CustomerStateUndeveloped:
                if (employee.getRole().getUndevelopedR() == RoleManager.RolePrevilgeHold) {
                    customers = customerDao.findByState(state);
                } else if (employee.getRole().getUndevelopedR() == RoleManager.RolePrevilgeAssign) {

                } else {
                    return Result.NoPrivilege();
                }
                break;
            case CustomerStateDeveloping:

                break;
            case CustomerStateDeveloped:

                break;
            case CustomerStateLost:
                break;
            default:
                break;
        }
        List<CustomerBean> customerBeans = new ArrayList<CustomerBean>();
        for (Customer customer : customers) {
            customerBeans.add(new CustomerBean(customer, false));
        }
        return Result.WithData(customerBeans);
    }

    @RemoteMethod
    public Result get(String cid, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        Customer customer = customerDao.get(cid);
        if (customer == null) {
            Debug.error("Cannot find a customer by this cid.");
            return Result.NoPrivilege();
        }
        switch (customer.getState()) {
            case CustomerStateUndeveloped:
                if (employee.getRole().getUndevelopedR() == RoleManager.RolePrevilgeAssign) {

                } else if (employee.getRole().getUndevelopedR() == RoleManager.RolePrevilgeNone) {
                    return Result.NoPrivilege();
                }
                break;
            case CustomerStateDeveloping:

                break;
            case CustomerStateDeveloped:

                break;
            case CustomerStateLost:
                break;
            default:
                break;
        }
        return Result.WithData(new CustomerBean(customer, true));
    }

    @RemoteMethod
    @Transactional
    public Result edit(String cid, String name, int capital, String contact, String items, int money, String remark, String document, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        Customer customer = customerDao.get(cid);
        if (customer == null) {
            Debug.error("Cannot find a customer by this cid.");
            return Result.NoPrivilege();
        }
        switch (customer.getState()) {
            case CustomerStateUndeveloped:
                if (employee.getRole().getUndevelopedW() == RoleManager.RolePrevilgeAssign) {

                } else if (employee.getRole().getUndevelopedW() == RoleManager.RolePrevilgeNone) {
                    return Result.NoPrivilege();
                }
                break;
            case CustomerStateDeveloping:

                break;
            case CustomerStateDeveloped:

                break;
            case CustomerStateLost:
                break;
            default:
                break;
        }
        customer.setUpdateAt(System.currentTimeMillis());
        customer.setName(name);
        customer.setCapital(capital);
        customer.setContact(contact);
        if (customer.getState() == CustomerStateDeveloped) {
            customer.setItems(items);
            customer.setMoney(money);
            customer.setRemark(remark);
            customer.setDocument(document);
        }
        customerDao.update(customer);
        return Result.WithData(true);
    }

    @RemoteMethod
    @Transactional
    public Result develop(String cid, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        Customer customer = customerDao.get(cid);
        if (customer == null) {
            Debug.error("Cannot find a customer by this cid.");
            return Result.NoPrivilege();
        }
        if (employee.getRole().getDevelop() != RoleManager.RolePrevilgeHold) {
            return Result.NoPrivilege();
        }
        if (customer.getState() != CustomerStateUndeveloped) {
            return Result.WithData(false);
        }
        Assign assign = new Assign();
        assign.setCreateAt(System.currentTimeMillis());
        assign.setR(employee.getRole().getDevelopingR() == RoleManager.RolePrevilgeHold);
        assign.setW(employee.getRole().getDevelopingW() == RoleManager.RolePrevilgeHold);
        assign.setD(employee.getRole().getDevelopingD() == RoleManager.RolePrevilgeHold);
        assign.setAssign(true);
        assign.setCustomer(customer);
        assign.setEmployee(employee);
        if (assignDao.save(assign) == null) {
            return Result.WithData(false);
        }
        customer.setState(CustomerStateDeveloping);
        customer.setUpdateAt(System.currentTimeMillis());
        customerDao.update(customer);
        return Result.WithData(true);
    }

}
