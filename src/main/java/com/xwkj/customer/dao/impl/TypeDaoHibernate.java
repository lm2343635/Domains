package com.xwkj.customer.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.customer.dao.TypeDao;
import com.xwkj.customer.domain.Type;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TypeDaoHibernate extends BaseHibernateDaoSupport<Type> implements TypeDao {

    public TypeDaoHibernate() {
        super();
        setClass(Type.class);
    }

    public List<Type> findByCategory(int category) {
        String hql = "from Type where category = ? order by createAt";
        return (List<Type>) getHibernateTemplate().find(hql, category);
    }
}
