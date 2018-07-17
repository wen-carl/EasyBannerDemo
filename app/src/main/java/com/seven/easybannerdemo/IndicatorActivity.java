package com.seven.easybannerdemo;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.seven.easybanner.EasyBanner;
import com.seven.easybanner.adapter.ImageBannerAdapter;
import com.seven.easybanner.model.DataModel;

import java.util.ArrayList;
import java.util.List;

public class IndicatorActivity extends AppCompatActivity implements ImageBannerAdapter.IImageLoader {

    private List<DataModel> mModels;
    private List<String> mModes;
    private List<String> mGravities;
    private List<String> mStyles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indicator);
        initData();

        final EasyBanner easyBanner = findViewById(R.id.easy_banner);
        easyBanner.setAdapter(new ImageBannerAdapter<DataModel>(mModels, this))
                .start();

        Spinner spinner_mode = findViewById(R.id.spinner_mode);
        spinner_mode.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mModes));
        spinner_mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = mModes.get(position);
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
        spinner_gravity.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mGravities));
        spinner_gravity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = mGravities.get(position);
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
        spinner_style.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mStyles));
        spinner_style.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = mStyles.get(position);
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

    private void initData() {
        mModels = new ArrayList<>();
        mModels.add(new DataModel("0", "http://ww4.sinaimg" +
                ".cn/large/006uZZy8jw1faic1xjab4j30ci08cjrv.jpg"));
        mModels.add(new DataModel("1", "http://ww4.sinaimg.cn/large/006uZZy8jw1faic21363tj30ci08ct96.jpg"));
        mModels.add(new DataModel("2", "http://ww4.sinaimg.cn/large/006uZZy8jw1faic259ohaj30ci08c74r.jpg"));
        mModels.add(new DataModel("3", "http://ww4.sinaimg.cn/large/006uZZy8jw1faic2b16zuj30ci08cwf4.jpg"));
        mModels.add(new DataModel("4", "http://ww4.sinaimg.cn/large/006uZZy8jw1faic2e7vsaj30ci08cglz.jpg"));

        mModes = new ArrayList<>();
        mModes.add("outside");
        mModes.add("inside");

        mGravities = new ArrayList<>();
        mGravities.add("start");
        mGravities.add("center");
        mGravities.add("end");

        mStyles = new ArrayList<>();
        mStyles.add("STYLE_IMAGE_INDICATOR");
        mStyles.add("STYLE_NUM_INDICATOR");
        mStyles.add("STYLE_TITLE");
        mStyles.add("STYLE_TITLE_WITH_IMAGE_INDICATOR");
        mStyles.add("STYLE_TITLE_WITH_NUM_INDICATOR");
        mStyles.add("STYLE_NONE");
    }

    @Override
    public void load(@NonNull ImageView imageView, int position, @NonNull DataModel model) {
        Glide.with(this)
                .load(model.getUrl())
                .into(imageView);
    }
}
