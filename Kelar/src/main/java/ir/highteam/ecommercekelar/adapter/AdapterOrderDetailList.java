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

import java.util.List;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.activity.ActivityProduct;
import ir.highteam.ecommercekelar.bundle.order.BundleOrderItem;
import ir.highteam.ecommercekelar.view.CustomToast;

/**
 * Created by mohammad hosein on 9/2/2016.
 */
public class AdapterOrderDetailList extends RecyclerView.Adapter<AdapterOrderDetailList.OrderItemViewHolder> {

    private List<BundleOrderItem> items;
    private Context contextMain;
    private Typeface tfSans;


    public AdapterOrderDetailList(List<BundleOrderItem> orderItems, Context context) {
        this.items = orderItems;
        this.contextMain = context;
        tfSans = Typeface.createFromAsset(contextMain.getAssets(), contextMain.getString(R.string.FONT_IRAN_SANS));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void insertItem(BundleOrderItem bundleOrderItem){
        items.add(bundleOrderItem);

    }

    public void removeAllItems(){
        items.clear();
        notifyDataSetChanged();
    }

    public void removeItem(int index) {
        items.remove(index);
        notifyItemRemoved(index);
    }


    @Override
    public void onBindViewHolder(OrderItemViewHolder orderItemViewHolder, final int position) {

        BundleOrderItem orderItemInfo = items.get(position);
        orderItemViewHolder.orderItemTitle.setText(orderItemInfo.title);
        orderItemViewHolder.orderItemPrice.setText(orderItemInfo.price + " " + contextMain.getString(R.string.CURRENCY_IRAN_RIAL));
        orderItemViewHolder.orderItemOff.setText(orderItemInfo.off + " " + contextMain.getString(R.string.CURRENCY_IRAN_RIAL));
        orderItemViewHolder.orderItemOff.setVisibility(View.GONE);
        orderItemViewHolder.orderItemDesc.setText(orderItemInfo.description);
        orderItemViewHolder.orderItemCount.setText(orderItemInfo.count + " " + contextMain.getString(R.string.ADAD));
        orderItemViewHolder.productId = orderItemInfo.productId;
        orderItemViewHolder.existence = orderItemInfo.existence;

        orderItemViewHolder.orderItemTitle.setTypeface(tfSans);
        orderItemViewHolder.orderItemPrice.setTypeface(tfSans);
        orderItemViewHolder.orderItemDesc.setTypeface(tfSans);
        orderItemViewHolder.orderItemCount.setTypeface(tfSans);
        orderItemViewHolder.orderItemOff.setTypeface(tfSans);

        Uri uri = Uri.parse(orderItemInfo.pic);
        orderItemViewHolder.orderItemImage.setImageURI(uri);
    }

    @Override
    public OrderItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView;
        itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_order_detail_item , viewGroup, false);
        OrderItemViewHolder orderItem = new OrderItemViewHolder(itemView);
        return orderItem;
    }


    public class OrderItemViewHolder extends RecyclerView.ViewHolder {

        protected TextView orderItemTitle;
        protected TextView orderItemPrice;
        protected TextView orderItemOff;
        protected TextView orderItemDesc;
        protected TextView orderItemCount;
        protected boolean existence;
        protected SimpleDraweeView orderItemImage;
        protected String productId,count;


        public OrderItemViewHolder(final View v) {
            super(v);

            contextMain = v.getContext();
            orderItemTitle = (TextView) v.findViewById(R.id.txtTitle);
            orderItemPrice = (TextView) v.findViewById(R.id.txtPrice);
            orderItemOff = (TextView) v.findViewById(R.id.txtOff);
            orderItemImage = (SimpleDraweeView) v.findViewById(R.id.imgProduct);
            orderItemDesc = (TextView) v.findViewById(R.id.txtDesc);
            orderItemCount = (TextView) v.findViewById(R.id.txtCount);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(existence){
                        Intent intent = new Intent(contextMain,ActivityProduct.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("productId", productId);
                        contextMain.startActivity(intent);
                    }else {
                        new CustomToast(contextMain.getString(R.string.YOUR_PRODUCT_NOT_EXIST),contextMain).showToast(false);
                    }
                }
            });

        }
    }

}
