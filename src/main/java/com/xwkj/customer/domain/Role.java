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

    // role name
    @Column(nullable = false)
    private String name;

    // privelge to read an undeveloped customer
    @Column(nullable = false)
    private Integer undevelopedR;

    // privelge to create and update an undeveloped customer
    @Column(nullable = false)
    private Integer undevelopedW;

    // privelge to delete an undeveloped customer
    @Column(nullable = false)
    private Integer undevelopedD;

    // privelge to read a developing customer
    @Column(nullable = false)
    private Integer developingR;

    // privelge to create and update a developing customer
    @Column(nullable = false)
    private Integer developingW;

    // privelge to delete a developing customer
    @Column(nullable = false)
    private Integer developingD;

    // privelge to read a developed customer
    @Column(nullable = false)
    private Integer developedR;

    // privelge to create and update a developed customer
    @Column(nullable = false)
    private Integer developedW;

    // privelge to delete a developed customer
    @Column(nullable = false)
    private Integer developedD;

    // privelge to read a lost customer
    @Column(nullable = false)
    private Integer lostR;

    // privelge to create and update a lost customer
    @Column(nullable = false)
    private Integer lostW;

    // privelge to delete a lost customer
    @Column(nullable = false)
    private Integer lostD;

    // privelge to transfer an undeveloped customer to a developing customer
    @Column(nullable = false)
    private Integer develop;

    // privelge to transfer an developing customer to a developed customer
    @Column(nullable = false)
    private Integer finish;

    // privelge to transfer an developed customer to a lost customer
    @Column(nullable = false)
    private Integer ruin;

    // privelge to transfer an lost customer to a developing customer
    @Column(nullable = false)
    private Integer recover;

    // privelge to asign a new manager for the customer
    @Column(nullable = false)
    private Integer assign;

    // privelge to create, update and delete server
    @Column(nullable = false)
    private Integer server;

    // privelge to create, update and delete domain
    @Column(nullable = false)
    private Integer domain;

    // Privilege to manage employee.
    @Column(nullable = false)
    private Integer employee;

    // Numbers of employees of this role.
    @Column(nullable = false)
    private Integer employees;

    // Privilege to view expiration date.
    private Integer expiration;

    // Privilege to edit all works.
    private Integer work;

    // Privilege to edit all bulletins;
    private Integer bulletin;

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

    public Integer getServer() {
        return server;
    }

    public void setServer(Integer server) {
        this.server = server;
    }

    public Integer getDomain() {
        return domain;
    }

    public void setDomain(Integer domain) {
        this.domain = domain;
    }

    public Integer getUndevelopedR() {
        return undevelopedR;
    }

    public void setUndevelopedR(Integer undevelopedR) {
        this.undevelopedR = undevelopedR;
    }

    public Integer getDevelopingR() {
        return developingR;
    }

    public void setDevelopingR(Integer developingR) {
        this.developingR = developingR;
    }

    public Integer getDevelopedR() {
        return developedR;
    }

    public void setDevelopedR(Integer developedR) {
        this.developedR = developedR;
    }

    public Integer getLostR() {
        return lostR;
    }

    public void setLostR(Integer lostR) {
        this.lostR = lostR;
    }

    public Integer getUndevelopedW() {
        return undevelopedW;
    }

    public void setUndevelopedW(Integer undevelopedW) {
        this.undevelopedW = undevelopedW;
    }

    public Integer getDevelopingW() {
        return developingW;
    }

    public void setDevelopingW(Integer developingW) {
        this.developingW = developingW;
    }

    public Integer getDevelopedW() {
        return developedW;
    }

    public void setDevelopedW(Integer developedW) {
        this.developedW = developedW;
    }

    public Integer getLostW() {
        return lostW;
    }

    public void setLostW(Integer lostW) {
        this.lostW = lostW;
    }

    public Integer getUndevelopedD() {
        return undevelopedD;
    }

    public void setUndevelopedD(Integer undevelopedD) {
        this.undevelopedD = undevelopedD;
    }

    public Integer getDevelopingD() {
        return developingD;
    }

    public void setDevelopingD(Integer developingD) {
        this.developingD = developingD;
    }

    public Integer getDevelopedD() {
        return developedD;
    }

    public void setDevelopedD(Integer developedD) {
        this.developedD = developedD;
    }

    public Integer getLostD() {
        return lostD;
    }

    public void setLostD(Integer lostD) {
        this.lostD = lostD;
    }

    public Integer getAssign() {
        return assign;
    }

    public void setAssign(Integer assign) {
        this.assign = assign;
    }

    public Integer getDevelop() {
        return develop;
    }

    public void setDevelop(Integer develop) {
        this.develop = develop;
    }

    public Integer getFinish() {
        return finish;
    }

    public void setFinish(Integer finish) {
        this.finish = finish;
    }

    public Integer getRuin() {
        return ruin;
    }

    public void setRuin(Integer ruin) {
        this.ruin = ruin;
    }

    public Integer getRecover() {
        return recover;
    }

    public void setRecover(Integer recover) {
        this.recover = recover;
    }

    public Integer getEmployee() {
        return employee;
    }

    public void setEmployee(Integer employee) {
        this.employee = employee;
    }

    public Integer getEmployees() {
        return employees;
    }

    public void setEmployees(Integer employees) {
        this.employees = employees;
    }

    public Integer getExpiration() {
        return expiration;
    }

    public void setExpiration(Integer expiration) {
        this.expiration = expiration;
    }

    public Integer getWork() {
        return work;
    }

    public void setWork(Integer work) {
        this.work = work;
    }

    public Integer getBulletin() {
        return bulletin;
    }

    public void setBulletin(Integer bulletin) {
        this.bulletin = bulletin;
    }

    public void setPrivileges(int [] privileges) {
        undevelopedR = privileges[0];
        undevelopedW = privileges[1];
        undevelopedD = privileges[2];
        developingR = privileges[3];
        developingW = privileges[4];
        developingD = privileges[5];
        developedR = privileges[6];
        developedW = privileges[7];
        developedD = privileges[8];
        lostR = privileges[9];
        lostW = privileges[10];
        lostD = privileges[11];
        develop = privileges[12];
        finish = privileges[13];
        ruin = privileges[14];
        recover = privileges[15];
        assign = privileges[16];
        server = privileges[17];
        domain = privileges[18];
        employee = privileges[19];
        expiration = privileges[20];
        work = privileges[21];
        bulletin = privileges[22];
    }

}
