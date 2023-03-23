package com.example.musicapplication.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicapplication.Model.SongItem;
import com.example.musicapplication.R;
import com.example.musicapplication.SongDetail;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaMetadata;
import com.google.android.exoplayer2.Player;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {
    //Items
    private Context context;
    private List<SongItem> songs;
    private songOnClick selectSong;
    private ExoPlayer player;
    SharedPreferences sharedPreferences;
    DatabaseReference storageReference;

    //Constructor
    public SongAdapter(@NonNull Context context, @NonNull List<SongItem> songs, songOnClick selectSong, ExoPlayer player) {
        this.context = context;
        this.songs = songs;
        this.selectSong = selectSong;
        this.player = player;
    }

    public SongAdapter() {
    }

    @NonNull
    @Override
    public SongAdapter.SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.single_song_item,parent,false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        SongItem songItem = songs.get(position);
        SongViewHolder viewHolder = (SongViewHolder) holder;
        SongDetail songDetail;

        //NEW EDIT 2
        Shimmer shimmer = new Shimmer.ColorHighlightBuilder()
                .setBaseColor(Color.parseColor("#f3f3f3"))
                .setBaseAlpha(1)
                .setHighlightColor(Color.parseColor("#E7E7E7"))
                .setHighlightAlpha(1)
                .setDropoff(50)
                .build();

        ShimmerDrawable shimmerDrawable = new ShimmerDrawable();
        shimmerDrawable.setShimmer(shimmer);

        //Set image on image view
        Glide.with(context).load(songItem.getArtwork_uri())
                .placeholder(shimmerDrawable)
                .into(holder.artSong);

        //get duration
        MediaPlayer mp = new MediaPlayer();
        try {
//            if (mp.isPlaying() || SongDetail.isPlayingSong == 1) {
//                mp.stop();
//                mp.reset();
//                mp.release();
//                Toast.makeText(context, "MP stopped", Toast.LENGTH_SHORT).show();
//            }
            Log.e("Song Uri ",songItem.getSong_uri());
            mp.reset();
            mp.setDataSource(songItem.getSong_uri());
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        player.addListener(new Player.Listener() {
//            @Override
//            public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
//                Player.Listener.super.onMediaItemTransition(mediaItem, reason);
//
//                assert  mediaItem != null;
//                viewHolder.title.setText(mediaItem.mediaMetadata.title);
//            }
//
//            @Override
//            public void onPlaybackStateChanged(int playbackState) {
//                Player.Listener.super.onPlaybackStateChanged(playbackState);
//            }
//        });
        //load from source
        viewHolder.title.setText(songItem.getTitle());
        viewHolder.author.setText(songItem.getAuthor());
        viewHolder.duration.setText(String.valueOf(convertDuration(mp.getDuration())));
//        Log.e("Duration ",String.valueOf(mp.getDuration()));

        String artWorkSong = songItem.getArtwork_uri();
        Log.e("Check URI ",artWorkSong.toString());
        if(artWorkSong != null){
            ImageView imageView = viewHolder.artSong;
            loadImage(artWorkSong.toString(), imageView);

            if(viewHolder.artSong.getDrawable() == null && artWorkSong == null){
                viewHolder.artSong.setImageResource(R.drawable.default_artwork);
            }
        }
        int temp = position;
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(SongDetail.isPlayingSong == 1){
                        SongDetail.isPlayingSong =2;
                        SongDetail.stopMusic();
                    }
                    Toast.makeText(context, "Playing " + viewHolder.title.getText() +"...", Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, "Test Num " + SongDetail.isPlayingSong, Toast.LENGTH_SHORT).show();
                    selectSong.songOnClick(songs.get(temp));
                    //store in sharedReference
                    sharedPreferences = context.getSharedPreferences("player_song", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("position", temp);
                    try {
                        editor.putString("list", ObjectSerializer.serialize((Serializable) songs));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    editor.commit();

//                    if(!player.isPlaying()){
//                        player.setMediaItems(getMediaItems(), temp, 0);
//                    }else{
//                        player.pause();
//                        player.seekTo(temp,0);
//                    }
//                    player.prepare();
//                    player.play();

//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//
//                        }
//                    },20000);







                }


            });
    }

    private List<MediaItem> getMediaItems() {
        List<MediaItem> mediaItems = new ArrayList<>();
        for(SongItem song: songs){
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

    public interface songOnClick{
        void songOnClick(SongItem songItem);
    }


    private void loadImage(String url, ImageView imageView) {
        Picasso.get().load(url).into(imageView);

    }
    //ViewHolder
    public class SongViewHolder extends RecyclerView.ViewHolder{

        ImageView artSong;
        TextView title, author, duration, size;
        public SongViewHolder(View itemView) {
            super(itemView);

            artSong = itemView.findViewById(R.id.imgSong);
            title = itemView.findViewById(R.id.titleSong);
            author = itemView.findViewById(R.id.authorName);
            duration = itemView.findViewById(R.id.duration);
            TextView progressView = itemView.findViewById(R.id.progressView);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    selectSong.songOnClick(songs.get(getAdapterPosition()));
//                    Toast.makeText(context, "Playing " + title.getText() +"...", Toast.LENGTH_SHORT).show();
//
//                    if(!player.isPlaying()){
//                        player.setMediaItems(getMediaItems(), getAdapterPosition(), 0);
//                    }else{
//                        player.pause();
//                        player.seekTo(getAdapterPosition(),0);
//                    }
//                    player.prepare();
//                    player.play();
//
//
//                }
//
//
//            });


        }
    }



    @Override
    public int getItemCount() {
        return songs.size();
    }

    //filter song
    @SuppressLint("NotifyDataSetChanged")
    public void filterSongs(List<SongItem> filterList){
        songs = filterList;
        notifyDataSetChanged();
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

    private void renderInfoSong(SongViewHolder songViewHolder, int posi){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("default_songs").child("songs");
        Query checkUserDB = databaseReference.child("songs");

        ValueEventListener postListener = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Log.e("Available ","value");
                    int count = 0;
                    for (DataSnapshot childSong : snapshot.getChildren()) {
                        Log.e("Available2 ",childSong.getValue().toString());

                        songViewHolder.title.setText(childSong.child("title").getValue(String.class));
                        songViewHolder.author.setText(childSong.child("author").getValue(String.class));
                        songViewHolder.duration.setText(childSong.child("duration").getValue(Integer.class).toString());
                        Picasso.get().load(childSong.child("artwork img").getValue(String.class)).into(songViewHolder.artSong);
                        count++;
                        if(count == posi){
                            break;
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        databaseReference.addValueEventListener(postListener);
//        checkUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    Log.e("Available ","value");
//                    for (DataSnapshot childSong : snapshot.getChildren()) {
//                        Log.e("Available2 ","value2");
//                        songViewHolder.title.setText(childSong.child("title").getValue(String.class));
//                        songViewHolder.author.setText(childSong.child("author").getValue(String.class));
//                        songViewHolder.duration.setText(childSong.child("duration").getValue(Integer.class).toString());
//                        Picasso.get().load(childSong.child("artwork img").getValue(String.class)).into(songViewHolder.artSong);
//
//                    }
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }
}
