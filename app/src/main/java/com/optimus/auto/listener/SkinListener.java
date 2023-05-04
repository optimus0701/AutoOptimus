package com.optimus.auto.listener;

import android.widget.ListAdapter;
import android.widget.ListView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.optimus.auto.SkinAdapter;
import java.util.ArrayList;


public class SkinListener implements ChildEventListener {
    private SkinAdapter adapterSkin;
    private ListView lvSkin;
    private ArrayList<String> skins;

    @Override
    public void onCancelled(DatabaseError databaseError) {
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String str) {
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String str) {
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
    }

    public SkinListener(ArrayList<String> arrayList, SkinAdapter skinAdapter, ListView listView) {
        this.skins = arrayList;
        this.adapterSkin = skinAdapter;
        this.lvSkin = listView;
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String str) {
        this.skins.add(dataSnapshot.getKey());
        this.lvSkin.setAdapter((ListAdapter) this.adapterSkin);
    }
}
