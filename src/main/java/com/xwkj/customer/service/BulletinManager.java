package com.xwkj.customer.service;

import com.xwkj.customer.bean.Result;

import javax.servlet.http.HttpSession;

public interface BulletinManager {

    /**
     * Add a new bullet.
     *
     * @param content
     * @param session
     * @return
     */
    Result add(String content, HttpSession session);

}
