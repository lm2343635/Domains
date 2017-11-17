package com.xwkj.customer.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "customer_expiration")
public class Expiration implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String tid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long createAt;

    @Column(nullable = false)
    private Long expireAt;

    @ManyToOne
    @JoinColumn(name = "tid", nullable = false)
    private Type type;

    @ManyToOne
    @JoinColumn(name = "cid", nullable = false)
    private Customer customer;

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

    public Long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Long createAt) {
        this.createAt = createAt;
    }

    public Long getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(Long expireAt) {
        this.expireAt = expireAt;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

}
