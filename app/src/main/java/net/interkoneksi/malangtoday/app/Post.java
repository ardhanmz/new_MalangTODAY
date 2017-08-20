package net.interkoneksi.malangtoday.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import net.interkoneksi.malangtoday.adaptor.AdaptorPost;
import net.interkoneksi.malangtoday.app.DetailPost;
import net.interkoneksi.malangtoday.app.RecyclerItemClickListener;
import net.interkoneksi.malangtoday.model.ModelPost;
import net.interkoneksi.malangtoday.R;
import net.interkoneksi.malangtoday.util.KonfigurasiAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class Post extends Fragment {

    View rootView;
    String adress, search = "";
    RecyclerView postList;
    List<ModelPost> postArray;
    AdaptorPost adapter;
    LinearLayoutManager linearLayoutManager;
    SwipeRefreshLayout swipeRefreshLayout;
    CardView noDataCard, noArchiveCard;
    Button btnReload;
    int page = 1, selectedCat = 0, previousTotal = 0,
            visibleThreshold = 5, firstVisibleItem, visibleItemCount,
            totalItemCount;
    boolean loading = true;

    public Post() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.postfragment, container, false);

        Bundle bundle = this.getArguments();
        selectedCat = bundle.getInt("cat", 0);
        search = bundle.getString("query", "");

        postList = (RecyclerView) rootView.findViewById(R.id.post_list);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        btnReload = (Button) rootView.findViewById(R.id.btn_reload);
        noDataCard = (CardView) rootView.findViewById(R.id.no_data);
        noArchiveCard = (CardView) rootView.findViewById(R.id.no_archive);

        postArray = new ArrayList<>();
        adapter = new AdaptorPost(getActivity(), postArray);
        postList.setLayoutManager(linearLayoutManager);
        postList.setAdapter(adapter);

        if (selectedCat > -1) {
            refreshPosts();
        } else {
            loadArchive();
        }

        postList.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (!swipeRefreshLayout.isRefreshing()) {
                    Intent intent = new Intent(getActivity(), DetailPost.class);
                    intent.putExtra("data", postArray.get(position).parseJSON().toString());
                    if (selectedCat == -1) {
                        intent.putExtra("is_archive", "true");
                    } else {
                        intent.putExtra("is_archive", "false");
                    }
                    startActivity(intent);
                }
            }
        }));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPosts();
            }
        });

        postList.addOnScrollListener(scrollListener);

        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                noDataCard.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(true);
                refreshPosts();
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (selectedCat == -1) {
            loadArchive();
        }
    }

    public void loadArchive() {
        postArray = new ArrayList<>();
        postArray = ModelPost.listAll(ModelPost.class);
        adapter.update(postArray);

        if (postArray.size() <= 0) {
            noArchiveCard.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.GONE);
        }
    }

    public void refreshPosts() {
        if (KonfigurasiAPI.isConnected(getActivity())) {
            swipeRefreshLayout.setRefreshing(true);
            postArray = new ArrayList<>();
            loading = false;
            page = 1;
            getPosts(selectedCat);
        } else {
            Snackbar.make(getActivity().findViewById(android.R.id.content),
                    getResources().getString(R.string.no_internet),
                    Snackbar.LENGTH_SHORT).show();
        }
    }

    public void getPosts(int cat) {
        if (cat == 0) {
            adress = "get_recent_posts/?page=" + page + "&count=" + KonfigurasiAPI.postCount;
        } else if (cat > 0) {
            adress = "get_category_posts/?id=" + cat + "&count=" + KonfigurasiAPI.postCount + "&page=" + page;
        }

        if (!search.equals("")) {
            adress = "get_search_results/?search=" + search + "&page=" + page + "&count=" + KonfigurasiAPI.postCount;
        }

        if (KonfigurasiAPI.isConnected(getActivity())) {
            KonfigurasiAPI.get(adress, null, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        JSONArray posts = response.getJSONArray("posts");

                        ModelPost postModel;
                        for (int i = 0; i < posts.length(); i++) {
                            postModel = new ModelPost();
                            postModel.parsePost(posts.getJSONObject(i));
                            postArray.add(postModel);
                        }
                        adapter.update(postArray);
                        swipeRefreshLayout.setRefreshing(false);

                        if (!(postArray.size() > 0)) {
                            swipeRefreshLayout.setVisibility(View.GONE);
                            noDataCard.setVisibility(View.VISIBLE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    swipeRefreshLayout.setRefreshing(false);
                    swipeRefreshLayout.setVisibility(View.GONE);
                    noDataCard.setVisibility(View.VISIBLE);
                }

            });
        } else {
            swipeRefreshLayout.setRefreshing(false);
            swipeRefreshLayout.setVisibility(View.GONE);
            noDataCard.setVisibility(View.VISIBLE);
        }
    }

    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            visibleItemCount = recyclerView.getChildCount();
            totalItemCount = linearLayoutManager.getItemCount();
            firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                }
            }
            if (!loading && (totalItemCount - visibleItemCount)
                    <= (firstVisibleItem + visibleThreshold)) {
                page++;
                getPosts(selectedCat);
                loading = true;
            }

        }
    };

    public void showDataUi() {
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        noDataCard.setVisibility(View.GONE);
    }

}
