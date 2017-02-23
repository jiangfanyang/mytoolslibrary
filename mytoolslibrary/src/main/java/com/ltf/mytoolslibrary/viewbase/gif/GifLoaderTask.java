package com.ltf.mytoolslibrary.viewbase.gif;

import android.content.Context;
import android.os.AsyncTask;

import com.ltf.mytoolslibrary.viewbase.CacheFolder.CacheFolderUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 作者：${李堂飞} on 2017/1/5 10:27
 * 邮箱：1195063924@qq.com
 * 注解:
 */

public class GifLoaderTask extends AsyncTask<String, Void, String> {
    private final Context context;

    private GifDrawer gifDrawer;

    public GifLoaderTask(GifDrawer gifDrawer, Context context) {
        this.gifDrawer = gifDrawer;
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        FileOutputStream fops = null;
        InputStream is = null;
        try {
            is = HttpLoader.getInputStreanFormUrl(params[0]);
//            String path = context.getCacheDir().getPath() + File.separator + GifDecoder.getGifImagePath(params[0]);
            String paths = CacheFolderUtils.getCacheFolderUtils().CacheTopDirectoryName +"/Gif/";
            String path = CacheFolderUtils.getCacheFolderUtils().setCropCacheFolderBackStr(paths);
            if(path.endsWith("/")){
                path = path + GifDecoder.getGifImagePath(params[0]);
            }else{
                path = path + "/" + GifDecoder.getGifImagePath(params[0]);
            }
            File file = new File(path);
            fops = new FileOutputStream(file);
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = is.read(buffer)) != -1) {
                fops.write(buffer, 0, len);
            }
            return params[0];
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fops != null) {
                    fops.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (s != null) {
            GifDecoder.with(context).load(s).into(gifDrawer.getImageView());
        }
    }
}
