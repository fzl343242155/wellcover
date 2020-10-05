package com.ljkj.wellcover.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;

public abstract class SingleItemAdapter<T> extends BaseRecyclerAdapter<T> {

    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        if (getLayoutId(viewType) !=0){
            return  new BaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(getLayoutId(viewType), parent, false));
        }
        return new BaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(getLayoutId(viewType), parent, false));
    }

    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, int position, T data) {
        if (viewHolder instanceof BaseViewHolder) {
            onBind((BaseViewHolder) viewHolder, position, data);
        }
    }

    public abstract void onBind(BaseViewHolder holder, int position, T data);

    public abstract int getLayoutId(int viewType);
}
