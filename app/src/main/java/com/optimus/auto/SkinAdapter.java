package com.optimus.auto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class SkinAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<String> skins;

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return null;
    }

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return 0L;
    }

    public SkinAdapter(Context context, int i, ArrayList<String> arrayList) {
        this.context = context;
        this.layout = i;
        this.skins = arrayList;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.skins.size();
    }

    /* loaded from: classes2.dex */
    private class ViewHolder {
        TextView tvSkin;

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
            viewHolder.tvSkin = (TextView) view2.findViewById(R.id.item_skin);
            view2.setTag(viewHolder);
        } else {
            view2 = view;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tvSkin.setText(this.skins.get(i));
        return view2;
    }
}
