package com.xwkj.domains.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.domains.dao.DomainDao;
import com.xwkj.domains.domain.Domain;
import org.springframework.stereotype.Repository;

@Repository
public class DomainDaoHibernate extends BaseHibernateDaoSupport<Domain> implements DomainDao {

    public DomainDaoHibernate() {
        super();
        setClass(Domain.class);
    }

}
