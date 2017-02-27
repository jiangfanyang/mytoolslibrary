package com.ltf.mytoolslibrary.viewbase.mvp;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.ltf.mytoolslibrary.viewbase.adapter.CommonAdapter1;
import com.ltf.mytoolslibrary.viewbase.adapter.ViewHolder;
import com.ltf.mytoolslibrary.viewbase.app.ApplicationBase;
import com.ltf.mytoolslibrary.viewbase.isnull.IsNullUtils;
import com.ltf.mytoolslibrary.viewbase.refresh.SwipyRefreshLayout;
import com.ltf.mytoolslibrary.viewbase.refresh.SwipyRefreshLayoutDirection;
import com.ltf.mytoolslibrary.viewbase.utils.GetJson;
import com.ltf.mytoolslibrary.viewbase.utils.HandlerUtil;
import com.ltf.mytoolslibrary.viewbase.utils.show.L;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 李堂飞 20160706 MVP思想:V处理UI更新和用户事件交互   M处理数据网络获取和携带数据   P处理业务逻辑(比如Adapter或数据加载以及更新的操作) 主要是释放代码量  View只需处理逻辑交互  此类封装了下拉刷新上拉加载以及Adapter 不需要View中设置Adapter
 * Mvp思想 使得架构通用化 封装网络请求数据
 * @param <T> 从网络获取的  是数据携带Model
 * @param <Y> T中的集合  是数据列表集合Model
 *
 * 该类的使用方法:
 * 1.网络请求需要返回字符串到View 请使用onInitData1(null,RequestParams params,String urls,final GetNetWorkData<String> ts);
 * 2.网络请求需要返回实体到View 请使用onInitData(final Class classOfT,RequestParams params,String urls,final GetNetWorkData<T> ts)
 * 3.网络请求需要返回实体到View并让该类实现刷新和Adapter的自动加载  请使用:
 *  (1)new GetNetWorkDataPresenter<UserMsgModle,UserMsgModle.data.list>(this, this);并让当前Activity实现GetNetWorkPresenter(Activity implements GetNetWorkPresenter)
 *  (2)设置自动Adapter GetNetWorkDataPresenter.UpdateUiFromAdapter(lv_msg, list, R.layout.list_steward_pay_msg);
 *  (3)设置刷新控件 GetNetWorkDataPresenter.initSwipyRefreshLayout(mSwipyRefreshLayout, SwipyRefreshLayoutDirection.BOTH, pageSize, "pageNumber", "pageSize");(注意:"pageNumber"和 "pageSize"是请求体中的key)
 *  (4)设置整个业务流程中需返回给View的数据  GetNetWorkDataPresenter.setonRefreshOnLoadPageNumber(monRefreshOnLoadPageNumber);次接口中包含有onBackPageNumberAndSize(int page, int size, List<Y> mData)
 *
 * 建议在onBackPageNumberAndSize()中设置listview的NullView(空布局);在UpdateUiFromAdapterConvert(ViewHolder helper, Object item,
int position)中更新Adapter的UI;  在AbsListViewOnItemClick(AdapterView parent, View view,int position, long id)中处理列表某项点击事件;
在onGetNetWorkData(T s)中处理更新Adapter(调用GetNetWorkDataPresenter.notifyDataSetChanged(Y);)
 */
public class GetNetWorkDataPresenter<T,Y> implements SwipyRefreshLayout.OnRefreshListener{

	private Context mContext;
	private GetNetWorkDataImpl mAppOrderBuyInfoImpl;

	public GetNetWorkDataImpl getmAppOrderBuyInfoImpl() {
		return mAppOrderBuyInfoImpl;
	}

	private GetNetWorkPresenter<Y> mPresenter;

	public GetNetWorkDataPresenter(Context mContext) {
		this.mContext = mContext;
		mAppOrderBuyInfoImpl = new GetNetWorkDataImpl();
	}

	public GetNetWorkDataPresenter(Context mContext, GetNetWorkPresenter<Y> mPresenter) {
		this.mContext = mContext;
		this.mPresenter = mPresenter;
		mAppOrderBuyInfoImpl = new GetNetWorkDataImpl();
	}

