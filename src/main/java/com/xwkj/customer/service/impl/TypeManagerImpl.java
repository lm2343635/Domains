package com.xwkj.customer.service.impl;

import com.sun.org.apache.bcel.internal.generic.FADD;
import com.xwkj.common.util.Debug;
import com.xwkj.customer.bean.Result;
import com.xwkj.customer.bean.TypeBean;
import com.xwkj.customer.domain.Employee;
import com.xwkj.customer.domain.Type;
import com.xwkj.customer.service.CustomerManager;
import com.xwkj.customer.service.RoleManager;
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
    public Result add(String name, int category, HttpSession session) {
        if (!checkAdminSession(session)) {
            return Result.NoSession();
        }
        Type type = new Type();
        type.setName(name);
        type.setCategory(category);
        type.setCreateAt(System.currentTimeMillis());
        return Result.WithData(typeDao.save(type) != null);
    }

    @RemoteMethod
    public Result getByCategory(int category, HttpSession session) {
        if (!checkAdminSession(session) && !checkEmployeeSession(session)) {
            return Result.NoSession();
        }
        List<TypeBean> typeBeans = new ArrayList<TypeBean>();
        for (Type type : typeDao.findByCategory(category)) {
            typeBeans.add(new TypeBean(type));
        }
        return Result.WithData(typeBeans);
    }

    @RemoteMethod
    public Result getAll(HttpSession session) {
        if (!checkAdminSession(session) && !checkEmployeeSession(session)) {
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
        Type type = typeDao.get(tid);
        if (type == null) {
            Debug.error("Cannot find a type by this tid.");
            return Result.WithData(false);
        }
        switch (type.getCategory()) {
            case TypeCategoryReport:
                if (reportDao.getCount(type, null, null, null) > 0) {
                    return Result.WithData(false);
                }
                break;
            case TypeCategoryDocument:
                if (documentDao.getCountByType(type) > 0) {
                    return Result.WithData(false);
                }
                break;
            case TypeCategoryExpiration:
                if (expirationDao.getSearchCount(type, null, null, null) > 0) {
                    return Result.WithData(false);

                }
                break;
        }
        typeDao.delete(type);
        return Result.WithData(true);
    }

}
