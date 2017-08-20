package net.interkoneksi.malangtoday.app;

import com.orm.SugarApp;

import org.acra.ACRA;

/**
 * Created by ardhanmz on 8/18/17.
 */

public class App extends SugarApp {
    @Override
    public void onCreate(){
        super.onCreate();
        ACRA.init(this);
    }
}
