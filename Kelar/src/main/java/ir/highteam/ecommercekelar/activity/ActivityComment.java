package ir.highteam.ecommercekelar.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.adapter.AdapterCommentList;
import ir.highteam.ecommercekelar.bundle.BundleComment;
import ir.highteam.ecommercekelar.utile.LanguageFunctions;
import ir.highteam.ecommercekelar.utile.network.HttpUrlFunction;
import ir.highteam.ecommercekelar.view.RtlSupport;

public class ActivityComment extends AppCompatActivity {

    private List<BundleComment> commentList;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView commentRecycler;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    TextView txtEmptyRecycler;

    private HttpUrlFunction urlFunction;
    private Typeface tfSans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        initCapabilities();
        initToolbar();
        initObjectAndView();
        initListenersAndEvent();
        initCommentRecycler();
    }

    private void initCapabilities(){
        urlFunction = new HttpUrlFunction(getApplicationContext());
        urlFunction.enableHttpCaching();
    }

    private void initToolbar(){
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
        toolbarTitle.setText(getString(R.string.ACTIVITY_COMMENT_TOOLBAR_TITLE));
        toolbarTitle.setTextSize(24);
        RtlSupport rtlSupport = new RtlSupport(getApplicationContext());
        rtlSupport.changeViewDirection(toolbarTitle);
    }

    private boolean loading = true;
    int pastVisibleItems, visibleItemCount, totalItemCount;

    private void initListenersAndEvent(){
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityComment.this.finish();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivitySubmitComment.class);
                startActivity(intent);
            }
        });


        commentRecycler.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = linearLayoutManager.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();

                    if (loading)
                    {
                        if ( (visibleItemCount + pastVisibleItems) >= totalItemCount)
                        {
                            loading = false;
                            Log.e("...", "Last Item Wow !");
                            //Do pagination.. i.e. fetch new data
                            FrameLayout frameHorizontalProgress = (FrameLayout) findViewById(R.id.frameHorizontalProgress);
                            frameHorizontalProgress.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });
    }

    private void initObjectAndView(){
        fab = (FloatingActionButton) findViewById(R.id.fab);
        commentList = new ArrayList<>();
        commentRecycler = (RecyclerView) findViewById(R.id.commentRecycler);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        txtEmptyRecycler = (TextView) findViewById(R.id.txtEmptyRecycler);
        txtEmptyRecycler.setTypeface(tfSans);
    }


    private void initCommentRecycler(){



        for (int i = 0; i < 10 ; i++) {

            BundleComment bundleComment = new BundleComment();
            bundleComment.title = getString(R.string.PERFECT);
            bundleComment.username =getString(R.string.MOHAMMAD_HOSSEIN);
            bundleComment.comment = getString(R.string.COMMENT_TEXT);
            commentList.add(bundleComment);
        }

        commentRecycler.setHasFixedSize(true);
        commentRecycler.setLayoutManager(linearLayoutManager);
        if(commentList.size() == 0){
            txtEmptyRecycler.setVisibility(View.VISIBLE);
        }
        AdapterCommentList adapterCommentList = new AdapterCommentList(commentList, getApplicationContext());
        commentRecycler.setAdapter(adapterCommentList);
    }

    @Override
    protected void onStop() {
        super.onStop();
        urlFunction.flushHttpCache();
    }
}
