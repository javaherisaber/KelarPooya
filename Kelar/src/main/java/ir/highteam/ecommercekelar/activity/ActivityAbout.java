package ir.highteam.ecommercekelar.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.utile.SendToFunctions;

public class ActivityAbout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView txtHighteam = (TextView) findViewById(R.id.txtHighteam);
        Typeface tfsans = Typeface.createFromAsset(getAssets(),getString(R.string.FONT_YEKAN));
        txtHighteam.setTypeface(tfsans);

        FrameLayout btnDeveloper = (FrameLayout) findViewById(R.id.btnDeveloper);
        btnDeveloper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendToFunctions sendToFunctions = new SendToFunctions(ActivityAbout.this);
                sendToFunctions.sendToCustomTab(getString(R.string.URL_HIGH_TEAM));
            }
        });
    }
}
