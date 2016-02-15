package ir.highteam.ecommercekelar.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.activity.ActivityProduct;
import ir.highteam.ecommercekelar.activity.ActivityPurchaseBasket;
import ir.highteam.ecommercekelar.bundle.BundleBasket;
import ir.highteam.ecommercekelar.database.DatabaseInteract;
import ir.highteam.ecommercekelar.utile.PrefsFunctions;
import ir.highteam.ecommercekelar.view.CustomAlertDialog;
import ir.highteam.ecommercekelar.view.CustomToast;

/**
 * Created by mohammad on 5/7/2016.
 */
public class AdapterBasketList extends RecyclerView.Adapter<AdapterBasketList.ProductViewHolder> {

    public ArrayList<BundleBasket> productList;
    private Context contextMain;
    private Typeface tfSans;
    private boolean[] notExist;
    private ActivityPurchaseBasket activityPurchaseBasket;

    String[] colors = {"#f49c10","#103bf4","#fb0596","#c0c0c0","#370596"};

    public AdapterBasketList(ArrayList<BundleBasket> productList, Context context , ActivityPurchaseBasket activity) {
        this.productList = productList;
        this.activityPurchaseBasket = activity;
        this.contextMain = context;
        notExist = new boolean[productList.size()];

        for (int i = 0; i < productList.size(); i++) {
            BundleBasket bundleBasket = productList.get(i);
            notExist[i] = bundleBasket.existence;
        }

        tfSans = Typeface.createFromAsset(contextMain.getAssets(), contextMain.getString(R.string.FONT_IRAN_SANS));
    }
    public AdapterBasketList(Context context) {
        productList = new ArrayList<BundleBasket>();
        this.contextMain = context;

    }

    public void insertItem(BundleBasket bundleBasket){
        productList.add(bundleBasket);

    }

    public void removeAllItems(){
        productList.clear();
        notifyDataSetChanged();
    }

