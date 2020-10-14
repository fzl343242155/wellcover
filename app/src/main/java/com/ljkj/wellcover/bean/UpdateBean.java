package com.ljkj.wellcover.bean;

/**
 * 文件名：UpdateBean
 * 作者：Turbo
 * 时间： 2020-10-14 14:31
 * 蚁穴虽小，溃之千里。
 */
public class UpdateBean {

    /**
     * data : {"content":"更新内容如下：\n   1.xxxxxx;\n   2.xxxxxx;\n   3.xxxxxx;\n","id":"1","api_key":"android","version_code":"2","version_name":"1.0.2"}
     * msg : 获取成功
     * status : 1
     */

    private DataBean data;
    private String msg;
    private int status;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class DataBean {
        /**
         * content : 更新内容如下：
         1.xxxxxx;
         2.xxxxxx;
         3.xxxxxx;
         * id : 1
         * api_key : android
         * version_code : 2
         * version_name : 1.0.2
         */

        private String content;
        private String id;
        private String api_key;
        private int version_code;
        private String version_name;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getApi_key() {
            return api_key;
        }

        public void setApi_key(String api_key) {
            this.api_key = api_key;
        }

        public int getVersion_code() {
            return version_code;
        }

        public void setVersion_code(int version_code) {
            this.version_code = version_code;
        }

        public String getVersion_name() {
            return version_name;
        }

        public void setVersion_name(String version_name) {
            this.version_name = version_name;
        }
    }
}
