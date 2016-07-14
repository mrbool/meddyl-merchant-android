package com.gtsoft.meddyl.merchant.views.object;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.Element;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.gtsoft.meddyl.merchant.R;
import com.gtsoft.meddyl.merchant.model.object.Credit_Card;
import com.gtsoft.meddyl.merchant.model.object.Industry;
import com.gtsoft.meddyl.merchant.model.object.Merchant;
import com.gtsoft.meddyl.merchant.system.gtsoft.GTTextView;
import com.gtsoft.meddyl.merchant.system.gtsoft.TextWatcherPhone;
import com.gtsoft.meddyl.merchant.system.gtsoft.Utils;
import com.gtsoft.meddyl.merchant.views.adapter.Industry_Adapter;
import com.gtsoft.meddyl.merchant.views.base.View_Controller;

import org.droidparts.widget.ClearableEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Register_Merchant extends View_Controller
{
    private ClearableEditText edtCompanyName;
    private ClearableEditText edtCompanyPhone;
    private ClearableEditText edtWebsite;
    private ClearableEditText edtJobTitle;

    private Spinner spnIndustry;
    private Industry_Adapter adapter;
    int industry_id;

    private Load_Industries_Async load_industries_async = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_merchant);

        Intent intent = getIntent();
        system_controller = intent.getParcelableExtra("system_controller");
        merchant_controller = intent.getParcelableExtra("merchant_controller");
        deal_controller = intent.getParcelableExtra("deal_controller");

        screen_title = "MERCHANT";
        left_button = "back";
        right_button = "next";

        Set_Controller_Properties();

        if(merchant_controller.getMerchantObj() == null)
            merchant_controller.setMerchantObj(new Merchant());

        if(merchant_controller.getMerchantObj().getIndustryObj() == null)
            merchant_controller.getMerchantObj().setIndustryObj(new Industry());

        edtCompanyName = (ClearableEditText) findViewById(R.id.edtCompanyName);
        edtCompanyPhone = (ClearableEditText) findViewById(R.id.edtCompanyPhone);
        edtWebsite = (ClearableEditText) findViewById(R.id.edtWebsite);
        spnIndustry = (Spinner) findViewById(R.id.spnIndustry);
        edtJobTitle = (ClearableEditText) findViewById(R.id.edtJobTitle);

        Load_Industries();

        Load_Data();

        if(debug)
            Debug();

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
        edtCompanyName.setText(merchant_controller.getMerchantObj().getCompanyName());
        edtCompanyPhone.setText(merchant_controller.getMerchantObj().getPhone());
        edtWebsite.setText(merchant_controller.getMerchantObj().getWebsite());
        edtJobTitle.setText(merchant_controller.getMerchantContactObj().getTitle());
    }

    private void Save_Data()
    {
        merchant_controller.getMerchantObj().setCompanyName(edtCompanyName.getText().toString().trim());
        merchant_controller.getMerchantObj().setPhone(edtCompanyPhone.getText().toString().trim());
        merchant_controller.getMerchantObj().setWebsite(edtWebsite.getText().toString().trim());
        merchant_controller.getMerchantContactObj().setTitle(edtJobTitle.getText().toString().trim());
        merchant_controller.getMerchantObj().getIndustryObj().setIndustryId(industry_id);
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
        String company_name = edtCompanyName.getText().toString().trim();
        String company_phone = edtCompanyPhone.getText().toString().replaceAll("[^\\d]", "");
        String website = edtWebsite.getText().toString().trim();
        String title = edtJobTitle.getText().toString().trim();

        successful = true;
        if(company_name.length() == 0)
        {
            successful = false;
            error_title = "Need Company Name";
            error_message = "Company name cannot be blank";
            edtCompanyName.requestFocus();
        }
        else if(company_phone.length() == 0)
        {
            successful = false;
            error_title = "Phone Incorrect";
            error_message = "You must enter a valid phone number";
            edtCompanyPhone.requestFocus();
        }
        else if(website.length() == 0)
        {
            successful = false;
            error_title = "Need Website";
            error_message = "Please enter your company's website or yelp link";
            edtWebsite.requestFocus();
        }
        else if(industry_id == 0)
        {
            successful = false;
            error_title = "Need Industry";
            error_message = "Please select and industry";
            hideKeyboard();
            spnIndustry.setFocusable(true);
            spnIndustry.setFocusableInTouchMode(true);
            spnIndustry.requestFocus();
        }
        else if(title.length() == 0)
        {
            successful = false;
            error_title = "Need Title";
            error_message = "Please enter a title";
            edtJobTitle.requestFocus();
        }

        if(!successful)
        {
            system_error_obj = null;
            Show_Alert_Dialog(error_title, error_message);
        }
        else
        {
            Industry industry_obj = new Industry();
            industry_obj.setIndustryId(industry_id);

            merchant_controller.getMerchantObj().setCompanyName(company_name);
            merchant_controller.getMerchantObj().setPhone(company_phone);
            merchant_controller.getMerchantObj().setWebsite(website);
            merchant_controller.getMerchantObj().setIndustryObj(industry_obj);
            merchant_controller.getMerchantContactObj().setTitle(title);

            Intent intent = new Intent(getApplicationContext(), Register_Location.class);
            intent.putExtra("system_controller", system_controller);
            intent.putExtra("merchant_controller", merchant_controller);
            intent.putExtra("deal_controller", deal_controller);
            startActivityForResult(intent, 1);
        }
    }

    public void Debug()
    {
        edtCompanyName.setText("George's Donuts");
        edtCompanyPhone.setText("(858) 699-0966");
        edtWebsite.setText("www.meddyl.com");
        edtJobTitle.setText("Manager");
    }

    private void Load_Industries()
    {
        load_industries_async = new Load_Industries_Async();
        load_industries_async.execute((Void) null);
    }

    private class Load_Industries_Async extends AsyncTask<Void, Void, Boolean>
    {
        private ProgressDialog dialog;

        public Load_Industries_Async()
        {
            dialog = new ProgressDialog(Register_Merchant.this);
        }

        @Override
        protected void onPreExecute()
        {
            dialog.setMessage("Loading");
            dialog.setCancelable(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... login_log_data)
        {
            Industry industry_obj = new Industry();
            industry_obj.setParentIndustryId(2);

            system_controller.setIndustryObj(industry_obj);
            system_controller.Get_Industry_Parent_Level();
            successful = system_controller.getSuccessful();
            system_error_obj = system_controller.getSystemErrorObj();

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
                    Show_Alert_Dialog_Error(Register_Merchant.this);
                }
                else
                {
                    Industry default_selection = new Industry();
                    default_selection.setIndustry("Select Industry");
                    default_selection.setIndustryId(0);

                    List<Industry> hold_list = new ArrayList<Industry>(Arrays.asList(system_controller.getIndustryObjArray()));
                    hold_list.add(default_selection);

                    Industry[] industry_spinner = new Industry[hold_list.size()];
                    for (int i = 0; i < hold_list.size(); i++)
                    {
                        industry_spinner[i] = hold_list.get(i);
                    }

                    adapter = new Industry_Adapter(Register_Merchant.this, android.R.layout.simple_spinner_item, industry_spinner);
                    spnIndustry.setAdapter(adapter);

                    // You can create an anonymous listener to handle the event when is selected an spinner item
                    spnIndustry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                    {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
                        {
                            Industry industry_obj = adapter.getItem(position);
                            industry_id = industry_obj.getIndustryId();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapter) {  }
                    });

                    if(merchant_controller.getMerchantObj().getIndustryObj().getIndustryId() == 0)
                        spnIndustry.setSelection(industry_spinner.length - 1);
                    else
                        spnIndustry.setSelection(adapter.Get_Index_Of_Id(merchant_controller.getMerchantObj().getIndustryObj().getIndustryId()));
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
            load_industries_async = null;

            if (dialog.isShowing())
            {
                dialog.dismiss();
            }
        }
    }
}
