package com.xwkj.customer.service;

import com.xwkj.customer.bean.Result;

import javax.servlet.http.HttpSession;

public interface WorkManager {

    /**
     * Create a new work.
     *
     * @param title
     * @param eid
     * @param session
     * @return
     */
    Result add(String title, String eid, HttpSession session);

    /**
     * Close a work.
     *
     * @param wid
     * @param session
     * @return
     */
    Result close(String wid, HttpSession session);

    /**
     * Get a work by wid.
     *
     * @param wid
     * @param session
     * @return
     */
    Result get(String wid, HttpSession session);

    /**
     * Get search count.
     *
     * @param active
     * @param title
     * @param sponsor
     * @param executor
     * @param start
     * @param end
     * @param session
     * @return
     */
    Result getSearchCount(boolean active, String title, String sponsor, String executor, String start, String end, HttpSession session);

    /**
     * Search works.
     * 
     * @param active
     * @param title
     * @param sponsor
     * @param executor
     * @param start
     * @param end
     * @param page
     * @param pageSize
     * @param session
     * @return
     */
    Result search(boolean active, String title, String sponsor, String executor, String start, String end, int page, int pageSize, HttpSession session);

}
