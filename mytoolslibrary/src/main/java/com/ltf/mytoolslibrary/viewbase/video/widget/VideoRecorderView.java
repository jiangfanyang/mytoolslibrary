package com.ltf.mytoolslibrary.viewbase.video.widget;

import android.content.Context;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.ltf.mytoolslibrary.viewbase.CacheFolder.CacheFolderUtils;
import com.ltf.mytoolslibrary.viewbase.app.ApplicationBase;
import com.ltf.mytoolslibrary.viewbase.isnull.IsNullUtils;
import com.ltf.mytoolslibrary.viewbase.utils.show.L;
import com.ltf.mytoolslibrary.viewbase.utils.show.T;
import com.ltf.mytoolslibrary.viewbase.video.listener.OnRecorderErrorListener;
import com.ltf.mytoolslibrary.viewbase.video.listener.OnVideoRecorderListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 李堂飞 on 2016/10/10.
 */
public class VideoRecorderView extends SurfaceView {

    // Handler
    private Handler mHandler = null;

    // 默认视频录制分辨率 width * height
    private final int DEF_VIDEO_WIDTH = 640;
    private final int DEF_VIDEO_HEIGHT = 360;
    // 默认最大视频拍摄时间
    private final int DEF_VIDEO_TIME_MAX = 10;
    // 默认打开摄像头
    private final boolean DEF_VIDEO_OPEN_CAMERA = true;

    // Surface
    private SurfaceHolder mSurfaceHolder = null;

    // Video
    private Camera camera = null;
    //    private int mCurCamera = 0;
    private MediaRecorder mMediaRecorder = null;

    // 视频分辨率 width * height
//    private int mPreviewWidth = DEF_VIDEO_WIDTH;
//    private int mPreviewHeight = DEF_VIDEO_HEIGHT;
    private int mVideoWidth = DEF_VIDEO_WIDTH;
    private int mVideoHeight = DEF_VIDEO_HEIGHT;
    // 视频最大拍摄时间
    private int mCurRecordTime = 0;
    private int mRecordTime = DEF_VIDEO_TIME_MAX;
    private boolean mIsOpenCamera = DEF_VIDEO_OPEN_CAMERA;

    public void setRecordTime(int mRecordTime) {
        this.mRecordTime = mRecordTime;
    }

    // 时间到时否是取消拍摄
    private boolean isCancelTimeOut = false;

    private Timer mTimer = null;
    private String recordFile = null; // 视频文件
    private long sizePicture = 0;

    public VideoRecorderView(Context context) {
        this(context, null);
        this.init();
    }

