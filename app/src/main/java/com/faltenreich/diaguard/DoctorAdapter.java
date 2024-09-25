package com.faltenreich.diaguard;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder> {

    private List<DoctorModle> docList;

    public DoctorAdapter(List<DoctorModle> docList) {
        this.docList = docList;
    }

    public void updateData(List<DoctorModle> newDoctorList) {
        this.docList = newDoctorList;
        notifyDataSetChanged();
    }

    private Context context;

    public DoctorAdapter(List<DoctorModle> docList, Context context) {
        this.docList = docList;
        this.context = context;
    }



    @NonNull
    @Override
    public DoctorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doc_item_layout,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorAdapter.ViewHolder holder, int position) {

        DoctorModle doctor = docList.get(position);
        holder.setData(doctor.getDoctorName(), doctor.getDocSpeciality(), doctor.getDocEmail(), doctor.getDocContact());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DoctorDetailsActivity.class);
                intent.putExtra("DOCTOR_NAME", doctor.getDoctorName());
                intent.putExtra("DOCTOR_SPECIALITY", doctor.getDocSpeciality());
                intent.putExtra("DOCTOR_ADDRESS", doctor.getDocAddress());
                intent.putExtra("DOCTOR_HOSPITAL", doctor.getDocHospital());
                intent.putExtra("DOCTOR_EMAIL", doctor.getDocEmail());
                intent.putExtra("DOCTOR_CONTACT", doctor.getDocContact());
                intent.putExtra("DOCTOR_STATUS", doctor.isDocStatus());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return docList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //private ImageView DocImage;
        private TextView DocName, DocSpeciality,DocEmail,DocContact;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //DocImage = itemView.findViewById(R.id.DocImage);
            DocName = itemView.findViewById(R.id.DocName);
            DocSpeciality = itemView.findViewById(R.id.DocSpeciality);
            DocEmail = itemView.findViewById(R.id.DocEmail);
            DocContact = itemView.findViewById(R.id.DocContact);
        }


        private void setData(String name, String speciality, String email, String con)
        {
            DocName.setText(name);
            DocSpeciality.setText(speciality);
            DocEmail.setText(email);
            DocContact.setText(con);

        }

    }
}
