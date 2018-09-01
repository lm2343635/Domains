package com.xwkj.customer.dao;

import com.xwkj.common.hibernate.BaseDao;
import com.xwkj.customer.domain.Check;
import com.xwkj.customer.domain.Domain;

public interface CheckDao extends BaseDao<Check> {

    /**
     * Delete by domain.
     * 
     * @param domain
     * @return
     */
    int deleteByDomain(Domain domain);

}
