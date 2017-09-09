package com.xwkj.domains.services.impl;

import com.xwkj.domains.domain.Server;
import com.xwkj.domains.services.ServerManager;
import com.xwkj.domains.services.common.ManagerTemplate;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

@Service
@RemoteProxy(name = "ServerManager")
public class ServerManagerImpl extends ManagerTemplate implements ServerManager {

    @RemoteMethod
    @Transactional
    public String add(String name, String address, String remark, HttpSession session) {
        if (!checkAdminSession(session)) {
            return null;
        }
        Server server = new Server();
        server.setName(name);
        server.setAddress(address);
        server.setRemark(remark);
        return serverDao.save(server);
    }

}
