package com.ltf.mytoolslibrary.viewbase.utils.editext_format;

import com.ltf.mytoolslibrary.viewbase.andreabaccega.widget.FormEditText;

/**
 * 作者：${李堂飞} on 2016/8/5 0005 15:17
 * 邮箱：1195063924@qq.com
 * 注解: FormEditText输入格式化
 */
public class FormEditTextCheckInputUtlis {

    public  static boolean gotoFormEditTextCheckInputUtlis(FormEditText... allFaileds){
        boolean allValid = true;
        for (FormEditText failed : allFaileds) {
            allValid = failed.testValidity() && allValid;
        }
        return allValid;
    }
}
