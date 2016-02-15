package ir.highteam.ecommercekelar.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import ir.highteam.ecommercekelar.activity.ActivityFavorite;
import ir.highteam.ecommercekelar.activity.ActivityProduct;
import ir.highteam.ecommercekelar.bundle.BundleFavorite;
import ir.highteam.ecommercekelar.database.DatabaseInteract;
import ir.highteam.ecommercekelar.view.CustomAlertDialog;
import ir.highteam.ecommercekelar.view.CustomToast;

/**
 * Created by mohammad on 5/7/2016.
 */
public class AdapterFavoriteList extends RecyclerView.Adapter<AdapterFavoriteList.ProductViewHolder> {

    private List<BundleFavorite> favoriteList;
    private Context contextMain;
    private ActivityFavorite activityFavorite;
    Typeface tfSans;


    public AdapterFavoriteList(List<BundleFavorite> favoriteList, Context context,ActivityFavorite activity) {
        this.favoriteList = favoriteList;
        this.contextMain = context;
        this.activityFavorite = activity;
        tfSans = Typeface.createFromAsset(contextMain.getAssets(), context.getString(R.string.FONT_IRAN_SANS));
    }
    public AdapterFavoriteList(Context context) {
        favoriteList = new ArrayList<BundleFavorite>();
        this.contextMain = context;
    }

    public void removeItem(int index) {
        favoriteList.remove(index);
        notifyItemRemoved(index);
    }

    public void insertItem(BundleFavorite bundleFavorite){
        favoriteList.add(bundleFavorite);
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    private final int VIEW_WITH_OFF = 1;
    private final int VIEW_WITHOUT_OFF = 2;

    @Override
    public int getItemViewType(int position) {
        if(!favoriteList.get(position).off.equals("")){
            return VIEW_WITH_OFF;
        }
        return VIEW_WITHOUT_OFF;
    }


    @Override
    public void onBindViewHolder(ProductViewHolder favoriteViewHolder, final int position) {


        BundleFavorite favoriteInfo = favoriteList.get(position);

        switch (favoriteViewHolder.getItemViewType()) {

            case VIEW_WITH_OFF:

                favoriteViewHolder.favoriteTitle.setText(favoriteInfo.title);
                favoriteViewHolder.favoritePrice.setText(favoriteInfo.price + " " + contextMain.getString(R.string.CURRENCY_IRAN_RIAL));
                favoriteViewHolder.favoriteOff.setText(favoriteInfo.off +" "+ contextMain.getString(R.string.CURRENCY_IRAN_RIAL));
                favoriteViewHolder.favoritePrice.setPaintFlags(favoriteViewHolder.favoritePrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                favoriteViewHolder.favoriteDesc.setText(favoriteInfo.description);
                favoriteViewHolder.favoriteTitle.setTypeface(tfSans);
                favoriteViewHolder.favoritePrice.setTypeface(tfSans);
                favoriteViewHolder.favoriteOff.setTypeface(tfSans);
                favoriteViewHolder.favoriteDesc.setTypeface(tfSans);
                favoriteViewHolder.productId = favoriteInfo.id;
                favoriteViewHolder.existence = favoriteInfo.existence;

                Uri uri = Uri.parse(favoriteInfo.pic);
                favoriteViewHolder.favoritePic.setImageURI(uri);

                break;

            case VIEW_WITHOUT_OFF:

                favoriteViewHolder.favoriteTitle.setText(favoriteInfo.title);
                favoriteViewHolder.favoritePrice.setText(favoriteInfo.price+ " " + contextMain.getString(R.string.CURRENCY_IRAN_RIAL));
                favoriteViewHolder.favoriteOff.setText(favoriteInfo.off+ " " + contextMain.getString(R.string.CURRENCY_IRAN_RIAL));
                favoriteViewHolder.favoriteOff.setVisibility(View.GONE);
                favoriteViewHolder.favoriteDesc.setText(favoriteInfo.description);
                favoriteViewHolder.favoriteTitle.setTypeface(tfSans);
                favoriteViewHolder.favoritePrice.setTypeface(tfSans);
                favoriteViewHolder.favoriteOff.setTypeface(tfSans);
                favoriteViewHolder.favoriteDesc.setTypeface(tfSans);
                favoriteViewHolder.productId = favoriteInfo.id;
                favoriteViewHolder.existence = favoriteInfo.existence;

                Uri uri2 = Uri.parse(favoriteInfo.pic);
                favoriteViewHolder.favoritePic.setImageURI(uri2);

                break;

        }


    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_favorite_item, viewGroup, false);
        ProductViewHolder favorite = new ProductViewHolder(itemView);
        return favorite;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        protected TextView favoriteTitle;
        protected TextView favoriteDesc;
        protected TextView favoritePrice;
        protected TextView favoriteOff;
        protected ImageView btnDelete;
        protected SimpleDraweeView favoritePic;
        protected String productId;
        protected boolean existence;

        public ProductViewHolder(View v) {
            super(v);

            contextMain = v.getContext();
            favoriteTitle = (TextView) v.findViewById(R.id.txtTitle);
            favoritePrice = (TextView) v.findViewById(R.id.txtPrice);
            favoriteOff = (TextView) v.findViewById(R.id.txtOff);
            favoritePic = (SimpleDraweeView) v.findViewById(R.id.imgProduct);
            favoriteDesc = (TextView) v.findViewById(R.id.txtDesc);
            btnDelete = (ImageView) v.findViewById(R.id.btnDelete);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(existence){
                        Intent intent = new Intent(contextMain,ActivityProduct.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("productId",productId);
                        contextMain.startActivity(intent);
                    }else {
                        new CustomToast(contextMain.getString(R.string.YOUR_PRODUCT_NOT_EXIST),contextMain).showToast(false);
                    }
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    CustomAlertDialog customAlertDialog = new CustomAlertDialog(contextMain);

                    AlertDialog dialog = new AlertDialog.Builder(contextMain)
                            .setMessage(contextMain.getString(R.string.ARE_YOU_SURE_DELETE_ITEM))
                            .setPositiveButton(contextMain.getString(R.string.YES), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    DatabaseInteract databaseInteract = new DatabaseInteract(contextMain);
                                    boolean status = databaseInteract.deleteFavorite(productId);
                                    if(status){
                                        removeItem(getLayoutPosition());
                                        int count = databaseInteract.getFavoriteCount();
                                        if(count == 0){
                                            activityFavorite.showEmptyListTxt();
                                        }
                                        new CustomToast(contextMain.getString( R.string.PRODUCT_DELETED), contextMain)
                                                .showToast(false);
                                    }else {
                                        new CustomToast(contextMain.getString( R.string.ERROR_OCCUR_TRY_AGAIN), contextMain)
                                                .showToast(true);
                                    }
                                }
                            })
                            .setCustomTitle(customAlertDialog.getTitleText(contextMain.getString(R.string.WARNING)))
                            .setNegativeButton(contextMain.getString(R.string.NO), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            })
                            .show();

                    customAlertDialog.setDialogStyle(dialog);
                }
            });

        }
    }
}
