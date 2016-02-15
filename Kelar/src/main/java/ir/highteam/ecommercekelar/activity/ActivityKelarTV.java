package ir.highteam.ecommercekelar.activity;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.adapter.AdapterKelarTVList;
import ir.highteam.ecommercekelar.bundle.aparat.BundleAparatVideo;
import ir.highteam.ecommercekelar.bundle.aparat.BundleAparatVideoItem;
import ir.highteam.ecommercekelar.json.JsonAparat;
import ir.highteam.ecommercekelar.utile.LanguageFunctions;
import ir.highteam.ecommercekelar.utile.network.HttpUrlFunction;
import ir.highteam.ecommercekelar.utile.network.NetworkFunctions;
import ir.highteam.ecommercekelar.view.RtlSupport;

public class ActivityKelarTV extends AppCompatActivity {

    private LinearLayoutManager linearLayoutManager;
    private RecyclerView kelarTvRecycler;
    private AdapterKelarTVList adapterKelarTvList;
    private FloatingActionButton fab;
    private Toolbar toolbar;

    private FrameLayout frameHorizontalProgress,frameCircularProgress,frameNoInternet;
    private AppCompatButton btnRetry;

    private int page = 0;
    private boolean hasNext = true;

    private HttpUrlFunction urlFunction;
    private TextView txtEmptyRecycler;
    private Typeface tfSans;
    private boolean loading = true;
    int pastVisibleItems, visibleItemCount, totalItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelar_tv);
        this.page = 0;

        initCapabilities();
        initToolbar();
        initObjectAndView();
        initListenersAndEvents();

        initKelarTVRecycler();
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
        toolbarTitle.setText(getString(R.string.ACTIVITY_KELAR_TV_TOOLBAR_TITLE));
        toolbarTitle.setTextSize(24);
        RtlSupport rtlSupport = new RtlSupport(getApplicationContext());
        rtlSupport.changeViewDirection(toolbarTitle);
    }

    private void initObjectAndView(){
        fab = (FloatingActionButton) findViewById(R.id.fab);
        frameCircularProgress = (FrameLayout) findViewById(R.id.frameCircularProgress);
        frameHorizontalProgress = (FrameLayout) findViewById(R.id.frameHorizontalProgress);
        frameNoInternet = (FrameLayout) findViewById(R.id.frameNoInternet);
        btnRetry = (AppCompatButton) findViewById(R.id.btnRetry);
        kelarTvRecycler = (RecyclerView) findViewById(R.id.kelarTVRecycler);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        txtEmptyRecycler = (TextView) findViewById(R.id.txtEmptyRecycler);
        txtEmptyRecycler.setTypeface(tfSans);

    }

    private void initListenersAndEvents(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kelarTvRecycler.smoothScrollToPosition(0);
            }
        });
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissNoInternet();
                showCircularProgress();
                initKelarTVRecycler();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityKelarTV.this.finish();
            }
        });

        kelarTvRecycler.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = linearLayoutManager.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();

                    if (loading)
                    {
                        if ( (visibleItemCount + pastVisibleItems) >= totalItemCount)
                        {
                            loading = false;
                            Log.e("...", "Last Item Wow !");
                            //Do pagination.. i.e. fetch new data
                            if(hasNext)
                            {
                                showHorizontalProgress();
                                page += Integer.parseInt(getString(R.string.APARAT_PER_PAGE));
                                initKelarTVRecycler();
                            }
                        }
                    }
                }
            }
        });
    }

    private void initKelarTVRecycler(){

        NetworkFunctions networkFunctions = new NetworkFunctions(getApplicationContext());
        if(networkFunctions.isOnline()){
            new getAparatVideos().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else {
            dismissCircularProgress();
            showNoInternet();
        }
    }

    private class getAparatVideos extends AsyncTask<Void,Void,ArrayList<BundleAparatVideoItem>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<BundleAparatVideoItem> doInBackground(Void... voids) {

            try {
                BundleAparatVideo aparatVideo = new JsonAparat().getUserVideos(ActivityKelarTV.this,
                        ActivityKelarTV.this.getString(R.string.APARAT_USER_NAME)
                ,Integer.parseInt(ActivityKelarTV.this.getString(R.string.APARAT_PER_PAGE)),page);

                hasNext = aparatVideo.hasNext;
                return aparatVideo.videos;
            }catch (Exception e){
                Crashlytics.logException(e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<BundleAparatVideoItem> list) {
            super.onPostExecute(list);
            if(list == null){
                showNoInternet();
                return;
            }

            if(list.size() == 0){
                txtEmptyRecycler.setVisibility(View.VISIBLE);
            }else {

                if(page==0)
                {
                    kelarTvRecycler.setHasFixedSize(true);
                    kelarTvRecycler.setLayoutManager(linearLayoutManager);
                    adapterKelarTvList = new AdapterKelarTVList(list,getApplicationContext(),ActivityKelarTV.this);
                    kelarTvRecycler.setAdapter(adapterKelarTvList);

                    dismissCircularProgress();
                }
                else
                {
                    int lastPost = adapterKelarTvList.getItemCount();
                    for (int i = 0; i < list.size(); i++) {
                        adapterKelarTvList.insertItem(list.get(i));
                    }
                    adapterKelarTvList.notifyItemRangeInserted(lastPost, list.size());
                    dismissHorizontalProgress();
                    loading = true;
                }
            }
        }
    }

    private void dismissCircularProgress(){
        frameCircularProgress.setVisibility(View.GONE);
    }

    private void showCircularProgress(){
        frameCircularProgress.setVisibility(View.VISIBLE);
    }

    private void dismissHorizontalProgress(){
        frameHorizontalProgress.setVisibility(View.GONE);
    }

    private void showHorizontalProgress(){
        frameHorizontalProgress.setVisibility(View.VISIBLE);
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

}