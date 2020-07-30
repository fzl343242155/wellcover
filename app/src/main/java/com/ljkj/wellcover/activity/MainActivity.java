package com.ljkj.wellcover.activity;

import com.amap.api.maps.MapView;
import com.ljkj.wellcover.R;
import com.ljkj.wellcover.bean.BaseData;
import com.ljkj.wellcover.utils.HttpServer;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity {

    @BindView(R.id.map)
    MapView map;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        super.initViews();
        showLoading();
        map.onCreate(getSavedInstanceState());

        HttpServer.$().onLogin("", "")
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showLoading();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseData<String>>() {
                    @Override
                    public void call(BaseData<String> stringBaseData) {
                        dismissLoading();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        dismissLoading();
                    }
                });
    }
}
