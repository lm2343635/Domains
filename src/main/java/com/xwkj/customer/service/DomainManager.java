package com.xwkj.customer.service;

import com.xwkj.customer.bean.Result;

import javax.servlet.http.HttpSession;

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
    Result add(String sid, String name, String domains, String language,
               String resolution, String path, String remark, HttpSession session);

    /**
     * Get all customer of a server.
     *
     * @param sid
     * @param session
     * @return
     */
    Result getBySid(String sid, HttpSession session);

    /**
     * Get all highlight customer.
     *
     * @param session
     * @return
     */
    Result getHightlightDomains(HttpSession session);

    /**
     * Get a domain by did.
     *
     * @param did
     * @param session
     * @return
     */
    Result get(String did, HttpSession session);

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
    Result modify(String did, String name, String domains, String language,
                   String resolution, String path, String remark, HttpSession session);

    /**
     * Remove a domain.
     *
     * @param did
     * @param session
     * @return
     */
    Result remove(String did, HttpSession session);

    /**
     * Transfer a domain to a new server.
     *
     * @param did
     * @param sid
     * @param session
     * @return
     */
    Result transfer(String did, String sid, HttpSession session);

    /**
     * Set highlight state of a domain.
     *
     * @param did
     * @param highlight
     * @param session
     * @return
     */
    Result setHighlight(String did, boolean highlight, HttpSession session);

}
