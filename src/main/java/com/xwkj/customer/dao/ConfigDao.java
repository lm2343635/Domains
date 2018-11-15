package com.xwkj.customer.dao;

import com.xwkj.common.hibernate.BaseDao;
import com.xwkj.customer.domain.Config;

import java.util.List;

public interface ConfigDao extends BaseDao<Config> {

    List<Config> findByClazz(String clazz);

}
