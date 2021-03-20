package com.example.codepath_project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.parse.ParseFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class PhotoActivity extends AppCompatActivity {
    public static final String TAG = "PhotoActivity";
    public static final int GALLERY_ACTIVITY_REQUEST_CODE = 41;
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    private TextView tvPrompt;
    private Button btnCamera;
    private Button btnGallery;
    private Button btnSubmit;
    private ImageView ivPhoto;
    private Task task;
    private File photoFile;
    private Uri fileProvider;
    public String photoFileName = "photo.jpg";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        task = getIntent().getParcelableExtra("task");
        setContentView(R.layout.activity_photo);
        tvPrompt = findViewById(R.id.tvPrompt);
        btnCamera = findViewById(R.id.btnCamera);
        btnGallery = findViewById(R.id.btnGallery);
        btnSubmit = findViewById(R.id.btnSubmit);
        ivPhoto = findViewById(R.id.ivPhoto);
        photoFile = getPhotoFileUri(photoFileName);
        // Create a File reference for future access
        fileProvider = FileProvider.getUriForFile(PhotoActivity.this, "com.codepath.fileprovider.todo", photoFile);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchGallery();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(photoFile == null || ivPhoto.getDrawable() == null)
                {
                    Toast.makeText(PhotoActivity.this, "There is no image!", Toast.LENGTH_SHORT).show();
                }
                // save image to Parse
                task.put(Task.KEY_PHOTO, new ParseFile(photoFile));
                // set as complete
                task.setComplete(true);
                task.saveInBackground();
                Toast.makeText(PhotoActivity.this, "Task was completed!", Toast.LENGTH_SHORT).show();
                finishAndRemoveTask();
            }
        });

        // Create pop-up
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8), (int)(height*.2));
    }

    private void launchGallery() {
        // TODO: Display gallery
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
        startActivityForResult(intent, GALLERY_ACTIVITY_REQUEST_CODE);
    }

    private void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(this.getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // RESIZE WINDOW
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                int width = dm.widthPixels;
                int height = dm.heightPixels;
                getWindow().setLayout((int)(width*.8), (int)(height*.65));
                // Make room - remove prompt
                tvPrompt.setVisibility(View.GONE);

                // retrieve camera photo from disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());

                // Load the taken image into the preview
                ivPhoto.setImageBitmap(takenImage);
                ivPhoto.setVisibility(View.VISIBLE);
                btnSubmit.setVisibility(View.VISIBLE);
            }
            else
            { // Result was a failure
                Toast.makeText(this, "Photo wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode == GALLERY_ACTIVITY_REQUEST_CODE)
        {
            if (resultCode == RESULT_OK)
            {
                // RESIZE WINDOW
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                int width = dm.widthPixels;
                int height = dm.heightPixels;
                getWindow().setLayout((int)(width*.8), (int)(height*.65));
                // Make room - remove prompt
                tvPrompt.setVisibility(View.GONE);

                fileProvider = data.getData();
                Bitmap chosenImage;
                try {
                    chosenImage = BitmapFactory.decodeStream(getContentResolver().openInputStream(fileProvider));
                    ivPhoto.setImageBitmap(chosenImage);
                    FileOutputStream fostream = new FileOutputStream(photoFile);
                    chosenImage.compress(Bitmap.CompressFormat.PNG, 50, fostream);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ivPhoto.setVisibility(View.VISIBLE);
                btnSubmit.setVisibility(View.VISIBLE);
            }
            else
            { // Result was a failure
                Toast.makeText(this, "Photo wasn't selected!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);
        return file;
    }

}