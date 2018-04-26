package com.xwkj.customer.service.impl;

import com.xwkj.common.util.Debug;
import com.xwkj.common.util.FileTool;
import com.xwkj.customer.bean.Result;
import com.xwkj.customer.bean.ServerBean;
import com.xwkj.customer.domain.Employee;
import com.xwkj.customer.domain.Server;
import com.xwkj.customer.service.RoleManager;
import com.xwkj.customer.service.ServerManager;
import com.xwkj.customer.service.common.ManagerTemplate;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RemoteProxy(name = "ServerManager")
public class ServerManagerImpl extends ManagerTemplate implements ServerManager {

    @RemoteMethod
    @Transactional
    public Result add(String name, String address, String remark, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        if (employee.getRole().getServer() != RoleManager.RolePrivilgeHold) {
            return Result.NoPrivilege();
        }
        Server server = new Server();
        server.setName(name);
        server.setAddress(address);
        server.setRemark(remark);
        server.setCreateAt(System.currentTimeMillis());
        server.setUpdateAt(server.getCreateAt());
        server.setDomains(0);
        return Result.WithData(serverDao.save(server));
    }

    @RemoteMethod
    public Result getAll(HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        if (employee.getRole().getServer() != RoleManager.RolePrivilgeHold) {
            return Result.NoPrivilege();
        }
        List<ServerBean> serverBeans = new ArrayList<ServerBean>();
        for (Server server : serverDao.findAll("updateAt", true)) {
            serverBeans.add(new ServerBean(server));
        }
        return Result.WithData(serverBeans);
    }

    @RemoteMethod
    public Result get(String sid, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        if (employee.getRole().getServer() != RoleManager.RolePrivilgeHold) {
            return Result.NoPrivilege();
        }
        Server server = serverDao.get(sid);
        if (server == null) {
            Debug.error("Cannot find a server by this sid.");
            return Result.WithData(null);
        }
        return Result.WithData(new ServerBean(server));
    }

    @RemoteMethod
    @Transactional
    public Result modify(String sid, String name, String address, String remark, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        if (employee.getRole().getServer() != RoleManager.RolePrivilgeHold) {
            return Result.NoPrivilege();
        }
        Server server = serverDao.get(sid);
        if (server == null) {
            Debug.error("Cannot find a server by this sid.");
            return Result.WithData(false);
        }
        server.setName(name);
        server.setAddress(address);
        server.setRemark(remark);
        server.setUpdateAt(System.currentTimeMillis());
        serverDao.update(server);
        return Result.WithData(true);
    }

    @RemoteMethod
    @Transactional
    public Result setUser(String sid, String user, boolean usingPublicKey, String credential, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        if (employee.getRole().getServer() != RoleManager.RolePrivilgeHold) {
            return Result.NoPrivilege();
        }
        Server server = serverDao.get(sid);
        if (server == null) {
            Debug.error("Cannot find a server by this sid.");
            return Result.WithData(false);
        }
        server.setUser(user);
        server.setUsingPublicKey(usingPublicKey);
        server.setCredential(credential);
        serverDao.update(server);
        if (usingPublicKey) {
            generatePublicKeyFile(server);
        }
        return Result.WithData(true);
    }

    @RemoteMethod
    @Transactional
    public Result remove(String sid, HttpSession session) {
        Employee employee = getEmployeeFromSession(session);
        if (employee == null) {
            return Result.NoSession();
        }
        if (employee.getRole().getServer() != RoleManager.RolePrivilgeHold) {
            return Result.NoPrivilege();
        }
        Server server = serverDao.get(sid);
        if (server == null) {
            Debug.error("Cannot find a server by this sid.");
            return Result.WithData(false);
        }
        if (server.getDomains() > 0) {
            return Result.WithData(false);
        }
        serverDao.delete(server);
        return Result.WithData(true);
    }

    private boolean generatePublicKeyFile(Server server) {
        if (!server.getUsingPublicKey()) {
            return false;
        }
        String path = configComponent.rootPath + configComponent.PublicKeyFolder;
        FileTool.createDirectoryIfNotExsit(path);
        String pathname = path + File.separator + server.getSid();
        File file = new File(pathname);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            out.write(server.getCredential().getBytes());
            out.close();
            Set<PosixFilePermission> permissions = new HashSet<PosixFilePermission>();
            permissions.add(PosixFilePermission.OWNER_READ);
            permissions.add(PosixFilePermission.OWNER_WRITE);
            Files.setPosixFilePermissions(Paths.get(pathname), permissions);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return file.exists();
    }

}
