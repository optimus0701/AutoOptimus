package com.optimus.auto.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.optimus.auto.MainActivity;
import com.optimus.auto.R;


public class SignupFragment extends Fragment {
    private FragmentActivity activity;
    private Button btnBackToLogin;
    private TextView btnResetPass;
    private TextView btnSignIn;
    private Button btnSignUp;
    private DatabaseReference databaseReference;
    private Dialog dialog;
    private TextInputEditText edtEmail;
    private TextInputEditText edtPass;
    private TextInputEditText edtPassConfirm;
    private TextInputEditText edtUsername;
    private FirebaseAuth mAuth;
    private LinearLayout view_sign_up;
    private LinearLayout view_verify;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_signup, viewGroup, false);
        activity = getActivity();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        dialog = new Dialog(getActivity());

        dialog.requestWindowFeature(1);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog);

        btnSignUp = (Button) inflate.findViewById(R.id.sign_up_button);
        btnSignIn = (TextView) inflate.findViewById(R.id.sign_in_button);
        btnResetPass = (TextView) inflate.findViewById(R.id.btn_reset_password);
        btnBackToLogin = (Button) inflate.findViewById(R.id.btn_back_login);
        edtEmail = (TextInputEditText) inflate.findViewById(R.id.email);
        edtPass = (TextInputEditText) inflate.findViewById(R.id.password);
        edtUsername = (TextInputEditText) inflate.findViewById(R.id.username);
        edtPassConfirm = (TextInputEditText) inflate.findViewById(R.id.RePassword);
        view_sign_up = (LinearLayout) inflate.findViewById(R.id.view_sign_up);
        view_verify = (LinearLayout) inflate.findViewById(R.id.view_verify);
        view_sign_up.setVisibility(View.VISIBLE);
        view_verify.setVisibility(View.GONE);


        btnSignUp.setOnClickListener(view -> {
            String email = SignupFragment.this.edtEmail.getText().toString().trim();
            String password = SignupFragment.this.edtPass.getText().toString().trim();
            final String username = SignupFragment.this.edtUsername.getText().toString().trim();
            String passConfirm = SignupFragment.this.edtPassConfirm.getText().toString().trim();
            if (username.isEmpty()) {
                edtUsername.setError("Nhập Tên Tài Khoản!");
            } else if (email.isEmpty()) {
                edtEmail.setError("Nhập Địa Chỉ Email!");
            } else if (password.isEmpty()) {
                edtPass.setError("Nhập Mật Khẩu!");
            } else if (passConfirm.isEmpty()) {
                edtPassConfirm.setError("Nhập Lại Mật Khẩu!");
            } else if (!passConfirm.equals(password)) {
                edtPassConfirm.setError("Nhập Lại Mật Khẩu Như Bên Trên!");
            } else if (username.length() > 12) {
                edtUsername.setError("Tên tài Khoản Quá Dài!");
            } else {
                dialog.show();
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        if (currentUser != null) {
                            currentUser.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(username).build());
                            if (!currentUser.isEmailVerified()) {
                                currentUser.sendEmailVerification();
                                view_verify.setVisibility(View.VISIBLE);
                                view_sign_up.setVisibility(View.GONE);
                                return;
                            }
                        }
                        dialog.dismiss();
                        view_verify.setVisibility(View.GONE);
                        view_sign_up.setVisibility(View.VISIBLE);
                        return;
                    }
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "Lỗi! Vui Lòng Thử Lại Sau!", Toast.LENGTH_LONG).show();
                });
            }
        });


        btnSignIn.setOnClickListener(view -> {
            FragmentManager supportFragmentManager = activity.getSupportFragmentManager();
            MainActivity.currentFragment = MainActivity.FRAGMENT_LOGIN;
            supportFragmentManager.beginTransaction().replace(R.id.content_frame, new LoginFragment()).commit();
        });


        btnResetPass.setOnClickListener(view -> {
            FragmentManager supportFragmentManager = activity.getSupportFragmentManager();
            MainActivity.currentFragment = MainActivity.FRAGMENT_RESET_PASS;
            supportFragmentManager.beginTransaction().replace(R.id.content_frame, new ResetPassFragment()).commit();
        });


        btnBackToLogin.setOnClickListener(view -> {
            view_sign_up.setVisibility(View.VISIBLE);
            view_verify.setVisibility(View.GONE);
            FragmentManager supportFragmentManager = activity.getSupportFragmentManager();
            MainActivity.currentFragment = MainActivity.FRAGMENT_LOGIN;
            supportFragmentManager.beginTransaction().replace(R.id.content_frame, new LoginFragment()).commit();
        });
        return inflate;
    }

}