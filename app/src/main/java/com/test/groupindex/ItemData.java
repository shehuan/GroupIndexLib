package com.test.groupindex;

import com.othershe.groupindexlib.bean.BaseItem;

public class ItemData extends BaseItem {

    private String title;

    public ItemData(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
