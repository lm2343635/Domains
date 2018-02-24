package com.xwkj.customer.bean;

import com.xwkj.customer.domain.Server;
import org.directwebremoting.annotations.DataTransferObject;

import java.util.Date;

@DataTransferObject
public class ServerBean {

    private String sid;
    private String name;
    private String address;
    private Date createAt;
    private Date updateAt;
    private int domains;
    private String user;
    private String remark;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public int getDomains() {
        return domains;
    }

    public void setDomains(int domains) {
        this.domains = domains;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public ServerBean(Server server) {
        this.sid = server.getSid();
        this.name = server.getName();
        this.address = server.getAddress();
        this.createAt = new Date(server.getCreateAt());
        this.updateAt = new Date(server.getUpdateAt());
        this.domains = server.getDomains();
        this.user = server.getUser();
        this.remark = server.getRemark() == null ? "" : server.getRemark();
    }

}
