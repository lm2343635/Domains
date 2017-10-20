package com.xwkj.customer.service.impl;

import com.xwkj.customer.bean.BulletinBean;
import com.xwkj.customer.bean.Result;
import com.xwkj.customer.domain.Bulletin;
import com.xwkj.customer.domain.Employee;
import com.xwkj.customer.service.BulletinManager;
import com.xwkj.customer.service.common.ManagerTemplate;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
@RemoteProxy(name = "BulletinManager")
public class BulletinManagerImpl extends ManagerTemplate implements BulletinManager {

    @RemoteMethod
    @Transactional
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

    @RemoteMethod
    public Result getByLimit(int limit, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        List<BulletinBean> bulletinBeans = new ArrayList<BulletinBean>();
        for (Bulletin bulletin : bulletinDao.findWithLimit(limit)) {
            bulletinBeans.add(new BulletinBean(bulletin));
        }
        return Result.WithData(bulletinBeans);
    }

}
