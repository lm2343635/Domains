package com.xwkj.customer.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.customer.dao.BulletinDao;
import com.xwkj.customer.domain.Bulletin;
import org.springframework.stereotype.Repository;

@Repository
public class BulletinDaoHibernate extends BaseHibernateDaoSupport<Bulletin> implements BulletinDao {

    public BulletinDaoHibernate() {
        super();
        setClass(Bulletin.class);
    }

}
