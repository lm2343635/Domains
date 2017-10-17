package com.xwkj.common.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;

import java.io.Serializable;
import java.util.List;

public abstract class BaseHibernateDaoSupport<T extends Serializable> extends HibernateDaoSupport implements BaseDao<T> {

    @Autowired
    public void init(SessionFactory sessionFactory) {
        setSessionFactory(sessionFactory);
    }

    private Class<T> clazz;

    protected final void setClass(final Class<T> clazzToSet) {
        this.clazz = clazzToSet;
    }

    public final T get(String id) {
        return getHibernateTemplate().get(clazz, id);
    }

    public String save(T entity) {
        String id = (String) getHibernateTemplate().save(entity);
        getHibernateTemplate().flush();
        return id;
    }

    public void update(T entity) {
        getHibernateTemplate().update(entity);
        getHibernateTemplate().flush();
    }

    public void delete(T entity) {
        getHibernateTemplate().delete(entity);
        getHibernateTemplate().flush();
    }

    public void delete(String id) {
        delete(get(id));
    }

    public List<T> findAll() {
        return (List<T>) getHibernateTemplate().find("from " + clazz.getName());
    }

    public List<T> findAll(String orderby, boolean desc) {
        String hql = "from " + clazz.getName() + " order by " + orderby;
        if (desc) {
            hql += " desc";
        }
        return (List<T>) getHibernateTemplate().find(hql);
    }

    /**
     * Find by page using hql
     *
     * @param hql
     * @param offset
     * @param pageSize
     * @return
     */
    public List findByPage(final String hql, final int offset, final int pageSize) {
        List list = getHibernateTemplate().execute(new HibernateCallback<List>() {
            public List doInHibernate(Session session) throws HibernateException {
                List result = session.createQuery(hql)
                        .setFirstResult(offset)
                        .setMaxResults(pageSize)
                        .list();
                return result;
            }
        });
        return list;
    }

    /**
     * Find by page using hql with a signle parameter.
     *
     * @param hql
     * @param value
     * @param offset
     * @param pageSize
     * @return
     */
    public List findByPage(final String hql, final Object value, final int offset, final int pageSize) {
        List list = getHibernateTemplate().execute(new HibernateCallback<List>() {
            public List doInHibernate(Session session) throws HibernateException {
                List result = session.createQuery(hql)
                        .setParameter(0, value)
                        .setFirstResult(offset)
                        .setMaxResults(pageSize)
                        .list();
                return result;
            }
        });
        return list;
    }

    /**
     * Find by page using hql with multiple parameters.
     *
     * @param hql
     * @param values
     * @param offset
     * @param pageSize
     * @return
     */
    public List findByPage(final String hql, final List<Object> values, final int offset, final int pageSize) {
        List list = getHibernateTemplate().execute(new HibernateCallback<List>() {
            public List doInHibernate(Session session) throws HibernateException {
                Query query = session.createQuery(hql);
                for (int i = 0; i < values.size(); i++) {
                    query.setParameter(i, values.get(i));
                }
                List result = query.setFirstResult(offset).setMaxResults(pageSize).list();
                return result;
            }
        });
        return list;
    }

}