package com.othershe.groupindexlib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import java.util.List;

public class GroupIndexItemDecoration extends RecyclerView.ItemDecoration {
    private Context mContext;

    private List<ItemData> datas;
    private int groupHeaderHeight;//GroupIndex高度
    private int groupHeaderLeftPadding;//GroupIndex高度左边的padding
    private boolean show = true;//是否显示顶部悬浮的GroupIndex

    private Paint mPaint;
    private TextPaint mTextPaint;

    @LayoutRes
    private int layoutId;
    private OnGroupHeaderViewListener groupHeaderViewListener;

    public GroupIndexItemDecoration(Context context) {
        mContext = context;
        groupHeaderHeight = Utils.dip2px(context, 20);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#FFDDDDDD"));

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

        int position = parent.getChildAdapterPosition(view);
        if (position == 0) {
            //给cp等于0的itemView设置padding值
            outRect.set(0, groupHeaderHeight, 0, 0);
        } else if (datas.get(position).getTag() != null && !datas.get(position).getTag().equals(datas.get(position - 1).getTag())) {
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
            if (cp == 0 || (datas.get(cp).getTag() != null && !datas.get(cp).getTag().equals(datas.get(cp - 1).getTag()))) {
                drawGroupHeader(c, parent, view, cp);
            }
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (!show) {
            return;
        }
        //列表第一个可见的itemView位置
        int position = ((LinearLayoutManager) (parent.getLayoutManager())).findFirstVisibleItemPosition();
        String tag = datas.get(position).getTag();
        View view = parent.findViewHolderForAdapterPosition(position).itemView;
        //当前itemView的data的tag和下一个itemView的不相等，则代表将要重新绘制悬停的item的头
        boolean flag = false;
        if (!TextUtils.isEmpty(tag) && !tag.equals(datas.get(position + 1).getTag())) {
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

    private void drawGroupHeader(Canvas c, RecyclerView parent, View view, int position) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int bottom = view.getTop() - params.topMargin;
        int top = bottom - groupHeaderHeight;
        c.drawRect(left, top, right, bottom, mPaint);
        String tag = datas.get(position).getTag();
        int x = left + groupHeaderLeftPadding;
        int y = top + (groupHeaderHeight + Utils.getTextHeight(mTextPaint, tag)) / 2;
        c.drawText(tag, x, y, mTextPaint);
    }

    private void drawGroupHeader1(Canvas c, RecyclerView parent, View view, int position) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        int bottom = view.getTop() - params.topMargin;
        int top = bottom - groupHeaderHeight;

        View view1 = LayoutInflater.from(mContext).inflate(layoutId, null, false);
        groupHeaderViewListener.onViewConvert(ViewHolder.create(view1), datas.get(position).getTag());
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(right - left, View.MeasureSpec.EXACTLY);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(groupHeaderHeight, View.MeasureSpec.EXACTLY);
        view1.measure(widthMeasureSpec, heightMeasureSpec);
        view1.layout(left, top, right, bottom);
        view1.draw(c);
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

    private void drawSuspensionGroupHeader(Canvas c, RecyclerView parent) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int bottom = groupHeaderHeight;
        int top = 0;
        View view1 = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(right - left, View.MeasureSpec.EXACTLY);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(groupHeaderHeight, View.MeasureSpec.EXACTLY);
        view1.measure(widthMeasureSpec, heightMeasureSpec);
        view1.layout(left, top, right, bottom);
        view1.setTop(top);
        view1.draw(c);
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

    public GroupIndexItemDecoration showSuspensionGroupIndex(boolean show) {
        this.show = show;
        return this;
    }

    public GroupIndexItemDecoration setOnGroupHeaderViewListener(int layoutId, OnGroupHeaderViewListener groupHeaderViewListener) {
        this.layoutId = layoutId;
        this.groupHeaderViewListener = groupHeaderViewListener;
        return this;
    }
}
