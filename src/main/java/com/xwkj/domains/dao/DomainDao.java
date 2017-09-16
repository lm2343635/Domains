package com.xwkj.domains.dao;

import com.xwkj.common.hibernate.BaseDao;
import com.xwkj.domains.domain.Domain;
import com.xwkj.domains.domain.Server;

import java.util.List;

public interface DomainDao extends BaseDao<Domain> {

    /**
     * Find all domains of a server.
     *
     * @param server
     * @return
     */
    List<Domain> findByServer(Server server);

    /**
     * Find all highlight domains.
     *
     * @return
     */
    List<Domain> findHighlightDomains();

}
