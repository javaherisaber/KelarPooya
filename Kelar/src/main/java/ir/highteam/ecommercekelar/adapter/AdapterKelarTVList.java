package ir.highteam.ecommercekelar.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.activity.ActivityKelarTV;
import ir.highteam.ecommercekelar.bundle.aparat.BundleAparatVideoItem;
import ir.highteam.ecommercekelar.utile.SendToFunctions;
import ir.highteam.ecommercekelar.utile.ShareFunctions;

/**
 * Created by Mahdizit on 06/09/2016.
 */
public class AdapterKelarTVList extends RecyclerView.Adapter<AdapterKelarTVList.KelarTvViewHolder> {

    private List<BundleAparatVideoItem> list;
    private Context contextMain;
    private ActivityKelarTV activity;
    Typeface tfSans;

    public AdapterKelarTVList(List<BundleAparatVideoItem> list, Context context, ActivityKelarTV a) {
        this.list = list;
        this.contextMain = context;
        this.activity = a;
        tfSans = Typeface.createFromAsset(contextMain.getAssets(), context.getString(R.string.FONT_IRAN_SANS));
    }

    public void insertItem(BundleAparatVideoItem item) {
        this.list.add(item);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(KelarTvViewHolder kelarTvViewHolder, final int position) {

        BundleAparatVideoItem videoItem = list.get(position);

        kelarTvViewHolder.txtDuration.setText(calculateDuration(Integer.parseInt(videoItem.duration)));
        kelarTvViewHolder.txtVisitCount.setText(videoItem.visitCount + " " + contextMain.getString(R.string.REVIEW));
        kelarTvViewHolder.txtTitle.setText(videoItem.title);
        kelarTvViewHolder.txtPostDate.setText(contextMain.getString(R.string.RELEASE_DATE) + " : " + videoItem.postDate);
        kelarTvViewHolder.frame = videoItem.frame;
        kelarTvViewHolder.url = videoItem.url;
        kelarTvViewHolder.bigPic = videoItem.bigPic;
        kelarTvViewHolder.txtDuration.setTypeface(tfSans);
        kelarTvViewHolder.txtVisitCount.setTypeface(tfSans);
        kelarTvViewHolder.txtTitle.setTypeface(tfSans);
        kelarTvViewHolder.txtPostDate.setTypeface(tfSans);

        Uri uri = Uri.parse(videoItem.smallPic);
        kelarTvViewHolder.imgPic.setImageURI(uri);
    }

    private String calculateDuration(int duration) {
        if ((duration / 3600) > 0) {
            int hour = duration / 3600;
            int min = (duration % 3600) / 60;
            int sec = (duration % 3600) % 60;
            return hour + ":" + min + ":" + sec;
        } else {
            int min = duration / 60;
            int sec = duration % 60;
            return min + ":" + sec;
        }
    }

    @Override
    public KelarTvViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_kelar_tv_item, viewGroup, false);
        KelarTvViewHolder holder = new KelarTvViewHolder(itemView);
        return holder;
    }

    public class KelarTvViewHolder extends RecyclerView.ViewHolder {

        protected ImageView btnShare, btnAparat;
        protected SimpleDraweeView imgPic;
        protected FrameLayout btnPlay;
        protected TextView txtDuration, txtVisitCount, txtTitle, txtPostDate;
        protected String frame, url, bigPic;

        public KelarTvViewHolder(View v) {
            super(v);

            contextMain = v.getContext();
            imgPic = (SimpleDraweeView) v.findViewById(R.id.imgPic);
            btnShare = (ImageView) v.findViewById(R.id.btnShare);
            btnAparat = (ImageView) v.findViewById(R.id.btnAparat);
            btnPlay = (FrameLayout) v.findViewById(R.id.btnPlay);
            txtDuration = (TextView) v.findViewById(R.id.txtDuration);
            txtVisitCount = (TextView) v.findViewById(R.id.txtVisitCnt);
            txtTitle = (TextView) v.findViewById(R.id.txtTitle);
            txtPostDate = (TextView) v.findViewById(R.id.txtPostDate);

            btnPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new SendToFunctions(activity).sendToCustomTab(frame);
                }
            });

            btnAparat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new SendToFunctions(activity).sendToCustomTab(url);
                }
            });

            btnShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new ShareFunctions(activity).sharePlainText("ویدیوی " + txtTitle.getText().toString() + "\n" +
                            "در کانال آپارات " + contextMain.getString(R.string.APARAT_USER_NAME) + " ببینید " + "\n" +
                            bigPic + "\n\n" + url);
                }
            });
        }
    }

}
