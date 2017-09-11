package com.xwkj.domains.service;

import com.xwkj.domains.bean.DomainBean;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface DomainManager {

    public final static int StateNormal = 0;

    /**
     * Add a new domain
     *
     * @param sid
     * @param name
     * @param domains
     * @param language
     * @param resolution
     * @param path
     * @param remark
     * @param session
     * @return
     */
    String add(String sid, String name, String domains, String language,
               String resolution, String path, String remark, HttpSession session);

    /**
     * Get all domains of a server.
     *
     * @param sid
     * @param session
     * @return
     */
    List<DomainBean> getBySid(String sid, HttpSession session);

    /**
     * Get a domain by did.
     *
     * @param did
     * @param session
     * @return
     */
    DomainBean get(String did, HttpSession session);

    /**
     * Modify a domain.
     *
     * @param did
     * @param name
     * @param domains
     * @param language
     * @param resolution
     * @param path
     * @param remark
     * @param session
     * @return
     */
    boolean modify(String did, String name, String domains, String language,
                   String resolution, String path, String remark, HttpSession session);

    /**
     * Remove a domain.
     *
     * @param did
     * @param session
     * @return
     */
    boolean remove(String did, HttpSession session);

    /**
     * Transfer a domain to a new server.
     *
     * @param did
     * @param sid
     * @param session
     * @return
     */
    boolean transfer(String did, String sid, HttpSession session);

}
