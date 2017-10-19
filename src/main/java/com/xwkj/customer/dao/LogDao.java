package com.xwkj.customer.dao;

import com.xwkj.common.hibernate.BaseDao;
import com.xwkj.customer.domain.Customer;
import com.xwkj.customer.domain.Employee;
import com.xwkj.customer.domain.Log;

import java.util.List;

public interface LogDao extends BaseDao<Log> {

    /**
     * Find logs by customer.
     *
     * @param customer
     * @return
     */
    List<Log> findByCustomer(Customer customer);

    /**
     * Get count for page finding.
     *
     * @param employee
     * @param customer
     * @param title
     * @param start
     * @param end
     * @return
     */
    int getCount(Employee employee, String customer, String title, Long start, Long end);

    /**
     * Find Log by page.
     *
     * @param employee
     * @param customer
     * @param title
     * @param start
     * @param end
     * @param offset
     * @param pageSize
     * @return
     */
    List<Log> find(Employee employee, String customer, String title, Long start, Long end, int offset, int pageSize);

    /**
     * Delete all logs of a customer.
     *
     * @param customer
     * @return
     */
    int deleteByCustomer(Customer customer);

}
