package com.test.groupindex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.othershe.groupindexlib.OnSideBarTouchListener;
import com.othershe.groupindexlib.SideBar;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mList = (RecyclerView) findViewById(R.id.list);
        SideBar sideBar = (SideBar) findViewById(R.id.side_bar);
        final TextView tip = (TextView) findViewById(R.id.tip);
        sideBar.setOnSideBarTouchListener(new OnSideBarTouchListener() {
            @Override
            public void onTouch(String letter, int position) {
                tip.setVisibility(View.VISIBLE);
                tip.setText(letter + position);
            }

            @Override
            public void onTouchEnd() {
                tip.setVisibility(View.GONE);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mList.setLayoutManager(layoutManager);
//        mList.addItemDecoration();
//        mList.setAdapter(adapter);
    }
}
