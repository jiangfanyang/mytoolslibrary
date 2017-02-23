package com.ltf.mytoolslibrary.viewbase.video.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.ltf.mytoolslibrary.R;
import com.ltf.mytoolslibrary.viewbase.base.ActivityTitleBase;
import com.ltf.mytoolslibrary.viewbase.constent.constent;
import com.ltf.mytoolslibrary.viewbase.utils.AppManager;
import com.ltf.mytoolslibrary.viewbase.utils.ScreenUtils;
import com.ltf.mytoolslibrary.viewbase.video.widget.VideoPreviewView;


/**
 * Created by 李堂飞 on 2016/10/11.
 */
public class VideoPreviewActivity extends ActivityTitleBase {

    private VideoPreviewView mVideoPreview;

    /**
     * 继承TitleFragmentBase  初始化title将要显示什么出来  什么可以点击
     */
    @Override
    protected void initTitle() {
        setUpTitleBack();
        setUpTitleCentreText("视频预览");
        findView();
        playVideo();
    }

    @Override
    public void onTitleBackClick() {
        super.onTitleBackClick();
        AppManager.getAppManager().finishActivity(this);
    }

    @Override
    public void onBackPressed() {
        AppManager.getAppManager().finishActivity(this);
        super.onBackPressed();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_preview_video);
    }

    /***
     * 设置是否状态栏颜色
     */
    @Override
    public boolean setIsViewStaueColor() {
        return true;
    }

    /**
     * 通知栏所需颜色
     **/
    @Override
    public String setStatusBarTintResource() {
        return R.color.black+"";
    }

    /**
     * 设置资源布局
     *
     * @return
     */
    @Override
    protected int setLayoutId() {
        return R.layout.activity_preview_video;
    }

    /**
     *
     */
    private void findView() {
        mVideoPreview = (VideoPreviewView) findViewById(R.id.video_preview);
        findViewById(R.id.root).setOnClickListener(rootClick());
        mVideoPreview.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenH(this), ScreenUtils.getScreenH(this)));
    }

    /**
     *
     */
    private void playVideo() {
        String path = getIntent().getStringExtra(constent.INTENT_VIDEO_PATH);
        mVideoPreview.setVideoPath(path);
        mVideoPreview.setOnVideoPlayListener(onVideoPlayListener());
        mVideoPreview.setOnVideoBufferListener(onVideoBufferListener());
        mVideoPreview.start();
    }

    /**
     * 点击根布局监听
     * @return
     */
    private View.OnClickListener rootClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        };
    }

    /**
     *
     * @return
     */
    private VideoPreviewView.OnVideoPlayListener onVideoPlayListener() {
        return new VideoPreviewView.OnVideoPlayListener() {
            @Override
            public void onProgress(VideoPreviewView view, int play, int duration) {
                Log.d(getClass().getSimpleName(), "progress ==>" + play / duration + "play ==>> " + play + "   duration ==> " + duration);
            }
        };
    }

    /**
     *
     * @return
     */
    private VideoPreviewView.OnVideoBufferListener onVideoBufferListener() {
        return new VideoPreviewView.OnVideoBufferListener() {
            @Override
            public void onBuffering(VideoPreviewView view, int progress) {

            }
        };
    }

    @Override
    protected void onResume() {
        mVideoPreview.resume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mVideoPreview.pause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mVideoPreview.stopPlayback();
        super.onDestroy();
    }
}
