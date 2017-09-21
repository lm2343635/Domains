package com.xwkj.customer.service;

import javax.servlet.http.HttpSession;

public interface EmployeeManager {

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



}
