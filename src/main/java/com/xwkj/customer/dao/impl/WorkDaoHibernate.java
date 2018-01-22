package com.xwkj.customer.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.customer.dao.WorkDao;
import com.xwkj.customer.domain.Work;
import org.springframework.stereotype.Repository;

@Repository
public class WorkDaoHibernate extends BaseHibernateDaoSupport<Work> implements WorkDao {

    public WorkDaoHibernate() {
        super();
        setClass(Work.class);
    }

}
