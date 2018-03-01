package liyang.sogou.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import liyang.sogou.demo.CustomView.DemoListView;
import liyang.sogou.demo.CustomView.DemoView;

public class Main2Activity extends AppCompatActivity implements DemoListView.OnDeleteListener {

    private DemoListView listView;
    private List<String> mData;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        listView = (DemoListView) findViewById(R.id.list);
        initData();
        initAdapter();
        listView.setOnDeleteListener(this);
    }

    private void initAdapter() {
        myAdapter = new MyAdapter();
        listView.setAdapter(myAdapter);
    }

    private void initData() {
        mData = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mData.add(" 测试数据 == " + i);
        }
    }

    @Override
    public void onDelete(int selectedPosition) {
        //delete button 的点击事件不会走DemoListView的onTouchListener,会导致button不能被正常移除，需要在回调中处理
        ViewGroup selectedView = (ViewGroup) listView.getChildAt(selectedPosition);
        for (int i = 0; i < selectedView.getChildCount(); i++) {
            View selectedViewChildAt = selectedView.getChildAt(i);
            if (selectedViewChildAt instanceof Button && selectedViewChildAt != null){
                Log.d("tag","remove button =============");
                selectedView.removeView(selectedViewChildAt);
            }
        }
        mData.remove(selectedPosition);
        myAdapter.notifyDataSetChanged();
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public String getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null){
                holder = new ViewHolder();
                convertView = View.inflate(getBaseContext(),R.layout.item_layout,null);
                holder.tv = convertView.findViewById(R.id.tv);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }
            holder.tv.setText(mData.get(position));
            return convertView;
        }
        class ViewHolder{
            TextView tv;
        }
    }

}