    public VideoRecorderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.init();
    }

    public VideoRecorderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    /**
     * 初始化
     */
    private void init() {
        mHandler = new Handler(Looper.getMainLooper());
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(videoCallback());
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }

    /**
     * 旋转数据
     *  data
     *            目标数据
     *            源数据
     * @param imageWidth
     *            源数据宽
     * @param imageHeight
     *            源数据高
     */
    private byte[] YUV42RotateDegree90(byte[] data, int imageWidth, int imageHeight)
    {
        byte [] yuv = new byte[imageWidth*imageHeight*3/2];

        // Y
        int i = 0;
        for(int x = 0;x < imageWidth;x++)
        {
            for(int y = imageHeight-1;y >= 0;y--)
            {
                yuv[i] = data[y*imageWidth+x];
                i++;
            }
        }

        // U and V
        i = imageWidth*imageHeight*3/2-1;
        int pos = imageWidth*imageHeight;
        for(int x = imageWidth-1;x > 0;x=x-2)
        {

            for(int y = 0;y < imageHeight/2;y++)
            {
                yuv[i] = data[pos+(y*imageWidth)+x];
                i--;
                yuv[i] = data[pos+(y*imageWidth)+(x-1)];
                i--;
            }
        }
        return yuv;
    }

    /**
     * 旋转数据
     *
     * @param dst
     *            目标数据
     * @param src
     *            源数据
     * @param srcWidth
     *            源数据宽
     * @param srcHeight
     *            源数据高
     */
    private void YV12RotateNegative90(byte[] dst, byte[] src, int srcWidth,
                                      int srcHeight) {
        int t = 0;
        int i, j;

        int wh = srcWidth * srcHeight;

        for (i = srcWidth - 1; i >= 0; i--) {
            for (j = srcHeight - 1; j >= 0; j--) {
                dst[t++] = src[j * srcWidth + i];
            }
        }

        for (i = srcWidth / 2 - 1; i >= 0; i--) {
            for (j = srcHeight / 2 - 1; j >= 0; j--) {
                dst[t++] = src[wh + j * srcWidth / 2 + i];
            }
        }

        for (i = srcWidth / 2 - 1; i >= 0; i--) {
            for (j = srcHeight / 2 - 1; j >= 0; j--) {
                dst[t++] = src[wh * 5 / 4 + j * srcWidth / 2 + i];
            }
        }

    }

    /**
     * 初始化摄像头
     * @throws IOException
     */
    private void initCamera() throws IOException{
        if (camera != null) {
            freeCameraResource();
        }
//        if (checkCameraFacing(Camera.CameraInfo.CAMERA_FACING_BACK)) {
//            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
//            mCurCamera = Camera.CameraInfo.CAMERA_FACING_BACK;
//        } else if (checkCameraFacing(Camera.CameraInfo.CAMERA_FACING_FRONT)) {
//            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
//            mCurCamera = Camera.CameraInfo.CAMERA_FACING_FRONT;
//        }
        camera = Camera.open();
        if (camera == null) {
            return;
        }

        camera.setPreviewCallback(new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {
                Log.d(getClass().getSimpleName(), "onPreviewFrame");
                byte[] src = new byte[data.length];
                System.arraycopy(data, 0, src, 0, data.length);
//                YV12RotateNegative90(data, src, mVideoWidth, mVideoHeight);
                data = YUV42RotateDegree90(data, mVideoHeight, mVideoWidth);
                camera.addCallbackBuffer(data);
            }
        });
        setCameraParams();
        // 默认是横屏，旋转 90 度
        camera.setDisplayOrientation(90);
        camera.setPreviewDisplay(mSurfaceHolder);
        camera.startPreview();
        camera.unlock();
    }


    /**
     * 设置倒计时结束时，是否是取消录制
     * @param cancel
     */
    public void setCancelTimeOut(boolean cancel) {
        isCancelTimeOut = cancel;
    }


    /**
     * 设置摄像头竖屏
     */
    private void setCameraParams() {
        if (camera != null) {
            Camera.Parameters parameters = camera.getParameters();
            parameters.set("orientation", Camera.Parameters.SCENE_MODE_LANDSCAPE);
//            parameters.set("rotation",180);
            List<Camera.Size> supportedPictureSizes = parameters.getSupportedPictureSizes();
            for (Camera.Size size : supportedPictureSizes) {
                sizePicture = (size.height * size.width) > sizePicture ? size.height * size.width : sizePicture;
            }
            // supportedPictureSizes  ====》 sizePicture
            setPreviewSize(parameters);
            camera.setParameters(parameters);
        }
    }

    /**
     * 根据手机支持的视频分辨率，设置预览尺寸
     * @param parameters
     */
    private void setPreviewSize(Camera.Parameters parameters) {
        if (camera == null) {
            return;
        }
        // 获取手机支持的分辨率集合，并以宽度为基准降序排序
        List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
        Collections.sort(previewSizes, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size s1, Camera.Size s2) {
                if (s1.width > s2.width) {
                    return -1;
                } else if (s1.width == s2.width) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });

        float tmp = 0f;
        float minDiff = 100f;
        float ratio = 3.0f / 4.0f;  // 高宽比 4:3 ,最接近屏幕宽度的分辨率，可以自己选择合适的
        Camera.Size best = null;
        for (Camera.Size s : previewSizes) {
            tmp = Math.abs(((float) s.height / (float) s.width) - ratio);
            Log.w(getClass().getSimpleName(), "set preview size: width ==> " + s.width + "  height ==> " + s.height);
            if (tmp < minDiff) {
                minDiff = tmp;
                best = s;
            }
        }

        // 预览比率
        Log.w(getClass().getSimpleName(), "set last preview size: width ==> " + best.width + "  height ==> " + best.height);
