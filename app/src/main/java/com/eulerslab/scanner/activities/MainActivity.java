package com.eulerslab.scanner.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.eulerslab.scanner.R;
import com.eulerslab.scanner.utils.MarginDecoration;
import com.eulerslab.scanner.utils.PicHolder;
import com.eulerslab.scanner.utils.imageFolder;
import com.eulerslab.scanner.utils.itemClickListener;
import com.eulerslab.scanner.utils.pictureFacer;
import com.eulerslab.scanner.utils.pictureFolderAdapter;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements itemClickListener {

    RecyclerView folderRecycler;
    TextView empty;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    /**
     * Request the user for permission to access media files and read images on the device
     * this will be useful as from api 21 and above, if this check is not done the Activity will crash
     * <p>
     * Setting up the RecyclerView and getting all folders that contain pictures from the device
     * the getPicturePaths() returns an ArrayList of imageFolder objects that is then used to
     * create a RecyclerView Adapter that is set to the RecyclerView
     *
     * @param savedInstanceState saving the activity state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

        empty = findViewById(R.id.empty);

        folderRecycler = findViewById(R.id.folderRecycler);
        folderRecycler.addItemDecoration(new MarginDecoration(this));
        folderRecycler.hasFixedSize();
        ArrayList<imageFolder> folds = getPicturePaths();

        if (folds.isEmpty()) {
            empty.setVisibility(View.VISIBLE);
        } else {
            RecyclerView.Adapter folderAdapter = new pictureFolderAdapter(folds, MainActivity.this, this);
            folderRecycler.setAdapter(folderAdapter);
        }
    }

    /**
     * @return gets all folders with pictures on the device and loads each of them in a custom object imageFolder
     * the returns an ArrayList of these custom objects
     */
    private ArrayList<imageFolder> getPicturePaths() {

        ArrayList<imageFolder> picFolders = null;
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "/DCIM/garvit engagement");
            File[] pictures = file.listFiles();
            picFolders = new ArrayList<>();

            for (int i = 0; i < pictures.length; i++) {
                String folderPath = pictures[i] + "";
                String folderpaths[] = folderPath.split("/");
                String folderName = folderpaths[folderpaths.length - 1];

                File picPath = new File(Environment.getExternalStorageDirectory(), "/DCIM/garvit engagement/" + folderName);
                File[] folderPictures = picPath.listFiles();

                imageFolder folds = new imageFolder();
                folds.setPath(folderPath);
                folds.setFolderName(folderName);
                folds.setFirstPic(folderPictures[0] + "");//if the folder has only one picture this line helps to set it as first so as to avoid blank image in itemview
                folds.addpics();
                picFolders.add(folds);
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"Check Storage Permission",Toast.LENGTH_LONG).show();
        }

        return picFolders;
    }


    @Override
    public void onPicClicked(PicHolder holder, int position, ArrayList<pictureFacer> pics) {

    }


    /**
     * Each time an item in the RecyclerView is clicked this method from the implementation of the transitListerner
     * in this activity is executed, this is possible because this class is passed as a parameter in the creation
     * of the RecyclerView's Adapter, see the adapter class to understand better what is happening here
     *
     * @param pictureFolderPath a String corresponding to a folder path on the device external storage
     */
    @Override
    public void onPicClicked(String pictureFolderPath, String folderName) {
        Intent move = new Intent(MainActivity.this, ImageDisplayActivity.class);
        move.putExtra("folderPath", pictureFolderPath);
        move.putExtra("folderName", folderName);

        //move.putExtra("recyclerItemSize",getCardsOptimalWidth(4));
        startActivity(move);
    }


   /* public int getCardsOptimalWidth(int numberOfRows){
        Configuration configuration = MainActivity.this.getResources().getConfiguration();
        int screenWidthDp = configuration.screenWidthDp; //The current width of the available screen space, in dp units, corresponding to screen width resource qualifier.
        int smallestScreenWidthDp = configuration.smallestScreenWidthDp; //The smallest screen size an application will see in normal operation, corresponding to smallest screen width resource qualifier.
        int each = screenWidthDp / numberOfRows;

        return each;
    }*/

   /* private int dpToPx(int dp) {
        float density = MainActivity.this.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }*/

}
