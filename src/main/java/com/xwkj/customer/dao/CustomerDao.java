package com.xwkj.customer.dao;

import com.xwkj.common.hibernate.BaseDao;
import com.xwkj.customer.domain.Area;
import com.xwkj.customer.domain.Customer;
import com.xwkj.customer.domain.Industry;

import java.util.List;

public interface CustomerDao extends BaseDao<Customer> {

    /**
     *
     * @param state
     * @param name
     * @param area
     * @param industry
     * @param lower
     * @param higher
     * @return
     */
    int getCount(int state, String name, Area area, Industry industry, int lower, int higher);

    /**
     *
     * @param state
     * @param name
     * @param area
     * @param industry
     * @param lower
     * @param higher
     * @param offset
     * @param pageSize
     * @return
     */
    List<Customer> find(int state, String name, Area area, Industry industry, int lower, int higher, int offset, int pageSize);

}