	private SwipyRefreshLayout mSwipyRefreshLayout;
	private String pageKey = "pageNumber";
	private String pageSizeKey = "pageSize";
	/**
	 * 初始化刷新以及上拉加载下拉刷新网络请求时的key与返回数据条数
	 * @param mSwipyRefreshLayout 刷新控件
	 * @param direction 刷新方式TOP只有下拉刷新 BOTTOM只有加载更多 BOTH全都有
	 * @param pageSize 网络请求时字符串中携带的数据条数
	 * @param pageKey 网络请求时pageNumber的key
	 * @param pageSizeKey 网络请求时pageSize的key
	 */
	public void initSwipyRefreshLayout(SwipyRefreshLayout mSwipyRefreshLayout, SwipyRefreshLayoutDirection direction, int pageSize, String pageKey, String pageSizeKey){
		this.mSwipyRefreshLayout = mSwipyRefreshLayout;
		this.pageSize = pageSize;
		this.pageSizeKey = pageSizeKey;
		this.pageKey = pageKey;
		mSwipyRefreshLayout.setOnRefreshListener(this);
		mSwipyRefreshLayout.setDirection(direction);
	}

	private CommonAdapter1<Y> adapter;

	/**
	 * @return 返回框架内的Adapter
	 */
	public CommonAdapter1<Y> getGetNetWorkAdapter(){
		return adapter;
	}

	private List<Y> mDatas = new ArrayList<>();
	private AbsListView mAbsListView;
	/**
	 * 构建Adapter
	 * @param mData 数据源
	 * @param mItemLayoutId listView或GridVIew的item布局参数
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void UpdateUiFromAdapter(AbsListView mAbsListView, List<Y> mData, int mItemLayoutId){
		mAbsListView.setFocusable(false);
		this.mDatas.addAll(mData);
		this.mAbsListView = mAbsListView;
		adapter = new CommonAdapter1<Y>(mContext,mDatas,mItemLayoutId) {

			@Override
			public void convert(ViewHolder helper, Y item, int position) {
				if(mPresenter != null)
					mPresenter.UpdateUiFromAdapterConvert(helper, item, position);
			}
		};
		mAbsListView.setAdapter(adapter);
		mAbsListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				if(mPresenter != null){
					mPresenter.AbsListViewOnItemClick(parent, view, position, id);
				}
			}
		});

		updateIsVisibiNullView();
	}

	private int typle = 0;
	/**设置listview没有数据时展示的VIew  此方法需重写AbsListView的onMeasure(int widthMeasureSpec, int heightMeasureSpec) 在方法中
	 * int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
	 *			MeasureSpec.AT_MOST);
	 * 否则会导致列表为空时不能再触发刷新
	 * typle:listview设置空布局的方式 0代表AbsListView.setEmptyView(nullView); 1代表判断数据集合是否为空来控制是否显示nullView 一般用方式1
	 * **/
	public void setAbsListViewNullView(View nullView,int typle){
		this.nullView = nullView;
		this.typle = typle;
	}

	public View getAbsListViewNullView(){
		return this.nullView;
	}

	private boolean isShuaXin = true;
	/**
	 * 刷新UpdateUiFromAdapter中的数据
	 * @param mData 服务器返回的数据需要更新到UI的数据源
	 */
	public List<Y> notifyDataSetChanged(List<Y> mData){
		if(isShuaXin){
			this.mDatas.clear();
		}
		this.mDatas.addAll(mData);
		if(adapter != null)
			adapter.notifyDataSetChanged();
		updateIsVisibiNullView();
		if(monRefreshOnLoadPageNumber != null){
			monRefreshOnLoadPageNumber.onBackPageNumberAndSize(pageNo, pageSize,mDatas);
		}
		return this.mDatas;
	}

	/** 调用此方法前务必先调用setAbsListViewNullView(View nullView,int typle) 否则无意义
	 * @param isShow 是否显示空布局
	 */
	public void setIsNullView(Boolean isShow){
		if (nullView != null) {
			if (isShow) {
				nullView.setVisibility(View.VISIBLE);
			} else {
				nullView.setVisibility(View.GONE);
			}
		}

		if(mAbsListView != null){
			if (!isShow) {
				mAbsListView.setVisibility(View.VISIBLE);
			} else {
				mAbsListView.setVisibility(View.GONE);
			}
		}
	}

