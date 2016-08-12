package com.gtsoft.meddyl.merchant.views.object;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.gtsoft.meddyl.merchant.R;
import com.gtsoft.meddyl.merchant.model.object.Contact;
import com.gtsoft.meddyl.merchant.system.gtsoft.GTTextView;
import com.gtsoft.meddyl.merchant.system.gtsoft.TextWatcherPhone;
import com.gtsoft.meddyl.merchant.system.gtsoft.Utils;
import com.gtsoft.meddyl.merchant.views.base.View_Controller;

import org.droidparts.widget.ClearableEditText;

public class Register_Validation_Create extends View_Controller
{
    private String first_name;
    private String last_name;
    private String email;
    private String phone;

    private Create_Validation_Async create_validation_async = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_validation_create);

        if(debug)
            Debug();

        Intent intent = getIntent();
        system_controller = intent.getParcelableExtra("system_controller");
        merchant_controller = intent.getParcelableExtra("merchant_controller");
        deal_controller = intent.getParcelableExtra("deal_controller");

        screen_title = "VALIDATE";
        left_button = "cancel";
        right_button = "next";

        Set_Controller_Properties();

        ClearableEditText edtPhone = (ClearableEditText) findViewById(R.id.edtPhone);
        edtPhone.addTextChangedListener(new TextWatcherPhone(edtPhone));

        Load_Data();
    }

    private void Load_Data()
    {
        // this avoids null error
        if(merchant_controller.getContactObj() == null)
            merchant_controller.setContactObj(new Contact());

        if(merchant_controller.getContactObj().getContactId() != 0)
        {
            ((GTTextView) findViewById(R.id.txvName)).setText(merchant_controller.getContactObj().getFirstName() + merchant_controller.getContactObj().getLastName());
            ((GTTextView) findViewById(R.id.txvEmail)).setText(merchant_controller.getContactObj().getEmail());

            ((GTTextView) findViewById(R.id.txvName)).setVisibility(View.VISIBLE);
            ((GTTextView) findViewById(R.id.txvEmail)).setVisibility(View.VISIBLE);
        }
        else
        {
            ((LinearLayout) findViewById(R.id.layName)).setVisibility(View.VISIBLE);
            ((ClearableEditText) findViewById(R.id.edtEmail)).setVisibility(View.VISIBLE);
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

    protected void Next()
    {
        first_name = ((ClearableEditText) findViewById(R.id.edtFirstName)).getText().toString().trim();
        last_name = ((ClearableEditText) findViewById(R.id.edtLastName)).getText().toString().trim();
        email = ((ClearableEditText) findViewById(R.id.edtEmail)).getText().toString().trim();
        phone = ((ClearableEditText) findViewById(R.id.edtPhone)).getText().toString().replaceAll("[^\\d]", "");

        successful = true;
        if(first_name.trim().length() == 0 && merchant_controller.getContactObj().getContactId() == 0)
        {
            successful = false;
            error_title = "First Name";
            error_message = "Please enter your first name";
            ((ClearableEditText) findViewById(R.id.edtFirstName)).requestFocus();
        }
        else if(last_name.trim().length() == 0 && merchant_controller.getContactObj().getContactId() == 0)
        {
            successful = false;
            error_title = "Last Name";
            error_message = "Please enter your last name";
            ((ClearableEditText) findViewById(R.id.edtLastName)).requestFocus();
        }
        else if(!Utils.isEmailValid(email) && merchant_controller.getContactObj().getContactId() == 0)
        {
            successful = false;
            error_title = "Email";
            error_message = "Please enter a valid email";
            ((ClearableEditText) findViewById(R.id.edtEmail)).requestFocus();
        }
        else if(phone.trim().length() != 10)
        {
            successful = false;
            error_title = "Phone Number";
            error_message = "Please enter a valid phone number";
            ((ClearableEditText) findViewById(R.id.edtPhone)).requestFocus();
        }

        if(!successful)
        {
            system_error_obj = null;
            Show_Alert_Dialog(error_title, error_message);
        }
        else
        {
            create_validation_async = new Create_Validation_Async();
            create_validation_async.execute((Void) null);
        }
    }

    public void Debug()
    {
        ((ClearableEditText) findViewById(R.id.edtFirstName)).setText("George");
        ((ClearableEditText) findViewById(R.id.edtLastName)).setText("Triarhos");
        ((ClearableEditText) findViewById(R.id.edtEmail)).setText("gtriarhos@hotmail.com");
        ((ClearableEditText) findViewById(R.id.edtPhone)).setText("(858) 699-0966");
    }

    public class Create_Validation_Async extends AsyncTask<Void, Void, Boolean>
    {
        private ProgressDialog dialog;

        public Create_Validation_Async()
        {
            dialog = new ProgressDialog(Register_Validation_Create.this);
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
            if(merchant_controller.getContactObj().getContactId() == 0)
            {
                Contact contact_obj = new Contact();
                contact_obj.setFirstName(first_name);
                contact_obj.setLastName(last_name);
                contact_obj.setEmail(email);
                contact_obj.setPhone(phone);

                merchant_controller.setContactObj(contact_obj);
            }
            else
            {
                merchant_controller.getContactObj().setPhone(phone);
            }

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
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register_Validation_Create.this);
                    builder.setCancelable(false);
                    builder.setTitle("Validation");
                    builder.setMessage(system_successful_obj.getMessage())
                            .setNeutralButton("OK", new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    dialog.cancel();

                                    Intent intent = new Intent(getApplicationContext(), Register_Validate.class);
                                    intent.putExtra("system_controller", system_controller);
                                    intent.putExtra("merchant_controller", merchant_controller);
                                    intent.putExtra("deal_controller", deal_controller);
                                    startActivity(intent);
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
