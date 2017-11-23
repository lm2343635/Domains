package com.xwkj.customer.dao;

import com.xwkj.common.hibernate.BaseDao;
import com.xwkj.customer.domain.Customer;
import com.xwkj.customer.domain.Expiration;
import com.xwkj.customer.domain.Type;

import java.util.List;

public interface ExpirationDao extends BaseDao<Expiration> {

    /**
     * Find expirations by a customer.
     *
     * @param customer
     * @return
     */
    List<Expiration> findByCustomer(Customer customer);

    /**
     * Delete all expirations of a customer.
     *
     * @param customer
     * @return
     */
    int deleteByCustomer(Customer customer);

    /**
     * Get count for search.
     *
     * @param type
     * @param customer
     * @param start
     * @param end
     * @return
     */
    int getSearchCount(Type type, String customer, Long start, Long end);

    /**
     * Get sum of money.
     *
     * @param type
     * @param customer
     * @param start
     * @param end
     * @return
     */
    int getMoneyCount(Type type, String customer, Long start, Long end);

    /**
     * Find expiration by page.
     *
     * @param type
     * @param customer
     * @param start
     * @param end
     * @param offset
     * @param pageSize
     * @return
     */
    List<Expiration> find(Type type, String customer, Long start, Long end, int offset, int pageSize);

}
