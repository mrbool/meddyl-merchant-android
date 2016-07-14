package com.gtsoft.meddyl.merchant.views.object;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gtsoft.meddyl.merchant.R;
import com.gtsoft.meddyl.merchant.model.object.Deal;
import com.gtsoft.meddyl.merchant.views.adapter.Deals_Adapter;
import com.gtsoft.meddyl.merchant.views.base.Fragment_Controller;
import com.gtsoft.meddyl.merchant.views.base.Tab_Controller;

public class Deals_Frag extends Fragment_Controller
{
    private SwipeRefreshLayout srlDeal;
    private ListView lvDeals;
    private SwipeRefreshLayout srlNoDeals;

    private Deal[] deals_array;

    private Get_Deals_Async get_deals_task = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.deals_frag, container, false);

        system_controller = getArguments().getParcelable("system_controller");
        merchant_controller = getArguments().getParcelable("merchant_controller");
        deal_controller = getArguments().getParcelable("deal_controller");

        srlDeal = (SwipeRefreshLayout) rootView.findViewById(R.id.srlDeal);
        srlNoDeals = (SwipeRefreshLayout) rootView.findViewById(R.id.srlNoDeals);
//        srlDeal.setOnRefreshListener(this);
//        srlDeal.setColorSchemeColors(android.R.color.holo_green_dark,
//                android.R.color.holo_red_dark,
//                android.R.color.holo_blue_dark,
//                android.R.color.holo_orange_dark);

        srlDeal.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                get_deals_task = new Get_Deals_Async();
                get_deals_task.execute((Void) null);
            }
        });
        srlDeal.setColorScheme(
                android.R.color.holo_blue_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light,
                android.R.color.holo_red_light);

        srlNoDeals.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                get_deals_task = new Get_Deals_Async();
                get_deals_task.execute((Void) null);
            }
        });
        srlNoDeals.setColorScheme(
                android.R.color.holo_blue_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light,
                android.R.color.holo_red_light);

        lvDeals= (ListView)rootView.findViewById(R.id.lvDeals);
        lvDeals.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Deal deal_obj = deals_array[position];
                deal_controller.setDealObj(deal_obj);

                Intent in = new Intent(getActivity(), Deal_Info.class);
                in.putExtra("system_controller", system_controller);
                in.putExtra("merchant_controller", merchant_controller);
                in.putExtra("deal_controller", deal_controller);
                startActivity(in);
            }
        });

        get_deals_task = new Get_Deals_Async();
        get_deals_task.execute((Void) null);

        return rootView;
    }

    public class Get_Deals_Async extends AsyncTask<Void, Void, Boolean>
    {
        private ProgressDialog dialog;

        public Get_Deals_Async()
        {
            dialog = new ProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute()
        {
            dialog.setMessage("Loading Deals");
            dialog.setCancelable(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... login_log_data)
        {
            deal_controller.setMerchantContactObj(merchant_controller.getMerchantContactObj());
            deal_controller.Get_Deals();
            deals_array = deal_controller.getDealObjArray();
            successful = deal_controller.getSuccessful();
            system_error_obj = deal_controller.getSystemErrorObj();
            system_successful_obj = deal_controller.getSystemSuccessfulObj();

            return successful;
        }

        @Override
        protected void onPostExecute(final Boolean success)
        {
            try
            {
                if (dialog.isShowing())
                {
                    dialog.dismiss();
                }

                if (srlDeal.isRefreshing())
                {
                    srlDeal.setRefreshing(false);
                }

                if (srlNoDeals.isRefreshing())
                {
                    srlNoDeals.setRefreshing(false);
                }

                if(successful)
                {
                    Deals_Adapter adapter = new Deals_Adapter(getActivity(), R.layout.deals_list_view, deals_array);
                    lvDeals.setAdapter(adapter);

                    if(deals_array.length > 0)
                    {
                        srlDeal.setVisibility(View.VISIBLE);
                        srlNoDeals.setVisibility(View.GONE);
                    }
                    else
                    {
                        srlDeal.setVisibility(View.GONE);
                        srlNoDeals.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    ((Tab_Controller)getActivity()).Show_Alert_Dialog("Error", system_error_obj.getMessage());
                }
            }
            catch (Exception ex)
            {
                Log.i("error in async", ex.getMessage());
            }
        }

        @Override
        protected void onCancelled()
        {
            get_deals_task = null;

            if (dialog.isShowing())
            {
                dialog.dismiss();
            }
        }
    }
}
