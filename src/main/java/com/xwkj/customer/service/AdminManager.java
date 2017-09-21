package com.xwkj.customer.service;

import net.sf.json.JSONArray;

import javax.servlet.http.HttpSession;

public interface AdminManager {

    public static final String AdminConfigPath = "WEB-INF/admin.json";
    public static final String AdminFlag = "98237h88ba292f0157c0054a2a00e554";

    /**
     * @param session
     * @return
     */
    JSONArray getAdmins(HttpSession session);

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

    /**
     * Add a new admin.
     *
     * @param username
     * @param password
     */
    boolean addAdmin(String username, String password, HttpSession session);

    /**
     * Modify password for an admin.
     *
     * @param username
     * @param oldPassword
     * @param newPassword
     */
    boolean modifyPassword(String username, String oldPassword, String newPassword, HttpSession session);

    /**
     * Remove an exsiting admin.
     *
     * @param username
     */
    boolean removeAdmin(String username, HttpSession session);

}