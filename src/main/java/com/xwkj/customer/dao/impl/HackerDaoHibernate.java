package com.xwkj.customer.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.customer.dao.HackerDao;
import com.xwkj.customer.domain.Hacker;

public class HackerDaoHibernate extends BaseHibernateDaoSupport<Hacker> implements HackerDao {

    public HackerDaoHibernate() {
        super();
        setClass(Hacker.class);
    }

}
