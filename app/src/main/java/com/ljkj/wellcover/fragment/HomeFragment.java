package com.ljkj.wellcover.fragment;


import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.ljkj.wellcover.R;
import com.ljkj.wellcover.activity.ActionActivity;
import com.ljkj.wellcover.adapter.BaseRecyclerAdapter;
import com.ljkj.wellcover.adapter.HomeAdapter;
import com.ljkj.wellcover.bean.BaseData;
import com.ljkj.wellcover.bean.EquipmentBean;
import com.ljkj.wellcover.bean.EquipmentInfoBean;
import com.ljkj.wellcover.utils.ConstantUtils;
import com.ljkj.wellcover.utils.HttpServer;
import com.ljkj.wellcover.utils.ImmersionBarUtils;
import com.ljkj.wellcover.utils.LoadUtil;
import com.ljkj.wellcover.view.LoadingLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 文件名：HomeFragment
 * 作者：Turbo
 * 时间： 2020-10-05 16:56
 * 蚁穴虽小，溃之千里。
 */
public class HomeFragment extends BaseFragment implements LoadingLayout.RetryListener {

    @BindView(R.id.rv_base)
    RecyclerView recycleLayout;
    @BindView(R.id.loadlayout)
    LoadingLayout loadingLayout;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.mv_map)
    MapView mvMap;
    @BindView(R.id.tv_map)
    TextView tvMap;

    private int page = 1;
    private HomeAdapter mHomeAdapter;
    private boolean isPlayListOrMap = false;
    private AMap amap;
    private MyLocationStyle myLocationStyle;
    private List<EquipmentInfoBean> mList = new ArrayList<>();

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_home;
    }

    @Override
    public void initImmersionBar() {
        ImmersionBarUtils.initColorBar(getActivity());
    }

    @Override
    protected void initViews() {
        super.initViews();

        mvMap.onCreate(getSavedInstanceState());
        amap = mvMap.getMap();
        myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);
        amap.setMyLocationStyle(myLocationStyle);
        amap.getUiSettings().setMyLocationButtonEnabled(true);
        amap.setMyLocationEnabled(true);

        loadingLayout.setRetryListener(this);
        loadingLayout.setEmptyText("暂无记录");
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                loadData();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                loadData();
            }
        });
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recycleLayout.setLayoutManager(mLayoutManager);
        recycleLayout.setItemAnimator(new DefaultItemAnimator());
        mHomeAdapter = new HomeAdapter(mContext);
        recycleLayout.setAdapter(mHomeAdapter);
        loadData();

        mHomeAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object data) {
                EquipmentInfoBean bean = (EquipmentInfoBean) data;
                Intent intent = new Intent(mContext, ActionActivity.class);
                intent.putExtra(ConstantUtils.DATA, bean);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public void onErrorclick() {
        page = 1;
        loadData();
    }

    private void loadData() {
        HttpServer.$().onEquipmentList(page)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showLoading();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseData<EquipmentBean>>() {
                    @Override
                    public void call(BaseData<EquipmentBean> response) {
                        dismissLoading();
                        boolean isRefresh;
                        if (response != null) {
                            if (response.getSuccess()) {
                                if (page == 1) {
                                    mList.clear();
                                    mList = response.getInfo().getList();
                                    if (mList.size() == 0) {
                                        refreshLayout.finishRefresh();
                                        loadingLayout.showEmpty();
                                        LoadUtil.forbidLoadMore(mList, refreshLayout, loadingLayout);
                                        return;
                                    }
                                    isRefresh = true;
                                } else {
                                    isRefresh = false;
                                    mList.addAll(response.getInfo().getList());
                                }
                                mHomeAdapter.addDatas(mList);
                                boolean hasNext = false;
                                if (response.getInfo().getTotalCount() > (response.getInfo().getTotalPage() * 10)) {
                                    hasNext = true;
                                }
                                LoadUtil.closeRefreshOrLoadMore(hasNext, isRefresh, refreshLayout, loadingLayout);
                                //地图添加marker
                                for (int i = 0; i < mList.size(); i++) {
                                    MarkerOptions markerOption = new MarkerOptions();
                                    markerOption.position(new LatLng(mList.get(i).getLongitude(), mList.get(i).getLatitude()));
                                    markerOption.icon(BitmapDescriptorFactory.fromResource(R.mipmap.location));//自定义标点的图片
                                    markerOption.title("编号：" + mList.get(i).getId()
                                            + "\n所属单位：" + mList.get(i).getCompany()
                                            + "\n所属街道：" + mList.get(i).getStreetName()
                                            + "\n状态：" + getLockStatus(mList.get(i).getLockStatus()));
                                    amap.addMarker(markerOption);
                                }
                            } else {
                                loadingLayout.showEmpty();
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        dismissLoading();
                    }
                });
    }

    private String getLockStatus(String lockStatus) {
        String result = "";
        //设备状态：1：开启  2 ： 关闭 3： 解除报警
        switch (lockStatus) {
            case "1":
                result = "开启";
                break;
            case "2":
                result = "关闭";
                break;
            case "3":
                result = "解除报警";
                break;
        }
        return result;
    }

    @OnClick(R.id.tv_map)
    public void onViewClicked() {
        if (isPlayListOrMap) {
            tvMap.setText("地图");
            refreshLayout.setVisibility(View.VISIBLE);
            mvMap.setVisibility(View.GONE);
        } else {
            tvMap.setText("列表");
            refreshLayout.setVisibility(View.GONE);
            mvMap.setVisibility(View.VISIBLE);
        }
        isPlayListOrMap = !isPlayListOrMap;
    }
}
