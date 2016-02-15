package ir.highteam.ecommercekelar.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.adapter.AdapterMetroProducts;
import ir.highteam.ecommercekelar.bundle.BundleBasket;
import ir.highteam.ecommercekelar.bundle.BundleFavorite;
import ir.highteam.ecommercekelar.bundle.BundleMetro;
import ir.highteam.ecommercekelar.bundle.product.BundleProductInfo;
import ir.highteam.ecommercekelar.bundle.product.BundleProductSlider;
import ir.highteam.ecommercekelar.database.DatabaseInteract;
import ir.highteam.ecommercekelar.json.JsonMetro;
import ir.highteam.ecommercekelar.json.JsonProductInfo;
import ir.highteam.ecommercekelar.utile.LanguageFunctions;
import ir.highteam.ecommercekelar.utile.MetroItem;
import ir.highteam.ecommercekelar.utile.ShareFunctions;
import ir.highteam.ecommercekelar.utile.PrefsFunctions;
import ir.highteam.ecommercekelar.utile.network.HttpUrlFunction;
import ir.highteam.ecommercekelar.utile.network.NetworkFunctions;
import ir.highteam.ecommercekelar.view.CustomToast;
import ir.highteam.ecommercekelar.view.PurchaseIconCompat;
import ir.highteam.ecommercekelar.view.RtlSupport;

public class ActivityProduct extends AppCompatActivity {

    private Typeface tfSans;
    private TextView txtTitle, txtDesc, txtPurchase, txtBtsheetTitle, txtBtsheetValuePrice, txtBtsheetTotalPrice ,txtRateNum;
    //    private Spinner spinnerBsheetColor;
    private EditText edtBtsheetProductCount;
    private BottomSheetBehavior bottomSheetBehavior;
    private FrameLayout btnPurchase;
    private LinearLayout btnFinalPurchase;
    private ImageView btnComment,btnShare,btnAddFavorite;
    private AppCompatButton btnTechnicalSpecifications;
    private Toolbar toolbar;
    private RatingBar ratingBar;

    private HttpUrlFunction urlFunction;

    private String productId,productPrice,productOff,productPic,productLink;
    private FrameLayout frameCircularProgress,frameNoInternet;
    private AppCompatButton btnRetry;

    private LinearLayout activityContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        initCapabilities();

        productId = getIntent().getExtras().getString("productId");

        initToolBar();
        initialObjectsAndVies();
        initListenerAndEvent();

