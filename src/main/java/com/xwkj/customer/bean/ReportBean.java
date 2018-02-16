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
    private EmployeeBean employee;
    private TypeBean type;

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

    public EmployeeBean getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeBean employee) {
        this.employee = employee;
    }

    public TypeBean getType() {
        return type;
    }

    public void setType(TypeBean type) {
        this.type = type;
    }

    public ReportBean(Report report, boolean full) {
        this.rid = report.getRid();
        this.createAt = new Date(report.getCreateAt());
        this.updateAt = new Date(report.getUpdateAt());
        this.title = report.getTitle();
        this.employee = new EmployeeBean(report.getEmployee(), false);
        this.type = new TypeBean(report.getType());
        if (full) {
            this.content = report.getContent();
        }
    }

}
