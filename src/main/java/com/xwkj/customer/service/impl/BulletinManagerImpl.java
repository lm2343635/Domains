package com.xwkj.customer.service.impl;

import com.xwkj.customer.bean.Result;
import com.xwkj.customer.domain.Bulletin;
import com.xwkj.customer.domain.Employee;
import com.xwkj.customer.service.BulletinManager;
import com.xwkj.customer.service.common.ManagerTemplate;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
@RemoteProxy(name = "BulletinManager")
public class BulletinManagerImpl extends ManagerTemplate implements BulletinManager {

    public Result add(String content, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        Bulletin bulletin = new Bulletin();
        bulletin.setCreateAt(System.currentTimeMillis());
        bulletin.setContent(content);
        bulletin.setEmployee(employee);
        return Result.WithData(bulletinDao.save(bulletin));
    }

}
