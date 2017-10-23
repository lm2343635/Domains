package com.xwkj.customer.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.customer.dao.ReportDao;
import com.xwkj.customer.domain.Report;
import org.springframework.stereotype.Repository;

@Repository
public class ReportDaoHibernate extends BaseHibernateDaoSupport<Report> implements ReportDao {

    public ReportDaoHibernate() {
        super();
        setClass(Report.class);
    }

}
