package com.ltf.mytoolslibrary.viewbase.mvp;


public interface GetNetWorkData<T> {

	void onGetNetWorkData(T s);
	void showError(String e);
}
