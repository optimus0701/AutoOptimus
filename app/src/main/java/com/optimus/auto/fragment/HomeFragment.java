package com.optimus.auto.fragment;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.UriPermission;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.DocumentsContract;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.optimus.auto.Actor;
import com.optimus.auto.ActorAdapter;
import com.optimus.auto.MainActivity;
import com.optimus.auto.R;
import com.optimus.auto.SkinAdapter;
import com.optimus.auto.listener.ActorListener;
import com.optimus.auto.listener.SkinListener;
import com.optimus.auto.listener.ValueActionEventListener;
import com.optimus.auto.listener.ValueInfoEventListener;
import com.startapp.sdk.adsbase.StartAppAd;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private AppCompatActivity activity;
    private ArrayList<Actor> actors;
    private ActorAdapter adapter;
    private FirebaseAuth auth;
    private DatabaseReference database;
    private GridView gvActor;
    private FirebaseUser user;
    private View view;

    private Uri treeUri;
    private Uri uri;
    private ContentResolver contentResolver;

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        activity = (AppCompatActivity) getActivity();
        contentResolver = activity.getContentResolver();
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null ||
                this.auth.getCurrentUser() != null && !this.auth.getCurrentUser().isEmailVerified()) {
            this.activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new LoginFragment()).commit();
            MainActivity.currentFragment = MainActivity.FRAGMENT_LOGIN;
        }


        user = auth.getCurrentUser();
        view = layoutInflater.inflate(R.layout.fragment_home, viewGroup, false);
        setHasOptionsMenu(true);
        database = FirebaseDatabase.getInstance().getReference();
        initView();


        if (Build.VERSION.SDK_INT >= 30) {
            uri = DocumentsContract.buildDocumentUri("com.android.externalstorage.documents", "primary:Android/data/com.garena.game.kgvn/files/Resources/1.50.1/");
            treeUri = DocumentsContract.buildTreeDocumentUri("com.android.externalstorage.documents", "primary:Android/data/com.garena.game.kgvn/files/Resources/1.50.1");
            openDirectory();
        }

        gvActor.setOnItemClickListener((adapterView, view, i, j) -> {
            Actor actor = adapter.getActorsFiltered().get(i);
            showDialogSkin(actor.getName(), actor.getFolderName());
        });
        return this.view;
    }



    private void openDirectory() {
        if (!checkIfGotAccess()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityResultContracts.StartActivityForResult activityResultContracts = new ActivityResultContracts.StartActivityForResult();
                ActivityResultLauncher launcher = registerForActivityResult(activityResultContracts, activityResult -> {
                    if (activityResult.getResultCode() != -1 || activityResult.getData() == null || activityResult.getData().getData() == null) {
                        return;
                    }
                    Uri data = activityResult.getData().getData();

                    contentResolver.takePersistableUriPermission(data, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    if (!checkIfGotAccess()) {
                        Toast.makeText(activity, "you didn't grant permission to the correct folder", Toast.LENGTH_SHORT).show();
                    }
                });
                launcher.launch(getPrimaryVolume().createOpenDocumentTreeIntent().putExtra("android.provider.extra.INITIAL_URI", uri));
            }
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private StorageVolume getPrimaryVolume() {
        return ((StorageManager) activity.getSystemService(Context.STORAGE_SERVICE)).getPrimaryStorageVolume();
    }

    private Boolean checkIfGotAccess() {
        List<UriPermission> persistedUriPermissions = activity.getContentResolver().getPersistedUriPermissions();
        for (int i = 0; i < persistedUriPermissions.size(); i++) {
            UriPermission uriPermission = persistedUriPermissions.get(i);
            if (uriPermission.getUri().equals(treeUri) && uriPermission.isWritePermission()) {
                return true;
            }
        }
        return false;
    }

    private void showDialogSkin(String actorName, final String folderName) {
        final DatabaseReference dbListSkin = database.child("ListSkin").child(actorName);

        final Dialog dialog = new Dialog(this.activity);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_list_skin);

        ListView listView = dialog.findViewById(R.id.dialog_skin_lv);

        ((TextView) dialog.findViewById(R.id.dialog_skin_title)).setText(actorName);
        final ArrayList arrayList = new ArrayList();

        SkinAdapter skinAdapter = new SkinAdapter(getActivity(), R.layout.item_skin, arrayList);
        dialog.show();
        dbListSkin.addChildEventListener(new SkinListener(arrayList, skinAdapter, listView));

        listView.setOnItemClickListener((adapterView, view, i, j) -> {
            StartAppAd.showAd(activity);
            DatabaseReference info = dbListSkin.child((String) arrayList.get(i)).child("info");
            DatabaseReference action = dbListSkin.child((String) arrayList.get(i)).child("action");
            dialog.dismiss();
            Dialog dialog2 = new Dialog(activity);
            dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog2.setCanceledOnTouchOutside(false);
            dialog2.setContentView(R.layout.dialog);
            if (Build.VERSION.SDK_INT >= 30) {
                try {
                    DocumentFile folder = DocumentFile.fromTreeUri(activity, treeUri);
                    DocumentFile folderInfo = folder.findFile("Prefab_Characters");
                    DocumentFile folderAction = folder.findFile("Ages").findFile("Prefab_Characters").findFile("Prefab_Hero");
                    if (folderInfo.exists()) {
                        info.addValueEventListener(new ValueInfoEventListener(activity, folderName, dialog2, user.getDisplayName() + user.getUid(), contentResolver, folderInfo));
                    } else {
                        Toast.makeText(activity, "File Not Found", Toast.LENGTH_SHORT).show();
                    }

                    if(folderAction.exists()) {
                        action.addValueEventListener(new ValueActionEventListener(activity, folderName, dialog2, user.getDisplayName() + user.getUid(), contentResolver, folderAction));
                    } else {
                        Toast.makeText(activity, "File Not Found", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                return;
            }
            info.addValueEventListener(new ValueInfoEventListener(activity, folderName, dialog2, user.getDisplayName() + user.getUid()));
            action.addValueEventListener(new ValueActionEventListener(activity, folderName, dialog2, user.getDisplayName() + user.getUid()));

        });
    }



    private void initView() {
        this.gvActor = this.view.findViewById(R.id.gv_actor);
        this.actors = new ArrayList<>();
        this.adapter = new ActorAdapter(getActivity(), R.layout.item_actor, this.actors);
        DatabaseReference databaseReference = this.database;
        if (databaseReference != null) {
            databaseReference.child("ListHero").addChildEventListener(new ActorListener(this.adapter, this.actors, this.gvActor));
        } else {
            Toast.makeText(getActivity(), "database is null. contact admin", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_search, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.actionSearch).getActionView();
        searchView.setQueryHint("Nhập Tên Tướng");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() { // from class: com.optimus.auto.fragment.HomeFragment.1
            @Override
            public boolean onQueryTextSubmit(String str) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String str) {
                if (HomeFragment.this.actors != null) {
                    HomeFragment.this.adapter.getFilter().filter(str);
                } else {
                    Toast.makeText(HomeFragment.this.getActivity(), "list actor is null. contact admin", Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, menuInflater);
    }
}