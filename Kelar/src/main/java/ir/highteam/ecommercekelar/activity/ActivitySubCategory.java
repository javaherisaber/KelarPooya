package ir.highteam.ecommercekelar.activity;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
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
import ir.highteam.ecommercekelar.adapter.AdapterSubCategoryRecycler;
import ir.highteam.ecommercekelar.bundle.BundleSubCategory;
import ir.highteam.ecommercekelar.json.JsonSubCategory;
import ir.highteam.ecommercekelar.utile.LanguageFunctions;
import ir.highteam.ecommercekelar.utile.network.HttpUrlFunction;
import ir.highteam.ecommercekelar.utile.network.NetworkFunctions;
import ir.highteam.ecommercekelar.view.RtlSupport;

public class ActivitySubCategory extends AppCompatActivity {

    private LinearLayoutManager linearLayoutManager;
    private RecyclerView subCategoryRecycler;
    private Toolbar toolbar;
    TextView txtCategoryTitle;

    private String categoryTitle;
    private String categoryId;

    private FrameLayout frameCircularProgress,frameNoInternet;
    private AppCompatButton btnRetry;

    private HttpUrlFunction urlFunction;
    private TextView txtEmptyRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);

        categoryId = getIntent().getExtras().getString("categoryId");
        categoryTitle = getIntent().getExtras().getString("categoryTitle");

        initCapabilities();
        initToolbar();
        initObjectAndView();
        initListenerAndEvent();

        initSubCategoryRecycler();
    }

    private void initCapabilities(){
        urlFunction = new HttpUrlFunction(getApplicationContext());
        urlFunction.enableHttpCaching();
    }

    private void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(LanguageFunctions.isRtlLanguage()){
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_forward);
        }
        Typeface tfSans = Typeface.createFromAsset(getAssets(), getString(R.string.FONT_IRAN_SANS));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setTypeface(tfSans);
        toolbarTitle.setText(getString(R.string.ACTIVITY_SUB_CATEGORY_TOOLBAR_TITLE));
        toolbarTitle.setTextSize(24);
        RtlSupport rtlSupport = new RtlSupport(getApplicationContext());
        rtlSupport.changeViewDirection(toolbarTitle);
    }

    private void initObjectAndView(){
        txtCategoryTitle = (TextView) findViewById(R.id.txtCategoryTitle);
        subCategoryRecycler = (RecyclerView) findViewById(R.id.subCategoryRecycler);
        frameCircularProgress = (FrameLayout) findViewById(R.id.frameCircularProgress);
        frameNoInternet = (FrameLayout) findViewById(R.id.frameNoInternet);
        btnRetry = (AppCompatButton) findViewById(R.id.btnRetry);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        txtEmptyRecycler = (TextView) findViewById(R.id.txtEmptyRecycler);
        Typeface typeface = Typeface.createFromAsset(getAssets(), getString(R.string.FONT_YEKAN));
        txtCategoryTitle.setTypeface(typeface);
        txtCategoryTitle.setText(categoryTitle);
		txtEmptyRecycler.setTypeface(typeface);
    }

    private void initListenerAndEvent(){
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySubCategory.this.finish();
            }
        });
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissNoInternet();
                showCircularProgress();
                initSubCategoryRecycler();
            }
        });
    }

    private void initSubCategoryRecycler(){
        NetworkFunctions networkFunctions = new NetworkFunctions(getApplicationContext());
        if(networkFunctions.isOnline()){
            new getCategories().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else {
            dismissCircularProgress();
            showNoInternet();
        }
    }

    private class getCategories extends AsyncTask<Void,Void,ArrayList<BundleSubCategory>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<BundleSubCategory> doInBackground(Void... voids) {

            try {
                return new JsonSubCategory().getHomeCategory(ActivitySubCategory.this,categoryId);
            }catch (Exception e){
                Crashlytics.logException(e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<BundleSubCategory> result) {
            super.onPostExecute(result);
            if(result == null){
                dismissCircularProgress();
                showNoInternet();
                return;
            }
            subCategoryRecycler.setHasFixedSize(true);
            subCategoryRecycler.setLayoutManager(linearLayoutManager);
	        if(result.size() == 0){
	            txtEmptyRecycler.setVisibility(View.VISIBLE);
	        }
            AdapterSubCategoryRecycler adapterSubCategoryList = new AdapterSubCategoryRecycler(result, getApplicationContext());
            subCategoryRecycler.setAdapter(adapterSubCategoryList);

            dismissCircularProgress();
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
}
