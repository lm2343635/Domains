package com.xwkj.customer.service.common;

import com.xwkj.customer.dao.DomainDao;
import com.xwkj.customer.dao.ServerDao;
import com.xwkj.customer.service.AdminManager;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;

public class ManagerTemplate {

    @Autowired
    protected ServerDao serverDao;

    @Autowired
    protected DomainDao domainDao;

    public boolean checkAdminSession(HttpSession session) {
        return session.getAttribute(AdminManager.ADMIN_FLAG) != null;
    }

}
