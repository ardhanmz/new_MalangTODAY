package net.interkoneksi.malangtoday.app;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import net.interkoneksi.malangtoday.JSONParser.ModelKomentar;
import net.interkoneksi.malangtoday.R;
import net.interkoneksi.malangtoday.adaptor.AdaptorKomentar;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by Ardhan MZ on 16-Aug-17.
 */



public class KomentarActivity extends SwipeActivity{

    public RecyclerView mRecyclerView;
    public LinearLayoutManager mLinearLayoutManager;
    public List<ModelKomentar> mList;
    public FloatingActionButton mFab;
    public AdaptorKomentar mAdaptor;
    public JSONArray jsonArray;
    int postid = -1;

    String commentstatus;
    CardView mDataCard;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.komentar_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.comment_list);
        mFab = (FloatingActionButton) findViewById(R.id.fab_add_comment);
        mDataCard = (CardView) findViewById(R.id.no_data);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Komentar");

        mLinearLayoutManager = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false);
        mList = new ArrayList<>();

        String data = getIntent().getStringExtra("data");
        postid = getIntent().getIntExtra("postId", -1);
        commentstatus = getIntent().getStringExtra("commentStatus");

        try {
            jsonArray = new JSONArray(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                ModelKomentar commentModel = new ModelKomentar();
                commentModel.parseComment(jsonArray.getJSONObject(i));
                mList.add(commentModel);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        mAdaptor = new AdaptorKomentar(getBaseContext(), mList);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAdaptor);

        if (!(mList.size() > 0)) {
            mRecyclerView.setVisibility(View.GONE);
            mDataCard.setVisibility(View.VISIBLE);
        }

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (commentstatus.equals("open")) {
                    KirimKomentar commentSubmitDialog = new KirimKomentar();
                    Bundle args = new Bundle();
                    args.putInt("postId", postid);
                    commentSubmitDialog.show(getSupportFragmentManager(), "");
                    commentSubmitDialog.setArguments(args);
                } else {
                    Snackbar.make(findViewById(android.R.id.content),
                            getResources().getString(R.string.comment_status_close_error),
                            Snackbar.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


}
