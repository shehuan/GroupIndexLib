package com.othershe.groupindexlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

public class SideBar extends View {

    private int INDEX_COLOR = Color.BLACK;//索引字符颜色
    private int TOUCH_COLOR = Color.parseColor("#88888888");//SideBar被触摸时的背景色
    private int UNTOUCH_COLOR = Color.TRANSPARENT;//SideBar默认背景色
    private int INDEX_SIZE = (int) TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics());

    //索引字符数组
    public String[] indexArray = {"↑", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};

    private int mWidth;//字符所在区域宽度
    private float mHeight;//字符所在区域高度
    private float mMarginTop;//顶部间距

    private TextPaint mTextPaint;

    private int maxWidth, maxHeight;

    private OnSideBarTouchListener onSideBarTouchListener;

    public SideBar(Context context) {
        this(context, null);
    }

    public SideBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SideBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SideBar, 0, 0);
        for (int i = 0; i < ta.getIndexCount(); i++) {
            int attr = ta.getIndex(i);
            if (attr == R.styleable.SideBar_text_size) {
                INDEX_SIZE = ta.getDimensionPixelSize(attr, INDEX_SIZE);
            } else if (attr == R.styleable.SideBar_text_color) {
                INDEX_COLOR = ta.getColor(attr, INDEX_COLOR);
            } else if (attr == R.styleable.SideBar_touch_color) {
                TOUCH_COLOR = ta.getColor(attr, TOUCH_COLOR);
            } else if (attr == R.styleable.SideBar_untouch_color) {
                UNTOUCH_COLOR = ta.getColor(attr, UNTOUCH_COLOR);
            }
        }
        ta.recycle();

        mTextPaint = new TextPaint();
        mTextPaint.setColor(INDEX_COLOR);
        mTextPaint.setTextSize(INDEX_SIZE);
        mTextPaint.setAntiAlias(true);
        setBackgroundColor(UNTOUCH_COLOR);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = (h * 1.0f / indexArray.length);
        mMarginTop = (h - mHeight * indexArray.length) / 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        //重新计算SideBar宽高
        if (heightMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.AT_MOST) {
            getMaxTextSize();
            if (heightMode == MeasureSpec.AT_MOST) {
                heightSize = (maxHeight + 15) * indexArray.length;
            }

            if (widthMode == MeasureSpec.AT_MOST) {
                widthSize = maxWidth + 10;
            }
        }

        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < indexArray.length; i++) {
            String index = indexArray[i];
            float x = (mWidth - mTextPaint.measureText(index)) / 2;
            float y = mMarginTop + mHeight * i + (mHeight + Utils.getTextHeight(mTextPaint, index)) / 2;//(mTextPaint.descent() - mTextPaint.ascent())
            canvas.drawText(index, x, y, mTextPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                // 按下字母的下标
                int pos = (int) ((event.getY() - mMarginTop) / mHeight);
                if (pos >= 0 && pos < indexArray.length) {
                    setBackgroundColor(TOUCH_COLOR);
                    if (onSideBarTouchListener != null) {
                        onSideBarTouchListener.onTouch(indexArray[pos]);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                setBackgroundColor(UNTOUCH_COLOR);
                if (onSideBarTouchListener != null) {
                    onSideBarTouchListener.onTouchEnd();
                }
                break;
        }

        return true;
    }

    /**
     * 计算索引字符的最大宽度、高度
     */
    private void getMaxTextSize() {
        for (String index : indexArray) {
            maxWidth = (int) Math.max(maxWidth, mTextPaint.measureText(index));
            maxHeight = Math.max(maxHeight, Utils.getTextHeight(mTextPaint, index));
        }
    }

    public void setIndexsArray(String[] indexArray) {
        this.indexArray = indexArray;
    }

    public void setOnSideBarTouchListener(OnSideBarTouchListener onSideBarTouchListener) {
        this.onSideBarTouchListener = onSideBarTouchListener;
    }
}
