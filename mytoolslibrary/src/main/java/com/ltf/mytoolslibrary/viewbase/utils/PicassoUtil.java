package com.ltf.mytoolslibrary.viewbase.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.ltf.mytoolslibrary.R;
import com.ltf.mytoolslibrary.viewbase.CacheFolder.CacheFolderUtils;
import com.ltf.mytoolslibrary.viewbase.isnull.IsNullUtils;
import com.ltf.mytoolslibrary.viewbase.utils.show.L;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 李堂飞
 * @Description 图片显示工具类
 * @date 2015年11月12日
 * 使用方法:
 * PicassoUtil.getInstantiation().setLoad_image(int load_image).setLoad_error(int load_error).onWidgetImage(Context context, String path, ImageView imageView);
 * @Copyright: All rights reserved.
 */
public class PicassoUtil {
    /**
     * 圆形头像
     */
    private int percentX = 90;
    /**
     * 设置正在加载图片的默认图
     */
    private int load_image = R.mipmap.default_image;
    /**
     * 设置加载图片失败的默认图
     */
    private int load_error = R.mipmap.default_image;

    public PicassoUtil() {

    }

    /**
     * 单例
     */
    private static PicassoUtil picassoUtil = null;

    /**
     * 获取操作单例
     *
     * @return 返回操作对象
     * @version 1.0
     * @updateInfo (此处输入修改内容, 若无修改可不写.)
     */
    public static PicassoUtil getInstantiation() {
        if (picassoUtil == null) {
            synchronized (PicassoUtil.class) {
                if (picassoUtil == null) {
                    picassoUtil = new PicassoUtil();
                }
            }
        }
        return picassoUtil;
    }

    /**
     * 设置头像的直径值
     *
     * @param percentX 直径值
     * @return picassoUtil 操作实例
     */
    public PicassoUtil setPercentX(int percentX) {
        if (percentX > 0) {
            this.percentX = percentX;
        }

        return picassoUtil;
    }

    /**
     * 设置正在加载的默认图片
     * <p/>
     *
     * @param load_image 图片的资源id
     * @return picassoUtil 操作实例
     */
    public PicassoUtil setLoad_image(int load_image) {
        if (load_image > 0) {
            this.load_image = load_image;
        }
        return picassoUtil;
    }

    /**
     * 设置加载失败的图片
     *
     * @param load_error 图片的资源id
     * @return picassoUtil 操作实例
     */
    public PicassoUtil setLoad_error(int load_error) {
        if (load_error > 0) {
            this.load_error = load_error;
        }

        return picassoUtil;
    }

