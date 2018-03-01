package liyang.sogou.demo.CustomView;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * Created by liyangos3323 on 2018/2/24.
 */

public class DemoListView extends ListView implements GestureDetector.OnGestureListener, View.OnClickListener, View.OnTouchListener {

    private GestureDetector gestureDetector;
    private boolean isDeleteShow;
    int selectedPosition;
    private Button deleteButton;
    private ViewGroup itemLayout;
    private OnDeleteListener listener;

    public DemoListView(Context context) {
        this(context, null);
    }

    public DemoListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DemoListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        gestureDetector = new GestureDetector(getContext(), this);
        setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (isDeleteShow) {
            Log.d("tag","=========================================================");
            // 更新一下itemView的位置
            selectedPosition = pointToPosition((int) event.getX(), (int) event.getY());
            //TODO remove current item view
            itemLayout.removeView(deleteButton);
            deleteButton = null;
            isDeleteShow = false;
            return false;
        } else {
            return gestureDetector.onTouchEvent(event);
        }
    }


    @Override
    public boolean onDown(MotionEvent e) {
        if (!isDeleteShow) {
            selectedPosition = pointToPosition((int) e.getX(), (int) e.getY());
        }
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (!isDeleteShow && Math.abs(velocityX) > Math.abs(velocityY) && velocityX < 0) {

            deleteButton = new Button(getContext());
            deleteButton.setText("Delete");
            deleteButton.setTextColor(Color.BLUE);
            deleteButton.setOnClickListener(this);


            Log.d("tag", " =selectedPosition= " + selectedPosition + " =getFirstVisiblePosition= " + getFirstVisiblePosition());
            itemLayout = (ViewGroup) getChildAt(selectedPosition - getFirstVisiblePosition());
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            itemLayout.addView(deleteButton, params);
            isDeleteShow = true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onDelete(selectedPosition);
        }
    }


    public interface OnDeleteListener {
        void onDelete(int selectedPosition);
    }

    public void setOnDeleteListener(OnDeleteListener onDeleteListener) {
        this.listener = onDeleteListener;
    }
}
