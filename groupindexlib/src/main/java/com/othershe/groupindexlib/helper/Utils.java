package com.othershe.groupindexlib.helper;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextPaint;

import java.util.List;

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

    /**
     * 测量字符宽度
     *
     * @param textPaint
     * @param text
     * @return
     */
    public static int getTextWidth(TextPaint textPaint, String text) {
        return (int) textPaint.measureText(text);
    }

    public static boolean listIsEmpty(List<String> list) {
        return list == null || list.size() == 0;
    }
}
