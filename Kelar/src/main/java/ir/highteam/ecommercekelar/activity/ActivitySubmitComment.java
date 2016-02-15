package ir.highteam.ecommercekelar.activity;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.utile.network.HttpUrlFunction;

public class ActivitySubmitComment extends AppCompatActivity {

    private TextView txtTitle, txtUsername;
    private EditText edtComment;
    private AppCompatButton btnSubmit;
    Typeface typeface;
    ScrollView scrollView;


    private HttpUrlFunction urlFunction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_comment);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        initCapabilities();
        initObjectAndView();

        edtComment.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    private void initCapabilities(){
        urlFunction = new HttpUrlFunction(getApplicationContext());
        urlFunction.enableHttpCaching();
    }

    private void initObjectAndView(){
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtUsername = (TextView) findViewById(R.id.txtUsername);
        btnSubmit = (AppCompatButton) findViewById(R.id.btnSubmit);
        edtComment = (EditText) findViewById(R.id.edtComment);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        typeface = Typeface.createFromAsset(getAssets(), getString(R.string.FONT_IRAN_SANS));

        txtTitle.setTypeface(typeface);
        txtUsername.setTypeface(typeface);
        btnSubmit.setTypeface(typeface);
        edtComment.setTypeface(typeface);
    }

    @Override
    protected void onStop() {
        super.onStop();
        urlFunction.flushHttpCache();
    }
}
