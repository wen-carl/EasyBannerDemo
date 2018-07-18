package com.seven.easybannerdemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.seven.easybanner.EasyBanner;
import com.seven.easybanner.adapter.ImageBannerAdapter;
import com.seven.easybanner.model.DataModel;

public class CustomActivity extends AppCompatActivity implements ImageBannerAdapter.IImageLoader {

    private EasyBanner mBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);

        mBanner = findViewById(R.id.easy_banner);
        mBanner.setAdapter(new ImageBannerAdapter<DataModel>(DataHolder.models, this))
                .start();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mBanner.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mBanner.pause();
    }

    @Override
    public void load(@NonNull ImageView imageView, int position, @NonNull DataModel model) {
        Glide.with(this)
                .load(model.getUrl())
                .into(imageView);
    }
}
