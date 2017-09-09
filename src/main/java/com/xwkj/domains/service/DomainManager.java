package com.xwkj.domains.service;

import javax.servlet.http.HttpSession;

public interface DomainManager {

    /**
     * Add a new domain
     *
     * @param name
     * @param domains
     * @param language
     * @param resolution
     * @param path
     * @param remark
     * @param session
     * @return
     */
    String add(String name, String domains, String language, String resolution, String path, String remark, HttpSession session);

}
