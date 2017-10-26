package com.othershe.groupindexlib.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.View;

import com.othershe.groupindexlib.helper.Utils;
import com.othershe.groupindexlib.listener.OnDrawItemDecorationListener;

import java.util.List;

public class GroupHeaderItemDecoration extends RecyclerView.ItemDecoration {
    private Context mContext;

    private List<String> tags;//列表数据源的tag集合
    private int groupHeaderHeight;//GroupHeader高度
    private int groupHeaderLeftPadding;//GroupHeader的左padding
    private boolean show = true;//是否显示顶部悬浮的GroupHeader

    private Paint mPaint;
    private TextPaint mTextPaint;

    private OnDrawItemDecorationListener drawItemDecorationListener;

    public GroupHeaderItemDecoration(Context context) {
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
        //ItemView的position==0 或者 当前itemView的data的tag和上一个ItemView的不相等，则为当前itemView设置top 偏移量
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
            //和getItemOffsets()里的条件判断类似，开始绘制分组的GroupHeader
            if (!Utils.listIsEmpty(tags) && (position == 0 || !tag.equals(tags.get(position - 1)))) {
                if (drawItemDecorationListener == null) {
                    drawGroupHeader(c, parent, view, tag);
                } else {
                    drawItemDecorationListener.onDrawGroupHeader(c, mPaint, mTextPaint, getGroupHeaderCoordinate(parent, view), position);
                }
            }
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        if (!show) {
            return;
        }
        //列表第一个可见的ItemView位置
        int position = ((LinearLayoutManager) (parent.getLayoutManager())).findFirstVisibleItemPosition();
        String tag = tags.get(position);
        //第一个可见的ItemView
        View view = parent.findViewHolderForAdapterPosition(position).itemView;
        //当前ItemView的data的tag和下一个ItemView的不相等，则代表将要重新绘制悬停的GroupHeader
        boolean flag = false;
        if (!Utils.listIsEmpty(tags) && (position + 1) < tags.size() && !tag.equals(tags.get(position + 1))) {
            //如果第一个可见ItemView的底部坐标小于groupHeaderHeight，则执行Canvas垂直位移操作
            if (view.getBottom() <= groupHeaderHeight) {
                c.save();
                flag = true;
                c.translate(0, view.getHeight() + view.getTop() - groupHeaderHeight);
            }
        }

        if (drawItemDecorationListener == null) {
            drawSuspensionGroupHeader(c, parent, tag);
        } else {
            drawItemDecorationListener.onDrawSuspensionGroupHeader(c, mPaint, mTextPaint, getSuspensionGroupHeaderCoordinate(parent), position);
        }

        if (flag) {
            c.restore();
        }
    }

    public int[] getGroupHeaderCoordinate(RecyclerView parent, View view) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int bottom = view.getTop() - params.topMargin;
        int top = bottom - groupHeaderHeight;
        return new int[]{left, top, right, bottom};
    }

    private void drawGroupHeader(Canvas c, RecyclerView parent, View view, String tag) {
        int[] params = getGroupHeaderCoordinate(parent, view);
        c.drawRect(params[0], params[1], params[2], params[3], mPaint);
        int x = params[0] + groupHeaderLeftPadding;
        int y = params[1] + (groupHeaderHeight + Utils.getTextHeight(mTextPaint, tag)) / 2;
        c.drawText(tag, x, y, mTextPaint);
    }

    public int[] getSuspensionGroupHeaderCoordinate(RecyclerView parent) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int bottom = groupHeaderHeight;
        int top = 0;
        return new int[]{left, top, right, bottom};
    }

    private void drawSuspensionGroupHeader(Canvas c, RecyclerView parent, String tag) {
        int[] params = getSuspensionGroupHeaderCoordinate(parent);
        c.drawRect(params[0], params[1], params[2], params[3], mPaint);
        int x = params[0] + groupHeaderLeftPadding;
        int y = params[1] + (groupHeaderHeight + Utils.getTextHeight(mTextPaint, tag)) / 2;
        c.drawText(tag, x, y, mTextPaint);
    }

    public GroupHeaderItemDecoration setGroupHeaderLeftPadding(int groupHeaderLeftPadding) {
        this.groupHeaderLeftPadding = Utils.dip2px(mContext, groupHeaderLeftPadding);
        return this;
    }

    public GroupHeaderItemDecoration setGroupHeaderTextColor(int groupHeaderTextColor) {
        mTextPaint.setColor(groupHeaderTextColor);
        return this;
    }

    public GroupHeaderItemDecoration setGroupHeaderTextColor(String groupHeaderTextColor) {
        mTextPaint.setColor(Color.parseColor(groupHeaderTextColor));
        return this;
    }

    public GroupHeaderItemDecoration setGroupHeaderTextSize(int groupHeaderTextSize) {
        mTextPaint.setTextSize(groupHeaderTextSize);
        return this;
    }

    public GroupHeaderItemDecoration setGroupHeaderColor(int groupHeaderColor) {
        mPaint.setColor(groupHeaderColor);
        return this;
    }

    public GroupHeaderItemDecoration setGroupHeaderColor(String groupHeaderColor) {
        mPaint.setColor(Color.parseColor(groupHeaderColor));
        return this;
    }

    public GroupHeaderItemDecoration setGroupHeaderHeight(int groupHeaderHeight) {
        this.groupHeaderHeight = Utils.dip2px(mContext, groupHeaderHeight);
        return this;
    }

    public GroupHeaderItemDecoration setTags(List<String> tags) {
        this.tags = tags;
        return this;
    }

    public GroupHeaderItemDecoration showSuspensionGroupHeader(boolean show) {
        this.show = show;
        return this;
    }

    public GroupHeaderItemDecoration setOnDrawItemDecorationListener(OnDrawItemDecorationListener drawItemDecorationListener) {
        this.drawItemDecorationListener = drawItemDecorationListener;
        return this;
    }
}
