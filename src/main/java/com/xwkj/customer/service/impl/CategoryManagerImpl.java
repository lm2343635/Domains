package com.xwkj.customer.service.impl;

import com.xwkj.customer.service.CategoryManager;
import com.xwkj.customer.service.common.ManagerTemplate;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;

@Service
@RemoteProxy(name = "CategoryManager")
public class CategoryManagerImpl extends ManagerTemplate implements CategoryManager {
}
