package com.xwkj.domains.services.impl;

import com.xwkj.domains.services.DomainManager;
import com.xwkj.domains.services.common.ManagerTemplate;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;

@Service
@RemoteProxy(name = "DomainManager")
public class DomainManagerImpl extends ManagerTemplate implements DomainManager {
    
}
