package ir.highteam.ecommercekelar.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ir.highteam.ecommercekelar.R;
import ir.highteam.ecommercekelar.bundle.BundleAgency;
import ir.highteam.ecommercekelar.view.CustomAlertDialog;

/**
 * Created by mohammad hosein on 8/27/2016.
 */
public class AdapterAgencyList extends RecyclerView.Adapter<AdapterAgencyList.AgencyViewHolder> {

    private List<BundleAgency> agencyList;
    private Context contextMain;
    Typeface tfSans;

    public AdapterAgencyList(List<BundleAgency> agencyList, Context context) {
        this.agencyList = agencyList;
        this.contextMain = context;
        tfSans = Typeface.createFromAsset(contextMain.getAssets(), context.getString(R.string.FONT_IRAN_SANS));
    }

    public void insertItem(BundleAgency moreAgencyItem){
        this.agencyList.add(moreAgencyItem);
    }

    @Override
    public int getItemCount() {
        return agencyList.size();
    }

    @Override
    public void onBindViewHolder(AgencyViewHolder agencyViewHolder, final int position) {

        BundleAgency bundleAgency = agencyList.get(position);

        agencyViewHolder.agencyName.setText(bundleAgency.name);
        agencyViewHolder.agencyPhone.setText(bundleAgency.phone);
        agencyViewHolder.agencyCity.setText(bundleAgency.city);
        agencyViewHolder.agencyName.setTypeface(tfSans);
        agencyViewHolder.agencyPhone.setTypeface(tfSans);
        agencyViewHolder.agencyCity.setTypeface(tfSans);

        agencyViewHolder.name = bundleAgency.name;
        agencyViewHolder.phone = bundleAgency.phone;
        agencyViewHolder.city = bundleAgency.city;
        agencyViewHolder.address = bundleAgency.address;

    }

    @Override
    public AgencyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_agency_item, viewGroup, false);
        AgencyViewHolder agency = new AgencyViewHolder(itemView);
        return agency;
    }

    public class AgencyViewHolder extends RecyclerView.ViewHolder {

        protected TextView agencyName;
        protected TextView agencyPhone;
        protected TextView agencyCity;
        protected String name,phone,city,address;

        public AgencyViewHolder(View v) {
            super(v);

            contextMain = v.getContext();
            agencyName = (TextView) v.findViewById(R.id.txtName);
            agencyPhone = (TextView) v.findViewById(R.id.txtPhone);
            agencyCity = (TextView) v.findViewById(R.id.txtCity);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CustomAlertDialog customAlertDialog = new CustomAlertDialog(contextMain);

                    AlertDialog dialog = new AlertDialog.Builder(contextMain)
                            .setCustomTitle(customAlertDialog.getTitleText(contextMain.getString(R.string.AGENCY_INFO)))
                            .setMessage("نام : " +name+"\n" + "شهر : " + city + "\n" + "تلفن : "  + phone + "\n" + "آدرس : " + address)
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
