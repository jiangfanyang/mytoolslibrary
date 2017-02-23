package com.ltf.mytoolslibrary.viewbase.CacheFolder;

import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.text.TextUtils;
import android.util.Log;

import com.ltf.mytoolslibrary.viewbase.app.ApplicationBase;
import com.ltf.mytoolslibrary.viewbase.isnull.IsNullUtils;
import com.ltf.mytoolslibrary.viewbase.utils.FileUtil;
import com.ltf.mytoolslibrary.viewbase.utils.show.L;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 作者：${李堂飞} on 2016/11/24 0024 10:29
 * 邮箱：1195063924@qq.com
 * 注解: 缓存文件夹设置
 *
 * 使用方法: 设置缓存路径
 * CacheFolderUtils.getCacheFolderUtils().CacheFolderBianLiangClear();
 * Stringpath = CacheFolderUtils.getCacheFolderUtils().setCropCacheFolderBackFile(context,cropCacheFolderName);
 *
 * 清空缓存方法:
 * CacheFolderUtils.getCacheFolderUtils().clearCache(Context a,String dirPath)
 *
 * 得到缓存根目录方法:
 *
 * CacheFolderUtils.getCacheFolderUtils().getCacheTopDirectory(Context activity)
 */
public class CacheFolderUtils {
    private static CacheFolderUtils mCacheFolder;
    public static String CacheTopDirectoryName = ApplicationBase.CrashErrorMessageName;
    public static CacheFolderUtils getCacheFolderUtils(){
        if(mCacheFolder == null){
            mCacheFolder = new CacheFolderUtils();
        }
        return mCacheFolder;
    }
    private CacheFolderUtils(){
    }

    private String SD_PATH = Environment.getExternalStorageDirectory()
            .getPath();
    private String sd_path2 = Environment.getRootDirectory().getPath();

    /***用户设置的缓存全路径*/
    private String CacheFolderss = "";

