package com.ljkj.wellcover.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ljkj.wellcover.R;
import com.ljkj.wellcover.activity.AddEquipmentActivity;
import com.ljkj.wellcover.bean.EventCenter;
import com.ljkj.wellcover.utils.ImmersionBarUtils;
import com.ljkj.wellcover.view.LoadingDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 文件名：BaseFragment
 * 作者：Turbo
 * 时间： 2020-10-05 16:52
 * 蚁穴虽小，溃之千里。
 */
public abstract class BaseFragment extends SimpleImmersionFragment {

    public View mRootView;
    protected Context mContext = null;
    private Unbinder mUnBinder;
    private Bundle savedInstanceState;
    public static LoadingDialog mDialog;

    /**
     * EventBus 回调方法
     *
     * @param eventCenter
     */
    public void onEventCallBack(EventCenter eventCenter) {

    }

    /**
     * 设置Fragment布局界面
     *
     * @return
     */
    protected abstract int getContentViewLayoutID();

    /**
     * 是否注册EventBus
     *
     * @return
     */
    public boolean isBindEventBusHere() {
        return false;
    }

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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 判断是否注册EventBus
        if (isBindEventBusHere()) {
            EventBus.getDefault().register(this);
        }

        ImmersionBarUtils.initTranslBars(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 根View 赋值
        mRootView = view;
        if (mRootView != null) {
            mUnBinder = ButterKnife.bind(this, mRootView);
        }
        this.savedInstanceState = savedInstanceState;

        initViews();
        initData();
    }

    public Bundle getSavedInstanceState() {
        return savedInstanceState;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // 设置Fragment布局
        if (getContentViewLayoutID() != 0) {
            return inflater.inflate(getContentViewLayoutID(), null);
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 如果注册 则反注册
        try {
            if (isBindEventBusHere()) {
                EventBus.getDefault().unregister(this);
            }
            if (mUnBinder != null&& mUnBinder != Unbinder.EMPTY) {
                mUnBinder.unbind();
                mUnBinder=null;
            }
        } catch (Exception e) {
        }
    }

    /**
     * EventBus回调
     *
     * @param eventCenter
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventCenter eventCenter) {
        if (null != eventCenter) {
            onEventCallBack(eventCenter);
        }
    }

    public void showLoading() {
        if(mDialog==null||!mDialog.isShowing()) {
            mDialog = new LoadingDialog(getActivity());
            mDialog.show();
        }
    }


    public void showLoading(String msg) {
        if(mDialog==null||!mDialog.isShowing()) {
            mDialog = new LoadingDialog(getActivity());
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
            },200);
        }
    }

    public void toast(String message){
        Toast.makeText(mContext,message,Toast.LENGTH_SHORT).show();
    }

}
