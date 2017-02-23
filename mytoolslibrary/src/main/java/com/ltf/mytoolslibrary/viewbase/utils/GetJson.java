package com.ltf.mytoolslibrary.viewbase.utils;

import com.ltf.mytoolslibrary.viewbase.isnull.IsNullUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetJson {
	
	/**
	 * 适用于{"key":"value"}
	 * @param json
	 * @param key
	 * @return返回所传JSON下对应Key的Value
	 */
	public static final String getJson(String json,String key){
		String value = "";
       if(IsNullUtils.isNulls(json)){
		   if("msg".equals(key+"")||"message".equals(key+"")){
			   return "网络请求失败,请稍候重试!";
		   }
		   return json;
	   }
		try {
			JSONObject jsonObject = new JSONObject(json);
			 value = jsonObject.optString(key, "");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}
	/**
	 * 适用于{"key":"value"}
	 * @param json
	 * @param key
	 * @return返回所传JSON下对应Key的Value
	 */
	public static final int getError(String json,String key){
		int value = -1 ;
       
		try {
			JSONObject jsonObject = new JSONObject(json);
			 value = jsonObject.optInt(key);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}
	/**
	 * 适用于{----{"key":"value"}----}
	 * @param json(字符串)
	 * @param object(object类名)
	 * @param key(键值)
	 * @return返回所传JSON下对应Key的Value
	 */
	public static final String getJsonObject(String json,String object,String key){
		String value = "";
       
		try {
			JSONObject jsonObject = new JSONObject(json);
		    JSONObject dataObject =    jsonObject.optJSONObject(object);
			value = dataObject.optString(key, "");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}
	/**
	 * 适用于{----data[{attrs{"key":"value"}}]----}
	 * @param json(字符串)
	 * @param date(list名)
	 * @param attrs(list中的类名)
	 * @param Key(键值)
	 * @return返回所传JSON下对应Key的Value
	 */
	public static final ArrayList<String> getJsonArray(String json,String date,String attrs,String Key){
		ArrayList<String> list=new ArrayList<String>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONArray jsonArray = jsonObject.optJSONArray(date);
			for(int i=0;i<jsonArray.length();i++){
				JSONObject pasObject = jsonArray.optJSONObject(i);
				JSONObject content =pasObject.optJSONObject(attrs);
				String value = content.optString(Key, "");
				list.add(value);
		    }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * 实名认证-获取对应城市区县
	 * 适用于{---data[{key,value}]---}
	 * @param json
	 * @param data
	 * @param key
	 * @return value list集合
	 */
	public static final ArrayList<String> getAppWebSite(String json,String data,String key){
		ArrayList<String> list=new ArrayList<String>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONArray jsonArray = jsonObject.optJSONArray(data);
			for(int i=0;i<jsonArray.length();i++){
				JSONObject pasObject = jsonArray.optJSONObject(i);
				String value = pasObject.optString(key,"");
				list.add(value);
		    }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * 适用于{---data{---address[   ]---}---}
	 * @param json
	 * @param data
	 * @param address
	 * @return 返回对应address的list集合
	 */
	public static final ArrayList<String> getWarrantyUserInfo(String json,String data,String address){
		ArrayList<String> list=new ArrayList<String>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONObject jsonData =jsonObject.optJSONObject(data);
			JSONArray jsonArray =  jsonData.optJSONArray(address);
			for(int i=0;i<jsonArray.length();i++){
		        list.add(jsonArray.optString(i));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * 适用于{---data{---merchantList[{"key":"value"}]---}---}
	 * @param json
	 * @param data
	 * @param merchantList
	 * @param key 返回key的list集合
	 * @return
	 */
	public static final ArrayList<String> getMerchantList(String json,String data,String merchantList,String key){
		ArrayList<String> list=new ArrayList<String>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONObject jsonData =jsonObject.optJSONObject(data);
			JSONArray jsonArray =  jsonData.optJSONArray(merchantList);
			for(int i=0;i<jsonArray.length();i++){
				JSONObject praiseRate =jsonArray.optJSONObject(i);
				String k = praiseRate.optString(key,"");
				list.add(k);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * 适用于{--reason{--balances{--balance2[{key,value}]--}--}--}
	 * @param json
	 * @param result
	 * @param balances
	 * @param balances2
	 * @param key
	 * @return
	 */
	public static final ArrayList<String> mbalance(String json,String result,String balances,String balances2,String key){
		ArrayList<String> list=new ArrayList<String>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONObject jsonResult=jsonObject.optJSONObject(result);
			JSONObject jsonBalances=jsonResult.optJSONObject(balances);
			JSONArray jsonBalances2=jsonBalances.optJSONArray(balances2);
			for(int i=0;i<jsonBalances2.length();i++){
				JSONObject jsonKey =jsonBalances2.optJSONObject(i);
				String value = jsonKey.optString(key,"");
				list.add(value);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

}
