package com.optimus.auto.listener;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.os.Build;
import android.widget.Toast;

import androidx.documentfile.provider.DocumentFile;

import com.google.android.gms.common.util.IOUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.optimus.auto.MainActivity;
import com.optimus.auto.ZipManager;
import com.optimus.auto.task.OnTaskCompleted;
import com.optimus.auto.task.ZstdActionTask;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class ValueActionEventListener implements ValueEventListener, OnTaskCompleted {
    private final String auth;
    private Activity activity;
    private Dialog dialog;
    private String folderName;
    private ContentResolver contentResolver;
    private DocumentFile folder;
    private DocumentFile documentFileAction = null;
    private File folderActorAction = new File(MainActivity.SRC + "Android/data/com.garena.game.kgvn/files/Resources/1.50.1/Ages/Prefab_Characters/Prefab_Hero");
    private File temp = new File(MainActivity.SRC + "ao/action/");
    private ZipManager zip = new ZipManager();

    public ValueActionEventListener(Activity activity, String folderName, Dialog dialog, String auth) {
        this.folderName = folderName;
        this.dialog = dialog;
        this.auth = auth;
        this.activity = activity;
    }

    public ValueActionEventListener(Activity activity, String folderName, Dialog dialog, String auth, ContentResolver contentResolver, DocumentFile folder) {
        this.folderName = folderName;
        this.dialog = dialog;
        this.auth = auth;
        this.activity = activity;
        this.contentResolver = contentResolver;
        this.folder = folder;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.getValue() != null) {
            String action = dataSnapshot.getValue().toString();
            String code = action.substring(0, 3);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                documentFileAction = folder.findFile("Actor_" + code + "_Actions.pkg.bytes");
                if (documentFileAction != null && contentResolver != null) {
                    try {
                        byte[] bytes = new byte[1024];
                        InputStream inputStream = contentResolver.openInputStream(documentFileAction.getUri());
                        temp.mkdirs();
                        temp = new File(temp, code);
                        temp.mkdirs();
                        temp = new File(temp, documentFileAction.getName());

                        OutputStream outputStream = new FileOutputStream(temp);
                        IOUtils.copyStream(inputStream, outputStream);
                        ZstdActionTask actionTask = new ZstdActionTask(activity,this, auth);
                        actionTask.execute(temp.getPath(), action, this.folderName);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            } else {
                String path = folderActorAction.getAbsolutePath();
                File file = new File(path, "Actor_" + code + "_Actions.pkg.bytes");
                if (file.isFile() && file.exists()) {
                    ZstdActionTask actionTask = new ZstdActionTask(activity,this, this.auth);
                    actionTask.execute(file.getAbsolutePath(), action, this.folderName);
                } else {
                    Toast.makeText(activity, "File Not Found Exception", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        this.dialog.dismiss();
        Toast.makeText(activity, databaseError.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTaskCompleted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (documentFileAction != null && contentResolver != null) {
                try {
                    OutputStream outputStream = contentResolver.openOutputStream(documentFileAction.getUri());
                    InputStream inputStream = new FileInputStream(temp);
                    IOUtils.copyStream(inputStream, outputStream);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        deleteDirectory(new File(MainActivity.SRC, "ao"));
        dialog.dismiss();
    }

    public boolean deleteDirectory(File folder) {
        File[] listFiles = folder.listFiles();
        if (listFiles != null) {
            for (File file : listFiles) {
                deleteDirectory(file);
                System.out.println(file.getAbsolutePath());
            }
        }
        return folder.delete();
    }
}
