package com.eulerslab.scanner.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eulerslab.scanner.R;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import java.io.File;
import java.util.ArrayList;

import static androidx.core.view.ViewCompat.setTransitionName;

/**
 * Author CodeBoy722
 *
 * A RecyclerView Adapter class that's populates a RecyclerView with images from
 * a folder on the device external storage
 */
public class picture_Adapter extends RecyclerView.Adapter<PicHolder> {

    private ArrayList<pictureFacer> pictureList;
    private Context pictureContx;
    private final itemClickListener picListerner;

    /**
     *
     * @param pictureList ArrayList of pictureFacer objects
     * @param pictureContx The Activities Context
     * @param picListerner An interface for listening to clicks on the RecyclerView's items
     */
    public picture_Adapter(ArrayList<pictureFacer> pictureList, Context pictureContx, itemClickListener picListerner) {
        this.pictureList = pictureList;
        this.pictureContx = pictureContx;
        this.picListerner = picListerner;
    }

    @NonNull
    @Override
    public PicHolder onCreateViewHolder(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View cell = inflater.inflate(R.layout.pic_holder_item, container, false);
        return new PicHolder(cell);
    }

    @Override
    public void onBindViewHolder(@NonNull final PicHolder holder, final int position) {

        final pictureFacer image = pictureList.get(position);

        if (image.getPicturePath().contains("PDF")) {
            generateImageFromPdf(Uri.fromFile(new File(image.getPicturePath())), holder);
        } else
        Glide.with(pictureContx)
                .load(image.getPicturePath())
                .apply(new RequestOptions().centerCrop())
                .into(holder.picture);

        setTransitionName(holder.picture, String.valueOf(position) + "_image");

        holder.picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picListerner.onPicClicked(holder,position, pictureList);
            }
        });

    }

    void generateImageFromPdf(Uri pdfUri, PicHolder holder) {
        int pageNumber = 0;
        PdfiumCore pdfiumCore = new PdfiumCore(pictureContx);
        try {
            //http://www.programcreek.com/java-api-examples/index.php?api=android.os.ParcelFileDescriptor
            ParcelFileDescriptor fd = pictureContx.getContentResolver().openFileDescriptor(pdfUri, "r");
            PdfDocument pdfDocument = pdfiumCore.newDocument(fd);
            pdfiumCore.openPage(pdfDocument, pageNumber);
            int width = pdfiumCore.getPageWidthPoint(pdfDocument, pageNumber);
            int height = pdfiumCore.getPageHeightPoint(pdfDocument, pageNumber);
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            pdfiumCore.renderPageBitmap(pdfDocument, bmp, pageNumber, 0, 0, width, height);
            Glide.with(pictureContx)
                    .load(bmp)
                    .apply(new RequestOptions().centerCrop())
                    .into(holder.picture);
            ;
            pdfiumCore.closeDocument(pdfDocument); // important!
        } catch (Exception e) {
            //todo with exception
        }
    }

    @Override
    public int getItemCount() {
        return pictureList.size();
    }
}
