package ir.highteam.ecommercekelar.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import ir.highteam.ecommercekelar.adapter.AdapterBasketList;
import ir.highteam.ecommercekelar.bundle.BundleBasket;
import ir.highteam.ecommercekelar.bundle.BundleBasketNotExist;
import ir.highteam.ecommercekelar.database.DatabaseInteract;
import ir.highteam.ecommercekelar.json.JsonBasket;
import ir.highteam.ecommercekelar.utile.LanguageFunctions;
import ir.highteam.ecommercekelar.utile.PrefsFunctions;
import ir.highteam.ecommercekelar.utile.network.HttpUrlFunction;
import ir.highteam.ecommercekelar.utile.network.NetworkFunctions;
import ir.highteam.ecommercekelar.view.CustomAlertDialog;
import ir.highteam.ecommercekelar.view.CustomToast;
import ir.highteam.ecommercekelar.view.RtlSupport;

public class ActivityPurchaseBasket extends AppCompatActivity {

    private LinearLayoutManager linearLayoutManager;
    private RecyclerView productRecycler;
    private Toolbar toolbar;
    private AdapterBasketList adapterBasketList;
    private HttpUrlFunction urlFunction;
    private TextView txtEmptyRecycler;
    private Typeface tfSans;

    private FrameLayout frameCircularProgress,frameNoInternet, btnFinalize;
    private AppCompatButton btnRetry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_basket);
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
        toolbarTitle.setText(getString(R.string.ACTIVITY_PURCHASE_BASKET_TOOLBAR_TITLE));
        toolbarTitle.setTextSize(24);
        RtlSupport rtlSupport = new RtlSupport(getApplicationContext());
        rtlSupport.changeViewDirection(toolbarTitle);
    }

    private void initObjectAndView(){
        productRecycler = (RecyclerView) findViewById(R.id.basketRecycler);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        txtEmptyRecycler = (TextView) findViewById(R.id.txtEmptyRecycler);
        txtEmptyRecycler.setTypeface(tfSans);
        TextView txtBtSheetSubmitOrder = (TextView) findViewById(R.id.txtBtSheetSubmitOrder);
        txtBtSheetSubmitOrder.setTypeface(tfSans);

        frameCircularProgress = (FrameLayout) findViewById(R.id.frameCircularProgress);
        frameNoInternet = (FrameLayout) findViewById(R.id.frameNoInternet);
        btnRetry = (AppCompatButton) findViewById(R.id.btnRetry);
        btnFinalize = (FrameLayout) findViewById(R.id.btnFinalize);
    }

    private void initListenersAndEvents(){
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityPurchaseBasket.this.finish();
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
            new getBasket().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else {
            dismissCircularProgress();
            showNoInternet();
        }

    }



    private class getBasket extends AsyncTask<Void,Void,ArrayList<BundleBasket>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<BundleBasket> doInBackground(Void... voids) {

            try {
                DatabaseInteract databaseInteract = new DatabaseInteract(ActivityPurchaseBasket.this);
                String user = new PrefsFunctions(ActivityPurchaseBasket.this).getLoginUser();
                String idList = databaseInteract.getBasketsId(user);
                if(idList.equals(""))
                    return new ArrayList<>();
                return new JsonBasket().getBaskets(ActivityPurchaseBasket.this,idList);
            }catch (Exception e){
                Crashlytics.logException(e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<BundleBasket> basketList) {
            super.onPostExecute(basketList);

            if(basketList == null){
                dismissCircularProgress();
                showNoInternet();
                return;
            }
            productRecycler.setHasFixedSize(true);
            productRecycler.setLayoutManager(linearLayoutManager);
            if(basketList.size() == 0){
                txtEmptyRecycler.setVisibility(View.VISIBLE);
            }
            adapterBasketList = new AdapterBasketList(basketList, getApplicationContext(),ActivityPurchaseBasket.this);
            productRecycler.setAdapter(adapterBasketList);
            dismissCircularProgress();
            final ArrayList<BundleBasketNotExist> notExistIds = new ArrayList<>();
            int index = 0;
            for (BundleBasket basket:basketList) {
                if(!basket.existence){
                    BundleBasketNotExist bundleBasketNotExist = new BundleBasketNotExist(basket.id,index);
                    notExistIds.add(bundleBasketNotExist);
                }
                index ++;
            }

            btnFinalize.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseInteract db = new DatabaseInteract(ActivityPurchaseBasket.this);
                    String user = new PrefsFunctions(ActivityPurchaseBasket.this).getLoginUser();
                    int result = db.getBasketCount(user);
                    if(result != 0){
                        if(notExistIds.size() == 0){
                            Intent intent = new Intent(ActivityPurchaseBasket.this,ActivitySetOrder.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }else {
                            showRemoveUnavailablesDialog(notExistIds);
                        }
                    }else {
                        new CustomToast(ActivityPurchaseBasket.this.getString( R.string.ADD_PRODUCT_FIRST), ActivityPurchaseBasket.this)
                                .showToast(false);
                    }
                }
            });
        }
    }

    private void showRemoveUnavailablesDialog(final ArrayList<BundleBasketNotExist> notExistIds){
        CustomAlertDialog customAlertDialog = new CustomAlertDialog(ActivityPurchaseBasket.this);

        AlertDialog dialog = new AlertDialog.Builder(ActivityPurchaseBasket.this)
                .setCustomTitle(customAlertDialog.getTitleText(getString(R.string.DELETE_UNAVAILABLE_PRODUCTS)))
                .setMessage(getString(R.string.SOME_PRODUCTS_UNAVAILABLE_DELETE))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.YES), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            int count = 0;
                            for (BundleBasketNotExist bundleBasketNotExist : notExistIds) {
                                bundleBasketNotExist.index -= count;
                                DatabaseInteract db = new DatabaseInteract(ActivityPurchaseBasket.this);
                                String user = new PrefsFunctions(ActivityPurchaseBasket.this).getLoginUser();
                                db.deleteBasket(bundleBasketNotExist.productId,user);
                                adapterBasketList.removeItem(bundleBasketNotExist.index);
                                count ++;
                            }
                        }catch (Exception e){
                            new CustomToast(getString(R.string.ERROR_OCCUR_TRY_AGAIN),ActivityPurchaseBasket.this).showToast(true);
                            Crashlytics.logException(e);
                        }finally {
                            notExistIds.clear();
                        }
                    }
                })
                .setNegativeButton(getString(R.string.NO), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                })
                .show();

        customAlertDialog.setDialogStyle(dialog);
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

    @Override
    protected void onResume() {
        super.onResume();
        if(!PrefsFunctions.isLoggedIn(ActivityPurchaseBasket.this)){
            finish();
        }
        refreshAdapter();
        PrefsFunctions prefs = new PrefsFunctions(ActivityPurchaseBasket.this);
        boolean purchaseState = prefs.getPurchaseStatus();
        if(purchaseState){

            prefs.putPurchaseStatus(false);
            String purchaseResult = prefs.getPurchaseResultNum();
            CustomAlertDialog customAlertDialog = new CustomAlertDialog(ActivityPurchaseBasket.this);
            AlertDialog dialog = new AlertDialog.Builder(ActivityPurchaseBasket.this)
                    .setCustomTitle(customAlertDialog.getTitleText(getString(R.string.ORDER_RESULT)))
                    .setMessage(getString(R.string.ORDER_SUCCESSFUL_ODER_NUM_EQUAL) + purchaseResult)
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.CLOSE), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    })
                    .show();

            customAlertDialog.setDialogStyle(dialog);
        }

    }

    private void refreshAdapter(){
        if(adapterBasketList != null)
        {
            DatabaseInteract databaseInteract = new DatabaseInteract(ActivityPurchaseBasket.this);
            String user = new PrefsFunctions(ActivityPurchaseBasket.this).getLoginUser();
            String idList = databaseInteract.getBasketsId(user);
            if(idList.equals("")){
                adapterBasketList.removeAllItems();
                txtEmptyRecycler.setVisibility(View.VISIBLE);
            }else {
                adapterBasketList.notifyDataSetChanged();
            }
        }
    }

    public void showEmptyListTxt(){
        txtEmptyRecycler.setVisibility(View.VISIBLE);
    }
}
