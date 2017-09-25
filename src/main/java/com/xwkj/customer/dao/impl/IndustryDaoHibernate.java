package com.xwkj.customer.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.customer.dao.IndustryDao;
import com.xwkj.customer.domain.Industry;
import org.springframework.stereotype.Repository;

@Repository
public class IndustryDaoHibernate extends BaseHibernateDaoSupport<Industry> implements IndustryDao {

    public IndustryDaoHibernate() {
        super();
        setClass(Industry.class);
    }

}
