package com.xwkj.customer.bean;

import com.xwkj.customer.domain.Work;
import org.directwebremoting.annotations.DataTransferObject;

import java.util.Date;

@DataTransferObject
public class WorkBean {

    private String wid;
    private Date createAt;
    private String title;
    private boolean active;
    private EmployeeBean sponsor;
    private EmployeeBean executor;

    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public EmployeeBean getSponsor() {
        return sponsor;
    }

    public void setSponsor(EmployeeBean sponsor) {
        this.sponsor = sponsor;
    }

    public EmployeeBean getExecutor() {
        return executor;
    }

    public void setExecutor(EmployeeBean executor) {
        this.executor = executor;
    }

    public WorkBean(Work work) {
        this.wid = work.getWid();
        this.createAt = new Date(work.getCreateAt());
        this.title = work.getTitle();
        this.active = work.getActive();
        this.sponsor = new EmployeeBean(work.getSponsor(), false);
        this.executor = new EmployeeBean(work.getExecutor(), false);
    }

}
