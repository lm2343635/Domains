package com.xwkj.customer.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.customer.dao.DocumentDao;
import com.xwkj.customer.domain.Customer;
import com.xwkj.customer.domain.Document;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.stereotype.Repository;

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
