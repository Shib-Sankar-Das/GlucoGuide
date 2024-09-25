package com.faltenreich.diaguard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FindDoctorActivity extends AppCompatActivity {

    private RecyclerView DoctorView;
    private DoctorAdapter adapter;
    private Button AddDoctor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_find_doctor);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        DoctorView = findViewById(R.id.doc_recycler_view);
        AddDoctor = findViewById(R.id.addDocButton);

        AddDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {startActivity(new Intent(FindDoctorActivity.this, AddDoctor.class));
                finish();

            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        DoctorView.setLayoutManager(layoutManager);


        DoctorAdapter adapter = new DoctorAdapter(DbQuery.g_doctorList, this);
        //adapter.notifyDataSetChanged();
        DoctorView.setAdapter(adapter);


    }

    private BroadcastReceiver updateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("DOCTOR_DATA_UPDATED".equals(intent.getAction())) {
                String email = intent.getStringExtra("DOCTOR_EMAIL");
                updateDoctorInList(email, intent);
            }
        }
    };

    private void updateDoctorInList(String email, Intent data) {
        for (int i = 0; i < DbQuery.g_doctorList.size(); i++) {
            if (DbQuery.g_doctorList.get(i).getDocEmail().equals(email)) {
                DoctorModle updatedDoctor = new DoctorModle(
                        data.getStringExtra("DOCTOR_NAME"),
                        data.getStringExtra("DOCTOR_SPECIALITY"),
                        data.getStringExtra("DOCTOR_ADDRESS"),
                        data.getStringExtra("DOCTOR_HOSPITAL"),
                        data.getStringExtra("DOCTOR_EMAIL"),
                        data.getStringExtra("DOCTOR_CONTACT"),
                        data.getBooleanExtra("DOCTOR_STATUS", false)
                );
                DbQuery.g_doctorList.set(i, updatedDoctor);
                adapter.notifyItemChanged(i);
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(updateReceiver, new IntentFilter("DOCTOR_DATA_UPDATED"));
        updateDoctorList();
        // Refresh the list when returning to this activity
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(updateReceiver);
    }

    private void updateDoctorList() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}