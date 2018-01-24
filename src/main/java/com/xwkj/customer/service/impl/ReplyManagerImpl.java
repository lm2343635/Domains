package com.xwkj.customer.service.impl;

import com.xwkj.common.util.Debug;
import com.xwkj.customer.bean.Result;
import com.xwkj.customer.domain.Employee;
import com.xwkj.customer.domain.Reply;
import com.xwkj.customer.domain.Work;
import com.xwkj.customer.service.ReplyManager;
import com.xwkj.customer.service.common.ManagerTemplate;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

@Service
@RemoteProxy(name = "ReplyManager")
public class ReplyManagerImpl extends ManagerTemplate implements ReplyManager {

    @RemoteMethod
    @Transactional
    public Result add(String wid, String content, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        Work work = workDao.get(wid);
        if (work == null) {
            Debug.error("Cannot find a work by this wid.");
            return Result.WithData(null);
        }
        Reply reply = new Reply();
        reply.setCreateAt(System.currentTimeMillis());
        reply.setContent(content);
        reply.setEmployee(employee);
        reply.setWork(work);
        String rid = replyDao.save(reply);
        if (rid == null) {
            return Result.WithData(null);
        }
        work.setReplys(work.getReplys() + 1);
        workDao.update(work);
        return Result.WithData(rid);
    }
    
}
