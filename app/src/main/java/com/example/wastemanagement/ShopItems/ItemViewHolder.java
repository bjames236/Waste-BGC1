package com.example.wastemanagement.ShopItems;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wastemanagement.Admin.itemClickListener;
import com.example.wastemanagement.R;

public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView title, header, description,price, housePostedDateLayout, listersNameLayout, statusLayout;
    public ImageView imageView;
    public itemClickListener listener;

    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.awarenesscover);
        title = (TextView) itemView.findViewById(R.id.titleText);
        header = (TextView) itemView.findViewById(R.id.headertext);


    }

    @Override
    public void onClick(View view) {
        listener.onClick(view, getAdapterPosition(), false);
    }

    public void setItemClickListener(itemClickListener listener){
        this.listener = listener;
    }

}