    /**
     * 得到缓存根目录
     */
    public String getCacheTopDirectory(){
        String SD_PATHs = "";
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)){
            SD_PATHs = SD_PATH;
        }else{
            SD_PATHs = sd_path2;
        }

        if(IsNullUtils.isNulls(SD_PATHs)){
            SD_PATHs = getPathAll();
        }
        return SD_PATHs;
    }

    /**设置本地测试路径 次集合将来会做遍历取出可用的储存路径作为数据保存路径**/
    public CacheFolderUtils setLocaPath(List<String> mVoldPublic){
        if(mVold == null || mVold.size() == 0){
            initLocaPath();
        }
        for (int i=0;i<mVoldPublic.size();i++){
            if(!mVold.contains(mVoldPublic.get(i)) && !IsNullUtils.isNulls(mVoldPublic.get(i))){
                mVold.add(mVoldPublic.get(i));
            }
        }

        return this;
    }

    /**初始化本地测试路径**/
    private static List<String> initLocaPath(){
        List<String> mVolds = null;
        if(mVolds == null){
            mVolds = new ArrayList<>();
        }
        mVolds.clear();
        mVolds.add("/mnt/sdcard");
        mVolds.add("/mnt/external_sd");
        mVolds.add("/mnt/sdcard2");
        mVolds.add("/storage/emulated/0");
        mVolds.add("/storage/emulated/1");
        mVolds.add("/storage/sdcard0");
        mVolds.add("/storage/sdcard1");

        return mVolds;
    }

    private static List<String> mVold = initLocaPath();
    /**得到本地测试路径 次集合将来会做遍历取出可用的储存路径作为数据保存路径**/
    public List<String> getLocaPath(){
        return mVold;
    }
    private String getPathAll(){
        if(mVold == null || mVold.size() == 0){
            initLocaPath();
        }
        StorageManager storageManager = (StorageManager) ApplicationBase.mApplicationBase.getSystemService(Context.STORAGE_SERVICE);
        try {
            Class<?>[] paramClasses = {};
            Method getVolumePathsMethod = StorageManager.class.getMethod("getVolumePaths", paramClasses);
            getVolumePathsMethod.setAccessible(true);
            Object[] params = {};
            Object invoke = getVolumePathsMethod.invoke(storageManager, params);
            for (int i = 0; i < ((String[])invoke).length; i++) {
                mVold.add(((String[])invoke)[i]);
            }
        } catch (Exception e1) {
            L.e("获取设备所有能用的储存路径失败",e1.toString());
        }

        for (int i=0;i<mVold.size();i++){
            String path = mVold.get(i);
            if(IsNullUtils.isNulls(path)){
                File file = new File(path);
                if(file.isDirectory() && file.canWrite()){
                    return path;
                }
            }
        }
        return ApplicationBase.mApplicationBase.getApplicationContext().getFilesDir().getAbsolutePath();
    }

    /**
     * 根据用户喜好设置
     * 设置保存文件夹名字  格式  /mytoolslibrary/CacheFolders/
     * 默认将缓存放入/mytoolslibrary/CacheFolders/目录下
     */
    public String setCropCacheFolderBackStr(String cropCacheFolderName){
        if(TextUtils.isEmpty(cropCacheFolderName)){
            cropCacheFolderName = CacheTopDirectoryName;
        }else{
            cropCacheFolderName = formatCacheFolderNameStartEnd(cropCacheFolderName);
        }
        if(getCacheTopDirectory().endsWith("/")){
            CacheFolderss = getCacheTopDirectory()+cropCacheFolderName;
        }else{
            CacheFolderss = getCacheTopDirectory()+formatCacheFolderNameStartEnd(cropCacheFolderName);
        }
        File dir = new File(CacheFolderss);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        L.e("缓存文件夹",CacheFolderss);
        return CacheFolderss;
    }
    /**
     * 根据用户喜好设置
     * 设置保存文件夹名字  格式  /mytoolslibrary/CacheFolders/
     * 默认将缓存放入/mytoolslibrary/CacheFolders/目录下
     */
    public File setCropCacheFolderBackFile(String cropCacheFolderName){
        if(TextUtils.isEmpty(cropCacheFolderName)){
            cropCacheFolderName = CacheTopDirectoryName;
        }else{
            cropCacheFolderName = formatCacheFolderNameStartEnd(cropCacheFolderName);
        }
        if(getCacheTopDirectory().endsWith("/")){
            CacheFolderss = getCacheTopDirectory()+cropCacheFolderName;
        }else{
            CacheFolderss = getCacheTopDirectory()+formatCacheFolderNameStartEnd(cropCacheFolderName);
        }
        File dir = new File(CacheFolderss);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        L.e("缓存文件夹",CacheFolderss);
        return dir;
    }

    /**
     * 设置毕加索 网络请求框架的缓存路径
     * 首先我们要提供了一个文件路径用来存放缓存，出于安全性的考虑，在Android中我们推荐使用Context.getExternalCacheDir()来作为缓存的存放路径，另外还需要指定缓存的大小就可以创建一个缓存了
     */
    public String setPicassoCashPath(){
//        CacheFolderss = getCashPath();
//        String CacheFolderNames = CacheFolderss +"PicassoCash/";
//        Picasso picasso = new Picasso.Builder(ApplicationBase.mApplicationBase.getApplicationContext()).downloader(
//                new OkHttpDownloader(new File(CacheFolderNames))).build();
//        Picasso.setSingletonInstance(picasso);
        return ApplicationBase.mApplicationBase.getApplicationContext().getApplicationContext().getExternalCacheDir().getPath() + "/picasso-cache";
//        return CacheFolderNames;
    }

    /**
     * 格式化缓存文件名字 保持首尾包含"/"
     * @param CacheFolderName
     * @return
     */
    public String formatCacheFolderNameStartEnd(String CacheFolderName) {
        if(!CacheFolderName.startsWith("/")){
            CacheFolderName = "/"+CacheFolderName;
        }
        if(!CacheFolderName.endsWith("/")){
            CacheFolderName = CacheFolderName + "/";
        }
        return CacheFolderName;
    }

    private boolean isClearComplete = false;
    public String getOkhttpCashPath(){//清理okhttp缓存还有问题 清理完了会报llegalStateException: cache is closed
        return CacheFolderss = getCashPath() +"OkHttpCash/";
    }
    /**
     * 缓存是否清理完成
     * @param activity
     * @param url
     * @return
     */
    public boolean isClearCacheComplete(Context activity,String... url){
        this.isClearComplete = false;
        //以下是删除用户设置缓存目录下的所有内容
        String daiQingKongPath = "";

        daiQingKongPath = getCashPath();

        //以下是删除毕加索缓存目录下的所有内容
        String cache = setPicassoCashPath();

        //以下是删除oktthp缓存目录下的所有内容
        String OkHttpCash = getOkhttpCashPath();

        for (int i=0;i<url.length;i++){
            L.e("缓存清空-"+url[i],"用户指定Url是否清空:"+new File(url[i]).listFiles());
            if((new File(url[i]).listFiles()==null?true:new File(url[i]).listFiles().length == 0)){
                this.isClearComplete = true;
            }else{
                this.isClearComplete = false;
            }
        }

        //本应用内部缓存(/data/data/com.xxx.xxx/cache)
        File appCache = activity.getCacheDir();
        ///data/data/com.xxx.xxx/files下的内容
//        File appfiles = activity.getFilesDir();

        L.e("缓存清空-"+daiQingKongPath,"用户设置缓存目录是否清空:"+new File(daiQingKongPath).listFiles());
        L.e("缓存清空-"+cache,"毕加索缓存目录是否清空:"+new File(cache).listFiles());
        L.e("缓存清空-"+OkHttpCash,"okhttp缓存目录是否清空:"+new File(OkHttpCash).listFiles());
        L.e("缓存清空-"+appCache,"本应用内部缓存(/data/data/com.xxx.xxx/cache)缓存目录是否清空:"+appCache.listFiles());
//        L.e("缓存清空-"+appfiles,"/data/data/com.xxx.xxx/files下的内容缓存目录是否清空:"+appfiles.listFiles());
        if((new File(daiQingKongPath).listFiles() ==null) &&
                (new File(cache).listFiles()==null)
                &&(new File(OkHttpCash).listFiles()==null)
                &&(appCache.listFiles()==null)
//                &&(appfiles.listFiles()==null)
                ){
            this.isClearComplete = true;
        }else{
            this.isClearComplete = false;
        }

        //外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            if(activity.getExternalCacheDir().listFiles()==null){
                this.isClearComplete = true;
            }else{
                this.isClearComplete = false;
            }
            L.e("缓存清空-"+activity.getExternalCacheDir(),"外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)下的内容缓存目录是否清空:"+activity.getExternalCacheDir().listFiles());
        }

        L.e("缓存清空-是否满足",this.isClearComplete!=true?"准备清理缓存":"没有缓存可以清理");
        return this.isClearComplete;
    }

    /**
     * 得到缓存根目录 首尾已经有“/”  类似：mnt/0/zerolife/
     * @return
     */
    private String getCashPath() {
        String daiQingKongPath;
        if(getCacheTopDirectory().endsWith("/")){
            daiQingKongPath = getCacheTopDirectory() + CacheTopDirectoryName;
        }else{
            daiQingKongPath = getCacheTopDirectory() + formatCacheFolderNameStartEnd(CacheTopDirectoryName);
        }

        if(!daiQingKongPath.endsWith("/")){
            daiQingKongPath = daiQingKongPath+"/";
        }
        return daiQingKongPath;
    }

    /**
     * 清空毕加索缓存的图片
     * 本方法清空原则: 优先清空用户指定目标目录文件下的所有文件
     * 框架默认清空路径如下:
     * 1.okgo网络请求框架下所有缓存内容 不提供设置自定义路径 安全考虑
     * 2.毕加索图片缓存框架下所有内容 不提供设置自定义路径 安全考虑  注意特别是 journal 删除了的话  它就再也不缓存了
     * 3.用户在Application中设置的缓存文件夹下CacheFolders文件夹中的所有文件
     * 建议:删除缓存是耗时操作 可放在线程中执行
     * @param a
     * @param dirPath 需要清空的目标路径  例如:/mnt/zerolife/picImage/  标志着清空picImage文件下的所有文件
     */
    public void clearCache(Context a,onBackClearCacheLisnner monBackClearCacheLisnner,String... dirPath){
        if(isClearCacheComplete(a,dirPath)){
            if(monBackClearCacheLisnner != null){
                monBackClearCacheLisnner.isClearCacheComplete(true);
                return;
            }
        }

        if(monBackClearCacheLisnner != null){
            monBackClearCacheLisnner.onBackClearCacheStart();
        }

        //
        try {
            for (int i=0;i<dirPath.length;i++){
                L.e("缓存清理路径",dirPath[i]);
                if(!TextUtils.isEmpty(dirPath[i])){
                    deleteFile(new File(dirPath[i]));
                }
            }

            //本应用内部缓存(/data/data/com.xxx.xxx/cache)
            DataCleanManager.cleanInternalCache(a);
            ///data/data/com.xxx.xxx/files下的内容
//            DataCleanManager.cleanFiles(a);
            //清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)
            DataCleanManager.cleanExternalCache(a);

            //以下是删除用户设置缓存目录下的所有内容 除缓存错误文件日志
            deleteFile(new File(getCashPath()));

            //以下是删除okgo网络请求框架下所有缓存内容 未实现
            //以下是删除oktthp缓存目录下的所有内容
            deleteFile(new File(getOkhttpCashPath()));

            if(!(OkHttpUtils.getInstance().getOkHttpClient().cache() == null)){
                OkHttpUtils.getInstance().getOkHttpClient().cache().delete();
            }
            //以下是删除毕加索缓存目录下的所有内容
            String paths = setPicassoCashPath();
            //然后删除这个路径里的文件
            deleteFile(new File(paths));
            if(monBackClearCacheLisnner != null){
                monBackClearCacheLisnner.onBackClearCacheFinnsh();
            }
        } catch (Exception e) {
            L.e("清理缓存失败",e.toString());
            if(monBackClearCacheLisnner != null){
                monBackClearCacheLisnner.isClearCacheComplete(false);
            }
        }
    }

    public interface onBackClearCacheLisnner{
        void onBackClearCacheFinnsh();
        void onBackClearCacheStart();
        void isClearCacheComplete(boolean isClear);
    }

    /**
     * 根据指定路径删除路径下所有文件包括文件夹
     */
    public void deleteFile(File root) {
        File files[] = root.listFiles();
        if (files != null)
            for (File f : files) {
                if (f.isDirectory()) { // 判断是否为文件夹
                    //以下注释处是不允许删除错误日志文件的
//                    if("CrashError".equals(f.getName()+"")){
//                        L.e("清理缓存中--","文件夹:"+f.getName()+"---失败,缓存日志不允许删除!");
//                    }else{
//                        deleteFile(f);
//                        try {
//                            f.delete();
//                            L.e("清理缓存中--","文件夹:"+f.getName()+"---删除成功!");
//                        } catch (Exception e) {
//                            L.e("清理缓存中--","文件夹:"+f.getName()+"---删除失败!"+e.toString());
//                        }
//                    }

                    deleteFile(f);
                    try {
                        f.delete();
                        L.e("清理缓存中--","文件夹:"+f.getName()+"---删除成功!");
                    } catch (Exception e) {
                        L.e("清理缓存中--","文件夹:"+f.getName()+"---删除失败!"+e.toString());
                    }
                } else {
                    if (f.exists()) { // 判断是否文件存在
                        if (f.getName().equals("journal")) {//特别是 journal 删除了的话  它就再也不缓存了
                            L.e("清理缓存中--",""+f.getName()+"--失败--journal 删除了的话  它就再也不缓存了!");
                        } else {
                            deleteFile(f);
                            try {
                                f.delete();
                                L.e("清理缓存中--","文件:"+f.getName()+"---删除成功!");
                            } catch (Exception e) {
                                L.e("清理缓存中--","文件:"+f.getName()+"---删除失败!"+e.toString());
                            }
                        }
                    }
                }
            }
    }

    /**得到缓存大小  计算所有需要清理缓存的地方的总额**/
    public String getCacheSize(Context context, String... filepath) {
        long CacheSizes = 0;
        try {
            //以下是删除用户设置缓存目录下的所有内容
            String daiQingKongPath = "";
            daiQingKongPath = getCashPath();
            long CacheSize = DataCleanManager.getFolderSize(new File(daiQingKongPath));
            CacheSizes += CacheSize;
            L.e("计算缓存大小", "用户设置缓存目录下的所有内容缓存大小(包含错误日志):"+CacheSize);
        } catch (Exception e) {
            L.e("计算缓存大小", "用户设置缓存目录下的所有内容缓存大小(包含错误日志)"+e.toString());
        }
        try {
            //以下是删除毕加索缓存目录下的所有内容
            String cache = setPicassoCashPath();
            long CacheSize = DataCleanManager.getFolderSize(new File(cache));
            CacheSizes += CacheSize;
            L.e("计算缓存大小", "毕加索缓存目录下的所有内容缓存大小:"+CacheSize);
        } catch (Exception e) {
            L.e("计算缓存大小", "毕加索缓存目录下的所有内容缓存大小"+e.toString());
        }
        try {
            //以下是删除oktthp缓存目录下的所有内容
            String OkHttpCash = getOkhttpCashPath();
            long CacheSize = DataCleanManager.getFolderSize(new File(OkHttpCash));
            CacheSizes += CacheSize;
            L.e("计算缓存大小", "oktthp缓存目录下的所有内容缓存大小:"+CacheSize);
        } catch (Exception e) {
            L.e("计算缓存大小", "oktthp缓存目录下的所有内容缓存大小"+e.toString());
        }
        try {
            //本应用内部缓存(/data/data/com.xxx.xxx/cache)
            long CacheSize = DataCleanManager.getFolderSize(context.getCacheDir());
            CacheSizes += CacheSize;
            L.e("计算缓存大小", "本应用内部缓存(/data/data/com.xxx.xxx/cache)缓存大小:"+CacheSize);
        } catch (Exception e) {
            L.e("计算缓存大小", "本应用内部缓存(/data/data/com.xxx.xxx/cache)缓存大小"+e.toString());
        }
//        try {
//            ///data/data/com.xxx.xxx/files下的内容
//            long CacheSize = DataCleanManager.getFolderSize(context.getFilesDir());
//            CacheSizes += CacheSize;
//            L.e("计算缓存大小", "/data/data/com.xxx.xxx/files下的内容缓存大小:"+CacheSize);
//        } catch (Exception e) {
//            L.e("计算缓存大小", "data/data/com.xxx.xxx/files下的内容缓存大小"+e.toString());
//        }
        try {
            //外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                long CacheSize = DataCleanManager.getFolderSize(context.getExternalCacheDir());
                CacheSizes += CacheSize;
                L.e("计算缓存大小", "外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)缓存大小:"+CacheSize);
            }
        } catch (Exception e) {
            L.e("计算缓存大小", "外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)缓存大小"+e.toString());
        }
        try {
            if(!IsNullUtils.isNulls(filepath)){
                long CacheSize = 0;
                for (int i = 0; i < filepath.length; i++) {
                    CacheSize += DataCleanManager.getFolderSize(new File(filepath[i]));
                }
                CacheSizes += CacheSize;
                L.e("计算缓存大小", "外部制定文件夹下的缓存大小:"+CacheSize);
            }
        } catch (Exception e) {
            L.e("计算缓存大小", "外部制定文件夹下的缓存大小"+e.toString());
        }
        return DataCleanManager.getFormatSize(CacheSizes);
    }
}
