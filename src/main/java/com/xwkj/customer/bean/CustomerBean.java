package com.xwkj.customer.bean;

import com.xwkj.customer.domain.Customer;
import org.directwebremoting.annotations.DataTransferObject;

import java.util.Date;
import java.util.List;

@DataTransferObject
public class CustomerBean {

    private String cid;
    private Date createAt;
    private Date updateAt;
    private int state;
    private String name;
    private int capital;
    private String contact;
    private String items;
    private int money;
    private String remark;
    private String document;
    private AreaBean area;
    private IndustryBean industry;
    private EmployeeBean register;
    private List<EmployeeBean> managers;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapital() {
        return capital;
    }

    public void setCapital(int capital) {
        this.capital = capital;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<EmployeeBean> getManagers() {
        return managers;
    }

    public void setManagers(List<EmployeeBean> managers) {
        this.managers = managers;
    }

    public AreaBean getArea() {
        return area;
    }

    public void setArea(AreaBean area) {
        this.area = area;
    }

    public IndustryBean getIndustry() {
        return industry;
    }

    public void setIndustry(IndustryBean industry) {
        this.industry = industry;
    }

    public EmployeeBean getRegister() {
        return register;
    }

    public void setRegister(EmployeeBean register) {
        this.register = register;
    }

    public CustomerBean(Customer customer, boolean full) {
        this.cid = customer.getCid();
        this.createAt = new Date(customer.getCreateAt());
        this.updateAt = new Date(customer.getUpdateAt());
        this.state = customer.getState();
        this.name = customer.getName();
        this.capital = customer.getCapital();
        this.area = new AreaBean(customer.getArea());
        this.industry = new IndustryBean(customer.getIndustry());
        if (full) {
            this.contact = customer.getContact();
            this.items = customer.getItems();
            this.money = customer.getMoney() == null ? 0 : customer.getMoney();
            this.document = customer.getDocument();
            this.remark = customer.getRemark();
            this.register = new EmployeeBean(customer.getRegister(), true);
        }
    }
}
