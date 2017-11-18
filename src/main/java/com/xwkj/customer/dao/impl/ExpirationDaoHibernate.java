package com.xwkj.customer.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.customer.dao.ExpirationDao;
import com.xwkj.customer.domain.Customer;
import com.xwkj.customer.domain.Expiration;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.HibernateCallback;
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

    public int deleteByCustomer(final Customer customer) {
        final String hql = "delete from Expiration where customer = ?";
        return getHibernateTemplate().execute(new HibernateCallback<Integer>() {
            public Integer doInHibernate(Session session) throws HibernateException {
                Query query = session.createQuery(hql);
                query.setParameter(0, customer);
                return query.executeUpdate();
            }
        });
    }

}
