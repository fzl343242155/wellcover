package com.ljkj.wellcover.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_HEADER = 999;
    public static final int TYPE_NORMAL = 998;
    public static final int TYPE_EMPTY = 997;
    public static final int TYPE_TITLE = 996;//用来做悬停

    public List<T> mDatas = new ArrayList<>();

    private View mHeaderView;
    private View mEmptyView;
    private View mTitleView;

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener li) {
        mListener = li;
    }

    public void setTitleView(View titleView) {//插入到一号位置
        mTitleView = titleView;
        notifyItemInserted(1);
    }

    public View getTitleView() {
        return mTitleView;
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    public void setEmptyView(View view) {
        mEmptyView = view;
        notifyDataSetChanged();
    }

    public void addDatas(List<T> datas) {//刷新数据
        mDatas = datas;
        notifyDataSetChanged();
    }

    public List<T> getDatas() {
        return mDatas;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && mHeaderView != null) return TYPE_HEADER;
        if (position == 1 && mTitleView != null) return TYPE_TITLE;
        if (mEmptyView != null && mDatas.size() == 0) return TYPE_EMPTY;
        return TYPE_NORMAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER) return new Holder(mHeaderView);
        if (mEmptyView != null && viewType == TYPE_EMPTY) return new Holder(mEmptyView);
        if (mTitleView != null && viewType == TYPE_TITLE) return new Holder(mTitleView);
        return onCreate(parent, viewType);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == TYPE_HEADER
                || getItemViewType(position) == TYPE_EMPTY
                || getItemViewType(position) == TYPE_TITLE)
            return;

        final int pos = getRealPosition(viewHolder);
        final T data = mDatas.get(pos);
        onBind(viewHolder, pos, data);

        if (mListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(pos, data);
                }
            });
        }

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) == TYPE_HEADER
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null
                && lp instanceof StaggeredGridLayoutManager.LayoutParams
                && holder.getLayoutPosition() == 0) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        if (mTitleView != null) {
            position--;
        }
        if (mHeaderView != null) {
            position--;
        }
        if (mEmptyView != null && mDatas.size() == 0) {
            position--;
        }
        return position;
    }

    @Override
    public int getItemCount() {
        if (mDatas == null) {
            return 0;
        }
        int size = mDatas.size();
        if (mHeaderView != null) {
            size++;
        }
        if (mEmptyView != null && mDatas.size() == 0) {
            size++;
        }
        if (mTitleView != null) {
            size++;
        }
        return size;
    }

    public abstract RecyclerView.ViewHolder onCreate(ViewGroup parent, final int viewType);

    public abstract void onBind(RecyclerView.ViewHolder viewHolder, int position, T data);

    public class Holder extends RecyclerView.ViewHolder {
        public Holder(View itemView) {
            super(itemView);
        }
    }

    public interface OnItemClickListener<T> {
        void onItemClick(int position, T data);
    }
}
