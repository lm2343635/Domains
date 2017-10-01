package com.xwkj.customer.service.impl;

import com.xwkj.common.util.Debug;
import com.xwkj.customer.domain.Area;
import com.xwkj.customer.service.AreaManager;
import com.xwkj.customer.service.common.ManagerTemplate;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

@Service
@RemoteProxy(name = "AreaManager")
public class AreaManagerImpl extends ManagerTemplate implements AreaManager {

    @RemoteMethod
    @Transactional
    public String add(String name, HttpSession session) {
        if (!checkAdminSession(session)) {
            return null;
        }
        Area area = new Area();
        area.setCreateAt(System.currentTimeMillis());
        area.setCustomers(0);
        area.setName(name);
        return areaDao.save(area);
    }

    @RemoteMethod
    @Transactional
    public boolean edit(String aid, String name, HttpSession session) {
        if (!checkAdminSession(session)) {
            return false;
        }
        Area area = areaDao.get(aid);
        if (area == null) {
            Debug.error("Cannot find an area by this aid.");
            return false;
        }
        area.setName(name);
        areaDao.update(area);
        return true;
    }

    @RemoteMethod
    @Transactional
    public boolean remove(String aid, HttpSession session) {
        if (!checkAdminSession(session)) {
            return false;
        }
        Area area = areaDao.get(aid);
        if (area == null) {
            Debug.error("Cannot find an area by this aid.");
            return false;
        }
        areaDao.delete(area);
        return true;
    }

}
