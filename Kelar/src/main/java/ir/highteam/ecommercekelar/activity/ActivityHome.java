package ir.highteam.ecommercekelar.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import io.fabric.sdk.android.Fabric;
import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.adapter.AdapterMetroProducts;
import ir.highteam.ecommercekelar.adapter.AdapterRootCategoryList;
import ir.highteam.ecommercekelar.bundle.BundleLocation;
import ir.highteam.ecommercekelar.bundle.BundleMetro;
import ir.highteam.ecommercekelar.bundle.BundleParcelableUpdate;
import ir.highteam.ecommercekelar.bundle.home.BundleHead;
import ir.highteam.ecommercekelar.bundle.home.BundleHomeCategory;
import ir.highteam.ecommercekelar.bundle.home.BundleHomeSlider;
import ir.highteam.ecommercekelar.database.DatabaseInteract;
import ir.highteam.ecommercekelar.downloadmanager.DownloadApkUpdate;
import ir.highteam.ecommercekelar.json.JsonCheckForUpdate;
import ir.highteam.ecommercekelar.json.JsonHead;
import ir.highteam.ecommercekelar.json.JsonHomeCategory;
import ir.highteam.ecommercekelar.json.JsonHomeSlider;
import ir.highteam.ecommercekelar.json.JsonMetro;
import ir.highteam.ecommercekelar.utile.MetroItem;
import ir.highteam.ecommercekelar.utile.PrefsFunctions;
import ir.highteam.ecommercekelar.utile.SendToFunctions;
import ir.highteam.ecommercekelar.utile.TelephonyFunctions;
import ir.highteam.ecommercekelar.utile.network.GoogleMapsFunctions;
import ir.highteam.ecommercekelar.utile.network.HttpUrlFunction;
import ir.highteam.ecommercekelar.utile.network.NetworkFunctions;
import ir.highteam.ecommercekelar.view.CustomAlertDialog;
import ir.highteam.ecommercekelar.view.CustomToast;
import ir.highteam.ecommercekelar.view.CustomTypefaceSpan;
import ir.highteam.ecommercekelar.view.PurchaseIconCompat;
import ir.highteam.ecommercekelar.view.RtlSupport;

public class ActivityHome extends AppCompatActivity {

    private SliderLayout productSlider;
    private DrawerLayout mDrawerLayout;
    private RecyclerView tabRecycler;
    private AdapterRootCategoryList adapterRootCategoryList;
    private Typeface tfSans;
    private NavigationView navigationView;
    private TextView txtAboveNavigation,txtBelowNavigation;
    private View headerView;

