package com.xwkj.customer.service.impl;

import com.xwkj.common.util.DateTool;
import com.xwkj.common.util.Debug;
import com.xwkj.customer.bean.CustomerBean;
import com.xwkj.customer.bean.EmployeeBean;
import com.xwkj.customer.bean.Result;
import com.xwkj.customer.domain.*;
import com.xwkj.customer.service.CustomerManager;
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
@RemoteProxy(name = "CustomerManager")
public class CustomerManagerImpl extends ManagerTemplate implements CustomerManager {

    @RemoteMethod
    @Transactional
    public Result addUndeveloped(String name, int capital, String contact, String aid, String iid, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        if (employee.getRole().getUndevelopedW() != RoleManager.RolePrevilgeHold) {
            return Result.NoPrivilege();
        }
        Area area = areaDao.get(aid);
        if (area == null) {
            Debug.error("Cannot find an area by this aid.");
            return Result.WithData(null);
        }
        Industry industry = industryDao.get(iid);
        if (industry == null) {
            Debug.error("Cannot find an industry by this iid.");
            return Result.WithData(null);
        }
        Customer customer = new Customer();
        customer.setState(CustomerStateUndeveloped);
        customer.setCreateAt(System.currentTimeMillis());
        customer.setUpdateAt(customer.getCreateAt());
        customer.setName(name);
        customer.setCapital(capital);
        customer.setContact(contact);
        customer.setArea(area);
        customer.setIndustry(industry);
        customer.setRegister(employee);
        String cid = customerDao.save(customer);
        if (cid == null) {
            Debug.error("Internal error: save customer failed.");
            return Result.WithData(null);
        }
        area.setCustomers(area.getCustomers() + 1);
        areaDao.update(area);
        industry.setCustomers(industry.getCustomers() + 1);
        industryDao.update(industry);
        return Result.WithData(cid);
    }

    @RemoteMethod
    public Result getByState(int state, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        List<Customer> customers = new ArrayList<Customer>();
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
                if (employee.getRole().getDevelopingR() == RoleManager.RolePrevilgeHold) {
                    customers = customerDao.findByState(state);
                } else if (employee.getRole().getDevelopingR() == RoleManager.RolePrevilgeAssign) {

                } else {
                    return Result.NoPrivilege();
                }
                break;
            case CustomerStateDeveloped:
                if (employee.getRole().getDevelopedR() == RoleManager.RolePrevilgeHold) {
                    customers = customerDao.findByState(state);
                } else if (employee.getRole().getDevelopedR() == RoleManager.RolePrevilgeAssign) {
                    for (Assign assign : assignDao.findByEmployee(employee)) {
                        if (assign.getR()) {
                            customers.add(assign.getCustomer());
                        }
                    }
                } else {
                    return Result.NoPrivilege();
                }
                break;
            case CustomerStateLost:
                if (employee.getRole().getLostR() == RoleManager.RolePrevilgeHold) {
                    customers = customerDao.findByState(state);
                } else if (employee.getRole().getLostR() == RoleManager.RolePrevilgeAssign) {

                } else {
                    return Result.NoPrivilege();
                }
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
        CustomerBean customerBean = new CustomerBean(customer, true);
        List<EmployeeBean> managers = new ArrayList<EmployeeBean>();
        for (Assign assign : assignDao.findByCustomer(customer)) {
            managers.add(new EmployeeBean(assign.getEmployee()));
        }
        customerBean.setManagers(managers);
        return Result.WithData(customerBean);
    }

