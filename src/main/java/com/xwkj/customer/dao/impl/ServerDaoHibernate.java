package com.xwkj.customer.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.customer.dao.ServerDao;
import com.xwkj.customer.domain.Server;
import org.springframework.stereotype.Repository;

@Repository
public class ServerDaoHibernate extends BaseHibernateDaoSupport<Server> implements ServerDao {

    public ServerDaoHibernate() {
        super();
        setClass(Server.class);
    }

}
