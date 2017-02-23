package com.ltf.mytoolslibrary.viewbase.gif;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 作者：${李堂飞} on 2017/1/5 10:28
 * 邮箱：1195063924@qq.com
 * 注解:
 */

public class HttpLoader {
    public static InputStream getInputStreanFormUrl(String param) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(param).openConnection();
        return conn.getInputStream();
    }
}
