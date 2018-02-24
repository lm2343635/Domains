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
     * @param frequncy
     * @param similarity
     * @param session
     * @return
     */
    Result add(String sid, String name, String domains, String language, String resolution,
               String path, String remark, int frequncy, int similarity, HttpSession session);

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
     * @param frequncy
     * @param similarity
     * @param session
     * @return
     */
    Result modify(String did, String name, String domains, String language, String resolution,
                  String path, String remark, int frequncy, int similarity, HttpSession session);

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

    /**
     * Set monitoring state of a domain.
     *
     * @param did
     * @param monitoring
     * @param session
     * @return
     */
    Result setMonitoring(String did, boolean monitoring, HttpSession session);

    /**
     * Load html of a page with charset.
     *
     * @param did
     * @param charset
     * @param session
     * @return
     */
    Result getWithGrabbedPgae(String did, String charset, HttpSession session);

    /**
     * Save the HTML string of the index page.
     *
     * @param did
     * @param charset
     * @param page
     * @param session
     * @return
     */
    Result savePage(String did, String charset, String page, HttpSession session);

}
