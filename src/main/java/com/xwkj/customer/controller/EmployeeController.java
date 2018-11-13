package com.xwkj.customer.controller;

import com.xwkj.customer.bean.EmployeeBean;
import com.xwkj.customer.controller.common.ControllerTemplate;
import com.xwkj.customer.controller.common.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Controller
@RequestMapping("/api/employee")
public class EmployeeController extends ControllerTemplate {

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity login(@RequestParam String name, @RequestParam String password,
                                       @RequestParam String identifier, String deviceToken,
                                       @RequestParam String os, String version, String lan,
                                       HttpServletRequest request) {
        if (!deviceManager.isLegalDevice(os)) {
            return generateBadRequest(ErrorCode.ErrorIllegalIDeviceOS);
        }
        final EmployeeBean employeeBean = employeeManager.getByName(name);
        if (employeeBean == null) {
            return generateBadRequest(ErrorCode.ErrorUserNameNotExist);
        }
        if (!employeeBean.getPassword().equals(password)) {
            return generateBadRequest(ErrorCode.ErrorPasswordWrong);
        }
        //Login success, register device.
        final String token = deviceManager.registerDevice(identifier, os, version, lan,
                deviceToken, getRemoteIP(request), employeeBean.getEid());
        return generateOK(new HashMap<String, Object>() {{
            put("token", token);
            put("employee", employeeBean);
        }});
    }

}
