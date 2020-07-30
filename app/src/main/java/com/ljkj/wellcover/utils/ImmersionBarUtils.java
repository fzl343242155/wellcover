package com.ljkj.wellcover.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.WindowManager;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.gyf.immersionbar.ImmersionBar;
import com.ljkj.wellcover.R;

/**
 * 文件名：ImmersionBarUtils
 * 作者：Turbo
 * 时间： 2020-07-30 15:37
 * 蚁穴虽小，溃之千里。
 */
public class ImmersionBarUtils {

    public static void initwhitetransbar(Activity activity) {
        ImmersionBar.with(activity)
                .keyboardEnable(true)
                .fullScreen(false)
                .autoDarkModeEnable(true)
                .statusBarColor(R.color.white)
                .fitsSystemWindows(false)
                .autoDarkModeEnable(true)
                .navigationBarColor(R.color.white)
                .init();
    }

    public static void initwhitetDialogransbar(DialogFragment dialogFragment) {
        ImmersionBar.with(dialogFragment)
                .keyboardEnable(true)
                .fullScreen(false)
                .statusBarColor(R.color.white)
                .keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
                .fitsSystemWindows(true)
                .autoDarkModeEnable(true)
                .navigationBarColor(R.color.white)
                .init();
    }

    public static void initTranslBar(Activity activity) {
        ImmersionBar.with(activity)
                .keyboardEnable(true)
                .fullScreen(false)
                .transparentStatusBar()
                .fitsSystemWindows(false)
                .autoDarkModeEnable(true)
                .navigationBarColor(R.color.white)
                .init();
    }

    public static void initTranslBar(Fragment fragment) {
        ImmersionBar.with(fragment)
                .keyboardEnable(true)
                .fullScreen(false)
                .transparentStatusBar()
                .fitsSystemWindows(false)
                .autoDarkModeEnable(true)
                .navigationBarColor(R.color.white)
                .init();
    }

    public static void initBaseBar(Activity activity) {
        ImmersionBar.with(activity)
                .keyboardEnable(true)
                .fullScreen(false)
                .statusBarColor(R.color.white)
                .keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
                .fitsSystemWindows(true)
                .autoDarkModeEnable(true)
                .navigationBarColor(R.color.white)
                .init();
    }

    public static void initColorBaseBar(Activity activity, int color) {
        ImmersionBar.with(activity)
                .keyboardEnable(true)
                .fullScreen(false)
                .statusBarColor(color)
                .keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
                .fitsSystemWindows(true)
                .autoDarkModeEnable(true)
                .navigationBarColor(R.color.white)
                .init();
    }

    public static void initColorBar(Activity activity) {
        ImmersionBar.with(activity)
                .keyboardEnable(true)
                .fullScreen(false)
                .transparentStatusBar()
                .keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
                .fitsSystemWindows(false)
                .autoDarkModeEnable(true)
                .navigationBarColor(R.color.white)
                .init();
    }

    public static void initbar(Activity activity) {
        ImmersionBar.with(activity)
                .keyboardEnable(true)
                .fullScreen(false)
                .transparentStatusBar()
                .autoDarkModeEnable(true)
                .navigationBarColor(R.color.transparent)
                .fitsSystemWindows(false)
                .init();
    }

    public static void initColorBar(Activity activity, int color) {
        ImmersionBar.with(activity)
                .keyboardEnable(true)
                .fullScreen(false)
                .statusBarColor(color)
                .keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
                .fitsSystemWindows(true)
                .autoDarkModeEnable(true)
                .navigationBarColor(R.color.white)
                .init();
    }

    public static void initbar(Fragment fragment, int statusbarcolor) {
        ImmersionBar.with(fragment)
                .keyboardEnable(true)
                .fullScreen(false)
                .statusBarColor(statusbarcolor)
                .keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
                .fitsSystemWindows(true)
                .autoDarkModeEnable(true)
                .navigationBarColor(R.color.white)
                .init();
    }

    public static void reset(Fragment fragment) {
        ImmersionBar.with(fragment).reset();
    }

    public static void reset(Activity activity) {
        ImmersionBar.with(activity).reset();
    }

    /**
     * 获取状态栏高度
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }
}
