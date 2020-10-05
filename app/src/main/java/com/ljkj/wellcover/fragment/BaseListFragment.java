package com.ljkj.wellcover.fragment;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.ljkj.wellcover.R;
import com.ljkj.wellcover.adapter.SingleItemAdapter;
import com.ljkj.wellcover.view.LoadingLayout;
import com.ljkj.wellcover.view.MyDrawerLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 文件名：BaseListFragment
 * 作者：Turbo
 * 时间： 2020-10-05 16:56
 * 蚁穴虽小，溃之千里。
 */
public abstract class BaseListFragment<T> extends BaseFragment {

    public ArrayList<T> mData = new ArrayList<>();
    public int page = 1;

    public SingleItemAdapter<T> adapter;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.iv_rightSmall)
    ImageView ivRightSmall;
    @BindView(R.id.titleLayout)
    RelativeLayout titleLayout;
    @BindView(R.id.header)
    RelativeLayout header;
    @BindView(R.id.header_act)
    RelativeLayout headerAct;
    @BindView(R.id.rv_base)
    RecyclerView rvBase;
    @BindView(R.id.loadlayout)
    LoadingLayout loadlayout;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.bottom)
    RelativeLayout bottom;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.drawerContent)
    FrameLayout drawerContent;
    @BindView(R.id.drawer)
    MyDrawerLayout drawer;
    @BindView(R.id.ivBack)
    ImageView ivBack;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.base_list;
    }

    @Override
    protected void initViews() {
        super.initViews();
        titleLayout.setVisibility(View.GONE);
        ivBack.setVisibility(View.GONE);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        configBottomMenu(bottom);
        configHeader(header);
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadData(++page);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                loadData(page);
            }
        });
        loadlayout.setRetryListener(() -> loadData(page));
        adapter = new SingleItemAdapter<T>() {
            @Override
            public void onBind(BaseViewHolder holder, int position, T data) {
                onBindItem(holder, position, data);
            }

            @Override
            public int getItemViewType(int position) {
                return BaseListFragment.this.getItemViewType(position);
            }

            @Override
            public int getLayoutId(int viewType) {
                return getItemLayoutId(viewType);
            }
        };
        adapter.addDatas(mData);
        rvBase.setAdapter(adapter);
        if (!getLoadMoreEnabled()) refreshLayout.setEnableLoadMore(false);
        if (!getRefreshEnabled()) refreshLayout.setEnableRefresh(false);
        loadData(page);
    }

    public void loadFalied() {
        if (getActivity() == null || getActivity().isFinishing()) return;
        dismissLoading();
        refreshLayout.finishLoadMore();
        refreshLayout.finishRefresh();
        if (mData.size() == 0) {
            if (adapter.getHeaderView() == null)
                loadlayout.showError();
            refreshLayout.setEnableLoadMore(false);
            refreshLayout.setEnableRefresh(false);
        } else {
            if (getLoadMoreEnabled()) refreshLayout.setEnableLoadMore(true);
            if (getRefreshEnabled()) refreshLayout.setEnableRefresh(true);
        }
    }

    public void loadSuccess(boolean hasNext, List<T> datas) {
        if (getActivity() == null || getActivity().isFinishing()) return;
        dismissLoading();
        refreshLayout.finishLoadMore();
        refreshLayout.finishRefresh();
        if (page == 1) mData.clear();
        if (datas != null) mData.addAll(datas);
        adapter.notifyDataSetChanged();
        if (hasNext) {
            if (getLoadMoreEnabled()) refreshLayout.setEnableLoadMore(true);
        } else {
            if (getLoadMoreEnabled()) refreshLayout.setEnableLoadMore(false);
        }
        if (mData.size() == 0) {
            if (adapter.getHeaderView() == null)
                loadlayout.showEmpty();
            if (getRefreshEnabled()) refreshLayout.setEnableRefresh(true);
        } else {
            loadlayout.showContent();
            if (getRefreshEnabled()) refreshLayout.setEnableRefresh(true);
        }
    }

    public abstract void loadData(int page);

    /**
     * RecyclerView  适配器布局ID
     *
     * @return
     */
    public abstract int getItemLayoutId(int viewType);

    public abstract void onBindItem(BaseViewHolder holder, int position, T data);

    public int getItemViewType(int position) {
        return 0;
    }

    /**
     * 是否 允许加载更多
     *
     * @return
     */
    public boolean getLoadMoreEnabled() {
        return true;
    }

    /**
     * 是否 允许刷新
     *
     * @return
     */
    public boolean getRefreshEnabled() {
        return true;
    }

    public void configBottomMenu(RelativeLayout parent) {

    }

    public void configHeader(RelativeLayout parent) {

    }
}
