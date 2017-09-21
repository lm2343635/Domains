package com.xwkj.customer.bean;

import com.xwkj.customer.domain.Employee;
import org.directwebremoting.annotations.DataTransferObject;

import java.util.Date;

@DataTransferObject
public class EmployeeBean {

    private String eid;
    private Date createAt;
    private Date updateAt;
    private String name;
    private String password;
    private boolean enable;
    private RoleBean role;

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public RoleBean getRole() {
        return role;
    }

    public void setRole(RoleBean role) {
        this.role = role;
    }

    public EmployeeBean(Employee employee) {
        this.eid = employee.getEid();
        this.createAt = new Date(employee.getCreateAt());
        this.updateAt = new Date(employee.getUpdateAt());
        this.name = employee.getName();
        this.password = employee.getPassword();
        this.enable = employee.getEnable();
        this.role = new RoleBean(employee.getRole());
    }

}
