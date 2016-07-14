package com.gtsoft.meddyl.merchant.views.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gtsoft.meddyl.merchant.R;
import com.gtsoft.meddyl.merchant.model.object.Deal;

import com.androidquery.AQuery;
import com.gtsoft.meddyl.merchant.system.gtsoft.GTTextView;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Account_Adapter extends ArrayAdapter<String>
{
    private Context context;
    private int layoutResourceId;
    private String data[] = null;

    public Account_Adapter(Context context, int layoutResourceId, String[] data)
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

        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/AvenirNext-Medium.ttf");

        GTTextView txvItem = (GTTextView)row.findViewById(R.id.txvItem);
        txvItem.setTypeface(face);
        txvItem.setText(data[position]);

        String item = data[position];
        if(item.equals("Log Out"))
        {
            ImageView imvRightArrow = (ImageView)row.findViewById(R.id.imvRightArrow);
            imvRightArrow.setVisibility(View.GONE);

            RelativeLayout rloTop = (RelativeLayout)row.findViewById(R.id.rloTop);
            rloTop.setVisibility(View.VISIBLE);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            txvItem.setLayoutParams(params);
            txvItem.setTextColor(ColorStateList.valueOf(Color.RED));
        }

        return row;
    }
}
