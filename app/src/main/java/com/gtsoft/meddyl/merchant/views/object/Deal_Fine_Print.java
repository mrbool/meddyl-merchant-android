package com.gtsoft.meddyl.merchant.views.object;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.gtsoft.meddyl.merchant.R;
import com.gtsoft.meddyl.merchant.model.object.Fine_Print_Option;
import com.gtsoft.meddyl.merchant.views.adapter.Fine_Print_Option_Adapter;
import com.gtsoft.meddyl.merchant.views.base.View_Controller;


public class Deal_Fine_Print extends View_Controller
{
    private Get_Fine_Print_Options_Async get_fine_print_options_async = null;
    private Fine_Print_Option_Adapter adapter;
    private ListView lvFinePrintOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deal_fine_print);

        if(debug)
            Debug();

        Intent intent = getIntent();
        system_controller = intent.getParcelableExtra("system_controller");
        merchant_controller = intent.getParcelableExtra("merchant_controller");
        deal_controller = intent.getParcelableExtra("deal_controller");

        screen_title = "FINE PRINT";
        left_button = "back";
        right_button = "next";

        Set_Controller_Properties();

        lvFinePrintOptions = (ListView) findViewById(R.id.lvFinePrintOptions);

        Load_Data();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        deal_controller =  data.getExtras().getParcelable("deal_controller");
        merchant_controller =  data.getExtras().getParcelable("merchant_controller");
        system_controller =  data.getExtras().getParcelable("system_controller");

        Load_Data();
    }

    private void Load_Data()
    {
        if(deal_controller.getFinePrintOptionObjArrayAll() != null)
        {
            adapter = new Fine_Print_Option_Adapter(Deal_Fine_Print.this, R.layout.deal_fine_print_list_view, deal_controller.getFinePrintOptionObjArrayAll());
            lvFinePrintOptions.setAdapter(adapter);
        }
        else
        {
            get_fine_print_options_async = new Get_Fine_Print_Options_Async();
            get_fine_print_options_async.execute((Void) null);
        }
    }

    private void Save_Data()
    {
        Fine_Print_Option [] fine_print_option_array= adapter.getCheckedData();
        deal_controller.getDealObj().setFinePrintOptionObjArray(fine_print_option_array);
        deal_controller.setFinePrintOptionObjArrayAll(adapter.getData());
    }

    @Override
    public void Back()
    {
        Save_Data();

        Intent back_intent = new Intent();
        back_intent.putExtra("deal_controller", deal_controller);
        back_intent.putExtra("merchant_controller", merchant_controller);
        back_intent.putExtra("system_controller", system_controller);
        setResult(1, back_intent);

        finish();
    }

    protected void Next()
    {
        Save_Data();

        Intent intent = new Intent(getApplicationContext(), Deal_Fine_Print_More.class);
        intent.putExtra("system_controller", system_controller);
        intent.putExtra("merchant_controller", merchant_controller);
        intent.putExtra("deal_controller", deal_controller);
        startActivityForResult(intent, 1);
    }

    public void Debug()
    {

    }

    private class Get_Fine_Print_Options_Async extends AsyncTask<Void, Void, Boolean>
    {
        private ProgressDialog dialog;

        public Get_Fine_Print_Options_Async()
        {
            dialog = new ProgressDialog(Deal_Fine_Print.this);
        }

        @Override
        protected void onPreExecute()
        {
            dialog.setMessage("Loading Options");
            dialog.setCancelable(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... login_log_data)
        {
            system_controller.Get_Fine_Print_Options();
            successful = system_controller.getSuccessful();
            system_error_obj = system_controller.getSystemErrorObj();
            system_successful_obj = system_controller.getSystemSuccessfulObj();

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

                if (!successful)
                {
                    Show_Alert_Dialog_Error(Deal_Fine_Print.this);
                }
                else
                {
                    adapter = new Fine_Print_Option_Adapter(Deal_Fine_Print.this, R.layout.deal_fine_print_list_view, system_controller.getFinePrintOptionObjArray());
                    lvFinePrintOptions.setAdapter(adapter);
                }
            }
            catch (Exception ex)
            {
                Log.i("error in async", ex.getMessage().toString());
            }
        }

        @Override
        protected void onCancelled()
        {
            get_fine_print_options_async = null;

            if (dialog.isShowing())
            {
                dialog.dismiss();
            }
        }
    }
}
