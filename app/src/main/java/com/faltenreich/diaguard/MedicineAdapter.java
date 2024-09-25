// MedicineAdapter.java
package com.faltenreich.diaguard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder> {

    private List<Medicine> medicineList;
    private final OnMedicineClickListener listener;

    public interface OnMedicineClickListener {
        void onMedicineClick(Medicine medicine);
    }

    public MedicineAdapter(List<Medicine> medicineList, OnMedicineClickListener listener) {
        this.medicineList = medicineList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medicine, parent, false);
        return new MedicineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        Medicine medicine = medicineList.get(position);
        holder.bind(medicine, listener);
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    public void updateList(List<Medicine> newList) {
        medicineList = newList;
        notifyDataSetChanged();
    }

    static class MedicineViewHolder extends RecyclerView.ViewHolder {

        private final ImageView medicineImage;
        private final TextView medicineName;
        private final TextView medicinePrice;
        private final TextView medicineAvailability;

        public MedicineViewHolder(@NonNull View itemView) {
            super(itemView);
            medicineImage = itemView.findViewById(R.id.medicine_image);
            medicineName = itemView.findViewById(R.id.medicine_name);
            medicinePrice = itemView.findViewById(R.id.medicine_price);
            medicineAvailability = itemView.findViewById(R.id.medicine_availability);
        }

        public void bind(Medicine medicine, OnMedicineClickListener listener) {
            Glide.with(itemView.getContext()).load(medicine.getImagePath()).into(medicineImage);
            medicineName.setText(medicine.getName());
            medicinePrice.setText("Price: " + medicine.getPrice());
            medicineAvailability.setText("Available: " + medicine.getAvailableUnits());

            itemView.setOnClickListener(v -> listener.onMedicineClick(medicine));
        }
    }
}