	/***
	 * 调用此方法前务必先调用setAbsListViewNullView(View nullView,int typle) 否则无意义
	 * */
	private void updateIsVisibiNullView() {
		if (mAbsListView != null && nullView != null) {
			if (getTyple() == 0) {
				mAbsListView.setEmptyView(nullView);
			}else if(typle == -1){
				if (mDatas.size() == 0) {
					nullView.setAlpha(1);
					mAbsListView.setAlpha(0);
				} else {
					nullView.setAlpha(0);
					mAbsListView.setAlpha(1);
				}
			} else {
				if (mDatas.size() == 0) {
					setIsNullView(true);
				} else {
					setIsNullView(false);
				}
			}
		}
	}

	public void clearInitData(){
		this.classOfT = null;
		this.isCache = true;
		this.params = null;
		this.urls = null;
		this.ts = null;
		this.typle = 0;
		this.NetWorkJiaZaiType = 0;
		this.isShuaXin = true;
		this.pageNo = 1;
		this.pageSize =20;
		this.monRefreshBack = null;
		this.SuccessdKey = null;
		this.SuccessdValue = null;
		this.Errormsg = null;
		this.IF_NONE_CACHE_REQUESTs = false;
		this.mCacheMode = GetNetWorkDataImpl.NO_CACHE;//默认不使用缓存
	}

	private boolean IF_NONE_CACHE_REQUESTs = false;
	/**
	 * 设置当前网络请求 IF_NONE_CACHE_REQUEST：如果缓存不存在才请求网络，否则使用缓存。
	 */
	public void setIF_NONE_CACHE_REQUEST(boolean IF_NONE_CACHE_REQUESTss){
		this.IF_NONE_CACHE_REQUESTs = IF_NONE_CACHE_REQUESTss;
		if(IF_NONE_CACHE_REQUESTs){
			isCache = true;
		}
	}

	private boolean getIF_NONE_CACHE_REQUEST(){
		return this.IF_NONE_CACHE_REQUESTs;
	}

	private int NetWorkJiaZaiType = 0;
	private Map<String, String> itemList = new HashMap<>();
	/**
	 * 设置网络加载方式  默认NetWorkJiaZaiType = 0;意识用框架默认加载方式
	 * NetWorkJiaZaiType = 1;(或其它值) 说明请求用URL拼接请求 请求的参数封装在HashMap<String, String> map中
	 */
	public void setNetWorkJiaZaiType(int NetWorkJiaZaiTypes,Map<String, String> map){
		this.itemList = map;
		this.NetWorkJiaZaiType = NetWorkJiaZaiTypes;
	}

	public Map<String, String> getNetWorkJiaZaiHashMap(){
		return itemList;
	}

	public int getNetWorkJiaZaiType(){
		return this.NetWorkJiaZaiType;
	}

	private View nullView = null;
	public int getTyple(){
		return this.typle;
	}

	/**移除列表中某一项**/
	public void remove(int position){
		if(this.mDatas.size() > position){
			this.mDatas.remove(position);
			if(adapter != null)
				adapter.notifyDataSetChanged();
		}
		if(monRefreshOnLoadPageNumber != null){
			monRefreshOnLoadPageNumber.onBackPageNumberAndSize(pageNo, pageSize,mDatas);
		}
	}

	public void notifyDataSetChanged(){
		if(adapter != null)
			adapter.notifyDataSetChanged();
	}

	private String SuccessdKey = null;
	private String SuccessdValue = null;
	private String Errormsg = null;
	/**
	 * 设置网络成功标志
	 * @param SuccessdKey 成功时的标示 如:200
	 * @param SuccessdValue 成功时需解析的字段名字 如:code
	 * @param Errormsg 失败时需解析的字段名字 如:msg
	 */
	public void setSuccessdTag(String SuccessdKey,String SuccessdValue,String Errormsg){
		this.SuccessdKey = SuccessdKey;
		this.SuccessdValue = SuccessdValue;
		this.Errormsg = Errormsg;
	}

