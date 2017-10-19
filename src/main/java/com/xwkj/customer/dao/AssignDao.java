package com.xwkj.customer.dao;

import com.xwkj.common.hibernate.BaseDao;
import com.xwkj.customer.domain.*;

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

    /**
     *
     * @param employee
     * @param state
     * @param name
     * @param area
     * @param industry
     * @param lower
     * @param higher
     * @return
     */
    int getCountForEmployee(Employee employee, int state, final String name, final Area area, final Industry industry, final int lower, final int higher);

    /**
     * Find assigns by employee.
     *
     * @param employee
     * @return
     */
    List<Assign> findByEmployee(Employee employee, int state, final String name, final Area area, final Industry industry, final int lower, final int higher, int offset, int pageSize);

    /**
     * Delete all assigns of a customer.
     *
     * @param customer
     */
    int deleteByCustomer(Customer customer);

}
