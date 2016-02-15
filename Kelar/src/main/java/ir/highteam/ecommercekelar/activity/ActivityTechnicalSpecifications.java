package ir.highteam.ecommercekelar.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.adapter.AdapterTechnicalSpecificationsList;
import ir.highteam.ecommercekelar.bundle.BundleTechnicalSpecifications;
import ir.highteam.ecommercekelar.utile.LanguageFunctions;
import ir.highteam.ecommercekelar.utile.network.HttpUrlFunction;
import ir.highteam.ecommercekelar.view.RtlSupport;

public class ActivityTechnicalSpecifications extends AppCompatActivity {

    private List<BundleTechnicalSpecifications> technicalSpecificationsList;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView technicalSpecificationsRecycler;
    private Toolbar toolbar;

    private HttpUrlFunction urlFunction;
    private TextView txtEmptyRecycler;
    private Typeface tfSans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technical_specifications);
        initCapabilities();
        initToolbar();
        initObjectAndView();
        initListenerAndEvent();
        initCommentRecycler();
    }

    private void initCapabilities(){
        urlFunction = new HttpUrlFunction(getApplicationContext());
        urlFunction.enableHttpCaching();
    }


    private void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(LanguageFunctions.isRtlLanguage()){
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_forward);
        }
        tfSans = Typeface.createFromAsset(getAssets(), getString(R.string.FONT_IRAN_SANS));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setTypeface(tfSans);
        toolbarTitle.setText(getString(R.string.ACTIVITY_TECHNICAL_SPECIFICATION_TOOLBAR_TITLE));
        toolbarTitle.setTextSize(24);
        RtlSupport rtlSupport = new RtlSupport(getApplicationContext());
        rtlSupport.changeViewDirection(toolbarTitle);
    }

    private void initObjectAndView(){
        technicalSpecificationsList = new ArrayList<BundleTechnicalSpecifications>();
        technicalSpecificationsRecycler = (RecyclerView) findViewById(R.id.technicalSpecificationsRecycler);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        txtEmptyRecycler = (TextView) findViewById(R.id.txtEmptyRecycler);
        txtEmptyRecycler.setTypeface(tfSans);
    }

    private void initListenerAndEvent(){
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityTechnicalSpecifications.this.finish();
            }
        });
    }

    private void initCommentRecycler(){

        for (int i = 0; i < 30 ; i++) {

            BundleTechnicalSpecifications bundleComment = new BundleTechnicalSpecifications();
            bundleComment.title = getString(R.string.PERFECT);
            bundleComment.value = getString(R.string.MOHAMMAD_HOSSEIN);
            technicalSpecificationsList.add(bundleComment);
        }

        technicalSpecificationsRecycler.setHasFixedSize(true);
        technicalSpecificationsRecycler.setLayoutManager(linearLayoutManager);
        AdapterTechnicalSpecificationsList adapterCommentList = new AdapterTechnicalSpecificationsList(technicalSpecificationsList, getApplicationContext());
        technicalSpecificationsRecycler.setAdapter(adapterCommentList);
    }

    @Override
    protected void onStop() {
        super.onStop();
        urlFunction.flushHttpCache();
    }
}
