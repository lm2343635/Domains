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
     * Get the latest bulletins, the number of these bulletins is the parameter limit.
     *
     * @param limit
     * @param session
     * @return
     */
    Result getByLimit(int limit, HttpSession session);

}
