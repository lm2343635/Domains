package com.xwkj.customer.service.impl;

import com.xwkj.customer.service.ExpirationManager;
import com.xwkj.customer.service.common.ManagerTemplate;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;

@Service
@RemoteProxy(name = "ExpirationManager")
public class ExpirationManagerImpl extends ManagerTemplate implements ExpirationManager {

}
