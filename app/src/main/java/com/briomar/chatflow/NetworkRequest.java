package com.briomar.chatflow;

import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.*;


public class NetworkRequest {
    public interface UploadCallback {
        void onUploadComplete(String result);
    }

    private static final String TAG = "NetworkRequest";


    public void simulateRequest(String screenshotURI, UploadCallback callback) {
        new UploadTask(callback).execute(screenshotURI);
    }

    private static class UploadTask extends AsyncTask<String, Integer, String> {

        private static final MediaType MEDIA_TYPE_JPEG = MediaType.parse("image/jpeg");
        private UploadCallback callback;
        private int progressPercent = 0;

        public int getProgressPercent() {
            return progressPercent;
        }

        public void setProgressPercent(int progressPercent) {
            this.progressPercent = progressPercent;
        }


        public UploadTask(UploadCallback callback) {
            this.callback = callback;
        }

        @Override
        protected String doInBackground(String... params) {
            String screenshotURI = params[0];

            // Should not be part of commit data.
            String url = "https://chatflow.lomogan.africa/generate-text";
            File imageFile = new File(screenshotURI);

            // DEV : Log.v("Image File",imageFile.getAbsolutePath());

            if (!imageFile.exists()) {
                Log.e(TAG, "Image file not found");
                return null;
            }

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(40, TimeUnit.SECONDS)
                    .build();

            RequestBody fileBody = RequestBody.create(MEDIA_TYPE_JPEG, imageFile);
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("image", imageFile.getName(), fileBody)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "Unexpected code " + response.body().string());
                    return null;
                }

                return response.body().string();
            } catch (IOException e) {
                Log.e(TAG, "Error: ", e);
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... progress){
            setProgressPercent(progress[0]);
            // TODO: Implement progress bar using `progressPercent`
            Log.v("Uploading Image Progress % : ", String.valueOf(getProgressPercent()));
        }

        @Override
        protected void onPostExecute(String result) {
            if (callback != null) {
                callback.onUploadComplete(result);
            }
        }
    }

}
