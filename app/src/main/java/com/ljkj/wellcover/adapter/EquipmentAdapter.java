package com.ljkj.wellcover.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ljkj.wellcover.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 文件名：HomeAdapter
 * 作者：Turbo
 * 时间： 2020-10-05 18:18
 * 蚁穴虽小，溃之千里。
 */
public class EquipmentAdapter extends BaseRecyclerAdapter<String> {

    private Context mContext;

    public EquipmentAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        return new HomeHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_equipment, parent, false));
    }

    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, int position, String data) {
        HomeHolder holder = (HomeHolder) viewHolder;

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

        public HomeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
