package com.ljkj.wellcover.utils.update;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.ljkj.wellcover.BuildConfig;
import com.ljkj.wellcover.bean.UpdateBean;
import com.ljkj.wellcover.utils.AppUtils;
import com.ljkj.wellcover.utils.SpUtils;
import com.ljkj.wellcover.utils.download.DownloadHelper;
import com.ljkj.wellcover.utils.download.DownloadManager;
import com.ljkj.wellcover.utils.download.OnDownloadListener;


import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.ljkj.wellcover.utils.ConstantUtils.SP_KEY_CACHE_APK_VERSION_CODE;
import static com.ljkj.wellcover.utils.ConstantUtils.SP_KEY_CACHE_VALID_TIME;

public class UpdateManager {
    private static volatile UpdateManager manager = null;

    private static final int MSG_ON_START = 1;
    private static final int MSG_ON_PROGRESS = 2;
    private static final int MSG_ON_DOWNLOAD_FINISH = 3;
    private static final int MSG_ON_FAILED = 4;
    private static final int MSG_ON_CANCLE = 5;
    private static final int MSG_ON_FIND_NEW_VERSION = 6;
    private static final int MSG_ON_NEWEST = 7;
    private static final int MSG_ON_UPDATE_EXCEPTION = 8;

    private DownloadManager mDownloadManager;

    private OnUpdateListener mOnUpdateListener;
    private OnCheckUpdateListener mOnCheckUpdateListener;

    private int mNewestVersionCode;
    private String mNewestVersionName;
    private String mNewVersionContent;

    /**
     * 最后一次保存cache的时间
     */
    private long mLastCacheSaveTime = 0;

    private UpdateManager() {
        mDownloadManager = DownloadManager.getInstance();
    }

