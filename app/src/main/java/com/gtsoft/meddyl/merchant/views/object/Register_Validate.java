package com.gtsoft.meddyl.merchant.views.object;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gtsoft.meddyl.merchant.R;
import com.gtsoft.meddyl.merchant.model.object.Contact;
import com.gtsoft.meddyl.merchant.model.object.Merchant_Contact_Validation;
import com.gtsoft.meddyl.merchant.system.gtsoft.GTTextView;
import com.gtsoft.meddyl.merchant.system.gtsoft.TextWatcherPhone;
import com.gtsoft.meddyl.merchant.system.gtsoft.Utils;
import com.gtsoft.meddyl.merchant.views.base.View_Controller;

import org.droidparts.widget.ClearableEditText;

public class Register_Validate extends View_Controller
{
    private ClearableEditText edtCode;
    private String code;
    private Validate_Async validate_async = null;
    private Create_Validation_Async create_validation_async = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_validate);

        if(debug)
            Debug();

        Intent intent = getIntent();
        system_controller = intent.getParcelableExtra("system_controller");
        merchant_controller = intent.getParcelableExtra("merchant_controller");
        deal_controller = intent.getParcelableExtra("deal_controller");

        screen_title = "VALIDATE";
        left_button = "cancel";
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
        txvPhoneNote.setText("Enter the code sent to " + Utils.Format_Phone(merchant_controller.getContactObj().getPhone()));

        Button btnResend = (Button) findViewById(R.id.btnResend);
        btnResend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                create_validation_async = new Create_Validation_Async();
                create_validation_async.execute((Void) null);
            }
        });
    }

    private void Validate_Code()
    {
        code = edtCode.getText().toString().trim();
        int length = code.length();

        if(length == 4)
        {
            validate_async = new Validate_Async();
            validate_async.execute((Void) null);
        }
    }

    protected void Cancel()
    {
        Intent intent = new Intent(getApplicationContext(), Main_View.class);
        intent.putExtra("system_controller", system_controller);
        intent.putExtra("merchant_controller", merchant_controller);
        intent.putExtra("deal_controller", deal_controller);
        startActivity(intent);
    }

    public void Debug()
    {
        ((ClearableEditText) findViewById(R.id.edtCode)).setText("858");
    }

    public class Validate_Async extends AsyncTask<Void, Void, Boolean>
    {
        private ProgressDialog dialog;

        public Validate_Async()
        {
            dialog = new ProgressDialog(Register_Validate.this);
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
            Merchant_Contact_Validation merchant_contact_validation_obj = new Merchant_Contact_Validation();
            merchant_contact_validation_obj.setValidationCode(code);

            merchant_controller.setMerchantContactValidationObj(merchant_contact_validation_obj);
            merchant_controller.Validate();
            successful = merchant_controller.getSuccessful();
            system_error_obj = merchant_controller.getSystemErrorObj();
            system_successful_obj = merchant_controller.getSystemSuccessfulObj();

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
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register_Validate.this);
                    builder.setCancelable(false);
                    builder.setTitle("Successful");
                    builder.setMessage(system_successful_obj.getMessage())
                            .setNeutralButton("OK", new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    dialog.cancel();

                                    Intent intent = new Intent(getApplicationContext(), Register_Merchant_Contact.class);
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
            validate_async = null;

            if (dialog.isShowing())
            {
                dialog.dismiss();
            }
        }
    }

    public class Create_Validation_Async extends AsyncTask<Void, Void, Boolean>
    {
        private ProgressDialog dialog;

        public Create_Validation_Async()
        {
            dialog = new ProgressDialog(Register_Validate.this);
        }

        @Override
        protected void onPreExecute()
        {
            dialog.setMessage("Sending Validation Code");
            dialog.setCancelable(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... login_log_data)
        {
            merchant_controller.Create_Validation();
            successful = merchant_controller.getSuccessful();
            system_error_obj = merchant_controller.getSystemErrorObj();
            system_successful_obj = merchant_controller.getSystemSuccessfulObj();

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
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register_Validate.this);
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
            create_validation_async = null;

            if (dialog.isShowing())
            {
                dialog.dismiss();
            }
        }
    }

}
