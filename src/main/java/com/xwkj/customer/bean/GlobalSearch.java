package com.xwkj.customer.bean;

import org.directwebremoting.annotations.DataTransferObject;

@DataTransferObject
public class GlobalSearch {

    public final static int GlobalSearchCustomer = 0;
    public final static int GlobalSearchDomain = 1;

    private int type;
    private String description;
    private String id;

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

    public GlobalSearch() {

    }

    public GlobalSearch(int type, String description, String id) {
        this.type = type;
        this.description = description;
        this.id = id;
    }

}
