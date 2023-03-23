package com.example.musicapplication.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.musicapplication.R;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 6000;
    Animation topAnim, botAnim, blinkAnim;
    LottieAnimationView animationView,animationView1,animationView2;
    TextView tvSplashTitle, tvSplashsub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        setContentView(R.layout.activity_splash);

        init();
        animationView.playAnimation();
        if (animationView.isAnimating()){

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    animationView1.setVisibility(View.GONE);
                    animationView2.setVisibility(View.GONE);

                }
            }, 3000);

            tvSplashTitle.setAnimation(botAnim);
            tvSplashsub.setAnimation(botAnim);
            animationView1.setVisibility(View.VISIBLE);
            animationView2.setVisibility(View.VISIBLE);


        }


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {



                Intent dashboard = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(dashboard);
                finish();
            }
        }, SPLASH_SCREEN);
    }

    public void init(){
//        topAnim = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.top_animation);
        botAnim = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.bottom_animation);
        blinkAnim = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.blink_animation);
        animationView2 = findViewById(R.id.animation_view2);
        animationView = findViewById(R.id.animation_view);
        animationView1 = findViewById(R.id.animation_view1);
        tvSplashsub = findViewById(R.id.tvSplashSlogan);
        tvSplashTitle = findViewById(R.id.tvSplashTitle);

    }
}