package com.xwkj.customer.service;

import com.xwkj.customer.bean.Result;

import javax.servlet.http.HttpSession;

public interface ReplyManager {

    /**
     * Add a reply for a work.
     *
     * @param wid
     * @param content
     * @param session
     * @return
     */
    Result add(String wid, String content, HttpSession session);


}
