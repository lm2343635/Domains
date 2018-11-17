package com.xwkj.customer.service.impl;

import com.xwkj.common.util.Debug;
import com.xwkj.customer.bean.DeviceBean;
import com.xwkj.customer.domain.Device;
import com.xwkj.customer.domain.Employee;
import com.xwkj.customer.service.DeviceManager;
import com.xwkj.customer.service.common.ManagerTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DeviceManagerImpl extends ManagerTemplate implements DeviceManager {

    @Transactional
    public String registerDevice(String identifier, String os, String version, String lan, String deviceToken, String ip, String eid) {
        Employee employee = employeeDao.get(eid);
        if (employee == null) {
            Debug.error("Cannot find an employee by this eid.");
            return null;
        }
        Device device = deviceDao.getByIdentifier(identifier);
        // If device info cannot be found, create a new device.
        if (device == null) {
            device = new Device();
            device.setCreateAt(System.currentTimeMillis());
            device.setUpdateAt(device.getCreateAt());
            device.setIdentifier(identifier);
            device.setOs(os);
            device.setVersion(version);
            device.setLan(lan);
            device.setDeviceToken(deviceToken);
            device.setIp(ip);
            device.setEmployee(employee);
            device.setToken(UUID.randomUUID().toString());
            if (deviceDao.save(device) == null) {
                return null;
            }
        } else {
            // Device info found, update device info and create new access token.
            device.setUpdateAt(System.currentTimeMillis());
            device.setOs(os);
            device.setLan(lan);
            device.setDeviceToken(deviceToken);
            device.setIp(ip);
            device.setEmployee(employee);
            device.setToken(UUID.randomUUID().toString());
            deviceDao.update(device);

        }
        return device.getToken();
    }

    public boolean isLegalDevice(String os) {
        return os.equals(iOSDevice) || os.equals(AndroidDevice);
    }

    @Transactional
    public boolean updateDeviceToken(String deviceToken, String token) {
        Device device = deviceDao.getByToken(token);
        if (device == null) {
            Debug.error("Cannot find a device by this token.");
            return false;
        }
        device.setDeviceToken(deviceToken);
        deviceDao.update(device);
        return true;
    }

    public DeviceBean authDevice(String token) {
        Device device = deviceDao.getByToken(token);
        if (device == null) {
            Debug.error("Cannot find a device by this token.");
            return null;
        }
        return new DeviceBean(device);
    }

    public List<DeviceBean> getDevicesByEid(String eid) {
        Employee employee = employeeDao.get(eid);
        if (employee == null) {
            Debug.error("Cannot find an employee by this eid.");
            return null;
        }
        return deviceDao.findByEmployee(employee).stream().map(device -> {
            return new DeviceBean(device);
        }).collect(Collectors.toList());
    }

}
