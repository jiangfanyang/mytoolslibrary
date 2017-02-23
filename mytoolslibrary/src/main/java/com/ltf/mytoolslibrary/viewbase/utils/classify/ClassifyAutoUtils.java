package com.ltf.mytoolslibrary.viewbase.utils.classify;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.ltf.mytoolslibrary.R;
import com.ltf.mytoolslibrary.viewbase.adapter.CommonAdapter1;
import com.ltf.mytoolslibrary.viewbase.adapter.ViewHolder;
import com.ltf.mytoolslibrary.viewbase.utils.AutoUtils;
import com.ltf.mytoolslibrary.viewbase.utils.PicassoUtil;
import com.ltf.mytoolslibrary.viewbase.utils.ScreenUtils;
import com.ltf.mytoolslibrary.viewbase.utils.show.L;
import com.ltf.mytoolslibrary.viewbase.views.MyGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 作者：${李堂飞} on 2016/10/31 0031 09:14
 * 邮箱：1195063924@qq.com
 * 注解: 分类抽成封装工具类 对外开放自动生成可向左滑动一屏一屏的布局 而且每个布局中自动生成gridview  其中每一项便为一个分类子项
 * 点击某一个子类回调 外部只需传入一个布局根控件 作为容器  但必须为Lineralaout的id 并且为水平
 * 以下是使用实例:(设置控件图片推荐使用ContextCompat.getDrawable(context,R.mipmap.indexz);)
 1.在XML中加入以下代码
 <LinearLayout
 android:id="@+id/auto"
 android:layout_width="match_parent"
 android:layout_height="wrap_content"
 android:background="@color/bg"
 android:orientation="horizontal"></LinearLayout>
 2.在代码中加入以下代码
 @Bind(R.id.auto)
 LinearLayout auto;
 List<ClassifyAutoBean> list = new ArrayList<>();
 list.clear();
 for (int i = 0;i<21;i++){
 ClassifyAutoBean b = new ClassifyAutoBean();
 b.setId(i+"");
 if(i == 0){
 b.setImg("http://i1.hexunimg.cn/2014-08-15/167580248.jpg");
 }else if(i == 1){
 b.setImg("http://i1.hexunimg.cn/2014-08-15/167580248.j");//此种设置方式 避免报错 将默认使用R.mipmap.ic_launcher
 }else if(i == 2){
 b.setImg("R.");//此种设置方式 避免报错 将默认使用R.mipmap.ic_launcher
 }else{
 b.setImg(""+R.mipmap.ic_launcher);
 }
 b.setTitle("图片"+i);
 b.setUrl("http://www.baidu.com");
 list.add(b);
 }
 ClassifyAutoUtils.getClassifyAutoUtils().StartClassifyAutoAdd(auto, this, list, new ClassifyAutoUtils.onBackAutoSingleClassify<ClassifyAutoBean>() {
 @Override
 public void onBackSingleClassify(ClassifyAutoBean item) {
 T.showShort(ActivitySelectLocationAddress.this,"当前被点击的是:"+item.getTitle());
 }
 });
 */
public class ClassifyAutoUtils {

    private static ClassifyAutoUtils mClassifyAutoUtils;
    public static ClassifyAutoUtils getClassifyAutoUtils(){
        if(mClassifyAutoUtils == null){
            mClassifyAutoUtils = new ClassifyAutoUtils();
        }
        return mClassifyAutoUtils;
    }
    private ClassifyAutoUtils(){
    }

