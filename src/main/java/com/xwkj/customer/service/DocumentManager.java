package com.xwkj.customer.service;

import com.xwkj.customer.bean.Result;

import javax.servlet.http.HttpSession;

public interface DocumentManager {

    /**
     * Save document info of a customer into database.
     *
     * @param cid
     * @param filename
     * @param session
     * @return
     */
    Result handleCustomerDocument(String cid, String filename, HttpSession session);

    /**
     * Save public document info into database.
     *
     * @param filename
     * @param session
     * @return
     */
    Result handlePublicDocument(String filename, HttpSession session);

    /**
     * Get all documents of a customer.
     *
     * @param cid
     * @param session
     * @return
     */
    Result getByCid(String cid, HttpSession session);

    /**
     * Get numbers of public documents searched by file name and update date.
     *
     * @param tid
     * @param filename
     * @param start
     * @param end
     * @param session
     * @return
     */
    Result getSearchPublicCount(String tid, String filename, String start, String end, HttpSession session);

    /**
     * Search public documents.
     *
     * @param tid
     * @param filename
     * @param start
     * @param end
     * @param page
     * @param pageSize
     * @param session
     * @return
     */
    Result searchPublic(String tid, String filename, String start, String end, int page, int pageSize, HttpSession session);

    /**
     * Get all public documents.
     *
     * @param session
     * @return
     */
    Result getPublic(HttpSession session);

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
