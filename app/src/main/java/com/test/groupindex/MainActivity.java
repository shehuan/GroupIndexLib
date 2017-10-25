package com.test.groupindex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.othershe.groupindexlib.DivideItemDecoration;
import com.othershe.groupindexlib.GroupIndexItemDecoration;
import com.othershe.groupindexlib.OnGroupHeaderViewListener;
import com.othershe.groupindexlib.OnSideBarTouchListener;
import com.othershe.groupindexlib.SideBar;
import com.othershe.groupindexlib.SortHelper;
import com.othershe.groupindexlib.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView list = (RecyclerView) findViewById(R.id.list);
        SideBar sideBar = (SideBar) findViewById(R.id.side_bar);
        final TextView tip = (TextView) findViewById(R.id.tip);

        final List<ItemData> datas = new ArrayList<>();
        ItemData data = new ItemData("北京");
        datas.add(data);
        ItemData data1 = new ItemData("上海");
        datas.add(data1);
        ItemData data2 = new ItemData("广州");
        datas.add(data2);
        ItemData data3 = new ItemData("深圳");
        datas.add(data3);
        ItemData data4 = new ItemData("西安");
        datas.add(data4);
        ItemData data5 = new ItemData("成都");
        datas.add(data5);
        ItemData data6 = new ItemData("南京");
        datas.add(data6);
        ItemData data7 = new ItemData("三亚");
        datas.add(data7);
        ItemData data8 = new ItemData("开封");
        datas.add(data8);
        ItemData data9 = new ItemData("杭州");
        datas.add(data9);
        ItemData data10 = new ItemData("嘉兴");
        datas.add(data10);
        ItemData data11 = new ItemData("兰州");
        datas.add(data11);
        ItemData data12 = new ItemData("新疆");
        datas.add(data12);
        ItemData data13 = new ItemData("西藏");
        datas.add(data13);
        ItemData data14 = new ItemData("昆明");
        datas.add(data14);
        ItemData data15 = new ItemData("大理");
        datas.add(data15);
        ItemData data16 = new ItemData("桂林");
        datas.add(data16);
        ItemData data17 = new ItemData("东莞");
        datas.add(data17);
        ItemData data18 = new ItemData("台湾");
        datas.add(data18);
        ItemData data19 = new ItemData("香港");
        datas.add(data19);
        ItemData data20 = new ItemData("澳门");
        datas.add(data20);
        ItemData data21 = new ItemData("宝鸡");
        datas.add(data21);
        ItemData data22 = new ItemData("蚌埠");
        datas.add(data22);
        ItemData data23 = new ItemData("钓鱼岛");
        datas.add(data23);
        ItemData data24 = new ItemData("安康");
        datas.add(data24);
        ItemData data25 = new ItemData("苏州");
        datas.add(data25);
        ItemData data26 = new ItemData("青岛");
        datas.add(data26);
        ItemData data27 = new ItemData("郑州");
        datas.add(data27);
        ItemData data28 = new ItemData("洛阳");
        datas.add(data28);
        ItemData data29 = new ItemData("石家庄");
        datas.add(data29);
        ItemData data30 = new ItemData("乌鲁木齐");
        datas.add(data30);
        ItemData data31 = new ItemData("武汉");
        datas.add(data31);
        ItemData data32 = new ItemData("←_←");
        datas.add(data32);
        ItemData data33 = new ItemData("⊙﹏⊙");
        datas.add(data33);
        ItemData data34 = new ItemData("Hello China");
        datas.add(data34);
        ItemData data35 = new ItemData("宁波");
        datas.add(data35);

        SortHelper<ItemData> sortHelper = new SortHelper<ItemData>() {
            @Override
            public String sortField(ItemData data) {
                return data.getTitle();
            }
        };
        sortHelper.sortByLetter(datas);
        List<String> tags = sortHelper.getTags(datas);

        MyAdapter adapter = new MyAdapter(this, datas, false);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        list.setLayoutManager(layoutManager);
        list.addItemDecoration(new DivideItemDecoration().setTags(tags));
        list.addItemDecoration(new GroupIndexItemDecoration(this)
                .setTags(tags)
                .setGroupHeaderHeight(30)
                .setGroupHeaderLeftPadding(20));
        list.setAdapter(adapter);

        sideBar.setOnSideBarTouchListener(tags, new OnSideBarTouchListener() {
            @Override
            public void onTouch(String text, int position) {
                tip.setVisibility(View.VISIBLE);
                tip.setText(text);
                if ("↑".equals(text)) {
                    layoutManager.scrollToPositionWithOffset(0, 0);
                    return;
                }
                if (position != -1) {
                    layoutManager.scrollToPositionWithOffset(position, 0);
                }
            }

            @Override
            public void onTouchEnd() {
                tip.setVisibility(View.GONE);
            }
        });
    }
}
