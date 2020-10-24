package com.ljkj.wellcover.bean;

import java.io.Serializable;

/**
 * 文件名：EquipmentInfoBean
 * 作者：Turbo
 * 时间： 2020-10-24 20:39
 * 蚁穴虽小，溃之千里。
 */
public class EquipmentInfoBean implements Serializable {

    /**
     * streetName : 步行街1
     * lockStatus : 2
     * createTime : 2020-10-08 09:57:59
     * company : 大乐透1
     * id : 99xxxxxx
     * userId : 10003
     * dimension : 11112
     * longitude : 121
     */

    private String streetName;
    private String lockStatus;
    private String createTime;
    private String company;
    private String id;
    private int userId;
    private double latitude;
    private double longitude;

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(String lockStatus) {
        this.lockStatus = lockStatus;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
