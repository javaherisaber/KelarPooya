package ir.highteam.ecommercekelar.activity;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import java.lang.reflect.Field;
import java.util.Locale;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.adapter.AdapterProductList;
import ir.highteam.ecommercekelar.bundle.BundleMoreProduct;
import ir.highteam.ecommercekelar.json.JsonMoreProduct;
import ir.highteam.ecommercekelar.utile.LanguageFunctions;
import ir.highteam.ecommercekelar.utile.network.HttpUrlFunction;
import ir.highteam.ecommercekelar.utile.network.NetworkFunctions;
import ir.highteam.ecommercekelar.view.SearchViewFormatter;

public class ActivitySearch extends AppCompatActivity {

    // View Objects
    //
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private SearchView searchView;
    private FrameLayout frameHorizontalProgress,frameCircularProgress,frameNoInternet;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView productRecycler;
    private TextView txtEmptyRecycler;
    private Typeface tfSans;
    private AppCompatButton btnRetry;

    // Local Variables
    //
    private int page;
    private boolean hasNext = true;
    private boolean loading = true;
    private String queryText = "";
    private AdapterProductList adapterProductList;
    private HttpUrlFunction urlFunction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initCapabilities();
        initToolbar();
        initObjectAndView();
        initListenerAndEvent();
    }

    private void initCapabilities(){
        urlFunction = new HttpUrlFunction(getApplicationContext());
        urlFunction.enableHttpCaching();
    }

	private void initDefaultProductRecycler(String queryText){
        NetworkFunctions networkFunctions = new NetworkFunctions(getApplicationContext());
        if(networkFunctions.isOnline()){
            queryText = Uri.encode(queryText);
            new getMoreProducts().execute(queryText);
        }else {
            dismissCircularProgress();
            showNoInternet();
        }

    }

    private void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tfSans = Typeface.createFromAsset(getAssets(), getString(R.string.FONT_IRAN_SANS));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(LanguageFunctions.isRtlLanguage()){
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_forward);
        }
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setTypeface(tfSans);
        toolbarTitle.setText(getString(R.string.ACTIVITY_SEARCH_TOOLBAR_TITLE));
        toolbarTitle.setTextSize(24);
