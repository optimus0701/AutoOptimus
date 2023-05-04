package com.optimus.auto.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.optimus.auto.MainActivity;
import com.optimus.auto.R;


public class LoginFragment extends Fragment {
    private FragmentActivity activity;
    private Button btnLogin;
    private TextView btnResetPass;
    private TextView btnSignUp;
    private Dialog dialog;
    private TextInputEditText edtEmail;
    private TextInputEditText edtPass;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_login, viewGroup, false);

        activity = getActivity();
        mAuth = FirebaseAuth.getInstance();

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(1);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog);


        edtEmail = inflate.findViewById(R.id.email);
        edtPass = inflate.findViewById(R.id.password);
        btnLogin = inflate.findViewById(R.id.btn_login);
        btnResetPass = inflate.findViewById(R.id.btn_reset_password);
        btnSignUp = inflate.findViewById(R.id.btn_signup);
        btnLogin.setOnClickListener(v -> {
            String email = LoginFragment.this.edtEmail.getText().toString().trim();
            String password = LoginFragment.this.edtPass.getText().toString().trim();
            if (email.isEmpty()) {
                edtEmail.setError("Nhập Địa Chỉ Email!");
            } else if (password.isEmpty()) {
                edtPass.setError("Nhập Mật Khẩu!");
            } else {
                dialog.show();
                if (mAuth.getCurrentUser() != null) {
                    mAuth.signOut();
                }
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener((task -> {
                    if (task.isSuccessful()) {
                        LoginFragment.this.dialog.dismiss();
                        FirebaseUser currentUser = LoginFragment.this.mAuth.getCurrentUser();
                        if (currentUser == null) {
                            Toast.makeText(LoginFragment.this.activity, "User is null! Contact admin", Toast.LENGTH_LONG).show();
                        } else if (currentUser.isEmailVerified()) {
                            Toast.makeText(LoginFragment.this.getActivity(), "Đăng Nhập Thành Công!", Toast.LENGTH_LONG).show();
                            LoginFragment.this.activity.recreate();
                        } else {
                            Toast.makeText(LoginFragment.this.getActivity(), "Vui Lòng Xác Thực Tài Khoản!", Toast.LENGTH_LONG).show();
                        }
                    } else if (task.getException() != null) {
                        LoginFragment.this.dialog.dismiss();
                        FragmentActivity fragmentActivity = LoginFragment.this.activity;
                        Toast.makeText(fragmentActivity, "Lỗi! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }));
            }
        });
        this.btnResetPass.setOnClickListener(view -> {
            FragmentManager supportFragmentManager = activity.getSupportFragmentManager();
            MainActivity.currentFragment = MainActivity.FRAGMENT_RESET_PASS;
            supportFragmentManager.beginTransaction().replace(R.id.content_frame, new ResetPassFragment()).commit();
        });
        this.btnSignUp.setOnClickListener(view -> {
            FragmentManager supportFragmentManager = activity.getSupportFragmentManager();
            MainActivity.currentFragment = MainActivity.FRAGMENT_SIGNUP;
            supportFragmentManager.beginTransaction().replace(R.id.content_frame, new SignupFragment()).commit();
        });
        return inflate;
    }

}