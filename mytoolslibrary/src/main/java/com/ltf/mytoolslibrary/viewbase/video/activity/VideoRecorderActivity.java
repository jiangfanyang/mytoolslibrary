package com.ltf.mytoolslibrary.viewbase.video.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.ltf.mytoolslibrary.R;
import com.ltf.mytoolslibrary.viewbase.base.ActivityTitleBase;
import com.ltf.mytoolslibrary.viewbase.utils.AppManager;
import com.ltf.mytoolslibrary.viewbase.utils.show.T;
import com.ltf.mytoolslibrary.viewbase.video.MagicProgressBar;
import com.ltf.mytoolslibrary.viewbase.video.listener.OnRecorderErrorListener;
import com.ltf.mytoolslibrary.viewbase.video.listener.OnVideoRecorderListener;
import com.ltf.mytoolslibrary.viewbase.video.widget.RecordButton;
import com.ltf.mytoolslibrary.viewbase.video.widget.VideoRecorderView;


/**
 * Created by 李堂飞 on 2016/10/10.
 */
public class VideoRecorderActivity extends ActivityTitleBase {

    // INTENT_PATH
    public static final String INTENT_PATH = "path";

    private VideoRecorderView mRecordView;
    private RecordButton mRecordButton;
    private TextView mTipLoosen;
    private TextView mTipGlide;
    private MagicProgressBar mPB;
    private boolean isAudio = true;

    /**
     * 继承TitleFragmentBase  初始化title将要显示什么出来  什么可以点击
     */
    @Override
    protected void initTitle() {
        isAudio = getIntent().getBooleanExtra("isAudio",true);
        setUpTitleBack();
        setUpTitleCentreText("录制视频");
        isComplete = false;
        findView();
        mRecordView.setRecordTime(getIntent().getIntExtra("time",10));
        mRecordView.setIsAudio(isAudio);
        initView();
        setListener();
    }

    @Override
    public void onTitleBackClick() {
        super.onTitleBackClick();
        AppManager.getAppManager().finishActivity(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_recorder_video);

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
        return R.layout.activity_recorder_video;
    }

    /**
     * 查找控件
     */
    private void findView() {
        mRecordView = (VideoRecorderView) findViewById(R.id.vr_record);
        mRecordButton = (RecordButton) findViewById(R.id.btn_record);
        mTipLoosen = (TextView) findViewById(R.id.tv_loosen_record);
        mTipGlide = (TextView) findViewById(R.id.tv_up_glide_record);
        mPB = (MagicProgressBar) findViewById(R.id.pb_record);

    }

    /**
     * 初始化
     */
    private void initView() {
        mTipGlide.setVisibility(View.GONE);
        mTipLoosen.setVisibility(View.GONE);
        mPB.setVisibility(View.GONE);
        mPB.setProgress(getProgress(0));
    }


    /**
     * 设置监听器
     */
    private void setListener() {
        mRecordView.setOnRecorderErrorListener(new OnRecorderErrorListener());
        mRecordView.setOnVideoRecorderListener(onVideoRecorderListener());
        mRecordButton.setOnTouchRecordListener(onTouchRecordListener());
    }

    /**
     * 获取进度
     * @param progress
     * @return
     */
    private int getProgress(int progress) {
        return 100 - progress;
    }

    private boolean isComplete = false;
    /**
     * 视频录制监听
     * @return
     */
    private OnVideoRecorderListener onVideoRecorderListener() {
        return new OnVideoRecorderListener() {
            @Override
            public void onRecorderProgress(VideoRecorderView view, int progress) {
                mPB.setProgress(getProgress(progress));
            }

            @Override
            public void onComplete(VideoRecorderView view, String path, long recordTime, int mCurTime) {
                if(mCurTime <= 3 && !isComplete){
                    mRecordView.cancelRecord();
                    T.showShort(VideoRecorderActivity.this,"录制时间小于3S...");
                    return;
                }
                BackMessage(path);
                isComplete = true;
            }

            @Override
            public void onCancel(VideoRecorderView view) {
                isComplete = false;
            }
        };
    }

    private void BackMessage(String path) {
        Intent intent = new Intent();
        intent.putExtra(INTENT_PATH, path);
        setResult(RESULT_OK, intent);
        AppManager.getAppManager().finishActivity(this);
    }


    /**
     * 按钮按下监听
     * @return
     */
    private RecordButton.OnTouchRecordListener onTouchRecordListener() {
        return new RecordButton.OnTouchRecordListener() {
            @Override
            public void onStateChange(RecordButton btn, RecordButton.RecordState state) {
                if (RecordButton.RecordState.READY_CANCEL == state) {
                    mTipGlide.setVisibility(View.GONE);
                    mTipLoosen.setVisibility(View.VISIBLE);
                    mRecordView.setCancelTimeOut(true);
                    mPB.setFillColor(getColorById(R.color.pb_loosen));
                } else if (RecordButton.RecordState.RECODEING == state) {
                    mTipGlide.setVisibility(View.VISIBLE);
                    mTipLoosen.setVisibility(View.GONE);
                    mRecordView.setCancelTimeOut(false);
                    mPB.setFillColor(getColorById(R.color.pb_normal));
                }
            }

            @Override
            public void onDown(RecordButton btn) {
                mTipGlide.setVisibility(View.VISIBLE);
                mTipLoosen.setVisibility(View.GONE);
                mPB.setVisibility(View.VISIBLE);
                mPB.setProgress(getProgress(0));
                mPB.setFillColor(getColorById(R.color.pb_normal));
                isComplete = false;
                // 开始录制
                mRecordView.startRecord();
            }

            @Override
            public void onUp(RecordButton btn, RecordButton.RecordState state) {
                mTipGlide.setVisibility(View.GONE);
                mTipLoosen.setVisibility(View.GONE);
                mPB.setVisibility(View.GONE);

                // 根据状态判断是否取消录制
                if (state == RecordButton.RecordState.READY_CANCEL) {
                    mRecordView.cancelRecord();
                } else if (state == RecordButton.RecordState.RECODEING) {
                    mRecordView.stopRecord();
                }
            }
        };
    }

    private int getColorById(@ColorRes int id) {
        return ContextCompat.getColor(VideoRecorderActivity.this, id);
    }
}
