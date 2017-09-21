package com.xwkj.customer.controller;

import com.xwkj.customer.service.AdminManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/logout")
public class LogoutController {

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public void adminLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().removeAttribute(AdminManager.AdminFlag);
        response.sendRedirect("/admin");
    }

}
