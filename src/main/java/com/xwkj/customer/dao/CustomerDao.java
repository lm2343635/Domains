package com.xwkj.customer.dao;

import com.xwkj.common.hibernate.BaseDao;
import com.xwkj.customer.domain.Customer;

import java.util.List;

public interface CustomerDao extends BaseDao<Customer> {

    /**
     * Find customer by state.
     *
     * @param state
     * @return
     */
    List<Customer> findByState(int state);

}
