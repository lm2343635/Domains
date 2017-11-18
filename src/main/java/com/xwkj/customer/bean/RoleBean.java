package com.xwkj.customer.bean;

import com.xwkj.customer.domain.Role;
import org.directwebremoting.annotations.DataTransferObject;

@DataTransferObject
public class RoleBean {

    private String rid;
    private String name;

    private int undevelopedR;
    private int undevelopedW;
    private int undevelopedD;
    private int developingR;
    private int developingW;
    private int developingD;
    private int developedR;
    private int developedW;
    private int developedD;
    private int lostR;
    private int lostW;
    private int lostD;
    private int develop;
    private int finish;
    private int ruin;
    private int recover;
    private int assign;
    private int server;
    private int domain;
    private int employee;
    private int expiration;

    private int employees;

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

    public int getUndevelopedR() {
        return undevelopedR;
    }

    public void setUndevelopedR(int undevelopedR) {
        this.undevelopedR = undevelopedR;
    }

    public int getUndevelopedW() {
        return undevelopedW;
    }

    public void setUndevelopedW(int undevelopedW) {
        this.undevelopedW = undevelopedW;
    }

    public int getUndevelopedD() {
        return undevelopedD;
    }

    public void setUndevelopedD(int undevelopedD) {
        this.undevelopedD = undevelopedD;
    }

    public int getDevelopingR() {
        return developingR;
    }

    public void setDevelopingR(int developingR) {
        this.developingR = developingR;
    }

    public int getDevelopingW() {
        return developingW;
    }

    public void setDevelopingW(int developingW) {
        this.developingW = developingW;
    }

    public int getDevelopingD() {
        return developingD;
    }

    public void setDevelopingD(int developingD) {
        this.developingD = developingD;
    }

    public int getDevelopedR() {
        return developedR;
    }

    public void setDevelopedR(int developedR) {
        this.developedR = developedR;
    }

    public int getDevelopedW() {
        return developedW;
    }

    public void setDevelopedW(int developedW) {
        this.developedW = developedW;
    }

    public int getDevelopedD() {
        return developedD;
    }

    public void setDevelopedD(int developedD) {
        this.developedD = developedD;
    }

    public int getLostR() {
        return lostR;
    }

    public void setLostR(int lostR) {
        this.lostR = lostR;
    }

    public int getLostW() {
        return lostW;
    }

    public void setLostW(int lostW) {
        this.lostW = lostW;
    }

    public int getLostD() {
        return lostD;
    }

    public void setLostD(int lostD) {
        this.lostD = lostD;
    }

    public int getDevelop() {
        return develop;
    }

    public void setDevelop(int develop) {
        this.develop = develop;
    }

    public int getFinish() {
        return finish;
    }

    public void setFinish(int finish) {
        this.finish = finish;
    }

    public int getRuin() {
        return ruin;
    }

    public void setRuin(int ruin) {
        this.ruin = ruin;
    }

    public int getRecover() {
        return recover;
    }

    public void setRecover(int recover) {
        this.recover = recover;
    }

    public int getAssign() {
        return assign;
    }

    public void setAssign(int assign) {
        this.assign = assign;
    }

    public int getServer() {
        return server;
    }

    public void setServer(int server) {
        this.server = server;
    }

    public int getDomain() {
        return domain;
    }

    public void setDomain(int domain) {
        this.domain = domain;
    }

    public int getEmployee() {
        return employee;
    }

    public void setEmployee(int employee) {
        this.employee = employee;
    }

    public int getExpiration() {
        return expiration;
    }

    public void setExpiration(int expiration) {
        this.expiration = expiration;
    }

    public int getEmployees() {
        return employees;
    }

    public void setEmployees(int employees) {
        this.employees = employees;
    }

    public RoleBean(Role role) {
        this.rid = role.getRid();
        this.name = role.getName();
        this.undevelopedR = role.getUndevelopedR();
        this.undevelopedW = role.getUndevelopedW();
        this.undevelopedD = role.getUndevelopedD();
        this.developingR = role.getDevelopingR();
        this.developingW = role.getDevelopingW();
        this.developingD = role.getDevelopingD();
        this.developedR = role.getDevelopedR();
        this.developedW = role.getDevelopedW();
        this.developedD = role.getDevelopedD();
        this.lostR = role.getLostR();
        this.lostW = role.getLostW();
        this.lostD = role.getLostD();
        this.develop = role.getDevelop();
        this.finish = role.getFinish();
        this.ruin = role.getRuin();
        this.recover = role.getRecover();
        this.assign = role.getAssign();
        this.server = role.getServer();
        this.domain = role.getDomain();
        this.employee = role.getEmployee();
        this.expiration = role.getExpiration();
        this.employees = role.getEmployees();
    }

}
