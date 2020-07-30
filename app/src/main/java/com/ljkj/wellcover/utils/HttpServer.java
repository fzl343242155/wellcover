package com.ljkj.wellcover.utils;

import com.google.gson.reflect.TypeToken;
import com.ljkj.wellcover.URLs;
import com.ljkj.wellcover.bean.BaseData;
import com.lzy.okgo.OkGo;
import com.lzy.okrx.adapter.ObservableBody;

import rx.Observable;

/**
 * 文件名：HttpServer
 * 作者：Turbo
 * 时间： 2020-07-29 17:27
 * 蚁穴虽小，溃之千里。
 */
public class HttpServer {

    private HttpServer() {
    }

    private static class SingletonHolder {
        private static final HttpServer INSTANCE = new HttpServer();
    }

    public static HttpServer $() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 登录接口
     *
     * @return
     */
    public Observable<BaseData<String>> onLogin(String acc_num, String code) {
        return OkGo.<BaseData<String>>post(URLs.LOGIN)
                .params("acc_num", acc_num)
                .params("code", code)
                .converter(new GenericConverter<BaseData<String>>((new TypeToken<BaseData<String>>() {
                }.getType())))
                .adapt(new ObservableBody<BaseData<String>>());
    }



}
