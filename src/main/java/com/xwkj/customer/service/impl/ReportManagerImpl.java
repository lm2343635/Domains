package com.xwkj.customer.service.impl;

import com.xwkj.common.util.DateTool;
import com.xwkj.common.util.Debug;
import com.xwkj.customer.bean.ReportBean;
import com.xwkj.customer.bean.Result;
import com.xwkj.customer.domain.Employee;
import com.xwkj.customer.domain.Report;
import com.xwkj.customer.domain.Type;
import com.xwkj.customer.service.ReportManager;
import com.xwkj.customer.service.RoleManager;
import com.xwkj.customer.service.TypeManager;
import com.xwkj.customer.service.common.ManagerTemplate;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
@RemoteProxy(name = "ReportManager")
public class ReportManagerImpl extends ManagerTemplate implements ReportManager {

    @RemoteMethod
    @Transactional
    public Result add(String title, String tid, String content, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        Type type = typeDao.get(tid);
        if (type == null) {
            Debug.error("Cannot find a type by this tid.");
            return Result.WithData(null);
        }
        if (type.getCategory() != TypeManager.TypeCategoryReport) {
            Debug.error("Type category error!");
            return Result.WithData(null);
        }
        Report report = new Report();
        report.setCreateAt(System.currentTimeMillis());
        report.setUpdateAt(report.getCreateAt());
        report.setTitle(title);
        report.setType(type);
        report.setContent(content);
        report.setEmployee(employee);
        return Result.WithData(reportDao.save(report));
    }

    @RemoteMethod
    public Result get(String rid, HttpSession session) {
        if (!checkEmployeeSession(session)) {
            return Result.NoSession();
        }
        Report report = reportDao.get(rid);
        if (report == null) {
            Debug.error("Cannot find a report by this rid.");
            return Result.WithData(false);
        }
        return Result.WithData(new ReportBean(report, true));
    }

    @RemoteMethod
    @Transactional
    public Result edit(String rid, String title, String tid, String content, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        Report report = reportDao.get(rid);
        if (report == null) {
            Debug.error("Cannot find a report by this rid.");
            return Result.WithData(false);
        }
        if (!report.getEmployee().equals(employee)) {
            return Result.NoPrivilege();
        }
        Type type = typeDao.get(tid);
        if (type == null) {
            Debug.error("Cannot find a type by this tid.");
            return Result.WithData(null);
        }
        if (type.getCategory() != TypeManager.TypeCategoryReport) {
            Debug.error("Type category error!");
        }
        report.setTitle(title);
        report.setType(type);
        report.setContent(content);
        report.setUpdateAt(System.currentTimeMillis());
        reportDao.update(report);
        return Result.WithData(true);
    }

    @RemoteMethod
    @Transactional
    public Result remove(String rid, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        Report report = reportDao.get(rid);
        if (report == null) {
            Debug.error("Cannot find a report by this rid.");
            return Result.WithData(false);
        }
        // The creator and other employees with the employee privilege can remove this report.
        if (!report.getEmployee().equals(employee) && employee.getRole().getEmployee() != RoleManager.RolePrivilgeHold) {
            return Result.NoPrivilege();
        }
        reportDao.delete(report);
        return Result.WithData(true);
    }

    @RemoteMethod
    public Result getSearchCount(String tid, String title, String start, String end, HttpSession session) {
        if (!checkEmployeeSession(session)) {
            return Result.NoSession();
        }
        Type type = typeDao.get(tid);
        if (type == null) {
            Debug.error("Cannot find a type by this tid.");
            return Result.WithData(null);
        }
        if (type.getCategory() != TypeManager.TypeCategoryReport) {
            Debug.error("Type category error!");
        }
        Long startStamp = null, endStamp = null;
        if (start != null && !start.equals("")) {
            startStamp = DateTool.transferDate(start + " 00:00:00", DateTool.DATE_HOUR_MINUTE_SECOND_FORMAT).getTime();
        }
        if (end != null && !end.equals("")) {
            endStamp = DateTool.transferDate(end + " 23:59:59", DateTool.DATE_HOUR_MINUTE_SECOND_FORMAT).getTime();
        }
        return Result.WithData(reportDao.getCount(type, title, startStamp, endStamp));
    }

    @RemoteMethod
    public Result search(String tid, String title, String start, String end, int page, int pageSize, HttpSession session) {
        if (!checkEmployeeSession(session)) {
            return Result.NoSession();
        }
        Type type = typeDao.get(tid);
        if (type == null) {
            Debug.error("Cannot find a type by this tid.");
            return Result.WithData(null);
        }
        if (type.getCategory() != TypeManager.TypeCategoryReport) {
            Debug.error("Type category error!");
        }
        Long startStamp = null, endStamp = null;
        if (start != null && !start.equals("")) {
            startStamp = DateTool.transferDate(start + " 00:00:00", DateTool.DATE_HOUR_MINUTE_SECOND_FORMAT).getTime();
        }
        if (end != null && !end.equals("")) {
            endStamp = DateTool.transferDate(end + " 23:59:59", DateTool.DATE_HOUR_MINUTE_SECOND_FORMAT).getTime();
        }
        int offset = (page - 1) * pageSize;
        List<ReportBean> reportBeans = new ArrayList<ReportBean>();
        for (Report report : reportDao.find(type, title, startStamp, endStamp, offset, pageSize)) {
            reportBeans.add(new ReportBean(report, false));
        }
        return Result.WithData(reportBeans);
    }

}
