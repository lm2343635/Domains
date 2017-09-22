package com.xwkj.customer.service;

import com.xwkj.customer.bean.Result;
import com.xwkj.customer.bean.ServerBean;

import javax.servlet.http.HttpSession;
import java.util.List;

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
    String add(String name, String address, String remark, HttpSession session);

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
    ServerBean get(String sid, HttpSession session);

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
    boolean modify(String sid, String name, String address, String remark, HttpSession session);

    /**
     * Remove a domain.
     *
     * @param sid
     * @param session
     * @return
     */
    boolean remove(String sid, HttpSession session);

}
