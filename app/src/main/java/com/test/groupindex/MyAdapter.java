package com.test.groupindex;

import android.content.Context;

import com.othershe.baseadapter.ViewHolder;
import com.othershe.baseadapter.base.CommonBaseAdapter;
import com.othershe.groupindexlib.BaseItem;

import java.util.List;

public class MyAdapter extends CommonBaseAdapter<ItemData> {
    public MyAdapter(Context context, List<ItemData> datas, boolean isOpenLoadMore) {
        super(context, datas, isOpenLoadMore);
    }

    @Override
    protected void convert(ViewHolder holder, ItemData data, int position) {
        holder.setText(R.id.title, data.getTitle());
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_layout;
    }
}
