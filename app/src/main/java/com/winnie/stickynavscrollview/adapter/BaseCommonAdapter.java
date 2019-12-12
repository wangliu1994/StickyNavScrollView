package com.winnie.stickynavscrollview.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 *
 * @author winnie
 * @date 2017/5/5
 */
public abstract class BaseCommonAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    private Context mContext;
    private int mLayoutId;
    private List<T> mDataList;
    private LayoutInflater mInflater;

    private OnItemClickListener<T> mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    protected BaseCommonAdapter(Context context, int layoutId, List<T> dataList) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mDataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        ViewHolder viewHolder = ViewHolder.get(mContext, null, parent, mLayoutId, -1);
        setListener(parent, viewHolder, viewType);
        return viewHolder;
    }

    private int getPosition(RecyclerView.ViewHolder viewHolder) {
        return viewHolder.getAdapterPosition();
    }

    private boolean isEnabled(int viewType)
    {
        return true;
    }


    private void setListener(final ViewGroup parent, final ViewHolder viewHolder, int viewType) {
        if (!isEnabled(viewType)) {
            return;
        }
        viewHolder.getConvertView().setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                int position = getPosition(viewHolder);
                mOnItemClickListener.onItemClick(parent, v, mDataList.get(position), position);
            }
        });


        viewHolder.getConvertView().setOnLongClickListener(v -> {
            if (mOnItemClickListener != null) {
                int position = getPosition(viewHolder);
                return mOnItemClickListener.onItemLongClick(parent, v, mDataList.get(position), position);
            }
            return false;
        });
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.updatePosition(position);
        convert(holder, mDataList.get(position));
    }

    /**
     *
     * @param holder xxx
     * @param t xxx
     */
    public abstract void convert(ViewHolder holder, T t);

    @Override
    public int getItemCount()
    {
        return mDataList.size();
    }
}
