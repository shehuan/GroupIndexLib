package com.othershe.groupindexlib;

import com.github.promeg.pinyinhelper.Pinyin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class SortHelper<T extends BaseItem> {


    public void sortByLetter(List<T> datas) {
        for (T data : datas) {

            char fist = sortField(data).toCharArray()[0];
            if (String.valueOf(fist).matches("[A-Za-z]")) {
                data.setTag(String.valueOf(fist).toUpperCase());
            } else if (Pinyin.isChinese(fist)) {
                data.setTag(Pinyin.toPinyin(fist).substring(0, 1));
            } else {
                //特殊字符情况
                data.setTag("#");
            }
        }
        Collections.sort(datas, new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                if (o1.getTag().equals("#")) {
                    return 1;
                } else if (o2.getTag().equals("#")) {
                    return -1;
                } else {
                    return o1.getTag().compareTo(o2.getTag());
                }
            }
        });
    }

    public List<String> getTags(List<T> datas) {
        List<String> tags = new ArrayList<>();
        for (BaseItem data : datas) {
            tags.add(data.getTag());
        }
        return tags;
    }

    public abstract String sortField(T data);
}
