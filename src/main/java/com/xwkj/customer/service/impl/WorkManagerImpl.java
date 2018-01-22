package com.xwkj.customer.service.impl;

import com.xwkj.customer.service.WorkManager;
import com.xwkj.customer.service.common.ManagerTemplate;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;

@Service
@RemoteProxy(name = "WorkManager")
public class WorkManagerImpl extends ManagerTemplate implements WorkManager {

}
