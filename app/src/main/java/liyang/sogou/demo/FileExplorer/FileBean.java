package liyang.sogou.demo.FileExplorer;

import android.net.Uri;
import android.support.v4.provider.DocumentFile;

/**
 * Created by liyangos3323 on 2018/2/27.
 */

public class FileBean {
    public String name;
    public String type;
    public String modifyTime;
    public String fileLength;
    public DocumentFile file;
    public Uri uri;
    public DocumentFile parentFile;
}
