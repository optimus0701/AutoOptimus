package com.optimus.auto;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.optimus.auto.fragment.HomeFragment;
import com.optimus.auto.fragment.LoginFragment;
import com.optimus.auto.fragment.NotificationFragment;
import com.optimus.auto.fragment.ReportFragment;
import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.adsbase.StartAppAd;
import java.io.File;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final int FRAGMENT_HOME = 0;
    public static final int FRAGMENT_LOGIN = 3;
    public static final int FRAGMENT_NOTIFICATION = 1;
    public static final int FRAGMENT_REPORT = 2;
    public static final int FRAGMENT_RESET_PASS = 5;
    public static final int FRAGMENT_SIGNUP = 4;
    public static final String SRC = Build.VERSION.SDK_INT >= 30 ?
            Environment.getExternalStorageDirectory() + "/"
            : Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/";
    public static int currentFragment = 0;
    private FirebaseAuth auth;
    private Banner banner;
    private Button btnLogout;
    private DrawerLayout drawerLayout;
    private View header;
    private TextView tvUserEmail;
    private TextView tvUsername;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, this.drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        header = headerView;
        headerView.setFitsSystemWindows(true);
        initView();
        this.banner.loadAd();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().findItem(R.id.item_home).setChecked(true);
        this.btnLogout.setOnClickListener(view -> logout());
    }

    public void logout() {
        this.auth.signOut();
        this.drawerLayout.closeDrawer(GravityCompat.START);
        checkUser();
    }

    public void initView() {
        this.tvUserEmail = this.header.findViewById(R.id.header_user_email);
        this.tvUsername = this.header.findViewById(R.id.header_username);
        this.btnLogout = this.header.findViewById(R.id.header_btn_logout);
        this.banner = findViewById(R.id.startAppBanner);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Utils.isPermissionGranted(this)) {
                takePermission();
            }
        }

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
        } else {
            checkUser();
        }
    }

    private void checkUser() {
        if (this.auth.getCurrentUser() != null) {
            FirebaseUser currentUser = this.auth.getCurrentUser();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new HomeFragment()).commit();
            currentFragment = 0;
            this.tvUserEmail.setVisibility(View.VISIBLE);
            this.tvUsername.setVisibility(View.VISIBLE);
            this.btnLogout.setVisibility(View.VISIBLE);
            this.tvUsername.setText(currentUser.getDisplayName());
            this.tvUserEmail.setText(currentUser.getEmail());
            return;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new LoginFragment()).commit();
        currentFragment = 3;
        this.tvUserEmail.setVisibility(View.INVISIBLE);
        this.tvUsername.setVisibility(View.INVISIBLE);
        this.btnLogout.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        System.out.println(itemId);
        if (itemId == R.id.item_home) {
            if (currentFragment != 0) {
                replaceFragment(new HomeFragment());
                currentFragment = 0;
            }
        } else if (itemId == R.id.item_notification) {
            if (currentFragment != 1) {
                replaceFragment(new NotificationFragment());
                currentFragment = 1;
            }
        } else if (itemId == R.id.item_report) {
            if (currentFragment != 2) {
                replaceFragment(new ReportFragment());
                currentFragment = 2;
            }
        } else if (itemId == R.id.item_exit) {
            finish();
        }
        this.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        StartAppAd.onBackPressed(this);
        super.onBackPressed();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.replace(R.id.content_frame, fragment);
        beginTransaction.commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                finish();
            } else {
                checkUser();
            }
        } else if (requestCode == 101) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        takePermission();
                    }
                }
            }
        }
    }






    @RequiresApi(api = Build.VERSION_CODES.R)
    private void takePermission() {
        try {
            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            intent.addCategory("android.intent.category.DEFAULT");
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, 101);
        } catch (Exception e) {
            e.printStackTrace();
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            startActivityForResult(intent, 101);
        }
    }

    public static boolean deleteDirectory(File file) {
        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            for (File file2 : listFiles) {
                deleteDirectory(file2);
                System.out.println(file2.getAbsolutePath());
            }
        }
        return file.delete();
    }


}
