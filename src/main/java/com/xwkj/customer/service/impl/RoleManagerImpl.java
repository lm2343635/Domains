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
    public String add(String name, int[] privileges, HttpSession session) {
        if (!checkAdminSession(session)) {
            return null;
        }
        Role role = new Role();
        role.setName(name);
        role.setPrivileges(privileges);
        role.setEmployees(0);
        return roleDao.save(role);
    }

    @RemoteMethod
    @Transactional
    public boolean edit(String rid, String name, int[] privileges, HttpSession session) {
        if (!checkAdminSession(session)) {
            return false;
        }
        Role role = roleDao.get(rid);
        if (role == null) {
            Debug.error("Cannot find a role by this rid.");
            return false;
        }
        role.setName(name);
        role.setPrivileges(privileges);
        roleDao.update(role);
        return true;
    }

    @RemoteMethod
    public RoleBean get(String rid, HttpSession session) {
        if (!checkAdminSession(session)) {
            return null;
        }
        Role role = roleDao.get(rid);
        if (role == null) {
            Debug.error("Cannot find a role by this rid.");
            return null;
        }
        return new RoleBean(role);
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
        // Cannot delete a role who has employees.
        if (role.getEmployees() > 0) {
            return false;
        }
        roleDao.delete(role);
        return true;
    }

}
