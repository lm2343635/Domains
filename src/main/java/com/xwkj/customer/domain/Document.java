package com.xwkj.customer.domain;

import com.xwkj.customer.component.ConfigComponent;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.File;
import java.io.Serializable;

@Entity
@Table(name = "customer_document")
public class Document implements Serializable {

    public static final String[] units = {"B", "KB", "MB", "GB", "TB"};

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String did;

    @Column(nullable = false)
    private String store;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private Long size;

    @Column(nullable = false)
    private Long uploadAt;

    @Column(nullable = false)
    private Boolean oss;

    @ManyToOne
    @JoinColumn(name = "cid")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "eid")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "tid")
    private Type type;

    public static String[] getUnits() {
        return units;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getUploadAt() {
        return uploadAt;
    }

    public void setUploadAt(Long uploadAt) {
        this.uploadAt = uploadAt;
    }

    public Boolean getOss() {
        return oss;
    }

    public void setOss(Boolean oss) {
        this.oss = oss;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getSizeString() {
        long size = this.size;
        int unit = 0;
        while (size >= 1024) {
            size /= 1024;
            unit++;
        }
        return size + " " + units[unit];
    }

    public String getPath() {
        String path = "";
        if (customer == null) {
            path += ConfigComponent.PublicDocumentFolder;
        } else {
            path += ConfigComponent.UploadFolder + File.separator + customer.getCid();
        }
        path += File.separator + store;
        return path;
    }

}
