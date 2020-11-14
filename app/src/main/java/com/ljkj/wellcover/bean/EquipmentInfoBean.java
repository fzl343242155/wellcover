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
     * deviceType : jg
     * versionNum : 2.1
     * latitude : 116.492536
     * bluetoothMac : 10xx
     * userId : 10003
     * streetName : 缘好来
     * lockStatus : 1
     * createTime : 2020-10-24 11:34:07
     * simInfo : 电信
     * company : null
     * id : Z
     * electricityQuantity : 29
     * longitude : 39.903693
     */

    private String deviceType;
    private String versionNum;
    private double latitude;
    private String bluetoothMac;
    private int userId;
    private String streetName;
    private String lockStatus;
    private String createTime;
    private String simInfo;
    private String company;
    private String id;
    private String electricityQuantity;
    private double longitude;

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getVersionNum() {
        return versionNum;
    }

    public void setVersionNum(String versionNum) {
        this.versionNum = versionNum;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getBluetoothMac() {
        return bluetoothMac;
    }

    public void setBluetoothMac(String bluetoothMac) {
        this.bluetoothMac = bluetoothMac;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

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

    public String getSimInfo() {
        return simInfo;
    }

    public void setSimInfo(String simInfo) {
        this.simInfo = simInfo;
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

    public String getElectricityQuantity() {
        return electricityQuantity;
    }

    public void setElectricityQuantity(String electricityQuantity) {
        this.electricityQuantity = electricityQuantity;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
