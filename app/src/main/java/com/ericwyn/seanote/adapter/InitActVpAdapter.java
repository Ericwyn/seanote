package com.ericwyn.seanote.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * the adapter of the viewpager in InitActivity
 * Created by ericwyn on 17-8-17.
 */

public class InitActVpAdapter extends PagerAdapter{
    private List<View> viewContainter;



    public InitActVpAdapter(List<View> viewContainter){
        this.viewContainter=viewContainter;

    }



    @Override
    public int getCount() {
        return viewContainter.size();
    }



    //滑动时候销毁当前的View
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
        container.removeView(viewContainter.get(position));
    }

    //生成的view
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(viewContainter.get(position));
        return viewContainter.get(position);
    }

    //
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


}
