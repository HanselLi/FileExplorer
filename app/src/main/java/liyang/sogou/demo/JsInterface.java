package liyang.sogou.demo;

import android.app.Application;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * Created by liyangos3323 on 2018/2/7.
 */

public class JsInterface {
    @JavascriptInterface
    public void shareWeiXinContact(){
        Toast.makeText(BrowserApp.mApp,"调起了",Toast.LENGTH_SHORT).show();
    }
}
