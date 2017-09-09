package com.xwkj.domains.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.domains.dao.ServerDao;
import com.xwkj.domains.domain.Server;
import org.springframework.stereotype.Repository;

@Repository
public class ServerDaoHibernate extends BaseHibernateDaoSupport<Server> implements ServerDao {

    public ServerDaoHibernate() {
        super();
        setClass(Server.class);
    }

}
