package com.ltf.mytoolslibrary.viewbase.mvp;

import android.app.Activity;

import com.ltf.mytoolslibrary.viewbase.CacheFolder.CacheFolderUtils;
import com.ltf.mytoolslibrary.viewbase.app.ApplicationBase;
import com.ltf.mytoolslibrary.viewbase.isnull.IsNullUtils;
import com.ltf.mytoolslibrary.viewbase.utils.NetworkUtils;
import com.ltf.mytoolslibrary.viewbase.utils.SharedPreferencesHelper;
import com.ltf.mytoolslibrary.viewbase.utils.show.L;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.MemoryCookieStore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * https://github.com/hongyangAndroid/okhttputils
 */
public class GetNetWorkDataImpl {

    /** 按照HTTP协议的默认缓存规则，例如有304响应头时缓存 */
    public static int DEFAULT = 1;

    /** 不使用缓存 */
    public static int NO_CACHE = 2;

    /** 请求网络失败后，读取缓存 */
    public static int REQUEST_FAILED_READ_CACHE = 3;

    /** 如果缓存不存在才请求网络，否则使用缓存 */
    public static int IF_NONE_CACHE_REQUEST = 4;

    /** 先使用缓存，不管是否存在，仍然请求网络 */
    public static int FIRST_CACHE_THEN_REQUEST = 5;
    private String urlJsonName = "";
    private boolean isFIRST_CACHE_THEN_REQUEST = false;
    private String netWorkCachPath = "";

    public static final long CACHE_NEVER_EXPIRE = -1;        //缓存永不过期
    /** 全局的缓存过期时间 */
    private static long cacheTime = -1;
    public static void setCacheTime(long cacheTimes){
        if (cacheTime <= -1) cacheTime = CACHE_NEVER_EXPIRE;
            cacheTime = cacheTimes;
        SharedPreferencesHelper.saveSharedPreferencestStringUtil(ApplicationBase.mApplicationBase,"DESCs", Activity.MODE_PRIVATE,""+System.currentTimeMillis());
    }

    /** 获取全局的缓存过期时间 */
    public long getCacheTime() {
        return cacheTime;
    }

    /**
     * @param cacheTime 允许的缓存时间
     * @param baseTime  基准时间,小于当前时间视为过期
     * @return 是否过期
     */
    public boolean checkExpire(long cacheTime, long baseTime) {
        if (cacheTime == CACHE_NEVER_EXPIRE) return false;
        return getLocalExpire() + cacheTime < baseTime;
    }

    public long getLocalExpire() {
        return Long.parseLong(SharedPreferencesHelper.getSharedPreferencestStringUtil(ApplicationBase.mApplicationBase, "DESCs", Activity.MODE_PRIVATE, ""+System.currentTimeMillis()));
    }

    /***初始化OkHttpClient*/
    public static OkHttpClient init(final long CacheTime){
        CookieJarImpl cookieJar = new CookieJarImpl(new MemoryCookieStore());
        String daiQingKongPath = "";
        if(CacheFolderUtils.getCacheFolderUtils().getCacheTopDirectory().endsWith("/")){
            daiQingKongPath = CacheFolderUtils.getCacheFolderUtils().getCacheTopDirectory() + ApplicationBase.CrashErrorMessageName;
        }else{
            daiQingKongPath = CacheFolderUtils.getCacheFolderUtils().getCacheTopDirectory() + CacheFolderUtils.getCacheFolderUtils().formatCacheFolderNameStartEnd(ApplicationBase.CrashErrorMessageName);
        }
        File httpCacheDirectory = new File(daiQingKongPath, "OkHttpCash");
        if (!httpCacheDirectory.exists()) {
            httpCacheDirectory.mkdirs();
        }
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(15000L, TimeUnit.MILLISECONDS)
                .readTimeout(15000L, TimeUnit.MILLISECONDS)
                .cookieJar(cookieJar)//清理okhttp缓存还有问题 清理完了会报llegalStateException: cache is closed
//                .cache(new okhttp3.Cache(httpCacheDirectory, 1024 * 1024 * 10))
//                .addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        Request request = chain.request();
//                        Response response = null;
//                        try{
//                            response = chain.proceed(request);
//                            response.newBuilder()
//                                    .removeHeader("Pragma") //缓存CacheTime时间后失效
//                                    .header("Cache-Control", "public, max-age=" + CacheTime)
//                                    .build();
//                        }catch (Exception e){
//                            L.e("设置缓存失效时间","--->失败!"+e.toString());
//                            response = chain.proceed(request);
//                        }
//                        return response;
//                    }
//                })
                //其他配置
                .build();

        GetNetWorkDataImpl.setCacheTime(CacheTime);//缓存5分钟
        OkHttpUtils.initClient(okHttpClient);
        return okHttpClient;
    }

