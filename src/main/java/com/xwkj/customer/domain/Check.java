package com.xwkj.customer.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "customer_check")
public class Check implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String cid;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private Long checkAt;

    @Column(nullable = false)
    private Double similarity;

    @Column(nullable = false)
    private Boolean replaced;

    @ManyToOne
    @JoinColumn(name = "did", nullable = false)
    private Domain domain;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getCheckAt() {
        return checkAt;
    }

    public void setCheckAt(Long checkAt) {
        this.checkAt = checkAt;
    }

    public Double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(Double similarity) {
        this.similarity = similarity;
    }

    public Boolean getReplaced() {
        return replaced;
    }

    public void setReplaced(Boolean replaced) {
        this.replaced = replaced;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }
    
}
