package com.gtsoft.meddyl.merchant.views.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Toast;

import com.gtsoft.meddyl.merchant.R;
import com.gtsoft.meddyl.merchant.model.object.Fine_Print_Option;
import com.gtsoft.meddyl.merchant.system.gtsoft.GTTextView;

import java.util.ArrayList;
import java.util.Arrays;

public class Fine_Print_Option_Adapter extends ArrayAdapter<Fine_Print_Option>
{

    private Context context;
    private ArrayList<Fine_Print_Option> fine_print_option_obj_array;

    public Fine_Print_Option_Adapter(Context context, int textViewResourceId, Fine_Print_Option[] fine_print_option_obj_array)
    {
        super(context, textViewResourceId, fine_print_option_obj_array);
        this.fine_print_option_obj_array = new ArrayList<Fine_Print_Option>();
        this.fine_print_option_obj_array.addAll(new ArrayList<Fine_Print_Option>(Arrays.asList(fine_print_option_obj_array)));
        this.context = context;
    }

    private class ViewHolder
    {
        GTTextView txvFinePrintOption;
        CheckBox name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        ViewHolder holder = null;

        Log.v("ConvertView", String.valueOf(position));

        if (convertView == null)
        {

            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = vi.inflate(R.layout.deal_fine_print_list_view, null);

            holder = new ViewHolder();
            holder.txvFinePrintOption = (GTTextView) convertView.findViewById(R.id.txvFinePrintOption);
            holder.name = (CheckBox) convertView.findViewById(R.id.ckbFinePrintOption);
            holder.name.setScaleX(1.5f);
            holder.name.setScaleY(1.5f);

            convertView.setTag(holder);

            holder.name.setOnClickListener( new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    CheckBox cb = (CheckBox) v;
                    Fine_Print_Option _fine_print_option = (Fine_Print_Option) cb.getTag();

                    _fine_print_option.setIsSelected(cb.isChecked());
                }
            });

        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        Fine_Print_Option fine_print_option = fine_print_option_obj_array.get(position);

        holder.txvFinePrintOption.setText(fine_print_option.getDisplay());
        holder.name.setChecked(fine_print_option.getIsSelected());
        holder.name.setTag(fine_print_option);

        return convertView;
    }

    public Fine_Print_Option[] getData()
    {
        Fine_Print_Option[] hold = new Fine_Print_Option[fine_print_option_obj_array.size()];

        fine_print_option_obj_array.toArray(hold);

        return hold;
    }

    public Fine_Print_Option[] getCheckedData()
    {
        ArrayList<Fine_Print_Option> checked = new ArrayList<Fine_Print_Option>();

        for (int i = 0; i < fine_print_option_obj_array.size(); i++)
        {
            if(fine_print_option_obj_array.get(i).getIsSelected())
                checked.add(fine_print_option_obj_array.get(i));
        }

        Fine_Print_Option[] hold = new Fine_Print_Option[checked.size()];

        checked.toArray(hold);

        return hold;
    }
}