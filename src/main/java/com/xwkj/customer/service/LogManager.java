package com.xwkj.customer.service;

import com.xwkj.customer.bean.Result;

import javax.servlet.http.HttpSession;

public interface LogManager {

    Result add(String cid, String content, HttpSession session);

}
