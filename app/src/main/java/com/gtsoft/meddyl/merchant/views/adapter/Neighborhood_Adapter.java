package com.gtsoft.meddyl.merchant.views.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gtsoft.meddyl.merchant.model.object.Neighborhood;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Neighborhood_Adapter extends ArrayAdapter<Neighborhood>
{

    // Your sent context
    private Context context;
    // Your custom values for the spinner (User)
    private Neighborhood[] values;

    public Neighborhood_Adapter(Context context, int textViewResourceId, Neighborhood[] values)
    {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    public int getCount()
    {
        return values.length;

//        int count = super.getCount();
//        return values.length > 0 ? values.length - 1 : values.length;
    }

    public Neighborhood getItem(int position)
    {
        return values[position];
    }

    public long getItemId(int position)
    {
        return position;
    }


    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = new TextView(context);
        label.setTextSize(17);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        label.setText(values[position].getNeighborhood());

        if(label.getText().equals("Select Neighborhood"))
            label.setTextColor(Color.LTGRAY);
        else
            label.setTextColor(Color.BLACK);

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        TextView label = new TextView(context);
        label.setTextSize(20);
        label.setTextColor(Color.BLACK);
        label.setText(values[position].getNeighborhood());

        return label;
    }

    public int Get_Index_Of_Id(int id)
    {
        for (int i = 0; i < values.length; i++)
        {
            if (values[i].getNeighborhoodId() == id)
            {
                return i;
            }
        }

        return -1;
    }

}