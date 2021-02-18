package com.example.cartooncrawl;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "LOG_TAG";

    public static RecyclerAdapter recyclerAdapter;
    public static ArrayList<PostInfo> infoList = new ArrayList<>();

    private boolean isHitGalleryCrawlMode = false;
    private int pageIndex = 1;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button homeGallModeBtn = (Button) findViewById(R.id.home_gallery_btn);
        Button hitGallModeBtn = (Button) findViewById(R.id.hit_gallery_btn);

        homeGallModeBtn.setOnClickListener(view -> {
            if(isHitGalleryCrawlMode){
                isHitGalleryCrawlMode = false;
                pageIndex = 1;
                truncatePage();
                Crawler.appendPostElements(isHitGalleryCrawlMode, 1);
            }
        });

        hitGallModeBtn.setOnClickListener(view -> {
            if(!isHitGalleryCrawlMode){
                isHitGalleryCrawlMode = true;
                pageIndex = 1;
                truncatePage();
                Crawler.appendPostElements(isHitGalleryCrawlMode, 1);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerAdapter = new RecyclerAdapter(infoList);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView rview, int newState) {
                super.onScrollStateChanged(rview, newState);

                if(!rview.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE){
                    if(isHitGalleryCrawlMode){
                        Crawler.appendPostElements(isHitGalleryCrawlMode, ++pageIndex);
                    }else{
                        Crawler.appendPostElements(isHitGalleryCrawlMode, ++pageIndex);
                    }
                }
            }
        });

        Crawler.appendPostElements(isHitGalleryCrawlMode, 1);
    }

    private void truncatePage(){
        infoList.clear();
        recyclerAdapter.notifyDataSetChanged();
    }
}