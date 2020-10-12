package com.ljkj.wellcover.utils;

import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ljkj.wellcover.URLs;
import com.ljkj.wellcover.bean.BaseData;
import com.ljkj.wellcover.bean.EquipmentBean;
import com.ljkj.wellcover.bean.InfoBean;
import com.ljkj.wellcover.bean.LoginBean;
import com.lzy.okgo.OkGo;
import com.lzy.okrx.adapter.ObservableBody;

import java.util.HashMap;
import java.util.Map;

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
    public Observable<BaseData<LoginBean>> onLogin(String username, String password) {
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        return OkGo.<BaseData<LoginBean>>post(URLs.LOGIN)
                .upJson(new Gson().toJson(map))
                .converter(new GenericConverter<BaseData<LoginBean>>((new TypeToken<BaseData<LoginBean>>() {
                }.getType())))
                .adapt(new ObservableBody<BaseData<LoginBean>>());
    }

    /**
     * 获取info
     *
     * @return
     */
    public Observable<BaseData<InfoBean>> getInfo() {
        return OkGo.<BaseData<InfoBean>>post(URLs.GETINFO)
                .headers("Cookie", SPUtils.getInstance().getString(ConstantUtils.COOKIE))
                .converter(new GenericConverter<BaseData<InfoBean>>((new TypeToken<BaseData<InfoBean>>() {
                }.getType())))
                .adapt(new ObservableBody<BaseData<InfoBean>>());
    }

    /**
     * 设备列表
     *
     * @return
     */
    public Observable<BaseData<EquipmentBean>> onEquipmentList(int page) {
        return OkGo.<BaseData<EquipmentBean>>get(URLs.LISTARTICLE)
                .headers("Cookie", SPUtils.getInstance().getString(ConstantUtils.COOKIE))
                .params("pageNum", page)
                .params("pageRow", 10)
                .converter(new GenericConverter<BaseData<EquipmentBean>>((new TypeToken<BaseData<EquipmentBean>>() {
                }.getType())))
                .adapt(new ObservableBody<BaseData<EquipmentBean>>());
    }

    /**
     * 添加设备
     *
     * @return
     */
    public Observable<BaseData> addArticle(String id, String latitude, String longitude, String streetName) {
        Map<String, String> map = new HashMap<>();
        map.put("company", "天津瓴建科技有限公司");
        map.put("id", id);
        map.put("latitude", latitude);
        map.put("lockStatus", "1");
        map.put("longitude", longitude);
        map.put("streetName", streetName);
        return OkGo.<BaseData>post(URLs.ADDARTICLE)
                .headers("Cookie", SPUtils.getInstance().getString(ConstantUtils.COOKIE))
                .upJson(new Gson().toJson(map))
                .converter(new GenericConverter<BaseData>((new TypeToken<BaseData>() {
                }.getType())))
                .adapt(new ObservableBody<BaseData>());
    }

    /**
     * 添加设备
     *
     * @return
     */
    public Observable<BaseData> deleteArticle(String id) {
        Map<String, String> map = new HashMap<>();
        map.put("ids", id);
        return OkGo.<BaseData>post(URLs.DELETEARTICLE)
                .headers("Cookie", SPUtils.getInstance().getString(ConstantUtils.COOKIE))
                .upJson(new Gson().toJson(map))
                .converter(new GenericConverter<BaseData>((new TypeToken<BaseData>() {
                }.getType())))
                .adapt(new ObservableBody<BaseData>());
    }


}
