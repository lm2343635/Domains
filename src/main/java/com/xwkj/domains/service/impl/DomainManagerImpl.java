package com.xwkj.domains.service.impl;

import com.xwkj.common.util.Debug;
import com.xwkj.domains.bean.DomainBean;
import com.xwkj.domains.domain.Domain;
import com.xwkj.domains.domain.Server;
import com.xwkj.domains.service.DomainManager;
import com.xwkj.domains.service.common.ManagerTemplate;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
@RemoteProxy(name = "DomainManager")
public class DomainManagerImpl extends ManagerTemplate implements DomainManager {

    @RemoteMethod
    @Transactional
    public String add(String sid, String name, String domains, String language,
                      String resolution, String path, String remark, HttpSession session) {
        if (!checkAdminSession(session)) {
            return null;
        }
        Server server = serverDao.get(sid);
        if (server == null) {
            Debug.error("Cannot find a server by this sid.");
            return null;
        }
        Domain domain = new Domain();
        domain.setName(name);
        domain.setDomains(domains);
        domain.setLanguage(language);
        domain.setResolution(resolution);
        domain.setPath(path);
        domain.setRemark(remark);
        domain.setHighlight(false);
        domain.setState(StateNormal);
        domain.setCreateAt(System.currentTimeMillis());
        domain.setUpdateAt(domain.getCreateAt());
        domain.setServer(server);
        String did = domainDao.save(domain);
        if (did == null) {
            return null;
        }
        server.setDomains(server.getDomains() + 1);
        serverDao.update(server);
        return did;
    }

    @RemoteMethod
    public List<DomainBean> getBySid(String sid, HttpSession session) {
        if (!checkAdminSession(session)) {
            return null;
        }
        Server server = serverDao.get(sid);
        if (server == null) {
            Debug.error("Cannot find a server by this sid.");
            return null;
        }
        List<DomainBean> domainBeans = new ArrayList<DomainBean>();
        for (Domain domain : domainDao.findByServer(server)) {
            domainBeans.add(new DomainBean(domain));
        }
        return domainBeans;
    }

    @RemoteMethod
    public DomainBean get(String did, HttpSession session) {
        if (!checkAdminSession(session)) {
            return null;
        }
        Domain domain = domainDao.get(did);
        if (domain == null) {
            Debug.error("Cannot find a domain by this did.");
            return null;
        }
        return new DomainBean(domain);
    }

    @RemoteMethod
    @Transactional
    public boolean modify(String did, String name, String domains, String language,
                          String resolution, String path, String remark, HttpSession session) {
        if (!checkAdminSession(session)) {
            return false;
        }
        Domain domain = domainDao.get(did);
        if (domain == null) {
            Debug.error("Cannot find a domain by this did.");
            return false;
        }
        domain.setName(name);
        domain.setDomains(domains);
        domain.setLanguage(language);
        domain.setResolution(resolution);
        domain.setPath(path);
        domain.setRemark(remark);
        domain.setUpdateAt(System.currentTimeMillis());
        domainDao.update(domain);
        return true;
    }

    @RemoteMethod
    @Transactional
    public boolean remove(String did, HttpSession session) {
        if (!checkAdminSession(session)) {
            return false;
        }
        Domain domain = domainDao.get(did);
        if (domain == null) {
            Debug.error("Cannot find a domain by this did.");
            return false;
        }
        Server server = domain.getServer();
        server.setDomains(server.getDomains() - 1);
        serverDao.update(server);
        domainDao.delete(domain);
        return true;
    }

    @RemoteMethod
    @Transactional
    public boolean transfer(String did, String sid, HttpSession session) {
        if (!checkAdminSession(session)) {
            return false;
        }
        Domain domain = domainDao.get(did);
        if (domain == null) {
            Debug.error("Cannot find a domain by this did.");
            return false;
        }
        Server newServer = serverDao.get(sid);
        if (newServer == null) {
            Debug.error("Cannot find a server by this sid");
            return false;
        }
        Server oldServer = domain.getServer();
        oldServer.setDomains(oldServer.getDomains() - 1);
        serverDao.update(oldServer);
        domain.setServer(newServer);
        domainDao.update(domain);
        newServer.setDomains(newServer.getDomains() + 1);
        serverDao.update(newServer);
        return true;
    }

    @RemoteMethod
    @Transactional
    public boolean setHighlight(String did, boolean highlight, HttpSession session) {
        if (!checkAdminSession(session)) {
            return false;
        }
        Domain domain = domainDao.get(did);
        if (domain == null) {
            Debug.error("Cannot find a domain by this did.");
            return false;
        }
        domain.setHighlight(highlight);
        return true;
    }

}
