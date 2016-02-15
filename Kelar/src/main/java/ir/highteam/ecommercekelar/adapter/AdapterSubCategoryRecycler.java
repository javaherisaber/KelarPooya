package ir.highteam.ecommercekelar.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.activity.ActivityProductList;
import ir.highteam.ecommercekelar.activity.ActivitySubCategory;
import ir.highteam.ecommercekelar.bundle.BundleSubCategory;

/**
 * Created by mohammad on 5/26/2016.
 */
public class AdapterSubCategoryRecycler extends RecyclerView.Adapter<AdapterSubCategoryRecycler.SubCategoryViewHolder>{

    private List<BundleSubCategory> subCategoryList;
    private Context contextMain;
    Typeface tfSans;

    public AdapterSubCategoryRecycler(List<BundleSubCategory> subCategoryList, Context context) {
        this.subCategoryList = subCategoryList;
        this.contextMain = context;
        tfSans = Typeface.createFromAsset(contextMain.getAssets(), context.getString(R.string.FONT_IRAN_SANS));
    }

    public AdapterSubCategoryRecycler(Context context) {
        subCategoryList = new ArrayList<BundleSubCategory>();
        this.contextMain = context;

    }

    public void insertItem(BundleSubCategory bundleSubCategory){
        subCategoryList.add(bundleSubCategory);
    }

    @Override
    public int getItemCount() {
        return subCategoryList.size();
    }

    @Override
    public void onBindViewHolder(SubCategoryViewHolder subCategoryViewHolder, final int position) {

        BundleSubCategory subCategoryInfo = subCategoryList.get(position);
        subCategoryViewHolder.subCategoryTitle.setText(subCategoryInfo.title);
        subCategoryViewHolder.subCategoryTitle.setTypeface(tfSans);
        subCategoryViewHolder.categoryId = subCategoryInfo.id;
        subCategoryViewHolder.hasChild = subCategoryInfo.hasChild;

        Uri uri = Uri.parse(subCategoryInfo.pic);
        subCategoryViewHolder.subCategoryImage.setImageURI(uri);
    }

    @Override
    public SubCategoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_sub_category_item, viewGroup, false);
        SubCategoryViewHolder subCategory = new SubCategoryViewHolder(itemView);
        return subCategory;
    }


    public class SubCategoryViewHolder extends RecyclerView.ViewHolder {

        protected TextView subCategoryTitle;
        protected SimpleDraweeView subCategoryImage;
        protected String categoryId;
        protected boolean hasChild;

        public SubCategoryViewHolder(View v) {
            super(v);
            contextMain = v.getContext();
            subCategoryTitle = (TextView) v.findViewById(R.id.txtTitle);
            subCategoryImage = (SimpleDraweeView) v.findViewById(R.id.imgCategory);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(hasChild)
                    {
                        Intent intent = new Intent(contextMain, ActivitySubCategory.class);
                        intent.putExtra("categoryId",categoryId);
                        intent.putExtra("categoryTitle",subCategoryTitle.getText().toString());
                        contextMain.startActivity(intent);
                    }
                    else {

                        Intent intent = new Intent(contextMain, ActivityProductList.class);
                        intent.putExtra("moreType", categoryId);
                        intent.putExtra("apiMoreUrl",contextMain.getString(R.string.URL_API_categoryProduct));
                        intent.putExtra("toolbarTitle",subCategoryTitle.getText().toString());
                        contextMain.startActivity(intent);
                    }
                }
            });

        }

    }
}