    /**初始化OkHttpUtils**/
    public static void initClient(OkHttpClient okHttpClient){
        OkHttpUtils.initClient(okHttpClient);
    }

    /**
     * 全局网络请求框架
     **/
    public void onInitData(final ICallBackListener<String> iCallBackListener, final Map<String, String> params, final String urls, final int mCacheMode) {
        if(urls.lastIndexOf("/") !=-1){
            urlJsonName = urls.substring(urls.lastIndexOf("/")+1);
        }
        if(CacheFolderUtils.getCacheFolderUtils().getCacheTopDirectory().endsWith("/")){//创建缓存文件夹
            File dir = new File(CacheFolderUtils.getCacheFolderUtils().getCacheTopDirectory()+ CacheFolderUtils.getCacheFolderUtils().CacheTopDirectoryName +"/netWorkCach/");
            if(mCacheMode != NO_CACHE && mCacheMode != DEFAULT && getCacheTime() != 0 && getCacheTime() != -1){
                if (dir.exists()){
                    long timeMillis = System.currentTimeMillis();
                    if(checkExpire(getCacheTime(),timeMillis)){//检查是否满足清缓存条件
                        CacheFolderUtils.getCacheFolderUtils().deleteFile(new File(dir.getAbsolutePath()));
//                        CacheFolderUtils.getCacheFolderUtils().deleteFile(new File(CacheFolderUtils.getCacheFolderUtils().getOkhttpCashPath()));
                    }
                }
            }
            if (!dir.exists()) {
                dir.mkdirs();
            }
            netWorkCachPath = dir.getPath();
        }else{
            File dir = new File(CacheFolderUtils.getCacheFolderUtils().getCacheTopDirectory()+"/"+ CacheFolderUtils.getCacheFolderUtils().CacheTopDirectoryName +"/netWorkCach/");
            if(mCacheMode != NO_CACHE && mCacheMode != DEFAULT && getCacheTime() != 0 && getCacheTime() != -1){
                if (dir.exists()){
                    long timeMillis = System.currentTimeMillis();
                    if(checkExpire(getCacheTime(),timeMillis)){//
                        CacheFolderUtils.getCacheFolderUtils().deleteFile(new File(dir.getAbsolutePath()));
//                        CacheFolderUtils.getCacheFolderUtils().deleteFile(new File(CacheFolderUtils.getCacheFolderUtils().getOkhttpCashPath()));
                    }
                }
            }
            if (!dir.exists()) {
                dir.mkdirs();
            }
            netWorkCachPath = dir.getPath();
        }

        if(SharedPreferencesHelper.getSharedPreferencesBooleanUtil(ApplicationBase.mApplicationBase,"isKeQuXiaoNotice",0,false)){
            if (iCallBackListener != null)
                iCallBackListener.showFailed("网络请求失败，请稍候重试..");
            return;
        }
        L.e("当前缓存模式", "---->" + mCacheMode);

        isFIRST_CACHE_THEN_REQUEST = false;

        if(IF_NONE_CACHE_REQUEST == mCacheMode ||//如果缓存不存在才请求网络，否则使用缓存
                FIRST_CACHE_THEN_REQUEST == mCacheMode){//先使用缓存，不管是否存在，仍然请求网络
            readcash(iCallBackListener, mCacheMode, new isNetWorkLisnner() {
                @Override
                public void onisNetWork() {
                    okhttp(urls, params, mCacheMode, iCallBackListener);
                }
            });
        }else{
            okhttp(urls, params, mCacheMode, iCallBackListener);
        }
    }

