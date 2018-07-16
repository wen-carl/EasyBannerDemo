package com.seven.easybannerdemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
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
    private Spinner mIndicatorStyle;
    private Spinner mTransformer;

    private List<DataModel> mModels;
    private List<String> mStyles;
    private List<String> mTrans;

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
        mBanner.setAdapter(new ImageBannerAdapter<DataModel>(mModels, this))
                .setOnBannerItemClickListener(this)
                .start();

        mIndicatorStyle = findViewById(R.id.spinner_indicator_style);
        mIndicatorStyle.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mStyles));
        mIndicatorStyle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String styleStr = mStyles.get(position);
                int style;
                switch (styleStr) {
                    case "STYLE_IMAGE_INDICATOR":
                        style = EasyBanner.STYLE_IMAGE_INDICATOR;
                        break;
                    case "STYLE_TITLE_WITH_IMAGE_INDICATOR":
                        style = EasyBanner.STYLE_TITLE_WITH_IMAGE_INDICATOR;
                        break;
                    case "STYLE_NUM_INDICATOR":
                        style = EasyBanner.STYLE_NUM_INDICATOR;
                        break;
                    case "STYLE_TITLE_WITH_NUM_INDICATOR":
                        style = EasyBanner.STYLE_TITLE_WITH_NUM_INDICATOR;
                        break;
                    case "STYLE_TITLE":
                        style = EasyBanner.STYLE_TITLE;
                        break;
                    case "STYLE_NONE":
                    default:
                        style = EasyBanner.STYLE_NONE;
                        break;
                }

                mBanner.setIndicatorStyle(style);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        mTransformer = findViewById(R.id.spinner_transformer);
        mTransformer.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mTrans));
        mTransformer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String transStr = mTrans.get(position);
                Class trans;
                switch (transStr) {
                    case "Accordion":
                        trans = EasyBanner.Transformer.Accordion;
                        break;
                    case "BackgroundToForeground":
                        trans = EasyBanner.Transformer.BackgroundToForeground;
                        break;
                    case "ForegroundToBackground":
                        trans = EasyBanner.Transformer.ForegroundToBackground;
                        break;
                    case "CubeIn":
                        trans = EasyBanner.Transformer.CubeIn;
                        break;
                    case "CubeOut":
                        trans = EasyBanner.Transformer.CubeOut;
                        break;
                    case "DepthPage":
                        trans = EasyBanner.Transformer.DepthPage;
                        break;
                    case "FlipHorizontal":
                        trans = EasyBanner.Transformer.FlipHorizontal;
                        break;
                    case "FlipVertical":
                        trans = EasyBanner.Transformer.FlipVertical;
                        break;
                    case "RotateDown":
                        trans = EasyBanner.Transformer.RotateDown;
                        break;
                    case "RotateUp":
                        trans = EasyBanner.Transformer.RotateUp;
                        break;
                    case "ScaleInOut":
                        trans = EasyBanner.Transformer.ScaleInOut;
                        break;
                    case "Stack":
                        trans = EasyBanner.Transformer.Stack;
                        break;
                    case "Tablet":
                        trans = EasyBanner.Transformer.Tablet;
                        break;
                    case "ZoomIn":
                        trans = EasyBanner.Transformer.ZoomIn;
                        break;
                    case "ZoomOut":
                        trans = EasyBanner.Transformer.ZoomOut;
                        break;
                    case "ZoomOutSlide":
                        trans = EasyBanner.Transformer.ZoomOutSlide;
                        break;
                    case "Default":
                    default:
                        trans = EasyBanner.Transformer.Default;
                        break;
                }

                //mBanner.setPageTransformer(trans);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
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
        mModels = new ArrayList<>();
        mModels.add(new DataModel("0", "http://ww4.sinaimg" +
                ".cn/large/006uZZy8jw1faic1xjab4j30ci08cjrv.jpg"));
        mModels.add(new DataModel("1", "http://ww4.sinaimg.cn/large/006uZZy8jw1faic21363tj30ci08ct96.jpg"));
        mModels.add(new DataModel("2", "http://ww4.sinaimg.cn/large/006uZZy8jw1faic259ohaj30ci08c74r.jpg"));
        mModels.add(new DataModel("3", "http://ww4.sinaimg.cn/large/006uZZy8jw1faic2b16zuj30ci08cwf4.jpg"));
        mModels.add(new DataModel("4", "http://ww4.sinaimg.cn/large/006uZZy8jw1faic2e7vsaj30ci08cglz.jpg"));

        mStyles = new ArrayList<>();
        mStyles.add("STYLE_IMAGE_INDICATOR");
        mStyles.add("STYLE_TITLE_WITH_IMAGE_INDICATOR");
        mStyles.add("STYLE_NUM_INDICATOR");
        mStyles.add("STYLE_TITLE_WITH_NUM_INDICATOR");
        mStyles.add("STYLE_TITLE");
        mStyles.add("STYLE_NONE");

        mTrans = new ArrayList<>();
        mTrans.add("Default");
        mTrans.add("Accordion");
        mTrans.add("BackgroundToForeground");
        mTrans.add("ForegroundToBackground");
        mTrans.add("CubeIn");
        mTrans.add("CubeOut");
        mTrans.add("DepthPage");
        mTrans.add("FlipHorizontal");
        mTrans.add("FlipVertical");
        mTrans.add("RotateDown");
        mTrans.add("RotateUp");
        mTrans.add("ScaleInOut");
        mTrans.add("Stack");
        mTrans.add("Tablet");
        mTrans.add("ZoomIn");
        mTrans.add("ZoomOut");
        mTrans.add("ZoomOutSlide");
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
