package com.xwkj.customer.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.customer.dao.DomainDao;
import com.xwkj.customer.domain.Customer;
import com.xwkj.customer.domain.Domain;
import com.xwkj.customer.domain.Server;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DomainDaoHibernate extends BaseHibernateDaoSupport<Domain> implements DomainDao {

    public DomainDaoHibernate() {
        super();
        setClass(Domain.class);
    }

    public List<Domain> findByServer(Server server) {
        String hql = "from Domain where server = ? order by createAt desc";
        return (List<Domain>) getHibernateTemplate().find(hql, server);
    }

    public List<Domain> findHighlightDomains() {
        String hql = "from Domain where highlight = true order by updateAt desc";
        return (List<Domain>) getHibernateTemplate().find(hql);
    }

    public List<Domain> findMonitoring() {
        String hql = "from Domain where monitoring = true order by updateAt desc";
        return (List<Domain>) getHibernateTemplate().find(hql);
    }

    public List<Domain> globalSearch(String keyword) {
        String hql = "from Domain where name like ? or domains like ?";
        keyword = "%" + keyword + "%";
        return (List<Domain>)getHibernateTemplate().find(hql, keyword, keyword);
    }

    public List<Domain> findByCustomer(Customer customer) {
        String hql = "from Domain where customer = ? order by createAt";
        return (List<Domain>) getHibernateTemplate().find(hql, customer);
    }

}
