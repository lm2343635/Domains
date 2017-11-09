package com.xwkj.customer.service.impl;

import com.xwkj.common.util.DateTool;
import com.xwkj.common.util.Debug;
import com.xwkj.common.util.FileTool;
import com.xwkj.customer.bean.DocumentBean;
import com.xwkj.customer.bean.Result;
import com.xwkj.customer.domain.Customer;
import com.xwkj.customer.domain.Document;
import com.xwkj.customer.domain.Employee;
import com.xwkj.customer.service.DocumentManager;
import com.xwkj.customer.service.RoleManager;
import com.xwkj.customer.service.common.ManagerTemplate;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RemoteProxy(name = "DocumentManager")
public class DocumentManagerImpl extends ManagerTemplate implements DocumentManager {

    @Transactional
    public Result handleCustomerDocument(String cid, String filename, HttpSession session) {
        String path = configComponent.rootPath + File.separator + configComponent.UploadFolder + File.separator + cid;
        File file = new File(path + File.separator + filename);
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            if (file.exists()) {
                file.delete();
            }
            return Result.NoSession();
        }
        Customer customer = customerDao.get(cid);
        if (customer == null) {
            Debug.error("Cannot find a customer by this cid.");
            if (file.exists()) {
                file.delete();
            }
            return Result.WithData(null);
        }
        switch (getPrivilegeForEmployee(employee, customer.getState(), RoleManager.PrivilegeWrite)) {
            case RoleManager.RolePrivilgeAssign:
                if (!assignWritePrivilege(customer, employee)) {
                    if (file.exists()) {
                        file.delete();
                    }
                    return Result.NoPrivilege();
                }
                break;
            case RoleManager.RolePrivilgeNone:
                if (file.exists()) {
                    file.delete();
                }
                return Result.NoPrivilege();
            default:
                break;
        }
        Document document = new Document();
        document.setFilename(filename);
        document.setUploadAt(System.currentTimeMillis());
        document.setStore(UUID.randomUUID().toString());
        document.setSize(file.length());
        document.setCustomer(customer);
        document.setEmployee(employee);
        // Save to persistent store.
        if (documentDao.save(document) == null) {
            if (file.exists()) {
                file.delete();
            }
            return Result.WithData(null);
        }
        // Modify file name.
        FileTool.modifyFileName(path, filename, document.getStore());
        return Result.WithData(new DocumentBean(document, false));
    }

    @Transactional
    public Result handlePublicDocument(String filename, HttpSession session) {
        String path = configComponent.rootPath + File.separator + configComponent.PublicDocumentFolder;
        File file = new File(path + File.separator + filename);
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            if (file.exists()) {
                file.delete();
            }
            return Result.NoSession();
        }
        Document document = new Document();
        document.setFilename(filename);
        document.setUploadAt(System.currentTimeMillis());
        document.setStore(UUID.randomUUID().toString());
        document.setSize(file.length());
        document.setEmployee(employee);
        // Save to persistent store.
        if (documentDao.save(document) == null) {
            if (file.exists()) {
                file.delete();
            }
            return Result.WithData(null);
        }
        // Modify file name.
        FileTool.modifyFileName(path, filename, document.getStore());
        return Result.WithData(new DocumentBean(document, false));
    }

    @RemoteMethod
    public Result getByCid(String cid, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        Customer customer = customerDao.get(cid);
        if (customer == null) {
            Debug.error("Cannot find a customer by this cid.");
            return Result.WithData(null);
        }
        switch (getPrivilegeForEmployee(employee, customer.getState(), RoleManager.PrivilegeRead)) {
            case RoleManager.RolePrivilgeAssign:
                if (!assignWritePrivilege(customer, employee)) {
                    return Result.NoPrivilege();
                }
                break;
            case RoleManager.RolePrivilgeNone:
                return Result.NoPrivilege();
            default:
                break;
        }
        List<DocumentBean> documentBeans = new ArrayList<DocumentBean>();
        for (Document document : documentDao.findByCustomer(customer)) {
            documentBeans.add(new DocumentBean(document, false));
        }
        return Result.WithData(documentBeans);
    }

    public Result getSearchPublicCount(String filename, String start, String end, HttpSession session) {
        if (!checkEmployeeSession(session)) {
            return Result.NoSession();
        }
        Long startStamp = null, endStamp = null;
        if (start != null && !start.equals("")) {
            startStamp = DateTool.transferDate(start + " 00:00:00", DateTool.DATE_HOUR_MINUTE_SECOND_FORMAT).getTime();
        }
        if (end != null && !end.equals("")) {
            endStamp = DateTool.transferDate(end + " 23:59:59", DateTool.DATE_HOUR_MINUTE_SECOND_FORMAT).getTime();
        }
        return Result.WithData(documentDao.getPublicCount(filename, startStamp, endStamp));
    }

    public Result searchPublic(String filename, String start, String end, int page, int pageSize, HttpSession session) {
        if (!checkEmployeeSession(session)) {
            return Result.NoSession();
        }
        Long startStamp = null, endStamp = null;
        if (start != null && !start.equals("")) {
            startStamp = DateTool.transferDate(start + " 00:00:00", DateTool.DATE_HOUR_MINUTE_SECOND_FORMAT).getTime();
        }
        if (end != null && !end.equals("")) {
            endStamp = DateTool.transferDate(end + " 23:59:59", DateTool.DATE_HOUR_MINUTE_SECOND_FORMAT).getTime();
        }
        int offset = (page - 1) * pageSize;
        List<DocumentBean> documentBeans = new ArrayList<DocumentBean>();
        for (Document document : documentDao.findPublic(filename, startStamp, endStamp, offset, pageSize)) {
            documentBeans.add(new DocumentBean(document, false));
        }
        return Result.WithData(documentBeans);
    }

    @RemoteMethod
    public Result getPublic(HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        List<DocumentBean> documentBeans = new ArrayList<DocumentBean>();
        for (Document document : documentDao.findPublicCustomer()) {
            documentBeans.add(new DocumentBean(document, false));
        }
        return Result.WithData(documentBeans);
    }

    @RemoteMethod
    public Result get(String did, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        Document document = documentDao.get(did);
        if (document == null) {
            Debug.error("Cannot find a document by this did.");
            return Result.WithData(null);
        }
        if (document.getCustomer() != null) {
            Customer customer = document.getCustomer();
            if (!document.getEmployee().equals(employee)) {
                switch (getPrivilegeForEmployee(employee, customer.getState(), RoleManager.PrivilegeWrite)) {
                    case RoleManager.RolePrivilgeAssign:
                        if (!assignWritePrivilege(customer, employee)) {
                            return Result.NoPrivilege();
                        }
                        break;
                    case RoleManager.RolePrivilgeNone:
                        return Result.NoPrivilege();
                    default:
                        break;
                }
            }
        }
        return Result.WithData(new DocumentBean(document, true));
    }

    @RemoteMethod
    @Transactional
    public Result remove(String did, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        Document document = documentDao.get(did);
        if (document == null) {
            Debug.error("Cannot find a document by this did.");
            return Result.WithData(false);
        }
        if (document.getCustomer() != null) {
            Customer customer = document.getCustomer();
            if (!document.getEmployee().equals(employee)) {
                switch (getPrivilegeForEmployee(employee, customer.getState(), RoleManager.PrivilegeDelete)) {
                    case RoleManager.RolePrivilgeAssign:
                        if (!assignWritePrivilege(customer, employee)) {
                            return Result.NoPrivilege();
                        }
                        break;
                    case RoleManager.RolePrivilgeNone:
                        return Result.NoPrivilege();
                    default:
                        break;
                }
            }
        }
        String path = configComponent.rootPath;
        path += document.getCustomer() == null ? configComponent.PublicDocumentFolder :
                (configComponent.UploadFolder + File.separator + document.getCustomer().getCid());
        File file = new File(path + File.separator + document.getStore());
        if (file.exists()) {
            file.delete();
        }
        documentDao.delete(document);
        return Result.WithData(true);
    }

}
