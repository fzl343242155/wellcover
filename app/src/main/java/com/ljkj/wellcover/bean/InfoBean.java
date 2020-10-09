package com.ljkj.wellcover.bean;

import java.util.List;

/**
 * 文件名：InfoBean
 * 作者：Turbo
 * 时间： 2020-10-09 11:09
 * 蚁穴虽小，溃之千里。
 */
public class InfoBean {

    /**
     * userPermission : {"menuList":["role","user","article"],"roleId":1,"nickname":"超级用户","roleName":"管理员","permissionList":["article:list","user:list","user:add","role:update","article:add","article:delete","role:list","article:update","user:update","role:delete","role:add"],"userId":10003}
     */

    private UserPermissionBean userPermission;

    public UserPermissionBean getUserPermission() {
        return userPermission;
    }

    public void setUserPermission(UserPermissionBean userPermission) {
        this.userPermission = userPermission;
    }

    public static class UserPermissionBean {
        /**
         * menuList : ["role","user","article"]
         * roleId : 1
         * nickname : 超级用户
         * roleName : 管理员
         * permissionList : ["article:list","user:list","user:add","role:update","article:add","article:delete","role:list","article:update","user:update","role:delete","role:add"]
         * userId : 10003
         */

        private int roleId;
        private String nickname;
        private String roleName;
        private int userId;
        private List<String> menuList;
        private List<String> permissionList;

        public int getRoleId() {
            return roleId;
        }

        public void setRoleId(int roleId) {
            this.roleId = roleId;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getRoleName() {
            return roleName;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public List<String> getMenuList() {
            return menuList;
        }

        public void setMenuList(List<String> menuList) {
            this.menuList = menuList;
        }

        public List<String> getPermissionList() {
            return permissionList;
        }

        public void setPermissionList(List<String> permissionList) {
            this.permissionList = permissionList;
        }
    }
}
