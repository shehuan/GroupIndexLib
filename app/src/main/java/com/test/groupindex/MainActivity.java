package com.test.groupindex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.othershe.groupindexlib.GroupIndexItemDecoration;
import com.othershe.groupindexlib.ItemData;
import com.othershe.groupindexlib.OnSideBarTouchListener;
import com.othershe.groupindexlib.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mList = (RecyclerView) findViewById(R.id.list);
        SideBar sideBar = (SideBar) findViewById(R.id.side_bar);
        final TextView tip = (TextView) findViewById(R.id.tip);

        final List<ItemData> datas = new ArrayList<>();
        final ItemData data = new ItemData("Hello", "H");
        datas.add(data);
        ItemData data1 = new ItemData("World", "W");
        datas.add(data1);
        ItemData data2 = new ItemData("Android", "A");
        datas.add(data2);
        ItemData data3 = new ItemData("Python", "P");
        datas.add(data3);
        ItemData data4 = new ItemData("Apple", "A");
        datas.add(data4);
        ItemData data5 = new ItemData("Java", "J");
        datas.add(data5);
        ItemData data6 = new ItemData("JavaScript", "J");
        datas.add(data6);
        ItemData data7 = new ItemData("Hi", "H");
        datas.add(data7);
        ItemData data8 = new ItemData("AK47", "A");
        datas.add(data8);
        ItemData data9 = new ItemData("JJJ", "J");
        datas.add(data9);
        ItemData data10 = new ItemData("Web", "W");
        datas.add(data10);
        ItemData data11 = new ItemData("Ppp", "P");
        datas.add(data11);
        ItemData data12 = new ItemData("How", "H");
        datas.add(data12);
        ItemData data13 = new ItemData("What", "W");
        datas.add(data13);
        ItemData data14 = new ItemData("Php", "P");
        datas.add(data14);
        ItemData data15 = new ItemData("WWW", "W");
        datas.add(data15);
        ItemData data16 = new ItemData("Jiaozi", "J");
        datas.add(data16);
        ItemData data17 = new ItemData("Jack", "J");
        datas.add(data17);
        ItemData data18 = new ItemData("Baidu", "B");
        datas.add(data18);
        ItemData data19 = new ItemData("Pause", "P");
        datas.add(data19);
        ItemData data20 = new ItemData("Back", "B");
        datas.add(data20);
        ItemData data21 = new ItemData("Biu", "B");
        datas.add(data21);

        Collections.sort(datas, new Comparator<ItemData>() {
            @Override
            public int compare(ItemData o1, ItemData o2) {
                return o1.getTag().compareTo(o2.getTag());
            }
        });

        MyAdapter adapter = new MyAdapter(this, datas, false);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mList.setLayoutManager(layoutManager);
        mList.addItemDecoration(new GroupIndexItemDecoration(this)
                .setDatas(datas)
                .setGroupHeaderHeight(30)
                .setGroupHeaderLeftPadding(20));
        mList.setAdapter(adapter);

        sideBar.setOnSideBarTouchListener(new OnSideBarTouchListener() {
            @Override
            public void onTouch(String letter, int position) {
                tip.setVisibility(View.VISIBLE);
                tip.setText(letter);
                if (position == 0){
                    layoutManager.scrollToPositionWithOffset(0, 0);
                    return;
                }
                for (int i = 0; i < datas.size(); i++) {
                    if (datas.get(i).getTag().equals(letter)) {
                        layoutManager.scrollToPositionWithOffset(i, 0);
                        return;
                    }
                }
            }

            @Override
            public void onTouchEnd() {
                tip.setVisibility(View.GONE);
            }
        });
    }
}
