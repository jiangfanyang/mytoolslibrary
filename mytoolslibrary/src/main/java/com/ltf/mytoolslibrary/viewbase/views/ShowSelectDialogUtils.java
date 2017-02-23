package com.ltf.mytoolslibrary.viewbase.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ltf.mytoolslibrary.R;
import com.ltf.mytoolslibrary.viewbase.adapter.CommonAdapter1;
import com.ltf.mytoolslibrary.viewbase.adapter.ViewHolder;
import com.ltf.mytoolslibrary.viewbase.utils.AutoUtils;

import java.util.List;

/**
 * 李堂飞 显示自定义内容提示框
 * Created by Administrator on 2016/4/23 0023.
 */
public class ShowSelectDialogUtils extends Dialog {

    private Context context;
    private String titlestr;
    private List<String> data;
    private ChangeSelect on;

    public ShowSelectDialogUtils(Context context) {
        super(context,R.style.my_dialog);
        this.context = context;
    }

    public ShowSelectDialogUtils(Context context, int themeResId) {
        super(context, R.style.my_dialog);
        this.context = context;
    }

    public ShowSelectDialogUtils(Context c, String titlestr, final List<String> data, final ChangeSelect on) {
        super(c, R.style.my_dialog);
        this.context = c;
        this.data = data;
        this.on = on;
        this.titlestr = titlestr;
    }

    public interface ChangeSelect {
        void OnSelect(int selectd, String name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(context).inflate(R.layout.item_type_select_list,null);
        AutoUtils.auto(view);
        setContentView(view);

        TextView title = (TextView) findViewById(R.id.title);
        ListView type_listview = (ListView) findViewById(R.id.type_listview);
        title.setText(titlestr);
        CommonAdapter1<String> adapter = new CommonAdapter1<String>(context, data, R.layout.item_type_select_list_item) {
            @Override
            public void convert(ViewHolder helper, String item, int position) {
                helper.setText(R.id.type_shagjia, "" + item);
            }
        };
        type_listview.setAdapter(adapter);
        type_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (on != null) {
                    on.OnSelect(position + 1, data.get(position));
                }
                dismiss();
            }
        });
    }

}
