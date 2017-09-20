package com.xwkj.customer.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "customer_assign")
public class Assign implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String aid;

    @Column(nullable = false)
    private Long createAt;

    @Column(nullable = false)
    private Boolean r;

    @Column(nullable = false)
    private Boolean w;

    @Column(nullable = false)
    private Boolean d;

    @Column(nullable = false)
    private Boolean assign;

    @ManyToOne
    @JoinColumn(name = "cid", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "eid", nullable = false)
    private Employee employee;

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public Long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Long createAt) {
        this.createAt = createAt;
    }

    public Boolean getR() {
        return r;
    }

    public void setR(Boolean r) {
        this.r = r;
    }

    public Boolean getW() {
        return w;
    }

    public void setW(Boolean w) {
        this.w = w;
    }

    public Boolean getD() {
        return d;
    }

    public void setD(Boolean d) {
        this.d = d;
    }

    public Boolean getAssign() {
        return assign;
    }

    public void setAssign(Boolean assign) {
        this.assign = assign;
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

}