    private void okhttp(String urls, Map<String, String> params, final int mCacheMode, final ICallBackListener<String> iCallBackListener) {
        if (NetworkUtils.isNetworkAvailable(ApplicationBase.mApplicationBase)){

        }else {
            if(mCacheMode == DEFAULT || mCacheMode == NO_CACHE){//不使用缓存
                if (iCallBackListener != null){
                    iCallBackListener.showFailed("网络请求失败，请稍候重试..");
                    return;}
            }

            if(IF_NONE_CACHE_REQUEST == mCacheMode){//如果缓存不存在才请求网络，否则使用缓存
                if (iCallBackListener != null){
                    iCallBackListener.showFailed("网络请求失败，请稍候重试..");
                    return;}
            }

            if(FIRST_CACHE_THEN_REQUEST == mCacheMode) {//先使用缓存，不管是否存在，仍然请求网络)
                return;
            }

            if(mCacheMode == REQUEST_FAILED_READ_CACHE){//请求网络失败后，读取缓存
                readcash(iCallBackListener,mCacheMode,null);
                return;
            }
        }
        OkHttpUtils
                .post()
                .url(urls)
                .tag(urls)//
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        L.e("GetNetWorkDataImpl_onError", "" + e.toString());
                        if(mCacheMode == DEFAULT || mCacheMode == NO_CACHE){//不使用缓存
                            if (iCallBackListener != null)
                                iCallBackListener.showFailed("网络请求失败，请稍候重试..");
                        }else if(mCacheMode == REQUEST_FAILED_READ_CACHE){//请求网络失败后，读取缓存
                            readcash(iCallBackListener,mCacheMode,null);
                        }else{
                            if (iCallBackListener != null)
                                iCallBackListener.showFailed("网络请求失败，请稍候重试..");
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        L.e("GetNetWorkDataImpl_onResponse", "" + response);
//                        SharedPreferencesHelper.saveSharedPreferencestStringUtil(ApplicationBase.mApplicationBase,urlJsonName, Activity.MODE_PRIVATE,response);
                        if(mCacheMode == DEFAULT || mCacheMode == NO_CACHE){//不使用缓存
                            if (iCallBackListener != null)
                                iCallBackListener.initData(response);
                        }else if(FIRST_CACHE_THEN_REQUEST == mCacheMode){//先使用缓存，不管是否存在，仍然请求网络 此时如果先前读取缓存成功则不返回数据  如不成功则返回新的
                            saveAsFileWriter(netWorkCachPath,urlJsonName+".txt",response);
                            if(!isFIRST_CACHE_THEN_REQUEST){
                                if (iCallBackListener != null)
                                    iCallBackListener.initData(response);
                            }
                        }else {
                            saveAsFileWriter(netWorkCachPath,urlJsonName+".txt",response);
                            if (iCallBackListener != null)
                                iCallBackListener.initData(response);
                        }
                    }
                });
    }

    private interface isNetWorkLisnner{
        /**需要请求网络**/
        void onisNetWork();
    }

    private void readcash(final ICallBackListener<String> iCallBackListener, final int mCacheMode, final isNetWorkLisnner misNetWorkLisnner) {
        //                            String json = SharedPreferencesHelper.getSharedPreferencestStringUtil(ApplicationBase.mApplicationBase,"urlJsonName", Activity.MODE_PRIVATE,"");
        readFileByLines(netWorkCachPath, urlJsonName + ".txt", new readFileByLinesBackLisnner() {
            @Override
            public void readFileByLinesBack(String json) {
                if(IsNullUtils.isNulls(json)){
                    L.e("GetNetWorkDataImpl_readFileByLinesBack", "读取缓存:缓存为空");
                    if(IF_NONE_CACHE_REQUEST == mCacheMode ||//如果缓存不存在才请求网络，否则使用缓存
                            FIRST_CACHE_THEN_REQUEST == mCacheMode){//先使用缓存，不管是否存在，仍然请求网络
                        if(FIRST_CACHE_THEN_REQUEST == mCacheMode){
                            isFIRST_CACHE_THEN_REQUEST = false;
                        }
                        if(misNetWorkLisnner != null){
                            misNetWorkLisnner.onisNetWork();
                        }
                    }else{
                        if (iCallBackListener != null)
                            iCallBackListener.showFailed("网络请求失败，请稍候重试.");
                    }
                }else{
                    L.e("GetNetWorkDataImpl_readFileByLinesBack", json);
                    if(IF_NONE_CACHE_REQUEST == mCacheMode || FIRST_CACHE_THEN_REQUEST == mCacheMode){
                        if (iCallBackListener != null)
                            iCallBackListener.initData(json);

                        if(FIRST_CACHE_THEN_REQUEST == mCacheMode){
                            isFIRST_CACHE_THEN_REQUEST = true;
                            if(misNetWorkLisnner != null){
                                misNetWorkLisnner.onisNetWork();
                            }
                        }
                    } else {
                        if (iCallBackListener != null)
                            iCallBackListener.initData(json);
                    }
                }
            }

            @Override
            public void readFileError(Exception e) {
                if(e == null){
                    L.e("GetNetWorkDataImpl_readFileError", "读取缓存失败:文件路径不存在");
                }else{
                    L.e("GetNetWorkDataImpl_readFileError", "读取缓存失败:"+e.toString());
                }

                if(IF_NONE_CACHE_REQUEST == mCacheMode ||//如果缓存不存在才请求网络，否则使用缓存
                        FIRST_CACHE_THEN_REQUEST == mCacheMode){//先使用缓存，不管是否存在，仍然请求网络
                    if(FIRST_CACHE_THEN_REQUEST == mCacheMode){
                        isFIRST_CACHE_THEN_REQUEST = false;
                    }
                    if(misNetWorkLisnner != null){
                        misNetWorkLisnner.onisNetWork();
                    }
                }else{
                    if (iCallBackListener != null)
                        iCallBackListener.showFailed("网络请求失败，请稍候重试.");
                }
            }

            @Override
            public void startReadFile(String path) {
                L.e("GetNetWorkDataImpl_readFileError", "开始读取缓存:"+path);
            }
        });
    }

    //保存字符串到文件中
    public void saveAsFileWriter(String path,String name,String content) {

        if(!path.endsWith("/")){
            path = path+"/";
        }
        FileWriter fwriter = null;
        try {
            File dirs = new File(path);
            if(!dirs.exists()){
                dirs.mkdirs();
            }
            File dir = new File(path , name);
            if(!dir.exists()){
                dir.createNewFile();
            }
            fwriter = new FileWriter(path + name);
            if(fwriter != null){
                fwriter.write(content);
            }else{
                L.e("GetNetWorkDataImpl_saveAsFileWriter", "缓存保存失败:路径为空");
            }
        } catch (IOException ex) {
            L.e("GetNetWorkDataImpl_saveAsFileWriter", "缓存保存失败:"+ex.toString());
        } finally {
            try {
                if (fwriter != null) {
                    fwriter.flush();
                    fwriter.close();
                }else{
                    L.e("GetNetWorkDataImpl_saveAsFileWriter", "缓存保存失败:路径为空");
                }
            } catch (IOException ex) {
                L.e("GetNetWorkDataImpl_saveAsFileWriter", "缓存保存失败:"+ex.toString());
            }
        }
    }

    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public  void readFileByLines(String path,String name,readFileByLinesBackLisnner mreadFileByLinesBackLisnner) {
        if(!path.endsWith("/")){
            path = path+"/";
        }
        File file = new File(path + name);
        if(!file.exists()){
            if(mreadFileByLinesBackLisnner != null){
                mreadFileByLinesBackLisnner.readFileError(null);
            }
            return;
        }
        if(mreadFileByLinesBackLisnner != null){
            mreadFileByLinesBackLisnner.startReadFile(file.getPath());
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            String content = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                content=tempString;
                line++;
            }

            if(mreadFileByLinesBackLisnner != null){
                mreadFileByLinesBackLisnner.readFileByLinesBack(content);
            }
            reader.close();
        } catch (IOException e) {
            L.e("GetNetWorkDataImpl_readFileByLines", "读取缓存文件失败:"+e.toString());
            if(mreadFileByLinesBackLisnner != null){
                mreadFileByLinesBackLisnner.readFileError(e);
            }
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    L.e("GetNetWorkDataImpl_readFileByLines", "读取缓存文件失败:"+e1.toString());
                    if(mreadFileByLinesBackLisnner != null){
                        mreadFileByLinesBackLisnner.readFileError(e1);
                    }
                }
            }
        }
    }

    public interface readFileByLinesBackLisnner{
        void readFileByLinesBack(String message);
        void readFileError(Exception e);
        void startReadFile(String path);
    }
    /**
     * 全局网络下载 "gson-2.2.1.jar"
     * 子类回调downloadProgress 得到下载进度
     **/
    public void onDownLoadFile(String url, String destFileDir, String fileNmae, final GetNetWorkDataPresenter.FileCallBack m) {

        OkHttpUtils//
                .get()//
                .url(url)//
                .tag(url)//
                .build()//
                .execute(new FileCallBack(destFileDir, fileNmae) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        m.onError(e);
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        m.onResponse(response);
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        m.inProgress(progress);
                    }
                });
    }

    /**
     * 全局网络上传  单个文件 addFile的第一个参数为文件的key，即类别表单中<input type="file" name="mFile"/>的name属性
     * .addFile("mFile", "messenger_01.png", file)//
     * params.put("log",new File(fileName));
     * 子类回调upProgress 得到上传进度
     * params:代表其他参数
     * filePath:单个文件的路径
     * fileName:本次上传的参数名 服务器通过这个参数得到文件
     **/
    public void onUpFile(String url, Map<String, String> params, String filePath, String fileName, final StringCallbacks m) {
        OkHttpUtils//
                .post()//
                .url(url)//
                .tag(url)//
                .params(params)
                .addFile(fileName,getFileName(filePath), new File(filePath))
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if(m != null){
                            m.onError(e);
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if(m != null){
                            m.onResponse(response);
                        }
                    }
                });
    }

    /**
     * 全局网络上传  多个文件
     * params.put("log",new File(fileName));
     * params:代表其他参数
     * filePath:多个文件
     * fileName:本次上传的参数名  服务器通过这个参数得到集合List<File> filePath
     * 子类回调upProgress 得到上传进度
     **/
    public void onUpFile(String url, Map<String, String> params, List<File> filePath, String fileName, final StringCallbacks m) {
        PostFormBuilder mPostFormBuilder = OkHttpUtils//
                .post()//
                .url(url)//
                .tag(url)//
                .params(params);
        for (int i = 0; i < filePath.size(); i++) {
            mPostFormBuilder.addFile(fileName + i,getFileName(filePath.get(i).getPath().toString()), filePath.get(i));
        }
        mPostFormBuilder.build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if(m != null){
                            m.onError(e);
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if(m != null){
                            m.onResponse(response);
                        }
                    }
                });
    }

    /**得到文件名字全称带后缀**/
    public String getFileName(String pathandname){
        int start=pathandname.lastIndexOf("/");
        if(start!=-1){
            return pathandname.substring(start+1,pathandname.length());
        }else{
            return new File(pathandname).getName();
        }

    }

    /**取消请求**/
    public void canceOkHttpUtilsUrl(Object tag){
        OkHttpUtils.getInstance().cancelTag(tag);
    }

    /**检查当前请求是否在请求队列中,如果在队列中则不在请求直接return,如果不在则添加到请求队列**/
    public void checkNetHave(String urls){
        if(ApplicationBase.urlList != null){
            if(ApplicationBase.urlList.contains(urls)){
                L.e("网络请求队列","队列:"+urls+"已存在,请勿重复请求!");
                return;
            }else{
                L.e("网络请求队列","队列:"+urls+"已添加至网络请求队列中...");
                ApplicationBase.urlList.add(urls);
            }
        }
    }

    /**移除队列中存在的网络请求**/
    public void removeHavedNet(String urls){
        if(ApplicationBase.urlList != null && ApplicationBase.urlList.size() != 0){
            L.e("网络请求队列","队列:"+urls+"已从网络请求队列移除...");
            ApplicationBase.urlList.remove(urls);
        }
    }

}
