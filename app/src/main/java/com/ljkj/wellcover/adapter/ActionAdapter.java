package com.ljkj.wellcover.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class ActionAdapter extends BaseRecyclerAdapter<String> {

    private Context mContext;

    public ActionAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        return new ActionHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_action, parent, false));
    }

    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, int position, String data) {
        ActionHolder holder = (ActionHolder) viewHolder;
        holder.tvPlay.setText(data);
    }

    class ActionHolder extends Holder {

        @BindView(R.id.tv_play)
        TextView tvPlay;

        public ActionHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
