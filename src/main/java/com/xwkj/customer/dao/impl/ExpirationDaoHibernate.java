package com.xwkj.customer.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.customer.dao.ExpirationDao;
import com.xwkj.customer.domain.Expiration;
import org.springframework.stereotype.Repository;

@Repository
public class ExpirationDaoHibernate extends BaseHibernateDaoSupport<Expiration> implements ExpirationDao {

    public ExpirationDaoHibernate() {
        super();
        setClass(Expiration.class);
    }

}
