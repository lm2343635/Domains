package com.xwkj.customer.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.customer.dao.BulletinDao;
import com.xwkj.customer.domain.Bulletin;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BulletinDaoHibernate extends BaseHibernateDaoSupport<Bulletin> implements BulletinDao {

    public BulletinDaoHibernate() {
        super();
        setClass(Bulletin.class);
    }

    public List<Bulletin> findWithLimit(final int limit) {
        final String hql = "from Bulletin order by createAt desc";
        return getHibernateTemplate().execute(new HibernateCallback<List<Bulletin>>() {
            public List<Bulletin> doInHibernate(Session session) throws HibernateException {
                Query query = session.createQuery(hql);
                query.setFirstResult(0);
                query.setMaxResults(limit);
                return query.list();
            }
        });
    }

}
