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
     * Edit a log.
     *
     * @param lid
     * @param title
     * @param content
     * @param session
     * @return
     */
    Result edit(String lid, String title, String content, HttpSession session);

}
