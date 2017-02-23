
package com.ltf.mytoolslibrary.viewbase.swipebacklayout.app;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.alipay_android_sdk.FragmentActivity;
import com.ltf.mytoolslibrary.R;
import com.ltf.mytoolslibrary.viewbase.constent.constent;
import com.ltf.mytoolslibrary.viewbase.isnull.IsNullUtils;
import com.ltf.mytoolslibrary.viewbase.swipebacklayout.SwipeBackLayout;
import com.ltf.mytoolslibrary.viewbase.swipebacklayout.Utils;
import com.ltf.mytoolslibrary.viewbase.utils.AutoUtils;
import com.ltf.mytoolslibrary.viewbase.utils.FormatTimeUtils;
import com.ltf.mytoolslibrary.viewbase.utils.SharedPreferencesHelper;
import com.ltf.mytoolslibrary.viewbase.utils.SystemBarTintManager;
import com.ltf.mytoolslibrary.viewbase.utils.show.L;

import butterknife.ButterKnife;

/**
 * 统筹整个主题 请务必使用isChangeStaueColor(isViewStaueColor,setStatusBarTintResource() == -1 ?  -1: 0xff446622,1);
 * 更换0xff446622为你想要的颜色值即可 同时注释掉isChangeStaueColor(isViewStaueColor,setStatusBarTintResource(),0);
 * flages是全局主题方式,themColor是全局主题颜色值 ,flages =0 时当themColor = R.color.title_color;
 * 当flages =1时 themColor=0xff446622;(或其它自定义值).
 * 当统筹整个主题时 务必flages =1.如若需控制当前页面下的按钮或一些布局变为主题颜色值务必重写onInitSettingButtonBg(int flage,int bg){};
 * <p>
 * 查看效果可使用如下语句:
 * //isChangeStaueColor(isViewStaueColor,setStatusBarTintResource(),0);//统筹整个主题时注释掉这句
 * //以下方式是统筹整个主题
 * //把16进制的字符数转换成十进制的整数
 * int i = parse("0xff446622".replace("0X","0x"));
 * System.out.println("int:"+i);
 * String hex=Integer.toHexString(i);
 * System.out.println("Hex:"+hex);int i = Integer.parseInt("0xff446622",16);
 * isChangeStaueColor(isViewStaueColor,setStatusBarTintResource() == -1 ?  -1: i,1);
 **/
public abstract class SwipeBackActivity extends FragmentActivity implements SwipeBackActivityBase {
    private SwipeBackActivityHelper mHelper;

    /**
     * 初始化设置当前Activity是否支持滑动返回上一个界面  在这个回调方法里设置
     **/
    public void initisBack() {FormatTimeUtils.getFormatTimeUtils().init(this,0l);
    }

    /**
     * 方法名: onInitSettingButtonBg(int flage,int bg)
     * 备注: 配合主题更换需要变化的按钮或布局 返回类型: void
     * 注解: bg是全局回调的主题颜色值
     * flage=0时View.setBackgroundResource(bg);
     * flage=1时View.setBackgroundColor(bg);
     */
    public void onInitSettingButtonBg(int flage, int bg) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ScreenAdaptation();
        ButterKnife.bind(this);
        initisBack();
        ThemColorSetting();
        super.onCreate(savedInstanceState);
        if (isBackUp) {
            mHelper = new SwipeBackActivityHelper(this);
            mHelper.onActivityCreate();
        } else {
            mHelper = null;
        }

