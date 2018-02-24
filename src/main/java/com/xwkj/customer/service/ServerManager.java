package com.xwkj.customer.service;

import com.xwkj.customer.bean.Result;

import javax.servlet.http.HttpSession;

public interface ServerManager {

    /**
     * Create a new server.
     *
     * @param name
     * @param address
     * @param remark
     * @param session
     * @return
     */
    Result add(String name, String address, String remark, HttpSession session);

    /**
     * Get all servers.
     *
     * @param session
     * @return
     */
    Result getAll(HttpSession session);

    /**
     * Get a server by sid.
     *
     * @param sid
     * @param session
     * @return
     */
    Result get(String sid, HttpSession session);

    /**
     * Modify a domain.
     *
     * @param sid
     * @param name
     * @param address
     * @param remark
     * @param session
     * @return
     */
    Result modify(String sid, String name, String address, String remark, HttpSession session);

    /**
     * Save user and password for linux servers.
     *
     * @param sid
     * @param user
     * @param password
     * @param session
     * @return
     */
    Result setUser(String sid, String user, String password, HttpSession session);

    /**
     * Remove a domain.
     *
     * @param sid
     * @param session
     * @return
     */
    Result remove(String sid, HttpSession session);

}
