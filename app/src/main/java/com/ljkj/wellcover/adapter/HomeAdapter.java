package com.ljkj.wellcover.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ljkj.wellcover.R;
import com.ljkj.wellcover.activity.ActionActivity;
import com.ljkj.wellcover.bean.EquipmentBean;
import com.ljkj.wellcover.utils.ConstantUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 文件名：HomeAdapter
 * 作者：Turbo
 * 时间： 2020-10-05 18:18
 * 蚁穴虽小，溃之千里。
 */
public class HomeAdapter extends BaseRecyclerAdapter<EquipmentBean.ListBean> {

    private Context mContext;

    public HomeAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        return new HomeHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false));
    }

    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, int position, EquipmentBean.ListBean data) {
        HomeHolder holder = (HomeHolder) viewHolder;
        holder.tvNumber.setText(data.getId());
        String lockStatus = data.getLockStatus();
        //设备状态：1：开启  2 ： 关闭 3： 解除报警
        switch (lockStatus) {
            case "1":
                holder.tvState.setText("开启");
                holder.tvState.setTextColor(mContext.getResources().getColor(R.color._1afa29));
                break;
            case "2":
                holder.tvState.setText("关闭");
                holder.tvState.setTextColor(mContext.getResources().getColor(R.color._d81e06));
                break;
            case "3":
                holder.tvState.setText("解除报警");
                holder.tvState.setTextColor(mContext.getResources().getColor(R.color._f4ea2a));
                break;
        }
        holder.tvUnit.setText(data.getCompany());
        holder.tvStreet.setText(data.getStreetName());
//        holder.rlAction.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(mContext, ActionActivity.class);
//                intent.putExtra(ConstantUtils.DATA, data);
//                mContext.startActivity(intent);
//            }
//        });
    }

    class HomeHolder extends Holder {

        @BindView(R.id.tv_number)
        TextView tvNumber;
        @BindView(R.id.tv_state)
        TextView tvState;
        @BindView(R.id.tv_unit)
        TextView tvUnit;
        @BindView(R.id.tv_street)
        TextView tvStreet;


        public HomeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
