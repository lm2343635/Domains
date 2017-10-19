package com.xwkj.customer.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.customer.dao.AssignDao;
import com.xwkj.customer.domain.*;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AssignDaoHibernate extends BaseHibernateDaoSupport<Assign> implements AssignDao {

    public AssignDaoHibernate() {
        super();
        setClass(Assign.class);
    }

    public Assign getByCustomerForEmployee(Customer customer, Employee employee) {
        String hql = "from Assign where customer = ? and employee = ?";
        List<Assign> assigns = (List<Assign>) getHibernateTemplate().find(hql, customer, employee);
        if (assigns.size() == 0) {
            return null;
        }
        return assigns.get(0);
    }

    public List<Assign> findByCustomer(Customer customer) {
        String hql = "from Assign where customer = ? order by createAt";
        return (List<Assign>) getHibernateTemplate().find(hql, customer);
    }

    public List<Assign> findByEmployee(Employee employee) {
        String hql = "from Assign where employee = ? order by createAt";
        return (List<Assign>) getHibernateTemplate().find(hql, employee);
    }

    public int getCountForEmployee(final Employee employee, final int state, final String name, final Area area, final Industry industry, final int lower, final int higher) {
        return getHibernateTemplate().execute(new HibernateCallback<Long>() {
            public Long doInHibernate(Session session) throws HibernateException {
                String hql = "select count(*) from Assign where employee = ? and customer.state = ?";
                List<Object> values = new ArrayList<Object>();
                values.add(employee);
                values.add(state);
                if (name != null && !name.equals("")) {
                    hql += " and customer.name like ? ";
                    values.add("%" + name + "%");
                }
                if (area != null) {
                    hql += " and customer.area = ? ";
                    values.add(area);
                }
                if (industry != null) {
                    hql += " and customer.industry = ? ";
                    values.add(industry);
                }
                if (lower > 0) {
                    hql += " and customer.capital >= ? ";
                    values.add(lower);
                }
                if (higher > 0) {
                    hql += " and customer.capital <= ? ";
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

    public List<Assign> findByEmployee(Employee employee, int state, String name, Area area, Industry industry, int lower, int higher, int offset, int pageSize) {
        String hql = "from Assign where employee = ? and customer.state = ?";
        List<Object> values = new ArrayList<Object>();
        values.add(employee);
        values.add(state);
        if (name != null && !name.equals("")) {
            hql += " and customer.name like ? ";
            values.add("%" + name + "%");
        }
        if (area != null) {
            hql += " and customer.area = ? ";
            values.add(area);
        }
        if (industry != null) {
            hql += " and customer.industry = ? ";
            values.add(industry);
        }
        if (lower > 0) {
            hql += " and customer.capital >= ? ";
            values.add(lower);
        }
        if (higher > 0) {
            hql += " and customer.capital <= ? ";
            values.add(higher);
        }
        hql += "order by customer.createAt desc";
        return findByPage(hql, values, offset, pageSize);
    }

    public int deleteByCustomer(final Customer customer) {
        final String hql = "delete from Assign where customer = ?";
        return getHibernateTemplate().execute(new HibernateCallback<Integer>() {
            public Integer doInHibernate(Session session) throws HibernateException {
                Query query = session.createQuery(hql);
                query.setParameter(0, customer);
                return query.executeUpdate();
            }
        });
    }

}
