package com.example.musicapplication;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.musicapplication.Model.DataShortHandler;

import java.util.ArrayList;


public class ShortFragment extends Fragment {

    VideoView videoView;
    ImageView imageView;
    TextView tvLikeBtn, tvDislikeBtn;
    LottieAnimationView lottieLike;
    Integer colorLike = 0, colorDislike = 0;

    FrameLayout shortFragment;
    Context context;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        init(view);

        super.onViewCreated(view, savedInstanceState);

//        List<Fragment> list = new ArrayList<>();
//        list.add(new ShortTwoFragment());
//
//        pagerAdapter = new VPageAdapter(context,list);
//        verticalViewPager.setAdapter(pagerAdapter);

//        String video_path = new StringBuilder("android.resource://")
//                .append(getContext().getPackageName())
//                .append("/raw/short_moment_1")
//                .toString();
//
//        Log.e("VIDEO PATH: ", video_path);
//        Uri uri = Uri.parse(video_path);
//        videoView.setVideoPath(video_path);
//        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mediaPlayer) {
//                mediaPlayer.start();
//
//            }
//        });
//
//        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mediaPlayer) {
//                mediaPlayer.start();
//            }
//        });
//        videoView.start();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
//        getActivity().setContentView(R.layout.fragment_short);
        context = container.getContext();
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_short, container, false);
        return viewGroup;
    }

    public void init(View view){
        videoView = view.findViewById(R.id.viewVideo);
        imageView = view.findViewById(R.id.imageView);
        tvLikeBtn = view.findViewById(R.id.tvLikeBtn);
        tvDislikeBtn = view.findViewById(R.id.tvDislikeBtn);
        lottieLike = view.findViewById(R.id.lavLikeReact);
        shortFragment = view.findViewById(R.id.shortFragment);

    }
}