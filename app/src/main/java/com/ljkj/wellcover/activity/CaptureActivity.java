package com.ljkj.wellcover.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.widget.CheckBox;

import com.ljkj.wellcover.R;
import com.ljkj.wellcover.utils.ConstantUtils;
import com.ljkj.wellcover.utils.ImmersionBarUtils;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

/**
 * 文件名：CaptureActivity
 * 作者：Turbo
 * 时间： 2020-10-26 16:21
 * 蚁穴虽小，溃之千里。
 */
public class CaptureActivity extends BaseActivity implements QRCodeView.Delegate {

    private static final String TAG = "CaptureActivity";

    @BindView(R.id.zxingview)
    ZXingView mZXingView;
    @BindView(R.id.light_switch)
    CheckBox lightSwitch;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_capture;
    }

    @OnClick(R.id.ivBack)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected void initViews() {
        super.initViews();
        ImmersionBarUtils.initColorBar(CaptureActivity.this);
        mZXingView.setDelegate(this);
        lightSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mZXingView.openFlashlight(); // 打开闪光灯
            } else {
                mZXingView.closeFlashlight();//关闭闪光灯
            }
        });
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        Log.e(TAG, "onScanQRCodeSuccess: " + result);
        //扫描结果成功回调
        if (!TextUtils.isEmpty(result)) {
            vibrate();
            Intent intent = new Intent();
            intent.putExtra(ConstantUtils.SCANQRCODESUCCESS, result);
            setResult(ConstantUtils.CAPTURE2ADDEQUIPMENT, intent);
            finish();
            return;
        }
        mZXingView.startSpot();// 开始识别
    }

    /**
     * 震动
     */
    @SuppressLint("MissingPermission")
    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {
        String tipText = mZXingView.getScanBoxView().getTipText();
        String ambientBrightnessTip = "\n环境过暗，请打开闪光灯";
        if (isDark) {
            if (!tipText.contains(ambientBrightnessTip)) {
                mZXingView.getScanBoxView().setTipText(tipText + ambientBrightnessTip);
            }
        } else {
            if (tipText.contains(ambientBrightnessTip)) {
                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip));
                mZXingView.getScanBoxView().setTipText(tipText);
            }
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        mZXingView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
        mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
    }

    @Override
    protected void onStop() {
        mZXingView.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mZXingView.onDestroy(); // 销毁二维码扫描控件
        super.onDestroy();
    }
}
