package ir.highteam.ecommercekelar.activity;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.List;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.adapter.AdapterProjectList;
import ir.highteam.ecommercekelar.bundle.home.BundleProject;
import ir.highteam.ecommercekelar.json.JsonProject;
import ir.highteam.ecommercekelar.utile.LanguageFunctions;
import ir.highteam.ecommercekelar.utile.network.HttpUrlFunction;
import ir.highteam.ecommercekelar.utile.network.NetworkFunctions;
import ir.highteam.ecommercekelar.view.RtlSupport;

public class ActivityProjectList extends AppCompatActivity {

    private Typeface tfSans;
    private List<BundleProject> projectList;
    private GridLayoutManager gridLayoutManager;
    private RecyclerView projectRecycler;

    private FrameLayout frameCircularProgress,frameNoInternet;
    private AppCompatButton btnRetry;

    private HttpUrlFunction urlFunction;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);

        initCapabilities();

        initToolBar();
        initObjectAndView();
        initListenerAndEvents();

        initActivityContent();

    }

    private void initCapabilities(){
        urlFunction = new HttpUrlFunction(getApplicationContext());
        urlFunction.enableHttpCaching();
    }

    private void initActivityContent(){
        NetworkFunctions networkFunctions = new NetworkFunctions(getApplicationContext());
        if(networkFunctions.isOnline()){
            new getProjects().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else {
            showNoInternet();
        }
    }

    private void initToolBar() {
        tfSans = Typeface.createFromAsset(getAssets(), getString(R.string.FONT_IRAN_SANS));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(LanguageFunctions.isRtlLanguage()){
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_forward);
        }
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setTypeface(tfSans);
        toolbarTitle.setText(getString(R.string.ACTIVITY_PROJECT_LIST_TOOLBAR_TITLE));
        toolbarTitle.setTextSize(24);
        RtlSupport rtlSupport = new RtlSupport(getApplicationContext());
        rtlSupport.changeViewDirection(toolbarTitle);
    }

    private void initObjectAndView(){
        projectRecycler = (RecyclerView) findViewById(R.id.projectRecycler);
        projectList = new ArrayList<>();
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);

        btnRetry = (AppCompatButton) findViewById(R.id.btnRetry);
        frameCircularProgress = (FrameLayout) findViewById(R.id.frameCircularProgress);
        frameNoInternet = (FrameLayout) findViewById(R.id.frameNoInternet);
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
                ActivityProjectList.this.finish();
            }
        });

    }

    private void initProjectRecycler(){
        for (int i = 0; i < 10 ; i++) {

            BundleProject bundleProject = new BundleProject();
            bundleProject.name = "پروژه ی اول";
            bundleProject.pic = "http://sahebnews.ir/files/uploads/2014/02/1-11.jpg";
            projectList.add(bundleProject);
        }

        projectRecycler.setHasFixedSize(true);
        projectRecycler.setLayoutManager(gridLayoutManager);
        AdapterProjectList adapterCommentList = new AdapterProjectList(projectList, getApplicationContext());
        projectRecycler.setAdapter(adapterCommentList);
    }

    private class getProjects extends AsyncTask<Void,Void,ArrayList<BundleProject>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<BundleProject> doInBackground(Void... voids) {

            try {
                    return new JsonProject().getProjects(ActivityProjectList.this);
            }catch (Exception e){
                Crashlytics.logException(e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<BundleProject> result) {
            super.onPostExecute(result);
            if(result == null){
                showNoInternet();
                return;
            }

            projectRecycler.setHasFixedSize(true);
            projectRecycler.setLayoutManager(gridLayoutManager);
            AdapterProjectList adapterCommentList = new AdapterProjectList(result, getApplicationContext());
            projectRecycler.setAdapter(adapterCommentList);

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
