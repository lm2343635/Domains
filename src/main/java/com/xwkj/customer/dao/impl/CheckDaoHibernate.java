package com.xwkj.customer.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.customer.dao.CheckDao;
import com.xwkj.customer.domain.Check;
import org.springframework.stereotype.Repository;

@Repository
public class CheckDaoHibernate extends BaseHibernateDaoSupport<Check> implements CheckDao {

    public CheckDaoHibernate() {
        super();
        setClass(Check.class);
    }

}
