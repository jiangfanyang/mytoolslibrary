package com.ltf.mytoolslibrary.viewbase.base;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

import com.ltf.mytoolslibrary.R;
import com.ltf.mytoolslibrary.viewbase.utils.NetworkUtils;

public abstract class FragmentBase extends Fragment{

	public View NullView;

	//用于标记视图是否初始化
	public boolean isVisible = false;
	//在onCreate方法之前调用，用来判断Fragment的UI是否是可见的
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(getUserVisibleHint()) {
			isVisible = true;
			onVisible();
		} else {
			isVisible = false;
			onInvisible();
		}
	}
	/**
	 * 视图可见
	 * */
	protected void onVisible(){
		initView();
	}
	/**
	 * 自定义抽象加载数据方法
	 * */
	protected abstract void initView();
	/**
	 * 视图不可见
	 * */
	protected void onInvisible(){}

	/**
	 * 拖拽时调用  回到顶部
	 */
	protected void goTop(){};

	public void setNullView(){
		NullView = getView().findViewById(R.id.null_layout);
	}
	
	public void setIsNullView(Boolean isShow){
		if(NullView == null){
			setNullView();
		}
		if(isShow){
			NullView.setVisibility(View.VISIBLE);
		}else{
			NullView.setVisibility(View.GONE);
		}
	}
	
	public void onBack(){};
	/**有网络的回调事件**/
	public void onHaveNetwork(){};
	/**无网络或网络发生错误的回调事件**/
	public void onNetworkError(){};
	/**
	 * 注意此方法是用于对网络是否可用的
	 * 
	 * @param c
	 */
	public void isNetWorkInfo(Context c) {
		if (NetworkUtils.isNetworkAvailable(c)) {
			if (!getActivity().isFinishing()) //判断activity是否还存在
			onHaveNetwork();
		} else {
			onNetworkError();
		}
	}
}
