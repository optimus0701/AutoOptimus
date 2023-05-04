package com.optimus.auto;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;
import androidx.documentfile.provider.DocumentFile;

import com.optimus.auto.task.OnDownloadCompleted;
import com.optimus.auto.task.OnTaskCompleted;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Zstd {
    private static final byte[] HEX_ARRAY = "0123456789ABCDEF".getBytes(StandardCharsets.US_ASCII);
    public static void zstd(String path, String auth, String folder, String replacement, OnDownloadCompleted downloadListener) throws IOException {
        File file = new File(path);
        if (file.isFile() && file.exists()) {
            String name = file.getName();

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", file.getName(),
                            RequestBody.create(MediaType.parse("application/octet-stream"), file))
                    .addFormDataPart("auth", auth)
                    .addFormDataPart("edit", replacement)
                    .addFormDataPart("folder", folder)
                    .build();
            Request request = new Request.Builder().url("http://vido0701.store/upload_action").post(body).build();

            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println(e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {
                        try {
                            BufferedInputStream bufferedInputStream = new BufferedInputStream(new URL("http://vido0701.store/downloads/" + auth + "/" + name).openStream());
                            FileOutputStream fileOutputStream = new FileOutputStream(file);
                            byte[] bArr = new byte[1024];
                            while (true) {
                                int read = bufferedInputStream.read(bArr, 0, 1024);
                                if (read == -1) {
                                    break;
                                }
                                fileOutputStream.write(bArr, 0, read);
                            }
                            bufferedInputStream.close();
                            downloadListener.onDownloadCompleted("");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("abc: " + response.code());
                    }
                }
            });
        }
    }



    public static void zstd(String path, String auth, OnDownloadCompleted downloadListener) {
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();
            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", file.getName(),
                            RequestBody.create(MediaType.parse("application/octet-stream"), file))
                    .addFormDataPart("auth", auth)
                    .build();
            Request request = new Request.Builder().url("http://vido0701.store/upload").post(body).build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println(e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {
                        try {
                            BufferedInputStream bufferedInputStream = new BufferedInputStream(new URL("http://vido0701.store/downloads/" + auth + "/" + file.getName()).openStream());
                            FileOutputStream fileOutputStream = new FileOutputStream(file);
                            byte[] bArr = new byte[1024];
                            while (true) {
                                int read = bufferedInputStream.read(bArr, 0, 1024);
                                if (read == -1) {
                                    break;
                                }
                                fileOutputStream.write(bArr, 0, read);
                            }
                            bufferedInputStream.close();
                            String source = bytesToHex(readFileToBytes(file.getAbsolutePath()));
                            downloadListener.onDownloadCompleted(source);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }


    public static void zstd(Context context, Uri uri, String str, String str2, String str3, String str4) throws IOException {
        DocumentFile fromTreeUri = DocumentFile.fromTreeUri(context, uri);
        if (fromTreeUri == null || !fromTreeUri.exists()) {
            return;
        }
        InputStream openInputStream = context.getContentResolver().openInputStream(uri);
        File file = new File(str + fromTreeUri.getName());
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        byte[] bArr = new byte[1024];
        while (true) {
            int read = openInputStream.read(bArr);
            if (read == -1) {
                break;
            }
            fileOutputStream.write(bArr, 0, read);
        }
        openInputStream.close();
        fileOutputStream.close();
        String name = fromTreeUri.getName();
        HttpEntity build = MultipartEntityBuilder.create().addPart("file", new FileBody(file)).addTextBody("folder", str3).addTextBody("edit", str4).addTextBody("auth", str2).build();
        HttpPost httpPost = new HttpPost("http://vido0701.store/upload_action");
        httpPost.setEntity(build);
        try {
            HttpResponse execute = HttpClientBuilder.create().build().execute((HttpUriRequest) httpPost);
            if (execute.getStatusLine().getStatusCode() == 200) {
                try {
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(new URL("http://vido0701.store/downloads/" + str2 + "/" + name).openStream());
                    OutputStream openOutputStream = context.getContentResolver().openOutputStream(uri);
                    byte[] bArr2 = new byte[1024];
                    while (true) {
                        int read2 = bufferedInputStream.read(bArr2, 0, 1024);
                        if (read2 == -1) {
                            break;
                        }
                        openOutputStream.write(bArr2, 0, read2);
                    }
                    openOutputStream.close();
                    bufferedInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(context, execute.toString(), Toast.LENGTH_LONG).show();
            }
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    private static byte[] readFileToBytes(String filePath) throws IOException {
        File file = new File(filePath);
        byte[] bytes = new byte[(int) file.length()];
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            fis.read(bytes);
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
        return bytes;
    }

    public static String bytesToHex(byte[] bArr) {
        byte[] bArr2 = new byte[bArr.length * 2];
        for (int i = 0; i < bArr.length; i++) {
            int i2 = bArr[i] & 255;
            int i3 = i * 2;
            byte[] bArr3 = HEX_ARRAY;
            bArr2[i3] = bArr3[i2 >>> 4];
            bArr2[i3 + 1] = bArr3[i2 & 15];
        }
        return new String(bArr2, StandardCharsets.UTF_8);
    }
}
