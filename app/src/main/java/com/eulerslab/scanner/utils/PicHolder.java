package com.eulerslab.scanner.utils;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eulerslab.scanner.R;


/**
 *Author CodeBoy722
 *
 * picture_Adapter's ViewHolder
 */

public class PicHolder extends RecyclerView.ViewHolder{

    public ImageView picture;

    PicHolder(@NonNull View itemView) {
        super(itemView);

        picture = itemView.findViewById(R.id.image);
    }
}
