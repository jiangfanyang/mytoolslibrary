package com.ltf.tools.mytoolslibrary;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.ltf.mytoolslibrary.viewbase.utils.PicassoUtil;

/**工具类库
 * 使用用例**/
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PicassoUtil.getInstantiation().onWidgetImage(this,"https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1487839107&di=934a0f03c91b97da2fbd2774e2a1590c&src=http://pic12.nipic.com/20110213/580124_212334691148_2.jpg", (ImageView) findViewById(R.id.imgs));
    }
}
