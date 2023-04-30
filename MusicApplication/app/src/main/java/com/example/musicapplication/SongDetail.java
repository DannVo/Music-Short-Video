package com.example.musicapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.palette.graphics.Palette;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicapplication.Adapter.ObjectSerializer;
import com.example.musicapplication.Adapter.SongAdapter;
import com.example.musicapplication.Model.SongItem;
import com.example.musicapplication.StrategyPattern.SkipButton;
import com.example.musicapplication.StrategyPattern.SkipNextSongButton;
import com.example.musicapplication.StrategyPattern.SkipPreviousSongButton;
import com.example.musicapplication.databinding.ActivityMainBinding;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaMetadata;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.EventListener;
import com.google.android.exoplayer2.Player.MediaItemTransitionReason;
import com.jgabrielfreitas.core.BlurImageView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SongDetail extends AppCompatActivity implements Player.Listener{

    public static SongItem songItem;
    public static ExoPlayer player;
    public static TextView playPauseBtn, skipNextBtn, skipPrevBtn;
    TextView tvTitle, tvAuthor, durationTime, progressView,
            repeatBtn, shuffleBtn, hideSongBtn,
            playerTitle;
    ImageView artworkImg;
    SeekBar seekBar;
    public static ConstraintLayout playerView;
    SharedPreferences sharedPreferences;
    List<SongItem> listSongs;
    BlurImageView blurImageView;

    int repeatMode = 1; //repeat all = 1. repeat one = 2, normal = 3
    int shuffleMode = 0;
    public static int isPlayingSong = 0;


    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onResume() {

//        sharedPreferences = getSharedPreferences("player_song", Context.MODE_PRIVATE);
//        Integer position = sharedPreferences.getInt("position", 9999);
//        if(player.isPlaying() && isPlayingSong == 1) {
//            Toast.makeText(this, "on Resume Pause", Toast.LENGTH_SHORT).show();
//            player.pause();
//            player.seekTo(position,0);
//        }

        super.onResume();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                ,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_song_detail);

        init();
        Intent intent = getIntent();

        //get posi, songs
        sharedPreferences = getSharedPreferences("player_song", Context.MODE_PRIVATE);
        Integer position = sharedPreferences.getInt("position", 9999);
        String list_songs = sharedPreferences.getString("list_songs", "");

        try {
            listSongs = (List<SongItem>) ObjectSerializer.deserialize(sharedPreferences.getString("list", ""));
            Log.e("List Songs: ", listSongs.get(position).getTitle().toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

        player = new ExoPlayer.Builder(this).build();
        //Toast.makeText(this, "This is posi "+ position+" "+isPlayingSong, Toast.LENGTH_SHORT).show();

        //play mp3 file
//        if(!player.isPlaying() && isPlayingSong == 0){
//            Toast.makeText(this, "Pause 1", Toast.LENGTH_SHORT).show();
//            player.setMediaItems(getMediaItems(), position, 0);
//            isPlayingSong = 1;
//        }
//        else if(isPlayingSong==2){
//            Toast.makeText(this, "Pause", Toast.LENGTH_SHORT).show();
////            player.pause();
////            player.seekTo(position,0);
//            player.setMediaItems(getMediaItems(), position, 0);
//            isPlayingSong = 1;
//        }
        //Toast.makeText(this, "Pause 1", Toast.LENGTH_SHORT).show();
        player.setMediaItems(getMediaItems(), position, 0);
        isPlayingSong = 1;
        player.prepare();
        player.play();

        songItem = (SongItem) intent.getSerializableExtra("songdata");
        //Toast.makeText(SongDetail.this, "Song Playing "+songItem.getTitle(), Toast.LENGTH_SHORT).show();
//        playerControls();

        //song name marque
        playerTitle.setSelected(true);

        player.addListener(new Player.Listener() {
            @Override
            public void onEvents(Player player, Player.Events events) {
                //Toast.makeText(SongDetail.this, "Check Curr "+songItem.getTitle(), Toast.LENGTH_SHORT).show();
//                storeCurrSong(songItem.getTitle(),songItem.getAuthor(), songItem.getDuration(), songItem.getArtwork_uri());
                Player.Listener.super.onEvents(player, events);
                //Toast.makeText(SongDetail.this, "onEvents", Toast.LENGTH_SHORT).show();

//                SongAdapter songAdapter = new SongAdapter();
//                tvTitle.setSelected(true);
//                tvTitle.setText(songItem.getTitle());
//                tvAuthor.setText(songItem.getAuthor());
//                loadImage(songItem.getArtwork_uri(), artworkImg);
//                durationTime.setText(songAdapter.convertDuration(songItem.getDuration()));
//                progressView.setText(convertDuration(player.getCurrentPosition()));
//                seekBar.setProgress((int) player.getCurrentPosition());
//                seekBar.setMax((int) player.getDuration());
//
//                updatePositionProgress();
            }

            @Override
            public void onMediaItemTransition(MediaItem mediaItem, int reason) {
                Player.Listener.super.onMediaItemTransition(mediaItem, reason);

                assert mediaItem !=null;
                //Toast.makeText(SongDetail.this, "onMedia", Toast.LENGTH_SHORT).show();
                tvTitle.setSelected(true);

                playerTitle.setText(mediaItem.mediaMetadata.title+" - "+mediaItem.mediaMetadata.artist);


                tvTitle.setText(mediaItem.mediaMetadata.title);
                tvAuthor.setText(mediaItem.mediaMetadata.artist);
                loadImage(songItem.getArtwork_uri(), artworkImg);

                durationTime.setText(convertDuration(songItem.getDuration()));
                progressView.setText(convertDuration(player.getCurrentPosition()));
                seekBar.setProgress((int) player.getCurrentPosition());
                seekBar.setMax((int) player.getDuration());
                playPauseBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_pause_circle_outline_24,0 ,0 ,0 );

                //blur image background
                updatePlayerBackgound();

                storeCurrSong(mediaItem.mediaMetadata.title.toString(),
                        mediaItem.mediaMetadata.artist.toString(),
                        songItem.getDuration(),
                        songItem.getArtwork_uri());
                updatePositionProgress();
                if(!player.isPlaying()){
                    //Toast.makeText(SongDetail.this, "Play and Pause", Toast.LENGTH_SHORT).show();
                    playPauseBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_play_circle_outline_38,0 ,0 ,0 );
                    player.play();
                }


            }

            @Override
            public void onPlaybackStateChanged(int playbackState) {
                Player.Listener.super.onPlaybackStateChanged(playbackState);

                if(playbackState == ExoPlayer.STATE_READY){
                    //Toast.makeText(SongDetail.this, "onPlayback1", Toast.LENGTH_SHORT).show();
                    playerTitle.setText(Objects.requireNonNull(player.getCurrentMediaItem()).mediaMetadata.title+
                            " - "+Objects.requireNonNull(player.getCurrentMediaItem()).mediaMetadata.artist);

                    tvTitle.setText(Objects.requireNonNull(player.getCurrentMediaItem()).mediaMetadata.title);
                    tvAuthor.setText(Objects.requireNonNull(player.getCurrentMediaItem()).mediaMetadata.artist);
                    loadImage(player.getCurrentMediaItem().mediaMetadata.artworkUri.toString(), artworkImg);

                    progressView.setText(convertDuration(player.getCurrentPosition()));
                    durationTime.setText(convertDuration(player.getDuration()));
                    seekBar.setMax((int)player.getDuration());
                    seekBar.setProgress((int) player.getCurrentPosition());
                    playPauseBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_pause_circle_outline_24,0 ,0 ,0 );

                    //blur image background
                    updatePlayerBackgound();

                    //store current song
                    storeCurrSong(Objects.requireNonNull(player.getCurrentMediaItem()).mediaMetadata.title.toString(),
                            Objects.requireNonNull(player.getCurrentMediaItem()).mediaMetadata.artist.toString(),
                            (int)player.getDuration(),
                            player.getCurrentMediaItem().mediaMetadata.artworkUri.toString());

                    updatePositionProgress();
                    if(!player.isPlaying()){
                        //Toast.makeText(SongDetail.this, "Play and Pause2", Toast.LENGTH_SHORT).show();
                        playPauseBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_play_circle_outline_38,0 ,0 ,0 );

                    }
                }else{
                    //Toast.makeText(SongDetail.this, "onPlayback2", Toast.LENGTH_SHORT).show();
                    playerTitle.setText(Objects.requireNonNull(player.getCurrentMediaItem()).mediaMetadata.title+
                            " - "+Objects.requireNonNull(player.getCurrentMediaItem()).mediaMetadata.artist);

                    tvTitle.setText(Objects.requireNonNull(player.getCurrentMediaItem()).mediaMetadata.title);
                    tvAuthor.setText(Objects.requireNonNull(player.getCurrentMediaItem()).mediaMetadata.artist);
                    loadImage(player.getCurrentMediaItem().mediaMetadata.artworkUri.toString(), artworkImg);

                    progressView.setText(convertDuration(player.getCurrentPosition()));
                    durationTime.setText(convertDuration(player.getDuration()));
                    seekBar.setMax((int)player.getDuration());
                    seekBar.setProgress((int) player.getCurrentPosition());
                    playPauseBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_play_circle_outline_38,0 ,0 ,0 );

                    //blur image background
                    updatePlayerBackgound();

                    //store current song
                    storeCurrSong(Objects.requireNonNull(player.getCurrentMediaItem()).mediaMetadata.title.toString(),
                            Objects.requireNonNull(player.getCurrentMediaItem()).mediaMetadata.artist.toString(),
                            (int)player.getDuration(),
                            player.getCurrentMediaItem().mediaMetadata.artworkUri.toString());
                }
            }
        });
        skipNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //Skip next track
        /*
        skipNextBtn.setOnClickListener(view -> skipNextSong());

        //Skip previous track
        skipPrevBtn.setOnClickListener(view -> skipPrevSong());
        */
        skipNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SkipButton skipNext = new SkipNextSongButton();
                skipNext.skipSong(player);
            }
        });
        skipPrevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SkipButton skipPrevious = new SkipPreviousSongButton();
                skipPrevious.skipSong(player);
            }
        });

        //Play Or Pause
        playPauseBtn.setOnClickListener(view -> playOrPause());

        //Seek bar listener
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressValue = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progressValue = seekBar.getProgress();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(player.getPlaybackState() == ExoPlayer.STATE_READY){
                    seekBar.setProgress(progressValue);
                    progressView.setText(convertDuration(progressValue));
                    player.seekTo(progressValue);
                }
            }
        });

        //Repeat music track
        repeatBtn.setOnClickListener(view -> {
            if(repeatMode == 1){
                //repeat one
                player.setRepeatMode(ExoPlayer.REPEAT_MODE_ONE);
                repeatMode = 2;
                repeatBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_repeat_one_24,0,0,0);

            }else if(repeatMode == 2){
                //repeat all
                player.setRepeatMode(ExoPlayer.REPEAT_MODE_ALL);
                repeatMode = 3;
                repeatBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_repeat_on_24,0,0,0);
            }else if(repeatMode == 3){
                //repeat normal
                player.setRepeatMode(ExoPlayer.REPEAT_MODE_OFF);
                repeatMode = 1;
                repeatBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_repeat_28,0,0,0);
            }
        });

        //Shuffle music
        shuffleBtn.setOnClickListener(view -> {
            if(shuffleMode == 0){
                player.setShuffleModeEnabled(true);
                shuffleMode = 1;
                shuffleBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_shuffle_on_24,0,0,0);
            }else{
                player.setShuffleModeEnabled(false);
                shuffleMode = 0;
                shuffleBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_shuffle_28,0,0,0);
            }
        });

        //Hide Song Detail
        hideSongBtn.setOnClickListener(view -> {
//            isPlayingSong = 1;
            onBackPressed();
        });

        
    }

    private void storeCurrSong(String title, String author, Integer duration, String artwork){
        //store in sharedReference
        sharedPreferences = getSharedPreferences("curr_player_song", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //Toast.makeText(this, title+" "+author, Toast.LENGTH_SHORT).show();
        editor.putString("CurrTitle", title);
        editor.putString("CurrAuthor", author);
        editor.putString("CurrDura", duration.toString());
        editor.putString("CurrArtwork", artwork);
        editor.commit();

    }

    public static void playOrPause() {
         if(player.isPlaying()){
             player.pause();
             playPauseBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_play_circle_outline_38,0,0,0);
         }else{
             player.play();
             playPauseBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_pause_circle_outline_24,0,0,0);
         }
    }

    public static void skipNextSong() {
        if(player.hasNextMediaItem()){
            player.seekToNext();
        }
    }

    public static void skipPrevSong() {
        if(player.hasPreviousMediaItem()){
            player.seekToPrevious();
        }
    }

    private List<MediaItem> getMediaItems() {
        List<MediaItem> mediaItems = new ArrayList<>();
        for(SongItem song: listSongs){
            MediaItem mediaItem = new MediaItem.Builder()
                    .setUri(song.getSong_uri())
                    .setMediaMetadata(getMetadata(song))
                    .build();

            mediaItems.add(mediaItem);
        }
        return mediaItems;
    }

    private MediaMetadata getMetadata(SongItem song) {
        return new MediaMetadata.Builder()
                .setTitle(song.getTitle())
                .setArtist(song.getAuthor())
                .setMediaUri(Uri.parse(song.getSong_uri()))
                .setAlbumTitle(song.getAlbum())
                .setArtworkUri(Uri.parse(song.getArtwork_uri()))
                .build();
    }