    public void removeItem(int index) {
        productList.remove(index);
        notifyItemRemoved(index);
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

        BundleBasket productInfo = productList.get(position);

        switch (productViewHolder.getItemViewType()) {

            case VIEW_WITH_OFF:

                productViewHolder.productTitle.setText(productInfo.title);
                productViewHolder.productPrice.setText(productInfo.price + " " + contextMain.getString(R.string.CURRENCY_IRAN_RIAL));
                productViewHolder.productOff.setText(productInfo.off + " " + contextMain.getString(R.string.CURRENCY_IRAN_RIAL));
                productViewHolder.productPrice.setPaintFlags(productViewHolder.productPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                productViewHolder.productDesc.setText(productInfo.description);
                productViewHolder.productCount.setText(productInfo.count);
                productViewHolder.count = productInfo.count;
                productViewHolder.productId = productInfo.id;
                productViewHolder.existence = productInfo.existence;

//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                    productViewHolder.productColor.setBackground(getColorView(productInfo.color));
//                } else {
//                    productViewHolder.productColor.setBackgroundDrawable(getColorView(productInfo.color));
//                }

                productViewHolder.productTitle.setTypeface(tfSans);
                productViewHolder.productPrice.setTypeface(tfSans);
                productViewHolder.productDesc.setTypeface(tfSans);
                productViewHolder.productCount.setTypeface(tfSans);
                productViewHolder.productOff.setTypeface(tfSans);
//                productViewHolder.productTxtColor.setTypeface(tfSans);
                productViewHolder.productTxtCount.setTypeface(tfSans);

//        productViewHolder.productNotExist.setTypeface(tfSans);

                Uri uri = Uri.parse(productInfo.pic);
                productViewHolder.productImage.setImageURI(uri);

                break;

            case VIEW_WITHOUT_OFF:

                productViewHolder.productTitle.setText(productInfo.title);
                productViewHolder.productPrice.setText(productInfo.price+ " " + contextMain.getString(R.string.CURRENCY_IRAN_RIAL));
                productViewHolder.productOff.setText(productInfo.off + " " + contextMain.getString(R.string.CURRENCY_IRAN_RIAL));
                productViewHolder.productOff.setVisibility(View.GONE);
                productViewHolder.productDesc.setText(productInfo.description);
                productViewHolder.productCount.setText(productInfo.count);
                productViewHolder.productId = productInfo.id;
                productViewHolder.existence = productInfo.existence;

//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                    productViewHolder.productColor.setBackground(getColorView(productInfo.color));
//                } else {
//                    productViewHolder.productColor.setBackgroundDrawable(getColorView(productInfo.color));
//                }

                productViewHolder.productTitle.setTypeface(tfSans);
                productViewHolder.productPrice.setTypeface(tfSans);
                productViewHolder.productDesc.setTypeface(tfSans);
                productViewHolder.productCount.setTypeface(tfSans);
                productViewHolder.productOff.setTypeface(tfSans);
//                productViewHolder.productTxtColor.setTypeface(tfSans);
                productViewHolder.productTxtCount.setTypeface(tfSans);

//        productViewHolder.productNotExist.setTypeface(tfSans);

                Uri uri2 = Uri.parse(productInfo.pic);
                productViewHolder.productImage.setImageURI(uri2);

                break;

        }

    }

        protected GradientDrawable getColorView(String colorHex){
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setColor(Color.parseColor(colorHex));
        return shape;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView;
        itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_purchase_basket_item, viewGroup, false);
        ProductViewHolder product = new ProductViewHolder(itemView);
        return product;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        protected TextView productTitle;
        protected TextView productPrice;
        protected TextView productOff;
        protected TextView productDesc;
        protected EditText productCount;
        protected TextView productTxtCount;
        protected boolean existence;
//        protected TextView productTxtColor;
        protected ImageView btnDelete;
        protected SimpleDraweeView productImage;
//        protected View productColor;
        protected String productId,count;

        private PopupMenu popupMenu;
        private final static int IdONE = 1;
        private final static int IdTWO = 2;
        private final static int IdTHREE = 3;
        private final static int IdFour = 4;
        private final static int IdFive = 5;

        public ProductViewHolder(final View v) {
            super(v);

            contextMain = v.getContext();
            productTitle = (TextView) v.findViewById(R.id.txtTitle);
            productPrice = (TextView) v.findViewById(R.id.txtPrice);
            productOff = (TextView) v.findViewById(R.id.txtOff);
            productImage = (SimpleDraweeView) v.findViewById(R.id.imgProduct);
            productDesc = (TextView) v.findViewById(R.id.txtDesc);
            btnDelete = (ImageView) v.findViewById(R.id.btnDelete);
            productCount = (EditText) v.findViewById(R.id.edtCount);
            productTxtCount = (TextView) v.findViewById(R.id.txtCount);
//            productTxtColor = (TextView) v.findViewById(R.id.txtColor);
//            productColor = v.findViewById(R.id.colorView);

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

            productCount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_NEXT) {
                        String regex = "([1-9])(([0-9]+)?)";
                        if(productCount.getText().toString().matches(regex)) {
                            DatabaseInteract db = new DatabaseInteract(contextMain);
                            String user = new PrefsFunctions(contextMain).getLoginUser();
                            boolean status = db.updateBasketCount(productCount.getText().toString(),productId,user);
                            if(status){
                                new CustomToast(contextMain.getString( R.string.COUNT_UPDATED), contextMain)
                                        .showToast(false);
                            }else{
                                new CustomToast(contextMain.getString( R.string.ERROR_OCCUR_TRY_AGAIN), contextMain)
                                        .showToast(true);
                            }
                        }else{
                            productCount.setText(count);
                            notifyDataSetChanged();
                            new CustomToast(contextMain.getString( R.string.COUNT_INVALID), contextMain)
                                    .showToast(false);
                        }

                    }
                    return false;
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
                                    String user = new PrefsFunctions(contextMain).getLoginUser();
                                    boolean status = databaseInteract.deleteBasket(productId,user);
                                    if(status){
                                        removeItem(getLayoutPosition());
                                        int purchaseCount = databaseInteract.getBasketCount(user);
                                        if(purchaseCount == 0){
                                            activityPurchaseBasket.showEmptyListTxt();
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



//            productColor.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    popupMenu = new PopupMenu(contextMain, v.findViewById(R.id.colorView));
//                    popupMenu.getMenu().add(Menu.NONE, IdONE, Menu.NONE, "Item 1");
//                    popupMenu.getMenu().add(Menu.NONE, IdTWO, Menu.NONE, "Item 2");
//                    popupMenu.getMenu().add(Menu.NONE, IdTHREE, Menu.NONE, "Item 3");
//                    popupMenu.getMenu().add(Menu.NONE, IdFour, Menu.NONE, "Item 3");
//                    popupMenu.getMenu().add(Menu.NONE, IdFive, Menu.NONE, "Item 3");
//                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                        @Override
//                        public boolean onMenuItemClick(MenuItem item) {
//
//                            String selectedColorHex = "#FFFFFF";
//                            switch (item.getItemId()) {
//                                case IdONE:
//                                    selectedColorHex = colors[0];
//                                    break;
//                                case IdTWO:
//                                    selectedColorHex = colors[1];
//                                    break;
//                                case IdTHREE:
//                                    selectedColorHex = colors[2];
//                                    break;
//                                case IdFour:
//                                    selectedColorHex = colors[3];
//                                    break;
//                                case IdFive:
//                                    selectedColorHex = colors[4];
//                                    break;
//
//
//                            }
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                                productColor.setBackground(getColorView(selectedColorHex));
//                            } else {
//                                productColor.setBackgroundDrawable(getColorView(selectedColorHex));
//                            }
//
//                            return false;
//                        }
//                    });
//                    popupMenu.show();
//                }
//            });

        }
    }

}
