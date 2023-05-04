package com.optimus.auto.task;

import android.app.Dialog;
import android.os.AsyncTask;
import com.optimus.auto.MainActivity;
import com.optimus.auto.ZipManager;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;


public class ResetModTask extends AsyncTask<String, Void, Void> {
    private Dialog dialog;

    public ResetModTask(Dialog dialog) {
        this.dialog = dialog;
    }


    @Override
    public Void doInBackground(String... strArr) {
        String url = strArr[0];
        String fileName = url.substring(url.lastIndexOf("/") + 1);
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new URL(url).openStream());
            File folder = new File(MainActivity.SRC + "ao");
            if (!folder.exists()) {
                folder.mkdirs();
            }
            File file = new File(folder, fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] bArr = new byte[1024];
            while (true) {
                int read = bufferedInputStream.read(bArr, 0, 1024);
                if (read != -1) {
                    fileOutputStream.write(bArr, 0, read);
                } else {
                    fileOutputStream.close();
                    ZipManager zipManager = new ZipManager();
                    zipManager.unzip(file, new File(MainActivity.SRC + "ao/data/"));
                    File folderInfo = new File(MainActivity.SRC + "Android/data/com.garena.game.kgvn/files/Resources/1.50.1/Prefab_Characters");
                    File folderAction = new File(MainActivity.SRC + "Android/data/com.garena.game.kgvn/files/Resources/1.50.1/Ages/Prefab_Characters/Prefab_Hero");
                    zipManager.unzip(new File(MainActivity.SRC + "ao/data/info.zip"), folderInfo);
                    zipManager.unzip(new File(MainActivity.SRC + "ao/data/action.zip"), folderAction);
                    bufferedInputStream.close();
                    return null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public void onPostExecute(Void r3) {
        Dialog dialog = this.dialog;
        if (dialog == null || !dialog.isShowing()) {
            return;
        }
        File file = new File(MainActivity.SRC, "ao");
        if (file.exists()) {
            MainActivity.deleteDirectory(file);
        }
        this.dialog.dismiss();
    }
}
