package com.seven.easybannerdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.seven.easybanner.EasyBanner;
import com.seven.easybanner.adapter.ImageBannerAdapter;
import com.seven.easybanner.model.DataModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ImageBannerAdapter.IImageLoader, EasyBanner.OnBannerItemClickListener {

    private EasyBanner mBanner;
    private Button mBtnPause;
    private Button mBtnStart;
    private Button mBtnReverse;

    private List<String> mItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnPause = findViewById(R.id.btn_pause);
        mBtnStart = findViewById(R.id.btn_stop);
        mBtnReverse = findViewById(R.id.btn_reverse);

        mBtnPause.setOnClickListener(this);
        mBtnStart.setOnClickListener(this);
        mBtnReverse.setOnClickListener(this);

        initData();
        mBanner = findViewById(R.id.easy_banner);
        mBanner.setAdapter(new ImageBannerAdapter<DataModel>(DataHolder.models, this))
                .setOnBannerItemClickListener(this)
                .start();

        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mItems));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = mItems.get(position);
                Intent intent = null;
                switch (item) {
                    case "IndicatorStyle":
                        intent = new Intent(MainActivity.this, IndicatorActivity.class);
                        break;
                    case "Transformer":
                        intent = new Intent(MainActivity.this, TransformerActivity.class);
                        break;
                    case "Custom":
                        intent = new Intent(MainActivity.this, CustomActivity.class);
                        break;
                    default:
                        break;
                }

                if (null != intent) {
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, item + " Developing!", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pause:
                mBanner.pause();
                mBtnPause.setEnabled(false);
                mBtnStart.setText("Start");
                break;
            case R.id.btn_stop:
                int status = mBanner.getStatus();
                if (status == EasyBanner.STATUS_PAUSED || status == EasyBanner.STATUS_STOPPED) {
                    mBanner.start();
                    mBtnStart.setText("Stop");
                    mBtnPause.setEnabled(true);
                } else {
                    mBanner.stop();
                    mBtnStart.setText("Start");
                    mBtnPause.setEnabled(false);
                }
                break;
            case R.id.btn_reverse:
                mBanner.setDirection(mBanner.getDirection() == EasyBanner.DIRECTION_POSITIVE ? EasyBanner.DIRECTION_NEGATIVE : EasyBanner.DIRECTION_POSITIVE);
                break;
            default:
                break;
        }
    }

    @Override
    public void load(@NonNull ImageView imageView, int position, @NonNull DataModel model) {
        Glide.with(imageView.getContext())
                .load(model.getUrl())
                .into(imageView);
    }

    private void initData() {
        DataHolder.models.add(new DataModel("0", getString(R.string.url1)));
        DataHolder.models.add(new DataModel("1", getString(R.string.url2)));
        DataHolder.models.add(new DataModel("2", getString(R.string.url3)));
        DataHolder.models.add(new DataModel("3", getString(R.string.url4)));
        DataHolder.models.add(new DataModel("4", getString(R.string.url5)));

        mItems = new ArrayList<>();
        mItems.add("IndicatorStyle");
        mItems.add("Transformer");
        mItems.add("Custom");
    }

    @Override
    public void onBannerClicked(@NonNull View view, int position, @NonNull DataModel model) {
        Toast.makeText(this, model.getDescription() + " position: " + position, Toast.LENGTH_SHORT).show();
    }

    private class MyTransformer implements ViewPager.PageTransformer {

        @Override
        public void transformPage(@NonNull View page, float position) {
            final float scale = position < 0 ? position + 1f : Math.abs(1f - position);
            page.setScaleX(scale);
            page.setScaleY(scale);
            page.setPivotX(page.getWidth() * 0.5f);
            page.setPivotY(page.getHeight() * 0.5f);
            page.setAlpha(position < -1f || position > 1f ? 0f : 1f - (scale - 1f));
        }
    }
}
