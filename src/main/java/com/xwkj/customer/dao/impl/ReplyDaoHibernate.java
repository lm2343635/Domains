package com.xwkj.customer.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.customer.dao.ReplyDao;
import com.xwkj.customer.domain.Reply;
import com.xwkj.customer.domain.Work;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReplyDaoHibernate extends BaseHibernateDaoSupport<Reply> implements ReplyDao {

    public ReplyDaoHibernate() {
        super();
        setClass(Reply.class);
    }

    public List<Reply> findByWork(Work work) {
        String hql = "from Reply where work = ? order by createAt";
        return (List<Reply>) getHibernateTemplate().find(hql, work);
    }

}
