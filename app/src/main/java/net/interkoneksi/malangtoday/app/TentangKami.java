package net.interkoneksi.malangtoday.app;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import net.interkoneksi.malangtoday.R;
import net.interkoneksi.malangtoday.util.KonfigurasiAPI;

/**
 * Created by ardhanmz on 8/18/17.
 */

public class TentangKami extends Fragment{
    ImageView fb, twitter, youtube, ig;

    public TentangKami(){
        //konstruktor kosong
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.tentangkami, container, false);

        fb = (ImageView) view.findViewById(R.id.facebook);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KonfigurasiAPI.alamatluar(getActivity(), "http://facebook.com/");
            }
        });

        twitter = (ImageView) view.findViewById(R.id.twitter);
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KonfigurasiAPI.alamatluar(getActivity(), "http://twitter.com");
            }
        });
        youtube = (ImageView) view.findViewById(R.id.youtube);
        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KonfigurasiAPI.alamatluar(getActivity(), "http://youtube.com");
            }
        });
        ig = (ImageView) view.findViewById(R.id.instagram);
        ig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KonfigurasiAPI.alamatluar(getActivity(), "http://instagram.com/");
            }
        });
        return view;
    }
}