//        RtlSupport rtlSupport = new RtlSupport(getApplicationContext());
//        rtlSupport.changeViewDirection(toolbarTitle);
    }

    private void initObjectAndView(){
        fab = (FloatingActionButton) findViewById(R.id.fab);
        frameHorizontalProgress = (FrameLayout) findViewById(R.id.frameHorizontalProgress);
        frameCircularProgress = (FrameLayout) findViewById(R.id.frameCircularProgress);
        frameNoInternet = (FrameLayout) findViewById(R.id.frameNoInternet);
        btnRetry = (AppCompatButton) findViewById(R.id.btnRetry);
        dismissCircularProgress();
        productRecycler = (RecyclerView) findViewById(R.id.productRecycler);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        txtEmptyRecycler = (TextView) findViewById(R.id.txtEmptyRecycler);
        txtEmptyRecycler.setTypeface(tfSans);

    }

    private void initListenerAndEvent(){
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
                dismissCircularProgress();
            }
        });

        productRecycler.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 0) //check for scroll down
                {
                    int visibleItemCount = linearLayoutManager.getChildCount();
                    int totalItemCount = linearLayoutManager.getItemCount();
                    int pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();

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
                                initDefaultProductRecycler(queryText);
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search_toolbar, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        if (null != searchView) {
            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
        }

        if(LanguageFunctions.isRtlLanguage()){

            new SearchViewFormatter()
                    .setSearchBackGroundResource(android.R.drawable.screen_background_dark_transparent)
                    .setSearchIconResource(R.drawable.ic_search, false, false) //true to icon inside edittext, false to outside
                    .setSearchTextColorResource(R.color.cardview_light_background)
                    .setSearchHintColorResource(R.color.iron)
                    .setSearchCollapsedIconResource(R.drawable.ic_arrow_forward)
                    .setSearchCloseIconResource(R.drawable.ic_close)
                    .setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS)
                    .format(searchView);
        }else {
            new SearchViewFormatter()
                    .setSearchBackGroundResource(android.R.drawable.screen_background_dark_transparent)
                    .setSearchIconResource(R.drawable.ic_search, false, false) //true to icon inside edittext, false to outside
                    .setSearchTextColorResource(R.color.cardview_light_background)
                    .setSearchHintColorResource(R.color.iron)
                    .setSearchCollapsedIconResource(R.drawable.ic_arrow_back)
                    .setSearchCloseIconResource(R.drawable.ic_close)
                    .setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS)
                    .format(searchView);
        }

        final TextView searchText = (TextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(searchText, R.drawable.cursor_color_white);
            if(LanguageFunctions.isRtlLanguage()){
                searchText.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
                searchText.setHint(getString(R.string.SEARCH));//This sets the cursor resource ID to 0 or @null which will make it visible on white background
            }else {
                searchText.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
                searchText.setHint("Search");//This sets the cursor resource ID to 0 or @null which will make it visible on white background
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
        }


        ImageView magImage = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
        magImage.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        magImage.setVisibility(View.GONE);

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                boolean isRTL = LanguageFunctions.isRtlLanguage();

                if(newText.length() == 0){
                    if(LanguageFunctions.isRtlLanguage()){
                        searchText.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                    }else {
                        searchText.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                    }
                }
                else if(newText.length() == 1) {
                    if (isRTL) {
                        searchText.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                    }
                }
                return true;
            }

            public boolean onQueryTextSubmit(String query) {

                showCircularProgress();
                if(adapterProductList != null)
                {
                    if(adapterProductList.getItemCount() != 0){
                        adapterProductList.clearItems();
                        adapterProductList.notifyDataSetChanged();
                    }
                }
                queryText = query;
                page = 1;
                initDefaultProductRecycler(query);
                return false;
            }
        };
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(queryTextListener);
        MenuItem searchMenuItem = menu.findItem( R.id.action_search );

            MenuItemCompat.setOnActionExpandListener(searchMenuItem, new MenuItemCompat.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem menuItem) {
                    return true;
                }
                @Override
                public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                    ActivitySearch.this.finish();
                    return false;
                }
            });

        MenuItemCompat.expandActionView(searchMenuItem);
        if(LanguageFunctions.isRtlLanguage()){

            new SearchViewFormatter()
                    .setSearchBackGroundResource(R.drawable.drawable_white)
                    .setSearchIconResource(R.drawable.ic_search_white_24dp, false, false) //true to icon inside edittext, false to outside
                    .setSearchTextColorResource(R.color.white)
                    .setSearchHintColorResource(R.color.iron)
                    .setSearchCollapsedIconResource(R.drawable.ic_arrow_forward)
                    .setSearchCloseIconResource(R.drawable.ic_colse)
                    .setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS)
                    .format(searchView);

        }else {

            new SearchViewFormatter()
                    .setSearchBackGroundResource(R.drawable.drawable_white)
                    .setSearchIconResource(R.drawable.ic_search_white_24dp, false, false) //true to icon inside edittext, false to outside
                    .setSearchTextColorResource(R.color.white)
                    .setSearchHintColorResource(R.color.iron)
                    .setSearchCollapsedIconResource(R.drawable.ic_arrow_back)
                    .setSearchCloseIconResource(R.drawable.ic_colse)
                    .setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS)
                    .format(searchView);
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

    private String getKeyboardLanguage() {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        InputMethodSubtype inputMethodSubtype = inputMethodManager.getCurrentInputMethodSubtype();
        if (Build.VERSION.SDK_INT >= 24) {
            return inputMethodSubtype.getLanguageTag();
        }else {
            return inputMethodSubtype.getLocale();
        }
    }

    private boolean isRTL(Locale locale) {
        final int directionality = Character.getDirectionality(locale.getDisplayName().charAt(0));
        return directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT ||
                directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            return true;
        }

        if (id == android.R.id.home) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class getMoreProducts extends AsyncTask<String,Void,BundleMoreProduct>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            txtEmptyRecycler.setVisibility(View.INVISIBLE);
        }

        @Override
        protected BundleMoreProduct doInBackground(String... query) {

            try {
                return new JsonMoreProduct().getMoreProduct(ActivitySearch.this,getString(R.string.URL_API_search),query[0],page);
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
                if (moreProducts.moreProductLists.size() == 0){
                    txtEmptyRecycler.setVisibility(View.VISIBLE);
                }
                adapterProductList = new AdapterProductList(moreProducts.moreProductLists, getApplicationContext(),ActivitySearch.this);
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
            }
            loading = true;
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

    @Override
    protected void onPause() {
        super.onPause();
        hideKeyboard();
    }

    private void hideKeyboard(){
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
