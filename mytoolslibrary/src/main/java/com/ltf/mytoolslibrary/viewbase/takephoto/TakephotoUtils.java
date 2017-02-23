package com.ltf.mytoolslibrary.viewbase.takephoto;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.ImageView;

import com.ltf.mytoolslibrary.R;
import com.ltf.mytoolslibrary.viewbase.CacheFolder.CacheFolderUtils;
import com.ltf.mytoolslibrary.viewbase.constent.constent;
import com.ltf.mytoolslibrary.viewbase.isnull.IsNullUtils;
import com.ltf.mytoolslibrary.viewbase.takephoto.bean.ImageItem;
import com.ltf.mytoolslibrary.viewbase.takephoto.loader.ImageLoader;
import com.ltf.mytoolslibrary.viewbase.takephoto.ui.ImageGridActivity;
import com.ltf.mytoolslibrary.viewbase.takephoto.ui.ImagePreviewDelActivity;
import com.ltf.mytoolslibrary.viewbase.takephoto.view.CropImageView;
import com.ltf.mytoolslibrary.viewbase.utils.AutoUtils;
import com.ltf.mytoolslibrary.viewbase.utils.PicassoUtil;
import com.ltf.mytoolslibrary.viewbase.utils.StartToUrlUtils;

import java.io.File;
import java.util.ArrayList;


/**
 * 作者：${李堂飞} on 2016/11/22 0022 15:26
 * 邮箱：1195063924@qq.com
 * 注解:拍照裁剪  圆形 和矩形裁剪
 */
public class TakephotoUtils {

    private static TakephotoUtils mTakephotoUtils;
    public static TakephotoUtils getTakephotoUtils(){
        if(mTakephotoUtils == null){
            mTakephotoUtils = new TakephotoUtils();
        }
        return mTakephotoUtils;
    }

    private TakephotoUtils(){
        if(imagePicker == null){
            imagePicker = ImagePicker.getInstance();
            imagePicker.setImageLoader(new GlideImageLoader());
        }
    }

    /**
     * 设置裁剪保存文件夹名字  格式  /ImagePicker/cropTemp/
     * 默认将缓存放入data/data/ImagePicker/cropTemp/目录下
     */
    public void setCropCacheFolder(String cropCacheFolderName){
        imagePicker.setCropCacheFolder(CacheFolderUtils.getCacheFolderUtils().setCropCacheFolderBackFile(cropCacheFolderName));
    }

    private ImagePicker imagePicker;

    public ImagePicker getImagePicker(){
        return imagePicker;
    }
    /**
     * 默认初始化  可以不用这种模式  这种事仿微信图片选择 单选模式
     * isCircular 标志是否采用圆形裁剪
     */
    public void initImagePicker(Activity activity,boolean isCircular) {
        NowModeStype = CropModeStype;
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(true);                           //允许裁剪（单选才有效）多选请改为false
        imagePicker.setMultiMode(false);                        //设置单选模式

        if(isCircular){
            setCircularCadius(activity, 120);
            imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        }else{
            imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
            imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状

            imagePicker.setFocusWidth(AutoUtils.getDisplayWidthValue(300));                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
            imagePicker.setFocusHeight(AutoUtils.getDisplayWidthValue(260));                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
            imagePicker.setOutPutX(AutoUtils.getDisplayWidthValue(300));                         //保存文件的宽度。单位像素
            imagePicker.setOutPutY(AutoUtils.getDisplayWidthValue(260));                         //保存文件的高度。单位像素
        }

        imagePicker.setSelectLimit(8);              //选中数量限制

    }

    /**裁剪模式**/
    public static final int CropModeStype = 3001;
    /**多选模式**/
    public static final int NoCropModeStype = 3002;
    /**当前选择模式  默认裁剪模式**/
    public static int NowModeStype = 3001;

    /**
     * 动态设置当前是什么模式  裁剪或者多选
     * isCrop=true说明是当前动态分配为裁剪模式
     * isCrop=false说明是当前动态分配为非裁剪模式
     */
    public void setModeStype(boolean isCrop){
        imagePicker.setCrop(isCrop);                           //允许裁剪（单选才有效）多选请改为false
        imagePicker.setMultiMode(!isCrop);                        //设置不是单选模式
    }

