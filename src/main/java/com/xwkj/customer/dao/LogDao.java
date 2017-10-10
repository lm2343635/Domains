package com.xwkj.customer.dao;

import com.xwkj.common.hibernate.BaseDao;
import com.xwkj.customer.domain.Customer;
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

}
