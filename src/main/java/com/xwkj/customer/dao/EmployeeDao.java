package com.xwkj.customer.dao;

import com.xwkj.common.hibernate.BaseDao;
import com.xwkj.customer.domain.Employee;

import java.util.List;

public interface EmployeeDao extends BaseDao<Employee> {

    /**
     * Find a employee by name.
     *
     * @param name
     * @return
     */
    Employee getByName(String name);

    /**
     * Find employees by privilege name and value.
     *
     * @param privilegeName
     * @param privilegeValue
     * @return
     */
    List<Employee> findByRolePrivilege(String privilegeName, int privilegeValue);

}
