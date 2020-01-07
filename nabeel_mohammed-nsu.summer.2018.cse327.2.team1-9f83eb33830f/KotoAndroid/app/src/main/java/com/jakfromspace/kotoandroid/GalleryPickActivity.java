package com.jakfromspace.kotoandroid;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class GalleryPickActivity extends AppCompatActivity {

    private static final int SELECT_PIC = 1;
    Button btnRun, btnCopy;
    ImageView imageViewGallery;
    TextView textViewGallery;
    String textVision = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_pick);

        //MainActivity.kotoItems.add(new KotoItem("Artwork & Frame",2000));
        MainActivity.adapter.notifyDataSetChanged();

        imageViewGallery = findViewById(R.id.galleryImageView);
        textViewGallery = findViewById(R.id.galleryTextView);
        btnRun = findViewById(R.id.buttonRun);
        btnCopy = findViewById(R.id.buttonCopy);
        btnRun.setVisibility(View.GONE);
        btnCopy.setVisibility(View.GONE);
    }


    public void BrowseGallery(View view) {
        Intent intentGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intentGallery, SELECT_PIC);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PIC){
            if (resultCode == RESULT_OK){
                Uri uri = data.getData();
                imageViewGallery.setImageURI(null);
                try {
                    imageViewGallery.setImageBitmap(KotoVision.handleSamplingAndRotationBitmap(getApplicationContext(),uri));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                btnRun.setVisibility(View.VISIBLE);
                btnCopy.setVisibility(View.VISIBLE);
                /*String[] projection = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(projection[0]);

                String filepath = cursor.getString(columnIndex);
                cursor.close();

                Bitmap bitmap = BitmapFactory.decodeFile(filepath);
                Drawable drawable = new BitmapDrawable(getResources(),bitmap);
                imageViewGallery.setBackground(drawable);*/
            }
            else Toast.makeText(getApplicationContext(), "File Selection Caused a Problem -RESULT-", Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(getApplicationContext(), "Couldn't Access Storage -REQUEST-", Toast.LENGTH_SHORT).show();
    }

    public void RunVision(View view) {
        Bitmap bitView = ((BitmapDrawable)imageViewGallery.getDrawable()).getBitmap();
        textVision = KotoVision.RunOCR(bitView, getApplicationContext(), "GalleryPickActivity");
        textViewGallery.setText(textVision);
        KotoVision.FilterNumber(textVision);
    }

    public void CopyClipboardVisionText(View view) {
        if (textVision == null) Toast.makeText(getApplicationContext(), "Nothing Copied", Toast.LENGTH_SHORT).show();
        else{
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("KotoVision Strings", textVision);
            if (clipboard != null) {
                clipboard.setPrimaryClip(clip);
            }

            Toast.makeText(getApplicationContext(), "Copied to Clipboard. Chill.", Toast.LENGTH_SHORT).show();
        }
    }
}
