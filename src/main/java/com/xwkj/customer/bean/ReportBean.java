package com.xwkj.customer.bean;

import com.xwkj.customer.domain.Report;
import org.directwebremoting.annotations.DataTransferObject;

import java.util.Date;

@DataTransferObject
public class ReportBean {

    private String rid;
    private Date createAt;
    private Date updateAt;
    private String title;
    private String content;
    private String employee;

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

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public ReportBean(Report report, boolean full) {
        this.rid = report.getLid();
        this.createAt = new Date(report.getCreateAt());
        this.updateAt = new Date(report.getUpdateAt());
        this.title = report.getTitle();
        this.employee = report.getEmployee().getName();
        if (full) {
            this.content = report.getContent();
        }
    }

}
