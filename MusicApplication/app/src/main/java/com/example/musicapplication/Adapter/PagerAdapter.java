package com.example.musicapplication.Adapter;

import android.Manifest;
import android.animation.Animator;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.musicapplication.BuildConfig;
import com.example.musicapplication.Model.DataShortHandler;
import com.example.musicapplication.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

public class PagerAdapter extends RecyclerView.Adapter<PagerAdapter.ViewHolder> {

    private ArrayList<DataShortHandler> data;
    private Activity activity;

    Integer colorLike = 0, colorDislike = 0;

    public PagerAdapter(ArrayList<DataShortHandler> data, Activity activity) {
        this.data = data;
        this.activity = activity;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_short, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //CHECK PERMISSION SCREENSHOT
        verifyStoragePermission(activity);
        holder.takePic.setOnClickListener(view -> takeScreenShot(activity.getWindow().getDecorView().getRootView(), "result"));

        holder.progressBar.setVisibility(View.VISIBLE);
        addInfoShort(holder, position);

        holder.videoView.setVideoURI(Uri.parse(data.get(position).getUrlVideo()));
        holder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                holder.progressBar.setVisibility(View.GONE);
                mediaPlayer.start();

                float vidRatio = mediaPlayer.getVideoWidth() / (float) mediaPlayer.getVideoHeight();
                float screenRatio = holder.videoView.getWidth() / (float) holder.videoView.getHeight();

                float scale = vidRatio / screenRatio;

                if (scale >= 1) {
                    holder.videoView.setScaleX(scale);
                } else {
                    holder.videoView.setScaleY(1f / scale);
                }

                holder.videoView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.pause();
                        } else {
                            mediaPlayer.start();
                        }

                    }
                });
            }
        });

        holder.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });


    }

    private void addInfoShort(ViewHolder holder, int position) {
        holder.title.setText(data.get(position).getDescript());
        holder.accName.setText(data.get(position).getAccName());
        holder.tag.setText(data.get(position).getTag());
        holder.like.setText(data.get(position).getLikeTotal());
        holder.comment.setText(data.get(position).getComments());

        holder.tvLikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int default_color = Color.parseColor("#ffffff");
                int onclick_color = Color.parseColor("#1971b0");
                Log.e("CHECK", "LIKE BUTTON");
                holder.lottieLike.addAnimatorListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        Log.e("Animation:", "start");
                        holder.lottieLike.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Log.e("Animation:", "end");
                        holder.lottieLike.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        Log.e("Animation:", "cancel");
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                        Log.e("Animation:", "repeat");
                    }
                });

                if ((colorLike.equals(default_color) || colorLike.equals(0)) && (colorDislike.equals(default_color) || colorDislike.equals(0))) {
                    setTextViewDrawableColor(holder.tvLikeBtn, onclick_color);
                    colorLike = onclick_color;
                    holder.lottieLike.setVisibility(View.VISIBLE);
                    holder.lottieLike.playAnimation();

                } else {
                    setTextViewDrawableColor(holder.tvLikeBtn, default_color);
                    colorLike = default_color;
                }
            }
        });

        holder.tvDislikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int default_color = Color.parseColor("#ffffff");
                int onclick_color = Color.parseColor("#1971b0");

                if ((colorDislike.equals(default_color) || colorDislike.equals(0)) && (colorLike.equals(default_color) || colorLike.equals(0))) {
                    setTextViewDrawableColor(holder.tvDislikeBtn, onclick_color);
                    colorDislike = onclick_color;

                } else {
                    setTextViewDrawableColor(holder.tvDislikeBtn, default_color);
                    colorDislike = default_color;
                }
            }
        });

    }

    private void setTextViewDrawableColor(TextView textView, int color) {
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        VideoView videoView;
        TextView title, accName, tag;
        TextView like, comment;

        TextView tvLikeBtn, tvDislikeBtn;
        LottieAnimationView lottieLike;
        ProgressBar progressBar;
        TextView takePic;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            videoView = itemView.findViewById(R.id.viewVideo);
            title = itemView.findViewById(R.id.tvTitleDesc);
            accName = itemView.findViewById(R.id.tvNameAcc);
            tag = itemView.findViewById(R.id.tvTag);
            like = itemView.findViewById(R.id.tvLikeTotal);
            comment = itemView.findViewById(R.id.tvCommentTotal);
            tvLikeBtn = itemView.findViewById(R.id.tvLikeBtn);
            tvDislikeBtn = itemView.findViewById(R.id.tvDislikeBtn);
            lottieLike = itemView.findViewById(R.id.lavLikeReact);
            progressBar = itemView.findViewById(R.id.progressBar);
            takePic = itemView.findViewById(R.id.screenShot);
        }
    }

    protected static File takeScreenShot(View view, String filename) {
        Date date = new Date();
        CharSequence format = DateFormat.format("dd-MM-yyyy_hh:mm:ss", date);
        try {
            String dirPath = Environment.getExternalStorageDirectory().toString() + "/screenshot";
            File fileDir = new File(dirPath);
            if (!fileDir.exists()) {
                boolean mkdir = fileDir.mkdir();
            }

            String path = dirPath + "/" + filename + "-" + format + ".jpeg";

            view.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
            view.setDrawingCacheEnabled(false);

            File imageFile = new File(path);

            FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            return imageFile;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSION_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    public static void verifyStoragePermission(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    PERMISSION_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }
}