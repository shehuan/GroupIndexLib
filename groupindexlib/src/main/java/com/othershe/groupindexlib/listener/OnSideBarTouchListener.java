package com.othershe.groupindexlib.listener;

public interface OnSideBarTouchListener {
    /**
     * 触摸SideBar时回调
     *
     * @param text     SideBar上选中的索引字符
     * @param position RecyclerView将要滚动到的位置(-1代表未找到目标位置，则列表不用滚动)
     */
    void onTouch(String text, int position);

    /**
     * 触摸结束回调
     */
    void onTouchEnd();
}
