package com.ljkj.wellcover.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.amap.api.fence.GeoFence;
import com.amap.api.fence.GeoFenceListener;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.ljkj.wellcover.R;
import com.ljkj.wellcover.utils.ImmersionBarUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 文件名：添加设备
 * 作者：Turbo
 * 时间： 2020-10-06 09:17
 * 蚁穴虽小，溃之千里。
 */
public class AddEquipmentActivity extends BaseActivity implements GeoFenceListener, AMap.OnMapClickListener, LocationSource, AMapLocationListener {

    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private LocationSource.OnLocationChangedListener mListener;
    // 中心点坐标
    private LatLng centerLatLng = null;
    // 中心点marker
    private Marker centerMarker;

    private BitmapDescriptor ICON_YELLOW = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
    private BitmapDescriptor ICON_RED = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
    private MarkerOptions markerOption = null;
    private List<Marker> markerList = new ArrayList<Marker>();


    @BindView(R.id.et_number)
    EditText etNumber;
    @BindView(R.id.et_longitude)
    EditText etLongitude;
    @BindView(R.id.et_latitude)
    EditText etLatitude;
    @BindView(R.id.mv_map)
    MapView mMapView;

    private AMap mAMap;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_addequipment;
    }

    @Override
    protected void initViews() {
        super.initViews();
        ImmersionBarUtils.initColorBaseBar(AddEquipmentActivity.this, R.color._09B1FF);
        mMapView.onCreate(getSavedInstanceState());
        markerOption = new MarkerOptions().draggable(true);
        if (mAMap == null) {
            mAMap = mMapView.getMap();
            mAMap.getUiSettings().setRotateGesturesEnabled(false);
            mAMap.moveCamera(CameraUpdateFactory.zoomBy(6));
            setUpMap();
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
        deactivate();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mMapView) {
            mMapView.onDestroy();
        }
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
    }

    List<GeoFence> fenceList = new ArrayList<GeoFence>();

    @Override
    public void onGeoFenceCreateFinished(final List<GeoFence> geoFenceList,
                                         int errorCode, String customId) {
        Message msg = Message.obtain();
        if (errorCode == GeoFence.ADDGEOFENCE_SUCCESS) {
            fenceList = geoFenceList;
            msg.obj = customId;
            msg.what = 0;
        } else {
            msg.arg1 = errorCode;
            msg.what = 1;
        }

    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": "
                        + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    private void addCenterMarker(LatLng latlng) {
        if (null == centerMarker) {
            centerMarker = mAMap.addMarker(markerOption);
        }
        centerMarker.setPosition(latlng);
        markerList.add(centerMarker);
    }


    /**
     * 激活定位
     */
    @Override
    public void activate(LocationSource.OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            // 设置定位监听
            mlocationClient.setLocationListener(this);
            // 设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            // 只是为了获取当前位置，所以设置为单次定位
            mLocationOption.setOnceLocation(true);
            // 设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            mlocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }


    @OnClick({R.id.ivBack, R.id.tv_complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.tv_complete:
                break;
        }
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        mAMap.setOnMapClickListener(this);
        mAMap.setLocationSource(this);// 设置定位监听
        mAMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        // 自定义系统定位蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(
                BitmapDescriptorFactory.fromResource(R.mipmap.car));
        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));
        // 自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(0);
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));
        // 将自定义的 myLocationStyle 对象添加到地图上
        mAMap.setMyLocationStyle(myLocationStyle);
        mAMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        mAMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        markerOption.icon(ICON_YELLOW);
        centerLatLng = latLng;
        addCenterMarker(centerLatLng);

        String latitude = centerLatLng.latitude + "";
        String longitude = centerLatLng.longitude + "";
        etLongitude.setText(longitude);
        etLatitude.setText(latitude);
    }


}