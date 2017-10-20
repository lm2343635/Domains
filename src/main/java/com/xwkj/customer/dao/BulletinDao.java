package com.xwkj.customer.dao;

import com.xwkj.common.hibernate.BaseDao;
import com.xwkj.customer.domain.Bulletin;

import java.util.List;

public interface BulletinDao extends BaseDao<Bulletin> {

    /**
     * Find bulletins with limitation.
     *
     * @param limit
     * @return
     */
    List<Bulletin> findWithLimit(int limit);

}
