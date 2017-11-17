package com.xwkj.customer.dao;

import com.xwkj.common.hibernate.BaseDao;
import com.xwkj.customer.domain.Customer;
import com.xwkj.customer.domain.Expiration;

import java.util.List;

public interface ExpirationDao extends BaseDao<Expiration> {

    /**
     * Find expirations by a customer.
     *
     * @param customer
     * @return
     */
    List<Expiration> findByCustomer(Customer customer);

}
