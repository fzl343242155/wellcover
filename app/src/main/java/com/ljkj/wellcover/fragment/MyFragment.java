package com.ljkj.wellcover.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ljkj.wellcover.BuildConfig;
import com.ljkj.wellcover.R;
import com.ljkj.wellcover.activity.EquipmentActivity;
import com.ljkj.wellcover.activity.LoginActivity;
import com.ljkj.wellcover.utils.ImmersionBarUtils;
import com.ljkj.wellcover.utils.update.OnUpdateListener;
import com.ljkj.wellcover.utils.update.UpdateManager;
import com.ljkj.wellcover.view.CircleImageView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 文件名：HomeFragment
 * 作者：Turbo
 * 时间： 2020-10-05 16:56
 * 蚁穴虽小，溃之千里。
 */
public class MyFragment extends BaseFragment {
    @BindView(R.id.civ_head)
    CircleImageView civHead;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_permissions)
    TextView tvPermissions;
    @BindView(R.id.ll_userinfo)
    LinearLayout llUserinfo;
    @BindView(R.id.tv_compayname)
    TextView tvCompayname;
    @BindView(R.id.ll_compayinfo)
    LinearLayout llCompayinfo;
    @BindView(R.id.rl_logout)
    RelativeLayout rlLogout;

    private UpdateManager mUpdateManager;
    private ProgressDialog mProgressDialog;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_my;
    }

    @Override
    public void initImmersionBar() {
        ImmersionBarUtils.initColorBar(getActivity());
    }

    @Override
    protected void initViews() {
        super.initViews();
        initProgressDialog();
        mUpdateManager = UpdateManager.getInstance();
    }

    @OnClick({R.id.rl_equipment, R.id.rl_editpassword, R.id.rl_permissions, R.id.rl_about, R.id.rl_logout, R.id.rl_update})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_equipment:
                startActivity(new Intent(mContext, EquipmentActivity.class));
                break;
            case R.id.rl_editpassword:
                break;
            case R.id.rl_permissions:
                break;
            case R.id.rl_about:
                break;
            case R.id.rl_logout:
                startActivity(new Intent(mContext, LoginActivity.class));
                getActivity().finish();
                break;
            case R.id.rl_update:
//                mUpdateManager.checkUpdate(ConstantUtils.VERSION_INFO_URL, new OnCheckUpdateListener() {
//                    @Override
//                    public void onFindNewVersion(String versionName, String newVersionContent) {
//                        String content = "最新版: V" + versionName + "\n" + newVersionContent;
//                        buildNewVersionDialog(content);
//                    }
//
//                    @Override
//                    public void onNewest() {
//                       toast("已经是最新版本");
//                        dismissProgressDialog();
//                    }
//                });
                String content = "最新版: V" + BuildConfig.VERSION_NAME + "\n更新内容如下：\n" +
                        "   1.xxxxxx\n" +
                        "   2.xxxxxx\n" +
                        "   3.xxxxxx";
                buildNewVersionDialog(content);


                break;
        }
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

                        mProgressDialog.show();
//                        mUpdateManager.startToUpdate(ConstantUtils.APK_URL, mOnUpdateListener);
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
