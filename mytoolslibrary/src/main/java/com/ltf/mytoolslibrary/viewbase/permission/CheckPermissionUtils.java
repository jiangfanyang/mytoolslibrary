package com.ltf.mytoolslibrary.viewbase.permission;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.ltf.mytoolslibrary.viewbase.base.ActivityTitleBase;
import com.ltf.mytoolslibrary.viewbase.constent.constent;
import com.ltf.mytoolslibrary.viewbase.utils.show.L;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.List;

/**
 * 权限检查类 李堂飞 20161221
 * 本类独立出来去做权限检查permission
 *  如果你的Activity继承的是AppCompatActivity、FragmentActivity或者它们的子类，
 *  那么你直接请求权限就可以。 2. 如果你的Fragment继承的是android.support.v4.app.Fragment或者它的子类，
 *  那么你直接请求权限就可以。 3. 如果你继承的是android.app.Activity、android.app.Fragment、
 *  在6.0以下的手机是没有onRequestPermissionsResult()方法的，所以需要在申请权限前判断：
 *  https://github.com/yanzhenjie/AndPermission
 */
public class CheckPermissionUtils {

    private static CheckPermissionUtils picUpdateUtils;

    public static CheckPermissionUtils getSelectPicUpdateUtils() {
        if (picUpdateUtils == null) {
            picUpdateUtils = new CheckPermissionUtils();
        }
        return picUpdateUtils;
    }

    private CheckPermissionUtils() {
    }

    private onBackPermissionResult monBackPermissionResult;

    public interface onBackPermissionResult {
        void onBackPermissionResult();
    }

    /**
     * 判断权限是否丢失，主要是方式用户关闭某些权限（被用户禁止权限）导致应用某些功能无法正常使用
     * 比如拍照或录制视屏 6.0以及以上属于危险级别的权限，直接使用会造成应用崩溃
     * @param requestCode 返回码
     * @param monBackPermissionResult
     * @param permission
     */
    public void checkPermission(int requestCode, Object activitys, boolean isSigno ,onBackPermissionResult monBackPermissionResult, String[] permission) {
        this.monBackPermissionResult = monBackPermissionResult;
        Activity activity = null;
        if(activitys instanceof FragmentActivity || activitys instanceof Activity){
            activity = (Activity) activitys;
        }else if(activitys instanceof Fragment){
            activity = ((Fragment) activitys).getActivity();
        }else{
            L.e("权限检查","obj 只能是 FragmentActivity 或者 android.support.v4.app.Fragment 及其子类");
            return;
        }
        // 先判断是否有权限。
        if(AndPermission.hasPermission(activity, permission)) {
            // 有权限，直接do anything.
            onBackPermissionResult();
        } else {
            // 申请权限。
            final Activity finalActivity = activity;
            AndPermission.with(activity)
                    .requestCode(100)
                    .permission(permission)
                    // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框，避免用户勾选不再提示。
                    .rationale(new RationaleListener() {
                        @Override
                        public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                            // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
                            AndPermission.rationaleDialog(finalActivity, rationale).show();
                        }
                    })
                    .send();
        }
    }

    /**权限检查成功**/
    public void onBackPermissionResult(){
        if (monBackPermissionResult != null) {
            monBackPermissionResult.onBackPermissionResult();
        }else{
            L.e("权限检查","权限检查回调出错:monBackPermissionResult="+monBackPermissionResult);
        }
    }

    /**用户从系统设置界面回来**/
    public void UserFromSettingBack(int requestCode){
        switch (requestCode){
            case constent.REQUEST_CODE_SETTING:
                if(monUserFromSettingBackLisnner != null){
                    monUserFromSettingBackLisnner.onUserFromSettingBack();
                }
                break;
        }
    }

    /**权限授权失败**/
    public void onRequestPermissionsResultOnFailed(Activity activity,List<String> deniedPermissions){
        // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
        if (AndPermission.hasAlwaysDeniedPermission(activity, deniedPermissions)) {
            // 第一种：用默认的提示语。
            AndPermission.defaultSettingDialog(activity, constent.REQUEST_CODE_SETTING).show();
            // 第二种：用自定义的提示语。
//             AndPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING)
//                     .setTitle("权限申请失败")
//                     .setMessage("我们需要的一些权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
//                     .setPositiveButton("好，去设置")
//                     .show();

//            第三种：自定义dialog样式。
//            SettingService settingService = AndPermission.defineSettingDialog(this, REQUEST_CODE_SETTING);
//            你的dialog点击了确定调用：
//            settingService.execute();
//            你的dialog点击了取消调用：
//            settingService.cancel();
        }else{
            //拒绝了权限没有勾选不再提示
            if(monUserFromSettingBackLisnner != null){
                monUserFromSettingBackLisnner.onHasAlwaysDeniedPermission();
            }
        }
    }

    private onUserFromSettingBackLisnner monUserFromSettingBackLisnner;

    public void setonUserFromSettingBackLisnner(onUserFromSettingBackLisnner monUserFromSettingBackLisnners){
        this.monUserFromSettingBackLisnner = monUserFromSettingBackLisnners;
    }
    public interface onUserFromSettingBackLisnner {
        /**拒绝了权限没有勾选不再提示**/
        void onHasAlwaysDeniedPermission();
        /**用户从系统设置界面回来**/
        void onUserFromSettingBack();
    }

}
