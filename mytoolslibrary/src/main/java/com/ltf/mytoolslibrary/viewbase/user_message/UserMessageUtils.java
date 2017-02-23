package com.ltf.mytoolslibrary.viewbase.user_message;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.ltf.mytoolslibrary.viewbase.utils.SharedPreferencesHelper;
import com.ltf.mytoolslibrary.viewbase.utils.show.L;

/**
 * 作者：${李堂飞} on 2016/8/3 0003 16:01
 * 邮箱：1195063924@qq.com
 * 注解: 操作用户的信息
 * U:代表用户信息Model
 */
public class UserMessageUtils<U> {

    private static UserMessageUtils mUserMessageUtils;
    public static UserMessageUtils getUserMessageUtils(){
        if(mUserMessageUtils == null){
            mUserMessageUtils = new UserMessageUtils();
        }
        return mUserMessageUtils;
    }
    private Gson gson;
    private UserMessageUtils(){
        gson = new Gson();
    }
    /**得到用户信息**/
    public U getLoginUserMessages(Context a,Class<U> c) {
        U u = null;
        String string = SharedPreferencesHelper
                .getSharedPreferencestStringUtil(a, "UserNameMesage2_0_zer_m",
                        Context.MODE_PRIVATE, "");
        if (!TextUtils.isEmpty(string + "")) {
            u = gson.fromJson(string, c);
        }else{
            return null;
        }
        return u;
    }

    /**设置用户信息
     * userMessage=null则表示清空操作 可以意味退出登录等
     * **/
    public void setLoginUserMessages(Context a,U userMessage) {
        if(userMessage == null){
            SharedPreferencesHelper.saveSharedPreferencestStringUtil(a,
                    "UserNameMesage2_0_zer_m", Context.MODE_PRIVATE, "");
        }else{
            String jsonMessage = gson.toJson(userMessage);
            L.e("数据保存",jsonMessage);
            SharedPreferencesHelper.saveSharedPreferencestStringUtil(a,
                    "UserNameMesage2_0_zer_m", Context.MODE_PRIVATE, jsonMessage);
        }
    }
}
