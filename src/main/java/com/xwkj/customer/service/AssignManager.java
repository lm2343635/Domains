package com.xwkj.customer.service;

import com.xwkj.customer.bean.Result;

import javax.servlet.http.HttpSession;

public interface AssignManager {

    /**
     * Assign a new manager for a customer.
     *
     * @param cid
     * @param eid
     * @param session
     * @return
     */
    Result assign(String cid, String eid, boolean r, boolean w, boolean d, boolean assign, HttpSession session);

    /**
     * Revoke an existing manager for a customer.
     *
     * @param cid
     * @param eid
     * @param session
     * @return
     */
    Result revokeAssign(String cid, String eid, HttpSession session);

    /**
     * Get count of search results.
     *
     * @param eid
     * @param state
     * @param name
     * @param aid
     * @param iid
     * @param lower
     * @param higher
     * @param session
     * @return
     */
    Result getSearchCount(String eid, int state, String name, String aid, String iid, int lower, int higher, HttpSession session);

    /**
     * Search assigned customers for a employee.
     *
     * @param eid
     * @param state
     * @param name
     * @param aid
     * @param iid
     * @param lower
     * @param higher
     * @param page
     * @param pageSize
     * @param session
     * @return
     */
    Result search(String eid, int state, String name, String aid, String iid, int lower, int higher, int page, int pageSize, HttpSession session);

}
