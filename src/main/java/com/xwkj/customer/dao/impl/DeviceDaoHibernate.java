package com.xwkj.customer.dao.impl;

import com.xwkj.common.hibernate.BaseHibernateDaoSupport;
import com.xwkj.customer.dao.DeviceDao;
import com.xwkj.customer.domain.Device;
import com.xwkj.customer.domain.Employee;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DeviceDaoHibernate extends BaseHibernateDaoSupport<Device> implements DeviceDao {

    public DeviceDaoHibernate() {
        super();
        setClass(Device.class);
    }

    public Device getByIdentifier(String identifier) {
        String hql = "from Device where identifier = ?";
        List<Device> devices = (List<Device>)getHibernateTemplate().find(hql, identifier);
        if (devices.size() == 0) {
            return null;
        }
        return devices.get(0);
    }

    public Device getByToken(String token) {
        String hql = "from Device where token =?";
        List<Device> devices = (List<Device>)getHibernateTemplate().find(hql, token);
        if (devices.size() == 0) {
            return null;
        }
        return devices.get(0);
    }

    public Device getByDeviceToken(String deviceToken) {
        String hql = "from Device where deviceToken =?";
        List<Device> devices = (List<Device>)getHibernateTemplate().find(hql, deviceToken);
        if (devices.size() == 0) {
            return null;
        }
        return devices.get(0);
    }

    public List<Device> findByEmployee(Employee employee) {
        String hql = "from Device where employee = ?";
        return (List<Device>)getHibernateTemplate().find(hql, employee);
    }

}
