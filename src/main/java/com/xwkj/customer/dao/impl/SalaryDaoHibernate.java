package com.xwkj.customer.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.customer.dao.SalaryDao;
import com.xwkj.customer.domain.Salary;
import org.springframework.stereotype.Repository;

@Repository
public class SalaryDaoHibernate extends BaseHibernateDaoSupport<Salary> implements SalaryDao {

    public SalaryDaoHibernate() {
        super();
        setClass(Salary.class);
    }

}
