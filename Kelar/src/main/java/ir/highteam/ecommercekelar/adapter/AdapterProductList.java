package ir.highteam.ecommercekelar.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.activity.ActivityProduct;
import ir.highteam.ecommercekelar.bundle.BundleFavorite;
import ir.highteam.ecommercekelar.bundle.BundleMoreProductItem;
import ir.highteam.ecommercekelar.database.DatabaseInteract;
import ir.highteam.ecommercekelar.utile.ShareFunctions;

/**
 * Created by mohammad on 5/7/2016.
 */
public class AdapterProductList extends RecyclerView.Adapter<AdapterProductList.ProductViewHolder> {

    private List<BundleMoreProductItem> productList;
    private Context contextMain;
    private Activity activity;
    Typeface tfSans;


    public AdapterProductList(List<BundleMoreProductItem> productList, Context context , Activity ac) {
        this.productList = productList;
        this.contextMain = context;
        this.activity = ac;
        tfSans = Typeface.createFromAsset(contextMain.getAssets(), context.getString(R.string.FONT_IRAN_SANS));
    }
    public AdapterProductList(Context context) {
        productList = new ArrayList<>();
        this.contextMain = context;

    }

    public void insertItem(BundleMoreProductItem moreProductItem){
        this.productList.add(moreProductItem);
    }

    public void clearItems(){
        productList.clear();
    }

    @Override
    public int getItemCount() {
        return productList.size();
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

        BundleMoreProductItem productInfo = productList.get(position);

        switch (productViewHolder.getItemViewType()) {

            case VIEW_WITH_OFF:

                productViewHolder.productTitle.setText(productInfo.name);
                productViewHolder.productPrice.setText(productInfo.price + " " + contextMain.getString(R.string.CURRENCY_IRAN_RIAL));
                productViewHolder.productOff.setText(productInfo.off + " " + contextMain.getString(R.string.CURRENCY_IRAN_RIAL));
                productViewHolder.productPriceasNum = productInfo.price;
                productViewHolder.productOffasNum = productInfo.off;
                productViewHolder.productPrice.setPaintFlags(productViewHolder.productPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                productViewHolder.productDesc.setText(productInfo.description);
                productViewHolder.productTitle.setTypeface(tfSans);
                productViewHolder.productId = productInfo.id;
                productViewHolder.productLink = productInfo.link;
                productViewHolder.productPrice.setTypeface(tfSans);
                productViewHolder.productOff.setTypeface(tfSans);
                productViewHolder.productDesc.setTypeface(tfSans);
                productViewHolder.productPic = productInfo.pic;

                Uri uri = Uri.parse(productInfo.pic);
                productViewHolder.productImage.setImageURI(uri);

                break;

            case VIEW_WITHOUT_OFF:

                productViewHolder.productTitle.setText(productInfo.name);
//                productViewHolder.productTitle.setText("این یک متن تست برای نمایش صحت عملکرد در حالت دو خطی است");
                productViewHolder.productPrice.setText(productInfo.price + " " + contextMain.getString(R.string.CURRENCY_IRAN_RIAL));
                productViewHolder.productOff.setText(productInfo.off + " " + contextMain.getString(R.string.CURRENCY_IRAN_RIAL));
                productViewHolder.productPriceasNum = productInfo.price;
                productViewHolder.productOffasNum = productInfo.off;
                productViewHolder.productOff.setVisibility(View.GONE);
                productViewHolder.productDesc.setText(productInfo.description);
                productViewHolder.productTitle.setTypeface(tfSans);
                productViewHolder.productId = productInfo.id;
                productViewHolder.productLink = productInfo.link;
                productViewHolder.productPrice.setTypeface(tfSans);
                productViewHolder.productOff.setTypeface(tfSans);
                productViewHolder.productDesc.setTypeface(tfSans);
                productViewHolder.productPic = productInfo.pic;

                Uri uri2 = Uri.parse(productInfo.pic);
                productViewHolder.productImage.setImageURI(uri2);

                break;
        }

    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_product_item, viewGroup, false);
        ProductViewHolder product = new ProductViewHolder(itemView);
        return product;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        protected TextView productTitle;
        protected TextView productPrice;
        protected TextView productOff;
        protected TextView productDesc;
        protected ImageView btnShare, btnFavorite;
        protected SimpleDraweeView productImage;
        protected String productId,productPriceasNum,productOffasNum,productLink,productPic;

        public ProductViewHolder(View v) {
            super(v);

            contextMain = v.getContext();
            productTitle = (TextView) v.findViewById(R.id.txtTitle);
            productPrice = (TextView) v.findViewById(R.id.txtPrice);
            productOff = (TextView) v.findViewById(R.id.txtOff);
            productImage = (SimpleDraweeView) v.findViewById(R.id.imgProduct);
            productDesc = (TextView) v.findViewById(R.id.txtDesc);
            btnShare = (ImageView) v.findViewById(R.id.btnShare);
            btnFavorite = (ImageView) v.findViewById(R.id.btnFavorite);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(contextMain,ActivityProduct.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("productId",productId);
                    contextMain.startActivity(intent);
                }
            });

            btnShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ShareFunctions shareFunctions = new ShareFunctions(activity);
                    shareFunctions.sharePlainText(productTitle.getText().toString() + "\nرا در " +
                            contextMain.getResources().getString(R.string.app_name) + " ببین " +  "\n" + productPic + "\n\n\n" + productLink);
                }
            });

            btnFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BundleFavorite bundleFavorite = new BundleFavorite();
                    bundleFavorite.id = productId;
                    bundleFavorite.title = productTitle.getText().toString();
                    bundleFavorite.description = productDesc.getText().toString();
                    bundleFavorite.price = productPriceasNum;
                    bundleFavorite.off = productOffasNum;
                    bundleFavorite.pic = productPic;

                    DatabaseInteract databaseInteract = new DatabaseInteract(contextMain);
                    databaseInteract.addToFavorite(bundleFavorite);
                }
            });

        }
    }
}