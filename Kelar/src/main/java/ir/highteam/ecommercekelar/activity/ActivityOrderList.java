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
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.adapter.AdapterOrderList;
import ir.highteam.ecommercekelar.bundle.BundleLoginResult;
import ir.highteam.ecommercekelar.bundle.order.BundleOrderList;
import ir.highteam.ecommercekelar.json.JsonLogin;
import ir.highteam.ecommercekelar.json.JsonOrder;
import ir.highteam.ecommercekelar.utile.LanguageFunctions;
import ir.highteam.ecommercekelar.utile.PrefsFunctions;
import ir.highteam.ecommercekelar.utile.network.HttpUrlFunction;
import ir.highteam.ecommercekelar.view.CustomToast;
import ir.highteam.ecommercekelar.view.RtlSupport;

public class ActivityOrderList extends AppCompatActivity {

    private LinearLayoutManager linearLayoutManager;
    private RecyclerView orderRecycler;
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
        setContentView(R.layout.activity_order_list);
        initCapabilities();
        initToolbar();
        initObjectAndView();
        initListenersAndEvents();

        initOrderRecycler();
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
        toolbarTitle.setText(getString(R.string.ACTIVITY_ORDER_LIST_TOOLBAR_TITLE));
        toolbarTitle.setTextSize(24);
        RtlSupport rtlSupport = new RtlSupport(getApplicationContext());
        rtlSupport.changeViewDirection(toolbarTitle);
    }

    private void initObjectAndView(){
        fab = (FloatingActionButton) findViewById(R.id.fab);
        orderRecycler = (RecyclerView) findViewById(R.id.orderRecycler);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        txtEmptyRecycler = (TextView) findViewById(R.id.txtEmptyRecycler);
        txtEmptyRecycler.setTypeface(tfSans);
        frameCircularProgress = (FrameLayout) findViewById(R.id.frameCircularProgress);
        frameNoInternet = (FrameLayout) findViewById(R.id.frameNoInternet);
        btnRetry = (AppCompatButton) findViewById(R.id.btnRetry);
    }

    private void initListenersAndEvents(){
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOrderList.this.finish();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderRecycler.smoothScrollToPosition(0);
            }
        });
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissNoInternet();
                showCircularProgress();
                initOrderRecycler();
            }
        });
    }


    private void initOrderRecycler(){

        new getOrder().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    private class getOrder extends AsyncTask<Void,Void,BundleOrderList> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected BundleOrderList doInBackground(Void... voids) {

            try {
                BundleOrderList orderList = new JsonOrder().getOrder(ActivityOrderList.this);
                if(orderList.result.status){
                    return orderList;
                }else {
                    String user = new PrefsFunctions(ActivityOrderList.this).getLoginUser();
                    String pass = new PrefsFunctions(ActivityOrderList.this).getLoginPass();
                    BundleLoginResult loginResult = new JsonLogin().checkLogin(ActivityOrderList.this,user,pass);
                    if(loginResult.status){
                        new PrefsFunctions(ActivityOrderList.this).putLoginToken(loginResult.token);
                        return new JsonOrder().getOrder(ActivityOrderList.this);
                    }else {
                        orderList.result.message = "-1";
                        new PrefsFunctions(ActivityOrderList.this).putLoginStatus(false);
                        return orderList;
                    }
                }
            }catch (Exception e){
                Crashlytics.logException(e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(BundleOrderList result) {
            super.onPostExecute(result);
            if(result == null){
                showNoInternet();
                return;
            }else if (result.result.message.equals("-1")){
                new CustomToast(getString(R.string.LOGIN_INFO_HAS_BEEN_CHANGED),ActivityOrderList.this).showToast(true);
                dismissCircularProgress();
                finish();
            }else if(result.result.status){
                orderRecycler.setHasFixedSize(true);
                orderRecycler.setLayoutManager(linearLayoutManager);
                if(result.orders.size() == 0){
                    txtEmptyRecycler.setVisibility(View.VISIBLE);
                }
                AdapterOrderList adapterOrderList = new AdapterOrderList(result.orders, getApplicationContext());
                orderRecycler.setAdapter(adapterOrderList);
            }else {
                showNoInternet();
                return;
            }

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
        ImageView imgNoInternet = (ImageView) findViewById(R.id.imgNoInternet);
        TextView txtNoInternet = (TextView) findViewById(R.id.txtNoInternet);
        imgNoInternet.setImageResource(R.drawable.oops);
        txtNoInternet.setText(getString(R.string.ERROR_OCCUR));
        dismissCircularProgress();
        frameNoInternet.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        urlFunction.flushHttpCache();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!PrefsFunctions.isLoggedIn(ActivityOrderList.this)){
            finish();
        }
    }
}
