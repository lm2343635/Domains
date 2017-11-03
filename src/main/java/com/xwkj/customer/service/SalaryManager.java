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
     * @param session
     * @return
     */
    Result add(String eid, String remark, int money, HttpSession session);

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
