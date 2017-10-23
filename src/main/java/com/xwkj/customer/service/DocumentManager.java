package com.xwkj.customer.service;

import com.xwkj.customer.bean.Result;

import javax.servlet.http.HttpSession;

public interface DocumentManager {

    Result handleCustomerDocument(String cid, String fileName, HttpSession session);

}
