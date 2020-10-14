package com.ljkj.wellcover.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.blankj.utilcode.util.ToastUtils;
import com.ljkj.wellcover.R;
import com.ljkj.wellcover.utils.update.OnCheckUpdateListener;
import com.ljkj.wellcover.utils.update.OnUpdateListener;
import com.ljkj.wellcover.utils.update.UpdateManager;

/**
 * 文件名：AppUpdateUtils
 * 作者：Turbo
 * 时间： 2020-10-14 15:13
 * 蚁穴虽小，溃之千里。
 */
public class AppUpdateUtils {

    private UpdateManager mUpdateManager;
    private ProgressDialog mProgressDialog;
    private Context mContext;
    private boolean mIsNewAPPVersion;

    public void update(Context context, boolean isNewAPPVersion) {
        mContext = context;
        mIsNewAPPVersion = isNewAPPVersion;
        initProgressDialog();
        mUpdateManager = UpdateManager.getInstance();

        mUpdateManager.checkUpdate(ConstantUtils.VERSION_INFO_URL, new OnCheckUpdateListener() {
            @Override
            public void onFindNewVersion(String versionName, String newVersionContent) {
                String content = "最新版: V" + versionName + "\n" + newVersionContent;
                buildNewVersionDialog(content);
            }

            @Override
            public void onNewest() {
                if (mIsNewAPPVersion) {
                    ToastUtils.showShort("已经是最新版本");
                }
                dismissProgressDialog();
            }
        });
    }

    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(mContext, R.style.DialogTheme);
        mProgressDialog.setMessage("正在下载,请稍等...");
        mProgressDialog.setMax(100);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", (dialog, which) -> mUpdateManager.cancleUpdate());
    }

    private void dismissProgressDialog() {
        mProgressDialog.setProgress(0);
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    /**
     * 创建发现新版本apk alert dialog
     *
     * @param message dialog显示消息
     */
    private void buildNewVersionDialog(String message) {
        AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setTitle("发现新版本")
                .setMessage(message)
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mUpdateManager.startToUpdate(ConstantUtils.APK_URL, mOnUpdateListener);
                    }
                })
                .setNegativeButton("取消", null)
                .create();
        dialog.show();
    }

    private OnUpdateListener mOnUpdateListener = new OnUpdateListener() {
        @Override
        public void onStartUpdate() {
            mProgressDialog.show();
        }

        @Override
        public void onProgress(int progress) {
            mProgressDialog.setProgress(progress);
        }

        @Override
        public void onApkDownloadFinish(String apkPath) {
            ToastUtils.showShort("newest apk download finish. apkPath: " + apkPath);
            dismissProgressDialog();
        }

        @Override
        public void onUpdateFailed() {
            ToastUtils.showShort("update failed.");
            dismissProgressDialog();
        }

        @Override
        public void onUpdateCanceled() {
            ToastUtils.showShort("update cancled.");
            dismissProgressDialog();
        }

        @Override
        public void onUpdateException() {
            ToastUtils.showShort("update exception.");
            dismissProgressDialog();
        }
    };
}
