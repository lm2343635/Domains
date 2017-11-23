package com.xwkj.customer.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.customer.dao.ExpirationDao;
import com.xwkj.customer.domain.Customer;
import com.xwkj.customer.domain.Expiration;
import com.xwkj.customer.domain.Type;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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

    public int getSearchCount(Type type, String customer, Long start, Long end) {
        return getCount("count(*)", type, customer, start, end);
    }

    public int getMoneyCount(Type type, String customer, Long start, Long end) {
        return getCount("sum(money)", type, customer, start, end);
    }

    public List<Expiration> find(Type type, String customer, Long start, Long end, int offset, int pageSize) {
        String hql = "from Expiration where true = true";
        List<Object> values = new ArrayList<Object>();
        if (type != null) {
            hql += " and type = ? ";
            values.add(type);
        }
        if (customer != null && !customer.equals("")) {
            hql += " and customer.name like ? ";
            values.add("%" + customer + "%");
        }
        if (start != null) {
            hql += " and expireAt >= ? ";
            values.add(start);
        }
        if (end != null) {
            hql += " and expireAt <= ? ";
            values.add(end);
        }
        hql += " order by expireAt";
        return findByPage(hql, values, offset, pageSize);
    }

    private int getCount(final String function, final Type type, final String customer, final Long start, final Long end) {
        return getHibernateTemplate().execute(new HibernateCallback<Long>() {
            public Long doInHibernate(Session session) throws HibernateException {
                String hql = "select " + function + " from Expiration where true = true";
                List<Object> values = new ArrayList<Object>();
                if (type != null) {
                    hql += " and type = ? ";
                    values.add(type);
                }
                if (customer != null && !customer.equals("")) {
                    hql += " and customer.name like ? ";
                    values.add("%" + customer + "%");
                }
                if (start != null) {
                    hql += " and expireAt >= ? ";
                    values.add(start);
                }
                if (end != null) {
                    hql += " and expireAt <= ? ";
                    values.add(end);
                }
                Query query = session.createQuery(hql);
                for (int i = 0; i < values.size(); i++) {
                    query.setParameter(i, values.get(i));
                }
                return (Long) query.uniqueResult();
            }
        }).intValue();
    }

}
