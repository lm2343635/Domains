package com.xwkj.customer.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.customer.dao.LogDao;
import com.xwkj.customer.domain.Customer;
import com.xwkj.customer.domain.Employee;
import com.xwkj.customer.domain.Log;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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

    public int getCount(final Employee employee, final String customer, final String title, final Long start, final Long end) {
        return getHibernateTemplate().execute(new HibernateCallback<Long>() {
            public Long doInHibernate(Session session) throws HibernateException {
                String hql = "select count(*) from Log where employee = ?";
                List<Object> values = new ArrayList<Object>();
                values.add(employee);
                if (customer != null && !customer.equals("")) {
                    hql += " and customer.name like ? ";
                    values.add("%" + customer + "%");
                }
                if (title != null && !title.equals("")) {
                    hql += " and title like ? ";
                    values.add("%" + title + "%");
                }
                if (start != null) {
                    hql += " and createAt >= ? ";
                    values.add(start);
                }
                if (end != null) {
                    hql += " and createAt <= ? ";
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

    public List<Log> find(Employee employee, String customer, String title, Long start, Long end, int offset, int pageSize) {
        String hql = "from Log where employee = ?";
        List<Object> values = new ArrayList<Object>();
        values.add(employee);
        if (customer != null && !customer.equals("")) {
            hql += " and customer.name like ? ";
            values.add("%" + customer + "%");
        }
        if (title != null && !title.equals("")) {
            hql += " and title like ? ";
            values.add("%" + title + "%");
        }
        if (start != null) {
            hql += " and createAt >= ? ";
            values.add(start);
        }
        if (end != null) {
            hql += " and createAt <= ? ";
            values.add(end);
        }
        hql += " order by createAt desc";
        return findByPage(hql, values, offset, pageSize);
    }

    public int deleteByCustomer(final Customer customer) {
        final String hql = "delete from Log where customer = ?";
        return getHibernateTemplate().execute(new HibernateCallback<Integer>() {
            public Integer doInHibernate(Session session) throws HibernateException {
                Query query = session.createQuery(hql);
                query.setParameter(0, customer);
                return query.executeUpdate();
            }
        });
    }

    public List<Log> findWithLimit(final int limit) {
        final String hql = "from Log order by createAt desc";
        return getHibernateTemplate().execute(new HibernateCallback<List<Log>>() {
            public List<Log> doInHibernate(Session session) throws HibernateException {
                Query query = session.createQuery(hql);
                query.setFirstResult(0);
                query.setMaxResults(limit);
                return query.list();
            }
        });
    }
}
