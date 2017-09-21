package com.xwkj.customer.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.customer.dao.EmployeeDao;
import com.xwkj.customer.domain.Employee;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeeDaoHibernate extends BaseHibernateDaoSupport<Employee> implements EmployeeDao {

    public EmployeeDaoHibernate() {
        super();
        setClass(Employee.class);
    }

}
