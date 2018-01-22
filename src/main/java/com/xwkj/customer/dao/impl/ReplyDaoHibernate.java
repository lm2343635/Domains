package com.xwkj.customer.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.customer.dao.ReplyDao;
import com.xwkj.customer.domain.Reply;
import org.springframework.stereotype.Repository;

@Repository
public class ReplyDaoHibernate extends BaseHibernateDaoSupport<Reply> implements ReplyDao {

    public ReplyDaoHibernate() {
        super();
        setClass(Reply.class);
    }

}
