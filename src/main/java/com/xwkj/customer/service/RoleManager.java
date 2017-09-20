package com.xwkj.customer.service;

import javax.servlet.http.HttpSession;

public interface RoleManager {

    /**
     * Add a new role
     *
     * @param name
     * @param privelges
     * @param session
     * @return
     */
    String add(String name, int [] privelges, HttpSession session);

}
