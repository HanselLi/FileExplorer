package liyang.sogou.demo.FileExplorer;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import liyang.sogou.demo.R;

/**
 * Created by liyangos3323 on 2018/2/27.
 */

public class FileAdapter extends RecyclerView.Adapter<MyHolder> {

    private List<FileBean> mData;
    private final Context mCxt;
    private OnItemClickListener listener;
    private OnItemLongClickListener longClickListener;

    public FileAdapter(List<FileBean> mData, Context context) {
        this.mData = mData;
        this.mCxt = context;
    }
    // 主动通知adapter数据变化并更新界面
    public void setDataSource(List<FileBean> list) {
        mData = list;
        notifyDataSetChanged();
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = View.inflate(mCxt, R.layout.item_file_tree, null);
        return new MyHolder(root);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {
        /*if (listener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(holder.getLayoutPosition(),holder.itemView);
                }
            });
        }
        if (longClickListener != null){
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //TODO 展示一个dialog，有复制，剪贴，重命名，删除功能
                    longClickListener.onItemLongClick(holder.getLayoutPosition(),holder.itemView);
                    return false;
                }
            });
        }*/
        boolean isDirectory = mData.get(position).file.isDirectory();
        if (!isDirectory) {
            holder.img.setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeResource(mCxt.getResources(), R.mipmap.file)));
        } else {
            holder.img.setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeResource(mCxt.getResources(), R.mipmap.folder)));
        }
        holder.itemView.setTag(position);
        holder.name.setText(mData.get(position).name);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s = format.format(Long.valueOf(mData.get(position).modifyTime));
        holder.time.setText(s);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener){
        this.longClickListener = longClickListener;
    }

    interface OnItemClickListener {
        void onItemClick(int position, View itemView);
    }
    interface  OnItemLongClickListener{
        void onItemLongClick(int layoutPosition, View itemView);
    }
}


class MyHolder extends RecyclerView.ViewHolder {
    TextView name;
    TextView time;
    ImageView img;

    public MyHolder(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.name);
        time = itemView.findViewById(R.id.time);
        img = itemView.findViewById(R.id.img);
    }
}
