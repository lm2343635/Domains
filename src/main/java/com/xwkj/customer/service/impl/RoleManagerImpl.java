package com.xwkj.customer.service.impl;

import com.xwkj.common.util.Debug;
import com.xwkj.customer.bean.RoleBean;
import com.xwkj.customer.domain.Role;
import com.xwkj.customer.service.RoleManager;
import com.xwkj.customer.service.common.ManagerTemplate;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
@RemoteProxy(name = "RoleManager")
public class RoleManagerImpl extends ManagerTemplate implements RoleManager {

    @RemoteMethod
    @Transactional
    public String add(String name, int[] privelges, HttpSession session) {
        if (!checkAdminSession(session)) {
            return null;
        }
        Role role = new Role();
        role.setName(name);
        role.setUndevelopedR(privelges[0]);
        role.setUndevelopedW(privelges[1]);
        role.setUndevelopedD(privelges[2]);
        role.setDevelopingR(privelges[3]);
        role.setDevelopingW(privelges[4]);
        role.setDevelopingD(privelges[5]);
        role.setDevelopedR(privelges[6]);
        role.setDevelopedW(privelges[7]);
        role.setDevelopedD(privelges[8]);
        role.setLostR(privelges[9]);
        role.setLostW(privelges[10]);
        role.setLostD(privelges[11]);
        role.setDevelop(privelges[12]);
        role.setFinish(privelges[13]);
        role.setRuin(privelges[14]);
        role.setRecover(privelges[15]);
        role.setAssign(privelges[16]);
        role.setServer(privelges[17]);
        role.setDomain(privelges[18]);
        return roleDao.save(role);
    }

    @RemoteMethod
    public List<RoleBean> getAll(HttpSession session) {
        if (!checkAdminSession(session)) {
            return null;
        }
        List<RoleBean> roleBeans = new ArrayList<RoleBean>();
        for (Role role : roleDao.findAll()) {
            roleBeans.add(new RoleBean(role));
        }
        return roleBeans;
    }

    @RemoteMethod
    @Transactional
    public boolean remove(String rid, HttpSession session) {
        if (!checkAdminSession(session)) {
            return false;
        }
        Role role = roleDao.get(rid);
        if (role == null) {
            Debug.error("Cannot find a role by this rid.");
            return false;
        }
        roleDao.delete(role);
        return true;
    }

}
