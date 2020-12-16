package com.ljkj.wellcover.utils;

import android.util.Log;

import com.ljkj.wellcover.BuildConfig;

/**
 * 文件名：Logger
 * 作者：Turbo
 * 时间： 12/15/20 5:30 PM
 * 蚁穴虽小，溃之千里。
 */
public class Logger {
    private static boolean isPlay = BuildConfig.DEBUG ? true : false;

    public static void e(String TAG, String message) {
        if (isPlay) {
            Log.e(TAG, message);
        }
    }
}
