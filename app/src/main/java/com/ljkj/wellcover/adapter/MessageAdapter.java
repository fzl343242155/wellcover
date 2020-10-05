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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 文件名：HomeAdapter
 * 作者：Turbo
 * 时间： 2020-10-05 18:18
 * 蚁穴虽小，溃之千里。
 */
public class MessageAdapter extends BaseRecyclerAdapter<String> {

    private Context mContext;

    public MessageAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        return new Message(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false));
    }

    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, int position, String data) {
        Message holder = (Message) viewHolder;

    }

    class Message extends Holder {

        public Message(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
