package com.othershe.groupindexlib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.View;

import java.util.List;

public class GroupIndexItemDecoration extends RecyclerView.ItemDecoration {
    private Context mContext;

    private List<String> tags;//列表数据源的tag集合
    private int groupHeaderHeight;//GroupIndex高度
    private int groupHeaderLeftPadding;//GroupIndex高度左边的padding
    private boolean show = true;//是否显示顶部悬浮的GroupIndex

    private Paint mPaint;
    private TextPaint mTextPaint;

    public GroupIndexItemDecoration(Context context) {
        mContext = context;
        groupHeaderHeight = Utils.dip2px(context, 20);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#FFEEEEEE"));

        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.parseColor("#FF999999"));
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

        int position = parent.getChildAdapterPosition(view);
        //itemView的position==0 或者 当前itemView的data的tag和上一个itemView的不相等，则为当前itemView设置top padding值
        if (!Utils.listIsEmpty(tags) && (position == 0 || !tags.get(position).equals(tags.get(position - 1)))) {
            outRect.set(0, groupHeaderHeight, 0, 0);
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        for (int i = 0; i < parent.getChildCount(); i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);
            String tag = tags.get(position);
            //和getItemOffsets()里的条件判断类似，开始绘制分组item的头
            if (!Utils.listIsEmpty(tags) && (position == 0 || !tag.equals(tags.get(position - 1)))) {
                drawGroupHeader(c, parent, view, tag);
            }
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        if (!show) {
            return;
        }
        //列表第一个可见的itemView位置
        int position = ((LinearLayoutManager) (parent.getLayoutManager())).findFirstVisibleItemPosition();
        String tag = tags.get(position);
        View view = parent.findViewHolderForAdapterPosition(position).itemView;
        //当前itemView的data的tag和下一个itemView的不相等，则代表将要重新绘制悬停的item的头
        boolean flag = false;
        if (!Utils.listIsEmpty(tags) && (position + 1) < tags.size() && !tag.equals(tags.get(position + 1))) {
            if (view.getBottom() <= groupHeaderHeight) {
                c.save();
                flag = true;
                c.translate(0, view.getHeight() + view.getTop() - groupHeaderHeight);
            }
        }

        drawSuspensionGroupHeader(c, parent, tag);

        if (flag) {
            c.restore();
        }
    }

    private void drawGroupHeader(Canvas c, RecyclerView parent, View view, String tag) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int bottom = view.getTop() - params.topMargin;
        int top = bottom - groupHeaderHeight;
        c.drawRect(left, top, right, bottom, mPaint);
        int x = left + groupHeaderLeftPadding;
        int y = top + (groupHeaderHeight + Utils.getTextHeight(mTextPaint, tag)) / 2;
        c.drawText(tag, x, y, mTextPaint);
    }

    private void drawSuspensionGroupHeader(Canvas c, RecyclerView parent, String tag) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int bottom = groupHeaderHeight;
        int top = 0;
        c.drawRect(left, top, right, bottom, mPaint);
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

    public GroupIndexItemDecoration setGroupHeaderColor(String groupHeaderColor) {
        mPaint.setColor(Color.parseColor(groupHeaderColor));
        return this;
    }

    public GroupIndexItemDecoration setGroupHeaderHeight(int groupHeaderHeight) {
        this.groupHeaderHeight = Utils.dip2px(mContext, groupHeaderHeight);
        return this;
    }

    public GroupIndexItemDecoration setTags(List<String> tags) {
        this.tags = tags;
        return this;
    }

    public GroupIndexItemDecoration showSuspensionGroupIndex(boolean show) {
        this.show = show;
        return this;
    }
}
