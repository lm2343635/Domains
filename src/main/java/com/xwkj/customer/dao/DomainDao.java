package com.xwkj.customer.dao;

import com.xwkj.common.hibernate.BaseDao;
import com.xwkj.customer.domain.Customer;
import com.xwkj.customer.domain.Domain;
import com.xwkj.customer.domain.Server;

import java.util.List;

public interface DomainDao extends BaseDao<Domain> {

    /**
     * Find all customer of a server.
     *
     * @param server
     * @return
     */
    List<Domain> findByServer(Server server);

    /**
     * Find all highlight customer.
     *
     * @return
     */
    List<Domain> findHighlightDomains();

    /**
     * Find all monitoring domains.
     *
     * @return
     */
    List<Domain> findMonitoring();

    /**
     * Global search for domain.
     *
     * @param keyword
     * @return
     */
    List<Domain> globalSearch(String keyword);

    /**
     * Find domain by customer.
     *
     * @param customer
     * @return
     */
    List<Domain> findByCustomer(Customer customer);

}