        this.setSwipeBackLayoutActivityFinishLinsnner(new onSwipeBackLayoutActivityFinishLinsnner() {
            @Override
            public void onSwipeBackLayoutActivityFinishs() {
                L.e("用户滑动触发打印", "此时用户滑动触发关闭当前界面时回调次事件给相应子类,子类重写onSwipeBackLayoutActivityFinish()方法获得事件并消费...");
                onSwipeBackLayoutActivityFinish();
            }
        });
    }

    /**
     * 主题颜色值设置
     */
    private void ThemColorSetting() {
        //以下方式是统筹整个主题
        String ThemColor = SharedPreferencesHelper.getSharedPreferencestStringUtil(this, "ThemColor", MODE_PRIVATE, "");
        if (TextUtils.isEmpty(ThemColor)) {//如果服务器没有反主题控制
            isChangeStaueColor(isViewStaueColor, setStatusBarTintResource());
        } else {
            isChangeStaueColor(isViewStaueColor, setStatusBarTintResource());
        }
    }

    /**
     * 屏幕适配
     */
    private void ScreenAdaptation() {
        isViewStaueColor = setIsViewStaueColor();
        if (isViewStaueColor) {
            AutoUtils.setSize(this, true, constent.designWidth, constent.designHeight);//有状态栏,设计尺寸的宽高
        } else {
            AutoUtils.setSize(this, false, constent.designWidth, constent.designHeight);//没有状态栏,设计尺寸的宽高
        }
        if (setLayoutId() != 0) {
            setContentView(setLayoutId());
        }
        AutoUtils.auto(this);//适配实际屏幕
    }

    public int parse(String s) {//"0xff446622"字符串转换成int 0xff446622
        if (!s.startsWith("0x"))
            return -1;
        int number = 0, n = 0;
        for (int i = 2; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '1':
                    n = 1;
                    break;
                case '2':
                    n = 2;
                    break;
                case '3':
                    n = 3;
                    break;
                case '4':
                    n = 4;
                    break;
                case '5':
                    n = 5;
                    break;
                case '6':
                    n = 6;
                    break;
                case '7':
                    n = 7;
                    break;
                case '8':
                    n = 8;
                    break;
                case '9':
                    n = 9;
                    break;
                case '0':
                    n = 0;
                    break;
                case 'a':
                case 'A':
                    n = 10;
                    break;
                case 'b':
                case 'B':
                    n = 11;
                    break;
                case 'c':
                case 'C':
                    n = 12;
                    break;
                case 'd':
                case 'D':
                    n = 13;
                    break;
                case 'e':
                case 'E':
                    n = 14;
                    break;
                case 'f':
                case 'F':
                    n = 15;
                    break;
                default:
                    return -1;
            }
            number = number * 16 + n;
        }
        return number;
    }

    private boolean isViewStaueColor = true;

    /***
     * 设置是否状态栏颜色
     */
    public abstract boolean setIsViewStaueColor();

    private Boolean isBackUp = true;

    /***
     * 设置是否滑动返回
     */
    public void setIsBackUp(Boolean isBackUps) {
        this.isBackUp = isBackUps;
    }

    /**
     * 通知栏所需颜色
     **/
    public abstract String setStatusBarTintResource();

    /**
     * 初始化标题栏颜色 返回全局主题颜色值加载方式 flage=0表示加载R.color.while样式的值(此种方式是默认加载方式) flage=1表示加载0xff446622样式的值
     **/
    public abstract void initTitleBarBg(int flage, int coror);

    /**
     * 设置资源布局
     *
     * @return
     */
    protected abstract int setLayoutId();

    public SystemBarTintManager tintManager;

    /**
     * 设置当前界面为沉浸式状态栏
     *
     * @param is
     * @param color
     **/
    private void isChangeStaueColor(boolean is, String color) {
        if (!is) {
            L.e("状态栏颜色", "----->不使用任何");
            return;
        }

        if (!IsNullUtils.isNulls(color)) {
            this.userSettingThemColor = color;
        } else {
            this.userSettingThemColor = "" + R.color.title_color;
        }

        if (this.userSettingThemColor.replace("0X", "0x").startsWith("0x")) {
            this.flages = 1;
        } else if ("-1".equals(this.userSettingThemColor + "")) {
            this.flages = -1;
        } else if ("0".equals(this.userSettingThemColor + "")) {//使用默认主题
            this.flages = 0;
            this.userSettingThemColor = "" + R.color.title_color;
        } else {
            this.flages = 0;
        }

        if (this.flages == 1) {
            //把16进制的字符数转换成十进制的整数
            int coror = parse(this.userSettingThemColor.replace("0X", "0x"));
            //        int i = parse("0xff446622".replace("0X","0x"));
            System.out.println("int:" + coror);
            String hex = Integer.toHexString(coror);
            System.out.println("Hex:" + hex);
            this.themColor = coror;
        } else {
            try {
                this.themColor = Integer.parseInt(this.userSettingThemColor);
            } catch (Exception e) {
                this.themColor = R.color.title_color;
            }
        }

        if (this.flages == -1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                setTranslucentStatusToImage(this.flages);
                return;
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);

            if (this.flages == 0) {
                tintManager.setStatusBarTintResource(this.themColor);//通知栏所需颜色
                initTitleBarBg(0, this.themColor);
                onInitSettingButtonBg(0, ContextCompat.getColor(this, this.themColor));
            } else {
                tintManager.setStatusBarTintColor(this.themColor);//通知栏所需颜色
                initTitleBarBg(1, this.themColor);
                onInitSettingButtonBg(1, this.themColor);
            }
        } else {
            L.e("状态栏颜色", "----->不使用任何_不满足所有条件");
        }
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public int themColor = R.color.title_color;
    public String userSettingThemColor = "" + R.color.title_color;
    public int flages = 0;

    private void setTranslucentStatusToImage(int flage) {
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        initTitleBarBg(flage, themColor);//此时flage=-1  不设置状态栏颜色  默认让布局顶上去重叠状态栏
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mHelper != null)
            mHelper.onPostCreate();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        if (mHelper != null) {
            return mHelper.getSwipeBackLayout();
        } else {
            return null;
        }
    }

    @Override
    protected void OnBackSysoFragmentActivity(long l, String s) {
        FormatTimeUtils.getFormatTimeUtils().format(l, s,this);
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        if (getSwipeBackLayout() == null) {
            return;
        }
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }

    /**
     * 用户滑动返回上级界面时触发的回调
     */
    public void onSwipeBackLayoutActivityFinish() {
    }

    public static onSwipeBackLayoutActivityFinishLinsnner mSwipeBackLayoutActivityFinishLinsnner;

    public void setSwipeBackLayoutActivityFinishLinsnner(onSwipeBackLayoutActivityFinishLinsnner mSwipeBackLayoutActivityFinishLinsnners) {
        this.mSwipeBackLayoutActivityFinishLinsnner = mSwipeBackLayoutActivityFinishLinsnners;
    }

    public interface onSwipeBackLayoutActivityFinishLinsnner {
        void onSwipeBackLayoutActivityFinishs();
    }
}
