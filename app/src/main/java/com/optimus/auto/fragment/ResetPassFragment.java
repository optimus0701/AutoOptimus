package com.optimus.auto.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.optimus.auto.MainActivity;
import com.optimus.auto.R;


public class ResetPassFragment extends Fragment {
    private FragmentActivity activity;
    private Button btnBack;
    private Button btnResetPass;
    private TextInputEditText edtEmail;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_reset_password, viewGroup, false);
        activity = getActivity();
        mAuth = FirebaseAuth.getInstance();
        edtEmail = (TextInputEditText) inflate.findViewById(R.id.email);
        btnBack = (Button) inflate.findViewById(R.id.btn_back);
        btnResetPass = (Button) inflate.findViewById(R.id.btn_reset_password);


        btnResetPass.setOnClickListener(view -> {
            String email = ResetPassFragment.this.edtEmail.getText().toString().trim();
            if (email.isEmpty()) {
                ResetPassFragment.this.edtEmail.setError("Nhập Địa Chỉ Email!");
            } else {

                ResetPassFragment.this.mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ResetPassFragment.this.getActivity(), "Sent!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ResetPassFragment.this.getActivity(), "Error!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        this.btnBack.setOnClickListener(view -> {
            FragmentManager supportFragmentManager = activity.getSupportFragmentManager();
            MainActivity.currentFragment = MainActivity.FRAGMENT_LOGIN;
            supportFragmentManager.beginTransaction().replace(R.id.content_frame, new LoginFragment()).commit();
        });
        return inflate;
    }
}