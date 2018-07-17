package com.seven.easybannerdemo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.seven.easybanner.EasyBanner;
import com.seven.easybanner.adapter.ImageBannerAdapter;
import com.seven.easybanner.model.DataModel;

import java.util.ArrayList;
import java.util.List;

public class TransformerActivity extends AppCompatActivity implements ImageBannerAdapter.IImageLoader {

    private List<DataModel> mModels;
    private List<String> mItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transformer);
        initData();

        final EasyBanner mBanner = findViewById(R.id.easy_banner);
        mBanner.setAdapter(new ImageBannerAdapter<DataModel>(mModels, this))
                .start();

        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mItems));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String transStr = mItems.get(position);
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

                mBanner.setPageTransformer(trans);
            }
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

        mItems = new ArrayList<>();
        mItems.add("Default");
        mItems.add("Accordion");
        mItems.add("BackgroundToForeground");
        mItems.add("ForegroundToBackground");
        mItems.add("CubeIn");
        mItems.add("CubeOut");
        mItems.add("DepthPage");
        mItems.add("FlipHorizontal");
        mItems.add("FlipVertical");
        mItems.add("RotateDown");
        mItems.add("RotateUp");
        mItems.add("ScaleInOut");
        mItems.add("Stack");
        mItems.add("Tablet");
        mItems.add("ZoomIn");
        mItems.add("ZoomOut");
        mItems.add("ZoomOutSlide");
    }

    @Override
    public void load(@NonNull ImageView imageView, int position, @NonNull DataModel model) {
        Glide.with(this)
                .load(model.getUrl())
                .into(imageView);
    }
}
