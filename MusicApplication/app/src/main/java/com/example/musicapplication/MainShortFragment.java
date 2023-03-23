package com.example.musicapplication;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.musicapplication.Adapter.PagerAdapter;
import com.example.musicapplication.Model.DataShortHandler;
import com.example.musicapplication.Other.RandomNumberNoDup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainShortFragment extends Fragment {

    private ArrayList<DataShortHandler> dataShortHandlers = new ArrayList<>();
    ArrayAdapter<String> listAdapter;
    ArrayList<String> videoList;
    ViewPager2 viewPager2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_short, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewPager2 = view.findViewById(R.id.viewPager);
        super.onViewCreated(view, savedInstanceState);

        //Get video source link
        ShortVideoApi videoApi = new ShortVideoApi();
        videoApi.execute();
        Log.e("Run this","Pass");

    }

    public class ShortVideoApi extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder builder = new StringBuilder();
            HttpURLConnection connection = null;
            try {

                URL url = new URL((String) BuildConfig.VIDEO_LINK);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                while (true) {
                    String readLine = reader.readLine();
                    String data = readLine;
                    if (data == null) {
                        break;
                    }
                    builder.append(data);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

            Log.e("GET DETAIL", builder.toString());
            return builder.toString();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String str_json) {

            try {
                JSONObject jsonObject = new JSONObject(str_json);
                JSONArray videoArray = jsonObject.getJSONArray("categories").getJSONObject(0).getJSONArray("videos");
                //Random number video with no duplicate
                RandomNumberNoDup rng = new RandomNumberNoDup(videoArray.length());
                for (int i = 0; i < videoArray.length(); i++) {

                    JSONObject video = videoArray.getJSONObject(rng.next());
                    String sourceVid = (String) video.getJSONArray("sources").get(0);

                    DataShortHandler data_video = new DataShortHandler();
                    data_video.setDescript(cutString(video.getString("description")));
                    data_video.setTag(video.getString("subtitle"));
                    data_video.setAccName(video.getString("title"));
                    data_video.setUrlVideo(sourceVid);

                    dataShortHandlers.add(data_video);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            PagerAdapter pagerAdapter = new PagerAdapter(dataShortHandlers, getActivity());
            viewPager2.setAdapter(pagerAdapter);

        }
    }

    public String cutString(String str){
        String name = "";
        if(str.length() >20){
            name = str.substring(0,14)+"...";
        }
        return name;
    }
}