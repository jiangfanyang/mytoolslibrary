package com.ltf.mytoolslibrary.viewbase.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 自定义listview
 * 
 * @author 李堂飞
 * 
 */
public class MyListView extends ListView {

	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyListView(Context context) {
		super(context);
	}

	public MyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		if(monBackOnMeasureBackHight != null){
			monBackOnMeasureBackHight.onBackOnMeasureBackHight(expandSpec);
		}
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
	
	private onBackOnMeasureBackHight monBackOnMeasureBackHight;
	public void setonBackOnMeasureBackHight(onBackOnMeasureBackHight monBackOnMeasureBackHight){
		this.monBackOnMeasureBackHight = monBackOnMeasureBackHight;
	}
	public interface onBackOnMeasureBackHight{
		void onBackOnMeasureBackHight(int OnMeasureBackHight);
	}

}
