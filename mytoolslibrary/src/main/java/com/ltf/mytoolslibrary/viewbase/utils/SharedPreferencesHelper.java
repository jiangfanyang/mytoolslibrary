package com.ltf.mytoolslibrary.viewbase.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author 李堂飞
 * SharedPreferences 保存方式  ，提供保存方法
 */
public class SharedPreferencesHelper {

	/**
	 * 保存字符�? * @param mContext 上下�? * @param mode 模式
	 * 
	 * @param key
	 *            key
	 * @param value
	 *            �?
	 */
	public static void setString(Context mContext, String name, int mode,
			String key, String value) {
		SharedPreferences sp = mContext.getSharedPreferences(name, mode);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * 读取字符�? * @param mContext 上下�? * @param mode 模式
	 * 
	 * @param key
	 *            key
	 * @return String
	 */
	public static String getString(Context mContext, String name, int mode,
			String key) {
		SharedPreferences sp = mContext.getSharedPreferences(name, mode);
		String value = sp.getString(key, "");
		return value;
	}

	/**
	 * 保存整形数据
	 * 
	 * @param mContext
	 *            上下�? * @param mode 模式
	 * @param key
	 *            key
	 * @param value
	 *            �?
	 */
	public static void setInt(Context mContext, String name, int mode,
			String key, int value) {
		SharedPreferences sp = mContext.getSharedPreferences(name, mode);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	/**
	 * 读取整形数据
	 * 
	 * @param mContext
	 *            上下�? * @param mode 模式
	 * @param key
	 *            key
	 * @return String
	 */
	public static int getInt(Context mContext, String name, int mode, String key) {
		SharedPreferences sp = mContext.getSharedPreferences(name, mode);
		int value = sp.getInt(key, 0);
		return value;
	}

	/**
	 * 保存boolean数据
	 * 
	 * @param mContext
	 *            上下�? * @param mode 模式
	 * @param key
	 *            key
	 * @param value
	 *            �?
	 */
	public static void setBoolean(Context mContext, String name, int mode,
			String key, boolean value) {
		SharedPreferences sp = mContext.getSharedPreferences(name, mode);
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	/**
	 * 读取boolean数据
	 * 
	 * @param mContext
	 *            上下�? * @param mode 模式
	 * @param key
	 *            key
	 * @return String
	 */
	public static boolean getBoolean(Context mContext, String name, int mode,
			String key) {
		SharedPreferences sp = mContext.getSharedPreferences(name, mode);
		boolean value = sp.getBoolean(key, false);
		return value;
	}

	/**
	 * 读取boolean数据
	 * 
	 * @param mContext
	 *            上下�? * @param mode 模式
	 * @param key
	 *            key
	 * @return String
	 */
	public static boolean getBoolean(Context mContext, String name, int mode,
			String key, boolean defaultValue) {
		SharedPreferences sp = mContext.getSharedPreferences(name, mode);
		boolean value = sp.getBoolean(key, defaultValue);
		return value;
	}
	

	/**通过SharedPreferences中对应的文件名获取布尔值类型的配置信息
	 * @param context   上下文
	 * @param name      保存的SharedPreferences文件名
	 * @param mode	           保存的模式
	 * @param isBoolean 默认返回值
	 * @return          返回布尔型变量值
	 */
	public static boolean getSharedPreferencesBooleanUtil(Context context,String name, int mode,Boolean isBoolean){
		SharedPreferences preferences = context.getSharedPreferences(name, mode);
		return preferences.getBoolean(name, isBoolean);
	}
	
	/**通过SharedPreferences中对应的文件名获取字符串类型的配置信息
	 * @param context   上下文
	 * @param name      保存的SharedPreferences文件名
	 * @param mode	           保存的模式
	 * @param string    默认返回值
	 * @return          返回字符串类型值
	 */
	public static String getSharedPreferencestStringUtil(Context context,String name, int mode,String string){
		SharedPreferences preferences = context.getSharedPreferences(name, mode);
		return preferences.getString(name, string);
	}
	
	/**通过SharedPreferences中对应的文件名获取字符串类型的配置信息
	 * @param context   上下文
	 * @param name      保存的SharedPreferences文件名
	 * @param mode	           保存的模式
	 * @param int       默认返回值
	 * @return          返回字符串类型值
	 */
	public static int getSharedPreferencestIntUtil(Context context,String name, int mode,int a){
		SharedPreferences preferences = context.getSharedPreferences(name, mode);
		return preferences.getInt(name, a);
	}
	
	/**通过SharedPreferences中对应的文件名保存布尔值类型的配置信息
	 * @param context   上下文
	 * @param name      保存的SharedPreferences文件名
	 * @param mode	           保存的模式
	 * @param isBoolean 需要保存的布尔值
	 * @return          null
	 */
	public static void saveSharedPreferencesBooleanUtil(Context context,String name, int mode,Boolean isBoolean){
		SharedPreferences preferences = context.getSharedPreferences(name, mode);
		preferences.edit().putBoolean(name, isBoolean).commit();
	}
	
	/**通过SharedPreferences中对应的文件名保存字符串类型的配置信息
	 * @param context   上下文
	 * @param name      保存的SharedPreferences文件名
	 * @param mode	           保存的模式 MODE_PRIVATE
	 * @param string    需要保存的字符串类型
	 * @return          null
	 */
	public static void saveSharedPreferencestStringUtil(Context context,String name, int mode,String string){
		SharedPreferences preferences = context.getSharedPreferences(name, mode);
		preferences.edit().putString(name, string).commit();
	}
	
	/**通过SharedPreferences中对应的文件名保存字符串类型的配置信息
	 * @param context   上下文
	 * @param name      保存的SharedPreferences文件名
	 * @param mode	           保存的模式
	 * @param int    需要保存的字符串类型
	 * @return          null
	 */
	public static void saveSharedPreferencestIntUtil(Context context,String name, int mode,int a){
		SharedPreferences preferences = context.getSharedPreferences(name, mode);
		preferences.edit().putInt(name, a).commit();
	}

}
