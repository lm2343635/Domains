package com.xwkj.customer.service;

import com.xwkj.customer.bean.AreaBean;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface AreaManager {

    /**
     * Add a new area.
     *
     * @param name
     * @param session
     * @return
     */
    String add(String name, HttpSession session);

    /**
     * Edit an area.
     *
     * @param aid
     * @param name
     * @param session
     * @return
     */
    boolean edit(String aid, String name, HttpSession session);

    /**
     * Remove an area.
     *
     * @param aid
     * @param session
     * @return
     */
    boolean remove(String aid, HttpSession session);

    /**
     * Get all areas.
     *
     * @return
     */
    List<AreaBean> getAll();

}
