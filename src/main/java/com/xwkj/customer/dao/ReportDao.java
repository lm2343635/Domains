package com.xwkj.customer.dao;

import com.xwkj.common.hibernate.BaseDao;
import com.xwkj.customer.domain.Report;

import java.util.List;

public interface ReportDao extends BaseDao<Report> {

    /**
     * Get search count.
     *
     * @param title
     * @param start
     * @param end
     * @return
     */
    int getCount(String title, Long start, Long end);

    /**
     * Find reports by page.
     *
     * @param title
     * @param start
     * @param end
     * @param offset
     * @param pageSize
     * @return
     */
    List<Report> find(String title, Long start, Long end, int offset, int pageSize);

}
