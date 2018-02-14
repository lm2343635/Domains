package com.xwkj.customer.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "customer_report")
public class Report implements Serializable {

    /**
     * Change key name from lid to rid.
     ALTER TABLE `customer`.`customer_report`
     CHANGE COLUMN `lid` `rid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL FIRST,
     DROP PRIMARY KEY,
     ADD PRIMARY KEY (`rid`) USING BTREE;
     */
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String rid;

    @Column(nullable = false)
    private Long createAt;

    @Column(nullable = false)
    private Long updateAt;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "eid", nullable = false)
    private Employee employee;

    /**
     * Update tid for all reports;
     update customer_report set tid = '402885ec617332dc0161733323f90000';
     */
    @ManyToOne
    @JoinColumn(name = "tid", nullable = false)
    private Type type;

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public Long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Long createAt) {
        this.createAt = createAt;
    }

    public Long getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Long updateAt) {
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

}
