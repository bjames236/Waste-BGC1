package com.example.wastemanagement.Admin;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wastemanagement.R;

public class AdminViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView houseAddressLayout, monthRentPriceLayout, houseContactLayout, housePostedDateLayout, listersNameLayout, statusLayout;
    public ImageView imageView;
    public itemClickListener listener;

    public AdminViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.houseImagelayout);
        houseAddressLayout = (TextView) itemView.findViewById(R.id.houseAddressLayout);
        monthRentPriceLayout = (TextView) itemView.findViewById(R.id.monthRentPriceLayout);
        houseContactLayout = (TextView) itemView.findViewById(R.id.houseContactLayout);
        housePostedDateLayout = (TextView) itemView.findViewById(R.id.housePostedDateLayout);
        listersNameLayout = (TextView) itemView.findViewById(R.id.listersNameLayout);
        statusLayout = (TextView) itemView.findViewById(R.id.statusLayout);

    }

    @Override
    public void onClick(View view) {
        listener.onClick(view, getAdapterPosition(), false);
    }

    public void setItemClickListener(itemClickListener listener){
        this.listener = listener;
    }

}
