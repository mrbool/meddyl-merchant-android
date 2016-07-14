package com.gtsoft.meddyl.merchant.views.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gtsoft.meddyl.merchant.R;
import com.gtsoft.meddyl.merchant.model.object.Deal;

import com.androidquery.AQuery;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Deals_Adapter extends ArrayAdapter<Deal>
{
    private Context context;
    private int layoutResourceId;
    private Deal data[] = null;

    public Deals_Adapter(Context context, int layoutResourceId, Deal[] data)
    {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;

        if(convertView==null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
        }

        TextView txvIssued = (TextView)row.findViewById(R.id.txvIssued);
        TextView txvDeal = (TextView)row.findViewById(R.id.txvDeal);
        TextView txvCertificates = (TextView)row.findViewById(R.id.txvCertificates);
        TextView txvStatus = (TextView)row.findViewById(R.id.txvStatus);
        ImageView imgDealImage=(ImageView)row.findViewById(R.id.imgDealImage); // thumb image

        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/AvenirNext-Medium.ttf");
        txvDeal.setTypeface(face);
        txvStatus.setTypeface(face);

        Deal deal_data;
        deal_data = data[position];

        Date entry_date = deal_data.getEntryDateUtcStamp();
        Date expiration_date = deal_data.getExpirationDate();
        SimpleDateFormat date_format =  new SimpleDateFormat("M/d/yyyy");

        txvIssued.setText("Issued on " + date_format.format(entry_date));
        txvDeal.setText(deal_data.getDeal());
        txvCertificates.setText(deal_data.getCertificateQuantity() + " certificates issued");
        txvStatus.setText(deal_data.getDealStatusObj().getStatus());

        AQuery aq = new AQuery(getContext());
        aq.id(imgDealImage).image(deal_data.getImage());

        return row;
    }
}
