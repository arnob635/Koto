package com.jakfromspace.kotoandroid;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * Coded by JAKfromSpace on 7/29/2018 for Koto.
 */

public class CameraActivity extends AppCompatActivity {

    String textVision = null;
    TextView textView;
    ImageView imageView;
    Button btnCamera;
    Button btnCopy;

    public static final int CAMERA_REQUEST = 21;
    Uri selectedImage;
    private Uri imageUri;
    final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_capture);

        btnCamera = findViewById(R.id.btnCamera);
        imageView = findViewById(R.id.imageView);
        btnCopy = findViewById(R.id.btnCopy);
        textView = findViewById(R.id.textView2);

        //MainActivity.kotoItems.add(new KotoItem("Canon DSLR",60000));
        MainActivity.adapter.notifyDataSetChanged();

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(CameraActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 110);
        } else {
            OpenCamera();
        }

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions(CameraActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 110);
                } else {
                    OpenCamera();
                }
            }
        });
        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textVision == null) Toast.makeText(getApplicationContext(), "Nothing Copied", Toast.LENGTH_SHORT).show();
                else{
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("KotoVision Strings", textVision);
                    Objects.requireNonNull(clipboard).setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext(), "Copied to Clipboard. Chill.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void OpenCamera(){
        Date cal = Calendar.getInstance().getTime();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        String filename = format.format(cal);
        File photo = new File(Environment.getExternalStorageDirectory(), filename);
        imageUri = FileProvider.getUriForFile(CameraActivity.this,
                "com.jakfromspace.kotoandroid.provider", photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));

        startActivityForResult(intent, CAMERA_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 110) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                OpenCamera();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode == RESULT_OK) {
            imageView.setImageURI(null);
            //taken images are usually lopsided
            try {
                imageView.setImageBitmap(KotoVision.handleSamplingAndRotationBitmap(getApplicationContext(),imageUri));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
            textVision = KotoVision.RunOCR(bitmap, getApplicationContext(), "CameraActivity");
            textView.setText(textVision);
            KotoVision.FilterNumber(textVision);

            //imageUri = data.getData();
            //imageView.setImageURI(imageUri);
            //Bitmap bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            //imageView.setImageBitmap(bitmap);
        }
        else Toast.makeText(getApplicationContext(), "Couldn't Get Result from Camera", Toast.LENGTH_SHORT).show();
    }
}