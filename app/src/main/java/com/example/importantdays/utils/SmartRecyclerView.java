package com.example.importantdays.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class SmartRecyclerView extends RecyclerView {

    public boolean recycleScroll = false;
    public boolean scrollDown = true;
    public int scrolledDistance = 0;
    public int previousItemIndex = -1;

    public SmartRecyclerView(@NonNull Context context) {
        super(context);
    }

    public SmartRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SmartRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN: {
//                Log.d("dudu", "recycleView received" + ev.getX() + ", " + ev.getY() + " DOWN");
//            }
//            break;
//            case MotionEvent.ACTION_MOVE: {
//                Log.d("dudu", "recycleView received" + ev.getX() + ", " + ev.getY() + " MOVE");
//            }
//            break;
//            case MotionEvent.ACTION_UP: {
//                Log.d("dudu", "recycleView received" + ev.getX() + ", " + ev.getY() + " UP");
//            }
//            break;
//        }
//        Log.d("du0du", "out intercept recycleScroll: " + recycleScroll + "  " + ev.getAction());

        return this.recycleScroll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.d("du0du", "out onTouchEvent "+ ev.getAction());

//        if(recycleScroll == false) {
//            return false;
//        }
        return super.onTouchEvent(ev);
    }

    public void startScroll(boolean scrollDown) {
        this.recycleScroll = true;
        this.scrollDown = scrollDown;
        this.scrolledDistance = 0;
    }

    public void stopScroll() {
        this.recycleScroll = false;
        this.scrolledDistance = 0;
    }
}
