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

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.PackageViewHolder> {

    private List<PackageModel> packageList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(PackageModel packageModel);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public PackageAdapter(List<PackageModel> packageList) {
        this.packageList = packageList;
    }

    @NonNull
    @Override
    public PackageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.package_item_layout, parent, false);
        return new PackageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PackageViewHolder holder, int position) {
        PackageModel packageModel = packageList.get(position);
        holder.packageName.setText(packageModel.getPackageName());
        holder.packagePrice.setText(String.valueOf(packageModel.getPrice()) + " Rs.");

        // Check if the image URL is not null or empty
        if (packageModel.getImage() != null && !packageModel.getImage().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(packageModel.getImage())
                    .apply(new RequestOptions().placeholder(R.drawable.lab)) // Use a placeholder image
                    .into(holder.packageImage);
        } else {
            holder.packageImage.setImageResource(R.drawable.lab); // Set a placeholder image
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(packageModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return packageList.size();
    }

    public static class PackageViewHolder extends RecyclerView.ViewHolder {
        TextView packageName, packagePrice;
        ImageView packageImage;

        public PackageViewHolder(@NonNull View itemView) {
            super(itemView);
            packageName = itemView.findViewById(R.id.package_name);
            packagePrice = itemView.findViewById(R.id.package_price);
            packageImage = itemView.findViewById(R.id.package_image);
        }
    }
}