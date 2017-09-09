package com.xwkj.domains.services;

import net.sf.json.JSONArray;

import javax.servlet.http.HttpSession;

public interface AdminManager {

    public static final String ADMIN_CONFIG_PATH = "WEB-INF/admin.json";
    public static final String ADMIN_FLAG = "98237h88ba292f0157c0054a2a00e554";

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
     * @return
     */
    boolean login(String username, String password, HttpSession session);

    /**
     * @param session
     * @return
     */
    String checkSession(HttpSession session);

    /**
     * @param username
     * @param password
     */
    boolean addAdmin(String username, String password, HttpSession session);

    /**
     * @param username
     * @param oldPassword
     * @param newPassword
     */
    boolean modifyPassword(String username, String oldPassword, String newPassword, HttpSession session);

    /**
     * @param username
     */
    boolean removeAdmin(String username, HttpSession session);

}