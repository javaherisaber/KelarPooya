package ir.highteam.ecommercekelar.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.activity.ActivityProduct;
import ir.highteam.ecommercekelar.bundle.BundleMetro;

public class AdapterMetroProducts extends RecyclerView.Adapter<AdapterMetroProducts.ProductViewHolder> {

    private List<BundleMetro> productList;
	private Context contextMain;
	Typeface tfSans;
    String metroType;

    public AdapterMetroProducts(List<BundleMetro> productList, Context context, String metroType) {
        this.productList = productList;
        this.contextMain = context;
        tfSans = Typeface.createFromAsset(contextMain.getAssets(), context.getString(R.string.FONT_IRAN_SANS));
        this.metroType = metroType;
    }
    public AdapterMetroProducts(Context context) {
        productList = new ArrayList<>();
        this.contextMain = context;

    }
    
    @Override
    public int getItemCount() {
        return productList.size();
    }
    
    public void removeItem(int index) {
        productList.remove(index);
        notifyDataSetChanged();
    }

    public void insertItem(BundleMetro bundleMetro){
        productList.add(bundleMetro);
    }
    
    public void removeAll() {
    	productList.clear();
    	notifyDataSetChanged();
	}
    
    public void insertItem(int count ,BundleMetro object) {

    	productList.add(object);  
    	notifyDataSetChanged();

    }

    private final int VIEW_WITH_OFF = 1;
    private final int VIEW_WITHOUT_OFF = 2;

    @Override
    public int getItemViewType(int position) {
        if(!productList.get(position).off.equals("")){
            return VIEW_WITH_OFF;
        }
        return VIEW_WITHOUT_OFF;
    }

    
    @Override
    public void onBindViewHolder(ProductViewHolder productViewHolder, final int position) {
        BundleMetro productInfo = productList.get(position);
        switch (productViewHolder.getItemViewType()) {

            case VIEW_WITH_OFF:

                productViewHolder.productTitle.setText(productInfo.name);
                productViewHolder.productPrice.setText(productInfo.price + " " + contextMain.getString(R.string.CURRENCY_IRAN_RIAL));
                productViewHolder.productId = productInfo.id;
                productViewHolder.productOff.setText(productInfo.off + " " + contextMain.getString(R.string.CURRENCY_IRAN_RIAL));
                productViewHolder.productPrice.setPaintFlags(productViewHolder.productPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                productViewHolder.productTitle.setTypeface(tfSans);
                productViewHolder.productPrice.setTypeface(tfSans);
                productViewHolder.productOff.setTypeface(tfSans);

                Uri imageUri = Uri.parse(productInfo.pic);
                productViewHolder.productImage.setImageURI(imageUri);

                break;

            case VIEW_WITHOUT_OFF:

                productViewHolder.productTitle.setText(productInfo.name);
                productViewHolder.productPrice.setText(productInfo.price + " " + contextMain.getString(R.string.CURRENCY_IRAN_RIAL));
                productViewHolder.productId = productInfo.id;
                productViewHolder.productOff.setText(productInfo.off + " " + contextMain.getString(R.string.CURRENCY_IRAN_RIAL));
                productViewHolder.productOff.setVisibility(View.GONE);
                productViewHolder.productTitle.setTypeface(tfSans);
                productViewHolder.productPrice.setTypeface(tfSans);
                productViewHolder.productOff.setTypeface(tfSans);

                Uri uri = Uri.parse(productInfo.pic);
                productViewHolder.productImage.setImageURI(uri);

                break;
        }

    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView;
        if (metroType.equals("AmazingOffers")) {
            itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_main_amazing_product_item, viewGroup, false);

        } else {
            itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_main_product_item, viewGroup, false);
        }
        ProductViewHolder product = new ProductViewHolder(itemView);
        return product;
    }

	public class ProductViewHolder extends RecyclerView.ViewHolder {

        protected TextView productTitle;
        protected TextView productPrice;
		protected TextView productOff;
        protected SimpleDraweeView productImage;
        protected String productId;

        public ProductViewHolder(View v) {
            super(v);
            
            contextMain = v.getContext();
            productTitle =  (TextView) v.findViewById(R.id.txtTitle);
            productPrice = (TextView)  v.findViewById(R.id.txtPrice);
            productOff = (TextView)  v.findViewById(R.id.txtOff);
            productImage = (SimpleDraweeView) v.findViewById(R.id.imgProduct);

            v.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
                    Intent intent = new Intent(contextMain,ActivityProduct.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("productId",productId);
                    intent.putExtra("productTitle",productTitle.getText().toString());
                    intent.putExtra("productPrice",productPrice.getText().toString());
                    intent.putExtra("productOff",productOff.getText().toString());
                    contextMain.startActivity(intent);
				}
				
			});               
			
        }
		
    } 

}