package com.xwkj.customer.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.customer.dao.ConfigDao;
import com.xwkj.customer.domain.Config;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ConfigDaoHibernate extends BaseHibernateDaoSupport<Config> implements ConfigDao {

    public ConfigDaoHibernate() {
        super();
        setClass(Config.class);
    }

    @Override
    public List<Config> findByClazz(String clazz) {
        String hql = "from Config where clazz = ?";
        return (List<Config>) getHibernateTemplate().find(hql, clazz);
    }
}
