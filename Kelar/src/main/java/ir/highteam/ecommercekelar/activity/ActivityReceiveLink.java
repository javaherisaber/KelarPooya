package ir.highteam.ecommercekelar.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.json.JsonReceiveLink;
import ir.highteam.ecommercekelar.utile.LanguageFunctions;
import ir.highteam.ecommercekelar.utile.network.HttpUrlFunction;
import ir.highteam.ecommercekelar.utile.network.NetworkFunctions;
import ir.highteam.ecommercekelar.view.RtlSupport;

public class ActivityReceiveLink extends AppCompatActivity {

    private HttpUrlFunction urlFunction;
    private Typeface tfSans;
    private AppCompatButton btnRetry;
    private FrameLayout frameNoInternet,frameCircularProgress,frameProductNotFound;

    private String receivedUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_recieve_link);

        initCapabilities();
        initToolBar();
        initialObjectsAndViews();
        initListenerAndEvent();
        receiveLink();
    }

    private void receiveLink(){
        Uri data = getIntent().getData();
        if (data != null) {
            receivedUrl = data.toString();
            String regex = "("+getString(R.string.URL_SHOP) +"/" +getString(R.string.URL_ADDRESS_PRODUCT_DIR)+")" + "(.*)";
            if(receivedUrl.matches(regex)){
                initLinkChecker();
            }else {
                frameProductNotFound.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initLinkChecker(){
        NetworkFunctions networkFunctions = new NetworkFunctions(getApplicationContext());
        if (networkFunctions.isOnline()) {
            new getProductId().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            showNoInternet();
        }
    }

    private void initCapabilities(){
        urlFunction = new HttpUrlFunction(getApplicationContext());
        urlFunction.enableHttpCaching();
    }

    private void initToolBar() {
        tfSans = Typeface.createFromAsset(getAssets(), getString(R.string.FONT_IRAN_SANS));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(LanguageFunctions.isRtlLanguage()){
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_forward);
        }
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setTypeface(tfSans);
        toolbarTitle.setText(getString(R.string.ACTIVITY_RECEIVE_LINK_TOOLBAR_TITLE));
        toolbarTitle.setTextSize(24);
        RtlSupport rtlSupport = new RtlSupport(getApplicationContext());
        rtlSupport.changeViewDirection(toolbarTitle);
    }

    private void initialObjectsAndViews() {

        frameNoInternet = (FrameLayout) findViewById(R.id.frameNoInternet);
        btnRetry = (AppCompatButton) findViewById(R.id.btnRetry);
        frameCircularProgress = (FrameLayout) findViewById(R.id.frameCircularProgress);
        frameProductNotFound  = (FrameLayout) findViewById(R.id.frameProductNotFound);
    }

    private void initListenerAndEvent() {

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissNoInternet();
                showCircularProgress();
                initLinkChecker();
            }
        });
    }

    private class getProductId extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                return new JsonReceiveLink().getProductLinkId(ActivityReceiveLink.this,receivedUrl);
            } catch (Exception e) {
                Crashlytics.logException(e);
                return "-1";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equals("-1")) {
                showNoInternet();
                return;
            }else if(result.equals("0")){
                frameProductNotFound.setVisibility(View.VISIBLE);
            }else{
                Intent intent = new Intent(ActivityReceiveLink.this, ActivityProduct.class);
                intent.putExtra("productId", result);
                startActivity(intent);
                finish();
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
        frameNoInternet.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        urlFunction.flushHttpCache();
    }


}
