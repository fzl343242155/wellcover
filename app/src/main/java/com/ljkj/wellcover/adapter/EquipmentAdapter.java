package com.ljkj.wellcover.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ljkj.wellcover.R;
import com.ljkj.wellcover.bean.EquipmentBean;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 文件名：HomeAdapter
 * 作者：Turbo
 * 时间： 2020-10-05 18:18
 * 蚁穴虽小，溃之千里。
 */
public class EquipmentAdapter extends BaseRecyclerAdapter<EquipmentBean.ListBean> {

    private Context mContext;
    private ImageView mIvSelect;
    private SparseBooleanArray mSelectedPositions = new SparseBooleanArray();

    public EquipmentAdapter(Context context, ImageView ivSelect) {
        this.mContext = context;
        this.mIvSelect = ivSelect;
    }

    //更新adpter的数据和选择状态
    public void updateDataSet(ArrayList<EquipmentBean.ListBean> list) {
        mSelectedPositions = new SparseBooleanArray();
        addDatas(list);
    }

    //获得选中条目的结果
    public ArrayList<EquipmentBean.ListBean> getSelectedItem() {
        ArrayList<EquipmentBean.ListBean> selectList = new ArrayList<>();
        for (int i = 0; i < getDatas().size(); i++) {
            if (isItemChecked(i)) {
                selectList.add(getDatas().get(i));
            }
        }
        return selectList;
    }

    //设置给定位置条目的选择状态
    public void setItemChecked(int position, boolean isChecked) {
        mSelectedPositions.put(position, isChecked);
        notifyItemChanged(position);
    }

    //根据位置判断条目是否选中
    public boolean isItemChecked(int position) {
        return mSelectedPositions.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        return new HomeHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_equipment, parent, false));
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
                break;
            case "2":
                holder.tvState.setText("关闭");
                break;
            case "3":
                holder.tvState.setText("解除报警");
                break;
        }
        holder.tvUnit.setText(data.getCompany());
        holder.tvStreet.setText(data.getStreetName());

        if (isItemChecked(position)) {
            //true
            holder.ivSelect.setBackgroundResource(R.color._07a0ed);
        } else {
            //false
            holder.ivSelect.setBackgroundResource(R.color.black);
        }

        holder.rlContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isItemChecked(position)) {
                    setItemChecked(position, false);
                } else {
                    setItemChecked(position, true);
                }
            }
        });

        if (getDatas().size() == getSelectedItem().size()) {
            mIvSelect.setBackgroundResource(R.color._07a0ed);
        }else{
            mIvSelect.setBackgroundResource(R.color.black);
        }
    }

    class HomeHolder extends Holder {

        @BindView(R.id.iv_select)
        ImageView ivSelect;
        @BindView(R.id.tv_number)
        TextView tvNumber;
        @BindView(R.id.tv_state)
        TextView tvState;
        @BindView(R.id.tv_unit)
        TextView tvUnit;
        @BindView(R.id.tv_street)
        TextView tvStreet;
        @BindView(R.id.rl_content)
        RelativeLayout rlContent;

        public HomeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
