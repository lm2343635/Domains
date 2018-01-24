package com.xwkj.customer.service.impl;

import com.xwkj.common.util.Debug;
import com.xwkj.customer.bean.Result;
import com.xwkj.customer.domain.Employee;
import com.xwkj.customer.domain.Work;
import com.xwkj.customer.service.WorkManager;
import com.xwkj.customer.service.common.ManagerTemplate;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

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

}
