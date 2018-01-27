package com.xwkj.customer.service.impl;

import com.xwkj.common.util.DateTool;
import com.xwkj.common.util.Debug;
import com.xwkj.customer.bean.Result;
import com.xwkj.customer.bean.WorkBean;
import com.xwkj.customer.domain.Employee;
import com.xwkj.customer.domain.Work;
import com.xwkj.customer.service.WorkManager;
import com.xwkj.customer.service.common.ManagerTemplate;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
@RemoteProxy(name = "WorkManager")
public class WorkManagerImpl extends ManagerTemplate implements WorkManager {

    @RemoteMethod
    @Transactional
    public Result add(String title, String eid, HttpSession session) {
        Employee sponsor = getEmployeeFromSession(session);
        if (sponsor == null) {
            return Result.NoSession();
        }
        Employee executor = employeeDao.get(eid);
        if (executor == null) {
            Debug.error("Cannot find an employee for executor.");
            return Result.WithData(null);
        }
        Work work = new Work();
        work.setCreateAt(System.currentTimeMillis());
        work.setTitle(title);
        work.setActive(true);
        work.setSponsor(sponsor);
        work.setReplys(0);
        work.setExecutor(executor);
        return Result.WithData(workDao.save(work));
    }

    @RemoteMethod
    @Transactional
    public Result close(String wid, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        Work work = workDao.get(wid);
        if (work == null) {
            Debug.error("Cannot find a work by this wid.");
            return Result.WithData(null);
        }
        if (!work.getSponsor().equals(employee)) {
            return Result.NoPrivilege();
        }
        work.setActive(false);
        workDao.update(work);
        return Result.WithData(true);
    }

    @RemoteMethod
    public Result get(String wid, HttpSession session) {
        if (!checkEmployeeSession(session)) {
            return Result.NoSession();
        }
        Work work = workDao.get(wid);
        if (work == null) {
            Debug.error("Cannot find a work by this wid.");
            return Result.WithData(null);
        }
        return Result.WithData(new WorkBean(work));
    }

    @RemoteMethod
    public Result getSearchCount(boolean active, String title, String sponsorId, String executorId, String start, String end, HttpSession session) {
        if (!checkEmployeeSession(session)) {
            return Result.NoSession();
        }
        Employee sponsor = null, exectuor = null;
        if (sponsorId != null && !sponsorId.equals("")) {
            sponsor = employeeDao.get(sponsorId);
            if (sponsor == null) {
                Debug.error("Cannot find a sponsor employee by this sponsor id.");
            }
        }
        if (executorId != null && !executorId.equals("")) {
            exectuor = employeeDao.get(executorId);
            if (exectuor == null) {
                Debug.error("Cannot find a exectuor employee by this exectuor id.");
            }
        }
        Long startStamp = null, endStamp = null;
        if (start != null && !start.equals("")) {
            startStamp = DateTool.transferDate(start + " 00:00:00", DateTool.DATE_HOUR_MINUTE_SECOND_FORMAT).getTime();
        }
        if (end != null && !end.equals("")) {
            endStamp = DateTool.transferDate(end + " 23:59:59", DateTool.DATE_HOUR_MINUTE_SECOND_FORMAT).getTime();
        }
        return Result.WithData(workDao.getCount(active, title, sponsor, exectuor, startStamp, endStamp));
    }

    @RemoteMethod
    public Result search(boolean active, String title, String sponsorId, String executorId, String start, String end, int page, int pageSize, HttpSession session) {
        if (!checkEmployeeSession(session)) {
            return Result.NoSession();
        }
        Employee sponsor = null, exectuor = null;
        if (sponsorId != null && !sponsorId.equals("")) {
            sponsor = employeeDao.get(sponsorId);
            if (sponsor == null) {
                Debug.error("Cannot find a sponsor employee by this sponsor id.");
            }
        }
        if (executorId != null && !executorId.equals("")) {
            exectuor = employeeDao.get(executorId);
            if (exectuor == null) {
                Debug.error("Cannot find a exectuor employee by this exectuor id.");
            }
        }
        Long startStamp = null, endStamp = null;
        if (start != null && !start.equals("")) {
            startStamp = DateTool.transferDate(start + " 00:00:00", DateTool.DATE_HOUR_MINUTE_SECOND_FORMAT).getTime();
        }
        if (end != null && !end.equals("")) {
            endStamp = DateTool.transferDate(end + " 23:59:59", DateTool.DATE_HOUR_MINUTE_SECOND_FORMAT).getTime();
        }
        int offset = (page - 1) * pageSize;
        List<WorkBean> workBeans = new ArrayList<WorkBean>();
        for (Work work : workDao.find(active, title, sponsor, exectuor, startStamp, endStamp, offset, pageSize)) {
            workBeans.add(new WorkBean(work));
        }
        return Result.WithData(workBeans);
    }

}
