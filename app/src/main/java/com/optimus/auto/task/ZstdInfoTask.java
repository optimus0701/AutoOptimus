package com.optimus.auto.task;


import android.app.Activity;
import android.app.Dialog;

import com.optimus.auto.HandleActivity;
import com.optimus.auto.Zstd;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;



public class ZstdInfoTask  {
    private static final byte[] HEX_ARRAY = "0123456789ABCDEF".getBytes(StandardCharsets.US_ASCII);
    private Activity activity;
    private String auth;
    private OnTaskCompleted listener;

    public ZstdInfoTask(Activity activity, OnTaskCompleted onTaskCompleted, String auth) {
        this.listener = onTaskCompleted;
        this.auth = auth;
        this.activity = activity;
    }



    public void execute(String path, String info, String folderName) {
        new Thread(() -> {
            doInBackground(path, info, folderName);
            activity.runOnUiThread(() -> {
                System.out.println("runOnUIThread");
            });
        }).start();
    }



    public void doInBackground(String path, String info, String folderName) {
        HandleActivity.KEY = 0;
        if (folderName.equals("141_Diaochan")) {
            folderName = "141_DiaoChan";
        }
        if (folderName.equals("107_Zhaoyun")) {
            folderName = "107_ZhaoYun";
        }
        if (folderName.equals("531_Keera")) {
            folderName = "531_keera";
        }

        String finalFolderName = folderName;
        Zstd.zstd(path, auth, source -> {
            try {
                File file = new File(path);
                String target = getTarget(source, finalFolderName) + "5F";
                String bytesInfo = bytesToHex((info + "_").getBytes());
                String name = finalFolderName.substring(finalFolderName.indexOf("_") + 1);
                if (target.length() == bytesInfo.length()) {
                    source = edit10To10(source, target, bytesInfo, bytesToHex(name.getBytes()), finalFolderName);
                } else if (target.length() == 10 && bytesInfo.length() == 12) {
                    source = edit10To12(source, target, bytesInfo, bytesToHex(name.getBytes()), finalFolderName);
                } else if (target.length() == 12 && bytesInfo.length() == 10) {
                    source = edit12To10(source, target, bytesInfo, bytesToHex(name.getBytes()), finalFolderName);
                }
                byte[] hexStringToByteArray = hexStringToByteArray(source);
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(hexStringToByteArray);
                fileOutputStream.close();
                Zstd.zstd(path, auth, source1 -> listener.onTaskCompleted());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }




    private String edit10To12(String source, String target, String info, String name, String folderName) {
        System.out.println("10-12");
        String LOD1 = target + name + bytesToHex("_LOD1".getBytes());
        String LOD2 = target + name + bytesToHex("_LOD2".getBytes());
        String LOD3 = target + name + bytesToHex("_LOD3".getBytes());
        String Show1 = target + name + bytesToHex("_Show1".getBytes());
        String Show2 = target + name + bytesToHex("_Show2".getBytes());
        String Show3 = target + name + bytesToHex("_Show3".getBytes());
        if (source.contains(LOD1)) {
            int indexOf = source.indexOf(LOD1);
            LOD1 = source.substring(indexOf, LOD1.length() + indexOf);
            if (source.contains(LOD1.replaceAll(target, info))) {
                int indexOf2 = source.indexOf(bytesToHex("VPrefab_Characters/Prefab_Hero/".getBytes()) + bytesToHex((folderName + "/").getBytes()) + LOD1.replaceAll(target, info));
                int indexOf3 = source.indexOf(bytesToHex("VPrefab_Characters/Prefab_Hero/".getBytes()) + bytesToHex((folderName + "/").getBytes()) + LOD1);
                source = source.substring(0, indexOf3 - 16) + source.substring(indexOf2 - 16, indexOf2 - 14) + source.substring(indexOf3 - 14);
            }
        }
        if (source.contains(LOD2)) {
            int indexOf4 = source.indexOf(LOD2);
            LOD2 = source.substring(indexOf4, LOD2.length() + indexOf4);
            if (source.contains(LOD2.replaceAll(target, info))) {
                int indexOf5 = source.indexOf(bytesToHex("VPrefab_Characters/Prefab_Hero/".getBytes()) + bytesToHex((folderName + "/").getBytes()) + LOD2.replaceAll(target, info));
                int indexOf6 = source.indexOf(bytesToHex("VPrefab_Characters/Prefab_Hero/".getBytes()) + bytesToHex((folderName + "/").getBytes()) + LOD2);
                source = source.substring(0, indexOf6 - 16) + source.substring(indexOf5 - 16, indexOf5 - 14) + source.substring(indexOf6 - 14);
            }
        }
        if (source.contains(LOD3)) {
            int indexOf7 = source.indexOf(LOD3);
            LOD3 = source.substring(indexOf7, LOD3.length() + indexOf7);
            if (source.contains(LOD3.replaceAll(target, info))) {
                int indexOf8 = source.indexOf(bytesToHex("VPrefab_Characters/Prefab_Hero/".getBytes()) + bytesToHex((folderName + "/").getBytes()) + LOD3.replaceAll(target, info));
                int indexOf9 = source.indexOf(bytesToHex("VPrefab_Characters/Prefab_Hero/".getBytes()) + bytesToHex((folderName + "/").getBytes()) + LOD3);
                source = source.substring(0, indexOf9 - 16) + source.substring(indexOf8 - 16, indexOf8 - 14) + source.substring(indexOf9 - 14);
            }
        }
        if (source.contains(Show1)) {
            int indexOf10 = source.indexOf(Show1);
            Show1 = source.substring(indexOf10, Show1.length() + indexOf10);
            if (source.contains(Show1.replaceAll(target, info))) {
                int indexOf11 = source.indexOf(bytesToHex("VPrefab_Characters/Prefab_Hero/".getBytes()) + bytesToHex((folderName + "/").getBytes()) + Show1.replaceAll(target, info));
                int indexOf12 = source.indexOf(bytesToHex("VPrefab_Characters/Prefab_Hero/".getBytes()) + bytesToHex((folderName + "/").getBytes()) + Show1);
                source = source.substring(0, indexOf12 - 16) + source.substring(indexOf11 - 16, indexOf11 - 14) + source.substring(indexOf12 - 14);
            }
        }
        if (source.contains(Show2)) {
            int indexOf13 = source.indexOf(Show2);
            Show2 = source.substring(indexOf13, Show2.length() + indexOf13);
            if (source.contains(Show2.replaceAll(target, info))) {
                int indexOf14 = source.indexOf(bytesToHex("VPrefab_Characters/Prefab_Hero/".getBytes()) + bytesToHex((folderName + "/").getBytes()) + Show2.replaceAll(target, info));
                int indexOf15 = source.indexOf(bytesToHex("VPrefab_Characters/Prefab_Hero/".getBytes()) + bytesToHex((folderName + "/").getBytes()) + Show2);
                source = source.substring(0, indexOf15 - 16) + source.substring(indexOf14 - 16, indexOf14 - 14) + source.substring(indexOf15 - 14);
            }
        }
        if (source.contains(Show3)) {
            int indexOf16 = source.indexOf(Show3);
            Show3 = source.substring(indexOf16, Show3.length() + indexOf16);
            if (source.contains(Show3.replaceAll(target, info))) {
                int indexOf17 = source.indexOf(bytesToHex("VPrefab_Characters/Prefab_Hero/".getBytes()) + bytesToHex((folderName + "/").getBytes()) + Show3.replaceAll(target, info));
                int indexOf18 = source.indexOf(bytesToHex("VPrefab_Characters/Prefab_Hero/".getBytes()) + bytesToHex((folderName + "/").getBytes()) + Show3);
                source = source.substring(0, indexOf18 - 16) + source.substring(indexOf17 - 16, indexOf17 - 14) + source.substring(indexOf18 - 14);
            }
        }
        return source.replaceAll(LOD1 + "04", LOD1.replaceAll(target, info)).replaceAll(LOD2 + "04", LOD2.replaceAll(target, info)).replaceAll(LOD3 + "04", LOD3.replaceAll(target, info)).replaceAll(Show1 + "04", Show1.replaceAll(target, info)).replaceAll(Show2 + "04", Show2.replaceAll(target, info)).replaceAll(Show3 + "04", Show3.replaceAll(target, info));
    }

    private String edit10To10(String source, String target, String info, String name, String folderName) {
        System.out.println("10-10");
        String LOD1 = target + name + bytesToHex("_LOD1".getBytes());
        String LOD2 = target + name + bytesToHex("_LOD2".getBytes());
        String LOD3 = target + name + bytesToHex("_LOD3".getBytes());
        String Show1 = target + name + bytesToHex("_Show1".getBytes());
        String Show2 = target + name + bytesToHex("_Show2".getBytes());
        String Show3 = target + name + bytesToHex("_Show3".getBytes());
        System.out.println("Show3" + Show3);
        if (source.contains(LOD1)) {
            int indexOf = source.indexOf(LOD1);
            LOD1 = source.substring(indexOf, LOD1.length() + indexOf);
            if (source.contains(LOD1.replaceAll(target, info))) {
                int indexOf2 = source.indexOf(bytesToHex("VPrefab_Characters/Prefab_Hero/".getBytes()) + bytesToHex((folderName + "/").getBytes()) + LOD1.replaceAll(target, info));
                int indexOf3 = source.indexOf(bytesToHex("VPrefab_Characters/Prefab_Hero/".getBytes()) + bytesToHex((folderName + "/").getBytes()) + LOD1);
                source = source.substring(0, indexOf3 - 16) + source.substring(indexOf2 - 16, indexOf2 - 14) + source.substring(indexOf3 - 14);
            }
        }
        if (source.contains(LOD2)) {
            int indexOf4 = source.indexOf(LOD2);
            LOD2 = source.substring(indexOf4, LOD2.length() + indexOf4);
            if (source.contains(LOD2.replaceAll(target, info))) {
                int indexOf5 = source.indexOf(bytesToHex("VPrefab_Characters/Prefab_Hero/".getBytes()) + bytesToHex((folderName + "/").getBytes()) + LOD2.replaceAll(target, info));
                int indexOf6 = source.indexOf(bytesToHex("VPrefab_Characters/Prefab_Hero/".getBytes()) + bytesToHex((folderName + "/").getBytes()) + LOD2);
                source = source.substring(0, indexOf6 - 16) + source.substring(indexOf5 - 16, indexOf5 - 14) + source.substring(indexOf6 - 14);
            }
        }
        if (source.contains(LOD3)) {
            int indexOf7 = source.indexOf(LOD3);
            LOD3 = source.substring(indexOf7, LOD3.length() + indexOf7);
            if (source.contains(LOD3.replaceAll(target, info))) {
                int indexOf8 = source.indexOf(bytesToHex("VPrefab_Characters/Prefab_Hero/".getBytes()) + bytesToHex((folderName + "/").getBytes()) + LOD3.replaceAll(target, info));
                int indexOf9 = source.indexOf(bytesToHex("VPrefab_Characters/Prefab_Hero/".getBytes()) + bytesToHex((folderName + "/").getBytes()) + LOD3);
                source = source.substring(0, indexOf9 - 16) + source.substring(indexOf8 - 16, indexOf8 - 14) + source.substring(indexOf9 - 14);
            }
        }
        if (source.contains(Show1)) {
            int indexOf10 = source.indexOf(Show1);
            Show1 = source.substring(indexOf10, Show1.length() + indexOf10);
            if (source.contains(Show1.replaceAll(target, info))) {
                int indexOf11 = source.indexOf(bytesToHex("VPrefab_Characters/Prefab_Hero/".getBytes()) + bytesToHex((folderName + "/").getBytes()) + Show1.replaceAll(target, info));
                int indexOf12 = source.indexOf(bytesToHex("VPrefab_Characters/Prefab_Hero/".getBytes()) + bytesToHex((folderName + "/").getBytes()) + Show1);
                source = source.substring(0, indexOf12 - 16) + source.substring(indexOf11 - 16, indexOf11 - 14) + source.substring(indexOf12 - 14);
            }
        }
        if (source.contains(Show2)) {
            int indexOf13 = source.indexOf(Show2);
            Show2 = source.substring(indexOf13, Show2.length() + indexOf13);
            if (source.contains(Show2.replaceAll(target, info))) {
                int indexOf14 = source.indexOf(bytesToHex("VPrefab_Characters/Prefab_Hero/".getBytes()) + bytesToHex((folderName + "/").getBytes()) + Show2.replaceAll(target, info));
                int indexOf15 = source.indexOf(bytesToHex("VPrefab_Characters/Prefab_Hero/".getBytes()) + bytesToHex((folderName + "/").getBytes()) + Show2);
                source = source.substring(0, indexOf15 - 16) + source.substring(indexOf14 - 16, indexOf14 - 14) + source.substring(indexOf15 - 14);
            }
        }
        if (source.contains(Show3)) {
            int indexOf16 = source.indexOf(Show3);
            Show3 = source.substring(indexOf16, Show3.length() + indexOf16);
            if (source.contains(Show3.replaceAll(target, info))) {
                int indexOf17 = source.indexOf(bytesToHex("VPrefab_Characters/Prefab_Hero/".getBytes()) + bytesToHex((folderName + "/").getBytes()) + Show3.replaceAll(target, info));
                int indexOf18 = source.indexOf(bytesToHex("VPrefab_Characters/Prefab_Hero/".getBytes()) + bytesToHex((folderName + "/").getBytes()) + Show3);
                source = source.substring(0, indexOf18 - 16) + source.substring(indexOf17 - 16, indexOf17 - 14) + source.substring(indexOf18 - 14);
            }
        }
        return source.replaceAll(LOD1, LOD1.replaceAll(target, info)).replaceAll(LOD2, LOD2.replaceAll(target, info)).replaceAll(LOD3, LOD3.replaceAll(target, info)).replaceAll(Show1, Show1.replaceAll(target, info)).replaceAll(Show2, Show2.replaceAll(target, info)).replaceAll(Show3, Show3.replaceAll(target, info));
    }

    private String edit12To10(String source, String target, String info, String name, String folderName) {
        System.out.println("12-10");
        String LOD1 = target + name + bytesToHex("_LOD1".getBytes());
        String LOD2 = target + name + bytesToHex("_LOD2".getBytes());
        String LOD3 = target + name + bytesToHex("_LOD3".getBytes());
        String Show1 = target + name + bytesToHex("_Show1".getBytes());
        String Show2 = target + name + bytesToHex("_Show2".getBytes());
        String Show3 = target + name + bytesToHex("_Show3".getBytes());
        if (source.contains(LOD1)) {
            int indexOf = source.indexOf(LOD1);
            LOD1 = source.substring(indexOf, LOD1.length() + indexOf);
            if (source.contains(LOD1.replaceAll(target, info))) {
                int indexOf2 = source.indexOf(bytesToHex("VPrefab_Characters/Prefab_Hero/".getBytes()) + bytesToHex((folderName + "/").getBytes()) + LOD1.replaceAll(target, info));
                int indexOf3 = source.indexOf(bytesToHex("VPrefab_Characters/Prefab_Hero/".getBytes()) + bytesToHex((folderName + "/").getBytes()) + LOD1);
                source = source.substring(0, indexOf3 - 16) + source.substring(indexOf2 - 16, indexOf2 - 14) + source.substring(indexOf3 - 14);
            }
        }
        if (source.contains(LOD2)) {
            int indexOf4 = source.indexOf(LOD2);
            LOD2 = source.substring(indexOf4, LOD2.length() + indexOf4);
            if (source.contains(LOD2.replaceAll(target, info))) {
                int indexOf5 = source.indexOf(bytesToHex("VPrefab_Characters/Prefab_Hero/".getBytes()) + bytesToHex((folderName + "/").getBytes()) + LOD2.replaceAll(target, info));
                int indexOf6 = source.indexOf(bytesToHex("VPrefab_Characters/Prefab_Hero/".getBytes()) + bytesToHex((folderName + "/").getBytes()) + LOD2);
                source = source.substring(0, indexOf6 - 16) + source.substring(indexOf5 - 16, indexOf5 - 14) + source.substring(indexOf6 - 14);
            }
        }
        if (source.contains(LOD3)) {
            int indexOf7 = source.indexOf(LOD3);
            LOD3 = source.substring(indexOf7, LOD3.length() + indexOf7);
            if (source.contains(LOD3.replaceAll(target, info))) {
                int indexOf8 = source.indexOf(bytesToHex("VPrefab_Characters/Prefab_Hero/".getBytes()) + bytesToHex((folderName + "/").getBytes()) + LOD3.replaceAll(target, info));
                int indexOf9 = source.indexOf(bytesToHex("VPrefab_Characters/Prefab_Hero/".getBytes()) + bytesToHex((folderName + "/").getBytes()) + LOD3);
                source = source.substring(0, indexOf9 - 16) + source.substring(indexOf8 - 16, indexOf8 - 14) + source.substring(indexOf9 - 14);
            }
        }
        if (source.contains(Show1)) {
            int indexOf10 = source.indexOf(Show1);
            Show1 = source.substring(indexOf10, Show1.length() + indexOf10);
            if (source.contains(Show1.replaceAll(target, info))) {
                int indexOf11 = source.indexOf(bytesToHex("VPrefab_Characters/Prefab_Hero/".getBytes()) + bytesToHex((folderName + "/").getBytes()) + Show1.replaceAll(target, info));
                int indexOf12 = source.indexOf(bytesToHex("VPrefab_Characters/Prefab_Hero/".getBytes()) + bytesToHex((folderName + "/").getBytes()) + Show1);
                source = source.substring(0, indexOf12 - 16) + source.substring(indexOf11 - 16, indexOf11 - 14) + source.substring(indexOf12 - 14);
            }
        }
        if (source.contains(Show2)) {
            int indexOf13 = source.indexOf(Show2);
            Show2 = source.substring(indexOf13, Show2.length() + indexOf13);
            if (source.contains(Show2.replaceAll(target, info))) {
                int indexOf14 = source.indexOf(bytesToHex("VPrefab_Characters/Prefab_Hero/".getBytes()) + bytesToHex((folderName + "/").getBytes()) + Show2.replaceAll(target, info));
                int indexOf15 = source.indexOf(bytesToHex("VPrefab_Characters/Prefab_Hero/".getBytes()) + bytesToHex((folderName + "/").getBytes()) + Show2);
                source = source.substring(0, indexOf15 - 16) + source.substring(indexOf14 - 16, indexOf14 - 14) + source.substring(indexOf15 - 14);
            }
        }
        if (source.contains(Show3)) {
            int indexOf16 = source.indexOf(Show3);
            Show3 = source.substring(indexOf16, Show3.length() + indexOf16);
            if (source.contains(Show3.replaceAll(target, info))) {
                int indexOf17 = source.indexOf(bytesToHex("VPrefab_Characters/Prefab_Hero/".getBytes()) + bytesToHex((folderName + "/").getBytes()) + Show3.replaceAll(target, info));
                int indexOf18 = source.indexOf(bytesToHex("VPrefab_Characters/Prefab_Hero/".getBytes()) + bytesToHex((folderName + "/").getBytes()) + Show3);
                source = source.substring(0, indexOf18 - 16) + source.substring(indexOf17 - 16, indexOf17 - 14) + source.substring(indexOf18 - 14);
            }
        }
        return source.replaceAll(LOD1, LOD1.replaceAll(target, info) + "04").replaceAll(LOD2, LOD2.replaceAll(target, info) + "04").replaceAll(LOD3, LOD3.replaceAll(target, info) + "04").replaceAll(Show1, Show1.replaceAll(target, info) + "04").replaceAll(Show2, Show2.replaceAll(target, info) + "04").replaceAll(Show3, Show3.replaceAll(target, info) + "04");
    }



    private static String getTarget(String source, String folderName) {
        String bytesToHex = bytesToHex(("Prefab_Hero/" + folderName + "/").getBytes());
        int indexOf = source.indexOf(bytesToHex);
        int length = bytesToHex.length() + indexOf;
        int indexOf2 = source.indexOf(bytesToHex("_".getBytes()), length);
        if (indexOf != -1) {
            String substring = source.substring(length, indexOf2);
            return substring.length() > 4 ? substring.substring(substring.lastIndexOf(bytesToHex("/".getBytes())) + 1) : substring;
        } else {
            return "";
        }
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

    public static byte[] hexStringToByteArray(String str) {
        int length = str.length();
        byte[] bArr = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            bArr[i / 2] = (byte) ((Character.digit(str.charAt(i), 16) << 4) + Character.digit(str.charAt(i + 1), 16));
        }
        return bArr;
    }


}
