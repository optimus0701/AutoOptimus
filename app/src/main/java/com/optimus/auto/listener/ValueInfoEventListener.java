package com.optimus.auto.listener;

import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.optimus.auto.MainActivity;
import com.optimus.auto.ZipManager;
import com.optimus.auto.task.OnTaskCompleted;
import com.optimus.auto.task.ZstdInfoTask;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ValueInfoEventListener implements ValueEventListener, OnTaskCompleted {
    private final String auth;
    private Dialog dialog;
    private String folderName;
    private Activity activity;
    File folderActorInfo = new File(MainActivity.SRC + "Android/data/com.garena.game.kgvn/files/Resources/1.50.1/Prefab_Characters");
    private ZipManager zip = new ZipManager();

    public ValueInfoEventListener(Activity activity, String folderName, Dialog dialog, String auth) {
        this.activity = activity;
        this.folderName = folderName;
        this.dialog = dialog;
        this.auth = auth;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.getValue() != null) {
            String info = dataSnapshot.getValue().toString();
            String code = info.substring(0, 3);
            String absolutePath = folderActorInfo.getAbsolutePath();
            File fileInfo = new File(absolutePath, "Actor_" + code + "_Infos.pkg.bytes");
            if (fileInfo.exists() && fileInfo.isFile()) {
                dialog.show();
                File folder = new File(MainActivity.SRC + "ao/info/" + code);
                try {
                    zip.unzip(fileInfo, folder);
                    String path = String.format("%s/Prefab_Hero/%s/%s_actorinfo.bytes", folder.getAbsolutePath(), folderName, folderName);
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

    @Override
    public void onCancelled(DatabaseError databaseError) {
        dialog.dismiss();
    }

    @Override
    public void onTaskCompleted() {
        String code = folderName.substring(0, 3);
        File file = new File(MainActivity.SRC + "/ao/info/" + code + "/");
        String path = folderActorInfo.getAbsolutePath();
        zip.zipDirectory(file, new File(path, "Actor_" + code + "_Infos.pkg.bytes").getAbsolutePath());
    }
}
