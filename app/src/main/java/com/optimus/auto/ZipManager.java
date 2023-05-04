package com.optimus.auto;

import android.content.Context;

import androidx.documentfile.provider.DocumentFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


public class ZipManager {
    List<String> filesListInDir = new ArrayList<>();

    public boolean unpackZip(String source, String target) {
        try {
            ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(new FileInputStream(source)));
            byte[] bArr = new byte[1024];
            while (true) {
                ZipEntry nextEntry = zipInputStream.getNextEntry();
                if (nextEntry != null) {
                    String name = nextEntry.getName();
                    if (nextEntry.isDirectory()) {
                        new File(target + name).mkdirs();
                    } else {
                        FileOutputStream fileOutputStream = new FileOutputStream(target + name);
                        while (true) {
                            int read = zipInputStream.read(bArr);
                            if (read == -1) {
                                break;
                            }
                            fileOutputStream.write(bArr, 0, read);
                        }
                        fileOutputStream.close();
                        zipInputStream.closeEntry();
                    }
                } else {
                    zipInputStream.close();
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void zipDirectory(File dir, String zipDirName) {
        try {
            populateFilesList(dir);

            FileOutputStream fos = new FileOutputStream(zipDirName);
            ZipOutputStream zos = new ZipOutputStream(fos);
            for(String filePath : filesListInDir){
                System.out.println("Zipping "+filePath);
                //for ZipEntry we need to keep only relative file path, so we used substring on absolute path
                ZipEntry ze = new ZipEntry(filePath.substring(dir.getAbsolutePath().length()+1, filePath.length()));
                zos.putNextEntry(ze);
                //read the file and write to ZipOutputStream
                FileInputStream fis = new FileInputStream(filePath);
                byte[] buffer = new byte[1024];
                int len;
                while ((len = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
                zos.closeEntry();
                fis.close();
            }
            zos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void populateFilesList(File dir) throws IOException {
        File[] files = dir.listFiles();
        for(File file : files){
            if(file.isFile()) filesListInDir.add(file.getAbsolutePath());
            else populateFilesList(file);
        }
    }




    public void unzip(File file, File file2) throws IOException {
        ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(new FileInputStream(file)));
        byte[] bytes = new byte[8192];
        while (true) {
            ZipEntry nextEntry = zipInputStream.getNextEntry();
            if (nextEntry != null) {
                File file3 = new File(file2, nextEntry.getName());
                File parentFile = nextEntry.isDirectory() ? file3 : file3.getParentFile();
                if (!parentFile.isDirectory()) {
                    parentFile.mkdirs();
                }
                if (!nextEntry.isDirectory()) {
                    FileOutputStream fileOutputStream = new FileOutputStream(file3);
                    while (true) {
                        try {
                            int read = zipInputStream.read(bytes);
                            if (read == -1) {
                                break;
                            }
                            fileOutputStream.write(bytes, 0, read);
                        } catch (Throwable th) {
                            zipInputStream.close();
                        }
                    }
                    fileOutputStream.close();
                }
            } else {
                zipInputStream.close();
                return;
            }
        }
    }

    public void unzip(Context context, DocumentFile documentFile, File file) throws IOException {
        ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(context.getContentResolver().openInputStream(documentFile.getUri())));
        byte[] bytes = new byte[8192];
        while (true) {
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            if (zipEntry != null) {
                File file2 = new File(file, documentFile.getName());
                File parentFile = documentFile.isDirectory() ? file2 : file2.getParentFile();
                if (!parentFile.isDirectory()) {
                    parentFile.mkdirs();
                }
                if (!documentFile.isDirectory()) {
                    FileOutputStream outputStream = new FileOutputStream(file2);
                    while (true) {
                        try {
                            int read = zipInputStream.read(bytes);
                            if (read == -1) {

                                break;
                            }
                            outputStream.write(bytes, 0, read);
                        } catch (Throwable th) {
                            zipInputStream.close();
                        }
                    }
                    outputStream.close();
                }
            } else {
                zipInputStream.close();
                return;
            }
        }
    }
}

