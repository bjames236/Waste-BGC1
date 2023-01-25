package com.example.wastemanagement.Awareness;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wastemanagement.Admin.itemClickListener;
import com.example.wastemanagement.R;

public class AwarenessViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView title, header, description, housePostedDateLayout, listersNameLayout, statusLayout;
    public ImageView imageView;
    public itemClickListener listener;

    public AwarenessViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.awarenesscover);
        title = (TextView) itemView.findViewById(R.id.titleText);
        header = (TextView) itemView.findViewById(R.id.headertext);
        description = (TextView) itemView.findViewById(R.id.descriptiontext);

    }

    @Override
    public void onClick(View view) {
        listener.onClick(view, getAdapterPosition(), false);
    }

    public void setItemClickListener(itemClickListener listener){
        this.listener = listener;
    }

}
