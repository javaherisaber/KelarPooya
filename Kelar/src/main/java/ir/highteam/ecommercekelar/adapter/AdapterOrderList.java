package ir.highteam.ecommercekelar.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.activity.ActivityOrderDetail;
import ir.highteam.ecommercekelar.bundle.order.BundleOrder;
import ir.highteam.ecommercekelar.bundle.order.BundleOrderShippingInfo;
import ir.highteam.ecommercekelar.view.CustomAlertDialog;

/**
 * Created by mohammad hosein on 9/1/2016.
 */
public class AdapterOrderList extends RecyclerView.Adapter<AdapterOrderList.OrderViewHolder>{

    private List<BundleOrder> orderList;
    private Context contextMain;
    Typeface tfSans;

    public AdapterOrderList(List<BundleOrder> orderList, Context context) {
        this.orderList = orderList;
        this.contextMain = context;
        tfSans = Typeface.createFromAsset(contextMain.getAssets(), context.getString(R.string.FONT_IRAN_SANS));
    }

    public void insertItem(BundleOrder moreOrderItem){
        this.orderList.add(moreOrderItem);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }


    @Override
    public void onBindViewHolder(OrderViewHolder orderViewHolder, final int position) {

        BundleOrder bundleOrder = orderList.get(position);

        orderViewHolder.orderNum.setText(contextMain.getString(R.string.ORDER_NUMBER) + " : "+ bundleOrder.orderNum);
        orderViewHolder.orderDate.setText(bundleOrder.date);
        orderViewHolder.orderTime.setText(bundleOrder.time);
        orderViewHolder.orderTotalCount.setText(bundleOrder.totalCount + " " + contextMain.getString(R.string.ADAD));
        orderViewHolder.orderTotalPrice.setText(bundleOrder.totalPrice + " " + contextMain.getString(R.string.CURRENCY_IRAN_RIAL));
        orderViewHolder.orderNum.setTypeface(tfSans);
        orderViewHolder.orderDate.setTypeface(tfSans);
        orderViewHolder.orderTime.setTypeface(tfSans);
        orderViewHolder.orderTotalCount.setTypeface(tfSans);
        orderViewHolder.orderTotalCountText.setTypeface(tfSans);
        orderViewHolder.orderTotalPrice.setTypeface(tfSans);
        orderViewHolder.orderTotalPriceText.setTypeface(tfSans);
        orderViewHolder.orderId = bundleOrder.orderNum;
        orderViewHolder.orderStatus = bundleOrder.orderStatus;
        orderViewHolder.shippingInfo = bundleOrder.shippingInfo;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_register_order_item, viewGroup, false);
        OrderViewHolder order = new OrderViewHolder(itemView);
        return order;
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        protected TextView orderNum;
        protected TextView orderDate;
        protected TextView orderTime;
        protected TextView orderTotalCount;
        protected TextView orderTotalCountText;
        protected TextView orderTotalPrice;
        protected TextView orderTotalPriceText;
        protected ImageView imgAgency;
        protected BundleOrderShippingInfo shippingInfo;
        protected String orderStatus,orderId;

        public OrderViewHolder(View v) {
            super(v);

            contextMain = v.getContext();
            orderNum = (TextView) v.findViewById(R.id.txtOrderNum);
            orderDate = (TextView) v.findViewById(R.id.txtDate);
            orderTime = (TextView) v.findViewById(R.id.txtTime);
            orderTotalCount = (TextView) v.findViewById(R.id.txtTotalCount);
            orderTotalCountText = (TextView) v.findViewById(R.id.txtTotalCountText);
            orderTotalPrice = (TextView) v.findViewById(R.id.txtTotalPrice);
            orderTotalPriceText = (TextView) v.findViewById(R.id.txtTotalPriceText);
            imgAgency = (ImageView) v.findViewById(R.id.imgAgency);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(contextMain, ActivityOrderDetail.class);
                    intent.putExtra("orderId",orderId);
                    contextMain.startActivity(intent);
                }
            });

            imgAgency.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CustomAlertDialog customAlertDialog = new CustomAlertDialog(contextMain);

                    AlertDialog dialog = new AlertDialog.Builder(contextMain)
                            .setCustomTitle(customAlertDialog.getTitleText(contextMain.getString(R.string.ORDER_INFO)))
                            .setMessage("نام : "  +shippingInfo.firstName
                                    + "\n" + "نام خانوادگی : " + shippingInfo.lastName
                                    + "\n" +"آدرس : " + shippingInfo.address
                                    + "\n" + "تلفن : " +shippingInfo.phone)
                            .setCancelable(true)
                            .setNegativeButton(R.string.OK, new DialogInterface.OnClickListener() {
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
