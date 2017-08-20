package net.interkoneksi.malangtoday.app;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import net.interkoneksi.malangtoday.R;
import net.interkoneksi.malangtoday.adaptor.AdaptorNavbar;
import net.interkoneksi.malangtoday.model.ModelKategori;
import net.interkoneksi.malangtoday.util.KonfigurasiAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity
        implements SearchView.OnQueryTextListener{

    String Kategori_url = "get_category_index/";
    private Toolbar toolbar;
    private DrawerLayout mDrawer;
    private NavigationView navview;
    private ExpandableListView navmenu;

    List<AdaptorNavbar.ExpandMenu> nlistHeader;
    HashMap<AdaptorNavbar.ExpandMenu, List<ModelKategori>> nListChild;
    private FragmentManager fm;
    private AdaptorNavbar nAdapter;
    private int selectCat = 0;
    private boolean postpage;
    private MenuItem itemsearch;
    private List<ModelKategori> listKategory;
    private boolean isReadyExit;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navview = (NavigationView) findViewById(R.id.nav_view);
        navmenu = (ExpandableListView) findViewById(R.id.nav_menu);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, toolbar,R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        fm = getSupportFragmentManager();
        navmenu.setGroupIndicator(null);
        navData();
        nAdapter = new AdaptorNavbar(this, nlistHeader, nListChild);
        navmenu.setAdapter(nAdapter);

        navmenu.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                if (i == 0){
                    selectCat = i;
                    loadPost(selectCat, "");
                }
                else if (i==1){
                    nlistHeader.get(i).toggle();
                    return false;
                }else if (i==2){
                    itemsearch.setVisible(false);
                    postpage = false;
                    fm.beginTransaction().replace(R.id.frame, new TentangKami()).addToBackStack(null)
                            .commit();
                }
                onBackPressed();
                return false;
            }
        });
        navmenu.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                selectCat = listKategory.get(i).id;
                loadPost(selectCat,"");
                onBackPressed();
                return true;
            }
        });
        loadPost(selectCat,"");

    }
    public void loadPost(int cat, String query){
        postpage= true;
        if (itemsearch != null){
            itemsearch.setVisible(true);
        }
        Post postfrg = new Post();
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        Bundle bundle = new Bundle();
        bundle.putInt("cat", cat);
        bundle.putString("query", query);
        postfrg.setArguments(bundle);
        fm.beginTransaction().replace(R.id.frame, postfrg).commit();

    }

    private void navData() {
        nlistHeader = new ArrayList<>();
        nListChild = new HashMap<>();
        AdaptorNavbar.ExpandMenu item = new AdaptorNavbar.ExpandMenu();
        item.iconName = getResources().getString(R.string.home);
        nlistHeader.add(item);
        item = new AdaptorNavbar.ExpandMenu();
        item.iconName = getResources().getString(R.string.categories);
        nlistHeader.add(item);
        listKategory = new ArrayList<>();
        Kategori();
        nListChild.put(nlistHeader.get(1), listKategory);
        item = new AdaptorNavbar.ExpandMenu();
        item.iconName= getResources().getString(R.string.about);
        nlistHeader.add(item);

    }


    @Override
    public void onBackPressed() {
        if(mDrawer.isDrawerOpen(GravityCompat.START)){
            mDrawer.closeDrawer(GravityCompat.START);
        }else {
            if (postpage){
                if (isReadyExit){
                    super.onBackPressed();
                }else {
                    isReadyExit = true;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isReadyExit = false;
                        }
                    },3000);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        itemsearch = menu.findItem(R.id.search);
        SearchView search = (SearchView) MenuItemCompat.getActionView(itemsearch);
        search.setOnQueryTextListener(this);
        MenuItemCompat.setOnActionExpandListener(itemsearch, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                loadPost(selectCat, "");
                return true;
            }
        });
        return true;
    }
    public void Kategori(){
        if (KonfigurasiAPI.isConnected(getBaseContext())){
            KonfigurasiAPI.get(Kategori_url, null, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                    try {
                        KonfigurasiAPI.save(getBaseContext(), "categories", response.toString());
                        JSONArray cat = response.getJSONArray("categories");
                        for (int i = 0; i < cat.length(); i++) {
                            ModelKategori header = new ModelKategori();
                            header.parseKategori(cat.getJSONObject(i));
                            listKategory.add(i, header);
                            nAdapter.updated();
                        }
                    }catch (JSONException je){
                        je.printStackTrace();
                    }
                }
            });
        }else {
            try {
                JSONObject response = new JSONObject(KonfigurasiAPI.load(getBaseContext(), "categories"));
                JSONArray categories = response.getJSONArray("categories");
                for (int i=0; i < categories.length(); i++){
                    ModelKategori header = new ModelKategori();
                    header.parseKategori(categories.getJSONObject(i));
                    listKategory.add(i, header);
                }

            }catch (JSONException je){
                je.printStackTrace();
            }

        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        loadPost(0, query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
