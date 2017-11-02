package com.xwkj.customer.service.impl;

import com.xwkj.customer.service.SalaryManager;
import com.xwkj.customer.service.common.ManagerTemplate;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;

@Service
@RemoteProxy(name = "SalaryManager")
public class SalaryManagerImpl extends ManagerTemplate implements SalaryManager {

}
