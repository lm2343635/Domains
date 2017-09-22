package com.xwkj.customer.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.customer.dao.CustomerDao;
import com.xwkj.customer.domain.Customer;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomerDaoHibernate extends BaseHibernateDaoSupport<Customer> implements CustomerDao {

    public CustomerDaoHibernate() {
        super();
        setClass(Customer.class);
    }

    public List<Customer> findByState(int state) {
        String hql = "from Customer where state = ? order by updateAt desc";
        return (List<Customer>) getHibernateTemplate().find(hql, state);
    }

}
