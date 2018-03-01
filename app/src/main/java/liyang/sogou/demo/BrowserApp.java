package liyang.sogou.demo;

import android.app.Application;

import com.lzy.okgo.OkGo;

/**
 * Created by liyangos3323 on 2018/2/7.
 */

public class BrowserApp extends Application {
    public static BrowserApp mApp;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp =this;
        OkGo.getInstance().init(this);
    }
}
