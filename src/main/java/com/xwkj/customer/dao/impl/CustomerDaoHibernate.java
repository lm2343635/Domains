package com.xwkj.customer.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.customer.dao.CustomerDao;
import com.xwkj.customer.domain.Area;
import com.xwkj.customer.domain.Customer;
import com.xwkj.customer.domain.Employee;
import com.xwkj.customer.domain.Industry;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class CustomerDaoHibernate extends BaseHibernateDaoSupport<Customer> implements CustomerDao {

    public CustomerDaoHibernate() {
        super();
        setClass(Customer.class);
    }

    public List<Customer> findByState(int state) {
        String hql = "from Customer where state = ? order by updateAt desc";
        return (List<Customer>) getHibernateTemplate().find(hql, state);
    }

    public int getCount(final int state, final String name, final Area area, final Industry industry, final int lower, final int higher) {
        return getHibernateTemplate().execute(new HibernateCallback<Long>() {
            public Long doInHibernate(Session session) throws HibernateException {
                String hql = "select count(*) from Customer where state = ?";
                List<Object> values = new ArrayList<Object>();
                values.add(state);
                if (name != null && !name.equals("")) {
                    hql += " and name like ? ";
                    values.add("%" + name + "%");
                }
                if (area != null) {
                    hql += " and area = ? ";
                    values.add(area);
                }
                if (industry != null) {
                    hql += " and industry = ? ";
                    values.add(industry);
                }
                if (lower > 0) {
                    hql += " and capital >= ? ";
                    values.add(lower);
                }
                if (higher > 0) {
                    hql += " and capital <= ? ";
                    values.add(higher);
                }
                Query query = session.createQuery(hql);
                for (int i = 0; i < values.size(); i++) {
                    query.setParameter(i, values.get(i));
                }
                return (Long) query.uniqueResult();
            }
        }).intValue();
    }

    public List<Customer> find(int state, String name, Area area, Industry industry, int lower, int higher, int offset, int pageSize) {
        String hql = "from Customer where state = ?";
        List<Object> values = new ArrayList<Object>();
        values.add(state);
        if (name != null && !name.equals("")) {
            hql += " and name like ? ";
            values.add("%" + name + "%");
        }
        if (area != null) {
            hql += " and area = ? ";
            values.add(area);
        }
        if (industry != null) {
            hql += " and industry = ? ";
            values.add(industry);
        }
        if (lower > 0) {
            hql += " and capital >= ? ";
            values.add(lower);
        }
        if (higher > 0) {
            hql += " and capital <= ? ";
            values.add(higher);
        }
        hql += " order by createAt desc";
        return findByPage(hql, values, offset, pageSize);
    }

    public int getCreatesCount(final Employee employee) {
        final String hql = "select count(*) from Customer where register = ?";
        return getHibernateTemplate().execute(new HibernateCallback<Long>() {
            public Long doInHibernate(Session session) throws HibernateException {
                Query query = session.createQuery(hql);
                query.setParameter(0, employee);
                return (Long) query.uniqueResult();
            }
        }).intValue();
    }

    public List<Customer> findByRegister(Employee employee, int offset, int pageSize) {
        String hql = "from Customer where register = ? order by createAt desc";
        return findByPage(hql, employee, offset, pageSize);
    }

    public List<Customer> globalSearch(String keyword) {
        String hql = "from Customer where name like ?";
        keyword = "%" + keyword + "%";
        return (List<Customer>)getHibernateTemplate().find(hql, keyword);
    }
}

