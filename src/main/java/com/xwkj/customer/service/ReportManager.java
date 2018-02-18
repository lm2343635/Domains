package com.xwkj.customer.service;

import com.xwkj.customer.bean.Result;

import javax.servlet.http.HttpSession;

public interface ReportManager {

    /**
     * Add a new report.
     *
     * @param title
     * @param content
     * @param tid
     * @param session
     * @return
     */
    Result add(String title, String tid, String content, HttpSession session);

    /**
     * Get a report.
     *
     * @param rid
     * @param session
     * @return
     */
    Result get(String rid, HttpSession session);

    /**
     * Edit an existing report.
     *
     * @param rid
     * @param tid
     * @param title
     * @param content
     * @param session
     * @return
     */
    Result edit(String rid, String title, String tid, String content, HttpSession session);

    /**
     * Remove an existing report.
     *
     * @param rid
     * @param session
     * @return
     */
    Result remove(String rid, HttpSession session);

    /**
     * Get search count for reports.
     *
     * @param tid
     * @param title
     * @param start
     * @param end
     * @param session
     * @return
     */
    Result getSearchCount(String tid, String title, String start, String end, HttpSession session);

    /**
     * Search reports.
     *
     * @param tid
     * @param title
     * @param start
     * @param end
     * @param page
     * @param pageSize
     * @param session
     * @return
     */
    Result search(String tid, String title, String start, String end, int page, int pageSize, HttpSession session);

}
