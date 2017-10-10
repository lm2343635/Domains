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
                if (employee.getRole().getUndevelopedR() == RoleManager.RolePrivilgeAssign) {
                    if (!assignReadPrivilege(customer, employee)) {
                        return Result.NoPrivilege();
                    }
                } else if (employee.getRole().getUndevelopedR() == RoleManager.RolePrivilgeNone) {
                    return Result.NoPrivilege();
                }
                break;
            case CustomerStateDeveloping:
                if (employee.getRole().getDevelopingR() == RoleManager.RolePrivilgeAssign) {
                    if (!assignReadPrivilege(customer, employee)) {
                        return Result.NoPrivilege();
                    }
                } else if (employee.getRole().getDevelopingR() == RoleManager.RolePrivilgeNone) {
                    return Result.NoPrivilege();
                }
                break;
            case CustomerStateDeveloped:
                if (employee.getRole().getDevelopedR() == RoleManager.RolePrivilgeAssign) {
                    if (!assignReadPrivilege(customer, employee)) {
                        return Result.NoPrivilege();
                    }
                } else if (employee.getRole().getDevelopedR() == RoleManager.RolePrivilgeNone) {
                    return Result.NoPrivilege();
                }
                break;
            case CustomerStateLost:
                if (employee.getRole().getLostR() == RoleManager.RolePrivilgeAssign) {
                    if (!assignReadPrivilege(customer, employee)) {
                        return Result.NoPrivilege();
                    }
                } else if (employee.getRole().getLostR() == RoleManager.RolePrivilgeNone) {
                    return Result.NoPrivilege();
                }
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
                if (employee.getRole().getUndevelopedW() == RoleManager.RolePrivilgeAssign) {
                    if (!assignWritePrivilege(customer, employee)) {
                        return Result.NoPrivilege();
                    }
                } else if (employee.getRole().getUndevelopedW() == RoleManager.RolePrivilgeNone) {
                    return Result.NoPrivilege();
                }
                break;
            case CustomerStateDeveloping:
                if (employee.getRole().getDevelopingW() == RoleManager.RolePrivilgeAssign) {
                    if (!assignWritePrivilege(customer, employee)) {
                        return Result.NoPrivilege();
                    }
                } else if (employee.getRole().getDevelopingW() == RoleManager.RolePrivilgeNone) {
                    return Result.NoPrivilege();
                }
                break;
            case CustomerStateDeveloped:
                if (employee.getRole().getDevelopedW() == RoleManager.RolePrivilgeAssign) {
                    if (!assignWritePrivilege(customer, employee)) {
                        return Result.NoPrivilege();
                    }
                } else if (employee.getRole().getDevelopedW() == RoleManager.RolePrivilgeNone) {
                    return Result.NoPrivilege();
                }
                break;
            case CustomerStateLost:
                if (employee.getRole().getLostW() == RoleManager.RolePrivilgeAssign) {
                    if (!assignWritePrivilege(customer, employee)) {
                        return Result.NoPrivilege();
                    }
                } else if (employee.getRole().getLostW() == RoleManager.RolePrivilgeNone) {
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
            customer.setExpireAt((expireAt.equals("") || expireAt == null) ? null : DateTool.transferDate(expireAt, DateTool.YEAR_MONTH_DATE_FORMAT).getTime());
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
        Assign assign = new Assign();
        assign.setCreateAt(System.currentTimeMillis());
        assign.setR(employee.getRole().getDevelopingR() == RoleManager.RolePrivilgeHold);
        assign.setW(employee.getRole().getDevelopingW() == RoleManager.RolePrivilgeHold);
        assign.setD(employee.getRole().getDevelopingD() == RoleManager.RolePrivilgeHold);
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

    private boolean assignReadPrivilege(Customer customer, Employee employee) {
        Assign assign = assignDao.getByCustomerForEmployee(customer, employee);
        if (assign == null) {
            return false;
        }
        if (assign.getR() == null) {
            return false;
        }
        return true;
    }

    private boolean assignWritePrivilege(Customer customer, Employee employee) {
        Assign assign = assignDao.getByCustomerForEmployee(customer, employee);
        if (assign == null) {
            return false;
        }
        if (assign.getW() == null) {
            return false;
        }
        return true;
    }

    private boolean assignDeletePrivilege(Customer customer, Employee employee) {
        Assign assign = assignDao.getByCustomerForEmployee(customer, employee);
        if (assign == null) {
            return false;
        }
        if (assign.getD() == null) {
            return false;
        }
        return true;
    }

    private int getPrivilegeForEmployee(Employee employee, int state, int type) {
        int privilege = RoleManager.RolePrivilgeNone;
        switch (state) {
            case CustomerStateUndeveloped:
                if (type == RoleManager.PrivilegeRead) {
                    privilege = employee.getRole().getUndevelopedR();
                } else if (type == RoleManager.PrivilegeWrite) {
                    privilege = employee.getRole().getUndevelopedW();
                } else if (type == RoleManager.PrivilegeDelete) {
                    privilege = employee.getRole().getUndevelopedD();
                }
                break;
            case CustomerStateDeveloping:
                if (type == RoleManager.PrivilegeRead) {
                    privilege = employee.getRole().getDevelopingR();
                } else if (type == RoleManager.PrivilegeWrite) {
                    privilege = employee.getRole().getDevelopingW();
                } else if (type == RoleManager.PrivilegeDelete) {
                    privilege = employee.getRole().getDevelopingD();
                }
                break;
            case CustomerStateDeveloped:
                if (type == RoleManager.PrivilegeRead) {
                    privilege = employee.getRole().getDevelopedR();
                } else if (type == RoleManager.PrivilegeWrite) {
                    privilege = employee.getRole().getDevelopedW();
                } else if (type == RoleManager.PrivilegeDelete) {
                    privilege = employee.getRole().getDevelopedD();
                }
                break;
            case CustomerStateLost:
                if (type == RoleManager.PrivilegeRead) {
                    privilege = employee.getRole().getLostR();
                } else if (type == RoleManager.PrivilegeWrite) {
                    privilege = employee.getRole().getLostW();
                } else if (type == RoleManager.PrivilegeDelete) {
                    privilege = employee.getRole().getLostD();
                }
                break;
            default:
                break;
        }
        return privilege;
    }

}
