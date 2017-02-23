package com.ltf.mytoolslibrary.viewbase.takephoto;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.ltf.mytoolslibrary.viewbase.CacheFolder.CacheFolderUtils;
import com.ltf.mytoolslibrary.viewbase.constent.constent;
import com.ltf.mytoolslibrary.viewbase.takephoto.bean.ImageFolder;
import com.ltf.mytoolslibrary.viewbase.takephoto.bean.ImageItem;
import com.ltf.mytoolslibrary.viewbase.takephoto.loader.ImageLoader;
import com.ltf.mytoolslibrary.viewbase.takephoto.view.CropImageView;
import com.ltf.mytoolslibrary.viewbase.utils.show.L;
import com.ltf.mytoolslibrary.viewbase.utils.show.T;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * ================================================
 * 作    者：李堂飞
 * 版    本：1.0
 * 创建日期：2016/5/19
 * 描    述：图片选择的入口类
 * 修订历史：
 * ================================================
 */
public class ImagePicker {

    public static final String TAG = ImagePicker.class.getSimpleName();
    public static final int REQUEST_CODE_TAKE = constent.REQUEST_CODE_TAKE;
    public static final int REQUEST_CODE_CROP = constent.REQUEST_CODE_CROP;
    public static final int REQUEST_CODE_PREVIEW = constent.REQUEST_CODE_PREVIEW;
    public static final int RESULT_CODE_ITEMS = constent.RESULT_CODE_ITEMS;
    public static final int RESULT_CODE_BACK = constent.RESULT_CODE_BACK;

    public static final String EXTRA_RESULT_ITEMS = "extra_result_items";
    public static final String EXTRA_SELECTED_IMAGE_POSITION = "selected_image_position";
    public static final String EXTRA_IMAGE_ITEMS = "extra_image_items";

    private boolean multiMode = true;    //图片选择模式
    private int selectLimit = 9;         //最大选择图片数量
    private boolean crop = true;         //裁剪
    private boolean showCamera = true;   //显示相机
    private boolean isSaveRectangle = false;  //裁剪后的图片是否是矩形，否者跟随裁剪框的形状
    private int outPutX = 800;           //裁剪保存宽度
    private int outPutY = 800;           //裁剪保存高度
    private int focusWidth = 280;         //焦点框的宽度
    private int focusHeight = 280;        //焦点框的高度
    private ImageLoader imageLoader;     //图片加载器
    private CropImageView.Style style = CropImageView.Style.RECTANGLE; //裁剪框的形状
    private File cropCacheFolder;
    private File takeImageFile;
    public Bitmap cropBitmap;

    private ArrayList<ImageItem> mSelectedImages = new ArrayList<>();   //选中的图片集合
    private List<ImageFolder> mImageFolders;      //所有的图片文件夹
    private int mCurrentImageFolderPosition = 0;  //当前选中的文件夹位置 0表示所有图片
    private List<OnImageSelectedListener> mImageSelectedListeners;          // 图片选中的监听回调

    private static ImagePicker mInstance;

    private ImagePicker() {
    }

    public static ImagePicker getInstance() {
        if (mInstance == null) {
            synchronized (ImagePicker.class) {
                if (mInstance == null) {
                    mInstance = new ImagePicker();
                }
            }
        }
        return mInstance;
    }

    public boolean isMultiMode() {
        return multiMode;
    }

    public void setMultiMode(boolean multiMode) {
        this.multiMode = multiMode;
    }

    public int getSelectLimit() {
        return selectLimit;
    }

    public void setSelectLimit(int selectLimit) {
        this.selectLimit = selectLimit;
    }

    public boolean isCrop() {
        return crop;
    }

    public void setCrop(boolean crop) {
        this.crop = crop;
    }

    public boolean isShowCamera() {
        return showCamera;
    }

    public void setShowCamera(boolean showCamera) {
        this.showCamera = showCamera;
    }

    public boolean isSaveRectangle() {
        return isSaveRectangle;
    }

    public void setSaveRectangle(boolean isSaveRectangle) {
        this.isSaveRectangle = isSaveRectangle;
    }

    public int getOutPutX() {
        return outPutX;
    }

    public void setOutPutX(int outPutX) {
        this.outPutX = outPutX;
    }

    public int getOutPutY() {
        return outPutY;
    }

    public void setOutPutY(int outPutY) {
        this.outPutY = outPutY;
    }

    public int getFocusWidth() {
        return focusWidth;
    }

    public void setFocusWidth(int focusWidth) {
        this.focusWidth = focusWidth;
    }

    public int getFocusHeight() {
        return focusHeight;
    }

    public void setFocusHeight(int focusHeight) {
        this.focusHeight = focusHeight;
    }

    public File getTakeImageFile() {
        return takeImageFile;
    }

    public File getCropCacheFolder(Context context) {
        if (cropCacheFolder == null) {
//            cropCacheFolder = new File(context.getCacheDir() + "/ImagePicker/cropTemp/");
            String path = CacheFolderUtils.getCacheFolderUtils().CacheTopDirectoryName +"/DCIM/camera/";
            cropCacheFolder = CacheFolderUtils.getCacheFolderUtils().setCropCacheFolderBackFile(path);
        }
        return cropCacheFolder;
    }

