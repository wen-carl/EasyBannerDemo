package com.seven.easybannerdemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.seven.easybanner.EasyBanner;
import com.seven.easybanner.adapter.ImageBannerAdapter;
import com.seven.easybanner.model.DataModel;

public class IndicatorActivity extends AppCompatActivity implements ImageBannerAdapter.IImageLoader {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indicator);

        final EasyBanner easyBanner = findViewById(R.id.easy_banner);
        easyBanner.setAdapter(new ImageBannerAdapter<DataModel>(DataHolder.models, this))
                .start();

        Spinner spinner_mode = findViewById(R.id.spinner_mode);
        spinner_mode.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, DataHolder.modes));
        spinner_mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = DataHolder.modes.get(position);
                switch (item) {
                    case "inside":
                        easyBanner.setIndicatorMode(EasyBanner.INDICATOR_MODE_INSIDE);
                        break;
                    case "outside":
                    default:
                        easyBanner.setIndicatorMode(EasyBanner.INDICATOR_MODE_OUTSIDE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        Spinner spinner_gravity = findViewById(R.id.spinner_gravity);
        spinner_gravity.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, DataHolder.gravities));
        spinner_gravity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = DataHolder.gravities.get(position);
                switch (item) {
                    case "start":
                        easyBanner.setIndicatorGravity(EasyBanner.GRAVITY_START);
                        break;
                    case "center":
                        easyBanner.setIndicatorGravity(EasyBanner.GRAVITY_CENTER);
                        break;
                    case "end":
                    default:
                        easyBanner.setIndicatorGravity(EasyBanner.GRAVITY_END);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        Spinner spinner_style = findViewById(R.id.spinner_style);
        spinner_style.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, DataHolder.styles));
        spinner_style.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = DataHolder.styles.get(position);
                switch (item) {
                    case "STYLE_IMAGE_INDICATOR":
                        easyBanner.setIndicatorStyle(EasyBanner.STYLE_IMAGE_INDICATOR);
                        break;
                    case "STYLE_NUM_INDICATOR":
                        easyBanner.setIndicatorStyle(EasyBanner.STYLE_NUM_INDICATOR);
                        break;
                    case "STYLE_TITLE":
                        easyBanner.setIndicatorStyle(EasyBanner.STYLE_TITLE);
                        break;
                    case "STYLE_TITLE_WITH_IMAGE_INDICATOR":
                        easyBanner.setIndicatorStyle(EasyBanner.STYLE_TITLE_WITH_IMAGE_INDICATOR);
                        break;
                    case "STYLE_TITLE_WITH_NUM_INDICATOR":
                        easyBanner.setIndicatorStyle(EasyBanner.STYLE_TITLE_WITH_NUM_INDICATOR);
                        break;
                    case "STYLE_NONE":
                    default:
                        easyBanner.setIndicatorStyle(EasyBanner.STYLE_NONE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    @Override
    public void load(@NonNull ImageView imageView, int position, @NonNull DataModel model) {
        Glide.with(this)
                .load(model.getUrl())
                .into(imageView);
    }
}
