package com.ltf.mytoolslibrary.viewbase.base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ltf.mytoolslibrary.R;
import com.ltf.mytoolslibrary.viewbase.app.ApplicationBase;
import com.ltf.mytoolslibrary.viewbase.base.quicksearch.SearchAdapter;
import com.ltf.mytoolslibrary.viewbase.base.quicksearch.dataBean;
import com.ltf.mytoolslibrary.viewbase.constent.constent;
import com.ltf.mytoolslibrary.viewbase.isnull.IsNullUtils;
import com.ltf.mytoolslibrary.viewbase.permission.CheckPermissionUtils;
import com.ltf.mytoolslibrary.viewbase.swipebacklayout.app.SwipeBackActivity;
import com.ltf.mytoolslibrary.viewbase.takephoto.TakephotoUtils;
import com.ltf.mytoolslibrary.viewbase.utils.AppManager;
import com.ltf.mytoolslibrary.viewbase.utils.NetworkUtils;
import com.ltf.mytoolslibrary.viewbase.utils.ScreenUtils;
import com.ltf.mytoolslibrary.viewbase.utils.show.L;
import com.ltf.mytoolslibrary.viewbase.utils.show.T;
import com.ltf.mytoolslibrary.viewbase.video.TakeVideoUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.List;

/**
 * @author litangfei
 * 所有activity都可以继承本父类 TitleActivityBase是自己封装的父类 TitleActivityBase则是所有子类继承后  调用
 * 其中的方法则可以让Activity页面展现不一样的标题
 * 如果要使用沉浸式状态栏务必在Activity的布局文件最外层控件加上一个属性：加入android:fitsSystemWindows="true"
 * AS 可以使用插件ButterKnife来进行反射 避免找id
 */
public abstract class ActivityTitleBase extends SwipeBackActivity{

