package com.ltf.mytoolslibrary.viewbase.alphaview;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：${李堂飞} on 2016/11/26 0026 15:50
 * 邮箱：1195063924@qq.com
 * 注解: 可以渐变的View
 */
public class AlphaIndicator extends LinearLayout {

    private ViewPager viewPager;
    private List<AlphaView> alphaViews = new ArrayList<>();
    /**
     * 子View的数量
     */
    private int childCount;
    /**
     * 当前的条目索引
     */
    private int currentItem = 0;

    public AlphaIndicator(Context context) {
        this(context, null);
    }

    public AlphaIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AlphaIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        init();
    }

    private void init() {
        if (viewPager == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        childCount = getChildCount();
        if (viewPager.getAdapter().getCount() != childCount) {
            throw new IllegalArgumentException("LinearLayout的子View数量必须和ViewPager条目数量一致");
        }
        for (int i = 0; i < childCount; i++) {
            if (getChildAt(i) instanceof AlphaView) {
                AlphaView alphaView = (AlphaView) getChildAt(i);
                alphaViews.add(alphaView);
                //设置点击监听
                alphaView.setOnClickListener(new MyOnClickListener(i));
            } else {
                throw new IllegalArgumentException("AlphaIndicator的子View必须是AlphaView");
            }
        }
        //对ViewPager添加监听
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        alphaViews.get(currentItem).setIconAlpha(1.0f);
    }

    private class MyOnPageChangeListener extends ViewPager.SimpleOnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            changAlpha(position, positionOffset);
        }

        @Override
        public void onPageSelected(int position) {
            changAlpha(position);
        }

    }

    /**
     * 一开始运行、滑动和点击tab结束后设置tab的透明度，fragment的透明度和大小
     */
    public void changAlpha(int postion) {
        //点击前先重置所有按钮的状态
        resetState();
        alphaViews.get(postion).setIconAlpha(1.0f);
        //不能使用平滑滚动，否者颜色改变会乱
        viewPager.setCurrentItem(postion, false);
        //点击是保存当前按钮索引
        currentItem = postion;
        for (int i = 0; i < alphaViews.size(); i++) {
            if (i == currentItem) {
                if (null != mFragmentLists.get(i).getView()) {
                    mFragmentLists.get(i).getView().setAlpha(1.0f);
                    mFragmentLists.get(i).getView().setScaleX(1.0f);
                    mFragmentLists.get(i).getView().setScaleY(1.0f);
                }
            } else {
                if (null != mFragmentLists.get(i).getView()) {
                    mFragmentLists.get(i).getView().setAlpha(0.0f);
                    mFragmentLists.get(i).getView().setScaleX(0.0f);
                    mFragmentLists.get(i).getView().setScaleY(0.0f);
                }
            }
        }
    }

    private boolean isScale = false;
    private List<Fragment> mFragmentLists = new ArrayList<>();
    public void setIsScale(boolean isScales){
        this.isScale = isScales;
    }

    public void setmFragmentLists(List<Fragment> mFragmentLists) {
        this.mFragmentLists = mFragmentLists;
    }

    /**
     * 根据滑动设置透明度
     */
    private void changAlpha(int pos, float posOffset) {
        int nextIndex = pos + 1;
        //滑动时的透明度动画
        if (posOffset > 0) {
            alphaViews.get(pos).setIconAlpha(1 - posOffset);
            alphaViews.get(nextIndex).setIconAlpha(posOffset);
        }
        if (posOffset > 0 && mFragmentLists.get(nextIndex).getView() != null && mFragmentLists.get(pos).getView() != null) {
            if(this.isScale){
                // 设置fragment的颜色渐变效果
                this.mFragmentLists.get(nextIndex).getView().setAlpha(posOffset);
                this.mFragmentLists.get(pos).getView().setAlpha(1 - posOffset);
                // 设置fragment滑动视图由大到小，由小到大的效果
                this.mFragmentLists.get(nextIndex).getView()
                        .setScaleX(0.5F + posOffset / 2);
                this.mFragmentLists.get(nextIndex).getView()
                        .setScaleY(0.5F + posOffset / 2);
                this.mFragmentLists.get(pos).getView().setScaleX(1 - (posOffset / 2));
                this.mFragmentLists.get(pos).getView().setScaleY(1 - (posOffset / 2));
            }else{
                // 设置fragment的颜色渐变效果
                this.mFragmentLists.get(nextIndex).getView().setAlpha(posOffset);
                mFragmentLists.get(pos).getView().setAlpha(1 - posOffset);
                //不进行缩放
                this.mFragmentLists.get(nextIndex).getView()
                        .setScaleX(1);
                this.mFragmentLists.get(nextIndex).getView()
                        .setScaleY(1);
                this.mFragmentLists.get(pos).getView().setScaleX(1);
                this.mFragmentLists.get(pos).getView().setScaleY(1);
            }

        }
    }

    private class MyOnClickListener implements OnClickListener {

        private int currentIndex;

        public MyOnClickListener(int i) {
            this.currentIndex = i;
        }

        @Override
        public void onClick(View v) {
            changAlpha(currentIndex);
        }
    }

    /**
     * 重置所有按钮的状态
     */
    private void resetState() {
        for (int i = 0; i < childCount; i++) {
            alphaViews.get(i).setIconAlpha(0);
        }
    }

    private static final String STATE_INSTANCE = "instance_state";
    private static final String STATE_ITEM = "state_item";

    /**
     * @return 当View被销毁的时候，保存数据
     */
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(STATE_INSTANCE, super.onSaveInstanceState());
        bundle.putInt(STATE_ITEM, currentItem);
        return bundle;
    }

    /**
     * @param state 用于恢复数据使用
     */
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            currentItem = bundle.getInt(STATE_ITEM);
            //重置所有按钮状态
            resetState();
            //恢复点击的条目颜色
            alphaViews.get(currentItem).setIconAlpha(1.0f);
            super.onRestoreInstanceState(bundle.getParcelable(STATE_INSTANCE));
        } else {
            super.onRestoreInstanceState(state);
        }
    }
}