    private LinearLayout layouts;
     /**保存展屏视图**/
     private List<View> mData = new ArrayList<>();
    /**
     * 自动根据服务器返回的分类数量 生成一屏一屏的布局 每一屏有8个分类子项 外部可向左滑动查看更多 点击每一项子分类必有回调
     * @param layout 必须为LinearLayout容器
     * @param activity 上下文
     * @param m 外部点击某子分类时回调的数据Model 传入null则不回调
     */
    public void StartClassifyAutoAdd(LinearLayout layout, Activity activity, List<ClassifyAutoBean> list, final onBackAutoSingleClassify m){
        this.layouts = layout;
        this.layouts.setGravity(Gravity.CENTER);
//        this.layouts.setBackgroundColor(ContextCompat.getColor(activity,R.color.white));
        if(list.size() == 0){
            this.layouts.setVisibility(View.GONE);
        }else{
            this.layouts.setVisibility(View.VISIBLE);
        }
        dataList.clear();
        /**每一屏的数据集合  通过Integer索引获取每一屏的数据集合**/
        final List<HashMap<Integer,List<ClassifyAutoBean>>> datas = new ArrayList<>();
        this.layouts.removeAllViews();//删除根目录下所有的孩子
//        this.layouts.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenW(activity),200));
        int num = list.size()%8;//这里是整除的时候  得到应该展现几屏  每屏8个子分类 即为分屏数量
        if(num != 0){
            num = list.size()/8+1; //如果不能整除  那么取余后+1  例如:num=9%8  余数为1  那么这时第一屏为8个余下一个因该分配给第二屏
        }else if(num == 0){
            num = list.size()/8;
        }
        mData.clear();//清空展屏视图集合
        datas.clear();//清空每一屏的数据集合 有多少展屏就有多少个List集合

        WrapContentHeightViewPager viewPager = new WrapContentHeightViewPager(activity);//得到动态设置宽高的Viewpager
        viewPager.removeAllViews();//清空第二次布局容器的所有孩子

        for (int i=0;i<num;i++){//循环生成每一屏的容器 并且将新生成的容器添加到根容器
            final MyGridView gridview = new MyGridView(activity);//得到每一屏的展现样式

            gridview.setId(i);
            gridview.setBackgroundColor(0x000000);
            gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));//设置展屏样式没有点击效果
            gridview.setNumColumns(4);//设置展屏样式为四列
//            gridview.setHorizontalSpacing(10);
//            gridview.setVerticalSpacing(10);
//            gridview.setPadding(10,10,10,10);
            List<ClassifyAutoBean> data = new ArrayList<>();//新产生一屏则新建一个数据集合
            data.clear();//防止数据混乱 添加移位问题
            for (int j=0;j<8;j++){//设置每一屏展现为8个
                int chIndex = i*8+j;//得到每一屏的展现索引  例如:第一屏第一个索引为0 那么第二屏的第一个索引为8
                if(chIndex < list.size()){//索引序号不能超过总的数据集合大小 否则有越界异常问题
                    data.add(list.get(chIndex));//满足条件的数据放入每一屏的数据集合中
                }
            }

