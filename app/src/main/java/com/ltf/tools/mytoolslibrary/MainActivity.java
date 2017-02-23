package com.ltf.tools.mytoolslibrary;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.ltf.mytoolslibrary.viewbase.utils.PicassoUtil;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PicassoUtil.getInstantiation().onWidgetImage(this,"", (ImageView) findViewById(R.id.imgs));
    }
}
