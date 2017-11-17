package com.xwkj.customer.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.customer.dao.ExpirationDao;
import com.xwkj.customer.domain.Customer;
import com.xwkj.customer.domain.Expiration;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ExpirationDaoHibernate extends BaseHibernateDaoSupport<Expiration> implements ExpirationDao {

    public ExpirationDaoHibernate() {
        super();
        setClass(Expiration.class);
    }

    public List<Expiration> findByCustomer(Customer customer) {
        String hql = "from Expiration where customer = ? order by createAt desc";
        return (List<Expiration>) getHibernateTemplate().find(hql, customer);
    }
}
