package com.xwkj.customer.service;

import net.sf.json.JSONArray;

import javax.servlet.http.HttpSession;

public interface AdminManager {

    public static final String AdminFlag = "98237h88ba292f0157c0054a2a00e554";

    /**
     * Admin login
     *
     * @param username
     * @param password
     * @param session
     * @return
     */
    boolean login(String username, String password, HttpSession session);

    /**
     * Check admin session.
     *
     * @param session
     * @return
     */
    String checkSession(HttpSession session);

}