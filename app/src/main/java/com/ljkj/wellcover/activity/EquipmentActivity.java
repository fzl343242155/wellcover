package com.ljkj.wellcover.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.ljkj.wellcover.R;
import com.ljkj.wellcover.adapter.EquipmentAdapter;
import com.ljkj.wellcover.bean.BaseData;
import com.ljkj.wellcover.bean.EquipmentBean;
import com.ljkj.wellcover.bean.EquipmentInfoBean;
import com.ljkj.wellcover.bean.EventCenter;
import com.ljkj.wellcover.utils.HttpServer;
import com.ljkj.wellcover.utils.ImmersionBarUtils;
import com.ljkj.wellcover.utils.LoadUtil;
import com.ljkj.wellcover.utils.Logger;
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
 * 文件名：设备管理
 * 作者：Turbo
 * 时间： 2020-10-06 09:12
 * 蚁穴虽小，溃之千里。
 */
public class EquipmentActivity extends BaseActivity implements LoadingLayout.RetryListener {

    private static final String TAG = "EquipmentActivity";

    @BindView(R.id.rv_base)
    RecyclerView recycleLayout;
    @BindView(R.id.loadlayout)
    LoadingLayout loadingLayout;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.iv_select)
    ImageView ivSelect;

    private int page = 1;
    private EquipmentAdapter mEquipmentAdapter;
    private List<EquipmentInfoBean> mList = new ArrayList<>();
    private boolean isAllSelect = true;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_equipment;
    }

    @Override
    public boolean isBindEventBusHere() {
        return true;
    }

    @Override
    public void onEventCallBack(EventCenter eventCenter) {
        super.onEventCallBack(eventCenter);
    }

    @Override
    protected void initViews() {
        super.initViews();
        ImmersionBarUtils.initColorBar(EquipmentActivity.this);
        loadingLayout.setRetryListener(this);
        loadingLayout.setEmptyText("暂无记录");
        ivSelect.setColorFilter(Color.WHITE);
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

        mEquipmentAdapter = new EquipmentAdapter(mContext, ivSelect);
        recycleLayout.setAdapter(mEquipmentAdapter);

        loadData();
    }

    @OnClick({R.id.ivBack, R.id.tv_add, R.id.ll_select, R.id.rl_delect})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.tv_add:
                startActivity(new Intent(mContext, AddEquipmentActivity.class));
                break;
            case R.id.ll_select:
                if (isAllSelect) {
                    for (int i = 0; i < mEquipmentAdapter.getDatas().size(); i++) {
                        mEquipmentAdapter.setItemChecked(i, isAllSelect);
                    }
                    ivSelect.setBackgroundResource(R.mipmap.selected);
                } else {
                    for (int i = 0; i < mEquipmentAdapter.getDatas().size(); i++) {
                        mEquipmentAdapter.setItemChecked(i, isAllSelect);
                    }
                    ivSelect.setBackgroundResource(R.mipmap.all_select);
                }
                isAllSelect = !isAllSelect;
                break;
            case R.id.rl_delect:
                List<EquipmentInfoBean> list = mEquipmentAdapter.getSelectedItem();
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < list.size(); i++) {
                    stringBuffer.append(list.get(i).getId() + ",");
                }
                String result = stringBuffer.toString().substring(0, stringBuffer.length() - 1);
                Logger.e(TAG, "onViewClicked: " + result);
                deleteData(result);
                break;
        }
    }

    private void deleteData(String id) {
//        HttpServer.$().deleteArticle(id)
//                .subscribeOn(Schedulers.io())
//                .doOnSubscribe(new Action0() {
//                    @Override
//                    public void call() {
//                        showLoading();
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<BaseData>() {
//                    @Override
//                    public void call(BaseData response) {
//                        dismissLoading();
//                        if (!TextUtils.isEmpty(response.getMsg())) {
//                            toast(response.getMsg());
//                            finish();
//                        }
//                    }
//                }, new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//                        dismissLoading();
//                    }
//                });
    }

    private void loadData() {
//        HttpServer.$().onEquipmentList(page)
//                .subscribeOn(Schedulers.io())
//                .doOnSubscribe(new Action0() {
//                    @Override
//                    public void call() {
//                        showLoading();
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<BaseData<EquipmentBean>>() {
//                    @Override
//                    public void call(BaseData<EquipmentBean> response) {
//                        dismissLoading();
//                        boolean isRefresh;
//                        if (response != null) {
//                            if (response.getSuccess()) {
//                                if (page == 1) {
//                                    mList.clear();
//                                    mList = response.getInfo().getList();
//                                    if (mList.size() == 0) {
//                                        refreshLayout.finishRefresh();
//                                        loadingLayout.showEmpty();
//                                        LoadUtil.forbidLoadMore(mList, refreshLayout, loadingLayout);
//                                        return;
//                                    }
//                                    isRefresh = true;
//                                } else {
//                                    isRefresh = false;
//                                    mList.addAll(response.getInfo().getList());
//                                }
//                                mEquipmentAdapter.addDatas(mList);
//                                boolean hasNext = false;
//                                if (response.getInfo().getTotalCount() > (response.getInfo().getTotalPage() * 10)) {
//                                    hasNext = true;
//                                }
//                                LoadUtil.closeRefreshOrLoadMore(hasNext, isRefresh, refreshLayout, loadingLayout);
//                            } else {
//                                loadingLayout.showEmpty();
//                            }
//                        }
//                    }
//                }, new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//                        dismissLoading();
//                    }
//                });

        List<EquipmentInfoBean> mList = new ArrayList<>();

        EquipmentInfoBean bean1 = new EquipmentInfoBean();
        bean1.setId("LJKJ20100819123");
        bean1.setLockStatus("1");
        bean1.setBluetoothMac("20:20:09:11:18:5E");
        bean1.setCompany("苏州维易斯特智能科技有限公司");
        bean1.setLatitude("116.396633");
        bean1.setLongitude("40.025402");
        bean1.setStreetName("奥林匹克森林公园");
        mList.add(bean1);

        EquipmentInfoBean bean2 = new EquipmentInfoBean();
        bean2.setId("WYST20100819123");
        bean2.setLockStatus("2");
        bean2.setBluetoothMac("20:20:09:11:18:5E");
        bean2.setCompany("苏州维易斯特智能科技有限公司");
        bean2.setLatitude("116.418336");
        bean2.setLongitude("40.02485");
        bean2.setStreetName("仰山公园");
        mList.add(bean2);

        EquipmentInfoBean bean3 = new EquipmentInfoBean();
        bean3.setId("WYST20100819122");
        bean3.setLockStatus("3");
        bean3.setBluetoothMac("20:20:09:11:18:5E");
        bean3.setCompany("苏州维易斯特智能科技有限公司");
        bean3.setLatitude("116.398933");
        bean3.setLongitude("39.999208");
        bean3.setStreetName("北京奥林匹克公园");
        mList.add(bean3);

        EquipmentInfoBean bean4 = new EquipmentInfoBean();
        bean4.setId("WYST20100819121");
        bean4.setLockStatus("1");
        bean4.setBluetoothMac("20:20:09:11:18:5E");
        bean4.setCompany("苏州维易斯特智能科技有限公司");
        bean4.setLatitude("116.435296");
        bean4.setLongitude("40.003298");
        bean4.setStreetName("华汇紫薇公园");
        mList.add(bean4);

        mEquipmentAdapter.addDatas(mList);

        LoadUtil.closeRefreshOrLoadMore(false, true, refreshLayout, loadingLayout);

    }

    @Override
    public void onErrorclick() {
        page = 1;
        loadData();
    }

}
