package com.xwkj.customer.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.customer.dao.TypeDao;
import com.xwkj.customer.domain.Type;
import org.springframework.stereotype.Repository;

@Repository
public class TypeDaoHibernate extends BaseHibernateDaoSupport<Type> implements TypeDao {

    public TypeDaoHibernate() {
        super();
        setClass(Type.class);
    }

}
