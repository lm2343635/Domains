package com.xwkj.customer.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.customer.dao.EmployeeDao;
import com.xwkj.customer.domain.Employee;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmployeeDaoHibernate extends BaseHibernateDaoSupport<Employee> implements EmployeeDao {

    public EmployeeDaoHibernate() {
        super();
        setClass(Employee.class);
    }

    public Employee getByName(String name) {
        String hql = "from Employee where name = ?";
        List<Employee> employees = (List<Employee>) getHibernateTemplate().find(hql, name);
        if (employees.size() == 0) {
            return null;
        }
        return employees.get(0);
    }

    public List<Employee> findByRolePrivilege(String privilegeName, int privilegeValue) {
        String hql = "from Employee where role." + privilegeName+ " = ?";
        return (List<Employee>) getHibernateTemplate().find(hql, privilegeValue);
    }

    public List<Employee> findByEnable(boolean enable) {
        String hql = "from Employee where enable = ?";
        return (List<Employee>) getHibernateTemplate().find(hql, enable);
    }

}
