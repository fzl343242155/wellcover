package com.ljkj.wellcover.activity;

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
import com.ljkj.wellcover.utils.ImmersionBarUtils;
import com.ljkj.wellcover.view.LoadingLayout;
import com.ljkj.wellcover.view.MyDrawerLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 文件名：BaseListActivity
 * 作者：Turbo
 * 时间： 2020-10-05 17:11
 * 蚁穴虽小，溃之千里。
 */
public abstract class BaseListActivity<T> extends BaseActivity {

    public ArrayList<T> mData = new ArrayList<>();
    public int page = 1;
    public SingleItemAdapter<T> adapter;

    @BindView(R.id.ivBack)
    ImageView ivBack;
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


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.base_list;
    }

    @Override
    protected void initViews() {
        super.initViews();
        ImmersionBarUtils.initColorBaseBar(this, R.color._09B1FF);

        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        configDrawerContent(drawerContent);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickRight();
            }
        });
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickRight();
            }
        });
        tvRight.setText(getRightTitleText());
        ivRight.setImageResource(getRightTitleIconRes());
        tvTitle.setText(getTitleText());
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
        bottom = findViewById(R.id.bottom);
        adapter = new SingleItemAdapter<T>() {
            @Override
            public void onBind(BaseViewHolder holder, int position, T data) {
                onBindItem(holder, position, data);
            }

            @Override
            public int getItemViewType(int position) {
                return BaseListActivity.this.getItemViewType(position);
            }

            @Override
            public int getLayoutId(int viewType) {
                return getItemLayoutId(viewType);
            }
        };
        adapter.addDatas(mData);
        rvBase.setAdapter(adapter);
        if (getBackIconRes() != 0) ivBack.setImageResource(getBackIconRes());
        if (!getLoadMoreEnabled()) refreshLayout.setEnableLoadMore(false);
        if (!getRefreshEnabled()) refreshLayout.setEnableRefresh(false);
        loadData(page);
        configBottomMenu(bottom);
        configHeader(headerAct);
    }

    @OnClick(R.id.ivBack)
    public void onViewClicked() {
        finish();
    }

    protected void configDrawerContent(FrameLayout drawerContent) {
    }

    public void loadFalied() {
        if (isFinishing()) return;
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
        if (isFinishing()) return;
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
            if (getRefreshEnabled()) refreshLayout.setEnableRefresh(false);
        } else {
            loadlayout.showContent();
            if (getRefreshEnabled()) refreshLayout.setEnableRefresh(true);
        }
    }

    public abstract void loadData(int page);


    public abstract String getTitleText();

    /**
     * RecyclerView  适配器布局ID
     *
     * @return
     */
    public abstract int getItemLayoutId(int viewType);

    public abstract void onBindItem(BaseViewHolder holder, int position, T data);

    public int getRightTitleIconRes() {
        return 0;
    }

    public String getRightTitleText() {
        return "";
    }

    public int getBackIconRes() {
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

    /**
     * 右侧点击事件
     */
    public void clickRight() {
    }

    public void configBottomMenu(RelativeLayout parent) {

    }

    public void configHeader(RelativeLayout parent) {

    }

    public int getItemViewType(int position) {
        return 0;
    }
}
