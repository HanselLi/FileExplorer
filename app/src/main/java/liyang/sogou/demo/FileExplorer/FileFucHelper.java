package liyang.sogou.demo.FileExplorer;

import android.content.Context;
import android.support.v4.provider.DocumentFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import liyang.sogou.demo.FileExplorer.FileBean;

/**
 * Created by liyangos3323 on 2018/2/27.
 */

public class FileFucHelper {
    public static void createNewFileOrFolder() {

    }

    public static boolean copyFileOrFolder(FileBean fileBean, Context context) {
        InputStream is = null;
        OutputStream os = null;
        try {
            DocumentFile parentFile = fileBean.parentFile;
            //新建一个文件，然后用流传过去。
            DocumentFile newFile = parentFile.createFile(parentFile.getType(), "我是复制的");
            is = context.getContentResolver().openInputStream(fileBean.uri);
            os = context.getContentResolver().openOutputStream(newFile.getUri());
            if (is == null || os == null) {
                return false;
            }
            byte[] bytes = new byte[1024];
            int len;
            while ((len = is.read(bytes)) != -1) {
                os.write(len);
                os.flush();
            }
            //长度相等表示复制成功
            if (fileBean.file.length() == newFile.length()) {
                return true;
            }
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
