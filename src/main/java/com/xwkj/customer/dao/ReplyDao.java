package com.xwkj.customer.dao;

import com.xwkj.common.hibernate.BaseDao;
import com.xwkj.customer.domain.Reply;
import com.xwkj.customer.domain.Work;

import java.util.List;

public interface ReplyDao extends BaseDao<Reply> {

    /**
     * Fina all replies of a work.
     *
     * @param work
     * @return
     */
    List<Reply> findByWork(Work work);

}
