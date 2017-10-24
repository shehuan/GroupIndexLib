package com.test.groupindex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.othershe.groupindexlib.DivideItemDecoration;
import com.othershe.groupindexlib.GroupIndexItemDecoration;
import com.othershe.groupindexlib.OnSideBarTouchListener;
import com.othershe.groupindexlib.SideBar;
import com.othershe.groupindexlib.SortHelper;

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
        ItemData data = new ItemData("小米");
        datas.add(data);
        ItemData data1 = new ItemData("华为");
        datas.add(data1);
        ItemData data2 = new ItemData("苹果");
        datas.add(data2);
        ItemData data3 = new ItemData("锤子");
        datas.add(data3);
        ItemData data4 = new ItemData("奇酷");
        datas.add(data4);
        ItemData data5 = new ItemData("金立");
        datas.add(data5);
        ItemData data6 = new ItemData("OPPO");
        datas.add(data6);
        ItemData data7 = new ItemData("vivo");
        datas.add(data7);
        ItemData data8 = new ItemData("中兴");
        datas.add(data8);
        ItemData data9 = new ItemData("乐视");
        datas.add(data9);
        ItemData data10 = new ItemData("小辣椒");
        datas.add(data10);
        ItemData data11 = new ItemData("诺基亚");
        datas.add(data11);
        ItemData data12 = new ItemData("努比亚");
        datas.add(data12);
        ItemData data13 = new ItemData("联想");
        datas.add(data13);
        ItemData data14 = new ItemData("摩托罗拉");
        datas.add(data14);
        ItemData data15 = new ItemData("索尼");
        datas.add(data15);
        ItemData data16 = new ItemData("三星");
        datas.add(data16);
        ItemData data17 = new ItemData("HTC");
        datas.add(data17);
        ItemData data18 = new ItemData("魅族");
        datas.add(data18);
        ItemData data19 = new ItemData("一加");
        datas.add(data19);
        ItemData data20 = new ItemData("酷派");
        datas.add(data20);
        ItemData data21 = new ItemData("ZUK");
        datas.add(data21);
        ItemData data22 = new ItemData("天语");
        datas.add(data22);
        ItemData data23 = new ItemData("美图");
        datas.add(data23);
        ItemData data24 = new ItemData("格力");
        datas.add(data24);
        ItemData data25 = new ItemData("黑莓");
        datas.add(data25);
        ItemData data26 = new ItemData("夏普");
        datas.add(data26);
        ItemData data27 = new ItemData("LG");
        datas.add(data27);
        ItemData data28 = new ItemData("坚果");
        datas.add(data28);
        ItemData data29 = new ItemData("魅蓝");
        datas.add(data29);
        ItemData data30 = new ItemData("红米");
        datas.add(data30);
        ItemData data31 = new ItemData("荣耀");
        datas.add(data31);
        ItemData data32 = new ItemData("←_←");
        datas.add(data32);
        ItemData data33 = new ItemData("⊙﹏⊙");
        datas.add(data33);

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
