package com.xwkj.domains.service.impl;

import com.xwkj.domains.domain.Domain;
import com.xwkj.domains.service.DomainManager;
import com.xwkj.domains.service.common.ManagerTemplate;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

@Service
@RemoteProxy(name = "DomainManager")
public class DomainManagerImpl extends ManagerTemplate implements DomainManager {

    @RemoteMethod
    @Transactional
    public String add(String name, String domains, String language, String resolution, String path, String remark, HttpSession session) {
        if (!checkAdminSession(session)) {
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
        domain.setCreateAt(System.currentTimeMillis());
        domain.setUpdateAt(domain.getCreateAt());
        return domainDao.save(domain);
    }

}
