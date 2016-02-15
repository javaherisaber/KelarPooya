package ir.highteam.ecommercekelar.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.bundle.BundleTechnicalSpecifications;

/**
 * Created by mohammad on 5/6/2016.
 */
public class AdapterTechnicalSpecificationsList extends RecyclerView.Adapter<AdapterTechnicalSpecificationsList.CommentViewHolder> {

    private List<BundleTechnicalSpecifications> technicalSpecificationsList;
    private Context contextMain;
    Typeface tfSans;

    public AdapterTechnicalSpecificationsList(List<BundleTechnicalSpecifications> technicalSpecificationsList, Context context) {
        this.technicalSpecificationsList = technicalSpecificationsList;
        this.contextMain = context;
        tfSans = Typeface.createFromAsset(contextMain.getAssets(), context.getString(R.string.FONT_IRAN_SANS));
    }

    public AdapterTechnicalSpecificationsList(Context context) {
        technicalSpecificationsList = new ArrayList<BundleTechnicalSpecifications>();
        this.contextMain = context;

    }

    @Override
    public int getItemCount() {
        return technicalSpecificationsList.size();
    }

    public void insertItem(BundleTechnicalSpecifications bundleTechnicalSpecifications){
        technicalSpecificationsList.add(bundleTechnicalSpecifications);
    }

    @Override
    public void onBindViewHolder(AdapterTechnicalSpecificationsList.CommentViewHolder technicalSpecificationsViewHolder, final int position) {
        BundleTechnicalSpecifications technicalSpecificationsInfo = technicalSpecificationsList.get(position);
        technicalSpecificationsViewHolder.technicalSpecificationsTitle.setText(technicalSpecificationsInfo.title + " : ");
        technicalSpecificationsViewHolder.technicalSpecificationsValue.setText(technicalSpecificationsInfo.value);
        technicalSpecificationsViewHolder.technicalSpecificationsTitle.setTypeface(tfSans);
        technicalSpecificationsViewHolder.technicalSpecificationsValue.setTypeface(tfSans);
    }

    @Override
    public AdapterTechnicalSpecificationsList.CommentViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_technical_specifications_item, viewGroup, false);
        CommentViewHolder technicalSpecifications = new CommentViewHolder(itemView);
        return technicalSpecifications;
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        protected TextView technicalSpecificationsTitle;
        protected TextView technicalSpecificationsValue;

        public CommentViewHolder(View v) {
            super(v);

            contextMain = v.getContext();
            technicalSpecificationsTitle = (TextView) v.findViewById(R.id.txtTitle);
            technicalSpecificationsValue = (TextView) v.findViewById(R.id.txtValue);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

        }

    }
}
