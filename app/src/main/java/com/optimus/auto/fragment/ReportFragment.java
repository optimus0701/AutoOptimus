package com.optimus.auto.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.optimus.auto.R;
import com.optimus.auto.Report;

public class ReportFragment extends Fragment {
    private Button btnSend;
    private DatabaseReference database;
    private Dialog dialog;
    private EditText edtContent;
    private EditText edtDevice;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_report, viewGroup, false);

        dialog = new Dialog(getActivity());

        dialog.requestWindowFeature(1);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        edtContent = (EditText) inflate.findViewById(R.id.rp_edt_content);
        edtDevice = (EditText) inflate.findViewById(R.id.rp_edt_device);
        btnSend = (Button) inflate.findViewById(R.id.rp_btn_send);


        btnSend.setOnClickListener(view -> {
            String device = ReportFragment.this.edtDevice.getText().toString().trim();
            String content = ReportFragment.this.edtContent.getText().toString().trim();
            if (device.isEmpty()) {
                ReportFragment.this.edtDevice.setError("Nhập Tên Thiết Bị");
            } else if (content.isEmpty()) {
                ReportFragment.this.edtContent.setError("Nhập Nội Dung");
            } else {
                ReportFragment.this.dialog.show();
                FirebaseUser currentUser = ReportFragment.this.mAuth.getCurrentUser();
                Report report = new Report(currentUser.getDisplayName(), currentUser.getEmail(), device, content);
                ReportFragment.this.database.child("report").push().setValue(report).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ReportFragment.this.getActivity(), "Đã Gửi!", Toast.LENGTH_LONG).show();
                        ReportFragment.this.edtContent.setText("");
                        ReportFragment.this.edtDevice.setText("");
                        ReportFragment.this.dialog.dismiss();
                        return;
                    }
                    Toast.makeText(ReportFragment.this.getActivity(), "Lỗi!", Toast.LENGTH_LONG).show();
                });
            }
        });
        return inflate;
    }
}