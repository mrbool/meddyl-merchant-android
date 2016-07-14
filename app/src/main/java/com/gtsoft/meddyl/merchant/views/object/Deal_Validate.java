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
import android.widget.Button;

import com.gtsoft.meddyl.merchant.R;
import com.gtsoft.meddyl.merchant.model.object.Deal_Validation;
import com.gtsoft.meddyl.merchant.model.object.Merchant_Contact_Validation;
import com.gtsoft.meddyl.merchant.system.gtsoft.GTTextView;
import com.gtsoft.meddyl.merchant.system.gtsoft.TextWatcherPhone;
import com.gtsoft.meddyl.merchant.system.gtsoft.Utils;
import com.gtsoft.meddyl.merchant.views.base.Tab_Controller;
import com.gtsoft.meddyl.merchant.views.base.View_Controller;

import org.droidparts.widget.ClearableEditText;

public class Deal_Validate extends View_Controller
{
    private ClearableEditText edtCode;
    private String code;
    private Validate_Deal_Async validate_deal_async = null;
    private Send_Deal_Validation_Async send_deal_validation_async = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deal_validate);

        if(debug)
            Debug();

        Intent intent = getIntent();
        system_controller = intent.getParcelableExtra("system_controller");
        merchant_controller = intent.getParcelableExtra("merchant_controller");
        deal_controller = intent.getParcelableExtra("deal_controller");

        screen_title = "VALIDATE";
        left_button = "later";
        right_button = "";

        Set_Controller_Properties();

        edtCode = (ClearableEditText) findViewById(R.id.edtCode);
        edtCode.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                Validate_Code();
            }
        });


        GTTextView txvPhoneNote = (GTTextView) findViewById(R.id.txvPhoneNote);
        txvPhoneNote.setText("Enter the code sent to " + Utils.Format_Phone(merchant_controller.getMerchantContactObj().getContactObj().getPhone()));

        Button btnResend = (Button) findViewById(R.id.btnResend);
        btnResend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                send_deal_validation_async = new Send_Deal_Validation_Async();
                send_deal_validation_async.execute((Void) null);
            }
        });
    }

    private void Validate_Code()
    {
        code = edtCode.getText().toString().trim();
        int length = code.length();

        if(length == 4)
        {
            validate_deal_async = new Validate_Deal_Async();
            validate_deal_async.execute((Void) null);
        }
    }

    public void Later()
    {
        Intent intent = new Intent(getApplicationContext(), Tab_Controller.class);
        intent.putExtra("system_controller", system_controller);
        intent.putExtra("merchant_controller", merchant_controller);
        intent.putExtra("deal_controller", deal_controller);
        startActivity(intent);
    }

    public void Debug()
    {
        ((ClearableEditText) findViewById(R.id.edtCode)).setText("858");
    }

    private class Validate_Deal_Async extends AsyncTask<Void, Void, Boolean>
    {
        private ProgressDialog dialog;

        public Validate_Deal_Async()
        {
            dialog = new ProgressDialog(Deal_Validate.this);
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
            Deal_Validation deal_validation_obj = new Deal_Validation();
            deal_validation_obj.setValidationCode(code);

            deal_controller.setMerchantContactObj(merchant_controller.getMerchantContactObj());
            deal_controller.setDealValidationObj(deal_validation_obj);
            deal_controller.Validate_Deal();
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

                if (successful)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Deal_Validate.this);
                    builder.setCancelable(false);
                    builder.setTitle("Validation");
                    builder.setMessage(system_successful_obj.getMessage())
                            .setNeutralButton("OK", new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    dialog.cancel();

                                    Intent intent = new Intent(getApplicationContext(), Tab_Controller.class);
                                    intent.putExtra("system_controller", system_controller);
                                    intent.putExtra("merchant_controller", merchant_controller);
                                    intent.putExtra("deal_controller", deal_controller);
                                    startActivity(intent);
                                }
                            }).create().show();
                }
                else
                {
                    edtCode.setText("");
                    Show_Alert_Dialog("Error", system_error_obj.getMessage());
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
            validate_deal_async = null;

            if (dialog.isShowing())
            {
                dialog.dismiss();
            }
        }
    }

    private class Send_Deal_Validation_Async extends AsyncTask<Void, Void, Boolean>
    {
        private ProgressDialog dialog;

        public Send_Deal_Validation_Async()
        {
            dialog = new ProgressDialog(Deal_Validate.this);
        }

        @Override
        protected void onPreExecute()
        {
            dialog.setMessage("Sending Validation");
            dialog.setCancelable(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... login_log_data)
        {
            deal_controller.setMerchantContactObj(merchant_controller.getMerchantContactObj());
            deal_controller.Send_Deal_Validation();
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

                if (successful)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Deal_Validate.this);
                    builder.setCancelable(false);
                    builder.setTitle("Validation");
                    builder.setMessage(system_successful_obj.getMessage())
                            .setNeutralButton("OK", new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    dialog.cancel();
                                }
                            }).create().show();
                }
                else
                {
                    Show_Alert_Dialog("Error", system_error_obj.getMessage());
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
            send_deal_validation_async = null;

            if (dialog.isShowing())
            {
                dialog.dismiss();
            }
        }
    }
}
