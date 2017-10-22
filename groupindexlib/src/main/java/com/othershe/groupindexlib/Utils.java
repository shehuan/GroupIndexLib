package com.othershe.groupindexlib;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextPaint;

public class Utils {
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 测量字符高度
     *
     * @param text
     * @return
     */
    public static int getTextHeight(TextPaint textPaint, String text) {
        Rect bounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.height();
    }
}
