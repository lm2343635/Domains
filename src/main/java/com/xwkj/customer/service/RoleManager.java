package com.xwkj.customer.service;

import com.xwkj.customer.bean.RoleBean;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface RoleManager {

    public final static int RolePrivilgeNone = 0;
    public final static int RolePrivilgeAssign = 1;
    public final static int RolePrivilgeHold = 2;

    public final static int PrivilegeRead = 0;
    public final static int PrivilegeWrite = 1;
    public final static int PrivilegeDelete = 2;

    /**
     * Add a new role by admin.
     *
     * @param name
     * @param privelges
     * @param session
     * @return
     */
    String add(String name, int [] privelges, HttpSession session);

    /**
     * Get a role get rid for admin.
     *
     * @param rid
     * @param session
     * @return
     */
    RoleBean get(String rid, HttpSession session);

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
