package com.ljkj.wellcover.utils;

import android.widget.LinearLayout;

import com.ljkj.wellcover.view.LoadingLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

/**
 * 文件名：LoadUtil
 * 作者：Turbo
 * 时间： 2020-10-05 18:16
 * 蚁穴虽小，溃之千里。
 */
public class LoadUtil {

    public static void forbidLoadMore(List list, SmartRefreshLayout smartRefreshLayout, LoadingLayout loadingLayout) {
        try {
            if (list == null || list.size() == 0) {
                loadingLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                loadingLayout.showEmpty();
                smartRefreshLayout.setEnableLoadMore(false);//禁止加载
            } else {
                loadingLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                loadingLayout.showContent();
                smartRefreshLayout.setEnableLoadMore(false);//允许加载
            }
            smartRefreshLayout.finishRefresh();
        } catch (Exception e) {

        }
    }

    public static void closeRefreshOrLoadMore(boolean hasNext, boolean isRefresh, SmartRefreshLayout smartRefreshLayout, LoadingLayout loadingLayout) {
        if (hasNext) {
            loadingLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            loadingLayout.showContent();
            smartRefreshLayout.setEnableLoadMore(true);//允许加载
        } else {
            loadingLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            loadingLayout.showContent();
            smartRefreshLayout.setEnableLoadMore(false);//禁止加载
        }

        if (isRefresh) {
            smartRefreshLayout.finishRefresh();
        } else {
            smartRefreshLayout.finishLoadMore();
        }
    }

    public static void closeRefreshOrLoadMore(boolean hasNext, boolean isRefresh, SmartRefreshLayout smartRefreshLayout) {
        smartRefreshLayout.setEnableLoadMore(hasNext);//允许加载
        if (isRefresh) {
            smartRefreshLayout.finishRefresh();
        } else {
            smartRefreshLayout.finishLoadMore();
        }
    }
}