	private View mTitleLocationLay,mTitleView,mTitleBackRightBtn,mTitleMapSearchLin;
	private RelativeLayout mSearch_LinearLayout;
	private ImageView searchImageView,mSearchDeleteImageView,mTitleRightImgIv,mTitleLeftImgIv,mTitleRightMapBtn,mTitleRightSearchBtn;
	private TextView mTitleCentreTextTv,mTitleLocationcityNameTv,mTitleBackBtn,mTitleCentreTextTvPic;
	private AutoCompleteTextView mSearchContentEditText;
	private Button mTitleSearchBtn;
	private AnimationDrawable animationDrawable;
	public View NullView;
	/**
	 * 方法名: onTitleBackClick() 备注: 返回按钮被点击了 返回类型: void
	 */
	public void onTitleBackClick(View v){
		
	};
	public void onTitleBackClick(){
		
	};
	/**
	 * 方法名: onTitleRightSerchBtnClick() 备注: 标题栏右边的搜索字样 按钮被点击了 返回类型: void
	 */
	public void onTitleRightSerchBtnClick(){};
	/**
	 * 方法名: onTitleRightSerchBtnClick() 备注: 标题栏右边的搜索字样 按钮被点击了 返回类型: void
	 */
	public void onTitleRightSerchBtnClick(Button v){};
	/**
	 * 方法名: onTitleRightImgClick() 备注: 标题栏右边的图片 按钮被点击了 返回类型: void
	 */
	public void onTitleRightImgClick(View v){};
	/**
	 * 方法名: onTitleRightTwoImgClick() 备注: 标题栏右边的两张图片 其中一张的按钮被点击了 返回类型: void
	 * 参数 :img 0 代表左边张图片被点击 1代表右边张图片被点击
	 */
	public void onTitleRightTwoImgClick(int img){};
	/**
	 * 方法名: onTitleCenterSerchClick() 备注: 标题栏左边的的图片 按钮被点击了 返回类型: void
	 */
	public void onTitleCenterSerchClick(){};
	/**
	 * 方法名: onTitleLeftImgClick() 备注: 标题栏左边的的图片 按钮被点击了 返回类型: void
	 */
	public void onTitleLeftImgClick(){};
	/**
	 * 方法名: onTitleCentreTextTvClick() 备注: 标题栏中间的字被点击了 返回类型: void
	 */
	public void onTitleCentreTextTvClick(){};
	/**
	 * 继承TitleFragmentBase  初始化title将要显示什么出来  什么可以点击
	 */
	protected abstract void initTitle();
	/**
	 * 监听回调dialog消失
	 * @param dialog
	 */
	public void onDismissd(DialogInterface dialog){};
	/**
	 * 方法名: onBackClick() 备注: 标题栏右边的定位城市字样 按钮被点击了 返回类型: void
	 */
	public void onTitleLocationcityNameClick(){};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppManager.getAppManager().addActivity(this);
		initTitle();
	}

	/**
	 * 判断当前SDK是否大于19  主要用于判断系统版本是否大于4.4以及以上
	 */
	public boolean IS_SDK_VERSION_FIT(){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			return true;
        }else{
			return false;
		}
	}

	public void setNullView(){
		NullView = this.findViewById(R.id.null_layout);
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
	
	private TextView mtipnet;
	private View mTitleBackBtnd;
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
			if (!isFinishing()) //判断activity是否还存在
			onHaveNetwork();
		} else {
			onNetworkError();
		}
	}

	@Override
	public void initTitleBarBg(int flage,int coror) {
		setTitleBarBg(flage,coror);
	}

	/**
	 * 设置标题栏背景
	 * flage=0代表 图片
	 * flage=1代表颜色
	 * **/
	public void setTitleBarBg(int flage,int coror){
		mTitleView = this.findViewById(R.id.title_view);
		if(mTitleView != null){
			if(flage == 0){
				mTitleView.setBackgroundResource(coror);
			}else if(flage == 1){
				mTitleView.setBackgroundColor(coror);
			}
		}
	}

	public void setTitleBarVisibility(){
		mTitleView = this.findViewById(R.id.title_view);
		mTitleView.setVisibility(View.VISIBLE);
	}
	
	public void setTitleBarNoVisibility(){
		mTitleView = this.findViewById(R.id.title_view);
		mTitleView.setVisibility(View.GONE);
	}
	
	/** 设置标题栏左边的定位城市 **/
	public void setUpLeftLocationCityName(String city) {
		mTitleLocationLay = this.findViewById(R.id.common_title_bar_location_lay);
		mTitleLocationcityNameTv = (TextView) this.findViewById(R.id.common_title_bar_city_name);
		mTitleLocationcityNameTv.setText(city);
		mTitleLocationLay.setVisibility(View.VISIBLE);
		mTitleLocationcityNameTv.setOnClickListener(onClickListener);
	} 
	
	/** 设置标题栏左边的返回键 **/
	public void setUpTitleBack() {
		mTitleBackBtn = (TextView) this.findViewById(R.id.common_title_bar_back_img);
		mTitleBackBtn.setVisibility(View.VISIBLE);
		mTitleBackBtn.setOnClickListener(onClickListener);
	}
	
	/** 设置标题栏左边的返回键 **/
	public void setUpTitleBack(String text,int drawable) {
		mTitleBackBtn = (TextView) this.findViewById(R.id.common_title_bar_back_img);
		mTitleBackBtn.setVisibility(View.VISIBLE);
		mTitleBackBtn.setText(text);
		if(drawable != 0){
			Drawable nav_up=getResources().getDrawable(drawable);  
			nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());  
			mTitleBackBtn.setCompoundDrawables(nav_up, null, null, null);  
		}else if(drawable == 0){
			mTitleBackBtn.setCompoundDrawables(null, null, null, null);
		}

		mTitleBackBtn.setOnClickListener(onClickListener);
	}
	
	/**
	 * 方法名: onTitleSearchSelectItem() 备注: 标题栏搜索下拉框中某项被选择回调 
	 */
	public void onTitleSearchSelectItem(AdapterView<?> parent, View view, int position, long id){};
	/**
	 * 方法名: onTitleSearchSelectItem() 备注: 标题栏搜索下拉框中某项被选择回调
	 */
	public void onTitleSearchSelectItem(View arg1, int position, dataBean data){};
	/**
	 * 方法名: onTitleSerchBtnClick() 备注: 搜索栏搜索按钮被点击了 返回类型: ImageView
	 */
	public void onTitleSerchBtnClick(ImageView v,CharSequence search){};
	private SearchAdapter<String> adapter = null;//
	/** 设置标题栏中间的搜索框
	 * boolean isString:为true说明数组String[] objects是纯String类型的数组  每项内容不含逗号分隔
	 * 为false表明回调onTitleSearchSelectItem(View arg1, int position, Object data)
	 * 为true表明回调onTitleSearchSelectItem(AdapterView<?> parent, View view, int position, long id)
	 * **/
	public void setUpTitleCenterSerch1(String[] objects) {
		mSearch_LinearLayout = (RelativeLayout) this .findViewById(R.id.search_LinearLayout);
		if(objects == null){
			mSearch_LinearLayout.setVisibility(View.GONE);
		}else{
			setUpTitleSearchBtn();
			mTitleView = this.findViewById(R.id.title_view);
			mSearchContentEditText = (AutoCompleteTextView) this .findViewById(R.id.searchContentEditText);
			mSearchDeleteImageView = (ImageView) this .findViewById(R.id.searchDeleteImageView);
			mSearchContentEditText.setThreshold(1);
			mSearchContentEditText.setDropDownVerticalOffset(20);
			mSearchContentEditText.setDropDownWidth(ScreenUtils.getScreenW(this));
			mSearch_LinearLayout.setVisibility(View.VISIBLE);
			adapter = new SearchAdapter<String>(this,R.layout.item_autocompletetextview_textview, objects,SearchAdapter.ALL);//速度优先
			mSearchContentEditText.setAdapter(adapter);//
			mSearchContentEditText.addTextChangedListener(onWatcherListener);
			mSearchDeleteImageView.setOnClickListener(onClickListener);
			mSearchContentEditText.setOnItemClickListener(onItemClickListeners);
		}
	}

	/**设置搜索框提示文字**/
	public void setSearchHintText(String msg){
		mSearchContentEditText = (AutoCompleteTextView) this .findViewById(R.id.searchContentEditText);
		mSearchContentEditText.setHint(msg);
	}

	/** 设置标题栏中间的搜索框
	 * boolean isString:为true说明数组String[] objects是纯String类型的数组  每项内容不含逗号分隔
	 * 为false表明回调onTitleSearchSelectItem(View arg1, int position, Object data)
	 * 为true表明回调onTitleSearchSelectItem(AdapterView<?> parent, View view, int position, long id)
	 * **/
	public void setUpTitleCenterSerch(List<dataBean> objects) {
		mSearch_LinearLayout = (RelativeLayout) this .findViewById(R.id.search_LinearLayout);
		if(objects == null){
			mSearch_LinearLayout.setVisibility(View.GONE);
		}else{
			setUpTitleSearchBtn();
			mTitleView = this.findViewById(R.id.title_view);
			mSearchContentEditText = (AutoCompleteTextView) this .findViewById(R.id.searchContentEditText);
			mSearchDeleteImageView = (ImageView) this .findViewById(R.id.searchDeleteImageView);
			mSearchContentEditText.setThreshold(1);
			mSearchContentEditText.setDropDownVerticalOffset(20);
			mSearchContentEditText.setDropDownWidth(ScreenUtils.getScreenW(this));
			mSearch_LinearLayout.setVisibility(View.VISIBLE);
			adapter = new SearchAdapter<String>(this,R.layout.item_autocompletetextview_textview, objects,SearchAdapter.ALL);//速度优先
			mSearchContentEditText.setAdapter(adapter);//
			mSearchContentEditText.addTextChangedListener(onWatcherListener);
			mSearchDeleteImageView.setOnClickListener(onClickListener);
//			mSearchContentEditText.setOnItemClickListener(onItemClickListeners);
			adapter.setOnCustomItemClickListener(mOnCustomItemClickListener);
		}
	}

	/**设置搜索框左边搜索图片和搜索框的背景色**/
	public void setUpTitleCenterSearchBg(int resid,int resid1){
		mSearchContentEditText = (AutoCompleteTextView) this .findViewById(R.id.searchContentEditText);
		searchImageView =  (ImageView) this.findViewById(R.id.searchImageView);
		mSearchContentEditText.setBackgroundResource(resid);
		searchImageView.setImageResource(resid1);
		mSearchContentEditText.setPadding(32, 0, 0, 0);
	}

	/**
	 * 设置标题栏中间的收入框显示内容
	 * @param msg
	 */
	public void setInputContentToSearchContentEditText(String msg){
		if(mSearchContentEditText != null){
			mSearchContentEditText.setText(msg);
			mSearchContentEditText.invalidate();
		}
	}

	/**设置搜索框左边搜索图片点击事件**/
	public void setUpTitleSearchBtn() {
		searchImageView = (ImageView) this.findViewById(R.id.searchImageView);
		searchImageView.setVisibility(View.VISIBLE);
		searchImageView.setOnClickListener(onClickListener);
	}

	private SearchAdapter.OnCustomItemClickListener mOnCustomItemClickListener = new SearchAdapter.OnCustomItemClickListener() {

		@Override
		public void onCustomItemClick(View arg1, int position, dataBean data) {
			onTitleSearchSelectItem(arg1,position,data);
		}
	};

	private OnItemClickListener onItemClickListeners = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			onTitleSearchSelectItem(arg0,  arg1,  arg2,  arg3);
		}
	};
	
	/** 设置标题栏左边的返回键右边的图片 **/
	public void setUpTitleBackRight() {
		mTitleBackRightBtn = this .findViewById(R.id.common_title_bar_back_right_view);
		mTitleBackRightBtn.setVisibility(View.VISIBLE);
	} 
	
	/** 设置标题栏左边 显示图片 **/
	public void setUpTitleLeftImg(int drawable) {
		mTitleLeftImgIv = (ImageView) this .findViewById(R.id.common_title_bar_left_img1);
		mTitleLeftImgIv.setImageResource(drawable);
		mTitleLeftImgIv.setVisibility(View.VISIBLE);
		mTitleLeftImgIv.setOnClickListener(onClickListener);
	} 
	
	/** 设置标题栏中间的字 **/
	public void setUpTitleCentreText(String titleCentreText,int color) {
		mTitleCentreTextTv = (TextView) this .findViewById(R.id.common_title_bar_title_tv);
		mTitleCentreTextTv.setText(titleCentreText);
		mTitleCentreTextTv.setVisibility(View.VISIBLE);
		mTitleCentreTextTv.setTextColor(ContextCompat.getColor(this,color));
		mTitleCentreTextTv.setOnClickListener(onClickListener);
	}

	/** 设置标题栏中间的字 **/
	public void setUpTitleCentreText(String titleCentreText) {
		mTitleCentreTextTv = (TextView) this .findViewById(R.id.common_title_bar_title_tv);
		mTitleCentreTextTv.setText(titleCentreText);
		mTitleCentreTextTv.setVisibility(View.VISIBLE);
		mTitleCentreTextTv.setOnClickListener(onClickListener);
	}

	/** 设置标题栏中间的字  带图片**/
	public void setUpTitleCentreText_pic(String titleCentreText,int pic) {
		mTitleCentreTextTvPic = (TextView) this .findViewById(R.id.common_title_bar_title_tv_pic);
		mTitleCentreTextTvPic.setText(titleCentreText);
		mTitleCentreTextTvPic.setVisibility(View.VISIBLE);
		
		Drawable nav_up=getResources().getDrawable(pic);  
		nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());  
		mTitleCentreTextTvPic.setCompoundDrawables(nav_up, null, null, null);  
		mTitleCentreTextTvPic.setOnClickListener(onClickListener);
	} 
	
	/** 设置标题栏右边 显示图片 **/
	public void setUpTitleRightImg(int drawable) {
		mTitleRightImgIv = (ImageView) this .findViewById(R.id.common_title_bar_right_img);
		if(drawable == 0){
			mTitleRightImgIv.setVisibility(View.GONE);
		}else{
			mTitleRightImgIv.setImageResource(drawable);
			mTitleRightImgIv.setVisibility(View.VISIBLE);
			mTitleRightImgIv.setOnClickListener(onClickListener);
		}
	}
	
	/** 设置标题栏最右边 显示字样 **/
	public void setUpTitleRightSearchBtn(String search) {
		mTitleSearchBtn = (Button) this .findViewById(R.id.searchButton);
		if(TextUtils.isEmpty(search)){
			mTitleSearchBtn.setVisibility(View.GONE);
		}else{
			mTitleSearchBtn.setVisibility(View.VISIBLE);
			mTitleSearchBtn.setText(search);
			mTitleSearchBtn.setOnClickListener(onClickListener);
		}
	}

	/** 设置标题栏最右边 显示字样 **/
	public void setUpTitleRightSearchBtn(String search,int color) {
		mTitleSearchBtn = (Button) this .findViewById(R.id.searchButton);
		if(TextUtils.isEmpty(search)){
			mTitleSearchBtn.setVisibility(View.GONE);
		}else{
			mTitleSearchBtn.setVisibility(View.VISIBLE);
			mTitleSearchBtn.setText(search);
			mTitleSearchBtn.setOnClickListener(onClickListener);
		}
		if(!IsNullUtils.isNullss(color+"") && color != 0){
			mTitleSearchBtn.setTextColor(ContextCompat.getColor(this,color));
		}
	}

	/** 设置标题栏最右边 是否显示字样 **/
	public void setUpTitleRightSearchBtn(String search,Boolean isVisibility) {
		mTitleSearchBtn = (Button) this .findViewById(R.id.searchButton);
		if(isVisibility){
			mTitleSearchBtn.setVisibility(View.VISIBLE);
		}else{
			mTitleSearchBtn.setVisibility(View.GONE);
		}
		mTitleSearchBtn.setText(search);
		mTitleSearchBtn.setOnClickListener(onClickListener);
	} 
	
	/** 设置标题栏右边两张图片 显示图片 **/
	public void setUpTitleRightTwoImg(int drawableLeft,int drawableRight) {
		mTitleRightMapBtn = (ImageView) this .findViewById(R.id.common_title_bar_right_map);
		mTitleRightSearchBtn = (ImageView) this .findViewById(R.id.common_title_bar_right_search);
		mTitleMapSearchLin = this .findViewById(R.id.common_title_bar_right_lay);
		mTitleRightMapBtn.setImageResource(drawableLeft);
		mTitleRightSearchBtn.setImageResource(drawableRight);
		mTitleMapSearchLin.setVisibility(View.VISIBLE);
		mTitleRightMapBtn.setOnClickListener(onClickListener);
		mTitleRightSearchBtn.setOnClickListener(onClickListener);
	} 

	private CharSequence search = "";
	/**
	 * 设置只能中间搜索框变化监听
	 * 此方法配合删除使用  搜索框内容变化则删除按钮显示  此时点击则删除则清空搜索 并隐藏删除按钮
	 */
	TextWatcher onWatcherListener = new TextWatcher() {
		
		/* (non-Javadoc)
		 * @see android.text.TextWatcher#onTextChanged(java.lang.CharSequence, int, int, int)
		 */
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			search = arg0;
			if(TextUtils.isEmpty(arg0)){
				mSearchDeleteImageView.setVisibility(View.GONE);
				setUpTitleRightSearchBtn("搜索", false);
			}else{
				mSearchDeleteImageView.setVisibility(View.VISIBLE);
				setUpTitleRightSearchBtn("搜索", true);
			}
			onTextChangeder(arg0, arg1, arg2, arg3,mTitleView);
		}
		
		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			beforeTextChangeder(arg0, arg1, arg2, arg3);
		}
		
		@Override
		public void afterTextChanged(Editable arg0) {
			afterTextChangeder(arg0);
		}
	};
	
	/**
	 * 此方法搜索框变化时调用 可根据自己需求选择实现
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param  v 被操作的view
	 */
	public void onTextChangeder(CharSequence arg0, int arg1, int arg2, int arg3,View v) {
		
	}
	
	/**
	 * 此方法搜索框变化时调用 可根据自己需求选择实现
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	public void beforeTextChangeder(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		
	}
	
	/**
	 * 此方法搜索框变化时调用 可根据自己需求选择实现
	 * @param arg0
	 */
	public void afterTextChangeder(Editable arg0) {
		
	}
	
	 /**
     * 当activity触屏事件时，隐藏输入法
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        hideSoftInput();
        return true;
    }

    /**
     * 隐藏输入法
     */
    public void hideSoftInput() {
        InputMethodManager imeManager = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imeManager.isActive();
        imeManager.hideSoftInputFromWindow(
                getWindow().getDecorView().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
	
	private OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(v == mTitleBackBtn){
				onTitleBackClick(v);
				onTitleBackClick();
			}else if(v == mTitleRightImgIv){
				onTitleRightImgClick(v);
			}else if(v == mTitleRightMapBtn){
				onTitleRightTwoImgClick(0);
			}else if(v == mTitleRightSearchBtn){
				onTitleRightTwoImgClick(1);
			}else if(v == mTitleLeftImgIv){
				onTitleLeftImgClick();
			}else if(v == mTitleSearchBtn){//搜索字样
				onTitleRightSerchBtnClick();
				onTitleRightSerchBtnClick(mTitleSearchBtn);
			}else if(v == mTitleCentreTextTv){
				onTitleCentreTextTvClick();
			}else if(v == mTitleCentreTextTvPic){
				onTitleCentreTextTvClick();
			}else if(v == mSearchDeleteImageView){
				mSearchContentEditText.setText("");
			}else if(v == mTitleLocationcityNameTv){
				onTitleLocationcityNameClick();
			}else if(v == mTitleBackBtnd){
				onBack();
			}else if(v == searchImageView){
				onTitleSerchBtnClick(searchImageView,search);
//				mSearchContentEditText.setText("");
			}
		}
	};

	/**
	 * Used to determine if the user accepted {@link android.Manifest.permission#SYSTEM_ALERT_WINDOW} or no.
	 * <p/>
	 * if you never passed the permission this method won't be called.
	 */
	@Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		CheckPermissionUtils.getSelectPicUpdateUtils().UserFromSettingBack(requestCode);
