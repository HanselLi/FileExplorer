package liyang.sogou.demo;

import android.content.Context;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.convert.StringConvert;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.DBCookieStore;

import java.util.IllegalFormatException;

import okhttp3.CookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by liyangos3323 on 2018/2/26.
 */

public class HttpUtils {

    static HttpUtils httpUtils;

    public static HttpUtils getInstance(){
        if (httpUtils == null){
            synchronized (HttpUtils.class){
                if (httpUtils == null) {
                    httpUtils = new HttpUtils();
                }
            }
        }
        return httpUtils;
    }

    public void DemoGet(Context context){
    }
}
