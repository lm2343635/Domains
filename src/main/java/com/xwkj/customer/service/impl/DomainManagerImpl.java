package com.xwkj.customer.service.impl;

import com.xwkj.common.util.Debug;
import com.xwkj.common.util.HTMLTool;
import com.xwkj.common.util.HTTPTool;
import com.xwkj.customer.bean.DomainBean;
import com.xwkj.customer.bean.Result;
import com.xwkj.customer.domain.Domain;
import com.xwkj.customer.domain.Employee;
import com.xwkj.customer.domain.Server;
import com.xwkj.customer.service.DomainManager;
import com.xwkj.customer.service.RoleManager;
import com.xwkj.customer.service.common.ManagerTemplate;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RemoteProxy(name = "DomainManager")
public class DomainManagerImpl extends ManagerTemplate implements DomainManager {

    @RemoteMethod
    @Transactional
    public Result add(String sid, String name, String domains, String language,
                      String resolution, String path, String remark, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        if (employee.getRole().getDomain() != RoleManager.RolePrivilgeHold) {
            return Result.NoPrivilege();
        }
        Server server = serverDao.get(sid);
        if (server == null) {
            Debug.error("Cannot find a server by this sid.");
            return Result.WithData(null);
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
            return Result.WithData(null);
        }
        server.setDomains(server.getDomains() + 1);
        serverDao.update(server);
        return Result.WithData(did);
    }

    @RemoteMethod
    public Result getBySid(String sid, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        if (employee.getRole().getDomain() != RoleManager.RolePrivilgeHold) {
            return Result.NoPrivilege();
        }
        Server server = serverDao.get(sid);
        if (server == null) {
            Debug.error("Cannot find a server by this sid.");
            return Result.WithData(null);
        }
        List<DomainBean> domainBeans = new ArrayList<DomainBean>();
        for (Domain domain : domainDao.findByServer(server)) {
            domainBeans.add(new DomainBean(domain));
        }
        return Result.WithData(domainBeans);
    }

    @RemoteMethod
    public Result getHightlightDomains(HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        if (employee.getRole().getDomain() != RoleManager.RolePrivilgeHold) {
            return Result.NoPrivilege();
        }
        List<DomainBean> domainBeans = new ArrayList<DomainBean>();
        for (Domain domain : domainDao.findHighlightDomains()) {
            domainBeans.add(new DomainBean(domain));
        }
        return Result.WithData(domainBeans);
    }

    @RemoteMethod
    public Result get(String did, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        if (employee.getRole().getDomain() != RoleManager.RolePrivilgeHold) {
            return Result.NoPrivilege();
        }
        Domain domain = domainDao.get(did);
        if (domain == null) {
            Debug.error("Cannot find a domain by this did.");
            return Result.WithData(null);
        }
        return Result.WithData(new DomainBean(domain));
    }

    @RemoteMethod
    @Transactional
    public Result modify(String did, String name, String domains, String language,
                          String resolution, String path, String remark, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        if (employee.getRole().getDomain() != RoleManager.RolePrivilgeHold) {
            return Result.NoPrivilege();
        }
        Domain domain = domainDao.get(did);
        if (domain == null) {
            Debug.error("Cannot find a domain by this did.");
            return Result.WithData(false);
        }
        domain.setName(name);
        domain.setDomains(domains);
        domain.setLanguage(language);
        domain.setResolution(resolution);
        domain.setPath(path);
        domain.setRemark(remark);
        domain.setUpdateAt(System.currentTimeMillis());
        domainDao.update(domain);
        return Result.WithData(true);
    }

    @RemoteMethod
    @Transactional
    public Result remove(String did, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        if (employee.getRole().getDomain() != RoleManager.RolePrivilgeHold) {
            return Result.NoPrivilege();
        }
        Domain domain = domainDao.get(did);
        if (domain == null) {
            Debug.error("Cannot find a domain by this did.");
            return Result.WithData(false);
        }
        Server server = domain.getServer();
        server.setDomains(server.getDomains() - 1);
        serverDao.update(server);
        domainDao.delete(domain);
        return Result.WithData(true);
    }

    @RemoteMethod
    @Transactional
    public Result transfer(String did, String sid, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        if (employee.getRole().getDomain() != RoleManager.RolePrivilgeHold) {
            return Result.NoPrivilege();
        }
        Domain domain = domainDao.get(did);
        if (domain == null) {
            Debug.error("Cannot find a domain by this did.");
            return Result.WithData(false);
        }
        Server newServer = serverDao.get(sid);
        if (newServer == null) {
            Debug.error("Cannot find a server by this sid");
            return Result.WithData(false);
        }
        Server oldServer = domain.getServer();
        oldServer.setDomains(oldServer.getDomains() - 1);
        serverDao.update(oldServer);
        domain.setServer(newServer);
        domainDao.update(domain);
        newServer.setDomains(newServer.getDomains() + 1);
        serverDao.update(newServer);
        return Result.WithData(true);
    }

    @RemoteMethod
    @Transactional
    public Result setHighlight(String did, boolean highlight, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        if (employee.getRole().getDomain() != RoleManager.RolePrivilgeHold) {
            return Result.NoPrivilege();
        }
        Domain domain = domainDao.get(did);
        if (domain == null) {
            Debug.error("Cannot find a domain by this did.");
            return Result.WithData(false);
        }
        domain.setHighlight(highlight);
        domainDao.update(domain);
        return Result.WithData(true);
    }

    @RemoteMethod
    public Result getWithGrabbedPgae(String did, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        if (employee.getRole().getDomain() != RoleManager.RolePrivilgeHold) {
            return Result.NoPrivilege();
        }
        final Domain domain = domainDao.get(did);
        if (domain == null) {
            Debug.error("Cannot find a domain by this did.");
            return Result.WithData(null);
        }
        String html = null;
        String site = domain.getDomains().split(",")[0];
        if (site != null && !site.equals("")) {
            html = HTTPTool.httpRequest("http://" + site);
        }
        final String page = (html == null) ? "" : HTMLTool.compress(html);
        return Result.WithData(new HashMap<String, Object>() {{
            put("domain", new DomainBean(domain));
            put("page", page);
        }});
    }

    @RemoteMethod
    @Transactional
    public Result savePage(String did, String page, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        if (employee.getRole().getDomain() != RoleManager.RolePrivilgeHold) {
            return Result.NoPrivilege();
        }
        Domain domain = domainDao.get(did);
        if (domain == null) {
            Debug.error("Cannot find a domain by this did.");
            return Result.WithData(false);
        }
        domain.setPage(HTMLTool.compress(page));
        domainDao.update(domain);
        return Result.WithData(true);
    }

}
