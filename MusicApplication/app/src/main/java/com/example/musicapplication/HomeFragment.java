package com.example.musicapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chibde.visualizer.BarVisualizer;
import com.example.musicapplication.Adapter.SongAdapter;
import com.example.musicapplication.Model.SongItem;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeFragment extends Fragment implements PopupMenu.OnMenuItemClickListener, SongAdapter.songOnClick, SwipeRefreshLayout.OnRefreshListener{

    RecyclerView recyclerView;
    ShimmerFrameLayout shimmerFrameLayout;
    ImageView editSong, testbtn, homeSongArtWorkView;
    TextView title, authorName, duraTime;
    Context context;
    ConstraintLayout clLayoutSong;
    SongAdapter songAdapter;
    List<SongItem> allsongs = new ArrayList<>();
    ActivityResultLauncher<String> storagePermission; // find songs in phone
    ExoPlayer exoPlayer;
    SwipeRefreshLayout swipeRefreshLayout;
    NestedScrollView nestedScrollView;

    ActivityResultLauncher<String> recordAudio; // find songs in phone
    final String recordAudioPermission = Manifest.permission.RECORD_AUDIO;

    //controls
    TextView changetext, songNameView, skipPrevBtn, skipNextBtn, playPauseBtn, repeatModeBtn, playlistBtn;
    TextView homeSongNameView, homeSongAuthorView, homeSkipPrevBtn, homePlayPauseBtn, homeSkipNextBtn, homeCloseBtn;
    //wrapper
    ConstraintLayout homeControlView, headWrapper, testingView,
            artworkWrapper, seekbarWrapper, controlWrapper, audioVisualizerWrapper;
    //artwork
    CircleImageView artworkView;
    //seek bar
    SeekBar seekBar;
    TextView progressView, durationView;
    SharedPreferences sharedPreferences;
    //audio visualizer
    BarVisualizer audioVisualizer;
    int defaultStatusColor;

    //test
    FirebaseDatabase database;
    DatabaseReference reference;
    List<SongItem> songholder = new ArrayList<>();


    @Override
    public void onStart() {

        super.onStart();
    }

    @Override
    public void onResume() {
        int checkRunning = SongDetail.isPlayingSong;
        Toast.makeText(context, "Is playing " + String.valueOf(checkRunning), Toast.LENGTH_SHORT).show();
        homeControlView.setVisibility(View.GONE);
        if(checkRunning == 1){

            //get shared preference
            sharedPreferences = context.getSharedPreferences("curr_player_song", Context.MODE_PRIVATE);
            String songName = sharedPreferences.getString("CurrTitle", "");
            String songAuthor = sharedPreferences.getString("CurrAuthor", "");
            String songArtwork = sharedPreferences.getString("CurrArtwork", "");
            String songDuration = sharedPreferences.getString("CurrDura", "");
            Toast.makeText(context, "Song curr "+ songName, Toast.LENGTH_SHORT).show();
            homeSongNameView.setText(trimSongName(songName));
            homeSongAuthorView.setText(trimSongAuthor(songAuthor));
            loadImage(songArtwork, homeSongArtWorkView);
            homeControlView.setVisibility(View.VISIBLE);

        }
        else if(checkRunning == 0){
            homeControlView.setVisibility(View.GONE);
        }
        super.onResume();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        init(view);

        super.onViewCreated(view, savedInstanceState);

        try {
            fetchSong();

            //delete //problems
//            exoPlayer = new ExoPlayer.Builder(getActivity()).build();

        } catch (Exception e) {
            e.printStackTrace();
        }

        //test homeControlWrapper
        testbtn = view.findViewById(R.id.testButt);
        testbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int checkRunning = SongDetail.isPlayingSong;
                if(checkRunning == 1){

                    startActivity(new Intent(getActivity(),SongDetail.class));
                    SongDetail.isPlayingSong = 0;
                }
                Toast.makeText(context, "Test Button", Toast.LENGTH_SHORT).show();
            }
        });

        homeControlView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SongItem songItem = (SongItem) SongDetail.songItem;
                Log.e("SONG ITEM ",convertDuration(SongDetail.player.getCurrentPosition()));
