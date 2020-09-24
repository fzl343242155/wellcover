package com.ljkj.wellcover.activity;

import android.location.Location;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.ljkj.wellcover.R;
import com.ljkj.wellcover.bean.BaseData;
import com.ljkj.wellcover.utils.HttpServer;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements AMap.OnMyLocationChangeListener {

    @BindView(R.id.map)
    MapView map;

    AMap amap;
    MyLocationStyle myLocationStyle;
    Marker[] marker = new Marker[10];
    int totalMarker;
    int WRITE_COARSE_LOCATION_REQUEST_CODE;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        super.initViews();
        map.onCreate(getSavedInstanceState());

        amap = map.getMap();
        myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);
        amap.setMyLocationStyle(myLocationStyle);
        amap.getUiSettings().setMyLocationButtonEnabled(true);
        amap.setMyLocationEnabled(true);
        amap.setOnMyLocationChangeListener(this);



        MarkerOptions markerOption = new MarkerOptions();


        markerOption.position(new LatLng(39.909843, 116.434739));//经纬度（左边纬度，右边经度）
        markerOption.icon(BitmapDescriptorFactory.fromResource(R.mipmap.car));//自定义标点的图片
        markerOption.title("北京站").snippet("39.909843, 116.434739");
        markerOption.draggable(true);//是否平铺，这里设置为平铺
        Marker marker = amap.addMarker(markerOption);
        marker.setObject("11");



//        HttpServer.$().onLogin("", "")
//                .subscribeOn(Schedulers.io())
//                .doOnSubscribe(new Action0() {
//                    @Override
//                    public void call() {
//                        showLoading();
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<BaseData<String>>() {
//                    @Override
//                    public void call(BaseData<String> stringBaseData) {
//                        dismissLoading();
//                    }
//                }, new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//                        dismissLoading();
//                    }
//                });
    }

    @Override
    public void onMyLocationChange(Location location) {

    }
}
