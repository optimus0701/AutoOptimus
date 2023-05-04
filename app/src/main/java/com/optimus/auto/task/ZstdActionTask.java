package com.optimus.auto.task;

import android.app.Activity;
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
                Toast.makeText(activity, "Đã Xong", Toast.LENGTH_LONG).show();
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
//    @Override
//    public Void doInBackground(String... strArr) {
//
//            String str6 = strArr[0];
//            String str7 = strArr[1];
//            String str8 = strArr[2];
//            String str9 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/ao/action/";
//            if (str7.equals("")) {
//                replacement = "prefab_skill_effects/hero_skill_effects/" + str8;
//            } else {
//                replacement = "prefab_skill_effects/hero_skill_effects/" + str8 + "/" + str7;
//            }
//            try {
//                Zstd.zstd(this.context, Uri.parse(str6), str9, this.auth, str8 + "/skill", replacement);
//                return null;
//            } catch (IOException e2) {
//                e2.printStackTrace();
//                Toast.makeText(this.context, "" + e2.getMessage(), Toast.LENGTH_LONG).show();
//                return null;
//            }
//        }
//    }

}
