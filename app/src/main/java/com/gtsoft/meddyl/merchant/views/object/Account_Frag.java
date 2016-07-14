package com.gtsoft.meddyl.merchant.views.object;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gtsoft.meddyl.merchant.R;
import com.gtsoft.meddyl.merchant.system.gtsoft.Utils;
import com.gtsoft.meddyl.merchant.views.adapter.Account_Adapter;
import com.gtsoft.meddyl.merchant.views.base.Fragment_Controller;

public class Account_Frag extends Fragment_Controller
{
    private ListView lvMenu;

    /*test git*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.account_frag, container, false);

        system_controller = getArguments().getParcelable("system_controller");
        merchant_controller = getArguments().getParcelable("merchant_controller");
        deal_controller = getArguments().getParcelable("deal_controller");

        final String[] menu_obj = new String[5];
        menu_obj[0] = "Edit Merchant";
        menu_obj[1] = "Edit Contact";
        menu_obj[2] = "Credit Cards";
        menu_obj[3] = "Get the Meddyl App!";
        menu_obj[4] = "Log Out";

        lvMenu= (ListView)rootView.findViewById(R.id.lvMenu);
        lvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String item = menu_obj[position];

                if(item.equals("Edit Merchant"))
                {
                    Intent in = new Intent(getActivity(), Merchant_Edit.class);
                    in.putExtra("system_controller", system_controller);
                    in.putExtra("merchant_controller", merchant_controller);
                    in.putExtra("deal_controller", deal_controller);
                    startActivity(in);
                }
                else if(item.equals("Edit Contact"))
                {
                    Intent in = new Intent(getActivity(), Contact_Edit.class);
                    in.putExtra("system_controller", system_controller);
                    in.putExtra("merchant_controller", merchant_controller);
                    in.putExtra("deal_controller", deal_controller);
                    startActivity(in);
                }
                else if(item.equals("Credit Cards"))
                {
                    Intent in = new Intent(getActivity(), Credit_Cards.class);
                    in.putExtra("system_controller", system_controller);
                    in.putExtra("merchant_controller", merchant_controller);
                    in.putExtra("deal_controller", deal_controller);
                    startActivity(in);
                }
                else if(item.equals("Get the Meddyl App!"))
                {
                    Intent in = new Intent(getActivity(), Customer_App.class);
                    in.putExtra("system_controller", system_controller);
                    in.putExtra("merchant_controller", merchant_controller);
                    in.putExtra("deal_controller", deal_controller);
                    startActivity(in);
                }
                else if(item.equals("Log Out"))
                {
                    Utils.deleteCache(getActivity().getApplicationContext());

                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("app", getActivity().MODE_PRIVATE).edit();
                    editor.clear();
                    editor.commit();

                    Intent intent = new Intent(getActivity(), Main_View.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });

        Account_Adapter adapter = new Account_Adapter(getActivity(), R.layout.account_list_view, menu_obj);
        lvMenu.setAdapter(adapter);

        return rootView;
    }


}
