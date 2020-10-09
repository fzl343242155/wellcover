package com.ljkj.wellcover.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ljkj.wellcover.R;
import com.ljkj.wellcover.adapter.EquipmentAdapter;
import com.ljkj.wellcover.bean.BaseData;
import com.ljkj.wellcover.bean.EquipmentBean;
import com.ljkj.wellcover.bean.EventCenter;
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
 * 文件名：设备管理
 * 作者：Turbo
 * 时间： 2020-10-06 09:12
 * 蚁穴虽小，溃之千里。
 */
public class EquipmentActivity extends BaseActivity implements LoadingLayout.RetryListener {

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
    private List<EquipmentBean.ListBean> mList = new ArrayList<>();

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

    @OnClick({R.id.ivBack, R.id.tv_add,R.id.ll_select, R.id.rl_delect})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.tv_add:
                startActivity(new Intent(mContext, AddEquipmentActivity.class));
                break;
            case R.id.ll_select:
                break;
            case R.id.rl_delect:
                break;
        }
    }

    @Override
    protected void initViews() {
        super.initViews();
        ImmersionBarUtils.initColorBaseBar(EquipmentActivity.this, R.color._09B1FF);
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

        mEquipmentAdapter = new EquipmentAdapter(mContext);
        recycleLayout.setAdapter(mEquipmentAdapter);

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
                                mEquipmentAdapter.addDatas(mList);
                                boolean hasNext = false;
                                if (response.getInfo().getTotalCount() > (response.getInfo().getTotalPage() * 10)) {
                                    hasNext = true;
                                }
                                LoadUtil.closeRefreshOrLoadMore(hasNext, isRefresh, refreshLayout, loadingLayout);
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

//        loadingLayout.showContent();
//
//        List<String> list = new ArrayList<>();
//        for (int i = 0; i < 20; i++) {
//            list.add(i + "");
//        }
//        mEquipmentAdapter.addDatas(list);

        //TODO 请求成功调用下边这些
//        boolean isRefresh;
//        if (response != null) {
//            if ("0".equals(response.getCode())) {
//                if (page == 1) {
//                    mList.clear();
//                    mList = response.getData().getPostsList();
//                    if (mList.size() == 0) {
//                        refreshLayout.finishRefresh();
//                        loadingLayout.showEmpty();
//                        LoadUtil.forbidLoadMore(mList, srl, loadingLayout);
//                        return;
//                    }
//                    isRefresh = true;
//                } else {
//                    isRefresh = false;
//                    mList.addAll(response.getData().getPostsList());
//                }
//                topicRvAdapter.addDatas(mList);
//                LoadUtil.closeRefreshOrLoadMore(response.getData().isHasNext(), isRefresh, refreshLayout, loadingLayout);
//            } else {
//                loadingLayout.showEmpty();
//            }
//        }
    }

    @Override
    public void onErrorclick() {
        page = 1;
        loadData();
    }

}