    /**
     * 获取updateManager实例
     *
     * @return updateManager实例
     */
    public static UpdateManager getInstance() {
        if (manager == null) {
            synchronized (UpdateManager.class) {
                if (manager == null) {
                    manager = new UpdateManager();
                }
            }
        }
        return manager;
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_ON_START:
                    if (mOnUpdateListener != null)
                        mOnUpdateListener.onStartUpdate();
                    break;

                case MSG_ON_PROGRESS:
                    if (mOnUpdateListener != null)
                        mOnUpdateListener.onProgress((Integer) msg.obj);
                    break;

                case MSG_ON_DOWNLOAD_FINISH:
                    if (mOnUpdateListener != null)
                        mOnUpdateListener.onApkDownloadFinish((String) msg.obj);
                    installApk((String) msg.obj);
                    break;

                case MSG_ON_FAILED:
                    if (mOnUpdateListener != null)
                        mOnUpdateListener.onUpdateFailed();
                    break;

                case MSG_ON_CANCLE:
                    if (mOnUpdateListener != null)
                        mOnUpdateListener.onUpdateCanceled();
                    break;

                case MSG_ON_UPDATE_EXCEPTION:
                    if (mOnUpdateListener != null)
                        mOnUpdateListener.onUpdateException();
                    break;

                case MSG_ON_FIND_NEW_VERSION:
                    UpdateBean.DataBean dataBean = (UpdateBean.DataBean) msg.obj;
//
                    mNewestVersionCode = dataBean.getVersion_code();
                    mNewestVersionName = dataBean.getVersion_name();
                    mNewVersionContent = dataBean.getContent();

                    if (mOnCheckUpdateListener != null)
                        mOnCheckUpdateListener.onFindNewVersion(mNewestVersionName,
                                mNewVersionContent);
                    break;

                case MSG_ON_NEWEST:
                    if (mOnCheckUpdateListener != null)
                        mOnCheckUpdateListener.onNewest();
                    break;
            }
        }
    };

    /**
     * 检查更新
     *
     * @param apkInfoUrl            服务器端保存新版apk相关信息json的url
     * @param onCheckUpdateListener onCheckUpdateListener
     */
    public void checkUpdate(String apkInfoUrl, OnCheckUpdateListener onCheckUpdateListener) {
        mOnCheckUpdateListener = onCheckUpdateListener;
        sendOkHttpRequest(apkInfoUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showShort("check update failed.");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String strJson = response.body().string();
                Log.e("onResponse", "response.body().string() = " + strJson);
                if (parseJson(strJson).getVersion_code() > AppUtils.getAppVersionCode()) {
                    //最后一次缓存的时间超过缓存文件有效期，或者最后一次缓存的apk不是最新版本的apk，删除缓存apk
                    if ((System.currentTimeMillis() - mLastCacheSaveTime > getCacheSaveValidTime())
                            || (getCacheApkVersionCode() != parseJson(strJson).getVersion_code())) {
                        clearCacheApkFile();
                        setCacheApkVersionCode(parseJson(strJson).getVersion_code());
                    }
                    sendMessage(MSG_ON_FIND_NEW_VERSION, parseJson(strJson));
                } else {
                    sendMessage(MSG_ON_NEWEST, null);
                    //当前已经是最新版本APK，清除本地已经缓存的apk安装包
                    clearCacheApkFile();
                }
            }
        });
    }

    /**
     * 开始更新App
     * <p>
     * 此时开始正式下载更新Apk
     *
     * @param apkUrl           服务端最新apk文件url
     * @param onUpdateListener onUpdateListener
     */
    public void startToUpdate(String apkUrl, OnUpdateListener onUpdateListener) {
        mOnUpdateListener = onUpdateListener;

        if (isEmpty(mNewestVersionName) || mNewestVersionCode == 0)
            return;

        downloadNewestApkFile(apkUrl, mNewestVersionCode, mNewestVersionName);
    }

    /**
     * 设置缓存文件有效时间，单位：秒
     * <p>
     * 默认缓存有效期为7天
     *
     * @param cacheValidTime 缓存文件有效时间
     */
    public void setCacheSaveValidTime(long cacheValidTime) {
        SpUtils.putLong(SP_KEY_CACHE_VALID_TIME, cacheValidTime);
    }

    /**
     * 获取缓存文件有效时间，单位：秒
     * <p>
     * 默认缓存有效期为7天
     *
     * @return 缓存文件有效时间
     */
    public long getCacheSaveValidTime() {
        return SpUtils.getLong(SP_KEY_CACHE_VALID_TIME, 60 * 60 * 24 * 7);
    }

    /**
     * 设置缓存文件版本号
     *
     * @param versionCode cacheApk版本号
     */
    public void setCacheApkVersionCode(int versionCode) {
        SpUtils.putInt(SP_KEY_CACHE_APK_VERSION_CODE, versionCode);
    }

    /**
     * 获取缓存文件版本号
     *
     * @return 缓存文件版本号
     */
    public int getCacheApkVersionCode() {
        return SpUtils.getInt(SP_KEY_CACHE_APK_VERSION_CODE, 0);
    }

    /**
     * 取消更新
     */
    public void cancleUpdate() {
        //保留下载已完成的部分apk cache文件，cache文件最多保留7天
        mDownloadManager.pauseDownload();
    }

    /**
     * 清除已下载的APK缓存
     */
    public void clearCacheApkFile() {
        Log.e("tag", "清除所有的apk文件");
        mDownloadManager.clearAllCacheFile();
    }

    /**
     * 下载最新版本的APK文件
     *
     * @param url               服务端最新apk文件url
     * @param newestVersionCode 最新版本APK版本号
     * @param newestVersionName 最新版本APK版本名称
     */
    private void downloadNewestApkFile(String url, int newestVersionCode, String
            newestVersionName) {
        String apkFileName = getApkNameWithVersionName(DownloadHelper.getUrlFileName(url),
                newestVersionName);

        sendMessage(MSG_ON_START, null);

        mDownloadManager.startDownload(url, apkFileName, new
                OnDownloadListener() {
                    @Override
                    public void onException() {
                        sendMessage(MSG_ON_UPDATE_EXCEPTION, null);
                    }

                    @Override
                    public void onProgress(int progress) {
                        sendMessage(MSG_ON_PROGRESS, progress);
                    }

                    @Override
                    public void onSuccess() {
                        mLastCacheSaveTime = System.currentTimeMillis();
                        sendMessage(MSG_ON_DOWNLOAD_FINISH, mDownloadManager.getDownloadFilePath());
                    }

                    @Override
                    public void onFailed() {
                        mLastCacheSaveTime = System.currentTimeMillis();
                        sendMessage(MSG_ON_FAILED, null);
                    }

                    @Override
                    public void onPaused() {
                        mLastCacheSaveTime = System.currentTimeMillis();
                        //取消升级时，调用download pause，保留已下载的部分apk文件
                        sendMessage(MSG_ON_CANCLE, null);
                    }

                    @Override
                    public void onCanceled() {
                        //为了保证断点续传，升级时，调用download pause，不使用cancle，onCancle不会被调用
                        mLastCacheSaveTime = System.currentTimeMillis();
                        sendMessage(MSG_ON_CANCLE, null);
                    }
                });
    }

    private void sendMessage(int msgWhat, Object o) {
        Message msg = Message.obtain();
        msg.what = msgWhat;
        msg.obj = o;
        mHandler.sendMessage(msg);
    }

    /**
     * 解析json数据
     *
     * @param jsonData json数据
     * @return dataBean
     */
    private UpdateBean.DataBean parseJson(String jsonData) {
        UpdateBean updateBean = new Gson().fromJson(jsonData, UpdateBean.class);
        return updateBean.getData();
    }

    /**
     * 获取带版本名称的apk文件名
     *
     * @param apkName apk原名
     * @return 带版本名称的apk文件名
     */
    private String getApkNameWithVersionName(String apkName, String versionName) {
        if (isEmpty(apkName))
            return apkName;

        apkName = apkName.substring(apkName.lastIndexOf("/") + 1, apkName.indexOf("" +
                ".apk"));
        Log.e("tag", "newApkName = " + apkName + "_v" + versionName + ".apk");
        return apkName + "_v" + versionName + ".apk";
    }

    /**
     * 安装 apk
     *
     * @param apkPath apk全路径
     */
    public void installApk(String apkPath) {
        if (isEmpty(apkPath)) {
            Log.e("tag", "apkPath is null.");
            return;
        }

        File file = new File(apkPath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // 由于没有在Activity环境下启动Activity,设置下面的标签
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 24) { //判断版本是否在7.0以上
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致  参数3 共享的文件
            Uri apkUri = FileProvider.getUriForFile(AppUtils.getContext(), BuildConfig
                    .APPLICATION_ID +
                    ".fileProvider", file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        AppUtils.getContext().startActivity(intent);
    }

    private void sendOkHttpRequest(final String address, final okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * 判断字符串是否有值，如果为null或者是空字符串或者只有空格或者为"null"字符串，则返回true，否则则返回false
     */
    private boolean isEmpty(String value) {
        if (value != null && !"".equalsIgnoreCase(value.trim())
                && !"null".equalsIgnoreCase(value.trim())) {
            return false;
        } else {
            return true;
        }
    }
}
