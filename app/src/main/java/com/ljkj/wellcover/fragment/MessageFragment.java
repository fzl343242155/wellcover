package com.ljkj.wellcover.fragment;


import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ljkj.wellcover.R;
import com.ljkj.wellcover.activity.ActionActivity;
import com.ljkj.wellcover.adapter.BaseRecyclerAdapter;
import com.ljkj.wellcover.adapter.HomeAdapter;
import com.ljkj.wellcover.adapter.MessageAdapter;
import com.ljkj.wellcover.view.LoadingLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 文件名：HomeFragment
 * 作者：Turbo
 * 时间： 2020-10-05 16:56
 * 蚁穴虽小，溃之千里。
 */
public class MessageFragment extends BaseFragment implements LoadingLayout.RetryListener{

    @BindView(R.id.rv_base)
    RecyclerView recycleLayout;
    @BindView(R.id.loadlayout)
    LoadingLayout loadingLayout;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private int page = 1;
    private MessageAdapter mMessageAdapter;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_message;
    }

    @Override
    protected void initViews() {
        super.initViews();
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

        mMessageAdapter = new MessageAdapter(mContext);
        recycleLayout.setAdapter(mMessageAdapter);

        loadData();

        mMessageAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object data) {
                startActivity(new Intent(mContext, ActionActivity.class));
            }
        });
    }

    private void loadData() {
        loadingLayout.showContent();

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i + "");
        }
        mMessageAdapter.addDatas(list);

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
