package com.xwkj.customer.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.customer.dao.RoleDao;
import com.xwkj.customer.domain.Role;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDaoHibernate extends BaseHibernateDaoSupport<Role> implements RoleDao {

    public RoleDaoHibernate() {
        super();
        setClass(Role.class);
    }

}
