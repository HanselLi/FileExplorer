package liyang.sogou.demo.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by liyangos3323 on 2018/2/23.
 */

public class DemoView extends View {
    private int defaultSize = 100;
    private Paint mPaint;

    public DemoView(Context context) {
        this(context, null);
    }

    public DemoView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DemoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);
//        inflate()
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int adaptWidth = getAdaptSize(wMode, width);
        int adaptHeight = getAdaptSize(hMode, height);
        setMeasuredDimension(Math.min(adaptHeight, adaptWidth), Math.min(adaptHeight, adaptWidth));
    }

    private int getAdaptSize(int mode, int size) {
        int result = defaultSize;
        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
                // 使用默认尺寸
                result = defaultSize;
                break;
            case MeasureSpec.AT_MOST:
                result = size;
                break;
            case MeasureSpec.EXACTLY:
                result = size;
                break;
            default:
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("tag","left = "+getX() + " top == "+getY()+" r == "+getMeasuredHeight()/2);
        canvas.drawCircle((getX()+getMeasuredHeight()/2), (getMeasuredHeight()/2), getMeasuredHeight()/2, mPaint);
    }
}
