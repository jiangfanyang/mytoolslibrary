package com.ltf.mytoolslibrary.viewbase.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/5/4 0004.
 */
public abstract class CommonAdapter1<T> extends BaseAdapter {
    protected LayoutInflater mInflater;
    protected Context mContext;
    protected List<T> mDatas;
    protected final int mItemLayoutId;

    public CommonAdapter1(Context context, List<T> mDatas, int mItemLayoutId) {
        this.mContext = context;
        this.mDatas = mDatas;
        this.mItemLayoutId = mItemLayoutId;
        mInflater = LayoutInflater.from(context);

    }


    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final ViewHolder viewHolder = ViewHolder.get(mContext, convertView, viewGroup, mItemLayoutId, position);
        convert(viewHolder, getItem(position),position);
        return viewHolder.getConvertView();
    }

    public abstract void convert(ViewHolder helper, T item,int position);

    private ViewHolder getViewHolder(int position, View convertView, ViewGroup parent) {
        return ViewHolder.get(mContext, convertView, parent, mItemLayoutId, position);
    }
}
