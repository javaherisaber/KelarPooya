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
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.adapter.AdapterProductList;
import ir.highteam.ecommercekelar.bundle.BundleMoreProduct;
import ir.highteam.ecommercekelar.json.JsonMoreProduct;
import ir.highteam.ecommercekelar.utile.LanguageFunctions;
import ir.highteam.ecommercekelar.utile.network.HttpUrlFunction;
import ir.highteam.ecommercekelar.utile.network.NetworkFunctions;
import ir.highteam.ecommercekelar.view.RtlSupport;

public class ActivityProductList extends AppCompatActivity {

    private LinearLayoutManager linearLayoutManager;
    private RecyclerView productRecycler;
    private AdapterProductList adapterProductList;
    private FloatingActionButton fab;
    private Toolbar toolbar;

    private FrameLayout frameHorizontalProgress,frameCircularProgress,frameNoInternet;
    private AppCompatButton btnRetry;

    //Extras from previous activity
    private String moreType,apiMoreUrl,toolbarTitle;
    private int page = 1;
    private boolean hasNext = true;

    private HttpUrlFunction urlFunction;
    private TextView txtEmptyRecycler;
    private Typeface tfSans;
    private boolean loading = true;
    int pastVisibleItems, visibleItemCount, totalItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        this.page = 1;

        moreType = getIntent().getExtras().getString("moreType");
        apiMoreUrl = getIntent().getExtras().getString("apiMoreUrl");
        toolbarTitle = getIntent().getExtras().getString("toolbarTitle");

        initCapabilities();
        initToolbar();
        initObjectAndView();
        initListenersAndEvents();


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
        toolbarTitle.setText(this.toolbarTitle);
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
        productRecycler = (RecyclerView) findViewById(R.id.productRecycler);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        txtEmptyRecycler = (TextView) findViewById(R.id.txtEmptyRecycler);
        txtEmptyRecycler.setTypeface(tfSans);

    }

    private void initListenersAndEvents(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productRecycler.smoothScrollToPosition(0);
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

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityProductList.this.finish();
            }
        });

        productRecycler.addOnScrollListener(new RecyclerView.OnScrollListener()
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
                                page++;
                                initProductRecycler();
                            }
                        }
                    }
                }
            }
        });
    }

    private void initProductRecycler(){

        NetworkFunctions networkFunctions = new NetworkFunctions(getApplicationContext());
        if(networkFunctions.isOnline()){
            new getMoreProducts().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else {
            dismissCircularProgress();
            showNoInternet();
        }
    }

    private class getMoreProducts extends AsyncTask<Void,Void,BundleMoreProduct>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected BundleMoreProduct doInBackground(Void... voids) {

            try {
                return new JsonMoreProduct().getMoreProduct(ActivityProductList.this,apiMoreUrl,moreType,page);
            }catch (Exception e){
                Crashlytics.logException(e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(BundleMoreProduct moreProducts) {
            super.onPostExecute(moreProducts);
            if(moreProducts == null){
                showNoInternet();
                return;
            }
            hasNext = moreProducts.hasNext;
            if(page==1)
            {
                productRecycler.setHasFixedSize(true);
                productRecycler.setLayoutManager(linearLayoutManager);
                adapterProductList = new AdapterProductList(moreProducts.moreProductLists, getApplicationContext(),ActivityProductList.this);
                productRecycler.setAdapter(adapterProductList);

                dismissCircularProgress();
            }
            else
            {
                int lastPost = adapterProductList.getItemCount();
                for (int i = 0; i < moreProducts.moreProductLists.size(); i++) {
                    adapterProductList.insertItem(moreProducts.moreProductLists.get(i));
                }
                adapterProductList.notifyItemRangeInserted(lastPost, moreProducts.moreProductLists.size());
                dismissHorizontalProgress();
                loading = true;
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