            HashMap<Integer,List<ClassifyAutoBean>> map = new HashMap<>();//用于存放每一屏数据集合的容器
            map.put(i,data);
            datas.add(map);
            gridview.setAdapter(initAdapter(activity,datas.get(i).get(i)));//通过每一屏的数据集合设置每一屏的展现数据
            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {//每一屏的展现样式子分类的点击事件回调
                    GridView vi = (GridView) view.getParent();
                    int position = vi.getId();
                    if(m != null){
                        m.onBackSingleClassify(datas.get(position).get(position).get(i));//回调每一屏中某子项被点击时的回调 通过每一屏的索引 和每一屏的数据集合 以及点击位置换算得到点击位置的数据源
                    }
                }
            });
            mData.add(gridview);
        }

        pageAdapter adapter = new pageAdapter(mData);
        viewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        this.layouts.addView(viewPager);
        notifyDataSetChange();
    }

    private CommonAdapter1<ClassifyAutoBean> initAdapter(final Activity activity, final List<ClassifyAutoBean> s){
        CommonAdapter1<ClassifyAutoBean> adapter = new CommonAdapter1<ClassifyAutoBean>(activity,s, R.layout.classify_auto_utils_item_classify) {
            @Override
            public void convert(ViewHolder helper, ClassifyAutoBean item, int position) {
                helper.setText(R.id.classify_tv,item.getTitle());
                if(item.getWidth() == 0 && item.getHeight() == 0){
                    helper.getImgView(R.id.classify_iv).setLayoutParams(new LinearLayout.LayoutParams(AutoUtils.getDisplayWidthValue(50), AutoUtils.getDisplayHeightValue(50)));
                }else{
                    helper.getImgView(R.id.classify_iv).setLayoutParams(new LinearLayout.LayoutParams(AutoUtils.getDisplayWidthValue(item.getWidth()), AutoUtils.getDisplayHeightValue(item.getHeight())));
                }
                //L.e("ClassifyAutoUtils设置分类图片资源--->",item.getImg());
                if(!TextUtils.isEmpty(item.getImg())){
                    if(item.getImg().startsWith("http://")||item.getImg().startsWith("https://")){
                        PicassoUtil.getInstantiation().setLoad_error(R.mipmap.default_image).setLoad_image(R.mipmap.default_image).onWidgetImage(activity,item.getImg(),helper.getImgView(R.id.classify_iv));
                    }else{
                        try{
                            helper.setImageDrawable(R.id.classify_iv, ContextCompat.getDrawable(activity,Integer.parseInt(item.getImg())));
                        }catch (Exception e){//资源发生错误时设置默认
                            L.e("ClassifyAutoUtils设置分类图片资源--->","当前设置的资源路径-->"+item.getImg()+"详细报错信息-->"+e.toString());
                            helper.setImageDrawable(R.id.classify_iv, ContextCompat.getDrawable(activity,R.mipmap.default_image));
                        }
                    }
                }else{
                    L.e("ClassifyAutoUtils设置分类图片资源--->","item.getImg()="+item.getImg());
                    helper.setImageDrawable(R.id.classify_iv, ContextCompat.getDrawable(activity,R.mipmap.default_image));
                }

                helper.getConvertView().setLayoutParams(new AbsListView.LayoutParams(ScreenUtils.getScreenW(activity)/4, AutoUtils.getDisplayHeightValue(100)));
            }
        };
        dataList.add(s);
        return adapter;
    }

    public void notifyDataSetChange(){
        for (int i=0;i<this.layouts.getChildCount();i++){
            ((ViewPager)this.layouts.getChildAt(i)).getAdapter().notifyDataSetChanged();
        }
        for (int i=0;i<mData.size();i++){
            ((CommonAdapter1<ClassifyAutoBean>)((GridView)mData.get(i)).getAdapter()).notifyDataSetChanged();
        }
    }

    private List<List<ClassifyAutoBean>> dataList = new ArrayList<>();
    private int seize = 0;
    /**更新图片路径 根据名字跟新**/
    public void updateImgPath(List<ClassifyAutoBean> data){
        seize = 0;
        for (int i=0;i<dataList.size();i++){
            seize += dataList.get(i).size();
        }
        if(data.size() == seize){
            int num = data.size()%8;//这里是整除的时候  得到应该展现几屏  每屏8个子分类 即为分屏数量
            if(num != 0){
                num = data.size()/8+1; //如果不能整除  那么取余后+1  例如:num=9%8  余数为1  那么这时第一屏为8个余下一个因该分配给第二屏
            }else if(num == 0){
                num = data.size()/8;
            }
            for (int j=0;j<num;j++){
                for (int i=0;i<dataList.get(j).size();i++){
                    for (int a=0;a<data.size();a++){
                        if(dataList.get(j).get(i).getTitle().equals(""+data.get(a).getTitle())){
                            dataList.get(j).get(i).setImg(data.get(a).getImg());
                            break;
                        }
                    }
                }
            }
            for (int i=0;i<this.layouts.getChildCount();i++){
                ((ViewPager)this.layouts.getChildAt(i)).getAdapter().notifyDataSetChanged();
            }
        }
    }

    public class pageAdapter extends PagerAdapter {

        private List<View> list = new ArrayList<>();
        public pageAdapter(List<View> lists){
            this.list = lists;
        }

        /****
         * 如果item的位置如果没有发生变化，则返回POSITION_UNCHANGED。如果返回了POSITION_NONE，表示该位置的item已经不存在了。
         * 默认的实现是假设item的位置永远不会发生变化，而返回POSITION_UNCHANGED
         * 所以我们可以尝试着修改适配器的写法，覆盖getItemPosition()方法，当调用notifyDataSetChanged时，
         * 让getItemPosition方法人为的返回POSITION_NONE，从而达到强迫viewpager重绘所有item的目的。
         * 缺点开销太大
         * @param
         * @return
         */
//        @Override
//        public int getItemPosition(Object object) {
//            return POSITION_NONE;
//        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View gridView = list.get(position);
            ViewGroup parent = (ViewGroup) gridView.getParent();
            if (parent != null) {
                parent.removeAllViews();
            }
            container.addView(gridView);
            return gridView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            GridView gridView = (GridView) object;
            container.removeView(gridView);
        }
    }

    public interface onBackAutoSingleClassify{
        /**返回外部点击某一个子分类所携带的Model数据**/
        void onBackSingleClassify(ClassifyAutoBean item);
    }

    public void onDestroy(){
        dataList.clear();
        dataList = null;
        this.layouts.removeAllViews();
        this.layouts = null;
        mClassifyAutoUtils = null;
    }

}