    /**
     * 设置图片圆形头像 使用于单个头像 不使用缓存 本地
     * 不使用缓存
     *
     * @param path      路径
     * @param imageView 显示控件
     * @version 1.0
     * @updateInfo (此处输入修改内容, 若无修改可不写.)
     */
    public void onRoundnessImage(Context context, String path, ImageView imageView) {
        if (!TextUtils.isEmpty(path) && null != imageView) {
            if(AnyIdCardCheckUtils.getInstance(context).isUrl(path) && !(path.startsWith("http://") || path.startsWith("https://"))){
                Picasso.with(context).load(new File(path)).transform(new RoundnessFormation()).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                        .placeholder(load_image).error(load_error).into(imageView);
            }else{
                path = getUrlToUtf(path);
                Picasso.with(context).load(path).transform(new RoundnessFormation()).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).placeholder(load_image)
                        .error(load_error).into(imageView);
            }
        }
    }

    /**
     * 根据显示控件压缩图片 适用于单张图片或详情
     * 在做Android图片加载的时候，由于手机屏幕受限，很多大图加载过来的时候，我们要求等比例缩放，比如按照固定的宽度，
     * 等比例缩放高度，使得图片的尺寸比例得到相应的缩放，但图片没有变形。显然按照android:scaleType不能实现，因为会有很多限制，
     * 所以必须要自己写算法。
     * 加载本地图片不使用缓存
     * @param path      图片路径
     * @param imageView 显示控件
     * @version 1.0
     * @updateInfo (此处输入修改内容, 若无修改可不写.)
     */
    public void onWidgetImage(Context context, String path, ImageView imageView) {
        if (!TextUtils.isEmpty(path) && null != imageView) {
            if(!AnyIdCardCheckUtils.getInstance(context).isUrl(path) && !(path.startsWith("http://") || path.startsWith("https://"))){
                Picasso.with(context).load(new File(path)).transform(getTransformation(imageView)).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).placeholder(load_image).error(load_error).into(imageView);
            }else{
                path = getUrlToUtf(path);
                Picasso.with(context).load(path).transform(getTransformation(imageView)).placeholder(load_image).error(load_error).into(imageView);
            }
        }
    }

    public String getUrlToUtf(String path) {
        if(IsNullUtils.isNulls(path)){
            return path;
        }
        //中文的utf-8范围
        Pattern p = Pattern.compile("[\\u4e00-\\u9fa5]");
        //找到中文url中的中文
        Matcher m = p.matcher(path);
        //依次递推，查找下一个单个文字，然后把他替换成utf-8
        while (m.find()) {
            String group = m.group();
            try {
                path = path.replaceFirst(group, URLEncoder.encode(group, "utf-8"));
            } catch (Exception e) {
                L.e("网址中文Utf-8转换异常", e.toString());
            }
        }
        //L.e("网址中文Utf-8转换","转换后的网址-->"+path);
        return path;
    }

    /**
     * @Description 自定裁剪圆形图片
     * @date 2015年11月20日
     * Inc. All rights reserved.
     */
    public class RoundnessFormation implements Transformation {

        @Override
        public String key() {
            return "square()";
        }

        @Override
        public Bitmap transform(Bitmap source) {
            Bitmap squaredBitmap = Bitmap.createScaledBitmap(source, percentX, percentX, true);
            if (squaredBitmap != source) {
                source.recycle();
            }
            Bitmap bitmap = Bitmap.createBitmap(percentX, percentX, Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);
            float r = percentX / 2f;
            canvas.drawCircle(r, r, r, paint);
            squaredBitmap.recycle();
            return bitmap;
        }
    }


    /**
     * 通过Picasso来缩放
     * 其实picasso提供了这样的方法。具体是显示Transformation 的transform方法。
     * 1、先获取网络或本地图片的宽高
     * 2、获取需要的目标宽
     * 3、按比例得到目标的高度
     * 4、按照目标的宽高创建新图
     * 在做Android图片加载的时候，由于手机屏幕受限，很多大图加载过来的时候，我们要求等比例缩放，比如按照固定的宽度，
     * 等比例缩放高度，使得图片的尺寸比例得到相应的缩放，但图片没有变形。显然按照android:scaleType不能实现，因为会有很多限制，
     * 所以必须要自己写算法。
     *
     * @param mImg
     * @return
     */
    private Transformation getTransformation(final ImageView mImg) {
        Transformation transformation = new Transformation() {

            @Override
            public Bitmap transform(Bitmap source) {

                Size msize = getImageViewSize(mImg);
                int targetWidth = msize.width;
                //L.e("source.getHeight()="+source.getHeight()+",source.getWidth()="+source.getWidth()+",targetWidth="+targetWidth);

                if (source.getWidth() == 0) {
                    return source;
                }

                //如果图片小于设置的宽度，则返回原图
                if (source.getWidth() < targetWidth) {
                    return source;
                } else {
                    //如果图片大小大于等于设置的宽度，则按照设置的宽度比例来缩放
                    double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
                    int targetHeight = (int) (targetWidth * aspectRatio);

                    //L.e("source.getHeight()="+targetHeight+",source.getWidth()="+targetWidth);
                    if (targetHeight != 0 && targetWidth != 0) {
                        Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                        if (result != source) {
                            // Same bitmap is returned if sizes are the same
                            source.recycle();
                        }
                        return result;
                    } else {
                        return source;
                    }
                }

            }

            @Override
            public String key() {
                return "transformation" + " desiredWidth";
            }
        };
        return transformation;
    }

    /**
     * 获取显示控件的大小
     *
     * @param imageView 显示控件
     * @return
     * @version 1.0
     * @updateInfo (此处输入修改内容, 若无修改可不写.)
     */
    private Size getImageViewSize(ImageView imageView) {

        Size imageSize = new Size();

        DisplayMetrics displayMetrics = imageView.getContext().getResources().getDisplayMetrics();

        LayoutParams lp = imageView.getLayoutParams();

        int width = imageView.getWidth();// 获取imageview的实际宽度
        if (width <= 0) {
            width = lp.width;// 获取imageview在layout中声明的宽度
        }
        if (width <= 0) {
            // width = imageView.getMaxWidth();// 检查最大值
            width = getImageViewFieldValue(imageView, "mMaxWidth");
        }
        if (width <= 0) {
            width = displayMetrics.widthPixels;
        }

        int height = imageView.getHeight();// 获取imageview的实际高度
        if (height <= 0) {
            height = lp.height;// 获取imageview在layout中声明的宽度
        }
        if (height <= 0) {
            height = getImageViewFieldValue(imageView, "mMaxHeight");// 检查最大值
        }
        if (height <= 0) {
            height = displayMetrics.heightPixels;
        }
        imageSize.width = width;
        imageSize.height = height;

        return imageSize;
    }

    /**
     * 通过反射技术获取显示控件的对象
     *
     * @param object    对象
     * @param fieldName 方法名称
     * @return
     * @version 1.0
     * @updateInfo (此处输入修改内容, 若无修改可不写.)
     */
    private static int getImageViewFieldValue(Object object, String fieldName) {
        int value = 0;
        try {
            Field field = ImageView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            int fieldValue = field.getInt(object);
            if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
                value = fieldValue;
            }
        } catch (Exception e) {
        }
        return value;
    }


    /**
     * @Description 记录计算的宽，高值
     * @date 2015年11月20日
     * Inc. All rights reserved.
     */
    private class Size {
        /**
         * 宽度
         */
        private int width;
        /**
         * 高度
         */
        private int height;

        @Override
        public String toString() {
            return "Size [width=" + width + ", height=" + height + "]";
        }
    }

    private String path;

    /**
     * 通过网络下载图片到指定目录 比如 /ZeroLife/picture/
     *
     * @param c
     * @param url
     */
    public void DownloadImage(Context c, String url, String dirPathName, final String imgName, final onBackDownLoadImagePathLisnner m) {
        path = getUrlToUtf(path);
        path = CacheFolderUtils.getCacheFolderUtils().setCropCacheFolderBackStr(dirPathName);
        //Target
        Target target = new Target() {

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                String imageName = "";
                if (TextUtils.isEmpty(imgName)) {
                    imageName = System.currentTimeMillis() + ".png";
                } else {
                    imageName = imgName + ".png";
                }

                File dcimFile = new File(path, imageName);

                FileOutputStream ostream = null;
                try {
                    ostream = new FileOutputStream(dcimFile);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                    ostream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (m != null) {
                    if (path.endsWith("/")) {
                        m.onBackDownLoadImagePath(path + imageName);
                    } else {
                        m.onBackDownLoadImagePath(path + "/" + imageName);
                    }
                }
//				Toast.makeText(PicActivity.this,"图片下载至:"+dcimFile,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        //Picasso下载
        Picasso.with(c).load(url).into(target);
    }

    public interface onBackDownLoadImagePathLisnner {
        void onBackDownLoadImagePath(String path);
    }

    /**
     * 格式化Url链接
     **/
    public static String FormatUrl(String imageURL, String IP) {
        String imgUri = "";
        if (TextUtils.isEmpty(imageURL) || "null".equals(imageURL + "")) {
            imageURL = "http://";
        }
        if (imageURL.startsWith("http://") || imageURL.startsWith("https://")) {
            imgUri = imageURL;
        } else {
            if (imageURL.startsWith("/")) {
                if (imageURL.startsWith("/resource")) {
                    imgUri = IP + imageURL;
                } else {
                    imgUri = IP + "/resource" + imageURL;
                }
            } else {
                if (imageURL.startsWith("resource")) {
                    imgUri = IP + "/" + imageURL;
                } else {
                    imgUri = IP + "/resource/" + imageURL;
                }
            }
        }
        return imgUri;
    }

    /**
     * 根据指定宽高裁剪图片并显示
     *
     * @param activity
     * @param path
     * @param width
     * @param height
     * @param imageView
     */
    public void onLocadCropWidthHeightImage(final Activity activity, String path, final int width, final int height, final ImageView imageView,Callback callback) {
        if(!AnyIdCardCheckUtils.getInstance(activity).isUrl(path) && !(path.startsWith("http://") || path.startsWith("https://"))){
            L.e("图片加载中","----本地图片加载--"+path);
            RequestCreator mRequestCreatorFile = Picasso.with(activity)//
                    .load(new File(path)).placeholder(load_image)//
                    .error(load_error).config(Config.RGB_565);
            if(width == 0 || height == 0){
                mRequestCreatorFile.memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE);//
            }else{
                mRequestCreatorFile.resize(width, height)//
                        .centerInside()//
                        .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE);//
            }
            if(callback == null){
                mRequestCreatorFile.into(imageView);
            }else{
                mRequestCreatorFile.into(imageView,callback);
            }
        }else{
            path = getUrlToUtf(path);
            L.e("图片加载中","----网络图片加载--"+path);
            RequestCreator mRequestCreator = Picasso.with(activity)//
                    .load(path).placeholder(load_image)//
                    .error(load_error);
            if(width == 0 || height == 0){

            }else{
                mRequestCreator//
                        .resize(width, height)//
                        .centerInside();//
            }
            if(callback == null){
                mRequestCreator.into(imageView);
            }else{
                mRequestCreator.into(imageView,callback);
            }
        }
    }
}
