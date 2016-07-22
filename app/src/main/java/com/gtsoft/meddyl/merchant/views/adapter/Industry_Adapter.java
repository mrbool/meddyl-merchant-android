package com.gtsoft.meddyl.merchant.views.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gtsoft.meddyl.merchant.R;
import com.gtsoft.meddyl.merchant.model.object.Industry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Industry_Adapter extends ArrayAdapter<Industry>
{

    // Your sent context
    private Context context;
    // Your custom values for the spinner (User)
    private Industry[] values;

    public Industry_Adapter(Context context, int textViewResourceId,
                       Industry[] values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    public int getCount(){

        return values.length;

//        int count = super.getCount();
//        return values.length > 0 ? values.length - 1 : values.length;
    }

    public Industry getItem(int position){
        return values[position];
    }

    public long getItemId(int position){
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
        label.setText(values[position].getIndustry());

        if(label.getText().equals("Select Industry"))
            label.setTextColor(context.getResources().getColor(R.color.light_gray));
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
        label.setText(values[position].getIndustry());

        return label;
    }

    public int Get_Index_Of_Id(int id)
    {
        for (int i = 0; i < values.length; i++)
        {
            if (values[i].getIndustryId() == id)
            {
                return i;
            }
        }

        return -1;
    }

    public Industry[] Add_Default(Industry[] values)
    {
        Industry default_selection = new Industry();
        default_selection.setIndustry("Select Industry");
        default_selection.setIndustryId(-1);

        List<Industry> industry_list = new ArrayList<Industry>(Arrays.asList(values));
        industry_list.add(default_selection);

        Industry[] test = new Industry[industry_list.size()];
        for (int i = 0; i < industry_list.size(); i++)
        {
            test[i] = industry_list.get(i);
        }

        return null;
    }
}