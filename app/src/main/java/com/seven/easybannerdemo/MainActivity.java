package com.seven.easybannerdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ImageBannerAdapter.IImageLoader, EasyBanner.OnBannerItemClickListener, ViewPager.OnPageChangeListener {

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
                .setAutoPlay(true)
                .addOnPageChangeListener(this)
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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

    @Override
    public void onPageSelected(int position) { }

    @Override
    public void onPageScrollStateChanged(int state) { }

    private class MyTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        @Override
        public void transformPage(@NonNull View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                //
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                //[0,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));//缩放的值,如果下于0.75就取0.75
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    //[-1,0]
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    //[0,1]
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                //[>1]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }
}
