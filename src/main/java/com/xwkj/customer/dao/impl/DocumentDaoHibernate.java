package com.xwkj.customer.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.customer.dao.DocumentDao;
import com.xwkj.customer.domain.Document;
import org.springframework.stereotype.Repository;

@Repository
public class DocumentDaoHibernate extends BaseHibernateDaoSupport<Document> implements DocumentDao {

    public DocumentDaoHibernate() {
        super();
        setClass(Document.class);
    }

}