    public void setCropCacheFolder(File cropCacheFolder) {
        this.cropCacheFolder = cropCacheFolder;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public void setImageLoader(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    public CropImageView.Style getStyle() {
        return style;
    }

    public void setStyle(CropImageView.Style style) {
        this.style = style;
    }

    public List<ImageFolder> getImageFolders() {
        return mImageFolders;
    }

    public void setImageFolders(List<ImageFolder> imageFolders) {
        mImageFolders = imageFolders;
    }

    public int getCurrentImageFolderPosition() {
        return mCurrentImageFolderPosition;
    }

    public void setCurrentImageFolderPosition(int mCurrentSelectedImageSetPosition) {
        mCurrentImageFolderPosition = mCurrentSelectedImageSetPosition;
    }

    public ArrayList<ImageItem> getCurrentImageFolderItems() {
        return mImageFolders.get(mCurrentImageFolderPosition).images;
    }

    public boolean isSelect(ImageItem item) {
        return mSelectedImages.contains(item);
    }

    public int getSelectImageCount() {
        if (mSelectedImages == null) {
            return 0;
        }
        return mSelectedImages.size();
    }

    public ArrayList<ImageItem> getSelectedImages() {
        return mSelectedImages;
    }

    public void clearSelectedImages() {
        if (mSelectedImages != null) mSelectedImages.clear();
    }

    public void clear() {
        if (mImageSelectedListeners != null) {
            mImageSelectedListeners.clear();
            mImageSelectedListeners = null;
        }
        if (mImageFolders != null) {
            mImageFolders.clear();
            mImageFolders = null;
        }
        if (mSelectedImages != null) {
            mSelectedImages.clear();
        }
        mCurrentImageFolderPosition = 0;
    }

    /** 拍照的方法 */
    public void takePicture(Activity activity, int requestCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
//            if (Utils.existSDCard()){
//                takeImageFile = new File(Environment.getExternalStorageDirectory(), "/DCIM/camera/");
//            }else {
//                takeImageFile = Environment.getDataDirectory();
//            }
            String path = CacheFolderUtils.getCacheFolderUtils().CacheTopDirectoryName +"/DCIM/camera/";
            takeImageFile = CacheFolderUtils.getCacheFolderUtils().setCropCacheFolderBackFile(path);
            takeImageFile = createFile(takeImageFile, "IMG_", ".jpg");
            if (takeImageFile != null) {
                // 默认情况下，即不需要指定intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                // 照相机有自己默认的存储路径，拍摄的照片将返回一个缩略图。如果想访问原始图片，
                // 可以通过dat extra能够得到原始图片位置。即，如果指定了目标uri，data就没有数据，
                // 如果没有指定uri，则data就返回有数据！
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(takeImageFile));
            }
        }
        try{
            activity.startActivityForResult(takePictureIntent, requestCode);
        }catch (Exception e){
            L.e("拍照反异常:-->"+e.toString());
            T.showShort(activity,"启动拍照失败,请重试...");
            activity.finish();
        }
    }

    /** 根据系统时间、前缀、后缀产生一个文件 */
    public static File createFile(File folder, String prefix, String suffix) {
        if (!folder.exists() || !folder.isDirectory()) folder.mkdirs();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        String filename = prefix + dateFormat.format(new Date(System.currentTimeMillis())) + suffix;
        return new File(folder, filename);
    }

    /** 扫描图片 */
    public static void galleryAddPic(Context context, File file) {
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), file.getName(), null);
        } catch (FileNotFoundException e) {
            L.e("把文件插入到系统图库-->",e.toString()+"");
        }
//        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        Uri contentUri = Uri.fromFile(file);
//        mediaScanIntent.setData(contentUri);
//        context.sendBroadcast(mediaScanIntent);
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + Uri.fromFile(file))));
    }

    /** 图片选中的监听 */
    public interface OnImageSelectedListener {
        void onImageSelected(int position, ImageItem item, boolean isAdd);
    }

    public void addOnImageSelectedListener(OnImageSelectedListener l) {
        if (mImageSelectedListeners == null) mImageSelectedListeners = new ArrayList<>();
        mImageSelectedListeners.add(l);
    }

    public void removeOnImageSelectedListener(OnImageSelectedListener l) {
        if (mImageSelectedListeners == null) return;
        mImageSelectedListeners.remove(l);
    }

    public void addSelectedImageItem(int position, ImageItem item, boolean isAdd) {
        if (isAdd) mSelectedImages.add(item);
        else mSelectedImages.remove(item);
        notifyImageSelectedChanged(position, item, isAdd);
    }

    private void notifyImageSelectedChanged(int position, ImageItem item, boolean isAdd) {
        if (mImageSelectedListeners == null) return;
        for (OnImageSelectedListener l : mImageSelectedListeners) {
            l.onImageSelected(position, item, isAdd);
        }
    }
}