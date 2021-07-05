package com.example.importantdays;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.importantdays.utils.NetworkUtils;
import com.example.importantdays.utils.SmartScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class DetailActivity extends AppCompatActivity {

    private static final String GET_ARTICLE_URL
            ="https://hello-cloudbase-9glzj75p529f0334-1305329753.ap-shanghai.app.tcloudbase.com/getArticle";

    public LinkedList<String> picUrls = new LinkedList<>();
    private SmartScrollView mScrollView;
    private String articleResult = "";
    private int[] section2ViewMargin = {28, 36, 28, 36};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();

        String article = intent.getStringExtra("article");

        mScrollView = (SmartScrollView) findViewById(R.id.smartScrollView);

        articleResult = getArticle(article);
        initTitle(articleResult);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initTextItems(articleResult);
        initAllPictures();
    }

    private String getArticle(String name) {
        final String[] result = {""};
        Thread thread = new Thread(() -> {
            NetworkUtils networkUtils = new NetworkUtils(GET_ARTICLE_URL);
            networkUtils.addQueryParm("article", name);
            result[0] = networkUtils.getInfo();
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result[0];

    }

    private void initTitle(String data) {
        ConstraintLayout titleLayout = (ConstraintLayout) findViewById(R.id.titleLayout);
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        int heightPixels = outMetrics.heightPixels;

        ViewGroup.LayoutParams params = titleLayout.getLayoutParams();
        params.height = heightPixels;
        titleLayout.setLayoutParams(params);

        mScrollView.heightPxiels = heightPixels;

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(data).getJSONObject("res");
            JSONArray itemsArray = jsonObject.getJSONArray("data");

            JSONObject picInfo = itemsArray.getJSONObject(0);

            String coverPicUrl = picInfo.getString("coverPicUrl");
            String title = picInfo.getString("title");

            ImageView imageView = findViewById(R.id.coverPic);
            TextView textView = findViewById(R.id.title);

            Glide.with(getApplicationContext())
                    .load(coverPicUrl)
                    .placeholder(R.drawable.fuzzy)
                    .into(imageView);
            textView.setText(title);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initTextItems(String data) {

        JSONObject jsonObject = null;
        try {

            jsonObject = new JSONObject(data).getJSONObject("res");
            JSONArray articleItems = jsonObject.getJSONObject("items").getJSONArray("data");

            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

            for(int i = 0; i < articleItems.length(); i++) {

                JSONObject itemInfo = articleItems.getJSONObject(i);
                picUrls.addLast(itemInfo.getString("picUrl"));

                boolean longItem = itemInfo.getBoolean("longItem");

                TextView placeHolderView = new TextView(this);
                LinearLayout.LayoutParams holderLayoutParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, mScrollView.heightPxiels);

                placeHolderView.setText("");
                placeHolderView.setLayoutParams(holderLayoutParams);

                linearLayout.addView(placeHolderView);

                if(longItem) {
                    TextView section1View = new TextView(this);
                    LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, mScrollView.heightPxiels);
                    layoutParams1.setMargins(
                            getPixelsFromDp(section2ViewMargin[0])
                            , 0
                            , getPixelsFromDp(section2ViewMargin[2])
                            , 0
                    );

                    section1View.setText(itemInfo.getString("section1"));
                    section1View.setLayoutParams(layoutParams1);
                    section1View.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    section1View.setTextColor(Color.rgb(255, 255,255));

                    linearLayout.addView(section1View);
                }

                TextView section2View = new TextView(this);
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams2.setMargins(
                        getPixelsFromDp(section2ViewMargin[0])
                        , getPixelsFromDp(section2ViewMargin[1])
                        , getPixelsFromDp(section2ViewMargin[2])
                        , getPixelsFromDp(section2ViewMargin[3])
                );

                section2View.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                section2View.setText(itemInfo.getString("section2").replace("\\n", "\n"));
                section2View.setLayoutParams(layoutParams2);
                section2View.setTextColor(Color.rgb(255, 255,255));
                linearLayout.addView(section2View);

                int itemHeight = mScrollView.heightPxiels
                        + getPixelsFromDp(section2ViewMargin[1] + section2ViewMargin[3]);
                if(longItem) {
                    itemHeight += mScrollView.heightPxiels;
                }
                if(i != 0) {
                    itemHeight += mScrollView.boundaryList.getLast();
                }
                mScrollView.boundaryList.addLast(itemHeight);

                int bufferI = i;
                section2View.post(() -> {
                    for(int j = bufferI; j < mScrollView.boundaryList.size(); j++) {
                        int height = mScrollView.boundaryList.get(j) + section2View.getHeight();
                        mScrollView.boundaryList.set(j, height);
                    }
                });
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initAllPictures() {
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        for(int i = 0; i < picUrls.size(); i++) {
            ImageView imageView = new ImageView(this);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            imageView.setLayoutParams(layoutParams);

            Glide.with(DetailActivity.this)
                    .load(picUrls.get(i))
                    .placeholder(R.drawable.fuzzy)
                    .into(imageView);

            Log.d("dudu", picUrls.get(i));

            Glide.with(DetailActivity.this)
                    .load(picUrls.get(i))
                    .apply(bitmapTransform(new BlurTransformation(25, 5)))
                    .into(new CustomTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            imageView.setBackground(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) { }
                    });
            if(picUrls.get(i).equals("\"\"")) {
                Resources resources = imageView.getContext().getResources();
                Drawable drawable = resources.getDrawable(R.drawable.fuzzy);
                imageView.setBackground(drawable);
            }

            frameLayout.addView(imageView, 0, layoutParams);
            mScrollView.imageViewList.addLast(imageView);
        }
    }

    private int getPixelsFromDp(int i){
        float scale = this.getResources().getDisplayMetrics().density;
        return (int) (i * scale + 0.5f);
    }
}
