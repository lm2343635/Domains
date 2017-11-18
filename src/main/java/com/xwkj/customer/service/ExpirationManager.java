package com.xwkj.customer.service;

import com.xwkj.customer.bean.Result;

import javax.servlet.http.HttpSession;

public interface ExpirationManager {

    /**
     * Create an expiration for a customer by an employee.
     *
     * @param cid
     * @param tid
     * @param expireAt
     * @param session
     * @return
     */
    Result add(String cid, String tid, String expireAt, HttpSession session);

    /**
     * Get all expirations of a customer.
     *
     * @param cid
     * @param session
     * @return
     */
    Result getByCid(String cid, HttpSession session);

    /**
     * Edit expireAt of an expiration.
     *
     * @param eid
     * @param expireAt
     * @param session
     * @return
     */
    Result edit(String eid, String expireAt, HttpSession session);

    /**
     * Remove an existing expiration.
     *
     * @param eid
     * @param session
     * @return
     */
    Result remove(String eid, HttpSession session);

}
