package com.othershe.groupindexlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

public class SideBar extends View {

    private int LETTER_COLOR = Color.BLUE;//索引字符颜色
    private int TOUCH_COLOR = Color.parseColor("#88888888");//SideBar被触摸时的背景色
    private int UNTOUCH_COLOR = Color.TRANSPARENT;//SideBar默认背景色
    private int LETTER_SIZE = (int) TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics());

    //索引字符数组
    public String[] indexArray = {"↑", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};

    private Context mContext;

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
        mContext = context;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SideBar, 0, 0);
        for (int i = 0; i < ta.getIndexCount(); i++) {
            int attr = ta.getIndex(i);
            if (attr == R.styleable.SideBar_text_size) {
                LETTER_SIZE = ta.getDimensionPixelSize(attr, LETTER_SIZE);
            } else if (attr == R.styleable.SideBar_text_color) {
                LETTER_COLOR = ta.getColor(attr, LETTER_COLOR);
            } else if (attr == R.styleable.SideBar_touch_color) {
                TOUCH_COLOR = ta.getColor(attr, TOUCH_COLOR);
            } else if (attr == R.styleable.SideBar_untouch_color) {
                UNTOUCH_COLOR = ta.getColor(attr, UNTOUCH_COLOR);
            }
        }
        ta.recycle();

        mTextPaint = new TextPaint();
        mTextPaint.setColor(LETTER_COLOR);
        mTextPaint.setTextSize(LETTER_SIZE);
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
            String letter = indexArray[i];
            float x = (mWidth - mTextPaint.measureText(letter)) / 2;
            float y = mMarginTop + mHeight * i + (mHeight + getTextHeight(letter)) / 2;//(mTextPaint.descent() - mTextPaint.ascent())
            canvas.drawText(letter, x, y, mTextPaint);
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
                        onSideBarTouchListener.onTouch(indexArray[pos], pos);
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
     * 测量字符高度
     *
     * @param text
     * @return
     */
    public int getTextHeight(String text) {
        Rect bounds = new Rect();
        mTextPaint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.height();
    }

    /**
     * 计算索引字符的最大宽度、高度
     */
    private void getMaxTextSize() {
        for (String letter : indexArray) {
            maxWidth = (int) Math.max(maxWidth, mTextPaint.measureText(letter));
            maxHeight = Math.max(maxHeight, getTextHeight(letter));
        }
    }

    public void setIndexArray(String[] indexArray) {
        this.indexArray = indexArray;
    }

    public void setOnSideBarTouchListener(OnSideBarTouchListener onSideBarTouchListener) {
        this.onSideBarTouchListener = onSideBarTouchListener;
    }
}
