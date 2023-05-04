package com.optimus.auto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes2.dex */
public class ActorAdapter extends BaseAdapter implements Filterable {
    private ArrayList<Actor> actors;
    private ArrayList<Actor> actorsFiltered;
    private Context context;
    private int layout;

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return null;
    }

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return 0L;
    }

    public ActorAdapter(Context context, int i, ArrayList<Actor> arrayList) {
        this.context = context;
        this.layout = i;
        this.actors = arrayList;
        this.actorsFiltered = arrayList;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.actorsFiltered.size();
    }

    @Override // android.widget.Filterable
    public Filter getFilter() {
        return new Filter() { // from class: com.optimus.auto.ActorAdapter.1
            @Override // android.widget.Filter
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charSequence2 = charSequence.toString();
                if (charSequence2.isEmpty()) {
                    ActorAdapter actorAdapter = ActorAdapter.this;
                    actorAdapter.actorsFiltered = actorAdapter.actors;
                } else {
                    ArrayList arrayList = new ArrayList();
                    Iterator it = ActorAdapter.this.actors.iterator();
                    while (it.hasNext()) {
                        Actor actor = (Actor) it.next();
                        if (actor.getName().toLowerCase().contains(charSequence2.toLowerCase())) {
                            arrayList.add(actor);
                        }
                    }
                    ActorAdapter.this.actorsFiltered = arrayList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = ActorAdapter.this.actorsFiltered;
                return filterResults;
            }

            @Override // android.widget.Filter
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                ActorAdapter.this.actorsFiltered = (ArrayList) filterResults.values;
                ActorAdapter.this.notifyDataSetChanged();
            }
        };
    }

    public ArrayList<Actor> getActorsFiltered() {
        return this.actorsFiltered;
    }

    /* loaded from: classes2.dex */
    private class ViewHolder {
        ImageView img_avt;
        TextView tv_name;

        private ViewHolder() {
        }
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view2;
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view2 = ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(this.layout, (ViewGroup) null);
            viewHolder.img_avt = (ImageView) view2.findViewById(R.id.item_hero_avt);
            viewHolder.tv_name = (TextView) view2.findViewById(R.id.item_hero_actor_name);
            view2.setTag(viewHolder);
        } else {
            view2 = view;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tv_name.setText(this.actorsFiltered.get(i).getName());
        return view2;
    }
}
