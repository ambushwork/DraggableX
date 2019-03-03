package com.netatmo.ylu.draggablex;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.netatmo.ylu.library.DraggableHelper;
import com.netatmo.ylu.library.DraggableInfo;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);
        ExampleAdapter adapter = new ExampleAdapter();
        adapter.setList(MockDataGenerator.generateMockList(20));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DraggableHelper.getInstance().setDraggableInfo(new DraggableInfo(R.color.colorPrimary, R.color.transparent));
        DraggableHelper.getInstance().attachRecyclerView(recyclerView, adapter);
    }
}
