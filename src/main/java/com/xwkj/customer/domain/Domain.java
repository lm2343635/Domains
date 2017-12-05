package com.xwkj.customer.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "customer_domain")
public class Domain implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String did;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String domains;

    @Column(nullable = false)
    private String language;

    @Column(nullable = false)
    private String resolution;

    @Column(nullable = false)
    private String path;

    @Column
    private String remark;

    @Column(nullable = false)
    private Boolean highlight;

    @Column(nullable = false)
    private Integer state;

    @Column(nullable = false)
    private Long createAt;

    @Column(nullable = false)
    private Long updateAt;

    @Column(columnDefinition = "LONGTEXT")
    private String index;

    @Column(columnDefinition = "LONGTEXT")
    private String check;

    @Column
    private Long checkAt;

    @Column
    private Integer similarity;

    @ManyToOne
    @JoinColumn(name = "sid", nullable = false)
    private Server server;

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

    public Boolean getHighlight() {
        return highlight;
    }

    public void setHighlight(Boolean highlight) {
        this.highlight = highlight;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
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

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public Long getCheckAt() {
        return checkAt;
    }

    public void setCheckAt(Long checkAt) {
        this.checkAt = checkAt;
    }

    public Integer getSimilarity() {
        return similarity;
    }

    public void setSimilarity(Integer similarity) {
        this.similarity = similarity;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

}