    /**
     * 得到当前是什么模式  裁剪或者多选
     */
    public int getModeStype(){
        return NowModeStype;
    }

    /**
     * 默认初始化  可以不用这种模式  这种事仿微信图片选择 多选模式
     */
    public void initImagePickerMore(Activity activity) {
        NowModeStype = NoCropModeStype;
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(false);                           //允许裁剪（单选才有效）多选请改为false
        imagePicker.setMultiMode(true);                        //设置不是单选模式

        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存

        if(imagePicker.getSelectLimit() == 9){
            imagePicker.setSelectLimit(8);              //选中数量限制
        }
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状

        imagePicker.setFocusWidth(AutoUtils.getDisplayWidthValue(300));                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(AutoUtils.getDisplayWidthValue(260));                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(AutoUtils.getDisplayWidthValue(300));                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(AutoUtils.getDisplayWidthValue(260));                         //保存文件的高度。单位像素
    }

    /**
     * 启动用户选择的图片预览  可删除
     * @param activity
     */
    public void startUserSelectYuLan(Activity activity, int positionClick, boolean isDelete, ArrayList<ImageItem> imageItems, onUserSelectPicBackLisnner monUserSelectPicBackLisnners){
        this.monUserSelectPicBackLisnner = monUserSelectPicBackLisnners;
        Bundle bundle = new Bundle();
        bundle.putInt(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION,positionClick);
        bundle.putBoolean("isDelete",isDelete);
        bundle.putSerializable(ImagePicker.EXTRA_IMAGE_ITEMS,imageItems);
        StartToUrlUtils.getStartToUrlUtils().startToActivity(activity,ImagePreviewDelActivity.class,bundle, constent.back_flage_yulan);
    }

    private ArrayList<ImageItem> imageItemsd = new ArrayList<>();
    /**
     * 启动用户选择的图片预览  可删除
     * @param activity
     */
    public void startUserSelectYuLan(Activity activity,int positionClick,boolean isDelete,String[] imageItems,onUserSelectPicBackLisnner monUserSelectPicBackLisnners){
        this.monUserSelectPicBackLisnner = monUserSelectPicBackLisnners;
        imageItemsd.clear();
        for (int i=0;i<imageItems.length;i++){
            ImageItem item = new ImageItem();
            item.path = imageItems[i];
            imageItemsd.add(item);
        }
        Bundle bundle = new Bundle();
        bundle.putInt(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION,positionClick);
        bundle.putBoolean("isDelete",isDelete);
        bundle.putSerializable(ImagePicker.EXTRA_IMAGE_ITEMS,imageItemsd);
        StartToUrlUtils.getStartToUrlUtils().startToActivity(activity,ImagePreviewDelActivity.class,bundle, constent.back_flage_yulan);
    }

    /**
     * 开始调用系统相机拍照或相册
     * @param activity
     * @param monUserSelectPicBackLisnners 用户选择图片返回
     */
    public void startTakePhoto(Activity activity,onUserSelectPicBackLisnner monUserSelectPicBackLisnners){
        this.monUserSelectPicBackLisnner = monUserSelectPicBackLisnners;
        Intent intent = new Intent(activity, ImageGridActivity.class);
        activity.startActivityForResult(intent, constent.back_flage_takephone);
    }

