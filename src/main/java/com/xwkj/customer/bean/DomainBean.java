package com.xwkj.customer.bean;

import com.xwkj.customer.domain.Domain;
import org.directwebremoting.annotations.DataTransferObject;

import java.util.Date;

@DataTransferObject
public class DomainBean {

    private String did;
    private String name;
    private String domains;
    private String language;
    private String resolution;
    private String path;
    private String remark;
    private boolean highlight;
    private Date createAt;
    private Date updateAt;
    private boolean grabbed;
    private boolean monitoring;
    private int similarity;
    private int frequency;
    private boolean alert;
    private String sid;

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomains() {
        return domains;
    }

    public void setDomains(String domains) {
        this.domains = domains;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isHighlight() {
        return highlight;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
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

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public boolean isGrabbed() {
        return grabbed;
    }

    public void setGrabbed(boolean grabbed) {
        this.grabbed = grabbed;
    }

    public boolean isMonitoring() {
        return monitoring;
    }

    public void setMonitoring(boolean monitoring) {
        this.monitoring = monitoring;
    }

    public int getSimilarity() {
        return similarity;
    }

    public void setSimilarity(int similarity) {
        this.similarity = similarity;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public boolean isAlert() {
        return alert;
    }

    public void setAlert(boolean alert) {
        this.alert = alert;
    }

    public DomainBean(Domain domain) {
        this.did = domain.getDid();
        this.name = domain.getName();
        this.domains = domain.getDomains();
        this.language = domain.getLanguage();
        this.resolution = domain.getResolution();
        this.path = domain.getPath();
        this.remark = domain.getRemark() == null ? "" : domain.getRemark();
        this.highlight = domain.getHighlight();
        this.createAt = new Date(domain.getCreateAt());
        this.updateAt = new Date(domain.getUpdateAt());
        this.sid = domain.getServer().getSid();
        this.grabbed = domain.getGrabbed();
        this.monitoring = domain.getMonitoring();
        this.similarity = domain.getSimilarity();
        this.frequency = domain.getFrequency();
        this.alert = domain.getAlert();
    }

}
