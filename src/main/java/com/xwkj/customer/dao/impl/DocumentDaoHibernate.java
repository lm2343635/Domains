package com.xwkj.customer.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.customer.dao.DocumentDao;
import com.xwkj.customer.domain.Customer;
import com.xwkj.customer.domain.Document;
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

}
