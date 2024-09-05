package com.briomar.chatflow;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import org.json.JSONException;
import org.json.JSONObject;


public class ChatFlow extends AppCompatActivity {

    private static final int REQUEST_PERMISSIONS_CODE = 1;
    private ImageView uploadedImage;
    private Button uploadButton;
    private Button resetButton;
    private TextView api_response_view;
    private ProgressBar progressBar;

    // Unique code
    ActivityResultLauncher<Intent> launchImageUpload = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.getData() != null) {
                        Uri selectedImageUri = data.getData();
                        Bitmap selectedImageBitmap = null;

                        try {
                            selectedImageBitmap = MediaStore.Images.Media.getBitmap(
                                    this.getContentResolver(), selectedImageUri
                            );
                            showSnackbar("Processing reply...");

                            // Get the file path from the URI
                            String imagePath = FileUtils.getRealPathFromURI(this,selectedImageUri);
                            Log.v("imagePath", String.valueOf(selectedImageUri));

                            File directoryToTessData = new File("app/src/main/assets/tessdata");

                            // For Debugging.
                            Log.v("Path to Image", imagePath);
                            Log.v("Path to TessData", directoryToTessData.getAbsolutePath());


                            if (imagePath != null) {
                                // Set the uploadButton to invisible
                                uploadButton.setVisibility(View.INVISIBLE);
                                // Show the progress bar
                                progressBar.setVisibility(View.VISIBLE);
                                // Set the screenshot URI
                                NetworkRequest networkRequest = new NetworkRequest();

                                networkRequest.simulateRequest(imagePath, new NetworkRequest.UploadCallback() {
                                    @Override
                                    public void onUploadComplete(String result) {
                                        if (result != null) {
                                            Log.v("Upload Result", "Response received: " + result);
                                            try {
                                                // Parse the JSON response
                                                JSONObject jsonResponse = new JSONObject(result);
                                                String apiResult = jsonResponse.getString("generated_text");

                                                // Hide the progress bar
                                                progressBar.setVisibility(View.INVISIBLE);
                                                // Set the API Response View to the parsed result
                                                api_response_view.setText(apiResult);
                                                // Show the reset button and result text
                                                api_response_view.setVisibility(View.VISIBLE);
                                                resetButton.setVisibility(View.VISIBLE);

                                            } catch (JSONException e) {
                                                Log.e("Upload Result", "Failed to parse JSON", e);
                                                api_response_view.setText("Error parsing response");
                                            }
                                        } else {
                                            Log.e("Upload Result", "Failed to upload the file");
                                            api_response_view.setText("Failed to upload the file");
                                        }
                                    }
                                });

                            } else {
                                showSnackbar("Could not get the file path. Please try again.");
                            }
                        } catch (IOException e) {
                            showSnackbar("Problem occurred uploading image. Please try again later.");
                            Log.e("Error : ", e.getMessage());
                        }

                        uploadedImage.setImageBitmap(selectedImageBitmap);
                    }
                }
            }
    );

    // Helper method to get the real path from URI
    private String getRealPathFromURI(Uri uri) {
        String result = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, proj, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            result = cursor.getString(columnIndex);
            cursor.close();
        }
        return result;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_main);

        uploadedImage = findViewById(R.id.uploadedImage);
        uploadButton = findViewById(R.id.uploadButton);
        resetButton = findViewById(R.id.resetButton);
        api_response_view = findViewById(R.id.api_response_view);
        progressBar = findViewById(R.id.progressBar);
        // Hide the progress bar and reset button and api_response_view
        api_response_view.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        resetButton.setVisibility(View.INVISIBLE);

        // Request permissions to access files
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkAndRequestPermissions();
        }

        uploadButton.setOnClickListener(v -> setUploadedImage());
        resetButton.setOnClickListener(v->resetState());
        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    private void resetState(){
        // Clear and hide API text
        api_response_view.setText("");
        api_response_view.setVisibility(View.INVISIBLE);
        // Remove image
        uploadedImage.setImageBitmap(null);
        // Hide Reset Button
        resetButton.setVisibility(View.INVISIBLE);
        // Initiate upload again
        setUploadedImage();
    }
    private void setUploadedImage() {
        Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        launchImageUpload.launch(i);
    }

    public void showSnackbar(String message) {
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.textView), message, Snackbar.LENGTH_LONG)
                .setAction("okay", v -> {
                    // Nothing happens Lol.
                    Log.v("User Action", "User clicked the okay button on the snackbar");
                });
        snackbar.setBackgroundTint(Color.rgb(224, 17, 95));
        snackbar.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_PERMISSIONS_CODE);
            } else {
                Log.v("Permissions", "READ_MEDIA_IMAGES permission already granted");
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS_CODE);
            } else {
                Log.v("Permissions", "READ_EXTERNAL_STORAGE permission already granted");
            }
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showSnackbar("Permissions granted. You can now upload files.");
            } else {
                showSnackbar("Permission denied. Cannot access the file.");
            }
        }
    }

}
