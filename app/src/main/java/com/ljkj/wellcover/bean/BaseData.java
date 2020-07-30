package com.ljkj.wellcover.bean;

import java.io.Serializable;

/**
 * 文件名：BaseData
 * 作者：Turbo
 * 时间： 2020-07-30 17:29
 * 蚁穴虽小，溃之千里。
 */
public class BaseData<T> implements Serializable {

    private String code;
    private String msg;
    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
