package com.xwkj.customer.service;

import javax.servlet.http.HttpSession;

public interface IndustryManager {

    /**
     * Add a new industry.
     *
     * @param name
     * @param session
     * @return
     */
    String add(String name, HttpSession session);

    /**
     * Edit an industry.
     *
     * @param iid
     * @param name
     * @param session
     * @return
     */
    boolean edit(String iid, String name, HttpSession session);

    /**
     * Remove an industry.
     *
     * @param iid
     * @param session
     * @return
     */
    boolean remove(String iid, HttpSession session);

}
