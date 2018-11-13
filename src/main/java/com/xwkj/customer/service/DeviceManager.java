package com.xwkj.customer.service;

import com.xwkj.customer.bean.DeviceBean;

import java.util.List;

public interface DeviceManager {

    public static final String iOSDevice = "iOS";
    public static final String AndroidDevice = "Android";

    /**
     * Register a device.
     *
     * @param identifier
     * @param os
     * @param version
     * @param lan
     * @param deviceToken
     * @param ip
     * @param eid
     * @return
     */
    String registerDevice(String identifier, String os, String version, String lan, String deviceToken, String ip, String eid);

    /**
     * Device OS must be iOS or Andriod
     *
     * @param os
     * @return
     */
    boolean isLegalDevice(String os);


    /**
     * Update device's device token of a user.
     *
     * @param deviceToken
     * @param token
     */
    boolean updateDeviceToken(String deviceToken, String token);

    /**
     * Auth and return the did for a device.
     *
     * @param token
     * @return
     */
    DeviceBean authDevice(String token);

    /**
     * Get all devices of a user by employee id.
     *
     * @param eid
     * @return
     */
    List<DeviceBean> getDevicesByEid(String eid);

}