    private byte horizontalFrameCounter = 0;
    private byte splashCounter = 0;
    private FrameLayout frameNoInternet, splashFrame, horizontalProgress;
    private HttpUrlFunction urlFunctions;
    private AppCompatButton btnRetry;
    private BundleParcelableUpdate upBundle;
    private LinearLayout metroLayout;
    private boolean doubleBackPress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_home);

        initCapabilities();
        initToolBar();
        initialObjectsAndViews();
        initListenerAndEvent();

        initUpdateChecker();
    }

    private void initUpdateChecker() {

        NetworkFunctions networkFunctions = new NetworkFunctions(getApplicationContext());
        if (networkFunctions.isOnline()) {
            new getUpdate().execute();
        } else {
            showNoInternet();
        }
    }

    private void initCapabilities() {
        urlFunctions = new HttpUrlFunction(getApplicationContext());
        urlFunctions.enableHttpCaching();
    }

    private void initSliderAndCategories() {
        //running these two tasks concurrently
        new getSliders().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new getHomeCategories().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void initializeMetroRecycler(ArrayList<BundleHead> heads) {
        for (BundleHead head :
                heads) {
            MetroItem headItem = new MetroItem();
            headItem.setMetroEnumAndUrl(getApplicationContext(),MetroItem.MetroEnum.empty);
            headItem.setMetroUrl(getString(R.string.URL_API_categoryProduct) + "/" + head.id);
            headItem.setPersianTitle(head.title);
            headItem.setMetroName(head.id);
            new getMetro().execute(headItem);
        }
        MetroItem metroEnumTopViewed = new MetroItem();
        metroEnumTopViewed.setMetroEnumAndUrl(getApplicationContext(), MetroItem.MetroEnum.topViewed);
        new getMetro().execute(metroEnumTopViewed);
//        metroEnum = MetroEnum.topPurchased;
//        MetroItem itemTopPurchased = new MetroItem();
//        itemTopPurchased.setMetroEnumAndUrl(getApplicationContext(), metroEnum);
//        new getMetro().execute(itemTopPurchased);
//        metroEnum = MetroEnum.mostPopular;
//        MetroItem itemMostPopular = new MetroItem();
//        itemMostPopular.setMetroEnumAndUrl(getApplicationContext(), metroEnum);
//        new getMetro().execute(itemMostPopular);
//        metroEnum = MetroEnum.topViewed;
//        MetroItem itemTopViewed = new MetroItem();
//        itemTopViewed.setMetroEnumAndUrl(getApplicationContext(), metroEnum);
//        new getMetro().execute(itemTopViewed);
//        metroEnum = MetroEnum.recentlyAdded;
//        MetroItem itemRecentlyAdded = new MetroItem();
//        itemRecentlyAdded.setMetroEnumAndUrl(getApplicationContext(), metroEnum);
//        new getMetro().execute(itemRecentlyAdded);
    }

    private int getDpSize(int sizeInPixel) {
        float d = getResources().getDisplayMetrics().density;
        int margin = (int) (sizeInPixel * d);
        return margin;
    }

    private void initialObjectsAndViews() {
        tabRecycler = (RecyclerView) findViewById(R.id.tabRecycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        tabRecycler.setHasFixedSize(true);
        tabRecycler.setLayoutManager(linearLayoutManager);
        productSlider = (SliderLayout) findViewById(R.id.slider);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        frameNoInternet = (FrameLayout) findViewById(R.id.frameNoInternet);
        btnRetry = (AppCompatButton) findViewById(R.id.btnRetry);
        splashFrame = (FrameLayout) findViewById(R.id.splashFrame);
        horizontalProgress = (FrameLayout) findViewById(R.id.frameHorizontalProgress);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        metroLayout = (LinearLayout) findViewById(R.id.linearRowLayout);

        if (navigationView != null) {
            headerView = navigationView.getHeaderView(0);
            txtAboveNavigation =(TextView) headerView.findViewById(R.id.txtAboveHeader);
            txtBelowNavigation = (TextView) headerView.findViewById(R.id.txtBelowHeader);
            txtAboveNavigation.setTypeface(tfSans);
        }

        initNavigationDrawer();
        changeDrawerFont();
    }


    private void changeDrawerFont() {
        Menu m = navigationView.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }
            applyFontToMenuItem(mi);
        }
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), getString(R.string.FONT_IRAN_SANS));
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    private void initListenerAndEvent() {
        headerView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!PrefsFunctions.isLoggedIn(ActivityHome.this)) {
                    mDrawerLayout.closeDrawers();
                    Intent intent = new Intent(ActivityHome.this, ActivityLogin.class);
                    startActivity(intent);
                }
            }
        });

        btnRetry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if( metroLayout.getChildCount() > 0){
                    metroLayout.post(new Runnable(){
                        @Override
                        public void run()
                        {
                            metroLayout.removeAllViews();
                        }
                    });
                }
                dismissNoInternet();
                showSplashScreen();
                showHorizontalProgress();
                initUpdateChecker();
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_favorite:
                        Intent favorite = new Intent(ActivityHome.this, ActivityFavorite.class);
                        startActivity(favorite);
                        break;
                    case R.id.action_call:
                        TelephonyFunctions call = new TelephonyFunctions(ActivityHome.this);
                        call.makeCall(getResources().getString(R.string.CENTRAL_PHONE));
                        break;
                    case R.id.action_about_us:
                        Intent about = new Intent(ActivityHome.this,ActivityAbout.class);
                        startActivity(about);
                        break;
                    case R.id.action_contact_us :
                        Intent contact = new Intent(ActivityHome.this,ActivityContactUs.class);
                        startActivity(contact);
                        break;
                    case R.id.action_basket:
                        Intent basket = new Intent(ActivityHome.this, ActivityPurchaseBasket.class);
                        startActivity(basket);
                        break;
                    case R.id.action_bot:
                        goToTelegramBot();
                        break;
                    case R.id.action_email:
                        sendEmail();
                        break;
                    case R.id.action_map:
                        goToLocation();
                        break;
                    case R.id.action_web:
                        goToWeb();
                        break;
                    case R.id.action_kelar_tv:
                        goToAparat();
                        break;
                    case R.id.action_agency :
                        Intent agency = new Intent(ActivityHome.this,ActivityAgencyList.class);
                        startActivity(agency);
                        break;
                    case R.id.action_projects :
                        Intent project = new Intent(ActivityHome.this,ActivityProjectList.class);
                        startActivity(project);
                        break;
                    case R.id.action_order :
                        Intent order = new Intent(ActivityHome.this, ActivityOrderList.class);
                        startActivity(order);
                        break;
                    case R.id.action_logout :
                        logOut();
                        break;
                }
                mDrawerLayout.closeDrawers();
                return false;
            }
        });
    }

    private void logOut(){
        CustomAlertDialog customAlertDialog = new CustomAlertDialog(ActivityHome.this);

        AlertDialog dialog = new AlertDialog.Builder(ActivityHome.this)
                .setCustomTitle(customAlertDialog.getTitleText(getString(R.string.LOG_OUT)))
                .setMessage(getString(R.string.DO_YOU_WANNA_LOG_OUT))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.YES), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        final ProgressDialog progress = ProgressDialog.show(ActivityHome.this, getString(R.string.PLEASE_WAIT),getString(R.string.LOGGING_OUT), true);
                        progress.show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                new PrefsFunctions(ActivityHome.this).putLoginStatus(false);
                                invalidateOptionsMenu();
                                initNavigationDrawer();
                                new CustomToast(getString(R.string.SUCCESSFUL_LOG_OUT),ActivityHome.this).showToast(true);
                                progress.dismiss();
                            }
                        }, 2000);
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

    private void goToLocation(){
        BundleLocation locationkelar = new BundleLocation();
        locationkelar.label = getResources().getString(R.string.GOOGLE_MAP_LABEL);
        locationkelar.latitude = Double.parseDouble(getResources().getString(R.string.GOOGLE_MAP_LATITUDE));
        locationkelar.longitude = Double.parseDouble(getResources().getString(R.string.GOOGLE_MAP_LONGITUDE));
        GoogleMapsFunctions mapsFunctions = new GoogleMapsFunctions(ActivityHome.this);
        mapsFunctions.goToDefaultWithPin(locationkelar);
    }

    private void sendEmail(){
        SendToFunctions sendToFunctions = new SendToFunctions(ActivityHome.this);
        sendToFunctions.sendToEmail(getString(R.string.EMAIL_ADDRESS),getString(R.string.EMAIL_SUBJECT));
    }

    private void goToWeb() {
        SendToFunctions sendToFunctions = new SendToFunctions(ActivityHome.this);
        sendToFunctions.sendToCustomTab(getResources().getString(R.string.URL_SHOP));
    }

    private void goToTelegramBot() {
        SendToFunctions sendToFunctions = new SendToFunctions(ActivityHome.this);
        sendToFunctions.sendToTelegramBot(getResources().getString(R.string.TELEGRAM_BOT));
    }

    private void goToAparat(){
        Intent intent = new Intent(ActivityHome.this, ActivityKelarTV.class);
        startActivity(intent);
    }


    private void initToolBar() {
        tfSans = Typeface.createFromAsset(getAssets(), getString(R.string.FONT_IRAN_SANS));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setTypeface(tfSans);
        toolbarTitle.setText(getResources().getString(R.string.app_name));
        toolbarTitle.setTextSize(24);
        RtlSupport rtlSupport = new RtlSupport(getApplicationContext());
        rtlSupport.changeViewDirection(toolbarTitle);
    }

    private void initNavigationDrawer(){
        if(PrefsFunctions.isLoggedIn(ActivityHome.this)){
            PrefsFunctions prefsFunctions = new PrefsFunctions(ActivityHome.this);
            txtAboveNavigation.setVisibility(View.VISIBLE);
            txtAboveNavigation.setText(prefsFunctions.getLoginFullName());
            txtBelowNavigation.setText(prefsFunctions.getLoginEmail());
            navDrawerLoginItemsVisibility(true);
        }else {
            txtAboveNavigation.setText("");
            txtAboveNavigation.setVisibility(View.INVISIBLE);
            txtBelowNavigation.setText(getString(R.string.LOGIN_TO_USER_ACCOUNT));
            navDrawerLoginItemsVisibility(false);
        }
    }

    private void navDrawerLoginItemsVisibility(boolean visibility){
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.action_basket).setVisible(visibility);
        nav_Menu.findItem(R.id.action_order).setVisible(visibility);
        nav_Menu.findItem(R.id.action_logout).setVisible(visibility);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home_toolbar, menu);
        if(PrefsFunctions.isLoggedIn(ActivityHome.this)){
            DatabaseInteract db = new DatabaseInteract(getApplicationContext());
            String user = new PrefsFunctions(getApplicationContext()).getLoginUser();
            int purchaseCount = db.getBasketCount(user);
            PurchaseIconCompat purchaseIconCompat = new PurchaseIconCompat(getApplicationContext());
            MenuItem basketIcon = menu.findItem(R.id.action_shop);
            basketIcon.setVisible(true);
            basketIcon.setIcon(purchaseIconCompat.buildPurchaseCounterDrawable(purchaseCount, R.drawable.ic_shopping_cart_white_24dp));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            Intent intent = new Intent(getApplicationContext(), ActivitySearch.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            return true;
        }

        if (id == R.id.action_shop) {
            Intent intent = new Intent(getApplicationContext(), ActivityPurchaseBasket.class);
            startActivity(intent);
            return true;
        }

        if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class getUpdate extends AsyncTask<Void, Void, BundleParcelableUpdate> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected BundleParcelableUpdate doInBackground(Void... voids) {
            try {
                return new JsonCheckForUpdate().CheckForUpdate(ActivityHome.this);
            } catch (Exception e) {
                Crashlytics.logException(e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(BundleParcelableUpdate upBundle) {
            super.onPostExecute(upBundle);
            ActivityHome.this.upBundle = upBundle;
            try {

                ImageView imgNoInternet = (ImageView) findViewById(R.id.imgNoInternet);
                TextView txtNoInternet = (TextView) findViewById(R.id.txtNoInternet);

                switch (upBundle.getTitle()) {

                    case "no":
                        new getHead().execute();
                        break;
                    case "update":
                        prepareUpdate();
                        new getHead().execute();
                        break;
                    case "urgent":
                        prepareUrgent();
                        dismissSplashScreen();
                        //
                        //show urgent frame layout here
                        imgNoInternet.setImageResource(R.drawable.urgent);
                        txtNoInternet.setText(getString(R.string.URGENT_FRAME_TEXT));
                        showNoInternet();
                        break;
                    case "lock":
                        dismissSplashScreen();
                        //
                        //show lock screen frame here
                        imgNoInternet.setImageResource(R.drawable.oops);
                        txtNoInternet.setText(getString(R.string.ERROR_OCCUR));
                        showNoInternet();
                        break;
                }
            } catch (Exception e) {
                showNoInternet();
                Crashlytics.logException(e);
            }
        }
    }

    private class getHead extends AsyncTask<Void, Void, ArrayList<BundleHead>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<BundleHead> doInBackground(Void... voids) {
            try {
                return new JsonHead().getHead(ActivityHome.this);
            } catch (Exception e) {
                Crashlytics.logException(e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<BundleHead> result) {
            super.onPostExecute(result);
            if (result == null) {
                showNoInternet();
                return;
            }

            initSliderAndCategories();
            initializeMetroRecycler(result);
        }
    }

    private class getSliders extends AsyncTask<Void, Void, ArrayList<BundleHomeSlider>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<BundleHomeSlider> doInBackground(Void... voids) {
            try {
                return new JsonHomeSlider().getHomeSlider(ActivityHome.this);
            } catch (Exception e) {
                Crashlytics.logException(e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<BundleHomeSlider> homeSliders) {
            super.onPostExecute(homeSliders);
            if (homeSliders == null) {
                showNoInternet();
                return;
            }
            HashMap<String, String> file_maps = new HashMap<>();
            for (int i = 0; i < homeSliders.size(); i++) {

                file_maps.put(homeSliders.get(i).id, homeSliders.get(i).pic);
            }

            for (String productId : file_maps.keySet()) {
                DefaultSliderView textSliderView = new DefaultSliderView(getApplicationContext());
                textSliderView
                        .description(productId)
                        .image(file_maps.get(productId))
                        .setScaleType(BaseSliderView.ScaleType.CenterCrop);

                textSliderView.bundle(new Bundle());
                textSliderView.getBundle()
                        .putString("productId", productId);
                textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                    @Override
                    public void onSliderClick(BaseSliderView slider) {

//                        Intent intent = new Intent(ActivityHome.this, ActivityProduct.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent.putExtra("productId", slider.getBundle().getString("productId"));
//                        startActivity(intent);

//                        Toast.makeText(ActivityHome.this,slider.getBundle().getString("productId"),Toast.LENGTH_SHORT).show();
                    }
                });
                productSlider.addSlider(textSliderView);
            }

            productSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
            productSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            productSlider.setCustomAnimation(new DescriptionAnimation());
            productSlider.setDuration(Long.valueOf(getString(R.string.HOME_SLIDER_DURATION)));

            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            float height = metrics.widthPixels / Float.valueOf(getString(R.string.HOME_SLIDER_RATIO));
            int rounded = Math.round(height);
            productSlider.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,rounded));

            splashCounter++;
            horizontalFrameCounter++;
            if (horizontalFrameCounter == 7) {
                dismissHorizontalProgress();
            }
        }
    }

    private class getHomeCategories extends AsyncTask<Void, Void, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            try {
                ArrayList<BundleHomeCategory> homeCategories = new JsonHomeCategory().getHomeCategory(ActivityHome.this);
                adapterRootCategoryList = new AdapterRootCategoryList(homeCategories, getApplicationContext());
            } catch (Exception e) {
                Crashlytics.logException(e);
                return -1;
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if (result == -1) {
                showNoInternet();
                return;
            }
            tabRecycler.setAdapter(adapterRootCategoryList);
            tabRecycler.getLayoutParams().height = getDpSize(40);

            splashCounter++;
            horizontalFrameCounter++;
            if (horizontalFrameCounter == 7) {
                dismissHorizontalProgress();
            }
        }
    }

    private class getMetro extends AsyncTask<MetroItem, Void, ArrayList<BundleMetro>> {
        private String moreTag;

        private String persianTitle;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<BundleMetro> doInBackground(MetroItem... MetroItem) {
            try {
                this.moreTag = MetroItem[0].getMetroName();
                this.persianTitle = MetroItem[0].getPersianTitle();
                return new JsonMetro().getMetro(ActivityHome.this,MetroItem[0].getMetroUrl());
            } catch (Exception e) {
                Crashlytics.logException(e);
                return null;
            }

        }

        @Override
        protected void onPostExecute(ArrayList<BundleMetro> homeMetros) {
            super.onPostExecute(homeMetros);

            if (homeMetros == null) {
                showNoInternet();
                return;
            }

            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View metroItem = layoutInflater.inflate(R.layout.category_item_top, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.TOP;
            metroItem.setLayoutParams(params);
            TextView txtCategoryTitle = (TextView) metroItem.findViewById(R.id.txtCateguriTitle);
            final AppCompatButton btnCategoryMore = (AppCompatButton) metroItem.findViewById(R.id.btnMore);
            txtCategoryTitle.setTypeface(tfSans);
            txtCategoryTitle.setText(persianTitle);
            btnCategoryMore.setTag(moreTag);
            btnCategoryMore.setTypeface(tfSans);
            metroLayout.addView(metroItem);

            btnCategoryMore.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ActivityHome.this, ActivityProductList.class);
                    intent.putExtra("moreType", btnCategoryMore.getTag().toString());
                    intent.putExtra("toolbarTitle",persianTitle);
                    String regex = "[0-9]+";
                    if(moreTag.matches(regex))
                    {
                        intent.putExtra("apiMoreUrl",getString(R.string.URL_API_categoryProduct));
                    }else {
                        intent.putExtra("apiMoreUrl", getString(R.string.URL_API_getMore));
                    }
                    startActivity(intent);
                }
            });

            RecyclerView itemRowRecycler = (RecyclerView) metroItem.findViewById(R.id.rowRecycler);
            LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
            itemRowRecycler.setHasFixedSize(true);
            itemRowRecycler.setLayoutManager(linearLayoutManager1);
            AdapterMetroProducts adapterMetroProducts = new AdapterMetroProducts(homeMetros, ActivityHome.this, moreTag);
            itemRowRecycler.setAdapter(adapterMetroProducts);

            horizontalFrameCounter++;
            if (horizontalFrameCounter == 7) {
                dismissHorizontalProgress();
            }
            if (splashCounter == 2) {
                dismissSplashScreen();
            }

        }

    }

    private void prepareUpdate(){
        final PrefsFunctions prefs = new PrefsFunctions(ActivityHome.this);
        int savedVersion = prefs.getApkUpdateVersion();
        if(upBundle.getVersion() > savedVersion){

            CustomAlertDialog customAlertDialog = new CustomAlertDialog(ActivityHome.this);

            AlertDialog dialog = new AlertDialog.Builder(ActivityHome.this)
                    .setCustomTitle(customAlertDialog.getTitleText(getString(R.string.UPDATE)))
                    .setMessage(getString(R.string.UPDATE_AVAILABLE_DO_YOU_WANT))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.YES), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (Build.VERSION.SDK_INT >= 23) {
                                grantWriteExternalStoragePermission();
                            }else{
                                downloadApk();
                            }

                        }
                    })
                    .setNegativeButton(getString(R.string.NO), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    })
                    .setNeutralButton(getString(R.string.NEVER_SHOW_AGAIN), new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {
                            prefs.putApkUpdateVersion(upBundle.getVersion());
                            dialog.dismiss();
                        }
                    })
                    .show();

            customAlertDialog.setDialogStyle(dialog);

        }
    }

    private void prepareUrgent(){
        CustomAlertDialog customAlertDialog = new CustomAlertDialog(ActivityHome.this);

        AlertDialog dialog = new AlertDialog.Builder(ActivityHome.this)
                .setCustomTitle(customAlertDialog.getTitleText(getString(R.string.UPDATE)))
                .setMessage(getString(R.string.UPDATE_AVAILABLE_DO_YOU_WANT))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.YES), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (Build.VERSION.SDK_INT >= 23) {
                            grantWriteExternalStoragePermission();
                        }else{
                            downloadApk();
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

    private void downloadApk(){
        showChangeLog(upBundle.getNote());
        DownloadApkUpdate downloadApk = new DownloadApkUpdate(ActivityHome.this, ActivityHome.this);
        downloadApk.StartDownload(upBundle.getUrlAddress(), upBundle.getFileName(), upBundle.getFileSizeByte());
    }

    private void grantWriteExternalStoragePermission(){

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(ActivityHome.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){

                ActivityCompat.requestPermissions(ActivityHome.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
        }
        else {
            downloadApk();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    downloadApk();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    if (Locale.getDefault().getISO3Language().equals("fas")
                        || Locale.getDefault().getISO3Language().equals("per")
                        || Locale.getDefault().getISO3Language().equals("fa")
                        || Locale.getDefault().getISO3Language().equals("farsi")) {

                        CustomAlertDialog customAlertDialog = new CustomAlertDialog(ActivityHome.this);

                        AlertDialog dialog = new AlertDialog.Builder(ActivityHome.this)
                                .setCustomTitle(customAlertDialog.getTitleText(getString(R.string.GUIDE)))
                                .setMessage(getString(R.string.READ_EXTERNAL_STORAGE_GUIDE_PERSIAN))
                                .setCancelable(false)
                                .setPositiveButton(getString(R.string.SETTINGS), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        intent.setData(Uri.parse("package:" + getPackageName()));
                                        startActivity(intent);

                                    }
                                })
                                .show();

                        customAlertDialog.setDialogStyle(dialog);
                    }
                    else{
                        CustomAlertDialog customAlertDialog = new CustomAlertDialog(ActivityHome.this);

                        AlertDialog dialog = new AlertDialog.Builder(ActivityHome.this)
                                .setCustomTitle(customAlertDialog.getTitleText(getString(R.string.GUIDE)))
                                .setCancelable(false)
                                .setMessage(getString(R.string.READ_EXTERNAL_STORAGE_GUIDE_ENGLISH))
                                .setPositiveButton(getString(R.string.SETTINGS), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        intent.setData(Uri.parse("package:" + getPackageName()));
                                        startActivity(intent);

                                    }
                                })
                                .show();

                        customAlertDialog.setDialogStyle(dialog);
                    }

                }

            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void showChangeLog(String text){
        CustomAlertDialog customAlertDialog = new CustomAlertDialog(ActivityHome.this);

        AlertDialog dialog = new AlertDialog.Builder(ActivityHome.this)
                .setCustomTitle(customAlertDialog.getTitleText(getString(R.string.NEW_VERSION_CHANGES)))
                .setMessage(text)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.CLOSE), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                })
                .show();

        customAlertDialog.setDialogStyle(dialog);
    }

    private void dismissSplashScreen() {

        if (splashFrame.getVisibility() == View.VISIBLE) {
            TranslateAnimation animate = new TranslateAnimation(0, splashFrame.getWidth(), 0, 0);
            animate.setDuration(500);
            animate.setFillAfter(true);
            splashFrame.startAnimation(animate);
            splashFrame.setVisibility(View.GONE);
            splashFrame.setClickable(false);
        }
    }

    private void showSplashScreen() {
        splashFrame.clearAnimation();
        splashFrame.setVisibility(View.VISIBLE);
    }

    private void dismissHorizontalProgress() {
        if (horizontalProgress.getVisibility() == View.VISIBLE)
            horizontalProgress.setVisibility(View.GONE);
    }

    private void showHorizontalProgress() {
        horizontalProgress.setVisibility(View.VISIBLE);
    }

    private void showNoInternet() {
        if (splashFrame.getVisibility() == View.VISIBLE)
            splashFrame.setVisibility(View.GONE);
        if (horizontalProgress.getVisibility() == View.VISIBLE)
            horizontalProgress.setVisibility(View.GONE);
        horizontalFrameCounter = 0;
        splashCounter = 0;
        frameNoInternet.setVisibility(View.VISIBLE);
    }

    private void dismissNoInternet() {
        frameNoInternet.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
        initNavigationDrawer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        urlFunctions.flushHttpCache();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackPress) {
            super.onBackPressed();
            return;
        }

        this.doubleBackPress = true;
        new CustomToast(getString(R.string.CLICK_TWICE_TO_EXIT),ActivityHome.this).showToast(true);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackPress=false;
            }
        }, 2000);
    }
}