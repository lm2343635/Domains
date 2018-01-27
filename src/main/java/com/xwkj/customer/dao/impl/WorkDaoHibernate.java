package com.xwkj.customer.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.customer.dao.WorkDao;
import com.xwkj.customer.domain.Employee;
import com.xwkj.customer.domain.Work;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class WorkDaoHibernate extends BaseHibernateDaoSupport<Work> implements WorkDao {

    public WorkDaoHibernate() {
        super();
        setClass(Work.class);
    }

    public int getCount(final Boolean active, final String title, final Employee sponsor, final Employee executor, final Long start, final Long end) {
        return getHibernateTemplate().execute(new HibernateCallback<Long>() {
            public Long doInHibernate(Session session) throws HibernateException {
                String hql = "select count(*) from Work where active = ?";
                List<Object> values = new ArrayList<Object>();
                values.add(active);
                if (title != null && !title.equals("")) {
                    hql += " and title like ? ";
                    values.add("%" + title + "%");
                }
                if (sponsor != null) {
                    hql += " and sponsor = ? ";
                    values.add(sponsor);
                }
                if (executor != null) {
                    hql += " and executor = ? ";
                    values.add(executor);
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

    public List<Work> find(Boolean active, String title, Employee sponsor, Employee executor, Long start, Long end, int offset, int pageSize) {
        String hql = "from Work where active = ?";
        List<Object> values = new ArrayList<Object>();
        values.add(active);
        if (title != null && !title.equals("")) {
            hql += " and title like ? ";
            values.add("%" + title + "%");
        }
        if (sponsor != null) {
            hql += " and sponsor = ? ";
            values.add(sponsor);
        }
        if (executor != null) {
            hql += " and executor = ? ";
            values.add(executor);
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

}
