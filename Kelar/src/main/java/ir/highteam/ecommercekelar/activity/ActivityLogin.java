package ir.highteam.ecommercekelar.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.bundle.BundleLoginResult;
import ir.highteam.ecommercekelar.json.JsonLogin;
import ir.highteam.ecommercekelar.utile.PrefsFunctions;
import ir.highteam.ecommercekelar.utile.network.HttpUrlFunction;
import ir.highteam.ecommercekelar.utile.network.NetworkFunctions;
import ir.highteam.ecommercekelar.view.CustomToast;

public class ActivityLogin extends AppCompatActivity implements View.OnClickListener {
    private TextView btnRegister,btnForgetPassword;
    private AppCompatButton btnSignIn;
    private EditText edtUserName,edtPassWord;
    private ProgressDialog progress;

    private HttpUrlFunction urlFunction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        initCapabilities();
        setFinishOnTouchOutside(false);
        initViewsAndObject();
        initListenerAndEvent();
    }

    private void initChecker(){
        NetworkFunctions networkFunctions = new NetworkFunctions(getApplicationContext());
        if(networkFunctions.isOnline()){
            if(edtUserName.getText().toString().equals("") || edtPassWord.getText().toString().equals(""))
            {
                new CustomToast(getString(R.string.FILL_ALL_FIELDS),ActivityLogin.this)
                        .showToast(false);
            }else {
                new checkLogin().execute();
            }
        }else {
            new CustomToast(getString(R.string.ENSURE_INTERNET_CONNECTION_AND_TRY_AGIAN),ActivityLogin.this)
                    .showToast(true);
        }
    }

    private void initCapabilities(){
        urlFunction = new HttpUrlFunction(getApplicationContext());
        urlFunction.enableHttpCaching();
    }

    private  void initViewsAndObject(){
        Typeface tfSans = Typeface.createFromAsset(getAssets(), getString(R.string.FONT_IRAN_SANS));
        btnRegister = (TextView) findViewById(R.id.btnRegister);
        btnForgetPassword = (TextView) findViewById(R.id.btnForgetPassword);
        btnSignIn = (AppCompatButton) findViewById(R.id.btnSignIn);
        edtPassWord = (EditText) findViewById(R.id.edtPassword);
        edtUserName = (EditText) findViewById(R.id.edtUsername);
        TextInputLayout txtInputUserName = (TextInputLayout) findViewById(R.id.txtInputUserName);
        TextInputLayout txtInputPassWord = (TextInputLayout) findViewById(R.id.txtInputPassword);
        txtInputPassWord.setTypeface(tfSans);
        txtInputUserName.setTypeface(tfSans);
        btnRegister.setTypeface(tfSans);
        btnForgetPassword.setTypeface(tfSans);
        btnSignIn.setTypeface(tfSans);
    }

    private void initListenerAndEvent(){
        btnRegister.setOnClickListener(this);
        btnForgetPassword.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        urlFunction.flushHttpCache();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSignIn :
                initChecker();
                break;
            case R.id.btnRegister :
                goToRegisterPage();
                break;
            case R.id.btnForgetPassword :
                goToForgetPass();
                break;
        }
    }

    private void goToRegisterPage(){
        Intent goToRegister = new Intent(ActivityLogin.this,ActivitySignUp.class);
        startActivity(goToRegister);
        finish();
    }

    private void goToForgetPass(){
        //Go to forgetPass
    }

    private class checkLogin extends AsyncTask<Void,Void,BundleLoginResult> {

        String userName,passWord;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = ProgressDialog.show(ActivityLogin.this, getString(R.string.PLEASE_WAIT),getString(R.string.SENDING_INFO), true);
            userName = edtUserName.getText().toString();
            passWord = edtPassWord.getText().toString();
        }

        @Override
        protected BundleLoginResult doInBackground(Void... voids) {

            try {
                JsonLogin login = new JsonLogin();
                return login.checkLogin(ActivityLogin.this,userName,passWord);
            }catch (Exception e){
                Crashlytics.logException(e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(BundleLoginResult result) {
            super.onPostExecute(result);
            progress.dismiss();
            if(result == null){
                new CustomToast(ActivityLogin.this.getString( R.string.ERROR_OCCUR_TRY_AGAIN), ActivityLogin.this)
                        .showToast(true);
            }else {

                if(!result.status){
                    new CustomToast(ActivityLogin.this.getString(R.string.WRONG_USER_OR_PASSWORD),ActivityLogin.this)
                            .showToast(true);
                }else {
                    new PrefsFunctions(ActivityLogin.this)
                            .saveLoginInfo(result.firstName + " " + result.lastName ,result.email,userName,passWord,result.token);
                    new CustomToast(ActivityLogin.this.getString(R.string.LOGIN_SUCCESSFUL),ActivityLogin.this)
                            .showToast(true);
                    finish();
                }
            }
        }
    }
}
