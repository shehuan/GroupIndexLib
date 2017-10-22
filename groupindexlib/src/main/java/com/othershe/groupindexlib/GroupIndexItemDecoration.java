package com.othershe.groupindexlib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;

import java.util.List;

public class GroupIndexItemDecoration extends RecyclerView.ItemDecoration {
    private Context mContext;

    private List<ItemData> datas;
    private int groupHeaderHeight;
    private int groupHeaderLeftPadding;

    private Paint mPaint;
    private TextPaint mTextPaint;

    public GroupIndexItemDecoration(Context context) {
        mContext = context;
        groupHeaderHeight = Utils.dip2px(context, 20);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#FFAAAAAA"));

        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.parseColor("#FF888888"));
        mTextPaint.setTextSize(Utils.sp2px(context, 14));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        RecyclerView.LayoutManager manager = parent.getLayoutManager();

        //只处理线性垂直类型的列表
        if ((manager instanceof LinearLayoutManager)
                && LinearLayoutManager.VERTICAL != ((LinearLayoutManager) manager).getOrientation()) {
            return;
        }

        int cp = parent.getChildAdapterPosition(view);
        if (cp == 0) {
            //给cp等于0的itemView设置padding值
            outRect.set(0, groupHeaderHeight, 0, 0);
        } else if (datas.get(cp).getTag() != null && !datas.get(cp).getTag().equals(datas.get(cp - 1).getTag())) {
            //当前itemView的data的tag和上一个item的不相等，则为当前itemView设置padding值
            outRect.set(0, groupHeaderHeight, 0, 0);
        } else {
            outRect.set(0, 0, 0, 0);
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        for (int i = 0; i < parent.getChildCount(); i++) {
            View view = parent.getChildAt(i);
            int cp = parent.getChildAdapterPosition(view);
            //和getItemOffsets()里的条件判断类似，开始绘制分组item的头
            if (cp == 0) {
                drawGroupHeader(c, parent, view, cp);
            } else if (datas.get(cp).getTag() != null && !datas.get(cp).getTag().equals(datas.get(cp - 1).getTag())) {
                drawGroupHeader(c, parent, view, cp);
            }
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        //列表第一个可见的itemView位置
        int ip = ((LinearLayoutManager)(parent.getLayoutManager())).findFirstVisibleItemPosition();
        String tag = datas.get(ip).getTag();
        View view1 = parent.findViewHolderForLayoutPosition(ip).itemView;
        //当前itemView的data的tag和下一个itemView的不相等，则代表将要重新绘制悬停的item的头
        boolean flag = false;
        if (!TextUtils.isEmpty(tag) && !tag.equals(datas.get(ip+1).getTag())){
            if (view1.getBottom() <= groupHeaderHeight){
                c.save();
                flag = true;
                c.translate(0,view1.getHeight() + view1.getTop() - groupHeaderHeight);
            }
        }

        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        int bottom =groupHeaderHeight;
        int top = 0;
        c.drawRect(left, top, right, bottom, mPaint);
        int x = left + groupHeaderLeftPadding;
        int y = top + (groupHeaderHeight + Utils.getTextHeight(mTextPaint, tag)) / 2;
        c.drawText(tag, x, y, mTextPaint);

        if (flag){
            c.restore();
        }
    }

    private void drawGroupHeader(Canvas c, RecyclerView parent, View view, int position) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        int bottom = view.getTop() - params.topMargin;
        int top = bottom - groupHeaderHeight;
        c.drawRect(left, top, right, bottom, mPaint);
        String tag = datas.get(position).getTag();
        int x = left + groupHeaderLeftPadding;
        int y = top + (groupHeaderHeight + Utils.getTextHeight(mTextPaint, tag)) / 2;
        c.drawText(tag, x, y, mTextPaint);
    }

    public GroupIndexItemDecoration setGroupHeaderLeftPadding(int groupHeaderLeftPadding) {
        this.groupHeaderLeftPadding = Utils.dip2px(mContext, groupHeaderLeftPadding);
        return this;
    }

    public GroupIndexItemDecoration setGroupHeaderTextColor(int groupHeaderTextColor) {
        mTextPaint.setColor(groupHeaderTextColor);
        return this;
    }

    public GroupIndexItemDecoration setGroupHeaderTextSize(int groupHeaderTextSize) {
        mTextPaint.setTextSize(groupHeaderTextSize);
        return this;
    }

    public GroupIndexItemDecoration setGroupHeaderColor(int groupHeaderColor) {
        mPaint.setColor(groupHeaderColor);
        return this;
    }

    public GroupIndexItemDecoration setGroupHeaderHeight(int groupHeaderHeight) {
        this.groupHeaderHeight = Utils.dip2px(mContext, groupHeaderHeight);
        return this;
    }

    public GroupIndexItemDecoration setDatas(List<ItemData> datas) {
        this.datas = datas;
        return this;
    }
}
