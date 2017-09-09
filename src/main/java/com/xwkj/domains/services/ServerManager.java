package com.xwkj.domains.services;

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
    String add(String name, String address, String remark, HttpSession session);

}
