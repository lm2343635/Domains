package com.xwkj.customer.dao;

import com.xwkj.common.hibernate.BaseDao;
import com.xwkj.customer.domain.Type;

import java.util.List;

public interface TypeDao extends BaseDao<Type> {

    /**
     * Find types by category.
     *
     * @param category
     * @return
     */
    List<Type> findByCategory(int category);

}
