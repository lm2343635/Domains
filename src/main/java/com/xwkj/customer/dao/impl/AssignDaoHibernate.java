package com.xwkj.customer.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.customer.dao.AssignDao;
import com.xwkj.customer.domain.Assign;
import com.xwkj.customer.domain.Customer;
import com.xwkj.customer.domain.Employee;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AssignDaoHibernate extends BaseHibernateDaoSupport<Assign> implements AssignDao {

    public AssignDaoHibernate() {
        super();
        setClass(Assign.class);
    }

    public Assign getByCustomerForEmployee(Customer customer, Employee employee) {
        String hql = "from Assign where customer = ? and employee = ?";
        List<Assign> assigns = (List<Assign>) getHibernateTemplate().find(hql, customer, employee);
        if (assigns.size() == 0) {
            return null;
        }
        return assigns.get(0);
    }

    public List<Assign> findByCustomer(Customer customer) {
        String hql = "from Assign where customer = ? order by createAt";
        return (List<Assign>) getHibernateTemplate().find(hql, customer);
    }

    public List<Assign> findByEmployee(Employee employee) {
        String hql = "from Assign where employee = ? order by createAt";
        return (List<Assign>) getHibernateTemplate().find(hql, employee);
    }

}
