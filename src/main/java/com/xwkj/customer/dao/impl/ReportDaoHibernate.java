package com.xwkj.customer.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.customer.dao.ReportDao;
import com.xwkj.customer.domain.Report;
import com.xwkj.customer.domain.Type;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ReportDaoHibernate extends BaseHibernateDaoSupport<Report> implements ReportDao {

    public ReportDaoHibernate() {
        super();
        setClass(Report.class);
    }

    public int getCount(final Type type, final String title, final Long start, final Long end) {
        return getHibernateTemplate().execute(new HibernateCallback<Long>() {
            public Long doInHibernate(Session session) throws HibernateException {
                String hql = "select count(*) from Report where type = ? ";
                List<Object> values = new ArrayList<Object>();
                values.add(type);
                if (title != null && !title.equals("")) {
                    hql += " and title like ?";
                    values.add("%" + title + "%");
                }
                if (start != null) {
                    hql += " and createAt >= ?";
                    values.add(start);
                }
                if (end != null) {
                    hql += " and createAt <= ?";
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

    public List<Report> find(Type type, String title, Long start, Long end, int offset, int pageSize) {
        String hql = "from Report where type = ? ";
        List<Object> values = new ArrayList<Object>();
        values.add(type);
        if (title != null && !title.equals("")) {
            hql += " and title like ?";
            values.add("%" + title + "%");
        }
        if (start != null) {
            hql += " and createAt >= ?";
            values.add(start);
        }
        if (end != null) {
            hql += " and createAt <= ?";
            values.add(end);
        }
        hql += " order by createAt desc";
        return findByPage(hql, values, offset, pageSize);
    }

}
