package com.xwkj.customer.controller;

import com.xwkj.customer.bean.DocumentBean;
import com.xwkj.customer.bean.Result;
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
@RequestMapping("/document")
public class DocumentController extends ControllerTemplate {

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity uploadCustomerDocument(@RequestParam String cid, HttpServletRequest request) {
        if (!checkEmployeeSession(request.getSession())) {
            return generateBadRequest(ErrorCode.ErrorNoSession);
        }
        String filepath = createUploadDirectory(cid);
        String fileName = upload(request, filepath);
        Result result = documentManager.handleCustomerDocument(cid, fileName, request.getSession());
        if (!result.isSession()) {
            return generateBadRequest(ErrorCode.ErrorNoSession);
        }
        if (!result.isPrivilege()) {
            return generateBadRequest(ErrorCode.ErrorNoPrivilge);
        }
        final DocumentBean documentBean = (DocumentBean) result.getData();
        return generateOK(new HashMap() {{
            put("did", documentBean.getDid());
            put("filename", documentBean.getFilename());
            put("size", documentBean.getSize());
        }});
    }

}
