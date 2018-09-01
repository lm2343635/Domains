package com.xwkj.customer.dao;

import com.xwkj.common.hibernate.BaseDao;
import com.xwkj.customer.domain.Employee;
import com.xwkj.customer.domain.Work;

import java.util.List;

public interface WorkDao extends BaseDao<Work> {

    /**
     * Get search count.
     * @param active
     * @param title
     * @param sponsor
     * @param executor
     * @param start
     * @param end
     * @return
     */
    int getCount(Boolean active, String title, Employee sponsor, Employee executor, Long start, Long end);

    /**
     * Find works.
     *
     * @param active
     * @param title
     * @param sponsor
     * @param executor
     * @param start
     * @param end
     * @param offset
     * @param pageSize
     * @return
     */
    List<Work> find(Boolean active, String title, Employee sponsor, Employee executor, Long start, Long end, int offset, int pageSize);

}
