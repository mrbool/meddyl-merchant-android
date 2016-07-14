package com.gtsoft.meddyl.merchant.views.object;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.gtsoft.meddyl.merchant.R;
import com.gtsoft.meddyl.merchant.model.object.Industry;
import com.gtsoft.meddyl.merchant.model.object.Merchant;
import com.gtsoft.meddyl.merchant.model.object.Merchant_Contact_Validation;
import com.gtsoft.meddyl.merchant.model.object.Neighborhood;
import com.gtsoft.meddyl.merchant.model.object.Zip_Code;
import com.gtsoft.meddyl.merchant.views.adapter.Industry_Adapter;
import com.gtsoft.meddyl.merchant.views.adapter.Neighborhood_Adapter;
import com.gtsoft.meddyl.merchant.views.base.View_Controller;

import org.droidparts.widget.ClearableEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Register_Location extends View_Controller
{

    private ClearableEditText edtAddress1;
    private ClearableEditText edtAddress2;
    private ClearableEditText edtZipCode;
    private Spinner spnNeighborhood;

    private int neighborhood_id;
    private String neighborhood;
    private String zip_code;

    private Neighborhood_Adapter adapter;
    private Get_Neighborhood_By_Zip_Async get_neighborhood_by_zip_async = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_location);

        Intent intent = getIntent();
        system_controller = intent.getParcelableExtra("system_controller");
        merchant_controller = intent.getParcelableExtra("merchant_controller");
        deal_controller = intent.getParcelableExtra("deal_controller");

        screen_title = "LOCATION";
        left_button = "back";
        right_button = "next";

        Set_Controller_Properties();

        if(merchant_controller.getMerchantObj().getZipCodeObj() == null)
            merchant_controller.getMerchantObj().setZipCodeObj(new Zip_Code());

        if(merchant_controller.getMerchantObj().getNeighborhoodObj() == null)
            merchant_controller.getMerchantObj().setNeighborhoodObj(new Neighborhood());

        edtAddress1 = (ClearableEditText) findViewById(R.id.edtAddress1);
        edtAddress2 = (ClearableEditText) findViewById(R.id.edtAddress2);
        edtZipCode = (ClearableEditText) findViewById(R.id.edtZipCode);
        edtZipCode.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                Get_Neighborhoods();
            }
        });

        spnNeighborhood = (Spinner) findViewById(R.id.spnNeighborhood);
        spnNeighborhood.setVisibility(View.INVISIBLE);

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
        edtAddress1.setText(merchant_controller.getMerchantObj().getAddress1());
        edtAddress2.setText(merchant_controller.getMerchantObj().getAddress2());
        edtZipCode.setText(merchant_controller.getMerchantObj().getZipCodeObj().getZipCode());
    }

    private void Save_Data()
    {
        merchant_controller.getMerchantObj().setAddress1(edtAddress1.getText().toString().trim());
        merchant_controller.getMerchantObj().setAddress2(edtAddress2.getText().toString().trim());
        merchant_controller.getMerchantObj().getZipCodeObj().setZipCode(edtZipCode.getText().toString().trim());
        merchant_controller.getMerchantObj().getNeighborhoodObj().setNeighborhoodId(neighborhood_id);
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
        String address_1 = edtAddress1.getText().toString().trim();
        String address_2 = edtAddress2.getText().toString().trim();
        String zip_code = edtZipCode.getText().toString().trim();

        successful = true;
        if(address_1.length() == 0)
        {
            successful = false;
            error_title = "Need Address";
            error_message = "Address cannot be blank";
            ((ClearableEditText) findViewById(R.id.edtAddress1)).requestFocus();
        }
        else if(zip_code.length() != 5)
        {
            successful = false;
            error_title = "Zip Code Incorrect";
            error_message = "Zip code is incorrect";
            ((ClearableEditText) findViewById(R.id.edtZipCode)).requestFocus();
        }

        if(!successful)
        {
            system_error_obj = null;
            Show_Alert_Dialog(error_title, error_message);
        }
        else
        {
            Neighborhood neighborhood_obj = new Neighborhood();
            neighborhood_obj.setNeighborhoodId(neighborhood_id);
            neighborhood_obj.setNeighborhood(neighborhood);

            merchant_controller.getMerchantObj().setAddress1(address_1);
            merchant_controller.getMerchantObj().setAddress2(address_2);
            merchant_controller.getMerchantObj().setNeighborhoodObj(neighborhood_obj);

            if(system_controller.getZipCodeObj().getNeighborhoodObjArray().length > 0 && neighborhood_id == 0)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(Register_Location.this);
                builder.setCancelable(false);
                builder.setTitle("Neighborhood");
                builder.setMessage("Would you like to add a neighborhood?")
                        .setPositiveButton("Yes", null)
                        .setNegativeButton("No", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton)
                            {
                                Call_Next_Screen();
                            }
                        }).show();
            }
            else
            {
                Call_Next_Screen();
            }
        }
    }

    public void Debug()
    {
        edtAddress1.setText("2600 Torrey Pines Rd");
        edtAddress2.setText("B19");
        edtZipCode.setText("9211");
    }

    private void Get_Neighborhoods()
    {
        zip_code = edtZipCode.getText().toString().trim();
        int length = zip_code.length();

        if(length == 5)
        {
            get_neighborhood_by_zip_async = new Get_Neighborhood_By_Zip_Async();
            get_neighborhood_by_zip_async.execute((Void) null);
        }
    }

    public void Call_Next_Screen()
    {
        Intent intent = new Intent(getApplicationContext(), Register_Description.class);
        intent.putExtra("system_controller", system_controller);
        intent.putExtra("merchant_controller", merchant_controller);
        intent.putExtra("deal_controller", deal_controller);
        startActivityForResult(intent, 1);
    }

    private class Get_Neighborhood_By_Zip_Async extends AsyncTask<Void, Void, Boolean>
    {
        private ProgressDialog dialog;

        public Get_Neighborhood_By_Zip_Async()
        {
            dialog = new ProgressDialog(Register_Location.this);
        }

        @Override
        protected void onPreExecute()
        {
            dialog.setMessage("Validating");
            dialog.setCancelable(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... login_log_data)
        {
            Zip_Code zip_code_obj = new Zip_Code();
            zip_code_obj.setZipCode(zip_code);

            system_controller.setZipCodeObj(zip_code_obj);
            system_controller.Get_Neighborhood_By_Zip();
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
                    Show_Alert_Dialog_Error(Register_Location.this);
                }
                else
                {
                    merchant_controller.getMerchantObj().setZipCodeObj(system_controller.getZipCodeObj());

                    if (system_controller.getZipCodeObj().getNeighborhoodObjArray().length == 0) {
                        spnNeighborhood.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        Neighborhood default_selection = new Neighborhood();
                        default_selection.setNeighborhood("Select Neighborhood");
                        default_selection.setNeighborhoodId(0);

                        List<Neighborhood> hold_list = new ArrayList<Neighborhood>(Arrays.asList(system_controller.getZipCodeObj().getNeighborhoodObjArray()));
                        hold_list.add(default_selection);

                        Neighborhood[] spinner_data = new Neighborhood[hold_list.size()];
                        for (int i = 0; i < hold_list.size(); i++)
                        {
                            spinner_data[i] = hold_list.get(i);
                        }

                        adapter = new Neighborhood_Adapter(Register_Location.this, android.R.layout.simple_spinner_item, spinner_data);
                        spnNeighborhood.setAdapter(adapter);
                        spnNeighborhood.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                        {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                                // Here you get the current item (a User object) that is selected by its position
                                Neighborhood neighborhood_obj = adapter.getItem(position);
                                neighborhood_id = neighborhood_obj.getNeighborhoodId();
                                neighborhood = neighborhood_obj.getNeighborhood();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapter)
                            {
                            }
                        });

                        if(merchant_controller.getMerchantObj().getIndustryObj().getIndustryId() == 0)
                        {
                            spnNeighborhood.setSelection(spinner_data.length - 1);
                        }
                        else
                        {
                            spnNeighborhood.setSelection(adapter.Get_Index_Of_Id(merchant_controller.getMerchantObj().getNeighborhoodObj().getNeighborhoodId()));
                        }

                        spnNeighborhood.setVisibility(View.VISIBLE);
                    }
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
            get_neighborhood_by_zip_async = null;

            if (dialog.isShowing())
            {
                dialog.dismiss();
            }
        }
    }
}