    /**
     * 图片裁剪宽高
     * @param activity
     * @param crop_widths 裁剪宽
     * @param crop_heights 裁剪高
     */
    public void crop_width_height(Activity activity,int crop_widths,int crop_heights){
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, crop_widths, activity.getResources().getDisplayMetrics());
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, crop_heights, activity.getResources().getDisplayMetrics());
        imagePicker.setFocusWidth(width);
        imagePicker.setFocusHeight(height);
    }

    /**
     * 保存图片的宽高
     */
    public void savePicwidth_height(Activity activity,int widths,int heights){
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, widths, activity.getResources().getDisplayMetrics());
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, heights, activity.getResources().getDisplayMetrics());
        imagePicker.setOutPutX(width);
        imagePicker.setOutPutY(height);
    }

    /**
     * 设置圆形裁剪半径
     * @param radius
     */
    public void setCircularCadius(Activity activity,int radius){
        imagePicker.setStyle(CropImageView.Style.CIRCLE);
        radius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, radius, activity.getResources().getDisplayMetrics());
        imagePicker.setFocusWidth(radius * 2);
        imagePicker.setFocusHeight(radius * 2);
    }

    /**
     * 是否为单选模式
     * @param is
     */
    public void isSingle_select(boolean is){
        imagePicker.setMultiMode(is);
    }

    /**
     * 设置最大选择图片数量
     * @param max
     */
    public void selectPicMax(int max){
        if(max >= 15){
            max = 15;
        }
        imagePicker.setSelectLimit(max);
    }

    /**
     * 是否显示相机
     * @param is
     */
    public void isShowXiangji(boolean is){
        imagePicker.setShowCamera(is);
    }

    /**
     * 是否需要裁剪
     * @param is
     */
    public void isCrop(boolean is){
        imagePicker.setCrop(is);
    }

    /**
     * 是否按矩形区域保存裁剪图片
     * @param is
     */
    public void isSaveRectangle(boolean is){
        imagePicker.setSaveRectangle(is);
    }
    public void onActivityResult(Activity activity,int requestCode, int resultCode, Intent data) {
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS || resultCode == ImagePicker.RESULT_CODE_BACK) {
            if (data != null && requestCode == constent.back_flage_takephone) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if(monUserSelectPicBackLisnner != null){
                    monUserSelectPicBackLisnner.onUserSelectPicBack(images);
                }
                RefreshMediaScanner(activity,images,null);
            }else if(data != null && requestCode == constent.back_flage_yulan){
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if(monUserSelectPicBackLisnner != null){
                    monUserSelectPicBackLisnner.onUserSelectPicBack(images);
                }
                RefreshMediaScanner(activity,images,null);
            }
        }
        if(getModeStype() == NoCropModeStype){
           setModeStype(false);//按当前模式分配非裁剪模式
        }else{
           setModeStype(true);
        }
    }

    /**
     * 刷新图库 data和data1二者选其一  其余值空(null)
     * @param activity
     * @param data
     * @param data1
     */
    public void RefreshMediaScanner(Activity activity, ArrayList<ImageItem> data, String[] data1){
        if(data == null || data1 == null){
            return;
        }
        if(data.size() !=0 || data1.length !=0){
            if(data != null){
                if((data.get(0).path+"").startsWith("http")){
                    return;
                }
                if(IsNullUtils.isNulls(data.get(0).path)){
                    return;
                }
            }
            if(data1 != null){
                if((data1[0]+"").startsWith("http")){
                    return;
                }
                if(IsNullUtils.isNulls(data1[0])){
                    return;
                }
            }
        }
        if(data != null && data.size() != 0){
            for (int i=0;i<data.size();i++){
                ImagePicker.galleryAddPic(activity,new File(data.get(i).path));
            }
        }else
        if(data1 != null && data1.length != 0){
            for (int i=0;i<data1.length;i++){
                ImagePicker.galleryAddPic(activity,new File(data1[i]));
            }
        }
    }

    private onUserSelectPicBackLisnner monUserSelectPicBackLisnner;
    public interface onUserSelectPicBackLisnner{
        void onUserSelectPicBack(ArrayList<ImageItem> pic);
    }

    public class GlideImageLoader implements ImageLoader {

        @Override
        public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
            if(IsNullUtils.isNulls(path)){
                path = "http://";
            }
            PicassoUtil.getInstantiation().setLoad_error(R.mipmap.default_image).setLoad_image(R.mipmap.default_image).onLocadCropWidthHeightImage(activity,path,width,height,imageView,null);
        }

        @Override
        public void clearMemoryCache() {
        }
    }
}
