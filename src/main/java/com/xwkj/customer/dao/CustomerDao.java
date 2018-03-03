package com.xwkj.customer.dao;

import com.xwkj.common.hibernate.BaseDao;
import com.xwkj.customer.domain.Area;
import com.xwkj.customer.domain.Customer;
import com.xwkj.customer.domain.Employee;
import com.xwkj.customer.domain.Industry;

import java.util.List;

public interface CustomerDao extends BaseDao<Customer> {

    /**
     * Get count for page finding.
     *
     * @param state
     * @param name
     * @param area
     * @param industry
     * @param lower
     * @param higher
     * @return
     */
    int getCount(int state, String name, Area area, Industry industry, int lower, int higher);

    /**
     * Find customer by pgae.
     *
     * @param state
     * @param name
     * @param area
     * @param industry
     * @param lower
     * @param higher
     * @param offset
     * @param pageSize
     * @return
     */
    List<Customer> find(int state, String name, Area area, Industry industry, int lower, int higher, int offset, int pageSize);

    /**
     * Get number of customers created by an employee.
     *
     * @param employee
     * @return
     */
    int getCreatesCount(Employee employee);

    /**
     * Find customer by employee.
     *
     * @param employee
     * @return
     */
    List<Customer> findByRegister(Employee employee, int offset, int pageSize);

    /**
     * Global search for Customer.
     *
     * @param keyword
     * @return
     */
    List<Customer> globalSearch(String keyword);

}
