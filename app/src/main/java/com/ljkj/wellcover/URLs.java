package com.ljkj.wellcover;

/**
 * 文件名：URLs
 * 作者：Turbo
 * 时间： 2020-07-30 16:34
 * 蚁穴虽小，溃之千里。
 */
public class URLs {

    public static final String BASEURL = "http://101.200.202.152:8080";

    /**
     * 登录接口
     */
    public static final String LOGIN = BASEURL + "/login/auth";
    public static final String GETINFO = BASEURL + "/login/getInfo";
    public static final String LISTARTICLE = BASEURL + "/article/listArticle";
    public static final String ADDARTICLE = BASEURL + "/article/addArticle";
    public static final String DELETEARTICLE = BASEURL + "/article/deleteArticle";
    public static final String FINDNEARDEVICE = BASEURL + "/article/findNearDevice";
}
