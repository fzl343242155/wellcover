package com.ljkj.wellcover.fragment;

import android.content.Intent;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ljkj.wellcover.R;
import com.ljkj.wellcover.activity.EquipmentActivity;
import com.ljkj.wellcover.activity.LoginActivity;
import com.ljkj.wellcover.utils.AppUpdateUtils;
import com.ljkj.wellcover.utils.ImmersionBarUtils;
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

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_my;
    }

    @Override
    public void initImmersionBar() {
        ImmersionBarUtils.initColorBar(getActivity());
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
                toast("改功能正在开发");
//                new AppUpdateUtils().update(mContext, true);
                break;
        }
    }
}
