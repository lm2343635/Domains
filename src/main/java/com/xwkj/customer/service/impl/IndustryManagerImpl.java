package com.xwkj.customer.service.impl;

import com.xwkj.common.util.Debug;
import com.xwkj.customer.bean.IndustryBean;
import com.xwkj.customer.domain.Industry;
import com.xwkj.customer.service.IndustryManager;
import com.xwkj.customer.service.common.ManagerTemplate;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProperty;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
@RemoteProxy(name = "IndustryManager")
public class IndustryManagerImpl extends ManagerTemplate implements IndustryManager {

    @RemoteMethod
    @Transactional
    public String add(String name, HttpSession session) {
        if (!checkAdminSession(session)) {
            return null;
        }
        Industry industry = new Industry();
        industry.setCreateAt(System.currentTimeMillis());
        industry.setCustomers(0);
        industry.setName(name);
        return industryDao.save(industry);
    }

    @RemoteMethod
    @Transactional
    public boolean edit(String iid, String name, HttpSession session) {
        if (!checkAdminSession(session)) {
            return false;
        }
        Industry industry = industryDao.get(iid);
        if (industry == null) {
            Debug.error("Cannot find an industry by this iid.");
            return false;
        }
        industry.setName(name);
        industryDao.update(industry);
        return true;
    }

    @RemoteMethod
    @Transactional
    public boolean remove(String iid, HttpSession session) {
        if (!checkAdminSession(session)) {
            return false;
        }
        Industry industry = industryDao.get(iid);
        if (industry == null) {
            Debug.error("Cannot find an industry by this iid.");
            return false;
        }
        industryDao.delete(industry);
        return true;
    }

    @RemoteMethod
    public List<IndustryBean> getAll() {
        List<IndustryBean> industryBeans = new ArrayList<IndustryBean>();
        for (Industry industry : industryDao.findAll("createAt", false)) {
            industryBeans.add(new IndustryBean(industry));
        }
        return industryBeans;
    }

}
