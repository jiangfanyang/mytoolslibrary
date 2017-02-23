package com.ltf.mytoolslibrary.viewbase.viewPagerGuidanceActivityMyLib;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.RelativeLayout;

import com.ltf.mytoolslibrary.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @项目:viewPagerGuidanceActivityMyLib
 * @类名称:ViewPagerContoler
 * @创建人:李堂飞
 * @版本:V 1.0
 * @创建时间:2014-8-5 下午9:00:50
 * @类描述:viewPagerGuidanceActivityMyLib下的ViewPagerContoler.java
 * @备注:该类是抽象为一个架包类  适用于一切Viewpager引导界面的美化设置 省去大量的代码复用 提供背景设置 布局设置以及参数设置 指示器设置以及适合的样式
 */
public class ViewPagerContoler {

	private Context mContext;
	private ViewPager mViewPager;

	public ViewPager getmViewPager() {
		return mViewPager;
	}

	private SpringIndicator mIndicatorGroup;
	private List<View> mViews;
	private ViewPagerAdapter mPagerAdapter;
	/**变量(viewpager_bg)描述: TODO**/
	private RelativeLayout viewpager_bg;

	public ViewPagerContoler(Context context) {
		super();
		this.mContext = context;

	}

	/**方法名: init()
	 * 备注: TODO
	 * @param views
	 * 返回类型: void
	 */
	public void init(List<View> views) {
		mViews = views;
		initViewPager();
		initIndicators();
	}

	/**viewPager的背景设置**/
	public void setViewpager_bg(int viewpager_bg) {
		this.viewpager_bg = (RelativeLayout) ((Activity) mContext).findViewById(R.id.viewpager_bg);
		this.viewpager_bg.setBackgroundResource(viewpager_bg);
	}

	/**设置指示器 **/
	private void initIndicators() {
		mIndicatorGroup = (SpringIndicator) ((Activity) mContext).findViewById(R.id.indicator);
		mIndicatorGroup.setViewPager(mViewPager);
	}

	/**隐藏指示器 **/
	public void initIndicators_yincang() {
		mIndicatorGroup = (SpringIndicator) ((Activity) mContext).findViewById(R.id.indicator);
		mIndicatorGroup.setVisibility(View.GONE);
	}

	public List<String> getTitles(){
		String[] str = new String[mViews.size()];
		for (int i = 0; i < mViews.size(); i++) {
			str[i] = "";
		}
		return newArrayList(str);
	}

	public <E> ArrayList<E> newArrayList(E[] str) {
		ArrayList<E> list = new ArrayList<>();
		list.clear();
		for (int i = 0; i < str.length; i++) {
			list.add(str[i]);
		}
		return list;
	}

	/** 设置ViewPager **/
	@SuppressWarnings("deprecation")
	private void initViewPager() {
		mViewPager = (ViewPager) ((Activity) mContext).findViewById(R.id.viewPager_lib);
		mPagerAdapter = new ViewPagerAdapter(mViews,getTitles());
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setOnPageChangeListener(pageChangeListener);
		mViewPager.addOnPageChangeListener(pageChangeListener);
	}

	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int arg0) {
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			if(pagerScrolledChangeListener != null){
				pagerScrolledChangeListener.OnViewPagerComplete(arg0, arg1, arg2);
			}
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}
	};

	private OnViewPagerScrolledChangeListener  pagerScrolledChangeListener;

	public void setOnViewPagerScrolledChangeListener(OnViewPagerScrolledChangeListener  pagerScrolledChangeListener){
		this.pagerScrolledChangeListener = pagerScrolledChangeListener;
	}
	/**
	 * 滑动停止时处于最后一页的值分别是(如果是三页)  2   0    0 分别对应arg0 arg1 arg2
	 * arg0  处于最后一页时在向左滑动则arg0等于当前页的值(onPageSelected中的arg0)
	 * arg2  在屏幕上的滑动距离 
	 */
	public interface OnViewPagerScrolledChangeListener{
		void OnViewPagerComplete(int arg0, float arg1, int arg2);
	}
}
