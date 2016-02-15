package ir.highteam.ecommercekelar.activity;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.adapter.AdapterAgencyList;
import ir.highteam.ecommercekelar.bundle.BundleAgency;
import ir.highteam.ecommercekelar.database.DatabaseInteract;
import ir.highteam.ecommercekelar.json.JsonAgency;
import ir.highteam.ecommercekelar.utile.LanguageFunctions;
import ir.highteam.ecommercekelar.utile.network.HttpUrlFunction;
import ir.highteam.ecommercekelar.utile.network.NetworkFunctions;
import ir.highteam.ecommercekelar.view.RtlSupport;

public class ActivityAgencyList extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private LinearLayoutManager linearLayoutManager;
    private RecyclerView agencyRecycler;

    private FrameLayout frameCircularProgress,frameNoInternet;
    private AppCompatButton btnRetry;

    private HttpUrlFunction urlFunction;
    private Toolbar toolbar;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agency_list);

        initCapabilities();

        initToolBar();
        initObjectAndView();
        initListenerAndEvents();

        initActivityContent();
    }

    private void initToolBar() {
        Typeface tfSans = Typeface.createFromAsset(getAssets(), getString(R.string.FONT_IRAN_SANS));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(LanguageFunctions.isRtlLanguage()){
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_forward);
        }
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setTypeface(tfSans);
        toolbarTitle.setText(getString(R.string.ACTIVITY_AGENCY_LIST_TOOLBAR_TITLE));
        toolbarTitle.setTextSize(24);
        RtlSupport rtlSupport = new RtlSupport(getApplicationContext());
        rtlSupport.changeViewDirection(toolbarTitle);
    }

    private void initCapabilities(){
        urlFunction = new HttpUrlFunction(getApplicationContext());
        urlFunction.enableHttpCaching();
    }

    private void initActivityContent(){
        NetworkFunctions networkFunctions = new NetworkFunctions(getApplicationContext());
        if(networkFunctions.isOnline()){
            new getAgency().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else {
            showNoInternet();
        }
    }

    private void initObjectAndView(){
        agencyRecycler = (RecyclerView) findViewById(R.id.agencyRecycler);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        btnRetry = (AppCompatButton) findViewById(R.id.btnRetry);
        frameCircularProgress = (FrameLayout) findViewById(R.id.frameCircularProgress);
        frameNoInternet = (FrameLayout) findViewById(R.id.frameNoInternet);
        progressDialog = new ProgressDialog(ActivityAgencyList.this,
                R.style.AppTheme_Dark_Dialog);
    }

    private void initListenerAndEvents(){
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissNoInternet();
                showCircularProgress();
                initActivityContent();
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityAgencyList.this.finish();
            }
        });

    }

    private void initSpinner(){
        Spinner spinnerCity = (Spinner) findViewById(R.id.spinnerAgencyCity);
        spinnerCity.setOnItemSelectedListener(this);
        DatabaseInteract db = new DatabaseInteract(ActivityAgencyList.this);

        // Spinner Drop down elements
        ArrayList<String> cities = new ArrayList<>();
        cities.add(getString(R.string.ALL_CITIES));
        ArrayList<String> temp =  db.getAllTempAgencyCities();
        for (int i = 0; i < temp.size(); i++) {
            cities.add(temp.get(i));
        }

        // Creating adapter for spinner
        ArrayAdapter<String> cityDataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cities);

        // Drop down layout style - list view with radio button
        cityDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerCity.setAdapter(cityDataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // On selecting a spinner item
        String item = adapterView.getItemAtPosition(i).toString();
        if(adapterView.getId() == R.id.spinnerAgencyCity){
            new reFetchAgency().execute(item);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private class reFetchAgency extends AsyncTask<String,Void,ArrayList<BundleAgency>> {

        String defaultSpinnerText;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            defaultSpinnerText = getString(R.string.ALL_CITIES);

            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

        @Override
        protected ArrayList<BundleAgency> doInBackground(String ... voids) {
            DatabaseInteract db = new DatabaseInteract(ActivityAgencyList.this);
            if(voids[0].equals(defaultSpinnerText)){
                return db.getAllTempAgencies();
            }else
                return db.getAllTempAgenciesWithCity(voids[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<BundleAgency> result) {
            super.onPostExecute(result);

            agencyRecycler.setHasFixedSize(true);
            agencyRecycler.setLayoutManager(linearLayoutManager);
            AdapterAgencyList adapterCommentList = new AdapterAgencyList(result, getApplicationContext());
            agencyRecycler.setAdapter(adapterCommentList);

            progressDialog.dismiss();
        }
    }

    private class getAgency extends AsyncTask<Void,Void,ArrayList<BundleAgency>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<BundleAgency> doInBackground(Void... voids) {

            try {
                Boolean result = new JsonAgency().getAgency(ActivityAgencyList.this);
                if(result)
                {
                    DatabaseInteract db = new DatabaseInteract(ActivityAgencyList.this);
                    return db.getAllTempAgencies();
                }else
                    return null;
            }catch (Exception e){
                Crashlytics.logException(e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<BundleAgency> result) {
            super.onPostExecute(result);
            if(result == null){
                showNoInternet();
                return;
            }

            initSpinner();

            agencyRecycler.setHasFixedSize(true);
            agencyRecycler.setLayoutManager(linearLayoutManager);
            AdapterAgencyList adapterCommentList = new AdapterAgencyList(result, getApplicationContext());
            agencyRecycler.setAdapter(adapterCommentList);

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
        frameCircularProgress.setVisibility(View.GONE);
        frameNoInternet.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        urlFunction.flushHttpCache();
    }

}
