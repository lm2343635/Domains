package com.xwkj.customer.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.customer.dao.CustomerDao;
import com.xwkj.customer.domain.Customer;

public class CustomerDaoHibernate extends BaseHibernateDaoSupport<Customer> implements CustomerDao {

    public CustomerDaoHibernate() {
        super();
        setClass(Customer.class);
    }

}
