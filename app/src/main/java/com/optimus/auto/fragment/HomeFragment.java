package com.optimus.auto.fragment;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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

/* loaded from: classes2.dex */
public class HomeFragment extends Fragment {
    private AppCompatActivity activity;
    private ArrayList<Actor> actors;
    private ActorAdapter adapter;
    private FirebaseAuth auth;
    private DatabaseReference database;
    private GridView gvActor;
    private FirebaseUser user;
    private View view;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        activity = (AppCompatActivity) getActivity();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        auth = firebaseAuth;
        if (firebaseAuth.getCurrentUser() == null ||
                this.auth.getCurrentUser() != null && !this.auth.getCurrentUser().isEmailVerified()) {
            this.activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new LoginFragment()).commit();
            MainActivity.currentFragment = MainActivity.FRAGMENT_LOGIN;
        }


        user = this.auth.getCurrentUser();
        view = layoutInflater.inflate(R.layout.fragment_home, viewGroup, false);
        setHasOptionsMenu(true);
        database = FirebaseDatabase.getInstance().getReference();
        initView();


        gvActor.setOnItemClickListener((adapterView, view, i, j) -> {
            Actor actor = adapter.getActorsFiltered().get(i);
            showDialogSkin(actor.getName(), actor.getFolderName());
        });
        return this.view;
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
                Toast.makeText(activity, "Hiện tại app chưa hỗ trợ thiết bị của bạn", Toast.LENGTH_LONG).show();
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