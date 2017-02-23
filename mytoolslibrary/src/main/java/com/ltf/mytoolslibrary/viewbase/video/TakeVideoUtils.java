package com.ltf.mytoolslibrary.viewbase.video;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.view.Gravity;
import android.widget.ImageView;

import com.ltf.mytoolslibrary.viewbase.base.ActivityTitleBase;
import com.ltf.mytoolslibrary.viewbase.constent.constent;
import com.ltf.mytoolslibrary.viewbase.isnull.IsNullUtils;
import com.ltf.mytoolslibrary.viewbase.permission.CheckPermissionUtils;
import com.ltf.mytoolslibrary.viewbase.video.activity.VideoPreviewActivity;
import com.ltf.mytoolslibrary.viewbase.video.activity.VideoRecorderActivity;
import com.ltf.mytoolslibrary.viewbase.views.UIAlertView;

import java.io.File;

import static android.os.Build.VERSION_CODES.M;

/**
 * 作者：${李堂飞} on 2016/12/21 0021 13:51
 * 邮箱：1195063924@qq.com
 * 注解: 拍摄短视频相关 预览视频
 */
public class TakeVideoUtils {

    private static TakeVideoUtils mTakeVideoUtils;
    public static TakeVideoUtils getTakeVideoUtils(){
        if(mTakeVideoUtils == null){
            mTakeVideoUtils = new TakeVideoUtils();
        }
        return mTakeVideoUtils;
    }
    private TakeVideoUtils(){

    }

    private int MaxTimes = 10;
    /**设置最大录制时间**/
    public TakeVideoUtils setMaxTime(int MaxTime){
        this.MaxTimes = MaxTime;
        return this;
    }
    /**
     * 启动录制视频
     * @param activity
     * @param isAudio 录制时是否携带声音
     * @param monTakeVideoClickLisnners
     */
    public void startTakeVideo(final Activity activity, final boolean isAudio, onTakeVideoClickLisnner monTakeVideoClickLisnners){
        this.monTakeVideoClickBackLisnner = monTakeVideoClickLisnners;
        String[] str;
        if(isAudio){
            str = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO};
        }else{
            str = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        }
        CheckPermissionUtils.getSelectPicUpdateUtils().checkPermission(-1, activity, false, new CheckPermissionUtils.onBackPermissionResult() {
            @Override
            public void onBackPermissionResult() {
                Intent intent = new Intent(activity, VideoRecorderActivity.class);
                intent.putExtra("isAudio",isAudio);
                intent.putExtra("time",MaxTimes);
                activity.startActivityForResult(intent, constent.RESULT_VIDEO_CODE_BACK);
            }
        }, str);
    }

    /**
     * 启动预览视频
     * @param activity
     * @param path
     */
    public void startVideoPlay(Activity activity,String path){
        Intent intent = new Intent(activity, VideoPreviewActivity.class);
        intent.putExtra(constent.INTENT_VIDEO_PATH, path);
        activity.startActivity(intent);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case constent.RESULT_VIDEO_CODE_BACK:
                if(monTakeVideoClickBackLisnner != null && data != null){
                    monTakeVideoClickBackLisnner.onTakeVideoClickBack(IsNullUtils.isNullBackStr(data.getStringExtra(VideoRecorderActivity.INTENT_PATH),""));
                }
                break;
        }

    }

    /**
     * 获取视频的第一帧图片并显示在一个ImageView中
     * @param VideoPath
     * @param imageView
     */
    public void setVideoToImageView(final Activity activity, String VideoPath, ImageView imageView){
        if (!IsNullUtils.isNulls(VideoPath)) {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();// 实例化MediaMetadataRetriever对象
            File file = new File(VideoPath);// 实例化File对象，文件路径为/storage/sdcard/Movies/music1.mp4
            if (file.exists()) {
                try{
                    mmr.setDataSource(file.getAbsolutePath());// 设置数据源为该文件对象指定的绝对路径
                    Bitmap bitmap = mmr.getFrameAtTime();// 获得视频第一帧的Bitmap对象
                    // btn_repair_add2.setImageBitmap(bitmap);//设置ImageView显示的图片
                    if (bitmap != null) {
                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, 480, 480);
                        imageView.setImageBitmap(bitmap);// 设置ImageView显示的图片
                    }
                }catch (Exception e){
                    if(Build.VERSION.SDK_INT < M){
                        final UIAlertView alertDialog = new UIAlertView(Gravity.LEFT,
                                activity,
                                "温馨提示",
                                "    录制视频需要赋予访问音频、摄像头以及存储的权限，不开启将无法正常工作！\n    请点击\"设置\"-\"权限\"打开所需权限。最后点击两次后退按钮，即可返回。",
                                "取消", "设置");
                        alertDialog.show();
                        alertDialog.setClicklistener(new UIAlertView.ClickListenerInterface() {
                                                         @Override
                                                         public void doLeft() {
                                                             alertDialog.dismiss();
                                                         }

                                                         @Override
                                                         public void doRight() {
                                                             ((ActivityTitleBase)activity).startAppSettings(activity);
                                                         }
                                                     }
                        );
                    }
                }
            }
        }
    }

    private onTakeVideoClickLisnner monTakeVideoClickBackLisnner;
    public interface onTakeVideoClickLisnner{
        /**返回视频录制路径**/
        void onTakeVideoClickBack(String videoPath);
    }
}
