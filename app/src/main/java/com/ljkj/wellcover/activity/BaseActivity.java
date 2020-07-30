package com.ljkj.wellcover.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ljkj.wellcover.R;
import com.ljkj.wellcover.utils.ImmersionBarUtils;
import com.ljkj.wellcover.view.LoadingDialog;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 文件名：BaseActivity
 * 作者：Turbo
 * 时间： 2020-07-29 17:28
 * 蚁穴虽小，溃之千里。
 */
public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder mUnBinder;
    protected Context mContext;
    private Bundle savedInstanceState;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        //设置透明状态栏
        ImmersionBarUtils.initBaseBar(this);
        // 竖屏锁定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (isBindEventBusHere()) {
            EventBus.getDefault().register(this);
        }
        if (getContentViewLayoutID() != 0) {
            setContentView(getContentViewLayoutID());
        } else {
            throw new IllegalArgumentException("You must return a right contentView layout resource Id");
        }
        mContext = this;
        mUnBinder = ButterKnife.bind(this);
        initViews();
        initData();
    }

    public Bundle getSavedInstanceState() {
        return savedInstanceState;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (isBindEventBusHere()) {
                EventBus.getDefault().unregister(this);
            }
            // 解除黄油刀
            if (mUnBinder != null && mUnBinder != Unbinder.EMPTY) {
                mUnBinder.unbind();
                mUnBinder = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否绑定EventBus
     *
     * @return
     */
    public boolean isBindEventBusHere() {
        return false;
    }

    /**
     * 设置布局ID
     *
     * @return
     */
    protected abstract int getContentViewLayoutID();

    /**
     * 初始化布局以及监听事件
     */
    protected void initViews() {
    }

    /**
     * 初始化业务操作
     */
    protected void initData() {
    }

    public RecyclerView.ItemDecoration getRecyclerViewDivider(@DrawableRes int drawableId) {
        DividerItemDecoration itemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(drawableId));
        return itemDecoration;
    }


    public LoadingDialog mDialog;


    public void showLoading() {
        if (mDialog == null || !mDialog.isShowing()) {
            mDialog = new LoadingDialog(this);
            mDialog.show();
        }
    }


    public void showLoading(String msg) {
        if (mDialog == null || !mDialog.isShowing()) {
            mDialog = new LoadingDialog(this);
            mDialog.show();
        }
    }

    public void dismissLoading() {
        if (mDialog != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mDialog.dismiss();
                }
            }, 200);

        }
    }

    public void displayImage(String url, ImageView imageView) {
        Glide.with(getApplicationContext())//
                .load(url)//
                .error(R.mipmap.ic_launcher)//
                .into(imageView);
    }
}
