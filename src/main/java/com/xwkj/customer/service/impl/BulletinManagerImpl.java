package com.xwkj.customer.service.impl;

import com.xwkj.customer.service.BulletinManager;
import com.xwkj.customer.service.common.ManagerTemplate;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;

@Service
@RemoteProxy(name = "BulletinManager")
public class BulletinManagerImpl extends ManagerTemplate implements BulletinManager {

}
