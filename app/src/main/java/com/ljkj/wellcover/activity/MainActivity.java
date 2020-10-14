package com.ljkj.wellcover.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.view.KeyEvent;

import com.ljkj.wellcover.R;
import com.ljkj.wellcover.fragment.HomeFragment;
import com.ljkj.wellcover.fragment.MessageFragment;
import com.ljkj.wellcover.fragment.MyFragment;
import com.ljkj.wellcover.utils.ConstantUtils;
import com.ljkj.wellcover.utils.update.OnCheckUpdateListener;
import com.ljkj.wellcover.utils.update.OnUpdateListener;
import com.ljkj.wellcover.utils.update.UpdateManager;
import com.ljkj.wellcover.view.BottomBar;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.bottom_bar)
    BottomBar mBottomBar;

    private long touchTime = 0;
    private UpdateManager mUpdateManager;
    private ProgressDialog mProgressDialog;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == event.getKeyCode()) {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - touchTime) >= 2000) {
                toast(getString(R.string.hint_click_exit));
                touchTime = currentTime;
            } else {
                finish();
            }
        }
        return false;
    }

    @Override
    protected void initViews() {
        mBottomBar.setContainer(R.id.fl_container)
                .setTitleSize(13)
                .setTitleBeforeAndAfterColor(getResources().getColor(R.color._cdcdcd),
                        getResources().getColor(R.color._515151))
                .addItem(HomeFragment.class,
                        getString(R.string.main_home),
                        R.mipmap.home,
                        R.mipmap.homed)
                .addItem(MessageFragment.class,
                        getString(R.string.main_messge),
                        R.mipmap.message,
                        R.mipmap.messaged)
                .addItem(MyFragment.class,
                        getString(R.string.main_my),
                        R.mipmap.my,
                        R.mipmap.myed)
                .setFirstChecked(0)
                .build();
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
            toast("newest apk download finish. apkPath: " + apkPath);
            dismissProgressDialog();
        }

        @Override
        public void onUpdateFailed() {
            toast("update failed.");
            dismissProgressDialog();
        }

        @Override
        public void onUpdateCanceled() {
            toast("update cancled.");
            dismissProgressDialog();
        }

        @Override
        public void onUpdateException() {
            toast("update exception.");
            dismissProgressDialog();
        }
    };
}
