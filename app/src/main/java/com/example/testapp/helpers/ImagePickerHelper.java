package com.example.testapp.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ImagePickerHelper {

    private final AppCompatActivity activity;
    private final ActivityResultLauncher<String> permissionLauncher;
    private final ActivityResultLauncher<Intent> imagePickerLauncher;
    private boolean isMultiple = false;
    private BiConsumer<Uri, MultipartBody.Part> onImageReady;
    private Consumer<Uri> onImageReadyConsumer;
    private BiConsumer<List<Uri>, List<MultipartBody.Part>> onImagesReady;
    private Consumer<List<Uri>> onImagesReadyConsumer;

    public ImagePickerHelper(AppCompatActivity activity) {
        this.activity = activity;

        permissionLauncher = activity.registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        launchImagePicker();
                    } else {
                        Toast.makeText(activity, "Permission required to access images", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        imagePickerLauncher = activity.registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    try {
                        if (isMultiple) {
                            List<Uri> uriList = new ArrayList<>();
                            List<MultipartBody.Part> partList = new ArrayList<>();

                            if (data.getClipData() != null) {
                                ClipData clipData = data.getClipData();
                                for (int i = 0; i < clipData.getItemCount(); i++) {
                                    Uri uri = clipData.getItemAt(i).getUri();
                                    if (uri != null) {
                                        uriList.add(uri);
                                        if (onImagesReady != null) {
                                            partList.add(createImagePart(getFileFromUri(uri)));
                                        }
                                    }
                                }
                            } else if (data.getData() != null) {
                                Uri uri = data.getData();
                                uriList.add(uri);
                                if (onImagesReady != null) {
                                    partList.add(createImagePart(getFileFromUri(uri)));
                                }
                            }

                            if (onImagesReady != null) {
                                onImagesReady.accept(uriList, partList);
                            } else if (onImagesReadyConsumer != null) {
                                onImagesReadyConsumer.accept(uriList);
                            }

                        } else {
                            Uri uri = data.getData();
                            if (uri != null) {
                                if (onImageReady != null) {
                                    MultipartBody.Part part = createImagePart(getFileFromUri(uri));
                                    onImageReady.accept(uri, part);
                                } else if (onImageReadyConsumer != null) {
                                    onImageReadyConsumer.accept(uri);
                                }
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(activity, "Failed to process image(s)", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        );

    }

    public void pickSingleImage(BiConsumer<Uri, MultipartBody.Part> callback) {
        this.isMultiple = false;
        this.onImageReady = callback;
        requestPermission();
    }

    public void pickMultipleImages(BiConsumer<List<Uri>, List<MultipartBody.Part>> callback) {
        this.isMultiple = true;
        this.onImagesReady = callback;
        requestPermission();
    }

    public void pickSingleImage(Consumer<Uri> callback) {
        this.isMultiple = false;
        this.onImageReadyConsumer = callback;
        requestPermission();
    }

    public void pickMultipleImages(Consumer<List<Uri>> callback) {
        this.isMultiple = true;
        this.onImagesReadyConsumer = callback;
        requestPermission();
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
        } else {
            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    private void launchImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, isMultiple);
        imagePickerLauncher.launch(Intent.createChooser(intent, isMultiple ? "Select Images" : "Select Image"));
    }

    private File getFileFromUri(Uri uri) throws IOException {
        InputStream inputStream = activity.getContentResolver().openInputStream(uri);
        File file = File.createTempFile("upload_", ".jpg", activity.getCacheDir());
        FileOutputStream outputStream = new FileOutputStream(file);
        byte[] buffer = new byte[4096];
        int read;
        if (inputStream != null) {
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            inputStream.close();
        }
        outputStream.flush();
        outputStream.close();
        return file;
    }

    private MultipartBody.Part createImagePart(File file) {
        RequestBody reqBody = RequestBody.create(file, MediaType.parse("image/*"));
        return MultipartBody.Part.createFormData("image", file.getName(), reqBody); // Change to "images" if required
    }

    public MultipartBody.Part createPartFromUri(Uri uri) throws IOException {
        InputStream inputStream = activity.getContentResolver().openInputStream(uri);
        File file = File.createTempFile("upload_", ".jpg", activity.getCacheDir());
        FileOutputStream out = new FileOutputStream(file);
        byte[] buffer = new byte[4096];
        int read;
        while ((read = inputStream.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        out.flush();
        out.close();
        inputStream.close();

        RequestBody reqBody = RequestBody.create(file, MediaType.parse("image/*"));
        return MultipartBody.Part.createFormData("image", file.getName(), reqBody);
    }
}


