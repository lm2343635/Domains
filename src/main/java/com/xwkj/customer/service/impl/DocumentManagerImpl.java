package com.xwkj.customer.service.impl;

import com.xwkj.customer.service.DocumentManager;
import com.xwkj.customer.service.common.ManagerTemplate;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;

@Service
@RemoteProxy(name = "DocumentManager")
public class DocumentManagerImpl extends ManagerTemplate implements DocumentManager {


}
