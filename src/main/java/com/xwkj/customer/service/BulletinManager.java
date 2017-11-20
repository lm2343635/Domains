package com.xwkj.customer.service;

import com.xwkj.customer.bean.Result;

import javax.servlet.http.HttpSession;

public interface BulletinManager {

    /**
     * Add a new bulletin.
     *
     * @param content
     * @param session
     * @return
     */
    Result add(String content, HttpSession session);

    /**
     * Get all top bulletins.
     *
     * @param session
     * @return
     */
    Result getTop(HttpSession session);

    /**
     * Get the number of untop bulletins.
     *
     * @param session
     * @return
     */
    Result getUntopCount(HttpSession session);

    /**
     * Get the untop bulletins by page.
     *
     * @param page
     * @param pageSize
     * @param session
     * @return
     */
    Result getUntopByPage(int page, int pageSize, HttpSession session);

    /**
     * Set a bulletin to the top.
     *
     * @param bid
     * @param top
     * @param session
     * @return
     */
    Result top(String bid, boolean top, HttpSession session);

}
