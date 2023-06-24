package com.son.comicreader;

import  android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

//import com.google.android.gms.common.internal.service.Common;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.son.comicreader.Adapter.MyComicAdapter;
import com.son.comicreader.Adapter.MySliderAdapter;
import com.son.comicreader.Common.Common;
import com.son.comicreader.Interface.IBannerLoadDone;
import com.son.comicreader.Interface.IComicLoadDone;
import com.son.comicreader.Model.Comic;
import com.son.comicreader.Service.PicassoLoadingService;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import ss.com.bannerslider.Slider;

public class MainActivity extends AppCompatActivity implements IBannerLoadDone, IComicLoadDone {

    Slider slider;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recycler_comic;
    TextView txt_comic;
    ImageView btn_filter_search;

    //Database
    DatabaseReference banners;
    DatabaseReference comics;

    //Listener
    IBannerLoadDone bannerListener;
    IComicLoadDone comicListener;

    android.app.AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);     //lưu và phục hồi trạng thái
        setContentView(R.layout.activity_main);

        //Init Database
        banners = FirebaseDatabase.getInstance().getReference("Banners");
        comics = FirebaseDatabase.getInstance().getReference("Comic");

        //Init Listener
        bannerListener = this;
        comicListener = this;

        btn_filter_search = (ImageView)findViewById(R.id.btn_show_filter_search);
        btn_filter_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, FilterSearchActivity.class));
            }
        });

        slider = (Slider) findViewById(R.id.slider);
        Slider.init(new PicassoLoadingService());

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_to_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                loadBanner();
//                loadComic();
//                swipeRefreshLayout.setRefreshing(false);
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        loadBanner();
                        loadComic();
//                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                loadBanner();
                loadComic();

            }
        });

        recycler_comic = (RecyclerView) findViewById(R.id.recycler_comic);
        recycler_comic.setHasFixedSize(true);
        recycler_comic.setLayoutManager(new GridLayoutManager(this, 2));

        txt_comic = (TextView) findViewById(R.id.txt_comic);
    }

    private void loadComic() {
        //show dialog
        alertDialog = new SpotsDialog.Builder().setContext(this)
                .setCancelable(false)
                .setMessage("Please wait...")
                .build();

        if(!swipeRefreshLayout.isRefreshing())
            alertDialog.show();
        else
            swipeRefreshLayout.setRefreshing(false);

        comics.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Comic> comic_load = new ArrayList<>();
                for (DataSnapshot bannerSnapShot:snapshot.getChildren())
                {
//                    Log.e("getChildren", String.valueOf(snapshot.getChildren()));
//                    Log.e("bannersnapshot", String.valueOf(bannerSnapShot));
                    Comic comic = bannerSnapShot.getValue(Comic.class);
                    comic_load.add(comic);
                }

                //Call listener
                comicListener.onComicLoadDoneListener(comic_load);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadBanner() {
        banners.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> bannerList = new ArrayList<>();
//                Log.e("abcd", String.valueOf(snapshot.getChildren()));
                for (DataSnapshot bannerSnapShot:snapshot.getChildren())
                {
//                    Log.e("getChildren", String.valueOf(snapshot.getChildren()));
//                    Log.e("bannersnapshot", String.valueOf(bannerSnapShot));
                    String image = bannerSnapShot.getValue(String.class);
                    bannerList.add(image);
                }

                //Call listener
                bannerListener.onBannerLoadDoneListener(bannerList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBannerLoadDoneListener(List<String> banners) {
        slider.setAdapter(new MySliderAdapter(banners));
    }

    @Override
    public void onComicLoadDoneListener(List<Comic> comicList) {
        Common.comicList = comicList;

        recycler_comic.setAdapter(new MyComicAdapter(getBaseContext(), comicList));

        txt_comic.setText(new StringBuilder("NEW COMIC (")
        .append(comicList.size())
        .append(")"));

        if(!swipeRefreshLayout.isRefreshing())
            alertDialog.dismiss();
    }
}