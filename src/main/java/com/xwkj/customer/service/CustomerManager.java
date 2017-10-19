package com.xwkj.customer.service;

import com.xwkj.customer.bean.CustomerBean;
import com.xwkj.customer.bean.Result;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface CustomerManager {

    public static final int CustomerStateUndeveloped = 0;
    public static final int CustomerStateDeveloping = 1;
    public static final int CustomerStateDeveloped = 2;
    public static final int CustomerStateLost = 3;

    /**
     * Employee add a new undeveloped customer.
     *
     * @param name
     * @param capital
     * @param contact
     * @param aid
     * @param iid
     * @param session
     * @return
     */
    Result addUndeveloped(String name, int capital, String contact, String aid, String iid, HttpSession session);

    /**
     * Get search count.
     *
     * @param state
     * @param name
     * @param aid
     * @param iid
     * @param lower
     * @param higher
     * @return
     */
    Result getSearchCount(int state, String name, String aid, String iid, int lower, int higher, HttpSession session);

    /**
     * Search customers for employees.
     *
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
    Result search(int state, String name, String aid, String iid, int lower, int higher, int page, int pageSize, HttpSession session);

    /**
     * Get a customer for a employee.
     *
     * @param cid
     * @param session
     * @return
     */
    Result get(String cid, HttpSession session);

    /**
     * Edit a customer.
     *
     * @param cid
     * @param name
     * @param capital
     * @param aid
     * @param iid
     * @param contact
     * @param items
     * @param money
     * @param remark
     * @param document
     * @param session
     * @return
     */
    Result edit(String cid, String name, int capital, String contact, String aid, String iid,
                String items, int money, String expireAt, String remark, String document, HttpSession session);

    /**
     * Develop a customer.
     * The state of this customer will transfer from undeveloped to developing.
     *
     * @param cid
     * @param session
     * @return
     */
    Result develop(String cid, HttpSession session);

    /**
     * Assign a new manager and finish developing a customer.
     * The state of this customer will transfer from developing to developed.
     *
     * @param cid
     * @param eid
     * @param session
     * @return
     */
    Result finish(String cid, String eid, HttpSession session);

    /**
     * Ruin a customer.
     * The state of this customer will transfer from developed to lost.
     *
     * @param cid
     * @param session
     * @return
     */
    Result ruin(String cid, HttpSession session);

    /**
     * Get number of customers created by an employee.
     *
     * @param eid
     * @param session
     * @return
     */
    Result getCreatesCount(String eid, HttpSession session);

    /**
     * Get all customers created by an employee.
     *
     * @param eid
     * @param page
     * @param pageSize
     * @param session
     * @return
     */
    Result getCreates(String eid, int page, int pageSize, HttpSession session);

}
