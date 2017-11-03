package com.xwkj.customer.dao;

import com.xwkj.common.hibernate.BaseDao;
import com.xwkj.customer.domain.Employee;
import com.xwkj.customer.domain.Salary;

import java.util.List;

public interface SalaryDao extends BaseDao<Salary> {

    /**
     * Find salaries by employee.
     *
     * @param employee
     * @return
     */
    List<Salary> findByEmployee(Employee employee);

}
