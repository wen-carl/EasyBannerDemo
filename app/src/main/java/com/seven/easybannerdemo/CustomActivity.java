package com.seven.easybannerdemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
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

        EasyBanner customBanner = new EasyBanner(this);
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(0, getResources().getDimensionPixelSize(R.dimen.dp300));
        params.startToStart = R.id.custom_container;
        params.endToEnd = R.id.custom_container;
        params.topToBottom = R.id.easy_banner;
        customBanner.setLayoutParams(params);
        ConstraintLayout layout = findViewById(R.id.custom_container);
        layout.addView(customBanner);
        customBanner.setAdapter(new ImageBannerAdapter<DataModel>(DataHolder.models, this))
                .setImageIndicatorStateBackgroundResource(R.drawable.indicator_image_background)
                .setImageIndicatorSize(getResources().getDimensionPixelSize(R.dimen.dp8), getResources().getDimensionPixelSize(R.dimen.dp4))
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
