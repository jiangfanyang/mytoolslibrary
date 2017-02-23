package com.ltf.mytoolslibrary.viewbase.utils.show;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;
/**
 * 作者：${李堂飞} on 2016/7/15 0015 09:02
 * 邮箱：1195063924@qq.com
 * 注解: 吐司管理工具
 */
public class ToastUtils {

	private static String oldMsg;
    protected static Toast toast   = null;  
    private static long oneTime=0;  
    private static long twoTime=0;  
      
    private static void showToast(Context context, String s,int CENTER){      
        if(toast==null){   
            toast =Toast.makeText(context, s, Toast.LENGTH_SHORT);  
            toast.show();  
            if(CENTER == -1){
            	
            }else{
            	toast.setGravity(Gravity.CENTER, 0, 0);
            }
            oneTime=System.currentTimeMillis();  
        }else{  
            twoTime=System.currentTimeMillis();  
            if(s.equals(oldMsg)){  
                if(twoTime-oneTime>Toast.LENGTH_SHORT){  
                    toast.show();  
                }  
            }else{  
                oldMsg = s;  
                toast.setText(s);  
                toast.show();  
            }         
        }  
        oneTime=twoTime;  
    }  
      
      
    public static void showToast(Context context, String msg){     
        showToast(context, msg,-1);  
    }  
    
    public static void showToast(Context context, int resId){     
    	showToast(context, context.getString(resId),-1);  
    }  
    
    public static void showToast_CENTER(Context context, int resId){     
    	showToast(context, context.getString(resId),1);  
    }

    public static void showToast_CENTER(Context context, String msg){
        showToast(context, msg,1);
    }
}
