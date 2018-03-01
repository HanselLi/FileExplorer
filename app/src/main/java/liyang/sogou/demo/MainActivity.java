package liyang.sogou.demo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import liyang.sogou.demo.FileExplorer.Main3Activity;


@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private RecyclerView demoRecycler;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = (WebView) findViewById(R.id.web);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setTextZoom(100);
        webView.addJavascriptInterface(new JsInterface(), "SogouLoginUtils");
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.loadUrl("file:///android_asset/demo.html");
        requestUsePermisson();
    }

    /**
     * 简单的请求一下权限
     * */
    private void requestUsePermisson() {
        int writePermisson = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermisson = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (!(writePermisson == PackageManager.PERMISSION_GRANTED &&
                readPermisson == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length == 2 && requestCode == 100) {
            Toast.makeText(this, "success ", Toast.LENGTH_SHORT).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }




    public void click(View view) {
       /* String content = ".中国香港(香港特别行政区|)";
        Log.d("tag", content);
        try {
            URL url = new URL("http://www.hongkongairlines.com/zh_CN/booking/flight");
            String host = url.getHost();
            Log.d("tag", "host is == " + host);
            String mRegex = "([.]|。)(中国)?台湾$";
            String mRegex2 = "([.]|。)(中国)?香港([(]香港特别行政区[)])?$";
            Pattern pattern = Pattern.compile(mRegex2, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(".中国香港(香港特别行政区)");
            boolean match = matcher.find();
            Log.d("tag", "match ? == " + match);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }*/
        startActivity(new Intent(this, Main3Activity.class));
    }




    /*private String getPathString() {
        String filesDir = getFilesDir().getAbsolutePath();
        String getCacheDir = getCacheDir().getAbsolutePath();
        String getDataDir = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            getDataDir = getDataDir().getAbsolutePath();
        }
        String getExternalCacheDir = getExternalCacheDir().getAbsolutePath();
        // only hosted by application ,not shared by all apps
        String externalFilesDir = getExternalFilesDir(Environment.DIRECTORY_MOVIES).getAbsolutePath();
        String environmentDownlodCachePath = Environment.getDownloadCacheDirectory().getAbsolutePath();
        String publicDirectoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsolutePath();
        String dataDirectorty = Environment.getDataDirectory().getAbsolutePath();
        String externalStorageDirectoryPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String rootDirectoryPath = Environment.getRootDirectory().getAbsolutePath();
        StringBuilder sb = new StringBuilder();
        sb.append("filesDir == " + filesDir + " getDataDir == " + getDataDir + "  getCacheDir == " + getCacheDir + "  getExternalCacheDir == " + getExternalCacheDir
                + " externalFilesDir == " + externalFilesDir + " environmentDownlodCachePath == " + environmentDownlodCachePath
                + " publicDirectoryPath == " + publicDirectoryPath + " dataDirectorty == " + " dataDirectorty == " + dataDirectorty
                + " externalStorageDirectoryPath == " + externalStorageDirectoryPath + " rootDirectoryPath == " + rootDirectoryPath);
        String s = sb.toString();
        return s;
    }*/
}