//                Intent data = new Intent(getActivity(), SongDetail.class);
//                data.putExtra("songdata", songItem);
//                startActivity(data);


//                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(HomeFragment.this, new SongDetail()).commit();
            }
        });

        //song modifier
        homePlayPauseBtn.setOnClickListener(view1 -> playPause());
        homeSkipPrevBtn.setOnClickListener(view1 -> skipPrevious());
        homeSkipNextBtn.setOnClickListener(view1 -> skipNext());
        homeCloseBtn.setOnClickListener(view1 -> closeBtn());


        swipeRefreshLayout.setOnRefreshListener(this);


        CircleImageView circleImageView = view.findViewById(R.id.profileImage);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });
    }

    private void closeBtn() {
        SongDetail.isPlayingSong = 0;
        homeControlView.setVisibility(View.GONE);
        SongDetail.player.stop();
        SongDetail.player.release();
    }

    private void skipNext() {
        SongDetail.skipNextSong();
    }

    private void skipPrevious() {
        SongDetail.skipPrevSong();

    }

    private void playPause() {
        ExoPlayer player = SongDetail.player;
        if(player.isPlaying()){
            homePlayPauseBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_play_circle_outline_24,0,0,0);
        }else{
            homePlayPauseBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_pause_circle_outline_home,0,0,0);
        }
        SongDetail.playOrPause();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = container.getContext();

        return inflater.inflate(R.layout.fragment_home, container, false);


    }

    //test
    public void renderDBSong(){
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("default_songs").child("songs");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSong : snapshot.getChildren()) {
                    Log.e("Value child ",childSong.getValue(SongItem.class).toString());
                    SongItem song = childSong.getValue(SongItem.class);
                    songholder.add(song);
                }
                SongAdapter songAdapter1 =new SongAdapter(context,songholder, HomeFragment.this,exoPlayer);
                recyclerView.setAdapter(songAdapter1);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void fetchSong(){
        try {
            //Read each file songs
//            renderDBSong();
//            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("default_songs");
//            Query checkUserDB = databaseReference.child("songs").orderByChild("title").equalTo(title.getText().toString());

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    getSongData();

                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            shimmerFrameLayout.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            shimmerFrameLayout.stopShimmer();


                            recyclerView.setLayoutManager(new LinearLayoutManager(context));
                            recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
                        }
                    },5000);


                    //NEW EDIT 2
