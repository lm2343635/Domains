package com.xwkj.customer.service;

import com.xwkj.customer.bean.Result;

import javax.servlet.http.HttpSession;

public interface CustomerManager {

    public static final int CustomerSataeUndeveloped = 0;

    /**
     * Employee add a new undeveloped customer.
     *
     * @param name
     * @param capital
     * @param contact
     * @param session
     * @return
     */
    Result addUndeveloped(String name, int capital, String contact, HttpSession session);

}
