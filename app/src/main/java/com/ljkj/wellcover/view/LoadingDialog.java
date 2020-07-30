package com.ljkj.wellcover.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.annotation.NonNull;

import com.ljkj.wellcover.R;

/**
 * 文件名：LoadingDialog
 * 作者：Turbo
 * 时间： 2020-07-30 16:48
 * 蚁穴虽小，溃之千里。
 */
public class LoadingDialog extends Dialog {

    public LoadingDialog(@NonNull Context context) {
        super(context, R.style.circular_dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_loading);
        setCanceledOnTouchOutside(false);
        setCancelable(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 屏蔽返回键
            dismiss();
            return true;
        }
        return true;
    }
}
