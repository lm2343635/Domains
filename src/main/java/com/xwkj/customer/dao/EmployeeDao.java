package com.xwkj.customer.dao;

import com.xwkj.common.hibernate.BaseDao;
import com.xwkj.customer.domain.Employee;

public interface EmployeeDao extends BaseDao<Employee> {

    /**
     * Find a employee by name.
     *
     * @param name
     * @return
     */
    Employee getByName(String name);

}
