package com.xwkj.customer.service;

import com.xwkj.customer.bean.EmployeeBean;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface EmployeeManager {

    public static final String EmployeeFlag = "675ea2340289fea2003b6e3f00a7d015";

    // ************* For admin ****************

    /**
     * Admin add a new employee.
     *
     * @param name
     * @param password
     * @param rid
     * @param session
     * @return
     */
    String add(String name, String password, String rid, HttpSession session);

    /**
     * Get all employees for admin.
     *
     * @param session
     * @return
     */
    List<EmployeeBean> getAll(HttpSession session);

    /**
     * Get employee by eid for admin.
     *
     * @param eid
     * @param session
     * @return
     */
    EmployeeBean get(String eid, HttpSession session);

    /**
     * Admin edit a employee.
     *
     * @param eid
     * @param name
     * @param rid
     * @param session
     * @return
     */
    boolean edit(String eid, String name, String rid, HttpSession session);

    /**
     * Admin remove a employee.
     *
     * @param eid
     * @param session
     * @return
     */
    boolean remove(String eid, HttpSession session);

    /**
     * Admin reset password for a employee.
     *
     * @param eid
     * @param password
     * @param session
     * @return
     */
    boolean resetPassword(String eid, String password, HttpSession session);

    // ************* For employee ****************

    /**
     * Employee login
     *
     * @param username
     * @param password
     * @param session
     * @return first page.
     */
    String login(String username, String password, HttpSession session);

    /**
     * Check employee session.
     *
     * @param session
     * @return
     */
    EmployeeBean checkSession(HttpSession session);

}
