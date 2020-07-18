package com.eulerslab.scanner.adapter;


import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.view.LayoutInflater;

import androidx.recyclerview.widget.RecyclerView;

import com.eulerslab.scanner.R;

// The adapter class which
// extends RecyclerView Adapter
public class PdfAdapter
        extends RecyclerView.Adapter<PdfAdapter.MyView> {

    private ImageView mImageView;
    private PdfRenderer mPdfRenderer;
    private PdfRenderer.Page mCurrentPage;


    public class MyView
            extends RecyclerView.ViewHolder {

        public MyView(View view) {
            super(view);

            mImageView = (ImageView) view.findViewById(R.id.image);
            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    // Constructor for adapter class
    // which takes a list of String type
    public PdfAdapter(PdfRenderer pdfRenderer, int pageCount) {
        this.mPdfRenderer = pdfRenderer;
    }

    // Override onCreateViewHolder which deals
    // with the inflation of the card layout
    // as an item for the RecyclerView.
    @Override
    public MyView onCreateViewHolder(ViewGroup parent,
                                     int viewType) {

        // Inflate item.xml using LayoutInflator
        View itemView
                = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.adapter_pdf_item,
                        parent,
                        false);

        // return itemView
        return new MyView(itemView);
    }

    // Override onBindViewHolder which deals
    // with the setting of different data
    // and methods related to clicks on
    // particular items of the RecyclerView.
    @Override
    public void onBindViewHolder(final MyView holder,
                                 final int position) {

        holder.setIsRecyclable(false);
        showPage(position);
    }

    // Override getItemCount which Returns
    // the length of the RecyclerView.
    @Override
    public int getItemCount() {
        return mPdfRenderer.getPageCount();
    }


    private void showPage(int index) {
        if (mPdfRenderer.getPageCount() <= index) {
            return;
        }
        Log.e("Garvit", index + "");

        // Make sure to close the current page before opening another one.
        if (null != mCurrentPage) {
            mCurrentPage.close();
        }
        // Use `openPage` to open a specific page in PDF.
        mCurrentPage = mPdfRenderer.openPage(index);

        // Important: the destination bitmap must be ARGB (not RGB).
        Bitmap bitmap = Bitmap.createBitmap(mCurrentPage.getWidth(), mCurrentPage.getHeight(),
                Bitmap.Config.ARGB_8888);
        mCurrentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
        mImageView.setImageBitmap(bitmap);
    }

}