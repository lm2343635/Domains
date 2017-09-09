package com.xwkj.domains.service;

import com.xwkj.domains.bean.ServerBean;

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
    List<ServerBean> getAll(HttpSession session);

}
