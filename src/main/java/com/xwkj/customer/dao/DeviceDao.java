package com.xwkj.customer.dao;

import com.xwkj.common.hibernate.BaseDao;
import com.xwkj.customer.domain.Device;
import com.xwkj.customer.domain.Employee;

import java.util.List;

public interface DeviceDao extends BaseDao<Device> {

    /**
     * Get a device by identifier.
     * @param identifier
     * @return
     */
    Device getByIdentifier(String identifier);

    /**
     * Get a device by login token
     * @param token
     * @return
     */
    Device getByToken(String token);

    /**
     * Get a device by device token.
     *
     * @param deviceToken
     * @return
     */
    Device getByDeviceToken(String deviceToken);

    /**
     * Find all devices of a employee.
     *
     * @param employee
     * @return
     */
    List<Device> findByEmployee(Employee employee);

}
