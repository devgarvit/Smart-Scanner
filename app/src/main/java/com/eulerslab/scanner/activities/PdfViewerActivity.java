package com.eulerslab.scanner.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;


import com.eulerslab.scanner.adapter.PdfAdapter;
import com.eulerslab.scanner.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class PdfViewerActivity extends AppCompatActivity {
    private static final String FILENAME = "policy.pdf";
    private ParcelFileDescriptor mFileDescriptor;
    private PdfRenderer mPdfRenderer;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    PdfAdapter adapter;
    LinearLayoutManager HorizontalLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);
        // initialisation with id's
        try {
            openRenderer(getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        recyclerView
                = (RecyclerView) findViewById(
                R.id.recyclerview);
        RecyclerViewLayoutManager
                = new LinearLayoutManager(
                getApplicationContext());

        // Set LayoutManager on Recycler View
        recyclerView.setLayoutManager(
                RecyclerViewLayoutManager);

        // calling constructor of adapter
        // with source list as a parameter
        adapter = new PdfAdapter(mPdfRenderer, mPdfRenderer.getPageCount());

        // Set Horizontal Layout Manager
        // for Recycler view
        HorizontalLayout
                = new LinearLayoutManager(
                PdfViewerActivity.this,
                LinearLayoutManager.HORIZONTAL,
                false);
        recyclerView.setLayoutManager(HorizontalLayout);

        // Set adapter on recycler view
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onDestroy() {
        try {
            closeRenderer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    private void openRenderer(Context context) throws IOException {
        File file = new File(context.getCacheDir(), FILENAME);
        if (!file.exists()) {
            InputStream asset = context.getAssets().open(FILENAME);
            FileOutputStream output = new FileOutputStream(file);
            final byte[] buffer = new byte[1024];
            int size;
            while ((size = asset.read(buffer)) != -1) {
                output.write(buffer, 0, size);
            }
            asset.close();
            output.close();
        }
        mFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        if (mFileDescriptor != null) {
            mPdfRenderer = new PdfRenderer(mFileDescriptor);
        }
    }

    private void closeRenderer() throws IOException {
        mPdfRenderer.close();
        mFileDescriptor.close();
    }


}
