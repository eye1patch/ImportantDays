package com.example.importantdays.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;

import java.lang.ref.WeakReference;
import java.util.LinkedList;

public class SmartScrollView extends ScrollView {

    private ISmartScrollListener mSmartScrollListener;
    public LinkedList<ImageView> imageViewList = new LinkedList<>();
    public LinkedList<Integer> boundaryList = new LinkedList<>();
    public int heightPxiels;
    private int nowIndex = 0;

    public SmartScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

//    @Override
//    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
//        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
//        if (scrollY == 0) {
//            isScrollToTop = clampedY;
//            isScrollToBottom = false;
//            Log.d("du1du", "onScrollChanged isScrolledToTop:" + isScrollToTop);
//        } else {
//            isScrollToTop = false;
//            isScrollToBottom = clampedY;
//            Log.d("du1du", "onScrollChanged isScrolledToBottom:" + isScrollToBottom);
//        }
//        notifyScrollChangedListener();
//        Log.d("dudu", scrollX + " " + scrollY);
//    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
////        Log.d("du0du", "in intercept: " + ev.getAction());
//        return false;
//    }

//    private float down, move;
//    private boolean firstMoveThisTouch, startScroll = false;
//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        Log.d("du0du", "in onTouchEvent: " + ev.getAction());
//
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN: {
//                down = ev.getY();
//                firstMoveThisTouch = true;
//            }
//            break;
//            case MotionEvent.ACTION_MOVE: {
//                move = ev.getY();
//                Log.d("dududu", down + " " + move);
//                if(firstMoveThisTouch) {
//                    if((move < down) && this.isScrollToBottom) { // 下滑
//                        startScroll = true;
//                    }
//                    if((move > down) && this.isScrollToTop) {
//                        startScroll = true;
//                    }
//                }
//                firstMoveThisTouch = false;
//            }
//            break;
//            case MotionEvent.ACTION_UP: {
//                if(startScroll) {
//                    notifyScrollChangedListener();
//                }
//            }
//            break;
//        }
//        super.onTouchEvent(ev);
//        return true;
//    }
//
    public interface ISmartScrollListener {
        void onScrollToTop();
        void onScrollToBottom();
    }
//
    public void setSmartScrollListener(ISmartScrollListener smartScrollListener) {
        this.mSmartScrollListener = smartScrollListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        int imageViewTop = 0;
        ImageView imageView = imageViewList.get(nowIndex);

        if((t > boundaryList.get(nowIndex)) && (t <= boundaryList.get(nowIndex) + heightPxiels)) {
            imageViewTop = - (t - boundaryList.get(nowIndex));
        }
        else if((t > boundaryList.get(nowIndex) + heightPxiels)
                && (oldt <= boundaryList.get(nowIndex) + heightPxiels)) {
            imageViewTop = -heightPxiels;
            nowIndex += 1;
        }
        else if((t < boundaryList.get(nowIndex))
                && (oldt >= boundaryList.get(nowIndex))) {
            imageViewTop = 0;
        }
        if(nowIndex > 0) {
            if((t < boundaryList.get(nowIndex - 1) + heightPxiels)
                    && (oldt >= boundaryList.get(nowIndex - 1) + heightPxiels)) {
                nowIndex -= 1;
                imageView = imageViewList.get(nowIndex);
                imageViewTop = - (t - boundaryList.get(nowIndex));
            }
        }

        imageView.layout(
                0
                , imageViewTop
                , imageView.getWidth()
                , imageViewTop + imageView.getHeight()
        );
//            if(oldt % (3 * heightPxiels)  <= 2 * heightPxiels) {
////                imageViewList.get(t / (3 * heightPxiels)).scrollBy(0, t % heightPxiels );
//                imageView.layout(
//                        0,  - t % heightPxiels
//                        , imageView.getWidth(), - t % heightPxiels + imageView.getHeight()
//                );
//            }
//            else {
////                imageViewList.get(t / (3 * heightPxiels)).scrollBy(0, t - oldt);
//                imageView.layout(
//                        0,  - t % heightPxiels
//                        , imageView.getWidth(), - t % heightPxiels + imageView.getHeight()
//                );
//            }
//        }
//        Log.d("du0du", l + " " + t);
//
//        System.out.println("onScrollChanged getScrollY():" + getScrollY() + " t: " + t + " paddingTop: " + getPaddingTop());
//        if (getScrollY() == 0) {
//            isScrollToTop = true;
//            isScrollToBottom = false;
//            System.out.println("onScrollChanged isScrolledToTop:" + isScrollToTop);
//        } else if (getScrollY() + getHeight() - getPaddingTop() - getPaddingBottom() == getChildAt(0).getHeight()) {
//            isScrollToBottom = true;
//            System.out.println("onScrollChanged isScrolledToBottom:" + isScrollToBottom);
//            isScrollToTop = false;
//        } else {
//            isScrollToTop = false;
//            isScrollToBottom = false;
//        }
//        notifyScrollChangedListener();
    }
//
//    public void notifyScrollChangedListener() {
//        if(isScrollToTop) {
//            if(mSmartScrollListener != null) {
//                mSmartScrollListener.onScrollToTop();
//            }
//        } else if(isScrollToBottom) {
//            if(mSmartScrollListener != null) {
//                mSmartScrollListener.onScrollToBottom();
//            }
//        }
//    }
//
//    public boolean isScrollToTop() {
//        return isScrollToTop;
//    }
//
//    public boolean isScrollToBottom() {
//        return isScrollToBottom;
//    }
}
