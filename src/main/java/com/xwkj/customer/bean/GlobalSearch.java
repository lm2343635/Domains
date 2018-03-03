package com.xwkj.customer.bean;

import org.directwebremoting.annotations.DataTransferObject;

import java.util.Date;

@DataTransferObject
public class GlobalSearch {

    public final static int GlobalSearchCustomer = 0;
    public final static int GlobalSearchDomain = 1;

    private int type;
    private String description;
    private Date updateAt;
    private String id;
    private String parentId;

    public static int getGlobalSearchCustomer() {
        return GlobalSearchCustomer;
    }

    public static int getGlobalSearchDomain() {
        return GlobalSearchDomain;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public GlobalSearch() {

    }

    public GlobalSearch(int type, String description, Date updateAt, String id) {
        this.type = type;
        this.description = description;
        this.id = id;
        this.updateAt = updateAt;
    }

    public GlobalSearch(int type, String description, Date updateAt, String id, String parentId) {
        this.type = type;
        this.description = description;
        this.updateAt = updateAt;
        this.id = id;
        this.parentId = parentId;
    }
}
