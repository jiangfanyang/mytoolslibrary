package com.ltf.mytoolslibrary.viewbase.alphaview;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import java.util.List;

/**
 * @author 李堂飞 20160820
 * 注解:仿微信主框架滑动效果
 * 使用时务必让实现的fragment继承LazyFragment 然后使用以下语句 懒加载
 * // 标志fragment是否初始化完成
private boolean isPrepared;
private View view;
 @Override
 public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
 if(view == null){
 view = inflater.inflate(R.layout.fragment_one , container , false);
 ButterKnife.inject(this, view);
 KLog.e("TAG" , "oneFragment--onCreateView");
 isPrepared = true;
 lazyLoad();
 }
 return view;
 }


 @Override
 protected void lazyLoad() {
 if(!isPrepared || !isVisible) {
 return;
 }
 KLog.e("TAG" , "oneFragment--lazyLoad");
 }
 */
public class Slideviewfragmentlib {

    public Slideviewfragmentlib(FragmentManager supportFragmentManager, final ViewPager mMainViewPager, AlphaIndicator alphaIndicators, List<Fragment> fragments, boolean isScales) {
        mMainViewPager.setAdapter(new FragmentAdapter(supportFragmentManager, fragments));
        alphaIndicators.setViewPager(mMainViewPager);
        alphaIndicators.setIsScale(isScales);
        alphaIndicators.setmFragmentLists(fragments);
    }

}
