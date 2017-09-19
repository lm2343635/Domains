package com.xwkj.customer.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "customer_role")
public class Role implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String rid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Boolean employee;

    @Column(nullable = false)
    private Boolean server;

    @Column(nullable = false)
    private Boolean domain;

    @Column(nullable = false)
    private Boolean undeveloped;

    @Column(nullable = false)
    private Boolean developing;

    @Column(nullable = false)
    private Boolean developed;

    @Column(nullable = false)
    private Boolean lost;

    @Column(nullable = false)
    private Boolean assign;

    @Column(nullable = false)
    private Boolean ruin;

    @Column(nullable = false)
    private Boolean recover;

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getEmployee() {
        return employee;
    }

    public void setEmployee(Boolean employee) {
        this.employee = employee;
    }

    public Boolean getServer() {
        return server;
    }

    public void setServer(Boolean server) {
        this.server = server;
    }

    public Boolean getDomain() {
        return domain;
    }

    public void setDomain(Boolean domain) {
        this.domain = domain;
    }

    public Boolean getUndeveloped() {
        return undeveloped;
    }

    public void setUndeveloped(Boolean undeveloped) {
        this.undeveloped = undeveloped;
    }

    public Boolean getDeveloping() {
        return developing;
    }

    public void setDeveloping(Boolean developing) {
        this.developing = developing;
    }

    public Boolean getDeveloped() {
        return developed;
    }

    public void setDeveloped(Boolean developed) {
        this.developed = developed;
    }

    public Boolean getLost() {
        return lost;
    }

    public void setLost(Boolean lost) {
        this.lost = lost;
    }

    public Boolean getAssign() {
        return assign;
    }

    public void setAssign(Boolean assign) {
        this.assign = assign;
    }

    public Boolean getRuin() {
        return ruin;
    }

    public void setRuin(Boolean ruin) {
        this.ruin = ruin;
    }

    public Boolean getRecover() {
        return recover;
    }

    public void setRecover(Boolean recover) {
        this.recover = recover;
    }
}
