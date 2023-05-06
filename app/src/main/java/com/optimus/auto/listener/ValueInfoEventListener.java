package com.optimus.auto.listener;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.widget.Toast;

import androidx.documentfile.provider.DocumentFile;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.optimus.auto.MainActivity;
import com.optimus.auto.ZipManager;
import com.optimus.auto.task.OnTaskCompleted;
import com.optimus.auto.task.ZstdInfoTask;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ValueInfoEventListener implements ValueEventListener, OnTaskCompleted {
    private final String auth;
    private Dialog dialog;
    private String folderName;
    private Activity activity;
    private ContentResolver contentResolver;
    private DocumentFile folder;
    private DocumentFile documentFileInfo = null;
    File folderActorInfo = new File(MainActivity.SRC + "Android/data/com.garena.game.kgvn/files/Resources/1.50.1/Prefab_Characters");
    private ZipManager zip = new ZipManager();

    public ValueInfoEventListener(Activity activity, String folderName, Dialog dialog, String auth) {
        this.activity = activity;
        this.folderName = folderName;
        this.dialog = dialog;
        this.auth = auth;
    }

    public ValueInfoEventListener(Activity activity, String folderName, Dialog dialog, String auth, ContentResolver contentResolver, DocumentFile folder) {
        this.activity = activity;
        this.folderName = folderName;
        this.dialog = dialog;
        this.auth = auth;
        this.contentResolver = contentResolver;
        this.folder = folder;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.getValue() != null) {
            String info = dataSnapshot.getValue().toString();
            String code = info.substring(0, 3);
            String folderInfoPath = folderActorInfo.getAbsolutePath();
            File folderTemp = new File(MainActivity.SRC + "ao/info/" + code);
            if (Build.VERSION.SDK_INT >= 30) {
                dialog.show();
                DocumentFile[] files = folder.listFiles();
                for (DocumentFile file : files) {
                    if (file.getName().contains(code)) {
                        documentFileInfo = file;
                        break;
                    }
                }
                if (documentFileInfo != null && contentResolver != null) {
                    try {
                        InputStream inputStream = contentResolver.openInputStream(documentFileInfo.getUri());
                        zip.unzip(inputStream, folderTemp);
                        String path = String.format("%s/Prefab_Hero/%s/%s_actorinfo.bytes", folderTemp.getAbsolutePath(), folderName, folderName);
                        ZstdInfoTask infoTask = new ZstdInfoTask(activity, this, auth);
                        infoTask.execute(path, info, folderName);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                } else {
                    dialog.dismiss();
                }
            } else {
                File fileInfo = new File(folderInfoPath, "Actor_" + code + "_Infos.pkg.bytes");
                if (fileInfo.exists() && fileInfo.isFile()) {
                    dialog.show();
                    try {
                        zip.unzip(fileInfo, folderTemp);
                        String path = String.format("%s/Prefab_Hero/%s/%s_actorinfo.bytes", folderTemp.getAbsolutePath(), folderName, folderName);
                        ZstdInfoTask infoTask = new ZstdInfoTask(activity, this, auth);
                        infoTask.execute(path, info, folderName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(dialog.getOwnerActivity(), "File Not Found Exception", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        dialog.dismiss();
    }

    @Override
    public void onTaskCompleted() {
        String code = folderName.substring(0, 3);
        File file = new File(MainActivity.SRC + "/ao/info/" + code + "/");
        String path = folderActorInfo.getAbsolutePath();
        if (Build.VERSION.SDK_INT >= 30) {
            if (documentFileInfo != null && contentResolver != null) {
                try {
                    OutputStream outputStream = contentResolver.openOutputStream(documentFileInfo.getUri());
                    zip.zipDirectory(file, outputStream);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            zip.zipDirectory(file, new File(path, "Actor_" + code + "_Infos.pkg.bytes").getAbsolutePath());
        }
    }
}
