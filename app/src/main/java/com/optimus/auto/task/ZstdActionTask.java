package com.optimus.auto.task;

import android.app.Activity;
import android.os.Looper;
import android.widget.Toast;
import com.optimus.auto.Zstd;
import java.io.File;
import java.io.IOException;



public class ZstdActionTask {
    private final String auth;
    private Activity activity;
    private OnTaskCompleted listener;


    public ZstdActionTask(Activity activity, OnTaskCompleted onTaskCompleted, String auth) {
        this.listener = onTaskCompleted;
        this.auth = auth;
        this.activity = activity;
    }

    public void execute(String path, String info, String folderName) {
        new Thread(() -> {
            doInBackground(path, info, folderName);
            activity.runOnUiThread(() -> {

            });
        }).start();
    }


    public void doInBackground(String path, String action, String folderName) {
        String replacement;
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            try {
                if (action.equals("empty")) {
                    replacement = "prefab_skill_effects/hero_skill_effects/" + folderName;
                } else {
                    replacement = "prefab_skill_effects/hero_skill_effects/" + folderName + "/" + action;
                }
                Zstd.zstd(path, auth, folderName + "/skill", replacement, source -> {
                    listener.onTaskCompleted();
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
