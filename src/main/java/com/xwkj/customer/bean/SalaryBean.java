package com.xwkj.customer.bean;

import com.xwkj.customer.domain.Salary;
import org.directwebremoting.annotations.DataTransferObject;

import java.util.Date;

@DataTransferObject
public class SalaryBean {

    private String sid;
    private Date createAt;
    private String remark;
    private int money;
    private String detail;
    private String eid;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public SalaryBean(Salary salary, boolean full) {
        this.sid = salary.getSid();
        this.createAt = new Date(salary.getCreateAt());
        this.remark = salary.getRemark();
        this.money = salary.getMoney();
        this.eid = salary.getEmployee().getEid();
        if (full) {
            this.detail = salary.getDetail();
        }
    }

}
