package com.xwkj.customer.service.impl;

import com.xwkj.common.util.Debug;
import com.xwkj.common.util.FileTool;
import com.xwkj.common.util.HTMLTool;
import com.xwkj.common.util.HTTPTool;
import com.xwkj.customer.bean.DomainBean;
import com.xwkj.customer.bean.Result;
import com.xwkj.customer.domain.Customer;
import com.xwkj.customer.domain.Domain;
import com.xwkj.customer.domain.Employee;
import com.xwkj.customer.domain.Server;
import com.xwkj.customer.service.CustomerManager;
import com.xwkj.customer.service.DomainManager;
import com.xwkj.customer.service.RoleManager;
import com.xwkj.customer.service.common.ManagerTemplate;
import org.apache.commons.io.FileUtils;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RemoteProxy(name = "DomainManager")
public class DomainManagerImpl extends ManagerTemplate implements DomainManager {

    @RemoteMethod
    @Transactional
    public Result add(String sid, String name, String cid, String domains, String language, String resolution,
                      String path, String remark, int frequncy, int similarity, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        if (employee.getRole().getDomain() != RoleManager.RolePrivilgeHold) {
            return Result.NoPrivilege();
        }
        Customer customer = customerDao.get(cid);
        if (customer == null) {
            Debug.error("Cannot fina a customer by this cid.");
            return Result.WithData(null);
        }
        if (customer.getState() != CustomerManager.CustomerStateDeveloped) {
            Debug.error("Customer should be developed!");
            return Result.WithData(null);
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
        domain.setCheckAt((long)0);
        domain.setGrabbed(false);
        domain.setMonitoring(false);
        domain.setAlert(false);
        domain.setFrequency(frequncy);
        domain.setSimilarity(similarity);
        domain.setServer(server);
        domain.setCustomer(customer);
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
            domainBeans.add(new DomainBean(domain, false));
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
            domainBeans.add(new DomainBean(domain, false));
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
        return Result.WithData(new DomainBean(domain, false));
    }

    @RemoteMethod
    @Transactional
    public Result modify(String did, String name, String cid, String domains, String language, String resolution,
                         String path, String remark, int frequncy, int similarity, HttpSession session) {
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
        Customer customer = customerDao.get(cid);
        if (customer == null) {
            Debug.error("Cannot fina a customer by this cid.");
            return Result.WithData(null);
        }
        if (customer.getState() != CustomerManager.CustomerStateDeveloped) {
            Debug.error("Customer should be developed!");
            return Result.WithData(null);
        }
        domain.setName(name);
        domain.setDomains(domains);
        domain.setLanguage(language);
        domain.setResolution(resolution);
        domain.setPath(path);
        domain.setRemark(remark);
        domain.setFrequency(frequncy);
        domain.setSimilarity(similarity);
        domain.setUpdateAt(System.currentTimeMillis());
        domain.setCustomer(customer);
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
        checkDao.deleteByDomain(domain);
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
    @Transactional
    public Result setMonitoring(String did, boolean monitoring, HttpSession session) {
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
        domain.setMonitoring(monitoring);
        domainDao.update(domain);
        return Result.WithData(true);
    }

    @RemoteMethod
    @Transactional
    public Result getWithGrabbedPgae(String did, String charset, HttpSession session) {
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
        String site = domain.getDomains().split(",")[0];
        if (site == null || site.equals("")) {
            return  Result.WithData(null);
        }
        String path = getRootPath() + configComponent.PublicIndexFolder + File.separator + domain.getDid();
        FileTool.createDirectoryIfNotExsit(path);
        File file = new File(path + File.separator + "index.html");
        try {
            FileUtils.copyURLToFile(new URL("http://" + site), file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        final String page = HTTPTool.httpRequest("http://" + site, charset);
        domain.setPage(page);
        domainDao.update(domain);
        return Result.WithData(new HashMap<String, Object>() {{
            put("domain", new DomainBean(domain, false));
            put("page", page);
        }});
    }

    @RemoteMethod
    @Transactional
    public Result savePage(String did, String charset, HttpSession session) {
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
        domain.setCharset(charset);
        domain.setGrabbed(true);
        domainDao.update(domain);
        return Result.WithData(true);
    }

    @RemoteMethod
    @Transactional
    public Result cancelAlert(String did, HttpSession session) {
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
        domain.setAlert(false);
        domainDao.update(domain);
        return Result.WithData(true);
    }

    @RemoteMethod
    public Result getByCid(String cid, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        Customer customer = customerDao.get(cid);
        if (customer == null) {
            Debug.error("Cannot find a customer by this cid");
            return Result.WithData(null);
        }
        List<DomainBean> domainBeans = new ArrayList<>();
        for (Domain domain : domainDao.findByCustomer(customer)) {
            domainBeans.add(new DomainBean(domain, true));
        }
        return Result.WithData(domainBeans);
    }

}
