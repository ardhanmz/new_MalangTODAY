package net.interkoneksi.malangtoday.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jude.swipbackhelper.SwipeBackHelper;

/**
 * Created by ardhanmz on 8/18/17.
 */

public class SwipeActivity extends AppCompatActivity{
    public class SwipeBaseActivity extends AppCompatActivity{
        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            SwipeBackHelper.onCreate(this);
            SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(true)
                    .setSwipeSensitivity(0.5f)
                    .setSwipeRelateEnable(true);
        }
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
    }

    @Override
    public void onResume(){
        super.onResume();
    }
}
