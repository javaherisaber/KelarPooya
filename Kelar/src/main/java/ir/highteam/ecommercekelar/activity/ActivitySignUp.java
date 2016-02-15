package ir.highteam.ecommercekelar.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.crashlytics.android.Crashlytics;

import butterknife.Bind;
import butterknife.ButterKnife;
import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.bundle.BundleApiResult;
import ir.highteam.ecommercekelar.bundle.BundleRegister;
import ir.highteam.ecommercekelar.json.JsonRegister;
import ir.highteam.ecommercekelar.utile.network.NetworkFunctions;
import ir.highteam.ecommercekelar.view.CustomAlertDialog;
import ir.highteam.ecommercekelar.view.CustomToast;

public class ActivitySignUp extends AppCompatActivity {

    @Bind(R.id.input_first_name) EditText edtFirstName;
    @Bind(R.id.input_last_name) EditText edtLastName;
    @Bind(R.id.input_email) EditText edtEmail;
    //@Bind(R.id.input_mobile) EditText edtMobile;
    @Bind(R.id.input_user_name) EditText edtUserName;
    @Bind(R.id.input_password) EditText edtPassWord;
    @Bind(R.id.input_reEnterPassword) EditText edtReEnterPassWord;
    @Bind(R.id.btn_signup) Button btnSignUp;

    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
        progressDialog = new ProgressDialog(ActivitySignUp.this,
                R.style.AppTheme_Dark_Dialog);
    }

    public void signUp() {

        if (validate()) {
            NetworkFunctions networkFunctions = new NetworkFunctions(getApplicationContext());
            if(networkFunctions.isOnline()){
                new checkLogin().execute();
            }else {
                new CustomToast(getString(R.string.ENSURE_INTERNET_CONNECTION_AND_TRY_AGIAN),ActivitySignUp.this)
                        .showToast(true);
            }
        }
    }

    private class checkLogin extends AsyncTask<Void,Void,BundleApiResult> {

        BundleRegister register;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            register = new BundleRegister();
            register.firstName = edtFirstName.getText().toString();
            register.lastName = edtLastName.getText().toString();
            register.email = edtEmail.getText().toString();
            //register.phone = edtMobile.getText().toString();
            register.phone = "";
            register.user = edtUserName.getText().toString();
            register.pass = edtPassWord.getText().toString();

            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(getString(R.string.SENDING_INFO));
            progressDialog.show();
        }

        @Override
        protected BundleApiResult doInBackground(Void... voids) {

            try {
                return new JsonRegister().register(ActivitySignUp.this,register);
            }catch (Exception e){
                Crashlytics.logException(e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(BundleApiResult result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            if(result == null){
                new CustomToast(getString( R.string.ERROR_OCCUR_TRY_AGAIN), ActivitySignUp.this).showToast(true);
            }else if(result.status){
                showSuccessfulDialog();
            }else if(result.message.equals("Unknown error")){
                new CustomToast(getString( R.string.ERROR_OCCUR_TRY_AGAIN), ActivitySignUp.this).showToast(true);
            }else if(result.message.equals("duplicate_email")){
                new CustomToast(getString( R.string.DUPLICATE_EMAIL), ActivitySignUp.this).showToast(true);
                edtEmail.setError(getString(R.string.DUPLICATE_EMAIL));
                edtEmail.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.red), PorterDuff.Mode.SRC_ATOP);
            }else if(result.message.equals("duplicate_user")){
                new CustomToast(getString( R.string.DUPLICATE_USER_NAME), ActivitySignUp.this).showToast(true);
                edtUserName.setError(getString(R.string.DUPLICATE_USER_NAME));
                edtUserName.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.red), PorterDuff.Mode.SRC_ATOP);
            }else if(result.message.equals("duplicate_user|duplicate_email")){
                new CustomToast(getString(R.string.DUPLICATE_EMAIL_AND_USER_NAME), ActivitySignUp.this).showToast(true);
                edtEmail.setError(getString(R.string.DUPLICATE_EMAIL));
                edtEmail.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.red), PorterDuff.Mode.SRC_ATOP);
                edtUserName.setError(getString(R.string.DUPLICATE_USER_NAME));
                edtUserName.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.red), PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

    private void showSuccessfulDialog(){
        CustomAlertDialog customAlertDialog = new CustomAlertDialog(ActivitySignUp.this);
        AlertDialog dialog = new AlertDialog.Builder(ActivitySignUp.this)
                .setCustomTitle(customAlertDialog.getTitleText(getString(R.string.REGISTER_RESULT)))
                .setMessage(R.string.USER_ACCOUNT_SUCCESSFULLY_CREATED)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                })
                .show();
        customAlertDialog.setDialogStyle(dialog);
    }

    public boolean validate() {
        boolean valid = true;

        String firstName = edtFirstName.getText().toString();
        String lastName = edtLastName.getText().toString();
        String email = edtEmail.getText().toString();
        //String mobile = edtMobile.getText().toString();
        String userName = edtUserName.getText().toString();
        String password = edtPassWord.getText().toString();
        String reEnterPassword = edtReEnterPassWord.getText().toString();

        if (firstName.isEmpty()) {
            edtFirstName.setError(getString(R.string.NAME_CANT_BE_EMPTY));
            edtFirstName.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.red), PorterDuff.Mode.SRC_ATOP);
            valid = false;
        } else {
            edtFirstName.setError(null);
        }

        if (lastName.isEmpty()) {
            edtLastName.setError(getString(R.string.LAST_NAME_CANT_BE_EMPTY));
            edtLastName.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.red), PorterDuff.Mode.SRC_ATOP);
            valid = false;
        } else {
            edtLastName.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError(getString(R.string.INVALID_EMAIL_ADDRESS));
            edtEmail.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.red), PorterDuff.Mode.SRC_ATOP);
            valid = false;
        } else {
            edtEmail.setError(null);
        }

//        if (mobile.isEmpty() || mobile.length()!=10) {
//            edtMobile.setError(getString(R.string.INVALID_PHONE_NUMBER));
//            edtMobile.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.red), PorterDuff.Mode.SRC_ATOP);
//            valid = false;
//        } else {
//            edtMobile.setError(null);
//        }

        if(userName.isEmpty() || userName.length() < 6 || userName.length() > 50){
            edtUserName.setError(getString(R.string.USER_NAME_LIMITATION));
            edtUserName.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.red), PorterDuff.Mode.SRC_ATOP);
            valid = false;
        }else {
            edtUserName.setError(null);
        }

        if (password.length() < 8 || reEnterPassword.length() < 8) {
            new CustomToast(getString(R.string.PASSWORD_LIMITATION),getApplicationContext()).showToast(true);
            valid = false;
        }else if(!(reEnterPassword.equals(password))){
            new CustomToast(getString(R.string.PASSWORD_AND_REENTER_DO_NOT_MATCH),getApplicationContext()).showToast(true);
            valid = false;
        }

        return valid;
    }
}