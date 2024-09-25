// LabTestActivity.java
package com.faltenreich.diaguard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LabTestActivity extends AppCompatActivity {

    private RecyclerView testview;
    private Button AppointmentButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_test);

        testview = findViewById(R.id.test_recycler_view);
        AppointmentButton = findViewById(R.id.appointment_button);

        AppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LabTestActivity.this, AppointmentActivity.class);
                startActivity(intent);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        testview.setLayoutManager(layoutManager);

        TestAdapter adapter = new TestAdapter(DbQuery.g_testList);
        testview.setAdapter(adapter);

        adapter.setOnItemClickListener(new TestAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String labId) {
                Intent intent = new Intent(LabTestActivity.this, LabPackagesActivity.class);
                intent.putExtra("LAB_ID", labId);
                startActivity(intent);
            }
        });
    }
}