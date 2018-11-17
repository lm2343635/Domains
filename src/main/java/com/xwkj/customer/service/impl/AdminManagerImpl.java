package com.xwkj.customer.service.impl;

import com.xwkj.customer.component.config.Admin;
import com.xwkj.customer.service.AdminManager;
import com.xwkj.customer.service.common.ManagerTemplate;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
@RemoteProxy(name = "AdminManager")
public class AdminManagerImpl extends ManagerTemplate implements AdminManager {

    @RemoteMethod
    public boolean login(String username, String password, HttpSession session) {
        Admin admin = configComponent.getAdmin();
        if (admin.username.equals(username) && admin.password.equals(password)) {
            session.setAttribute(AdminFlag, username);
            return true;

        }
        return false;
    }

    @RemoteMethod
    public String checkSession(HttpSession session) {
        if (session.getAttribute(AdminFlag) == null) {
            return null;
        }
        return (String) session.getAttribute(AdminFlag);
    }

}