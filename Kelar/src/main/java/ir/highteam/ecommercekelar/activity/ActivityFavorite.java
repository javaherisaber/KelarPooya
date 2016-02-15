package ir.highteam.ecommercekelar.activity;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.adapter.AdapterFavoriteList;
import ir.highteam.ecommercekelar.bundle.BundleFavorite;
import ir.highteam.ecommercekelar.database.DatabaseInteract;
import ir.highteam.ecommercekelar.json.JsonFavorite;
import ir.highteam.ecommercekelar.utile.LanguageFunctions;
import ir.highteam.ecommercekelar.utile.network.HttpUrlFunction;
import ir.highteam.ecommercekelar.utile.network.NetworkFunctions;
import ir.highteam.ecommercekelar.view.RtlSupport;

public class ActivityFavorite extends AppCompatActivity {

    private LinearLayoutManager linearLayoutManager;
    private RecyclerView favoriteRecycler;
    private Toolbar toolbar;
    private FloatingActionButton fab;

    private HttpUrlFunction urlFunction;
    private TextView txtEmptyRecycler;
    private Typeface tfSans;

    private FrameLayout frameCircularProgress,frameNoInternet;
    private AppCompatButton btnRetry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        initCapabilities();
        initToolbar();
        initObjectAndView();
        initListenerAndEvent();

        initProductRecycler();
    }

    private void initCapabilities(){
        urlFunction = new HttpUrlFunction(getApplicationContext());
        urlFunction.enableHttpCaching();
    }

    private void initToolbar(){
        tfSans = Typeface.createFromAsset(getAssets(), getString(R.string.FONT_IRAN_SANS));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(LanguageFunctions.isRtlLanguage()){
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_forward);
        }
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setTypeface(tfSans);
        toolbarTitle.setText(getString(R.string.ACTIVITY_FAVORITE_TOOLBAR_TITLE));
        toolbarTitle.setTextSize(24);
        RtlSupport rtlSupport = new RtlSupport(getApplicationContext());
        rtlSupport.changeViewDirection(toolbarTitle);
    }

    private void initObjectAndView(){
        fab = (FloatingActionButton) findViewById(R.id.fab);
        favoriteRecycler = (RecyclerView) findViewById(R.id.favoriteRecycler);
        frameCircularProgress = (FrameLayout) findViewById(R.id.frameCircularProgress);
        frameNoInternet = (FrameLayout) findViewById(R.id.frameNoInternet);
        btnRetry = (AppCompatButton) findViewById(R.id.btnRetry);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        txtEmptyRecycler = (TextView) findViewById(R.id.txtEmptyRecycler);
        txtEmptyRecycler.setTypeface(tfSans);
    }

    private void initListenerAndEvent(){
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityFavorite.this.finish();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favoriteRecycler.smoothScrollToPosition(0);
            }
        });
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissNoInternet();
                showCircularProgress();
                initProductRecycler();
            }
        });
    }


    private void initProductRecycler(){

        NetworkFunctions networkFunctions = new NetworkFunctions(getApplicationContext());
        if(networkFunctions.isOnline()){
            new getFavorites().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else {
            dismissCircularProgress();
            showNoInternet();
        }

    }

    private class getFavorites extends AsyncTask<Void,Void,ArrayList<BundleFavorite>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<BundleFavorite> doInBackground(Void... voids) {

            try {
                DatabaseInteract databaseInteract = new DatabaseInteract(ActivityFavorite.this);
                String idList = databaseInteract.getFavoritesId();
                if(idList.equals(""))
                    return new ArrayList<>();
                return new JsonFavorite().getFavorites(ActivityFavorite.this,idList);
            }catch (Exception e){
                Crashlytics.logException(e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<BundleFavorite> bundleFavorites) {
            super.onPostExecute(bundleFavorites);
            if(bundleFavorites == null){
                dismissCircularProgress();
                showNoInternet();
                return;
            }
            favoriteRecycler.setHasFixedSize(true);
            favoriteRecycler.setLayoutManager(linearLayoutManager);
            if(bundleFavorites.size() == 0){
                txtEmptyRecycler.setVisibility(View.VISIBLE);
            }
            AdapterFavoriteList adapterFavoriteList = new AdapterFavoriteList(bundleFavorites, getApplicationContext(),ActivityFavorite.this);
            favoriteRecycler.setAdapter(adapterFavoriteList);
            frameCircularProgress.setVisibility(View.INVISIBLE);
        }
    }

    private void dismissCircularProgress(){
        frameCircularProgress.setVisibility(View.GONE);
    }

    private void showCircularProgress(){
        frameCircularProgress.setVisibility(View.VISIBLE);
    }

    private void dismissNoInternet(){
        frameNoInternet.setVisibility(View.GONE);
    }

    private void showNoInternet(){
        frameNoInternet.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        urlFunction.flushHttpCache();
    }

    public void showEmptyListTxt(){
        txtEmptyRecycler.setVisibility(View.VISIBLE);
    }
}
