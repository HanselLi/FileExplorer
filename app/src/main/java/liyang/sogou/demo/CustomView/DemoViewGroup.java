package liyang.sogou.demo.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by liyangos3323 on 2018/2/23.
 */

public class DemoViewGroup extends ViewGroup {
    public DemoViewGroup(Context context) {
        this(context, null);
    }

    public DemoViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DemoViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }

    /**
     * 1.确定子view大小个数，进而确定父view的大小
     *
     * @param widthMeasureSpec  parent spec
     * @param heightMeasureSpec parent spec
     **/
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (getChildCount() == 0) {
            setMeasuredDimension(0, 0);
        } else {
            if (wMode == MeasureSpec.AT_MOST && hMode == MeasureSpec.AT_MOST) {
                int maxWidth = getMaxWidth();
                int totalHeight = getTotalHeight();
                setMeasuredDimension(maxWidth, totalHeight);
            } else if (hMode == MeasureSpec.AT_MOST) {
                setMeasuredDimension(width, getTotalHeight());
            } else if (wMode == MeasureSpec.AT_MOST) {
                setMeasuredDimension(getMaxWidth(), height);
            }
        }
    }

    private int getTotalHeight() {
        int totalHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            int measuredHeight = getChildAt(i).getMeasuredHeight();
            totalHeight += measuredHeight;
        }
        return totalHeight;
    }

    private int getMaxWidth() {
        int width = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            int measuredWidth = childAt.getMeasuredWidth();
            if (measuredWidth > width) {
                width = measuredWidth;
            }
        }
        return width;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // 用来保存高度
        int currentHeight = t;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            int childMeasuredHeight = child.getMeasuredHeight();
            int childMeasuredWidth = child.getMeasuredWidth();
            child.layout(l + getPaddingLeft(), getPaddingTop()+currentHeight, l + getPaddingLeft() + childMeasuredWidth, t + getPaddingTop() + currentHeight+childMeasuredHeight);
            currentHeight += childMeasuredHeight;
        }
    }


}
