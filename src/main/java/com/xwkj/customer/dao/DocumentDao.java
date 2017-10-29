package com.xwkj.customer.dao;

import com.xwkj.common.hibernate.BaseDao;
import com.xwkj.customer.domain.Customer;
import com.xwkj.customer.domain.Document;

import java.util.List;

public interface DocumentDao extends BaseDao<Document> {

    /**
     * Find documents by customer.
     *
     * @param customer
     * @return
     */
    List<Document> findByCustomer(Customer customer);

    /**
     * Delete all documents of a customer.
     *
     * @param customer
     * @return
     */
    int deleteByCustomer(Customer customer);

}
