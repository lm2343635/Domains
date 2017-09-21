package com.xwkj.customer.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.customer.dao.LogDao;
import com.xwkj.customer.domain.Log;
import org.springframework.stereotype.Repository;

@Repository
public class LogDaoHibernate extends BaseHibernateDaoSupport<Log> implements LogDao {

    public LogDaoHibernate() {
        super();
        setClass(Log.class);
    }

}
