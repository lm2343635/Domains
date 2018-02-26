package com.xwkj.customer.bean;

import com.xwkj.customer.domain.Document;
import org.directwebremoting.annotations.DataTransferObject;

import java.util.Date;

@DataTransferObject
public class DocumentBean {

    private String did;
    private String filename;
    private String store;
    private String size;
    private Date uploadAt;
    private String employee;
    private String cid;
    private TypeBean type;

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Date getUploadAt() {
        return uploadAt;
    }

    public void setUploadAt(Date uploadAt) {
        this.uploadAt = uploadAt;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public TypeBean getType() {
        return type;
    }

    public void setType(TypeBean type) {
        this.type = type;
    }

    public DocumentBean(Document document, boolean full) {
        this.did = document.getDid();
        this.filename = document.getFilename();
        this.size = document.getSizeString();
        this.uploadAt = new Date(document.getUploadAt());
        this.employee = document.getEmployee().getName();
        this.cid = document.getCustomer() == null ? null : document.getCustomer().getCid();
        this.type = document.getType() == null ? null : new TypeBean(document.getType());
        if (full) {
            this.store = document.getStore();
        }
    }

}