//		super.onActivityResult(requestCode,resultCode,data);
		TakephotoUtils.getTakephotoUtils().onActivityResult(this, requestCode,  resultCode, data);
		TakeVideoUtils.getTakeVideoUtils().onActivityResult(requestCode,  resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}

	//----------------------------------权限回调处理----------------------------------//

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
			grantResults) {
		/**
		 * 转给AndPermission分析结果。
		 *
		 * @param requestCode  请求码。
		 * @param permissions  权限数组，一个或者多个。
		 * @param grantResults 请求结果。
		 * @param listener PermissionListener 对象。
		 */
		AndPermission.onRequestPermissionsResult(requestCode, permissions, grantResults, new PermissionListener() {
			@Override
			public void onSucceed(int requestCode, List<String> grantPermissions) {
				// 有权限，直接do anything.
				CheckPermissionUtils.getSelectPicUpdateUtils().onBackPermissionResult();
			}

			@Override
			public void onFailed(int requestCode, List<String> deniedPermissions) {
				CheckPermissionUtils.getSelectPicUpdateUtils().onRequestPermissionsResultOnFailed(ActivityTitleBase.this,deniedPermissions);
			}
		});
	}

	public String PACKAGE = "package:";
	// 启动应用的设置
	public void startAppSettings(Activity cx) {
		Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
		intent.setData(Uri.parse( PACKAGE + cx.getPackageName()));
		cx.startActivity(intent);
	}

}
