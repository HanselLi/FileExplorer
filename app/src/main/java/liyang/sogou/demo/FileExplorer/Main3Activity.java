package liyang.sogou.demo.FileExplorer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.support.annotation.RequiresApi;
import android.support.v4.provider.DocumentFile;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import liyang.sogou.demo.R;

public class Main3Activity extends AppCompatActivity implements View.OnClickListener {

    private static final String URL_KEY = "url_key";
    private static final String MY_SP = "my_sp";
    private RecyclerView recycler;
    private SharedPreferences mSp;
    List<FileBean> mData = new ArrayList<>();
    private FileAdapter fileAdapter;
    List<DocumentFile> mOrderContainer = new ArrayList<>();
    private PopupWindow chooseWindow;
    private AlertDialog createFileDialog;
    // 记录长按item的位置position
    private int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setLayoutManager(new GridLayoutManager(this, 2));
        initChoosePopWindow();
        createFileDialog = initCreateFileDialog();
        //看下是否有外置SD卡
        if (mSp == null) {
            mSp = getSharedPreferences(MY_SP, MODE_PRIVATE);
        }
        checkExternalStoragePathValid();
        recycler.addOnItemTouchListener(new RecyclerItemListener(recycler, new RecyclerItemListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                DocumentFile file = mData.get(position).file;
                if (file.isDirectory()) {
                    if (mData.size() != 0) {
                        mData.clear();
                        // 初始化这个文件夹下的数据内容
                        initDecendantsData(file);
                        fileAdapter.setDataSource(mData);
                        //在点击文件夹的情况下，把当前的DocumentFile加进去保存在集合里
                        mOrderContainer.add(file);
                    }
                } else {
                    shareIntent(mData.get(position).name);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                // 记录当前的position供删除功能使用
                currentPosition = position;
                chooseWindow.showAtLocation(findViewById(android.R.id.content), Gravity.LEFT, 0, getResources().getDisplayMetrics().heightPixels - dp2px(50));
            }
        }));

        /**
         *  这段代码有问题，使用在OnBindeViewHolder添加自定义
         *  OnItemClickListener回调接口执行后不能正确区分
         *  单击事件和长按事件，使用recyclerView的addItemTouchListener
         *  通过手势监听实现 见RecyclerItemListener.class
         **/
        //在数据未初始化时候设置点击事件不响应,回调listener为空,放在后面即可
        /*fileAdapter.setOnItemClickListener(new FileAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View itemView) {
                DocumentFile file = mData.get(position).file;
                if (file.isDirectory()) {
                    if (mData.size() != 0) {
                        mData.clear();
                        // 初始化这个文件夹下的数据内容
                        initDecendantsData(file);
                        fileAdapter.setDataSource(mData);
                        //在点击文件夹的情况下，把当前的DocumentFile加进去保存在集合里
                        mOrderContainer.add(file);
                    }
                } else {
                    shareIntent(mData.get(position).name);
                }
            }
        });
        // 长按
        fileAdapter.setOnItemLongClickListener(new FileAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int layoutPosition, View itemView) {
                //
                recycler.setTag(layoutPosition);
                chooseWindow.showAtLocation(findViewById(android.R.id.content), Gravity.LEFT, 0, getResources().getDisplayMetrics().heightPixels - dp2px(50));
            }
        });*/
    }

    private void initChoosePopWindow() {
        chooseWindow = new PopupWindow(Main3Activity.this);
        chooseWindow.setHeight(dp2px(50));
        chooseWindow.setWidth(getResources().getDisplayMetrics().widthPixels);
        View chooseItem = LayoutInflater.from(this).inflate(R.layout.dialog_item, null);
        chooseWindow.setContentView(chooseItem);
        Button copyBtn = chooseItem.findViewById(R.id.copy_btn);
        Button deleteBtn = chooseItem.findViewById(R.id.delete_btn);
        Button createBtn = chooseItem.findViewById(R.id.create_btn);
        copyBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        createBtn.setOnClickListener(this);
    }

    public int dp2px(int dp) {
        return (int) (getResources().getDisplayMetrics().density * dp + 0.5f);
    }


    public void dissmissPopWindow() {
        if (chooseWindow != null && chooseWindow.isShowing()) {
            chooseWindow.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        dissmissPopWindow();
        if (isRootFileTree()) {
            super.onBackPressed();
        } else {
            if (mOrderContainer != null && mOrderContainer.size() > 0) {
                //拿到上级的file,重新初始化数据，更新界面
                DocumentFile parentFile = mOrderContainer.get(mOrderContainer.size() - 1).getParentFile();
                //此处需要重新清理下数据源
                mData.clear();
                initDecendantsData(parentFile);
                fileAdapter.setDataSource(mData);
                mOrderContainer.remove(mOrderContainer.size() - 1);
            }
        }
    }

    /**
     * 获取文件夹的子文件内容并展示
     **/
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void initDecendantsData(DocumentFile file) {
        if (file.isDirectory()) {
            DocumentFile[] documentFiles = file.listFiles();
            for (int i = 0; i < documentFiles.length; i++) {
                FileBean bean = new FileBean();
                //加入每个文件夹的uri
                bean.parentFile = documentFiles[i].getParentFile();
                bean.uri = documentFiles[i].getUri();
                bean.name = documentFiles[i].getName();
                bean.type = documentFiles[i].getType();
                bean.modifyTime = String.valueOf(documentFiles[i].lastModified());
                bean.fileLength = String.valueOf(documentFiles[i].length());
                bean.file = documentFiles[i];
                mData.add(bean);
            }
        }
    }

    private void checkExternalStoragePathValid() {
        String externalSDCardPath = externalSDCardPath();
        if (!TextUtils.isEmpty(externalSDCardPath)) {
            updateViews();
        } else {
            Toast.makeText(this, "没有可用的外置SD卡", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     *  执行展示fileTree Ui 的逻辑
     **/
    private void updateViews() {
        operateExternalSDViaDocumentTree();
    }

    /**
     * 拿到外置SD卡的路径
     * 具体见http://blog.csdn.net/xingnan4414/article/details/79388972
     **/
    public String externalSDCardPath() {
        try {
            StorageManager storageManager = (StorageManager) getSystemService(Context.STORAGE_SERVICE);
            // getStorageVolumes是7.0才有的方法,getStorageVolumes根据你的编译版本可以使用
            // getVolumePaths,getVolumeList代替，但思路是一样的
            List<StorageVolume> storageVolumes;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                storageVolumes = storageManager.getStorageVolumes();
            } else {
                Toast.makeText(this, "该API只能在7.0手机上有效", Toast.LENGTH_SHORT).show();
                return "";
            }
            Class<?> volumeClass = Class.forName("android.os.storage.StorageVolume");
            Method getPath = volumeClass.getDeclaredMethod("getPath");
            Method isRemovable = volumeClass.getDeclaredMethod("isRemovable");
            getPath.setAccessible(true);
            isRemovable.setAccessible(true);
            //
            String mPath;
            for (int i = 0; i < storageVolumes.size(); i++) {
                StorageVolume storageVolume = storageVolumes.get(i);
                mPath = (String) getPath.invoke(storageVolume);
                Boolean isRemove = (Boolean) isRemovable.invoke(storageVolume);
                if (isRemove) {
                    return mPath;
                }
                Log.d("tag2", "mPath is === " + mPath + "isRemoveble == " + isRemove);
            }
        } catch (Exception e) {
            Log.d("tag2", "e == " + e.getMessage());
        }
        return "";
    }
    /**
     * 执行步骤：首次安装后发送Intent,打开系统的
     * FileTree UI 界面，点击选择同意后返回onActivityResult
     * 拿到Uri并存在SP里面，下次进入后直接从Sp拿即可。
     **/
    public void operateExternalSDViaDocumentTree() {
        Uri storeUri = null;
        if (mSp != null) {
            String uri = mSp.getString(URL_KEY, "");
            if (!TextUtils.isEmpty(uri)) {
                storeUri = Uri.parse(uri);
            }
        }
        if (storeUri != null) {
            //设置adapter,并更新数据和布局
            initData(storeUri);

            fileAdapter = new FileAdapter(mData, this);

            recycler.setAdapter(fileAdapter);
            fileAdapter.notifyDataSetChanged();
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            startActivityForResult(intent, 200);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                //保存一下url,下次就不用再跳到系统Intent页面去初始化。
                mSp.edit().putString(URL_KEY, uri.toString()).apply();
                // 设置获取了持久化的URI权限
                getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION |
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                updateViews();
            }
        }
    }
    /**
     * 第一次初始化整个SDCard的fileTree数据
     * 区别@Method initDecendantsData()这个
     * 方法是为了初始化下一级的文件数据用
     **/
    public void initData(Uri uri) {
        DocumentFile df = DocumentFile.fromTreeUri(this, uri);
        if (df.isDirectory()) {
            mData.clear();//清一下数据，用最新的
            DocumentFile[] documentFiles = df.listFiles();
            // 初始化数据
            for (int i = 0; i < documentFiles.length; i++) {
                FileBean bean = new FileBean();
                //加入每个文件夹的uri
                bean.parentFile = documentFiles[i].getParentFile();
                bean.uri = documentFiles[i].getUri();
                bean.name = documentFiles[i].getName();
                bean.type = documentFiles[i].getType();
                bean.modifyTime = String.valueOf(documentFiles[i].lastModified());
                bean.fileLength = String.valueOf(documentFiles[i].length());
                bean.file = documentFiles[i];
                mData.add(bean);
            }
        }
    }
    /**
     *  点击长按弹出菜单中的“新建”功能后，
     *  弹出一个输入diaolog
     **/
    public AlertDialog initCreateFileDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View itemView = View.inflate(this, R.layout.create_file_layout, null);
        final EditText editText = itemView.findViewById(R.id.edit);
        return mBuilder.setTitle("请输入文件名")
                .setView(itemView)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DocumentFile parentFile = mData.get(0).parentFile;
                        if (parentFile.isDirectory()) {
                            if (!TextUtils.isEmpty(editText.getText())) {
                                parentFile.createDirectory(editText.getText().toString());
                                Toast.makeText(Main3Activity.this, "文件夹创建成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Main3Activity.this, "文件名不能为空", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
    }

    @Override
    public void onClick(View v) {
        dissmissPopWindow();
        switch (v.getId()) {
            case R.id.copy_btn:
                boolean copySuccess = FileFucHelper.copyFileOrFolder(mData.get(currentPosition), Main3Activity.this);
                if (copySuccess) {
                    Toast.makeText(Main3Activity.this, "已复制到同级文件夹，自己慢慢找吧", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.create_btn:
                if (createFileDialog != null && !createFileDialog.isShowing()) {
                    createFileDialog.show();
                }
                break;
            case R.id.delete_btn:
                //数据没有了de话加个保护
                if (mData.size() == 0) {
                    return;
                }
                DocumentFile file = mData.get(currentPosition).file;
                if (file != null && file.exists()) {
                    boolean delete = file.delete();
                    if (delete) {
                        Toast.makeText(Main3Activity.this, "已删除", Toast.LENGTH_SHORT).show();
                    }
                }
                //TODO refresh the current file tree UI status
                break;
            default:
        }

    }

    // 点击文件的话直接执行分享行为
    private void shareIntent(String name) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_STREAM, name);
        startActivity(intent);
    }

    /**
     * 一个集合维护点击加入的DocumentFile，
     * 点击返回键删除顶级的DocumentFile,
     * 当集合为空时候表明为root节点
     * root节点的话配合点击back键的行为
     * 直接返回，否则更新界面为上一级
     **/
    public boolean isRootFileTree() {
        if (mOrderContainer.size() == 0) {
            return true;
        } else {
            return false;
        }
    }
}
