package com.xwkj.customer.bean;

import com.xwkj.customer.domain.Type;
import org.directwebremoting.annotations.DataTransferObject;

import java.util.Date;

@DataTransferObject
public class TypeBean {

    private String tid;
    private String name;
    private Date createAt;

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public TypeBean(Type type) {
        this.tid = type.getTid();
        this.name = type.getName();
        this.createAt = new Date(type.getCreateAt());
    }

}
