package com.xwkj.customer.dao;

import com.xwkj.common.hibernate.BaseDao;
import com.xwkj.customer.domain.Report;
import com.xwkj.customer.domain.Type;

import java.util.List;

public interface ReportDao extends BaseDao<Report> {

    /**
     * Get search count.
     *
     * @param type
     * @param title
     * @param start
     * @param end
     * @return
     */
    int getCount(Type type, String title, Long start, Long end);

    /**
     * Find reports by page.
     *
     * @param type
     * @param title
     * @param start
     * @param end
     * @param offset
     * @param pageSize
     * @return
     */
    List<Report> find(Type type, String title, Long start, Long end, int offset, int pageSize);

}
