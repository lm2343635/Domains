package com.xwkj.domains.service.impl;

import com.xwkj.domains.bean.ServerBean;
import com.xwkj.domains.domain.Server;
import com.xwkj.domains.service.ServerManager;
import com.xwkj.domains.service.common.ManagerTemplate;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

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
        server.setCreateAt(System.currentTimeMillis());
        server.setUpdateAt(server.getCreateAt());
        server.setDomains(0);
        return serverDao.save(server);
    }

    @RemoteMethod
    public List<ServerBean> getAll(HttpSession session) {
        if (!checkAdminSession(session)) {
            return null;
        }
        List<ServerBean> serverBeans = new ArrayList<ServerBean>();
        for (Server server : serverDao.findAll("createAt", true)) {
            serverBeans.add(new ServerBean(server));
        }
        return serverBeans;
    }

}
