package com.othershe.groupindexlib;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

public class GroupIndexItemDecoration extends RecyclerView.ItemDecoration {
    private int itemHeight;
    private List<BaseBean> datas;

    public GroupIndexItemDecoration(int itemHeight, List<BaseBean> datas) {
        this.itemHeight = itemHeight;
        this.datas = datas;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        RecyclerView.LayoutManager manager = parent.getLayoutManager();
        int cp = parent.getChildAdapterPosition(view);

        if (!(manager instanceof LinearLayoutManager)) {
            return;
        }

        if (cp == 0) {
            //给cp等于0的itemView设置padding值
            outRect.set(0, itemHeight, 0, 0);
        } else if (datas.get(cp).getTag() != null && !datas.get(cp).getTag().equals(datas.get(cp - 1).getTag())) {
            //当前itemView的data的tag和上一个item的不相等，则为当前itemView设置padding值
            outRect.set(0, itemHeight, 0, 0);
        } else {
            outRect.set(0, 0, 0, 0);
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }
}
