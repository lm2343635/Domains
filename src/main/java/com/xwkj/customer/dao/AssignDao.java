package com.xwkj.customer.dao;

import com.xwkj.common.hibernate.BaseDao;
import com.xwkj.customer.domain.Assign;
import com.xwkj.customer.domain.Customer;
import com.xwkj.customer.domain.Employee;

import java.util.List;

public interface AssignDao extends BaseDao<Assign> {

    /**
     * Get an assign object for a employee by customer.
     *
     * @param customer
     * @param employee
     * @return
     */
    Assign getByCustomerForEmployee(Customer customer, Employee employee);

    /**
     * Find assigns by customer.
     *
     * @param customer
     * @return
     */
    List<Assign> findByCustomer(Customer customer);

}