        initActivityContent();
    }

    private void initActivityContent(){
        NetworkFunctions networkFunctions = new NetworkFunctions(getApplicationContext());
        if(networkFunctions.isOnline()){
            new getProductInfo().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,productId);

            MetroItem item = new MetroItem();
            item.setMetroEnumAndUrl(getApplicationContext(), MetroItem.MetroEnum.relatedProduct);
            new getMetro().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,item);
        }else {
            showNoInternet();
        }
    }

    private void initCapabilities(){
        urlFunction = new HttpUrlFunction(getApplicationContext());
        urlFunction.enableHttpCaching();
    }

    private void initToolBar(){
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
        toolbarTitle.setText(getString(R.string.ACTIVITY_PRODUCT_TOOLBAR_TITLE));
        toolbarTitle.setTextSize(24);
        RtlSupport rtlSupport = new RtlSupport(getApplicationContext());
        rtlSupport.changeViewDirection(toolbarTitle);
        FrameLayout bottomSheet = (FrameLayout) findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    private void initialObjectsAndVies() {

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtDesc = (TextView) findViewById(R.id.txtDesc);
        txtPurchase = (TextView) findViewById(R.id.txtPurchase);
        txtBtsheetTitle = (TextView) findViewById(R.id.txtBtsheetTitle);
        txtBtsheetValuePrice = (TextView) findViewById(R.id.txtBtsheetValuePrice);
        txtBtsheetTotalPrice = (TextView) findViewById(R.id.txtBtsheetTotalPrice);
        txtRateNum = (TextView) findViewById(R.id.txtRateNum);
//		spinnerBsheetColor = (Spinner) findViewById(R.id.spinnerBsheetColor);
        edtBtsheetProductCount = (EditText) findViewById(R.id.edtBtsheetProductCount);
        btnPurchase = (FrameLayout) findViewById(R.id.btnPurchase);
        btnFinalPurchase = (LinearLayout) findViewById(R.id.btnFinalPurchase);
        btnComment = (ImageView) findViewById(R.id.imgComment);
        btnAddFavorite = (ImageView) findViewById(R.id.btnAddFavorite);
        btnShare = (ImageView) findViewById(R.id.btnShare);
        btnTechnicalSpecifications = (AppCompatButton) findViewById(R.id.btnTechnicalSpecifications);
        activityContent =(LinearLayout) findViewById(R.id.productContent);
        txtTitle.setTypeface(tfSans);
        txtDesc.setTypeface(tfSans);
        txtPurchase.setTypeface(tfSans);
        edtBtsheetProductCount.setTypeface(tfSans);

//        if(!PrefsFunctions.isLoggedIn(getApplicationContext())){
//            ImageView btSheetBasketIcon = (ImageView) findViewById(R.id.imgBasketBtSheetPrice);
//            View lineBtSheetPrice = (View) findViewById(R.id.lineBtSheetPrice);
//            btSheetBasketIcon.setVisibility(View.GONE);
//            lineBtSheetPrice.setVisibility(View.GONE);
//        }

        btnRetry = (AppCompatButton) findViewById(R.id.btnRetry);
        frameCircularProgress = (FrameLayout) findViewById(R.id.frameCircularProgress);
        frameNoInternet = (FrameLayout) findViewById(R.id.frameNoInternet);
    }

    private void initListenerAndEvent(){
        btnComment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityComment.class);
                startActivity(intent);
            }
        });

        btnRetry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissNoInternet();
                showCircularProgress();
                initActivityContent();
            }
        });

        btnShare.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareFunctions shareFunctions = new ShareFunctions(ActivityProduct.this);
                shareFunctions.sharePlainText(txtTitle.getText().toString() + "\nرا در " +
                        getResources().getString(R.string.app_name) + " ببین " +  "\n" + productPic + "\n\n\n" + productLink);
            }
        });
        btnAddFavorite.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                BundleFavorite bundleFavorite = new BundleFavorite();
                bundleFavorite.id = productId;
                bundleFavorite.title = txtTitle.getText().toString();
                bundleFavorite.price = productPrice;
                bundleFavorite.off = productOff;
                bundleFavorite.description = txtDesc.getText().toString();
                bundleFavorite.pic = productPic;

                DatabaseInteract databaseInteract = new DatabaseInteract(ActivityProduct.this);
                databaseInteract.addToFavorite(bundleFavorite);
            }
        });
        btnTechnicalSpecifications.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityTechnicalSpecifications.class);
                startActivity(intent);
            }
        });

        btnPurchase.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(PrefsFunctions.isLoggedIn(ActivityProduct.this)){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });

        btnFinalPurchase.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                String regex = "([1-9])(([0-9]+)?)";
                if(!edtBtsheetProductCount.getText().toString().matches(regex)) {
                    edtBtsheetProductCount.setText("1");
                    new CustomToast(ActivityProduct.this.getString(R.string.COUNT_INVALID), ActivityProduct.this)
                            .showToast(false);
                    txtBtsheetTotalPrice.setText(productPrice + " " + getString(R.string.CURRENCY_IRAN_RIAL));
                }else{
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

                    BundleBasket bundleBasket = new BundleBasket();
                    bundleBasket.id = productId;
                    bundleBasket.title = txtTitle.getText().toString();
                    bundleBasket.price = productPrice;
                    bundleBasket.off = productOff;
                    bundleBasket.description = txtDesc.getText().toString();
                    bundleBasket.pic = productPic;
                    bundleBasket.existence = false;
                    bundleBasket.color = "#ffffff";
                    bundleBasket.count = edtBtsheetProductCount.getText().toString();
                    bundleBasket.userName = new PrefsFunctions(ActivityProduct.this).getLoginUser();
                    DatabaseInteract databaseInteract = new DatabaseInteract(ActivityProduct.this);
                    databaseInteract.addToBasket(bundleBasket, ActivityProduct.this);
                }
            }
        });

        edtBtsheetProductCount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_NEXT) {
                    String regex = "([1-9])(([0-9]+)?)";
                    if(edtBtsheetProductCount.getText().toString().matches(regex)) {
                        double number = Double.parseDouble(textView.getText().toString());
                        if(!productPrice.equals("")){
                            double totalPrice = (Double.parseDouble(productPrice)) * (number);
                            txtBtsheetTotalPrice.setText(String.valueOf(new DecimalFormat("#").format(totalPrice)) + " " + getString(R.string.CURRENCY_IRAN_RIAL));
                        }
                    }else{
                        edtBtsheetProductCount.setText("1");
                        new CustomToast(ActivityProduct.this.getString(R.string.COUNT_INVALID), ActivityProduct.this)
                                .showToast(false);
                        txtBtsheetTotalPrice.setText(productPrice + " " + getString(R.string.CURRENCY_IRAN_RIAL));
                    }

                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.toggleSoftInput(0, 0);
                }
                return false;
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityProduct.this.finish();
            }
        });

        activityContent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
            }
        });
    }

    private int getScreenSize(){
        return (getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK);
    }
    private void initializeSlider(ArrayList<BundleProductSlider> sliders) {

        SliderLayout productSlider = (SliderLayout)findViewById(R.id.slider);
        HashMap<String,String> file_maps = new HashMap<>();
        for (int i = 0; i < sliders.size() ; i++) {
            int screenSize = getScreenSize();
            if (screenSize == Configuration.SCREENLAYOUT_SIZE_SMALL) {
                file_maps.put(String.valueOf(i), sliders.get(i).smallPic);
            }
            else if (screenSize == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
                file_maps.put(String.valueOf(i), sliders.get(i).mediumPic);
            }
            else if (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE) {
                file_maps.put(String.valueOf(i), sliders.get(i).largePic);
            }
            else if (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
                file_maps.put(String.valueOf(i), sliders.get(i).largePic);
            }
        }
        productPic = sliders.get(0).largePic;
        for(String name : file_maps.keySet()){
            DefaultSliderView textSliderView = new DefaultSliderView(this);
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.FitCenterCrop);

            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            productSlider.addSlider(textSliderView);
        }

        productSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        productSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        productSlider.setCustomAnimation(new DescriptionAnimation());
        productSlider.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));
        productSlider.setDuration(Long.valueOf(getString(R.string.PRODUCT_SLIDER_DURATION)));

    }

    private void initBtsheetPurchase(){
//    private void initBtsheetPurchase(String [] colorsName){
        TextView txtBtsheet1, txtBtsheet2, txtBtsheet3,txtBtsheet5;
//                TextView txtBtsheet4;

        txtBtsheet1 = (TextView) findViewById(R.id.txtBtsheet1);
        txtBtsheet2 = (TextView) findViewById(R.id.txtBtsheet2);
        txtBtsheet3 = (TextView) findViewById(R.id.txtBtsheet3);
//        txtBtsheet4 = (TextView) findViewById(R.id.txtBtsheet4);
        txtBtsheet5 = (TextView) findViewById(R.id.txtBtSheetSubmitOrder);

        txtBtsheet1.setTypeface(tfSans);
        txtBtsheet2.setTypeface(tfSans);
        txtBtsheet3.setTypeface(tfSans);
//        txtBtsheet4.setTypeface(tfSans);
        txtBtsheet5.setTypeface(tfSans);
        txtBtsheetTitle.setTypeface(tfSans);
        txtBtsheetValuePrice.setTypeface(tfSans);
        txtBtsheetTotalPrice.setTypeface(tfSans);


//        ArrayAdapter<String> spinColorAdapter = new ArrayAdapter<String>(ActivityProduct.this, android.R.layout.simple_spinner_item, colorsName);
//        spinColorAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
//        spinnerBsheetColor.setAdapter(spinColorAdapter);
    }

    private void initProductColors(String [] colorsHex) {

        LinearLayout layoutColors = (LinearLayout) findViewById(R.id.layoutColor);
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < colorsHex.length; i++) {
            View colorLayer = layoutInflater.inflate(R.layout.custom_color_view, null);
            View colorView = colorLayer.findViewById(R.id.colorView);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                colorView.setBackground(getColorView(colorsHex[i]));
            } else {
                if (Build.VERSION.SDK_INT >= 16) {
                    colorView.setBackground(getColorView(colorsHex[i]));
                }else{
                    colorView.setBackgroundDrawable(getColorView(colorsHex[i]));
                }
            }
            layoutColors.addView(colorLayer);
        }
    }


    private GradientDrawable getColorView(String colorHex){
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setColor(Color.parseColor(colorHex));
        return shape;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home_toolbar, menu);
        if(PrefsFunctions.isLoggedIn(ActivityProduct.this)){
            DatabaseInteract db = new DatabaseInteract(getApplicationContext());
            String user = new PrefsFunctions(getApplicationContext()).getLoginUser();
            int purchaseCount = db.getBasketCount(user);
            PurchaseIconCompat purchaseIconCompat = new PurchaseIconCompat(getApplicationContext());
            MenuItem menuItem = menu.findItem(R.id.action_shop);
            menuItem.setVisible(true);
            menuItem.setIcon(purchaseIconCompat.buildPurchaseCounterDrawable(purchaseCount, R.drawable.ic_shopping_cart_white_24dp));
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

        if(id == R.id.action_shop){
            Intent intent = new Intent(getApplicationContext(), ActivityPurchaseBasket.class);
            startActivity(intent);
            return true;
        }

        if (id == android.R.id.home) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class getProductInfo extends AsyncTask<String,Void,BundleProductInfo>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected BundleProductInfo doInBackground(String ...productId) {
            try{
                return new JsonProductInfo().getProductInfo(ActivityProduct.this,productId[0]);
            }catch (Exception e){
                Crashlytics.logException(e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(BundleProductInfo bundleProductInfo) {
            super.onPostExecute(bundleProductInfo);
            if(bundleProductInfo == null){
                showNoInternet();
                return;
            }
            txtTitle.setText(bundleProductInfo.title);
            txtBtsheetTitle.setText(bundleProductInfo.title);

//            ArrayList<BundleProductColor> colors = bundleProductInfo.productColors;
//            String[] colorsName = new String[colors.size()];
//            String[] colorsHex = new String[colors.size()];
//            for (int i = 0; i < colors.size(); i++) {
//                colorsName[i] = colors.get(i).name;
//                colorsHex[i] = colors.get(i).hex;
//            }
            initBtsheetPurchase();
//            initBtsheetPurchase(colorsName);
//            initProductColors(colorsHex);

            txtPurchase.setText(bundleProductInfo.price + " " + getString(R.string.CURRENCY_IRAN_RIAL));
            txtBtsheetTotalPrice.setText(bundleProductInfo.price + " " + getString(R.string.CURRENCY_IRAN_RIAL));
            txtBtsheetValuePrice.setText(bundleProductInfo.price + " " + getString(R.string.CURRENCY_IRAN_RIAL));
            if(bundleProductInfo.rate == 0)
                txtRateNum.setText(getString(R.string.NO_RATE_YET));
            else
                txtRateNum.setText(String.valueOf(bundleProductInfo.rate));
            txtDesc.setText(bundleProductInfo.description);
            ratingBar.setRating(bundleProductInfo.rate);

            productLink = bundleProductInfo.link;
            productOff = bundleProductInfo.off;
            productPrice = bundleProductInfo.price;

            try {
                initializeSlider(bundleProductInfo.productSlider);
            }catch (Exception e){
                Crashlytics.logException(e);
                showNoInternet();
                return;
            }
            dismissCircularProgress();
        }
    }

    private class getMetro extends AsyncTask<MetroItem,Void,ArrayList<BundleMetro>> {
        private String metroType;
        private String persianTitle ;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<BundleMetro> doInBackground(MetroItem... MetroItem) {

            try {
                this.metroType = MetroItem[0].getMetroName();
                this.persianTitle = MetroItem[0].getPersianTitle();
                return new JsonMetro().getMetro(ActivityProduct.this,MetroItem[0].getMetroUrl() + "/" + productId);
            }catch (Exception e){
                Crashlytics.logException(e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<BundleMetro> homeMetros) {
            super.onPostExecute(homeMetros);
            if(homeMetros == null){
                showNoInternet();
                return;
            }
            LinearLayout itemRowLayout = (LinearLayout) findViewById(R.id.linearRowLayout);

            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View categoryTopItem = layoutInflater.inflate(R.layout.category_item_top, null);
            TextView txtCategoryTitle = (TextView) categoryTopItem.findViewById(R.id.txtCateguriTitle);
            final Button btnCategoryMore = (Button) categoryTopItem.findViewById(R.id.btnMore);
            txtCategoryTitle.setTypeface(tfSans);
            txtCategoryTitle.setText(persianTitle);
            btnCategoryMore.setTag(metroType);
            btnCategoryMore.setTypeface(tfSans);
            itemRowLayout.addView(categoryTopItem);

            btnCategoryMore.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ActivityProduct.this, ActivityProductList.class);
                    intent.putExtra("moreType", btnCategoryMore.getTag().toString() + "/" + productId);
                    intent.putExtra("apiMoreUrl",getString(R.string.URL_API_shopRootAddress));
                    intent.putExtra("toolbarTitle",getString(R.string.RELATED_PRODUCTS_PERSIAN));
                    startActivity(intent);
                }
            });


            RecyclerView itemRowRecycler = (RecyclerView) categoryTopItem.findViewById(R.id.rowRecycler);


            LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
            itemRowRecycler.setHasFixedSize(true);

            itemRowRecycler.setLayoutManager(linearLayoutManager1);
            AdapterMetroProducts adapterMetroProducts = new AdapterMetroProducts(homeMetros , getApplicationContext(), metroType);
            itemRowRecycler.setAdapter(adapterMetroProducts);

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
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    @Override
    protected void onStop() {
        super.onStop();
        urlFunction.flushHttpCache();
    }
}