package ir.highteam.ecommercekelar.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.bundle.BundleLoginResult;
import ir.highteam.ecommercekelar.bundle.order.BundleOrderResult;
import ir.highteam.ecommercekelar.bundle.order.BundleOrderShippingInfo;
import ir.highteam.ecommercekelar.database.DatabaseInteract;
import ir.highteam.ecommercekelar.json.JsonLogin;
import ir.highteam.ecommercekelar.json.JsonSubmitOrder;
import ir.highteam.ecommercekelar.utile.LanguageFunctions;
import ir.highteam.ecommercekelar.utile.PrefsFunctions;
import ir.highteam.ecommercekelar.utile.network.HttpUrlFunction;
import ir.highteam.ecommercekelar.utile.network.NetworkFunctions;
import ir.highteam.ecommercekelar.view.CustomToast;
import ir.highteam.ecommercekelar.view.RtlSupport;

public class ActivitySetOrder extends AppCompatActivity {

    private EditText edtFirstName,edtLastName,edtAddress,edtPhoneNum;
    private FrameLayout btnOrder;
    private ProgressDialog progress;

    private HttpUrlFunction urlFunction;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initCapabilities();

        initToolBar();
        initObjectAndViews();
        initListenerAndViews();
        initEditTexts();

    }

    private void initCapabilities(){
        urlFunction = new HttpUrlFunction(getApplicationContext());
        urlFunction.enableHttpCaching();
    }

    private void initEditTexts(){
        BundleOrderShippingInfo info = new PrefsFunctions(ActivitySetOrder.this).getOrderShippingInfo();
        edtFirstName.setText(info.firstName);
        edtLastName.setText(info.lastName);
        edtAddress.setText(info.address);
        edtPhoneNum.setText(info.phone);
    }

    private void initToolBar() {
        Typeface tfSans = Typeface.createFromAsset(getAssets(), getString(R.string.FONT_IRAN_SANS));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if(LanguageFunctions.isRtlLanguage()){
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_forward);
        }
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setTypeface(tfSans);
        toolbarTitle.setText(getString(R.string.ACTIVITY_SET_ORDER_TOOLBAR_TITLE));
        toolbarTitle.setTextSize(24);
        RtlSupport rtlSupport = new RtlSupport(getApplicationContext());
        rtlSupport.changeViewDirection(toolbarTitle);
    }

    private void initObjectAndViews(){

        edtFirstName = (EditText) findViewById(R.id.edtFirstName);
        edtLastName = (EditText) findViewById(R.id.edtLastName);
        edtAddress = (EditText) findViewById(R.id.edtAddress);
        edtPhoneNum = (EditText) findViewById(R.id.edtPhoneNum);
        TextView txtOrder = (TextView) findViewById(R.id.txtBtSheetSubmitOrder);
        TextInputLayout inputAddress = (TextInputLayout) findViewById(R.id.inputAddress);
        TextInputLayout inputFirstName = (TextInputLayout) findViewById(R.id.inputFirstName);
        TextInputLayout inputPhoneNum = (TextInputLayout) findViewById(R.id.inputPhoneNum);
        TextInputLayout inputLastName = (TextInputLayout) findViewById(R.id.inputLastName);

        btnOrder = (FrameLayout) findViewById(R.id.btnOrder);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        Typeface tfSans;
        tfSans = Typeface.createFromAsset(getAssets(), getString(R.string.FONT_IRAN_SANS));

        edtFirstName.setTypeface(tfSans);
        edtLastName.setTypeface(tfSans);
        edtAddress.setTypeface(tfSans);
        edtPhoneNum.setTypeface(tfSans);
        txtOrder.setTypeface(tfSans);

        inputAddress.setTypeface(tfSans);
        inputLastName.setTypeface(tfSans);
        inputPhoneNum.setTypeface(tfSans);
        inputFirstName.setTypeface(tfSans);
    }

    private void initListenerAndViews(){

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtPhoneNum.getText().toString().equals("") || edtAddress.getText().toString().equals("")
            |edtLastName.getText().toString().equals("") || edtFirstName.getText().toString().equals("")){
                    new CustomToast(ActivitySetOrder.this.getString( R.string.FILL_ALL_FIELDS), ActivitySetOrder.this)
                            .showToast(false);
                }else {
                    BundleOrderShippingInfo info = new BundleOrderShippingInfo();
                    info.firstName = edtFirstName.getText().toString();
                    info.lastName = edtLastName.getText().toString();
                    info.address = edtAddress.getText().toString();
                    info.phone = edtPhoneNum.getText().toString();
                    new PrefsFunctions(ActivitySetOrder.this).saveOrderShippingInfo(info);

                    NetworkFunctions networkFunctions = new NetworkFunctions(getApplicationContext());
                    if(networkFunctions.isOnline()){
                        new setOrder().execute();
                    }else {
                        new CustomToast(getString(R.string.ENSURE_INTERNET_CONNECTION_AND_TRY_AGIAN),ActivitySetOrder.this)
                                .showToast(true);
                    }
                }
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySetOrder.this.finish();
            }
        });
    }


    private class setOrder extends AsyncTask<Void,Void,BundleOrderResult> {

        String firstName,lastName,address,phone;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = ProgressDialog.show(ActivitySetOrder.this, getString(R.string.PLEASE_WAIT),getString(R.string.SENDING_INFO), true);
            firstName = edtFirstName.getText().toString();
            lastName = edtLastName.getText().toString();
            address = edtAddress.getText().toString();
            phone = edtPhoneNum.getText().toString();
        }

        @Override
        protected BundleOrderResult doInBackground(Void... voids) {

            try {
                BundleOrderResult orderResult = new JsonSubmitOrder().submitOrder(ActivitySetOrder.this,firstName,lastName,address,phone);
                if(orderResult.result.status){
                    return orderResult;
                }else {
                    String user = new PrefsFunctions(ActivitySetOrder.this).getLoginUser();
                    String pass = new PrefsFunctions(ActivitySetOrder.this).getLoginPass();
                    BundleLoginResult loginResult = new JsonLogin().checkLogin(ActivitySetOrder.this,user,pass);
                    if(loginResult.status){
                        new PrefsFunctions(ActivitySetOrder.this).putLoginToken(loginResult.token);
                        return new JsonSubmitOrder().submitOrder(ActivitySetOrder.this,firstName,lastName,address,phone);
                    }else {
                        orderResult.result.message = "-1";
                        new PrefsFunctions(ActivitySetOrder.this).putLoginStatus(false);
                        return orderResult;
                    }
                }
            }catch (Exception e){
                Crashlytics.logException(e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(BundleOrderResult result) {
            super.onPostExecute(result);
            progress.dismiss();
            if(result==null){
                new CustomToast(ActivitySetOrder.this.getString( R.string.ERROR_OCCUR_TRY_AGAIN), ActivitySetOrder.this)
                        .showToast(true);
            }else if (result.result.message.equals("-1")){
                new CustomToast(getString(R.string.LOGIN_INFO_HAS_BEEN_CHANGED),ActivitySetOrder.this).showToast(true);
                finish();
            }else if(result.result.status){

                deleteAllBaskets();
                PrefsFunctions prefs = new PrefsFunctions(ActivitySetOrder.this);
                prefs.putPurchaseStatus(true);
                prefs.putPurchaseResultNum(result.orderNum);

                finish();
            }else {
                new CustomToast(ActivitySetOrder.this.getString( R.string.ERROR_OCCUR_TRY_AGAIN), ActivitySetOrder.this)
                        .showToast(true);
            }

        }
    }

    private void deleteAllBaskets(){
        DatabaseInteract db = new DatabaseInteract(ActivitySetOrder.this);
        String user = new PrefsFunctions(ActivitySetOrder.this).getLoginUser();
        db.deleteAllBaskets(user);
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

    @Override
    protected void onStop() {
        super.onStop();
        urlFunction.flushHttpCache();
    }
}
