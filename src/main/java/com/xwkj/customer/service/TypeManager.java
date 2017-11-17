package com.xwkj.customer.service;

import com.xwkj.customer.bean.Result;

import javax.servlet.http.HttpSession;

public interface TypeManager {

    /**
     * Add a new type for expiration.
     *
     * @param name
     * @param session
     * @return
     */
    Result add(String name, HttpSession session);

    /**
     * Get all types.
     *
     * @param session
     * @return
     */
    Result getAll(HttpSession session);

    /**
     * Remove an existing type.
     *
     * @param tid
     * @param session
     * @return
     */
    Result remove(String tid, HttpSession session);

}
