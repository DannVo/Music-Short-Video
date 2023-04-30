package com.example.musicapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.musicapplication.Login.LoginActivity;
import com.example.musicapplication.StatePattern.PremiumUser;
import com.example.musicapplication.StatePattern.Short;
import com.example.musicapplication.StatePattern.User;
import com.example.musicapplication.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton btn;
    ActivityMainBinding activityMainBinding;
    boolean doubleBackToExitPressedOnce = false;

    SharedPreferences sharedPreferences;
    public static final String userID = "userID";
    public static final String fileName = "login";
    public static final String userName = "username";
    public static final String signUpName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                ,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        btn = (FloatingActionButton) findViewById(R.id.add);

        sharedPreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);

        if (sharedPreferences.contains(userName)){
            Toast.makeText(MainActivity.this, "Welcome back, "+sharedPreferences.getString(userName,"")+"!", Toast.LENGTH_SHORT).show();
        }



        replaceFragment(new HomeFragment());
        activityMainBinding.bottomNaviView.setBackground(null);

        activityMainBinding.bottomNaviView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    if (sharedPreferences.getString(userName,"").equals("danh123")){
                        User user = new User();
                        user.setState(new PremiumUser());
                        user.setBtn(btn);
                        user.applyState();
                    }
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.shorts:
                    User user = new User();
                    user.setState(new Short());
                    user.setBtn(btn);
                    user.applyState();
                    replaceFragment(new MainShortFragment());
                    break;
                case R.id.subscription:
                    replaceFragment(new SubscriptFragment());
                    break;
                case R.id.library:
                    replaceFragment(new LibraryFragment());
                    break;
            }
            return true;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
    }

    protected void onStart() {
        super.onStart();
        if (!sharedPreferences.contains(userName)){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            //Exit the Music App
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;

            }
        }, 2000);


    }

    @Override
    protected void onDestroy() {
        // destroy completely all processes run in background
        android.os.Process.killProcess(android.os.Process.myPid());
        super.onDestroy();

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}