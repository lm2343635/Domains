package com.xwkj.customer.service;

import com.xwkj.customer.bean.Result;

import javax.servlet.http.HttpSession;

public interface SalaryManager {

    /**
     * Create a new salary record.
     *
     * @param eid
     * @param remark
     * @param money
     * @param detail
     * @param session
     * @return
     */
    Result add(String eid, String remark, int money, String detail, HttpSession session);

    /**
     * Get a salary record by eid.
     *
     * @param sid
     * @param session
     * @return
     */
    Result get(String sid, HttpSession session);

    /**
     * Remove an existing salary record.
     *
     * @param sid
     * @param session
     * @return
     */
    Result remove(String sid, HttpSession session);

    /**
     * Get all salary record of an employee.
     *
     * @param eid
     * @param session
     * @return
     */
    Result getByEid(String eid, HttpSession session);

}
