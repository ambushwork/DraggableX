package com.netatmo.ylu.draggablex;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.netatmo.ylu.library.DraggableHelper;

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
        DraggableHelper.getInstance().attachRecyclerView(recyclerView, adapter);
    }
}
