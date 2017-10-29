package com.xwkj.customer.service;

import com.xwkj.customer.bean.Result;

import javax.servlet.http.HttpSession;

public interface DocumentManager {

    /**
     * Save document info to database.
     *
     * @param cid
     * @param fileName
     * @param session
     * @return
     */
    Result handleCustomerDocument(String cid, String fileName, HttpSession session);

    /**
     * Get all documents of a customer.
     *
     * @param cid
     * @param session
     * @return
     */
    Result getByCid(String cid, HttpSession session);

    /**
     * Get a document by did.
     *
     * @param did
     * @param session
     * @return
     */
    Result get(String did, HttpSession session);

    /**
     * Remove a document and delete the file.
     *
     * @param did
     * @param session
     * @return
     */
    Result remove(String did, HttpSession session);

}
