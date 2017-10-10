package com.xwkj.customer.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.customer.dao.LogDao;
import com.xwkj.customer.domain.Customer;
import com.xwkj.customer.domain.Log;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LogDaoHibernate extends BaseHibernateDaoSupport<Log> implements LogDao {

    public LogDaoHibernate() {
        super();
        setClass(Log.class);
    }

    public List<Log> findByCustomer(Customer customer) {
        String hql = "from Log where customer = ? order by updateAt desc";
        return (List<Log>) getHibernateTemplate().find(hql, customer);
    }

}
