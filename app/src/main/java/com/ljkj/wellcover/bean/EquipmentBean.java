package com.ljkj.wellcover.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 文件名：EquipmentBean
 * 作者：Turbo
 * 时间： 2020-10-09 10:54
 * 蚁穴虽小，溃之千里。
 */
public class EquipmentBean implements Serializable {


    /**
     * totalPage : 1
     * list : [{"streetName":"步行街1","lockStatus":"2","createTime":"2020-10-08 09:57:59","company":"大乐透1","id":"99xxxxxx","userId":10003,"dimension":"11112","longitude":"121"},{"streetName":"步行街2222","lockStatus":"2","createTime":"2020-10-08 10:17:01","company":"大乐透2","id":"99xxx1111xxx","userId":10003,"dimension":"1112","longitude":"122"},{"streetName":"双井","lockStatus":"1","createTime":"2020-10-09 09:08:32","company":"大连xx公司","id":"89o0000111","userId":10008,"dimension":"89.2","longitude":"12.1"},{"streetName":"wc","lockStatus":"1","createTime":"2020-10-08 11:35:27","company":"bj","id":"77777","userId":10008,"dimension":"12","longitude":"89"},{"streetName":"","lockStatus":"3","createTime":"2020-10-08 11:23:35","company":"","id":"1111111111","userId":10003,"dimension":"","longitude":""}]
     * totalCount : 5
     */

    private int totalPage;
    private int totalCount;
    private List<ListBean> list;

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Serializable{
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
}
