package com.xwkj.customer.service.impl;

import com.xwkj.customer.bean.Result;
import com.xwkj.customer.bean.TypeBean;
import com.xwkj.customer.domain.Type;
import com.xwkj.customer.service.TypeManager;
import com.xwkj.customer.service.common.ManagerTemplate;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
@RemoteProxy(name = "TypeManager")
public class TypeManagerImpl extends ManagerTemplate implements TypeManager {

    @RemoteMethod
    @Transactional
    public Result add(String name, HttpSession session) {
        if (!checkAdminSession(session)) {
            return Result.NoSession();
        }
        Type type = new Type();
        type.setName(name);
        type.setCreateAt(System.currentTimeMillis());
        return Result.WithData(typeDao.save(type));
    }

    @RemoteMethod
    public Result getAll(HttpSession session) {
        if (!checkAdminSession(session)) {
            return Result.NoSession();
        }
        List<TypeBean> typeBeans = new ArrayList<TypeBean>();
        for (Type type : typeDao.findAll("createAt", true)) {
            typeBeans.add(new TypeBean(type));
        }
        return Result.WithData(typeBeans);
    }

    @RemoteMethod
    @Transactional
    public Result remove(String tid, HttpSession session) {
        if (!checkAdminSession(session)) {
            return Result.NoSession();
        }
        try {
            typeDao.delete(tid);
            return Result.WithData(true);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.WithData(false);
        }
    }

}