//        mPreviewWidth = best.width;
//        mPreviewHeight = best.height;
//        mSurfaceHolder.setFixedSize(best.width, best.height);
        parameters.setPreviewSize(best.width, best.height);

        // 大部分手机支持的预览尺寸和录制尺寸是一样的，也有特例，有些手机获取不到，那就把设置录制尺寸放到设置预览的方法里面
        if (parameters.getSupportedVideoSizes() == null || parameters.getSupportedVideoSizes().size() == 0) {
            mVideoWidth = best.width;
            mVideoHeight = best.height;
        } else {
            setVideoSize(parameters);
        }
    }

    /**
     * 根据手机支持的视频分辨率，设置录制尺寸
     * @param parameters
     */
    private void setVideoSize(Camera.Parameters parameters) {
        if (camera == null) {
            return;
        }
        //获取手机支持的分辨率集合，并以宽度为基准降序排序
        List<Camera.Size> previewSizes = parameters.getSupportedVideoSizes();
        Collections.sort(previewSizes, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size lhs, Camera.Size rhs) {
                if (lhs.width > rhs.width) {
                    return -1;
                } else if (lhs.width == rhs.width) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });

        float tmp = 0f;
        float minDiff = 100f;
        float ratio = 3.0f / 4.0f;//高宽比率3:4，且最接近屏幕宽度的分辨率
        Camera.Size best = null;
        for (Camera.Size s : previewSizes) {
            tmp = Math.abs(((float) s.height / (float) s.width) - ratio);
            Log.w(getClass().getSimpleName(), "set video size: width ==> " + s.width + "  height ==> " + s.height);
            if (tmp < minDiff) {
                minDiff = tmp;
                best = s;
            }
        }
        Log.w(getClass().getSimpleName(), "set last video size: width ==> " + best.width + "  height ==> " + best.height);
        //设置录制尺寸
        mVideoWidth = best.width;
        mVideoHeight = best.height;
    }


    /**
     * 检查当前是否有摄像头
     * @param facing
     * @return
     */
    private boolean checkCameraFacing(int facing) {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0, len = Camera.getNumberOfCameras(); i < len; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (facing == cameraInfo.facing) {
                return true;
            }
        }
        return false;
    }

    /**
     * 释放摄像头资源
     */
    private void freeCameraResource() {
        try {
            if (camera != null) {
                camera.setPreviewCallback(null);
                camera.stopPreview();
                camera.lock();
                camera.release();
            }
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "freeCameraResource error", e);
        } finally {
            camera = null;
        }
    }

    /**
     * SurfaceHolder 回调
     * @return
     */
    private SurfaceHolder.Callback videoCallback() {
        return new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                if (!mIsOpenCamera) {
                    return;
                }
                try {
                    initCamera();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                if (!mIsOpenCamera) {
                    return;
                }
                freeCameraResource();
            }
        };
    }

    /**
     * 初始化录制
     */
    private void initRecord() throws IOException {
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.reset();
        if (camera != null)
            mMediaRecorder.setCamera(camera);
        mMediaRecorder.setOnErrorListener(onErrorListener());
        mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);    // 视频源

        if(isAudio){
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);       // 音频源
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);  // 音频格式
        }

        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);  // 输出格式
//        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);    // 视频录制格式
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);// 视频录制格式

//        mSurfaceHolder.setFixedSize(mVideoWidth, mVideoHeight);
        mMediaRecorder.setVideoSize(mVideoWidth, mVideoHeight);             // 设置分辨率
//        mMediaRecorder.setVideoEncodingBitRate(1 * 1280 * 720);// 设置帧频率，然后就清晰了
//        mMediaRecorder.setVideoSize(1920, 1080 );             // 设置分辨率
        // TODO 设置每秒帧数 这个设置有可能会出问题，有的手机不支持这种帧率就会录制失败，这里使用默认的帧率，当然视频的大小肯定会受影响
        // mediaRecorder.setVideoFrameRate(25);
        if (sizePicture < 3000000) {    // 这里设置可以调整清晰度
            mMediaRecorder.setVideoEncodingBitRate(3 * 1024 * 512);
        } else if (sizePicture <= 5000000) {
            mMediaRecorder.setVideoEncodingBitRate(2 * 1024 * 512);
        } else {
            mMediaRecorder.setVideoEncodingBitRate(1 * 1024 * 512);
        }

