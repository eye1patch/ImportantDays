package com.example.importantdays;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.importantdays.utils.MonthBrowseAdapter;
import com.example.importantdays.utils.NetworkUtils;
import com.example.importantdays.utils.RoundImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;

import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;


public class MainActivity extends AppCompatActivity {

    private static final String GET_PICS_URL
            ="https://hello-cloudbase-9glzj75p529f0334-1305329753.ap-shanghai.app.tcloudbase.com/getMainActivityPics";

    HashMap<Integer, String> imageViewArticle = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            loadMainPictures();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void loadMainPictures() throws InterruptedException {

        final String[] result = {""};
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                NetworkUtils networkUtils = new NetworkUtils(GET_PICS_URL);
                result[0] = networkUtils.getInfo();
            }
        });
        thread.start();

        LinkedList<JSONObject> monthItemInfoList = new LinkedList<>();
//        mUrlList.addLast("https://img1.baidu.com/it/u=2264516197,445405554&fm=26&fmt=auto&gp=0.jpg");
//        mUrlList.addLast("https://img1.baidu.com/it/u=4075683411,3547467971&fm=26&fmt=auto&gp=0.jpg");
//        mUrlList.addLast("https://img1.baidu.com/it/u=444246795,4045182965&fm=26&fmt=auto&gp=0.jpg");

        thread.join();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result[0]).getJSONObject("res");
            JSONArray itemsArray = jsonObject.getJSONArray("data");

            for(int i = 0; i < itemsArray.length(); i++) {
                JSONObject picInfo = itemsArray.getJSONObject(i);
                String imageViewId = picInfo.getString("imageViewId");
                String picUrl = picInfo.getString("picUrl");
                String article = picInfo.getString("article");

                if(imageViewId.equals("browseByMonth")) {
                    monthItemInfoList.addLast(picInfo);
                } else {
                    int id = getResources().getIdentifier(imageViewId,"id", getPackageName());
                    ImageView imageView = (ImageView) findViewById(id);

                    imageViewArticle.put(id, article);

                    Glide.with(getApplicationContext())
                            .load(picUrl)
                            .into(imageView);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RecyclerView mRecyclerView = findViewById(R.id.monthRecyclerView);
        MonthBrowseAdapter mAdapter = new MonthBrowseAdapter(this, monthItemInfoList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

    }

    // 点击图片进入具体介绍
    public void getDetail(View view) throws InterruptedException {
//        Intent intoDetail = new Intent(MainActivity.this, SecondActivity.class);

        String article = imageViewArticle.get(view.getId());

        Intent intoDetail = new Intent(MainActivity.this, DetailActivity.class);
        intoDetail.putExtra("article", article);
        startActivity(intoDetail);
    }
}