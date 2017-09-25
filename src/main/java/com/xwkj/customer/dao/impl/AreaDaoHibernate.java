package com.xwkj.customer.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.customer.dao.AreaDao;
import com.xwkj.customer.domain.Area;
import org.springframework.stereotype.Repository;

@Repository
public class AreaDaoHibernate extends BaseHibernateDaoSupport<Area> implements AreaDao {

    public AreaDaoHibernate() {
        super();
        setClass(Area.class);
    }

}
