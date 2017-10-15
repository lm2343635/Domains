package com.xwkj.customer.service;

import com.xwkj.customer.bean.EmployeeBean;
import com.xwkj.customer.bean.Result;

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

    // ************* For admin & employee ****************

    /**
     * Get all employees for admin.
     *
     * @param session
     * @return
     */
    Result getAll(HttpSession session);

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

    /**
     * Get employees who can be a manager of a developing customer.
     *
     * @param session
     * @return
     */
    Result getDevelopingAssignableEmployees(HttpSession session);

    /**
     * Get employees who can be a manager of a developed customer.
     *
     * @param cid
     * @param session
     * @return
     */
    Result getDevelopedAssinableEmployees(String cid, HttpSession session);

    /**
     * Grt assign state of a employee for a developed customer.
     *
     * @param cid
     * @return
     */
    Result assignForCustomer(String cid, HttpSession session);


}
