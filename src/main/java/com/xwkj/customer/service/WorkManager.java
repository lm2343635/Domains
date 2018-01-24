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

}
