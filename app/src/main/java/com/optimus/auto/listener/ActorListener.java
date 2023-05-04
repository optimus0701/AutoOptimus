package com.optimus.auto.listener;

import android.widget.GridView;
import android.widget.ListAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.optimus.auto.Actor;
import com.optimus.auto.ActorAdapter;
import com.optimus.auto.ActorComparator;
import java.util.ArrayList;
import java.util.Collections;


public class ActorListener implements ChildEventListener {
    private ArrayList<Actor> actors;
    private ActorAdapter adapter;
    private GridView gridView;

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

    public ActorListener(ActorAdapter actorAdapter, ArrayList<Actor> arrayList, GridView gridView) {
        this.adapter = actorAdapter;
        this.actors = arrayList;
        this.gridView = gridView;
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String str) {
        this.actors.add((Actor) dataSnapshot.getValue(Actor.class));
        this.gridView.setAdapter((ListAdapter) this.adapter);
        Collections.sort(this.actors, new ActorComparator());
    }
}
