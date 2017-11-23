package com.xwkj.customer.bean;

import com.xwkj.customer.domain.Expiration;
import org.directwebremoting.annotations.DataTransferObject;

import java.util.Date;

@DataTransferObject
public class ExpirationBean {

    private String eid;
    private Date createAt;
    private Date updateAt;
    private Date expireAt;
    private int money;
    private TypeBean type;
    private CustomerBean customer;

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

    public Date getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(Date expireAt) {
        this.expireAt = expireAt;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public TypeBean getType() {
        return type;
    }

    public void setType(TypeBean type) {
        this.type = type;
    }

    public CustomerBean getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerBean customer) {
        this.customer = customer;
    }

    public ExpirationBean(Expiration expiration, boolean full) {
        this.eid = expiration.getEid();
        this.createAt = new Date(expiration.getCreateAt());
        this.updateAt = new Date(expiration.getUpdateAt());
        this.expireAt = new Date(expiration.getExpireAt());
        this.money = expiration.getMoney();
        this.type = new TypeBean(expiration.getType());
        if (full) {
            this.customer = new CustomerBean(expiration.getCustomer(), false);
        }
    }

}
