package com.xwkj.customer.service.impl;

import com.xwkj.customer.service.ReportManager;
import com.xwkj.customer.service.common.ManagerTemplate;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;

@Service
@RemoteProxy(name = "ReportManager")
public class ReportManagerImpl extends ManagerTemplate implements ReportManager {

}
