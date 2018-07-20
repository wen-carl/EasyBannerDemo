package com.seven.easybannerdemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import com.seven.easybanner.transformer.AccordionTransformer;

public class TransformerActivity extends AppCompatActivity implements ImageBannerAdapter.IImageLoader, EasyBanner.OnBannerItemClickListener {

    private EasyBanner mBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transformer);

        mBanner = findViewById(R.id.easy_banner);
        mBanner.setAdapter(new ImageBannerAdapter<DataModel>(DataHolder.models, this))
                .setOnBannerItemClickListener(this)
                .setPageTransformer(new AccordionTransformer())
                .start();

        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, DataHolder.transformers));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String transStr = DataHolder.transformers.get(position);
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

    @Override
    public void onBannerClicked(@NonNull View view, int position, @NonNull DataModel model) {
        Toast.makeText(this, "position: " + position + "\ntxt: " + model.getDescription(), Toast.LENGTH_SHORT).show();
    }
}