//        if (mCurCamera == Camera.CameraInfo.CAMERA_FACING_BACK) {
//            mMediaRecorder.setOrientationHint(90);     // 输出旋转 90 度，保持竖屏录制
//        } else if (mCurCamera == Camera.CameraInfo.CAMERA_FACING_FRONT) {
//            mMediaRecorder.setOrientationHint(270);     // 输出旋转 270 度，保持竖屏录制
//        }

        mMediaRecorder.setOrientationHint(90);
        if (recordFile == null) {
            recordFile = createSaveVideoFile();
        }
        mMediaRecorder.setOutputFile(recordFile);
        mMediaRecorder.prepare();
        mMediaRecorder.start();
    }

    /**
     * 录制错误监听
     * @return
     */
    private MediaRecorder.OnErrorListener onErrorListener() {
        return new MediaRecorder.OnErrorListener() {
            @Override
            public void onError(MediaRecorder mediaRecorder, int i, int i1) {
                if (mOnErrorListener != null) {
                    mOnErrorListener.onError(mediaRecorder, i, i1);
                }
            }
        };
    }

    /**
     * 开始录制
     */
    public void startRecord() {
        // 视频文件保存路径
        recordFile = createSaveVideoFile();

        try {
            if (!mIsOpenCamera) {
                initCamera();
            }
            initRecord();
        } catch (IOException e) {
            e.printStackTrace();
            releaseRecord();
            freeCameraResource();
        } catch (Exception e) {
            e.printStackTrace();
            releaseRecord();
            freeCameraResource();
        }

        // 计时
        mCurRecordTime = 0;
        // 设置倒计时结束不取消
        isCancelTimeOut = false;
        final int intervalTime = mRecordTime * 1000 / 100;
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mCurRecordTime += intervalTime;
                if (mOnVideoRecorderListener != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mOnVideoRecorderListener.onRecorderProgress(VideoRecorderView.this, mCurRecordTime / intervalTime);
                        }
                    });
                }
                if (mCurRecordTime >= mRecordTime * 1000) {
                    // 达到指定录制时间，停止录制
                    if (isCancelTimeOut) {
                        cancelRecord();
                    } else {
                        stopRecord();
                    }
                }
            }
        }, 0, intervalTime);
    }

    /**
     * 停止录制
     */
    public void stopRecord() {
        stopTimer();
        releaseRecord();
        freeCameraResource();
        if (mOnVideoRecorderListener != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mOnVideoRecorderListener.onComplete(VideoRecorderView.this, recordFile, mCurRecordTime / (mRecordTime * 1000 / 100),mCurRecordTime/1000);
                    recordFile = null;
                    mCurRecordTime = 0;
                }
            });
        }
    }

    /**
     * 取消录制
     */
    public void cancelRecord() {
        // 先停止录制
        stopTimer();
        releaseRecord();
        freeCameraResource();
        try {
            // 删除录制文件
            new File(recordFile).delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mOnVideoRecorderListener != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mOnVideoRecorderListener.onCancel(VideoRecorderView.this);
                    // 重新初始化相机
                    try {
                        initCamera();
                    } catch (IOException e) {
                        L.e("释放录制资源报错",e.toString());
                        T.showShort(ApplicationBase.mApplicationBase,"录制视频保存出错!");
                    }
                    recordFile = null;
                    mCurRecordTime = 0;
                }
            });
        }
    }

    /**
     * 释放录制资源
     */
    private void releaseRecord() {
        if (mMediaRecorder != null) {
            mMediaRecorder.setOnErrorListener(null);    // 设置后防止崩溃
            mMediaRecorder.setPreviewDisplay(null);
            try{
                mMediaRecorder.stop();
                mMediaRecorder.release();
            }catch (Exception e){
                L.e("释放录制资源报错",e.toString());
            }
            mMediaRecorder = null;
        }
    }

    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }


    /**
     * 创建录制文件保存路径
     * @return
     */
    private String createSaveVideoFile() {
        String p = createSaveVideoFiles();
        return IsNullUtils.isNulls(p)?CacheFolderUtils.getCacheFolderUtils().getCacheTopDirectory():p;
    }

    /**
     * 创建录制文件保存路径
     * @return
     */
    private String createSaveVideoFiles() {
        String takeVideoFile = CacheFolderUtils.getCacheFolderUtils().setCropCacheFolderBackStr(CacheFolderUtils.CacheTopDirectoryName+"/Video");

        if(!takeVideoFile.endsWith("/")){
            takeVideoFile = takeVideoFile +"/";
        }

        File path = createRecordDir(takeVideoFile);
        if(path == null){
            return "";
        }else{
            return createRecordDir(takeVideoFile).getAbsolutePath();
        }
    }

    private File createRecordDir(String str) {
        File sampleDir = new File(str);
        if (!sampleDir.exists()) {
            sampleDir.mkdirs();
        }
        File mRecordFile = null;
        // 创建文件
        try {
            mRecordFile = File.createTempFile(getVideoFileName(), ".mp4", sampleDir); //mp4格式
        } catch (IOException e) {
            L.e("创建视频存放文件夹出错",e.toString());
        }
        return mRecordFile;
    }

    private String getVideoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("'MP_4'_yyyyMMdd_HHmmss");
        return sdf.format(date);
//        return "MP_4_ChaoEr_WeiXiu";
    }


    /**
     * 录制监听器
     */
    private OnVideoRecorderListener mOnVideoRecorderListener = null;

    /**
     * 设置录制监听
     * @param listener
     */
    public void setOnVideoRecorderListener(OnVideoRecorderListener listener) {
        mOnVideoRecorderListener = listener;
    }

    /**
     * 录制错误监听器
     */
    private MediaRecorder.OnErrorListener mOnErrorListener = null;

    /**
     * 设置录制错误监听
     * @param listener
     */
    public void setOnRecorderErrorListener(OnRecorderErrorListener listener) {
        mOnErrorListener = listener;
    }

    private boolean isAudio = true;
    public void setIsAudio(boolean isAudio) {
        this.isAudio = isAudio;
    }
}