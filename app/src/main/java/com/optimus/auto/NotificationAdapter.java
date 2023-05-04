package com.optimus.auto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

/* loaded from: classes2.dex */
public class NotificationAdapter extends BaseAdapter {
    private List<Notification> array;
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

    public NotificationAdapter(Context context, int i, List<Notification> list) {
        this.context = context;
        this.layout = i;
        this.array = list;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.array.size();
    }

    /* loaded from: classes2.dex */
    private class ViewHolder {
        TextView tv_content;
        TextView tv_username;

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
            viewHolder.tv_username = (TextView) view2.findViewById(R.id.item_no_tv_username);
            viewHolder.tv_content = (TextView) view2.findViewById(R.id.item_no_tv_content);
            view2.setTag(viewHolder);
        } else {
            view2 = view;
            viewHolder = (ViewHolder) view.getTag();
        }
        Notification notification = this.array.get(i);
        viewHolder.tv_username.setText(notification.getUsername());
        viewHolder.tv_content.setText(notification.getContent());
        return view2;
    }
}
