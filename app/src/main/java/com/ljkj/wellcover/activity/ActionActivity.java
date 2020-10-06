package com.ljkj.wellcover.activity;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ljkj.wellcover.R;
import com.ljkj.wellcover.adapter.ActionAdapter;
import com.ljkj.wellcover.utils.ImmersionBarUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 文件名：ActionActivity
 * 作者：Turbo
 * 时间： 2020-10-05 19:09
 * 蚁穴虽小，溃之千里。
 */
public class ActionActivity extends BaseActivity {
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.rl_content)
    RecyclerView rlContent;

    private List<String> mList = new ArrayList<>();
    private ActionAdapter mActionAdapter;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_action;
    }

    @OnClick({R.id.ivBack, R.id.rl_open, R.id.rl_close, R.id.rl_clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.rl_open:
                mList.add("2020-01-01 29:30:12: 开启");
                mActionAdapter.addDatas(mList);
                break;
            case R.id.rl_close:
                mList.add("2020-01-01 29:30:12: 关闭");
                mActionAdapter.addDatas(mList);
                break;
            case R.id.rl_clear:
                mList.add("2020-01-01 29:30:12: 解除报警");
                mActionAdapter.addDatas(mList);
                break;
        }
    }

    @Override
    protected void initViews() {
        super.initViews();
        ImmersionBarUtils.initColorBaseBar(ActionActivity.this, R.color._09B1FF);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rlContent.setLayoutManager(mLayoutManager);
        rlContent.setItemAnimator(new DefaultItemAnimator());

        mActionAdapter = new ActionAdapter(mContext);
        rlContent.setAdapter(mActionAdapter);

    }

}
