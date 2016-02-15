package ir.highteam.ecommercekelar.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.activity.ActivityProductList;
import ir.highteam.ecommercekelar.activity.ActivitySubCategory;
import ir.highteam.ecommercekelar.bundle.home.BundleHomeCategory;

public class AdapterRootCategoryList extends RecyclerView.Adapter<AdapterRootCategoryList.ProductViewHolder> {

    private List<BundleHomeCategory> categoryList;
	private Context contextMain;
	Typeface tfSans;

    public AdapterRootCategoryList(List<BundleHomeCategory> appList, Context context) {
        this.categoryList = appList;
        this.contextMain = context;
        tfSans = Typeface.createFromAsset(contextMain.getAssets(), context.getString(R.string.FONT_IRAN_SANS));
    }
    
    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public void insertItem(BundleHomeCategory bundleHomeCategory){
        categoryList.add(bundleHomeCategory);
    }
    
    @Override
    public void onBindViewHolder(ProductViewHolder appViewHolder, final int position) {
        BundleHomeCategory tabInfo = categoryList.get(position);
        appViewHolder.txtTabName.setText(tabInfo.name);
        appViewHolder.txtTabName.setTypeface(tfSans);
        appViewHolder.categoryId = tabInfo.id;
        appViewHolder.hasChild = tabInfo.hasChild;
        
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_category_item, viewGroup, false);
        ProductViewHolder app = new ProductViewHolder(itemView);
        return app;
       
    }

	public class ProductViewHolder extends RecyclerView.ViewHolder {

        protected TextView txtTabName;
        protected String categoryId;
        protected boolean hasChild;

        public ProductViewHolder(View v) {
            super(v);
            
            contextMain = v.getContext();
            txtTabName = (TextView) v.findViewById(R.id.txtTabName);
            
            v.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {

                    if(hasChild)
                    {
                        Intent intent = new Intent(contextMain, ActivitySubCategory.class);
                        intent.putExtra("categoryId",categoryId);
                        intent.putExtra("categoryTitle",txtTabName.getText().toString());
                        contextMain.startActivity(intent);
                    }
                    else {

                        Intent intent = new Intent(contextMain, ActivityProductList.class);
                        intent.putExtra("moreType", categoryId);
                        intent.putExtra("apiMoreUrl",contextMain.getString(R.string.URL_API_categoryProduct));
                        intent.putExtra("toolbarTitle",txtTabName.getText().toString());
                        contextMain.startActivity(intent);
                    }
				}
			});   
            
        }
		
    } 

}