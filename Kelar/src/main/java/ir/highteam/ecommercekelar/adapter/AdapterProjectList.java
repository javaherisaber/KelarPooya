package ir.highteam.ecommercekelar.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.bundle.home.BundleProject;

/**
 * Created by mohammad hosein on 8/27/2016.
 */
public class AdapterProjectList extends RecyclerView.Adapter<AdapterProjectList.ProjectViewHolder> {

    private List<BundleProject> projectList;
    private Context contextMain;
    Typeface tfSans;

    public AdapterProjectList(List<BundleProject> projectList, Context context) {
        this.projectList = projectList;
        this.contextMain = context;
        tfSans = Typeface.createFromAsset(contextMain.getAssets(), context.getString(R.string.FONT_IRAN_SANS));
    }

    public void insertItem(BundleProject moreProjectItem){
        this.projectList.add(moreProjectItem);
    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }

    @Override
    public void onBindViewHolder(ProjectViewHolder projectViewHolder, final int position) {

        BundleProject bundleProject = projectList.get(position);

        projectViewHolder.projectName.setText(bundleProject.name);
        projectViewHolder.projectName.setTypeface(tfSans);

        Uri uri = Uri.parse(bundleProject.pic);
        projectViewHolder.projectImage.setImageURI(uri);
    }

    @Override
    public ProjectViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_project_item, viewGroup, false);
        ProjectViewHolder project = new ProjectViewHolder(itemView);
        return project;
    }

    public class ProjectViewHolder extends RecyclerView.ViewHolder {

        protected TextView projectName;
        protected SimpleDraweeView projectImage;

        public ProjectViewHolder(View v) {
            super(v);

            contextMain = v.getContext();
            projectName = (TextView) v.findViewById(R.id.txtProjectName);
            projectImage = (SimpleDraweeView) v.findViewById(R.id.imgProjectImage);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

        }
    }

}
