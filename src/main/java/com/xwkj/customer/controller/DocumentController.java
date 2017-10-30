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
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

@Controller
@RequestMapping("/document")
public class DocumentController extends ControllerTemplate {

    @RequestMapping(value = "/upload/customer", method = RequestMethod.POST)
    public ResponseEntity uploadCustomerDocument(@RequestParam String cid, HttpServletRequest request) {
        if (!checkEmployeeSession(request.getSession())) {
            return generateBadRequest(ErrorCode.ErrorNoSession);
        }
        String filepath = createUploadDirectory(cid);
        String filename = upload(request, filepath);
        Result result = documentManager.handleCustomerDocument(cid, filename, request.getSession());
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

    @RequestMapping(value = "/upload/public", method = RequestMethod.POST)
    public ResponseEntity uploadPublicDocument(HttpServletRequest request) {
        if (!checkEmployeeSession(request.getSession())) {
            return generateBadRequest(ErrorCode.ErrorNoSession);
        }
        String filename = upload(request, configComponent.rootPath + configComponent.PublicDocumentFolder);
        Result result = documentManager.handlePublicDocument(filename, request.getSession());
        if (!result.isSession()) {
            return generateBadRequest(ErrorCode.ErrorNoSession);
        }
        final DocumentBean documentBean = (DocumentBean) result.getData();
        return generateOK(new HashMap() {{
            put("did", documentBean.getDid());
            put("filename", documentBean.getFilename());
            put("size", documentBean.getSize());
        }});
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void downloadDocument(@RequestParam String did, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!checkEmployeeSession(request.getSession())) {
            sessionError(response);
            return;
        }
        Result result = documentManager.get(did, request.getSession());
        if (!result.isSession()) {
            sessionError(response);
            return;
        }
        if (!result.isPrivilege()) {
            response.getWriter().println("No privilege to download this file.");
            return;
        }
        DocumentBean documentBean = (DocumentBean) result.getData();
        String path = documentBean.getCid() == null ?
                configComponent.rootPath + configComponent.PublicDocumentFolder : createUploadDirectory(documentBean.getCid());
        download( path + File.separator + documentBean.getStore(), documentBean.getFilename(), response);
    }

    private void sessionError(HttpServletResponse response) {
        try {
            response.sendRedirect("/employee/session.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