    @RemoteMethod
    @Transactional
    public Result edit(String cid, String name, int capital, String contact, String aid, String iid,
                       String items, int money, String expireAt, String remark, String document, HttpSession session) {
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
                if (employee.getRole().getDevelopingW() == RoleManager.RolePrevilgeAssign) {

                } else if (employee.getRole().getDevelopingW() == RoleManager.RolePrevilgeNone) {
                    return Result.NoPrivilege();
                }
                break;
            case CustomerStateDeveloped:
                if (employee.getRole().getDevelopedW() == RoleManager.RolePrevilgeAssign) {

                } else if (employee.getRole().getDevelopedW() == RoleManager.RolePrevilgeNone) {
                    return Result.NoPrivilege();
                }
                break;
            case CustomerStateLost:
                if (employee.getRole().getLostW() == RoleManager.RolePrevilgeAssign) {

                } else if (employee.getRole().getLostW() == RoleManager.RolePrevilgeNone) {
                    return Result.NoPrivilege();
                }
                break;
            default:
                break;
        }
        customer.setUpdateAt(System.currentTimeMillis());
        customer.setName(name);
        customer.setCapital(capital);
        customer.setContact(contact);
        if (!customer.getArea().getAid().equals(aid)) {
            Area newArea = areaDao.get(aid);
            if (newArea == null) {
                Debug.error("Cannot find an area by this aid.");
                return Result.WithData(false);
            }
            Area oldArea = customer.getArea();
            oldArea.setCustomers(oldArea.getCustomers() - 1);
            areaDao.update(oldArea);
            newArea.setCustomers(newArea.getCustomers() + 1);
            areaDao.update(newArea);
            customer.setArea(newArea);
        }
        if (!customer.getIndustry().getIid().equals(iid)) {
            Industry newIndustry = industryDao.get(iid);
            if (newIndustry == null) {
                Debug.error("Cannot find an industry by this iid.");
                return Result.WithData(false);
            }
            Industry oldIndustry = customer.getIndustry();
            oldIndustry.setCustomers(oldIndustry.getCustomers() - 1);
            industryDao.update(oldIndustry);
            newIndustry.setCustomers(newIndustry.getCustomers() + 1);
            industryDao.update(newIndustry);
            customer.setIndustry(newIndustry);
        }
        if (customer.getState() == CustomerStateDeveloped) {
            customer.setItems(items);
            customer.setMoney(money);
            customer.setExpireAt((expireAt.equals("") || expireAt == null) ? null : DateTool.transferDate(expireAt, DateTool.YEAR_MONTH_DATE_FORMAT).getTime());
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
        if (employee.getRole().getDevelop() != RoleManager.RolePrevilgeHold) {
            return Result.NoPrivilege();
        }
        Customer customer = customerDao.get(cid);
        if (customer == null) {
            Debug.error("Cannot find a customer by this cid.");
            return Result.WithData(false);
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

    @RemoteMethod
    @Transactional
    public Result finish(String cid, String eid, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        if (employee.getRole().getFinish() != RoleManager.RolePrevilgeHold) {
            return Result.NoPrivilege();
        }
        Customer customer = customerDao.get(cid);
        if (customer == null) {
            Debug.error("Cannot find a customer by this cid.");
            return Result.WithData(false);
        }
        Employee manager = employeeDao.get(eid);
        if (manager == null) {
            Debug.error("Cannot find an employee by this eid");
            return Result.WithData(false);
        }
        if (customer.getState() != CustomerStateDeveloping) {
            return Result.WithData(false);
        }
        Assign assign = assignDao.getByCustomerForEmployee(customer, employee);
        if (assign == null) {
            if (employee.getRole().getAssign() != RoleManager.RolePrevilgeHold) {
                return Result.NoPrivilege();
            } else {
                // If the assign is not the old manager, create a new assign object.
                assign = new Assign();
                assign.setCustomer(customer);
                // Remove old manager.
                for (Assign oldAsign : assignDao.findByCustomer(customer)) {
                    assignDao.delete(oldAsign);
                }
            }
        }
        if (!employee.equals(manager)) {
            assign.setEmployee(manager);
            assign.setCreateAt(System.currentTimeMillis());
        }
        // The first manager of a developed customer should hold all privilges.
        assign.setR(true);
        assign.setW(true);
        assign.setD(true);
        assign.setAssign(true);
        if (assign.getAid() == null) {
            if (assignDao.save(assign) == null) {
                return Result.WithData(false);
            }
        } else {
            assignDao.update(assign);
        }
        customer.setUpdateAt(System.currentTimeMillis());
        customer.setState(CustomerStateDeveloped);
        customerDao.update(customer);
        return Result.WithData(true);
    }

    @RemoteMethod
    @Transactional
    public Result assign(String cid, String eid, boolean r, boolean w, boolean d, boolean a, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        // Deny employees without assign privilege directly.
        if (employee.getRole().getAssign() == RoleManager.RolePrevilgeNone) {
            return Result.NoPrivilege();
        }
        Customer customer = customerDao.get(cid);
        if (customer == null) {
            Debug.error("Cannot find a customer by this cid.");
            return Result.WithData(false);
        }
        if (employee.getRole().getAssign() == RoleManager.RolePrevilgeAssign) {
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
        if (manager.getRole().getDevelopedR() == RoleManager.RolePrevilgeNone ||
                manager.getRole().getDevelopedW() == RoleManager.RolePrevilgeNone ||
                manager.getRole().getDevelopedD() == RoleManager.RolePrevilgeNone) {
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
        if (employee.getRole().getAssign() == RoleManager.RolePrevilgeNone) {
            return Result.NoPrivilege();
        }
        Customer customer = customerDao.get(cid);
        if (customer == null) {
            Debug.error("Cannot find a customer by this cid.");
            return Result.WithData(false);
        }
        if (employee.getRole().getAssign() == RoleManager.RolePrevilgeAssign) {
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

}
