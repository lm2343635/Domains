package com.xwkj.customer.service;

import com.xwkj.customer.bean.Result;

import javax.servlet.http.HttpSession;

public interface LogManager {

    /**
     * Add a new log for a customer.
     *
     * @param cid
     * @param title
     * @param content
     * @param session
     * @return
     */
    Result add(String cid, String title, String content, HttpSession session);

    /**
     * Get logs by cid.
     *
     * @param cid
     * @param session
     * @return
     */
    Result getByCid(String cid, HttpSession session);

    /**
     * Get a log by iid.
     *
     * @param lid
     * @param session
     * @return
     */
    Result get(String lid, HttpSession session);

    /**
     * Edit a log by log creator.
     *
     * @param lid
     * @param title
     * @param content
     * @param session
     * @return
     */
    Result edit(String lid, String title, String content, HttpSession session);

    /**
     * Remove a log by log creator.
     *
     * @param lid
     * @param session
     * @return
     */
    Result remove(String lid, HttpSession session);

    /**
     * Get search count.
     *
     * @param eid
     * @param customer
     * @param title
     * @param start
     * @param end
     * @param session
     * @return
     */
    Result getSearchCount(String eid, String customer, String title, String start, String end, HttpSession session);

    /**
     * Search Log.
     *
     * @param eid
     * @param customer
     * @param title
     * @param start
     * @param end
     * @param page
     * @param pageSize
     * @param session
     * @return
     */
    Result search(String eid, String customer, String title, String start, String end, int page, int pageSize, HttpSession session);

}