//                    nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//                        @Override
//                        public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                            if(scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()){
//
//                                getSongData();
//                            }
//                        }
//                    });
                }
            }, 2000);
        } catch (Exception e) {
            Log.e("Exception", String.valueOf(e));
            e.printStackTrace();
        }
    }

    //NEW EDIT 2
    private void getSongData() {
        String urlStoreData = "https://music-app-ad3f9-default-rtdb.firebaseio.com/default_songs/songs.json";

        StringRequest request = new StringRequest(urlStoreData, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("CANNOT ","No response");
                if(response != null){
                    Log.e("FINISH ","finish");
//                    shimmerFrameLayout.setVisibility(View.GONE);
//                    recyclerView.setVisibility(View.VISIBLE);
//                    shimmerFrameLayout.stopShimmer();
//                    shimmerFrameLayout.setVisibility(View.GONE);

                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        parseArray(jsonArray);
                        Log.e("JSON Array: ", response.toString());
                        Log.e("FINISH2 ","finish 2");
                        Log.e("FINISH2 ",jsonArray.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

//                    parseArray(jsonArray);
//                    try {
//
//                    } catch (JSONException exception){
//                        exception.printStackTrace();
//                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }

    private void parseArray(JSONArray jsonArray) {

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject object = jsonArray.getJSONObject(i);
                Iterator keys = object.keys();

                while(keys.hasNext()) {
                    // loop to get the dynamic key
                    String currentDynamicKey = (String)keys.next();

                    // get the value of the dynamic key // get id song
                    JSONObject obj = object.getJSONObject(currentDynamicKey);

                    Log.e("JSON Object: ",obj.toString());

                    SongItem songItem = new SongItem();
                    songItem.setAlbum(obj.getString("album"));
                    songItem.setAlbum_code(obj.getInt("album_code"));
                    songItem.setArtwork_uri(obj.getString("artwork_uri"));
                    songItem.setAuthor(obj.getString("author"));
                    songItem.setDuration(obj.getInt("duration"));
                    songItem.setId(obj.getString("id"));
                    songItem.setNote(obj.getString("note"));
                    songItem.setSize(obj.getInt("size"));
                    songItem.setSong_uri(obj.getString("song_uri"));
                    songItem.setStatus(obj.getInt("status"));
                    songItem.setTitle(obj.getString("title"));

                    songholder.add(songItem);
                }
//                JSONObject obj = js.getJSONObject(i);


            } catch (JSONException e){
                e.printStackTrace();
            }

        }
        SongAdapter songAdapter1 =new SongAdapter(context,songholder, HomeFragment.this,exoPlayer);
        recyclerView.setAdapter(songAdapter1);
    }

    private void loadImage(String url, ImageView imageView) {
        Picasso.get().load(url).into(imageView);

    }

    public void addDefaultSong(){
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("default_songs");




//        SongItem song1 = new SongItem(
//                "Stay",
//                "Justin Bieber",
//                "https://firebasestorage.googleapis.com/v0/b/music-app-71267.appspot.com/o/default%20songs%2Fy2mate.com%20-%20The%20Kid%20LAROI%20Justin%20Bieber%20%20Stay%20Lyrics.mp3?alt=media&token=817235dc-fb01-42e7-b334-f07ddd60a2af",
//                "https://i1.sndcdn.com/artworks-D5hmCb17GnqTy7uV-SSoZtw-t500x500.jpg",
//                1000,
//                1000,
//                1,
//                "",
//                0,
//                ""
//        );
//
//        SongItem song2 = new SongItem(
//                "Made You Look",
//                "Megha Trainor",
//                "https://firebasestorage.googleapis.com/v0/b/music-app-71267.appspot.com/o/default%20songs%2Fy2mate.com%20-%20Meghan%20Trainor%20%20Made%20You%20Look%20Lyrics.mp3?alt=media&token=85cf7f6c-03f9-42b1-b93c-68341b060bc0",
//                "https://ih1.redbubble.net/image.4377649425.0048/poster,504x498,f8f8f8-pad,600x600,f8f8f8.u1.jpg",                1000,
//                1000,
//                1,
//                "",
//                0,
//                ""
//        );
//
//        SongItem song3 = new SongItem(
//                "Surrender",
//                "Natalie Taylor",
//                "https://firebasestorage.googleapis.com/v0/b/music-app-71267.appspot.com/o/default%20songs%2Fy2mate.com%20-%20Natalie%20Taylor%20%20Surrender%20Lyrics.mp3?alt=media&token=cdd024eb-4c28-4743-9952-455174b6738e",
//                "https://cdn.ontourmedia.io/u2/non_secure/images/20220509/surrender_cover1652120108/large.jpg",
//                1000,
//                1000,
//                1,
//                "",
//                0,
//                ""
//        );
//        songholder.add(song1);
//        songholder.add(song2);
//        songholder.add(song3);
//
//        SongAdapter songAdapter1 =new SongAdapter(context,songholder);
//        recyclerView.setAdapter(songAdapter1);
//
//        //add database
//        String gen1 = generateSongID(song1.getTitle(),song1.getAlbum());//Stay
//        String gen2 = generateSongID(song2.getTitle(),song2.getAlbum());//Made you look
//        String gen3 = generateSongID(song3.getTitle(),song3.getAlbum());//Surrender
//
//        JsonObject innerObject = new JsonObject();
//        innerObject.addProperty("title", "Surrender");
//        innerObject.addProperty("author", "Natalie Taylor");
//        innerObject.addProperty("song url", "https://firebasestorage.googleapis.com/v0/b/music-app-71267.appspot.com/o/default%20songs%2Fy2mate.com%20-%20Natalie%20Taylor%20%20Surrender%20Lyrics.mp3?alt=media&token=cdd024eb-4c28-4743-9952-455174b6738e");
//        innerObject.addProperty("artwork img", "https://cdn.ontourmedia.io/u2/non_secure/images/20220509/surrender_cover1652120108/large.jpg");
//        innerObject.addProperty("size", 1000);
//        innerObject.addProperty("duration", 1000);
//        innerObject.addProperty("status", 1);
//        innerObject.addProperty("album", "");
//        innerObject.addProperty("album code", 0);
//        innerObject.addProperty("note", "");
//
//        JsonObject innerObject1 = new JsonObject();
//        innerObject1.addProperty("title", "Stay");
//        innerObject1.addProperty("author", "Justin Bieber");
//        innerObject1.addProperty("song url", "https://firebasestorage.googleapis.com/v0/b/music-app-71267.appspot.com/o/default%20songs%2Fy2mate.com%20-%20The%20Kid%20LAROI%20Justin%20Bieber%20%20Stay%20Lyrics.mp3?alt=media&token=817235dc-fb01-42e7-b334-f07ddd60a2af");
//        innerObject1.addProperty("artwork img", "https://i1.sndcdn.com/artworks-D5hmCb17GnqTy7uV-SSoZtw-t500x500.jpg");
//        innerObject1.addProperty("size", 1000);
//        innerObject1.addProperty("duration", 1000);
//        innerObject1.addProperty("status", 1);
//        innerObject1.addProperty("album", "");
//        innerObject1.addProperty("album code", 0);
//        innerObject1.addProperty("note", "");
//
//        JsonObject innerObject2 = new JsonObject();
//        innerObject2.addProperty("title", "Made You Look");
//        innerObject2.addProperty("author", "Megha Trainor");
//        innerObject2.addProperty("song url", "https://firebasestorage.googleapis.com/v0/b/music-app-71267.appspot.com/o/default%20songs%2Fy2mate.com%20-%20Meghan%20Trainor%20%20Made%20You%20Look%20Lyrics.mp3?alt=media&token=85cf7f6c-03f9-42b1-b93c-68341b060bc0");
//        innerObject2.addProperty("artwork img", "https://ih1.redbubble.net/image.4377649425.0048/poster,504x498,f8f8f8-pad,600x600,f8f8f8.u1.jpg");
//        innerObject2.addProperty("size", 1000);
//        innerObject2.addProperty("duration", 1000);
//        innerObject2.addProperty("status", 1);
//        innerObject2.addProperty("album", "");
//        innerObject2.addProperty("album code", 0);
//        innerObject2.addProperty("note", "");
//
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.add(gen3, innerObject);
//        jsonObject.add(gen1, innerObject1);
//        jsonObject.add(gen2, innerObject2);
//
//
//        Gson gson = new Gson();
//        String json = gson.toJson(song1);
//        Map<String, Object> jsonMap = new Gson().fromJson(gson.toJson(jsonObject), new TypeToken<HashMap<String, Object>>() {}.getType());
//        reference.child("songs").setValue(jsonMap);
//        reference.child(gen2).setValue(song2);
//        reference.child(gen3).setValue(song3);

//        reference.child("test").setValue("97889789789789");

        Toast.makeText(getActivity(),"You have create default songs!",Toast.LENGTH_SHORT).show();
    }

    public String generateSongID(String title, String album){
        if(album.isEmpty() || album == ""){
            String spec_default = "";
            String sb = randomString_6();
            if(title.isEmpty() || title == "") {
                spec_default = "SPDFN000";
                return spec_default+sb.toString();
            }
                //album is empty => code "SPDF"
//            String head = album.toUpperCase().substring(0,2);
            String name = title.substring(0,4).toUpperCase().replace(' ','X');
            String mixname = "SPDF"+name;
            String new_generateID = mixname + sb.toString();
            return new_generateID;
        }else{
            String sb = randomString_6();
            String head = album.toUpperCase().substring(0,2);
            String name = title.substring(0,4).toUpperCase().replace(' ','X');
            String mixname = "SP"+head+name;
            String new_generateID = mixname + sb.toString();
            return new_generateID;
        }

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

    public String randomString_6(){
        String codeID = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789";
        StringBuilder sb = new StringBuilder(6);

        for (int i = 0; i < 4; i++) {
            int index = (int)(codeID.length() * Math.random());

            sb.append(codeID.charAt(index));
        }
        return sb.toString();
    }


    public void init(View view) {

        recyclerView = view.findViewById(R.id.recycler_item);
        shimmerFrameLayout = view.findViewById(R.id.shimmerLayoutHorizontal);
        shimmerFrameLayout.startShimmer();

        clLayoutSong = view.findViewById(R.id.clItem);
        editSong = view.findViewById(R.id.editSong);
        title = view.findViewById(R.id.titleSong);
        authorName = view.findViewById(R.id.authorName);
        editSong = view.findViewById(R.id.editSong);
        duraTime = view.findViewById(R.id.duration);
        progressView = (TextView) view.findViewById(R.id.progressView);
        changetext = view.findViewById(R.id.change_text);
        testingView = view.findViewById(R.id.testingView);

        homeControlView = view.findViewById(R.id.homeControlWrapper);
        homeSongNameView = view.findViewById(R.id.homeSongName);
        homeSongAuthorView = view.findViewById(R.id.homeSongAuthor);
        homeSongArtWorkView = view.findViewById(R.id.artworkImg);

        swipeRefreshLayout = view.findViewById(R.id.swiperRefresh);
        nestedScrollView = view.findViewById(R.id.drawerLayout);

        homePlayPauseBtn = view.findViewById(R.id.homePlayPauseBtn);
        homeSkipPrevBtn = view.findViewById(R.id.homeSkipPrevBtn);
        homeSkipNextBtn = view.findViewById(R.id.homeSkipNextBtn);
        homeCloseBtn = view.findViewById(R.id.homeCloseBtn);

    }

    public void showPopup(View v){
        PopupMenu popupMenu = new PopupMenu(getActivity(), v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_profile_icon);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.item1:
                Toast.makeText(this.getActivity(), "Item 1 clicked", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item2:
                Toast.makeText(this.getActivity(), "Item 2 clicked", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item3:
                Toast.makeText(this.getActivity(), "Item 3 clicked", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }

    public static String trimSongName(String str){
        String temp = str;
        if(str.length() > 18){
            temp = str.substring(0,14) + "...";
        }
        return temp;
    }

    public static String trimSongAuthor(String str){
        String temp = str;
        if(str.length() > 20){
            temp = str.substring(0,16) + "...";
        }
        return temp;
    }
    @Override
    public void songOnClick(SongItem songItem) {
        Intent data = new Intent(context, SongDetail.class);
        data.putExtra("songdata", songItem);
//        data.putExtra("detail_desc",allDescription);

        startActivity(data);


    }

    @Override
    public void onRefresh() {
        try {
            shimmerFrameLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
//            renderDBSong();

        } catch (Exception e) {
            e.printStackTrace();
        }
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);

//                recyclerView.setLayoutManager(new LinearLayoutManager(context));
//                recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));



            }
        }, 2000);

    }
}