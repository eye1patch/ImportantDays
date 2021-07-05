package com.example.importantdays.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.importantdays.DetailActivity;
import com.example.importantdays.MainActivity;
import com.example.importantdays.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

public class MonthBrowseAdapter extends
        RecyclerView.Adapter<MonthBrowseAdapter.MonthViewHolder> {

    private static final String TAG = "WordListAdapter";

    private final LinkedList<JSONObject> mMonthInfoList;
    private LayoutInflater mInflater;

    public MonthBrowseAdapter(Context context, LinkedList<JSONObject> monthInfoList) {
        mInflater = LayoutInflater.from(context);
        mMonthInfoList = monthInfoList;
    }

    class MonthViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{
        public final ImageView monthPic;
        public final TextView nameView;
        public final TextView monthView;
        final MonthBrowseAdapter mAdapter;

        public MonthViewHolder(@NonNull View itemView, MonthBrowseAdapter adapter) {
            super(itemView);
            monthPic = itemView.findViewById(R.id.monthPic);
            nameView = itemView.findViewById(R.id.dayName);
            monthView = itemView.findViewById(R.id.month);
            this.mAdapter = adapter;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int mPosition = getLayoutPosition();

            JSONObject monthInfo = mMonthInfoList.get(mPosition);
            String article = "";
            try {
                article = monthInfo.getString("article");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Intent intoDetail = new Intent(monthPic.getContext(), DetailActivity.class);
            Log.d("dudu", "!" + article);
            intoDetail.putExtra("article", article);
            monthPic.getContext().startActivity(intoDetail);
        }
    }

    @NonNull
    @Override
    public MonthBrowseAdapter.MonthViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.month_item, parent, false);
        return new MonthViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull MonthBrowseAdapter.MonthViewHolder holder, int position) {
        JSONObject monthInfo = mMonthInfoList.get(position);
        try {
            holder.nameView.setText(monthInfo.getString("name"));
            holder.monthView.setText(monthInfo.getString("month"));
            Glide.with(holder.monthPic.getContext())
                    .load(monthInfo.getString("picUrl"))
                    .placeholder(R.drawable.fuzzy)
                    .into(holder.monthPic);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mMonthInfoList.size();
    }
}
