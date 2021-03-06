package com.xwkj.customer.service.impl;

import com.xwkj.common.util.DateTool;
import com.xwkj.common.util.Debug;
import com.xwkj.common.util.FileTool;
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
import java.io.File;
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
        if (employee.getRole().getUndevelopedW() != RoleManager.RolePrivilgeHold) {
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
    public Result getSearchCount(int state, String name, String aid, String iid, int lower, int higher, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        Area area = null;
        if (aid != null && !aid.equals("")) {
            area = areaDao.get(aid);
            if (area == null) {
                Debug.error("Cannot find an area by this aid");
            }
        }
        Industry industry = null;
        if (iid != null && !iid.equals("")) {
            industry = industryDao.get(iid);
            if (industry == null) {
                Debug.error("Cannot find an industry by this iid.");
            }
        }
        int count = 0;
        switch (getPrivilegeForEmployee(employee, state, RoleManager.PrivilegeRead)) {
            case RoleManager.RolePrivilgeHold:
                count = customerDao.getCount(state, name, area, industry, lower, higher);
                break;
            case RoleManager.RolePrivilgeAssign:
                count = assignDao.getCountForEmployee(employee, state, name, area, industry, lower, higher);
                break;
            case RoleManager.RolePrivilgeNone:
                return Result.NoPrivilege();
            default:
                break;
        }
        return Result.WithData(count);
    }

    @RemoteMethod
    public Result search(int state, String name, String aid, String iid, int lower, int higher, int page, int pageSize, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        Area area = null;
        if (aid != null && !aid.equals("")) {
            area = areaDao.get(aid);
            if (area == null) {
                Debug.error("Cannot find an area by this aid");
            }
        }
        Industry industry = null;
        if (iid != null && !iid.equals("")) {
            industry = industryDao.get(iid);
            if (industry == null) {
                Debug.error("Cannot find an industry by this iid.");
            }
        }
        int offset = (page - 1) * pageSize;
        List<CustomerBean> customerBeans = new ArrayList<CustomerBean>();
        switch (getPrivilegeForEmployee(employee, state, RoleManager.PrivilegeRead)) {
            case RoleManager.RolePrivilgeHold:
                for (Customer customer : customerDao.find(state, name, area, industry, lower, higher, offset, pageSize)) {
                    customerBeans.add(new CustomerBean(customer, false));
                }
                break;
            case RoleManager.RolePrivilgeAssign:
                for (Assign assign : assignDao.findByEmployee(employee, state, name, area, industry, lower, higher, offset, pageSize)) {
                    customerBeans.add(new CustomerBean(assign.getCustomer(), false));
                }
                break;
            case RoleManager.RolePrivilgeNone:
                return Result.NoPrivilege();
            default:
                break;
        }
        return Result.WithData(customerBeans);
    }

    @RemoteMethod
    public Result searchForDomain(String name, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        if (employee.getRole().getDomain() != RoleManager.RolePrivilgeHold) {
            return Result.NoPrivilege();
        }
        List<CustomerBean> customerBeans = new ArrayList<CustomerBean>();
        for (Customer customer : customerDao.find(CustomerManager.CustomerStateDeveloped, name, null, null, -1, -1, 0, Integer.MAX_VALUE)) {
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

        CustomerBean customerBean = new CustomerBean(customer, true);
        List<EmployeeBean> managers = new ArrayList<EmployeeBean>();
        for (Assign assign : assignDao.findByCustomer(customer)) {
            managers.add(new EmployeeBean(assign.getEmployee(), true));
        }
        customerBean.setManagers(managers);
        return Result.WithData(customerBean);
    }

    @RemoteMethod
    @Transactional
    public Result edit(String cid, String name, int capital, String contact, String aid, String iid,
                       String items, int money, String remark, String document, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        Customer customer = customerDao.get(cid);
        if (customer == null) {
            Debug.error("Cannot find a customer by this cid.");
            return Result.WithData(false);
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

        customer.setUpdateAt(System.currentTimeMillis());
        customer.setName(name);
        customer.setCapital(capital);
        customer.setContact(contact);
        customer.setRemark(remark);
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
            customer.setDocument(document);
        }
        customerDao.update(customer);
        return Result.WithData(true);
    }

    @RemoteMethod
    @Transactional
    public Result remove(String cid, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        Customer customer = customerDao.get(cid);
        if (customer == null) {
            Debug.error("Cannot find a customer by this cid.");
            return Result.WithData(false);
        }
        switch (getPrivilegeForEmployee(employee, customer.getState(), RoleManager.PrivilegeDelete)) {
            case RoleManager.RolePrivilgeAssign:
                if (!assignDeletePrivilege(customer, employee)) {
                    return Result.NoPrivilege();
                }
                break;
            case RoleManager.RolePrivilgeNone:
                return Result.NoPrivilege();
            default:
                break;
        }
        // Remove all assigns of this customer.
        assignDao.deleteByCustomer(customer);
        // Remove all logs of this customer.
        logDao.deleteByCustomer(customer);
        // Remove all expirations of this customer.
        expirationDao.deleteByCustomer(customer);
        // Remove all documents and folder of this customer.
        documentDao.deleteByCustomer(customer);
        FileTool.delDirectory(getRootPath() + File.separator + configComponent.UploadFolder + File.separator + customer.getCid());
        // Remove the customer.
        customerDao.delete(customer);
        return Result.WithData(true);
    }

    @RemoteMethod
    @Transactional
    public Result develop(String cid, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        if (employee.getRole().getDevelop() != RoleManager.RolePrivilgeHold) {
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
        return Result.WithData(setDevelopingByEmployee(customer, employee));
    }

    @RemoteMethod
    @Transactional
    public Result finish(String cid, String eid, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        if (employee.getRole().getFinish() != RoleManager.RolePrivilgeHold) {
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
            if (employee.getRole().getAssign() != RoleManager.RolePrivilgeHold) {
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
    public Result ruin(String cid, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        if (employee.getRole().getRuin() != RoleManager.RolePrivilgeHold) {
            return Result.NoPrivilege();
        }
        Customer customer = customerDao.get(cid);
        if (customer == null) {
            Debug.error("Cannot find a customer by this cid.");
            return Result.WithData(false);
        }
        if (customer.getState() != CustomerStateDeveloped) {
            return Result.WithData(false);
        }
        // Delete all assigns of this customer.
        assignDao.deleteByCustomer(customer);
        // Delete all expirations of this customer.
        expirationDao.deleteByCustomer(customer);
        // Delete unuseful attributes.
        customer.setItems(null);
        customer.setMoney(null);
        customer.setDocument(null);
        customer.setState(CustomerStateLost);
        customerDao.update(customer);
        return Result.WithData(true);
    }

    @RemoteMethod
    @Transactional
    public Result recover(String cid, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        if (employee.getRole().getRecover() != RoleManager.RolePrivilgeHold) {
            return Result.NoPrivilege();
        }
        Customer customer = customerDao.get(cid);
        if (customer == null) {
            Debug.error("Cannot find a customer by this cid.");
            return Result.WithData(false);
        }
        if (customer.getState() != CustomerStateLost) {
            return Result.WithData(false);
        }
        return Result.WithData(setDevelopingByEmployee(customer, employee));
    }

    @RemoteMethod
    public Result getCreatesCount(String eid, HttpSession session) {
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
        return Result.WithData(customerDao.getCreatesCount(employee));
    }

    @RemoteMethod
    public Result getCreates(String eid, int page, int pageSize, HttpSession session) {
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
        List<CustomerBean> customerBeans = new ArrayList<CustomerBean>();
        for (Customer customer : customerDao.findByRegister(employee, offset, pageSize)) {
            customerBeans.add(new CustomerBean(customer, false));
        }
        return Result.WithData(customerBeans);
    }

    private boolean setDevelopingByEmployee(Customer customer, Employee employee) {
        Assign assign = new Assign();
        assign.setCreateAt(System.currentTimeMillis());
        assign.setR(employee.getRole().getDevelopingR() == RoleManager.RolePrivilgeHold);
        assign.setW(employee.getRole().getDevelopingW() == RoleManager.RolePrivilgeHold);
        assign.setD(employee.getRole().getDevelopingD() == RoleManager.RolePrivilgeHold);
        assign.setAssign(true);
        assign.setCustomer(customer);
        assign.setEmployee(employee);
        if (assignDao.save(assign) == null) {
            return false;
        }
        customer.setState(CustomerStateDeveloping);
        customer.setUpdateAt(System.currentTimeMillis());
        customerDao.update(customer);
        return true;
    }

}
