package com.ltf.mytoolslibrary.viewbase.takephoto.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ltf.mytoolslibrary.R;
import com.ltf.mytoolslibrary.viewbase.takephoto.ImagePicker;
import com.ltf.mytoolslibrary.viewbase.takephoto.Utils;
import com.ltf.mytoolslibrary.viewbase.takephoto.bean.ImageFolder;
import com.ltf.mytoolslibrary.viewbase.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧 Github地址：https://github.com/jeasonlzy0216
 * 版    本：1.0
 * 创建日期：2016/5/19
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class ImageFolderAdapter extends BaseAdapter {

    private ImagePicker imagePicker;
    private Activity mActivity;
    private LayoutInflater mInflater;
    private int mImageSize;
    private List<ImageFolder> imageFolders;
    private int lastSelected = 0;

    public ImageFolderAdapter(Activity activity, List<ImageFolder> folders) {
        mActivity = activity;
        if (folders != null && folders.size() > 0) imageFolders = folders;
        else imageFolders = new ArrayList<>();

        imagePicker = ImagePicker.getInstance();
        mImageSize = Utils.getImageItemWidth(mActivity);
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void refreshData(List<ImageFolder> folders) {
        if (folders != null && folders.size() > 0) imageFolders = folders;
        else imageFolders.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return imageFolders.size();
    }

    @Override
    public ImageFolder getItem(int position) {
        return imageFolders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_folder_list_item, parent, false);
            holder = new ViewHolder(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ImageFolder folder = getItem(position);
        holder.folderName.setText(folder.name);
        holder.imageCount.setText(mActivity.getString(R.string.folder_image_count, folder.images.size()));
        holder.folderName.setTextSize(AutoUtils.getDisplayHeightValue(6));
        holder.imageCount.setTextSize(AutoUtils.getDisplayHeightValue(4));
        LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(AutoUtils.getDisplayWidthValue(120), AutoUtils.getDisplayHeightValue(120));
        l.setMargins(AutoUtils.getDisplayHeightValue(4), AutoUtils.getDisplayHeightValue(4), AutoUtils.getDisplayHeightValue(4), AutoUtils.getDisplayHeightValue(4));//4个参数按顺序分别是左上右下
        holder.cover.setLayoutParams(l);
        imagePicker.getImageLoader().displayImage(mActivity, folder.cover.path, holder.cover, mImageSize, mImageSize);

        if (lastSelected == position) {
            holder.folderCheck.setVisibility(View.VISIBLE);
        } else {
            holder.folderCheck.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    public void setSelectIndex(int i) {
        if (lastSelected == i) {
            return;
        }
        lastSelected = i;
        notifyDataSetChanged();
    }

    public int getSelectIndex() {
        return lastSelected;
    }

    private class ViewHolder {
        ImageView cover;
        TextView folderName;
        TextView imageCount;
        ImageView folderCheck;

        public ViewHolder(View view) {
            cover = (ImageView) view.findViewById(R.id.iv_cover);
            folderName = (TextView) view.findViewById(R.id.tv_folder_name);
            imageCount = (TextView) view.findViewById(R.id.tv_image_count);
            folderCheck = (ImageView) view.findViewById(R.id.iv_folder_check);
            view.setTag(this);
        }
    }
}
