package com.ltf.mytoolslibrary.viewbase.utils.autoImage;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ltf.mytoolslibrary.R;
import com.ltf.mytoolslibrary.viewbase.utils.AutoUtils;
import com.ltf.mytoolslibrary.viewbase.utils.PicassoUtil;
import com.ltf.mytoolslibrary.viewbase.utils.ScreenUtils;
import com.ltf.mytoolslibrary.viewbase.utils.show.L;

import java.util.List;


/**
 * 作者：${李堂飞} on 2016/11/3 0003 14:27
 * 邮箱：1195063924@qq.com
 * 注解: 活动列表图片 自适应服务器 点击图片跳转WAP 纵向
 */
public class CampaignAutoImageUtils {

    private static CampaignAutoImageUtils mCampaignAutoImageUtils;
    public static CampaignAutoImageUtils getCampaignAutoImageUtils(){
        if(mCampaignAutoImageUtils == null){
            mCampaignAutoImageUtils = new CampaignAutoImageUtils();
        }
        return mCampaignAutoImageUtils;
    }
    private CampaignAutoImageUtils(){

    }

    /**
     * 活动图片列表展示入口
     * @param activity 上下文
     * @param group 容器
     * @param mCampaignAutoImage 数据集合
     * @param m 回调事件
     */
    public void startCampaignAutoImage(Activity activity, LinearLayout group, final List<CampaignAutoImage> mCampaignAutoImage, final onBackAutoSingleCampaignAutoImage m){
        if(mCampaignAutoImage.size() == 0){
            group.setVisibility(View.GONE);
            return;
        }else{
            group.setVisibility(View.VISIBLE);
        }
        group.removeAllViews();
        group.setBackgroundColor(0xffffffff);
        group.setGravity(Gravity.CENTER);
        for (int i=0;i<mCampaignAutoImage.size();i++){
            ImageView iv = new ImageView(activity);
            iv.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
            iv.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenW(activity), AutoUtils.getDisplayHeightValue(246)));
//            iv.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenW(activity), LinearLayout.LayoutParams.WRAP_CONTENT));
            if(!TextUtils.isEmpty(mCampaignAutoImage.get(i).getImgPath())){
                if(mCampaignAutoImage.get(i).getImgPath().startsWith("http://")||mCampaignAutoImage.get(i).getImgPath().startsWith("https://")){
                    PicassoUtil.getInstantiation().setLoad_error(R.mipmap.default_image).setLoad_image(R.mipmap.default_image).onWidgetImage(activity, mCampaignAutoImage.get(i).getImgPath(),iv);
                    iv.setVisibility(View.VISIBLE);
                }else{
                    try{
                        iv.setImageDrawable(ContextCompat.getDrawable(activity,Integer.parseInt(mCampaignAutoImage.get(i).getImgPath())));
//                        iv.setBackgroundColor(0xffffffff);
//                        iv.setVisibility(View.VISIBLE);
                    }catch (Exception e){//资源发生错误时设置默认
                        L.e("ClassifyAutoUtils设置分类图片资源--->","当前设置的资源路径-->"+mCampaignAutoImage.get(i).getImgPath()+"详细报错信息-->"+e.toString());
                        iv.setImageDrawable(ContextCompat.getDrawable(activity, R.mipmap.default_image));
//                        iv.setBackgroundColor(0xffC1C2C7);
//                        iv.setVisibility(View.GONE);
                    }
                }
            }else{
                iv.setImageDrawable(ContextCompat.getDrawable(activity,R.mipmap.default_image));
//                iv.setBackgroundColor(0xffC1C2C7);
//                iv.setVisibility(View.GONE);
            }

            if(i != 0 && mCampaignAutoImage.size() != 1){//当只有一个的时候不需要底部添加间隔 如果有多个i=0时不添加 i!=0时先添加间隔再添加图片
                View v2 = new View(activity);
                LinearLayout.MarginLayoutParams lp = new LinearLayout.MarginLayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT , AutoUtils.getDisplayHeightValue(6));
                lp.leftMargin = 0;
                lp.rightMargin = 0;
                lp.topMargin = 0;
                lp.bottomMargin = 0;
                v2.setBackgroundColor(ContextCompat.getColor(activity,R.color.bg));
                v2.setLayoutParams(lp);
                group.addView(v2);
            }
            group.addView(iv);

            iv.setId(i);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(m != null){
                        m.onBackSingleCampaignAutoImage(mCampaignAutoImage.get(view.getId()));
                    }
                }
            });
        }
    }

    public interface onBackAutoSingleCampaignAutoImage{
        /**返回外部点击某一个子分类所携带的Model数据**/
        void onBackSingleCampaignAutoImage(CampaignAutoImage item);
    }
}
