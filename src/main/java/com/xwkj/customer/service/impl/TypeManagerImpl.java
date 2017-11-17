package com.xwkj.customer.service.impl;

import com.xwkj.customer.service.TypeManager;
import com.xwkj.customer.service.common.ManagerTemplate;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;

@Service
@RemoteProxy(name = "TypeManager")
public class TypeManagerImpl extends ManagerTemplate implements TypeManager {
}