	private boolean isCache = true;//默认使用缓存

	/***设置是否网络请求缓存**/
	public void setIsCache(boolean isCaches){
		this.isCache = isCaches;
	}

	private boolean getIsisCache(){
		return this.isCache;
	}

	private Class classOfT = null;
	private Map<String,String> params = null;
	private String urls = null;
	private GetNetWorkData<T> ts;
	@SuppressWarnings("unchecked")
	public void onInitData(final Class classOfT, Map<String,String> params, final String urls, final GetNetWorkData<T> ts){
		this.classOfT = classOfT;
		this.params = params;
		this.urls = urls;
		this.ts = ts;

		mAppOrderBuyInfoImpl.checkNetHave(urls);
		if(mPresenter != null){
			mPresenter.showLoading();
		}
		if(mSwipyRefreshLayout != null && pageNo == 1){
			mSwipyRefreshLayout.setRefreshing(true);
		}
		String strUrl = "";
		if(getNetWorkJiaZaiType() != 0){
			String strs = "";
			params = null;
			Map<String, String> env = getNetWorkJiaZaiHashMap();
			for (String envName : env.keySet()){
				strs += "&"+envName+"="+env.get(envName);
			}

			if(!IsNullUtils.isNulls(pageKey)){
				strUrl = urls +"?"+pageKey+"="+pageNo;
			}
			if(!IsNullUtils.isNulls(pageSizeKey)){
				strUrl = strUrl +"&"+pageSizeKey+"="+pageSize;
			}

			strUrl += strs;
		}else{
			strUrl = urls;
		}

		L.e("url:",strUrl+"----"+ ApplicationBase.getRunningActivityName(mContext));
		mAppOrderBuyInfoImpl.onInitData(new ICallBackListener<String>() {

			@Override
			public void showFailed(final String error) {
				mAppOrderBuyInfoImpl.removeHavedNet(urls);
				HandlerUtil.updateUIFromAnyThread(new Runnable() {
					@Override
					public void run() {
						ts.showError(error);
						isSuccessd = true;
						handler.removeMessages(1);
						if(mPresenter != null){
							mPresenter.hideLoading();
						}
						updateIsVisibiNullView();
						if(mSwipyRefreshLayout != null){
							mSwipyRefreshLayout.setRefreshing(false);
						}
					}
				});
			}

			@Override
			public void initData(final String arg0) {
				mAppOrderBuyInfoImpl.removeHavedNet(urls);
				HandlerUtil.updateUIFromAnyThread(new Runnable() {
					@Override
					public void run() {
						isSuccessd = true;
						handler.removeMessages(1);
						if(IsNullUtils.isNulls(arg0)){
							ts.showError("网络请求失败，请稍候重试");
						}else{
							if(classOfT == null){
								ts.onGetNetWorkData((T) arg0);
							}else{
								//L.e("设置请求参数监听",SuccessdKey,SuccessdValue,Errormsg);
								if(IsNullUtils.isNulls(SuccessdKey,SuccessdValue,Errormsg)){
									if("200".equals(GetJson.getJson(arg0, "code"))){
										try{
											T s = (T) new Gson().fromJson(arg0, classOfT);
											ts.onGetNetWorkData(s);
										}catch (Exception e){
											ts.showError("服务器数据异常");
											L.e("服务器数据异常:",e.toString());
										}
									}else{
										ts.showError(GetJson.getJson(arg0, "msg"));
									}
								}else{
									if((SuccessdKey+"").equals(GetJson.getJson(arg0, SuccessdValue))){
										try{
											T s = (T) new Gson().fromJson(arg0, classOfT);
											ts.onGetNetWorkData(s);
										}catch (Exception e){
											ts.showError("服务器数据异常");
											L.e("服务器数据异常:",e.toString());
										}
									}else{
										ts.showError(GetJson.getJson(arg0, Errormsg));
									}
								}
							}
						}
						if(mPresenter != null){
							mPresenter.hideLoading();
						}
						updateIsVisibiNullView();
						if(mSwipyRefreshLayout != null){
							mSwipyRefreshLayout.setRefreshing(false);
						}
					}
				});
			}
		}, HandlerUtil.paramsIsNull(params), strUrl,getCacheMode());
		SwipyRefreshLayout_GC();
	}

