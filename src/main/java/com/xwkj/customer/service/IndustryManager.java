package com.xwkj.customer.service;

import com.xwkj.customer.bean.IndustryBean;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface IndustryManager {

    /**
     * Add a new industry.
     *
     * @param name
     * @param session
     * @return
     */
    String add(String name, HttpSession session);

    /**
     * Edit an industry.
     *
     * @param iid
     * @param name
     * @param session
     * @return
     */
    boolean edit(String iid, String name, HttpSession session);

    /**
     * Remove an industry.
     *
     * @param iid
     * @param session
     * @return
     */
    boolean remove(String iid, HttpSession session);

    /**
     * Get all industry.
     *
     * @return
     */
    List<IndustryBean> getAll();

}
