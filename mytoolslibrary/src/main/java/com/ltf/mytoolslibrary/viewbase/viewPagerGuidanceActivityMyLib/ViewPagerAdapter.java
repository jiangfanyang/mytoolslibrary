package com.ltf.mytoolslibrary.viewbase.viewPagerGuidanceActivityMyLib;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {
	private List<View> views;
	private List<String> title;
	public ViewPagerAdapter(List<View> views,List<String> title) {
		super();
		this.views = views;
		this.title = title;
	}

	@Override
	public int getCount() {
		return views.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(views.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = views.get(position);
		container.addView(view);
		return view;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return title.get(position);
	}
	
}
