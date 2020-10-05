package com.ljkj.wellcover.activity;


import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;

import com.ljkj.wellcover.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 文件名：LoginActivity
 * 作者：Turbo
 * 时间： 2020-10-05 16:02
 * 蚁穴虽小，溃之千里。
 */
public class LoginActivity extends BaseActivity {
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_login;
    }

    @OnClick(R.id.rl_login)
    public void onViewClicked() {
        String userName = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {
            login(userName, password);
        } else {
            toast("用户名或密码不能为空");
        }
    }

    //TODO 网络请求 实现登录功能
    private void login(String userName, String password) {
        startActivity(new Intent(mContext, MainActivity.class));
        finish();
    }
}
