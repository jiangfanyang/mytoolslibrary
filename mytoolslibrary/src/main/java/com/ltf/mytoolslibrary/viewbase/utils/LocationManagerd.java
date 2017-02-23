package com.ltf.mytoolslibrary.viewbase.utils;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.ltf.mytoolslibrary.viewbase.utils.show.L;
import com.ltf.mytoolslibrary.viewbase.utils.show.T;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * 作者：${李堂飞} on 2016/8/10 0010 14:11
 * 邮箱：1195063924@qq.com
 * 注解: 利用API内置定位
 */
public class LocationManagerd implements LocationListener {

    private LocationManager locationManager;
    private LocationProvider gpsProvider, netProvider;
    private static LocationManagerd mLocationManagerd;
    public static LocationManagerd getmLocationManagerd(){
        if(mLocationManagerd == null){
            mLocationManagerd = new LocationManagerd();
        }
        return mLocationManagerd;
    }
    private LocationManagerd(){

    }

    private Context activity;
    /**
     * 初始化LocationManager  利用内置API进行定位
     * @param activity
     */
    public void initLocationManager(Context activity) {
        this.activity=activity;
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        gpsProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER);//1.通过GPS定位，较精确，也比较耗电
        netProvider = locationManager.getProvider(LocationManager.NETWORK_PROVIDER);//2.通过网络定位，对定位精度度不高或省点情况可考虑使用
    }

    /*
   * 进行定位
       * provider:用于定位的locationProvider字符串:LocationManager.NETWORK_PROVIDER/LocationManager.GPS_PROVIDER
   * minTime:时间更新间隔，单位：ms
       * minDistance:位置刷新距离，单位：m
   * listener:用于定位更新的监听者locationListener
   */
    public void StartLocation(Context activity, long minTime, float minDistance) {
        this.activity=activity;
        if(NetworkUtils.isNetworkAvailable(activity)){
            if(locationManager == null){
                initLocationManager(activity);
            }
            if(locationManager.getProvider(LocationManager.NETWORK_PROVIDER) == null && locationManager.getProvider(LocationManager.GPS_PROVIDER) == null){
                //无法定位：1、提示用户打开定位服务；2、跳转到设置界面
                Toast.makeText(activity, "无法定位，请打开定位服务", Toast.LENGTH_SHORT).show();
                Intent i = new Intent();
                i.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                activity.startActivity(i);
            } else {
                if (locationManager.getProvider(LocationManager.NETWORK_PROVIDER) != null && locationManager.getProvider(LocationManager.GPS_PROVIDER) != null){
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, this);
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location == null){
                        L.e("onLocationChanged->WIFI>","location=null,导致定位失败!");
                        return;
                    }
                    double latitude = location.getLatitude();     //经度
                    double longitude = location.getLongitude(); //纬度
                    double altitude =  location.getAltitude();     //海拔

                    Address mAddress = getLocationAddressFromGeocoder(activity,latitude,longitude,null);
                    if(mOnLocationUtilsListener != null){
                        mOnLocationUtilsListener.onLocationBackLatLon(mAddress,latitude,longitude);
                    }
                }else if (locationManager.getProvider(LocationManager.NETWORK_PROVIDER) != null) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, this);
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location == null){
                        L.e("onLocationChanged->WIFI>","location=null,导致定位失败!");
                        return;
                    }
                    double latitude = location.getLatitude();     //经度
                    double longitude = location.getLongitude(); //纬度
                    double altitude =  location.getAltitude();     //海拔
                    Address mAddress = getLocationAddressFromGeocoder(activity,latitude,longitude,null);
                    if(mOnLocationUtilsListener != null){
                        mOnLocationUtilsListener.onLocationBackLatLon(mAddress,latitude,longitude);
                    }
                }else{
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, this);
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location == null){
                        L.e("onLocationChanged->GPS>","location=null,导致定位失败!");
                        return;
                    }
                    double latitude = location.getLatitude();     //经度
                    double longitude = location.getLongitude(); //纬度
                    double altitude =  location.getAltitude();     //海拔
                    Address mAddress = getLocationAddressFromGeocoder(activity,latitude,longitude,null);
                    if(mOnLocationUtilsListener != null){
                        mOnLocationUtilsListener.onLocationBackLatLon(mAddress,latitude,longitude);
                    }
                }
            }
        }else {
            T.showShort(activity,"网络连接失败,请检查网络是否打开...");
        }
    }

    /**
     * 位置改变回调方法 //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
     * @param location 当前的位置
     * @return void
     */
    @Override
    public void onLocationChanged(Location location) {
        if (location == null){
            L.e("onLocationChanged->>","location=null,导致定位失败!");
            return;
        }
        //得到纬度
        double latitude = location.getLatitude();
        //得到经度
        double longitude = location.getLongitude();

        Address mAddress = getLocationAddressFromGeocoder(activity,latitude,longitude,null);
        if(mOnLocationUtilsListener != null){
            mOnLocationUtilsListener.onLocationBackLatLon(mAddress,latitude,longitude);
        }
    }

    /**
     * 通过经纬度得到对应的地址信息
     * **/
    public Address getLocationAddressFromGeocoder(Context activity,double latitude,double longitude,ongetLocationAddressFromGeocoder mongetLocationAddressFromGeocoder){
        Geocoder gc = new Geocoder(activity, Locale.getDefault());
        List<Address> locationList = null;
        try {
            locationList = gc.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Address address = locationList.get(0);//得到Address实例
//Log.i(TAG, "address =" + address);
        String countryName = address.getCountryName();//得到国家名称，比如：中国
        L.i("LocationManagerd", "countryName = " + countryName);
        String locality = address.getLocality();//得到城市名称，比如：北京市
        L.i("LocationManagerd", "locality = " + locality);
        String addressLine = "";
        for (int i = 0; address.getAddressLine(i) != null; i++) {
            addressLine = address.getAddressLine(i);//得到周边信息，包括街道等，i=0，得到街道名称
            L.i("LocationManagerd", "addressLine = " + addressLine);
        }
        if(mongetLocationAddressFromGeocoder != null){
            mongetLocationAddressFromGeocoder.ongetLocationAddressFromGeocoder(address,countryName,locality,addressLine);
        }
        return address;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
// Provider的转态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
    }

    @Override
    public void onProviderEnabled(String s) {
//  Provider被enable时触发此函数，比如GPS被打开
    }

    @Override
    public void onProviderDisabled(String s) {
// Provider被disable时触发此函数，比如GPS被关闭
    }

    public interface ongetLocationAddressFromGeocoder{
        /**
         * address 得到Address实例
         * countryName 得到国家名称，比如：中国
         * locality 得到城市名称，比如：北京市
         * addressLine 得到周边信息，包括街道等，i=0，得到街道名称
         * **/
        void ongetLocationAddressFromGeocoder(Address address,String countryName,String locality,String addressLine);
    }

    private OnLocationUtilsListener mOnLocationUtilsListener;
    public void setOnLocationUtilsListener(OnLocationUtilsListener mOnLocationUtilsListener){
        this.mOnLocationUtilsListener = mOnLocationUtilsListener;
    }
    public interface OnLocationUtilsListener {
        /**
         * address 得到Address实例
         * 回调定位成功 返回定位经纬度
         */
        void onLocationBackLatLon(Address mAddress,double lat, double lon);
    }
}
