package com.xwkj.customer.service;

import com.xwkj.customer.bean.Result;

import javax.servlet.http.HttpSession;

public interface TypeManager {

    public final static int TypeCategoryExpiration = 0;
    public final static int TypeCategoryReport = 1;
    public final static int TypeCategoryDocument = 2;

    /**
     * Add a new type for expiration.
     *
     * @param name
     * @param category
     * @param session
     * @return
     */
    Result add(String name, int category, HttpSession session);

    /**
     * Get types by category.
     *
     * @param category
     * @param session
     * @return
     */
    Result getByCategory(int category, HttpSession session);

    /**
     * Get all types.
     *
     * @param session
     * @return
     */
    Result getAll(HttpSession session);

    /**
     * Remove an existing type.
     *
     * @param tid
     * @param session
     * @return
     */
    Result remove(String tid, HttpSession session);

}
