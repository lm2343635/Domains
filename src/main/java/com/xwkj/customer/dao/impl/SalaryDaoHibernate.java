package com.xwkj.customer.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.customer.dao.SalaryDao;
import com.xwkj.customer.domain.Employee;
import com.xwkj.customer.domain.Salary;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SalaryDaoHibernate extends BaseHibernateDaoSupport<Salary> implements SalaryDao {

    public SalaryDaoHibernate() {
        super();
        setClass(Salary.class);
    }

    public List<Salary> findByEmployee(Employee employee) {
        String hql = "from Salary where employee = ? order by createAt desc";
        return (List<Salary>) getHibernateTemplate().find(hql, employee);
    }

}
