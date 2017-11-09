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
     * Get number of public documents.
     *
     * @param filename
     * @param start
     * @param end
     * @return
     */
    int getPublicCount(String filename, Long start, Long end);

    /**
     * Find all public documents by file name and update date.
     * @param filename
     * @param start
     * @param end
     * @param offset
     * @param pageSize
     * @return
     */
    List<Document> findPublic(String filename, Long start, Long end, int offset, int pageSize);

    /**
     * Find all public documents.
     *
     * @return
     */
    List<Document> findPublicCustomer();

    /**
     * Delete all documents of a customer.
     *
     * @param customer
     * @return
     */
    int deleteByCustomer(Customer customer);

}
