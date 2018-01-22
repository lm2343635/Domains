package com.xwkj.customer.service.impl;

import com.xwkj.customer.service.ReplyManager;
import com.xwkj.customer.service.common.ManagerTemplate;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;

@Service
@RemoteProxy(name = "ReplyManager")
public class ReplyManagerImpl extends ManagerTemplate implements ReplyManager {

}
