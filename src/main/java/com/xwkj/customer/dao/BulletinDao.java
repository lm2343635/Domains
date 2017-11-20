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

    /**
     * Find all top bulletins.
     *
     * @return
     */
    List<Bulletin> findTop();

    /**
     * Get the number of untop bulletins.
     *
     * @return
     */
    int getUntopCount();

    /**
     * Find untop bulletins by page.
     *
     * @param offset
     * @param pageSize
     * @return
     */
    List<Bulletin> findUntop(int offset, int pageSize);

}
