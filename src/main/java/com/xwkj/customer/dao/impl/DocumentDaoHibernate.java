package com.xwkj.customer.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.customer.dao.DocumentDao;
import com.xwkj.customer.domain.Customer;
import com.xwkj.customer.domain.Document;
import com.xwkj.customer.domain.Type;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class DocumentDaoHibernate extends BaseHibernateDaoSupport<Document> implements DocumentDao {

    public DocumentDaoHibernate() {
        super();
        setClass(Document.class);
    }

    public List<Document> findByCustomer(Customer customer) {
        String hql = "from Document where customer = ? order by uploadAt desc";
        return (List<Document>) getHibernateTemplate().find(hql, customer);
    }

    public int getPublicCount(final Type type, final String filename, final Long start, final Long end) {
        return getHibernateTemplate().execute(new HibernateCallback<Long>() {
            public Long doInHibernate(Session session) throws HibernateException {
                String hql = "select count(*) from Document where customer = null and type = ? ";
                List<Object> values = new ArrayList<Object>();
                values.add(type);
                if (filename != null && !filename.equals("")) {
                    hql += " and filename like ?";
                    values.add("%" + filename + "%");
                }
                if (start != null) {
                    hql += " and uploadAt >= ?";
                    values.add(start);
                }
                if (end != null) {
                    hql += " and uploadAt <= ?";
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

    public List<Document> findPublic(final Type type, String filename, Long start, Long end, int offset, int pageSize) {
        String hql = "from Document where customer = null and type = ? ";
        List<Object> values = new ArrayList<Object>();
        values.add(type);
        if (filename != null && !filename.equals("")) {
            hql += " and filename like ?";
            values.add("%" + filename + "%");
        }
        if (start != null) {
            hql += " and uploadAt >= ?";
            values.add(start);
        }
        if (end != null) {
            hql += " and uploadAt <= ?";
            values.add(end);
        }
        hql += " order by uploadAt desc";
        return findByPage(hql, values, offset, pageSize);
    }

    public List<Document> findPublicCustomer() {
        String hql = "from Document where customer = null order by uploadAt desc";
        return (List<Document>) getHibernateTemplate().find(hql);
    }

    public int deleteByCustomer(final Customer customer) {
        final String hql = "delete from Document where customer = ?";
        return getHibernateTemplate().execute(new HibernateCallback<Integer>() {
            public Integer doInHibernate(Session session) throws HibernateException {
                Query query = session.createQuery(hql);
                query.setParameter(0, customer);
                return query.executeUpdate();
            }
        });
    }

}
