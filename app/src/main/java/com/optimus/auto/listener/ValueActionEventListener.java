package com.optimus.auto.listener;

import android.app.Activity;
import android.app.Dialog;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.optimus.auto.MainActivity;
import com.optimus.auto.ZipManager;
import com.optimus.auto.task.OnTaskCompleted;
import com.optimus.auto.task.ZstdActionTask;
import java.io.File;


public class ValueActionEventListener implements ValueEventListener, OnTaskCompleted {
    private final String auth;
    private Activity activity;
    private Dialog dialog;
    private String folderName;
    private File folderActorAction = new File(MainActivity.SRC + "Android/data/com.garena.game.kgvn/files/Resources/1.50.1/Ages/Prefab_Characters/Prefab_Hero");
    private ZipManager zip = new ZipManager();

    public ValueActionEventListener(Activity activity, String folderName, Dialog dialog, String auth) {
        this.folderName = folderName;
        this.dialog = dialog;
        this.auth = auth;
        this.activity = activity;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.getValue() != null) {
            String action = dataSnapshot.getValue().toString();
            String code = action.substring(0, 3);
            String absolutePath = folderActorAction.getAbsolutePath();
            File file = new File(absolutePath, "Actor_" + code + "_Actions.pkg.bytes");
            if (file.isFile() && file.exists()) {
                ZstdActionTask actionTask = new ZstdActionTask(activity,this, this.auth);
                actionTask.execute(file.getAbsolutePath(), action, this.folderName);
            } else {
                Toast.makeText(activity, "File Not Found Exception", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
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
