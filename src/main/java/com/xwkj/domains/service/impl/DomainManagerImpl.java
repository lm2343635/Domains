package com.xwkj.domains.service.impl;

import com.xwkj.domains.service.DomainManager;
import com.xwkj.domains.service.common.ManagerTemplate;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;

@Service
@RemoteProxy(name = "DomainManager")
public class DomainManagerImpl extends ManagerTemplate implements DomainManager {

}
