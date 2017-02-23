package com.ltf.mytoolslibrary.viewbase.video.listener;


import com.ltf.mytoolslibrary.viewbase.video.widget.VideoRecorderView;

/**
 * Created by 李堂飞 on 2016/10/10.
 */
public interface OnVideoRecorderListener {

    /**
     * 录制进度
     * @return
     */
    void onRecorderProgress(VideoRecorderView view, int progress);

    /**
     * 录制完成
     * @param path
     */
    void onComplete(VideoRecorderView view, String path, long recordTime, int time);

    /**
     * 取消录制
     */
    void onCancel(VideoRecorderView view);

}
