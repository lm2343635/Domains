package com.xwkj.customer.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.customer.dao.CheckDao;
import com.xwkj.customer.domain.Check;
import com.xwkj.customer.domain.Domain;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.stereotype.Repository;

@Repository
public class CheckDaoHibernate extends BaseHibernateDaoSupport<Check> implements CheckDao {

    public CheckDaoHibernate() {
        super();
        setClass(Check.class);
    }

    public int deleteByDomain(Domain domain) {
        final String hql = "delete from Check where domain = ?";
        return getHibernateTemplate().execute(new HibernateCallback<Integer>() {
            public Integer doInHibernate(Session session) throws HibernateException {
                Query query = session.createQuery(hql);
                query.setParameter(0, domain);
                return query.executeUpdate();
            }
        }).intValue();
    }

}
