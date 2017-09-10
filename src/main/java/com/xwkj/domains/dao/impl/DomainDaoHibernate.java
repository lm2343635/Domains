package com.xwkj.domains.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.domains.dao.DomainDao;
import com.xwkj.domains.domain.Domain;
import com.xwkj.domains.domain.Server;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DomainDaoHibernate extends BaseHibernateDaoSupport<Domain> implements DomainDao {

    public DomainDaoHibernate() {
        super();
        setClass(Domain.class);
    }

    public List<Domain> findByServer(Server server) {
        String hql = "from Domain where server = ? order by createAt desc";
        return (List<Domain>) getHibernateTemplate().find(hql, server);
    }

}
