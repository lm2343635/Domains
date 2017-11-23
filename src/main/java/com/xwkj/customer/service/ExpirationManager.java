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
     * @param money
     * @param session
     * @return
     */
    Result add(String cid, String tid, String expireAt, int money, HttpSession session);

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
     * @param money
     * @param session
     * @return
     */
    Result edit(String eid, String expireAt, int money, HttpSession session);

    /**
     * Remove an existing expiration.
     *
     * @param eid
     * @param session
     * @return
     */
    Result remove(String eid, HttpSession session);

    /**
     * Get search count and money count.
     *
     * @param tid
     * @param customer
     * @param start
     * @param end
     * @param session
     * @return
     */
    Result getCount(String tid, String customer, String start, String end, HttpSession session);

    /**
     * Search expirations and show by page.
     *
     * @param tid
     * @param customer
     * @param start
     * @param end
     * @param page
     * @param pageSize
     * @param session
     * @return
     */
    Result search(String tid, String customer, String start, String end, int page, int pageSize, HttpSession session);

}
