package com.ltf.mytoolslibrary.viewbase.mvp;

import android.view.View;
import android.widget.AdapterView;

import com.ltf.mytoolslibrary.viewbase.adapter.ViewHolder;

public interface GetNetWorkPresenter<Y> extends Presenter{

	/**adapter的通用返回格式**/
	void UpdateUiFromAdapterConvert(ViewHolder helper, Y item, int position);
	
	/**AbsListView列表项某项被点击了**/
	void AbsListViewOnItemClick(AdapterView<?> parent, View view, int position, long id);
}
