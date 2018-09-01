package com.xwkj.customer.bean;

import com.xwkj.customer.domain.Reply;
import org.directwebremoting.annotations.DataTransferObject;

import java.util.Date;

@DataTransferObject
public class ReplyBean {

    private String rid;
    private Date createAt;
    private String content;
    private EmployeeBean employee;
    private String wid;

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public EmployeeBean getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeBean employee) {
        this.employee = employee;
    }

    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }

    public ReplyBean(Reply reply) {
        this.rid = reply.getRid();
        this.createAt = new Date(reply.getCreateAt());
        this.content = reply.getContent();
        this.employee = new EmployeeBean(reply.getEmployee(), false);
        this.wid = reply.getWork().getWid();
    }

}
