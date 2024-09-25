package com.faltenreich.diaguard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    private List<AppointmentModel> appointmentList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(AppointmentModel appointmentModel);
        void onCancelClick(AppointmentModel appointmentModel);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public AppointmentAdapter(List<AppointmentModel> appointmentList) {
        this.appointmentList = appointmentList;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_item_layout, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        AppointmentModel appointmentModel = appointmentList.get(position);
        holder.testName.setText(appointmentModel.getTestName());
        holder.labName.setText(appointmentModel.getLabName());
        holder.date.setText(appointmentModel.getDate());
        holder.time.setText(appointmentModel.getTime());
        holder.cost.setText(String.valueOf(appointmentModel.getPrice()) + " Rs.");

        if (appointmentModel.getTestImage() != null && !appointmentModel.getTestImage().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(appointmentModel.getTestImage())
                    .apply(new RequestOptions().placeholder(R.drawable.lab))
                    .into(holder.testImage);
        } else {
            holder.testImage.setImageResource(R.drawable.lab);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(appointmentModel);
            }
        });

        holder.cancelButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCancelClick(appointmentModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView testName, labName, date, cost, time;
        ImageView testImage;
        View cancelButton;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            testName = itemView.findViewById(R.id.test_name);
            labName = itemView.findViewById(R.id.lab_name);
            date = itemView.findViewById(R.id.date_text);
            time = itemView.findViewById(R.id.time_text);
            cost = itemView.findViewById(R.id.cost);
            testImage = itemView.findViewById(R.id.test_image);
            cancelButton = itemView.findViewById(R.id.cancel_button);
        }
    }
}