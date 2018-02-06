package com.xwkj.customer.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.customer.dao.CategoryDao;
import com.xwkj.customer.domain.Category;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryDaoHibernate extends BaseHibernateDaoSupport<Category> implements CategoryDao {

    public CategoryDaoHibernate() {
        super();
        setClass(Category.class);
    }

}
