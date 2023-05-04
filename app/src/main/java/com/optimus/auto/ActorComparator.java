package com.optimus.auto;

import java.util.Comparator;

/* loaded from: classes2.dex */
public class ActorComparator implements Comparator<Actor> {
    @Override // java.util.Comparator
    public int compare(Actor actor, Actor actor2) {
        return actor.getName().compareTo(actor2.getName());
    }
}
