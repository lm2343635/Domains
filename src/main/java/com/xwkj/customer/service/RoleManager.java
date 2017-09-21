package com.xwkj.customer.service;

import com.xwkj.customer.bean.RoleBean;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface RoleManager {

    public final static int RolePrevilgeNone = 0;
    public final static int RolePrevilgeAssign = 1;
    public final static int RolePrevilgeHold = 2;

    /**
     * Add a new role
     *
     * @param name
     * @param privelges
     * @param session
     * @return
     */
    String add(String name, int [] privelges, HttpSession session);

    /**
     * Get all roles for admin.
     *
     * @param session
     * @return
     */
    List<RoleBean> getAll(HttpSession session);

    /**
     * Admin remove a role.
     *
     * @param rid
     * @param session
     * @return
     */
    boolean remove(String rid, HttpSession session);

}
