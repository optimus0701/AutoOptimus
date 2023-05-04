package com.optimus.auto;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.UriPermission;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.DocumentsContract;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.optimus.auto.task.OnTaskCompleted;



import java.io.File;
import java.util.List;

/* loaded from: classes2.dex */
public class HandleActivity extends AppCompatActivity implements OnTaskCompleted {
    public static int KEY;
    public static final int KEY_ACTION = 0;
    public static final int KEY_INFO = 0;
    private static String code;
    private FirebaseAuth auth;
    private Dialog dialog;
    private ContentResolver resolver;
    Uri treeUri;
    Uri uri;
    private FirebaseUser user;
    private final String EXTERNAL_STORAGE_PROVIDER_AUTHORITY = "com.android.externalstorage.documents";
    private final String ANDROID_DOCID = "primary:Android/data/com.garena.game.kgvn/files/Resources/1.47.1/Prefab_Characters";

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_handle);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        this.auth = firebaseAuth;
        this.user = firebaseAuth.getCurrentUser();
        this.uri = DocumentsContract.buildDocumentUri("com.android.externalstorage.documents", "primary:Android/data/com.garena.game.kgvn/files/Resources/1.47.1/Prefab_Characters");
        this.treeUri = DocumentsContract.buildTreeDocumentUri("com.android.externalstorage.documents", "primary:Android/data/com.garena.game.kgvn/files/Resources/1.47.1/Prefab_Characters");
        Dialog dialog = new Dialog(this);
        this.dialog = dialog;
        dialog.requestWindowFeature(1);
        this.dialog.setCanceledOnTouchOutside(false);
        this.dialog.setContentView(R.layout.dialog);
        if (Build.VERSION.SDK_INT >= 29) {
            openDirectory();
        }
    }

    private void openDirectory() {
        if (checkIfGotAccess().booleanValue()) {
            myFun();
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback() { // from class: com.optimus.auto.HandleActivity$$ExternalSyntheticLambda0
                @Override // androidx.activity.result.ActivityResultCallback
                public final void onActivityResult(Object obj) {
                    HandleActivity.this.m143lambda$openDirectory$0$comoptimusautoHandleActivity((ActivityResult) obj);
                }
            }).launch(getPrimaryVolume().createOpenDocumentTreeIntent().putExtra("android.provider.extra.INITIAL_URI", this.uri));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$openDirectory$0$com-optimus-auto-HandleActivity  reason: not valid java name */
    public /* synthetic */ void m143lambda$openDirectory$0$comoptimusautoHandleActivity(ActivityResult activityResult) {
        if (activityResult.getResultCode() != -1 || activityResult.getData() == null || activityResult.getData().getData() == null) {
            return;
        }
        Uri data = activityResult.getData().getData();
        ContentResolver contentResolver = getContentResolver();
        this.resolver = contentResolver;
        contentResolver.takePersistableUriPermission(data, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (checkIfGotAccess().booleanValue()) {
            myFun();
        } else {
            Toast.makeText(this, "you didn't grant permission to the correct folder", Toast.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private StorageVolume getPrimaryVolume() {
        return ((StorageManager) getSystemService(Context.STORAGE_SERVICE)).getPrimaryStorageVolume();
    }

    private Boolean checkIfGotAccess() {
        List<UriPermission> persistedUriPermissions = getContentResolver().getPersistedUriPermissions();
        for (int i = 0; i < persistedUriPermissions.size(); i++) {
            UriPermission uriPermission = persistedUriPermissions.get(i);
            if (uriPermission.getUri().equals(this.treeUri) && uriPermission.isReadPermission()) {
                return true;
            }
        }
        return false;
    }

    private void myFun() {
        Intent intent = getIntent();
        String stringExtra = intent.getStringExtra("info");
        String stringExtra2 = intent.getStringExtra("action");
        code = intent.getStringExtra("code");
        String stringExtra3 = intent.getStringExtra("folderName");
        Toast.makeText(this, stringExtra + "" + stringExtra2 + "" + code, Toast.LENGTH_LONG).show();
        this.user.getDisplayName();
        this.user.getUid();
        DocumentFile fromSingleUri = DocumentFile.fromSingleUri(this, Uri.parse(this.treeUri + "/Actor_" + code + "_Infos.pkg.bytes"));
        if (fromSingleUri != null) {
            if (fromSingleUri.exists()) {
                fromSingleUri.delete();
                Toast.makeText(this, "deleted file", Toast.LENGTH_LONG).show();
            }
            DocumentFile fromTreeUri = DocumentFile.fromTreeUri(this, Uri.parse(this.treeUri + "/Prefab_Hero/" + stringExtra3));
            StringBuilder sb = new StringBuilder();
            sb.append(stringExtra3);
            sb.append("_actorinfo.bytes");
            fromTreeUri.createFile("text/*", sb.toString());
        }
    }

    @Override // com.optimus.auto.task.OnTaskCompleted
    public void onTaskCompleted() {
        if (KEY == 0) {
            DocumentFile fromTreeUri = DocumentFile.fromTreeUri(this, Uri.parse(this.treeUri + "/com.garena.game.kgvn/files/Resources/1.47.1/Ages/Prefab_Characters/Prefab_Hero/Prefab_Characters/Actor_" + code + "_Infos.pkg.bytes"));
            if (fromTreeUri != null) {
                //new ZipManager().zipDirectory(this, new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/ao/info/" + code + "/"), fromTreeUri);
                deleteDirectory(new File(MainActivity.SRC, "ao"));
                this.dialog.dismiss();
                return;
            }
            Toast.makeText(this, "File Info Null In Zip", Toast.LENGTH_LONG).show();
        }
    }

    public boolean deleteDirectory(File file) {
        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            for (File file2 : listFiles) {
                deleteDirectory(file2);
                System.out.println(file2.getAbsolutePath());
            }
        }
        return file.delete();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 2002 && i2 == -1 && intent != null && intent.getData() != null) {
            Toast.makeText(this, intent.getData().toString(), Toast.LENGTH_LONG).show();
        }
        super.onActivityResult(i, i2, intent);
    }
}
