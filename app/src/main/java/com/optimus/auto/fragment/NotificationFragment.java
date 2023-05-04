package com.optimus.auto.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.optimus.auto.Notification;
import com.optimus.auto.NotificationAdapter;
import com.optimus.auto.R;
import com.optimus.auto.task.ResetModTask;
import com.startapp.sdk.adsbase.StartAppAd;
import java.util.ArrayList;
import java.util.List;


public class NotificationFragment extends Fragment {
    private NotificationAdapter adapter;
    private List<Notification> array;
    private Button btnResetMod;
    private DatabaseReference database;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_notification, viewGroup, false);
        listView = (ListView) inflate.findViewById(R.id.lv_notification);
        btnResetMod = (Button) inflate.findViewById(R.id.btn_reset);
        array = new ArrayList();
        adapter = new NotificationAdapter(getActivity(), R.layout.item_notifications, this.array);

        Notification notification = new Notification("Admin", "Cảm Ơn Bạn Đã Tải Ứng Dụng!\nỨng Dụng Thuộc Về Đô-Optimus.\nVui Lòng Không Reup Dưới Mọi Hình Thức.");

        database = FirebaseDatabase.getInstance().getReference();

        database.child("notifications").addChildEventListener(new ChildEventListener() {
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

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String str) {
               array.add((Notification) dataSnapshot.getValue(Notification.class));
               listView.setAdapter((ListAdapter) adapter);
            }
        });

        btnResetMod.setOnClickListener(view -> {
            StartAppAd.showAd(getActivity());
            Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(1);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.dialog);
            dialog.show();
            new ResetModTask(dialog).execute("http://vido0701.store/app/reset/v24.1.zip");
        });
        return inflate;
    }


}