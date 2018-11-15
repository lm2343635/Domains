package com.xwkj.customer.dao;

import com.xwkj.common.hibernate.BaseDao;
import com.xwkj.customer.domain.Customer;
import com.xwkj.customer.domain.Document;
import com.xwkj.customer.domain.Type;

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
     * @param type
     * @param filename
     * @param start
     * @param end
     * @return
     */
    int getPublicCount(Type type, String filename, Long start, Long end);

    /**
     * Get count by type.
     *
     * @param type
     * @return
     */
    int getCountByType(Type type);

    /**
     * Find all public documents by file name and update date.
     *
     * @param type
     * @param filename
     * @param start
     * @param end
     * @param offset
     * @param pageSize
     * @return
     */
    List<Document> findPublic(Type type, String filename, Long start, Long end, int offset, int pageSize);

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

    /**
     * Find by oss uploaded state.
     * 
     * @param oss
     * @return
     */
    List<Document> findByOSS(boolean oss);

}