	private int mCacheMode = GetNetWorkDataImpl.NO_CACHE;//默认不使用缓存
	/**
	 * 设置缓存模式
	 * @param m
	 */
	public void setCacheMode(int m){
		this.mCacheMode = m;
	}

	public int getCacheMode(){
		if(getIsisCache()){//
			if(getIF_NONE_CACHE_REQUEST()){//IF_NONE_CACHE_REQUEST
				return GetNetWorkDataImpl.IF_NONE_CACHE_REQUEST;
			}else{//REQUEST_FAILED_READ_CACHE
				return GetNetWorkDataImpl.REQUEST_FAILED_READ_CACHE;
			}
		}
		return mCacheMode;
	}

	private int pageNo = 1;
	private int pageSize =10;
	@Override
	public void onRefresh(int index) {
//		new Handler().postDelayed(new Runnable() {
//	        @Override public void run() {
//	        	//加载刷新数据
//				if(monRefreshBack != null){
//					monRefreshBack.ononRefreshBack(pageNo,pageSize);
//				}else {
//					pageNo = 1;
//					isShuaXin = true;
//					if(monRefreshOnLoadPageNumber != null){
//						monRefreshOnLoadPageNumber.onBackPageNumberAndSize(pageNo, pageSize,mDatas);
//					}
//					isNetWorkInfo((Activity) mContext);
//				}
//				mSwipyRefreshLayout.setRefreshing(false);
//	        }
//	    }, 200);
		handler.removeMessages(1);
		//加载刷新数据
		if(monRefreshBack != null){
			monRefreshBack.ononRefreshBack(pageNo,pageSize);
		}else {
			pageNo = 1;
			isShuaXin = true;
			if(monRefreshOnLoadPageNumber != null){
				monRefreshOnLoadPageNumber.onBackPageNumberAndSize(pageNo, pageSize,mDatas);
			}
			isNetWorkInfo();
		}
		SwipyRefreshLayout_GC();
	}

	@Override
	public void onLoad(int index) {
//		new Handler().postDelayed(new Runnable() {
//	        @Override public void run() {
//	        	mSwipyRefreshLayout.setRefreshing(false);
//	        	pageNo += 1;
//	        	isShuaXin = false;
//	        	if(monRefreshOnLoadPageNumber != null){
//	    			monRefreshOnLoadPageNumber.onBackPageNumberAndSize(pageNo, pageSize,mDatas);
//	    		}
//				if(getNetWorkJiaZaiType() != 0){
//					onInitData(classOfT, null, urls, ts);
//				}else{
//					params.put(pageKey, pageNo+"");
//					params.put(pageSizeKey, pageSize+"");
//					onInitData(classOfT, params, urls, ts);
//				}
////				params.put(pageKey, pageNo+"");
////				params.put(pageSizeKey, pageSize+"");
////	    		onInitData(classOfT, params, urls, ts);
//	        }
//	    }, 200);
		handler.removeMessages(1);
		if(monRefreshBack != null){
			monRefreshBack.onOnLoadBack(++pageNo,pageSize);
			return;
		}
		pageNo += 1;
		isShuaXin = false;
		if(monRefreshOnLoadPageNumber != null){
			monRefreshOnLoadPageNumber.onBackPageNumberAndSize(pageNo, pageSize,mDatas);
		}
		if(getNetWorkJiaZaiType() != 0){
			onInitData(classOfT, null, urls, ts);
		}else{

			if(!IsNullUtils.isNullss(params,classOfT,urls,ts)){
				if(!IsNullUtils.isNulls(pageKey)){
					params.put(pageKey, pageNo+"");
				}
				if(!IsNullUtils.isNulls(pageSizeKey)){
					params.put(pageSizeKey, pageSize+"");
				}
				onInitData(classOfT, params, urls, ts);
			}else{
				mSwipyRefreshLayout.setRefreshing(false);
				L.e("onLoad","classOfT=="+classOfT+"或者params=="+params+"或者urls=="+urls+"或者ts=="+ts);
			}
		}
		SwipyRefreshLayout_GC();
	}

