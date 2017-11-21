package com.xwkj.customer.service.impl;

import com.xwkj.common.util.Debug;
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
        bulletin.setUpdateAt(bulletin.getCreateAt());
        bulletin.setContent(content);
        bulletin.setTop(false);
        bulletin.setEmployee(employee);
        return Result.WithData(bulletinDao.save(bulletin));
    }

    @RemoteMethod
    public Result get(String bid, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        Bulletin bulletin = bulletinDao.get(bid);
        if (bulletin == null) {
            Debug.error("Cannot find a bulletin by this bid.");
            return Result.WithData(false);
        }
        return Result.WithData(new BulletinBean(bulletin));
    }

    @RemoteMethod
    public Result getTop(HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        List<BulletinBean> bulletinBeans = new ArrayList<BulletinBean>();
        for (Bulletin bulletin : bulletinDao.findTop()) {
            bulletinBeans.add(new BulletinBean(bulletin));
        }
        return Result.WithData(bulletinBeans);
    }

    @RemoteMethod
    public Result getUntopCount(HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        return Result.WithData(bulletinDao.getUntopCount());
    }

    @RemoteMethod
    public Result getUntopByPage(int page, int pageSize, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        int offset = (page - 1) * pageSize;
        List<BulletinBean> bulletinBeans = new ArrayList<BulletinBean>();
        for (Bulletin bulletin : bulletinDao.findUntop(offset, pageSize)) {
            bulletinBeans.add(new BulletinBean(bulletin));
        }
        return Result.WithData(bulletinBeans);
    }

    @RemoteMethod
    @Transactional
    public Result top(String bid, boolean top, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        Bulletin bulletin = bulletinDao.get(bid);
        if (bulletin == null) {
            Debug.error("Cannot find a bulletin by this bid.");
            return Result.WithData(false);
        }
        if (!bulletin.getEmployee().equals(employee)) {
            return Result.NoPrivilege();
        }
        bulletin.setTop(top);
        bulletinDao.update(bulletin);
        return Result.WithData(true);
    }

    @RemoteMethod
    @Transactional
    public Result remove(String bid, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        Bulletin bulletin = bulletinDao.get(bid);
        if (bulletin == null) {
            Debug.error("Cannot find a bulletin by this bid.");
            return Result.WithData(false);
        }
        if (!bulletin.getEmployee().equals(employee)) {
            return Result.NoPrivilege();
        }
        bulletinDao.delete(bulletin);
        return Result.WithData(true);
    }

    @RemoteMethod
    @Transactional
    public Result edit(String bid, String content, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        Bulletin bulletin = bulletinDao.get(bid);
        if (bulletin == null) {
            Debug.error("Cannot find a bulletin by this bid.");
            return Result.WithData(false);
        }
        if (!bulletin.getEmployee().equals(employee)) {
            return Result.NoPrivilege();
        }
        bulletin.setContent(content);
        bulletin.setUpdateAt(System.currentTimeMillis());
        bulletinDao.update(bulletin);
        return Result.WithData(true);
    }

}
