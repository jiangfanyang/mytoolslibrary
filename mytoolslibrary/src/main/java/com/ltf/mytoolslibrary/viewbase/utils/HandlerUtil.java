package com.ltf.mytoolslibrary.viewbase.utils;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.ltf.mytoolslibrary.viewbase.isnull.IsNullUtils;
import com.ltf.mytoolslibrary.viewbase.utils.show.L;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by 李堂飞 on 2016/3/20.
 */
public class HandlerUtil {
    /**
     * 在任何线程通过该方法更新UI
     *
     * @param r
     */
    public static void updateUIFromAnyThread(Runnable r) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(r);
    }

    private static Map<String, String> maps = new HashMap<>();
    public static Map<String, String> paramsIsNull(Map<String, String> params) {
        Iterator iterator = params.keySet().iterator();
        while (iterator.hasNext()) {//找出key中的value是否为空，为空的就删除
            String key = (String) iterator.next();
            L.e("key:"+key,"value:"+params.get(key));
            if ("".equals(params.get(key)+"") || TextUtils.isEmpty(key) || "null".equals(key+"")) {
                iterator.remove();
                params.remove(key);
            }
        }
        maps.clear();
        Iterator iterator1 = params.keySet().iterator();
        while (iterator1.hasNext()) {
            String key = (String) iterator1.next();
            if(!IsNullUtils.isNullss(params.get(key))){//只添加value不为空的
                L.e("重新格式化请求参数:","key:"+key+"--value:"+params.get(key));
                maps.put(key,params.get(key));
            }
        }
        return maps;
    }
}
