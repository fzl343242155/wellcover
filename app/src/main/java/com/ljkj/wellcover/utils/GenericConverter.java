package com.ljkj.wellcover.utils;

import com.google.gson.Gson;
import com.ljkj.wellcover.R;
import com.ljkj.wellcover.WellCoverApplication;
import com.lzy.okgo.convert.Converter;

import java.lang.reflect.Type;

import okhttp3.Response;

/**
 * 文件名：GenericConverter
 * 作者：Turbo
 * 时间： 2020-07-30 15:37
 * 蚁穴虽小，溃之千里。
 */
public class GenericConverter<T> implements Converter<T> {

    private String TAG = this.getClass().getSimpleName();
    private Class<T> tClass;

    private Type type;

    public GenericConverter(Class<T> clazz) {
        tClass = clazz;
    }

    public GenericConverter(Type type) {
        this.type = type;
    }

    @Override
    public T convertResponse(Response response) throws Throwable {
        if (!response.isSuccessful()) {
            if (response.code() >= 400 && response.code() < 500) {
                throw new RuntimeException(WellCoverApplication.getInstance().getResources().getString(R.string.Interface_not_found));
            }
            if (response.code() >= 500) {
                throw new RuntimeException(WellCoverApplication.getInstance().getResources().getString(R.string.server_not_responding));
            }
            return null;
        }
        String json = response.body().string();
        if (tClass != null) {
            return new Gson().fromJson(json, tClass);
        }
        if (type != null) {
            return new Gson().fromJson(json, type);
        }
        throw new IllegalAccessException(WellCoverApplication.getInstance().getResources().getString(R.string.please_input_typeinfo));
    }
}
