package com.faltenreich.diaguard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {

    private List<TestModle> testList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(String labId);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public TestAdapter(List<TestModle> testList) {
        this.testList = testList;
    }

    @NonNull
    @Override
    public TestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestAdapter.ViewHolder holder, int position) {
        String name = testList.get(position).getLabName();
        String add = testList.get(position).getAddress();
        String con = testList.get(position).getContact();

        holder.setData(name, add, con);

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(testList.get(position).getLabID());
            }
        });
    }

    @Override
    public int getItemCount() {
        return testList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView LabName, address, contact;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            LabName = itemView.findViewById(R.id.LabName);
            address = itemView.findViewById(R.id.address);
            contact = itemView.findViewById(R.id.Contact);
        }

        private void setData(String name, String add, String con) {
            LabName.setText(name);
            address.setText(add);
            contact.setText(con);
        }
    }
}