	private onRefreshOnLoadPageNumber<Y> monRefreshOnLoadPageNumber;
	public void setonRefreshOnLoadPageNumber(onRefreshOnLoadPageNumber<Y> monRefreshOnLoadPageNumber){
		this.monRefreshOnLoadPageNumber = monRefreshOnLoadPageNumber;
	}
	public interface onRefreshOnLoadPageNumber<Y>{
		void onBackPageNumberAndSize(int page, int size, List<Y> mData);
	}

	private onRefreshOnLoadBack monRefreshBack;
	public void setonRefreshOnLoadBack(onRefreshOnLoadBack monRefreshBack){
		this.monRefreshBack = monRefreshBack;
	}
	public interface onRefreshOnLoadBack{
		/**刷新返回**/
		void ononRefreshBack(int page, int size);
		/**加载更多返回**/
		void onOnLoadBack(int page, int size);
	}

	/**判断网络是否可用**/
	private void isNetWorkInfo() {
		if(getNetWorkJiaZaiType() != 0){
			onInitData(classOfT, null, urls, ts);
		}else{
			if(!IsNullUtils.isNullss(params,classOfT,urls,ts)){
				if(!IsNullUtils.isNulls(pageKey)){
					params.put(pageKey, pageNo+"");
				}
				if(!IsNullUtils.isNulls(pageSizeKey)){
					params.put(pageSizeKey, pageSize+"");
				}
				onInitData(classOfT, params, urls, ts);
			}else{
				mSwipyRefreshLayout.setRefreshing(false);
				L.e("onRefresh","classOfT=="+classOfT+"或者params=="+params+"或者urls=="+urls+"或者ts=="+ts);
			}
		}
	}

	/**
	 * 释放动画  超市15S自动取消网络请求
	 */
	public void SwipyRefreshLayout_GC(){
//		new Handler().postDelayed(new Runnable() {
//	        @Override public void run() {
//				isSuccessd = false;
//				cancelGetNetData();
//			}
//		}, 15000);
		handler.sendMessageDelayed(handler.obtainMessage(1),15000);
	}

	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			isSuccessd = false;
			cancelGetNetData();
		}
	};

	/***标志是否取消  超时取消功能**/
	private boolean isSuccessd = false;
	/**
	 * 取消某个请求 根据URL来取消对应的网络请求
	 */
	public void cancelGetNetData() {
		mAppOrderBuyInfoImpl.removeHavedNet(urls);
		if(isSuccessd){
			return;
		}
		if(mSwipyRefreshLayout != null){
			mSwipyRefreshLayout.setRefreshing(false);
		}
		if(!IsNullUtils.isNulls(urls))
			mAppOrderBuyInfoImpl.canceOkHttpUtilsUrl(urls);

		if(ts != null)
			ts.showError("网络请求失败，请稍候重试！");
		if(mPresenter != null){
			mPresenter.hideLoading();
		}
		updateIsVisibiNullView();
	}

	/**
	 * 全局网络上传
	 **/
	public void onUpFile(String url, Map<String, String> params,String filePath, String fileName,StringCallbacks m) {
		mAppOrderBuyInfoImpl.onUpFile(url,params,filePath,fileName,m);
	}

	/**
	 * 下载文件
	 * @param url
	 * @param destFileDir 目标文件目录
	 * @param fileNmae 保存的名字
	 * @param m 下载的回调监听文件
	 */
	public void DownLoadFile(String url,String destFileDir,String fileNmae,FileCallBack m){
		mAppOrderBuyInfoImpl.onDownLoadFile(url,destFileDir,fileNmae,m);
	}

	/**
	 * 全局网络上传  多个文件
	 * params.put("log",new File(fileName));
	 *
	 * 子类回调upProgress 得到上传进度
	 **/
	public void onUpFile(String url, Map<String, String> params, List<File> filePath,String fileName, StringCallbacks m){
		mAppOrderBuyInfoImpl.onUpFile(url,params,filePath,fileName,m);
	}

	public interface FileCallBack{
		void inProgress(float progress);
		void onError(Exception e);
		void onResponse(File response);
	}

}