//    @Override
//    protected void onPostResume() {
//        super.onPostResume();
//        if(player.isPlaying() || isPlayingSong == 2){
//            player.stop();
//            player.release();
//        }
//
//    }
    public static void stopMusic(){
        player.stop();
        player.release();
    }
    @Override
    protected void onRestart() {
        super.onRestart();

    }
    @Override
    protected void onDestroy() {
//        Toast.makeText(this, "Number "+isPlayingSong, Toast.LENGTH_SHORT).show();
//        if(player.isPlaying() && isPlayingSong == 2){
//            player.stop();
//            player.release();
//
//        }
        super.onDestroy();

    }


    @Override
    public void onBackPressed() {

        isPlayingSong = 1;

        super.onBackPressed();
        overridePendingTransition(R.anim.slide_down,R.anim.slide_down);

//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//
//        fragmentTransaction.replace(R.id.playerView, new HomeFragment()).commit();
    }

    private void updatePositionProgress() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(player.isPlaying()){
//                    tvTitle.setSelected(true);
//
//                    tvTitle.setText(songItem.getTitle());
//                    tvAuthor.setText(songItem.getAuthor());
//                    loadImage(songItem.getArtwork_uri(), artworkImg);
//
//                    durationTime.setText(convertDuration(songItem.getDuration()));
                    progressView.setText(convertDuration(player.getCurrentPosition()));
                    seekBar.setProgress((int) player.getCurrentPosition());
//                    seekBar.setMax((int) player.getDuration());


                }
                updatePositionProgress();
            }
        },0);
    }

    private void playerControls() {

        playerListener();
    }

    private void playerListener() {
        SongAdapter songAdapter = new SongAdapter();
        tvTitle.setSelected(true);
        tvTitle.setText(songItem.getTitle());
        tvAuthor.setText(songItem.getAuthor());
        loadImage(songItem.getArtwork_uri(), artworkImg);
        durationTime.setText(songAdapter.convertDuration(songItem.getDuration()));




    }

    private void loadImage(String url, ImageView imageView) {
        Picasso.get().load(url).into(imageView);

    }

    public String convertDuration(long duration){
        String durationText;

        int hrs = (int) duration / (1000*60*60) ;
        int min = (int) (duration % (1000*60*60)) / (1000*60);
        int sec = (int) ((duration % (1000*60*60)) % (1000*60) / 1000);
//        int sec = (int) (((duration % (1000*60*60)) % (1000*60*60)) % (1000*600)) / 1000;

        if(hrs <1){
            durationText = String.format("%02d:%02d", min, sec);
        }else{
            durationText = String.format("%1d:%02d:%02d", hrs, min, sec);

        }
        return durationText;

    }

    private void updatePlayerBackgound(){
        BitmapDrawable bitmapDrawable = (BitmapDrawable) artworkImg.getDrawable();
        if(bitmapDrawable == null){
            bitmapDrawable = (BitmapDrawable) ContextCompat.getDrawable(this, R.drawable.color_home_bg);
        }

        assert bitmapDrawable != null;
        Bitmap bm = bitmapDrawable.getBitmap();

        blurImageView.setImageBitmap(bm);
        blurImageView.setBlur(8);

        //player control colors
        Palette.from(bm).generate(palette -> {
            if(palette != null){
                Palette.Swatch swatch = palette.getDarkVibrantSwatch();
                if(swatch == null){
                    swatch = palette.getMutedSwatch();
                    if(swatch == null){
                        swatch = palette.getDominantSwatch();
                    }
                }

                //extract text-color
                assert swatch != null;
                int titleTextColor = swatch.getTitleTextColor();
                int bodyTextColor = swatch.getBodyTextColor();
                int rgbColor = swatch.getRgb();

//                getWindow().setStatusBarColor(rgbColor);
//                getWindow().setNavigationBarColor(rgbColor);

                //more color view
                tvTitle.setTextColor(titleTextColor);
                playerTitle.setTextColor(titleTextColor);
                hideSongBtn.getCompoundDrawables()[0].setTint(titleTextColor);
                progressView.setTextColor(bodyTextColor);
                durationTime.setTextColor(bodyTextColor);

                repeatBtn.getCompoundDrawables()[0].setTint(bodyTextColor);
                skipPrevBtn.getCompoundDrawables()[0].setTint(bodyTextColor);
                skipNextBtn.getCompoundDrawables()[0].setTint(bodyTextColor);
                playPauseBtn.getCompoundDrawables()[0].setTint(titleTextColor);
                shuffleBtn.getCompoundDrawables()[0].setTint(titleTextColor);


            }
        });
    }

    public void init() {
        tvTitle = findViewById(R.id.Title);
        tvAuthor = findViewById(R.id.authorTitle);
        durationTime = findViewById(R.id.durationView);
        artworkImg = findViewById(R.id.artWorkView);
        progressView = findViewById(R.id.progressView);
        seekBar = findViewById(R.id.seekBar);
        blurImageView = findViewById(R.id.blurImageView);
        
        playPauseBtn = findViewById(R.id.playPauseButton);
        skipNextBtn = findViewById(R.id.skipNextButton);
        skipPrevBtn = findViewById(R.id.skipPrevButton);
        repeatBtn = findViewById(R.id.repeatButton);
        shuffleBtn = findViewById(R.id.shuffleButton);

        hideSongBtn = findViewById(R.id.playerDropdown);
        playerView = findViewById(R.id.playerView);

        playerTitle = findViewById(R.id.playerTitle);


    }
}