package com.gtsoft.meddyl.merchant.views.object;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.androidquery.AQuery;
import com.gtsoft.meddyl.merchant.R;
import com.gtsoft.meddyl.merchant.model.object.Contact;
import com.gtsoft.meddyl.merchant.model.object.Industry;
import com.gtsoft.meddyl.merchant.model.object.Merchant;
import com.gtsoft.meddyl.merchant.model.object.Merchant_Contact;
import com.gtsoft.meddyl.merchant.model.object.Neighborhood;
import com.gtsoft.meddyl.merchant.model.object.Zip_Code;
import com.gtsoft.meddyl.merchant.system.gtsoft.GTTextView;
import com.gtsoft.meddyl.merchant.system.gtsoft.TextWatcherPhone;
import com.gtsoft.meddyl.merchant.system.gtsoft.Utils;
import com.gtsoft.meddyl.merchant.views.base.View_Controller;
import com.lyft.android.scissors.CropView;

import org.droidparts.widget.ClearableEditText;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class Contact_Edit extends View_Controller
{
    private ProgressDialog dialog;

    private ClearableEditText edtFirstName;
    private GTTextView txvPhone;
    private ClearableEditText edtLastName;
    private ClearableEditText edtEmail;
    private ClearableEditText edtJobTitle;

    private Get_Merchant_Contact_Async get_merchant_contact_async = null;
    private Update_Merchant_Contact_Async update_merchant_contact_async = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_edit);

        dialog = new ProgressDialog(Contact_Edit.this);
        dialog.setMessage("Loading");
        dialog.setCancelable(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();

        if(debug)
            Debug();

        Intent intent = getIntent();
        system_controller = intent.getParcelableExtra("system_controller");
        merchant_controller = intent.getParcelableExtra("merchant_controller");
        deal_controller = intent.getParcelableExtra("deal_controller");

        screen_title = "EDIT";
        left_button = "back";
        right_button = "save";

        Set_Controller_Properties();

        txvPhone = (GTTextView) findViewById(R.id.txvPhone);
        edtFirstName = (ClearableEditText) findViewById(R.id.edtFirstName);
        edtLastName = (ClearableEditText) findViewById(R.id.edtLastName);
        edtEmail = (ClearableEditText) findViewById(R.id.edtEmail);
        edtJobTitle = (ClearableEditText) findViewById(R.id.edtJobTitle);

        get_merchant_contact_async = new Get_Merchant_Contact_Async();
        get_merchant_contact_async.execute((Void) null);
    }


    private void Load_Data()
    {
        Merchant_Contact merchant_contact_obj = merchant_controller.getMerchantContactObj();
        Contact contact_obj = merchant_contact_obj.getContactObj();

        edtFirstName.setText(contact_obj.getFirstName());
        txvPhone.setText(Utils.Format_Phone(contact_obj.getPhone()));
        edtLastName.setText(contact_obj.getLastName());
        edtEmail.setText(contact_obj.getEmail());
        edtJobTitle.setText(merchant_contact_obj.getTitle());

        edtFirstName.setVisibility(View.VISIBLE);
        txvPhone.setVisibility(View.VISIBLE);
        edtLastName.setVisibility(View.VISIBLE);
        edtEmail.setVisibility(View.VISIBLE);
        edtJobTitle.setVisibility(View.VISIBLE);

        if (dialog.isShowing())
        {
            dialog.dismiss();
        }

        hideKeyboard();
    }

    public void Save()
    {
        String first_name = edtFirstName.getText().toString().trim();
        String last_name = edtLastName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String title = edtJobTitle.getText().toString().trim();

        successful = true;
        if(first_name.length() == 0)
        {
            successful = false;
            error_title = "Need First Name";
            error_message = "First name cannot be blank";
            edtFirstName.requestFocus();
        }
        else if(last_name.length() == 0)
        {
            successful = false;
            error_title = "Last Name";
            error_message = "Last name cannot be blank";
            edtLastName.requestFocus();
        }
        else if(!Utils.isEmailValid(email))
        {
            successful = false;
            error_title = "Email";
            error_message = "Please enter a valid email";
            edtJobTitle.requestFocus();
        }
        else if(title.length() == 0)
        {
            successful = false;
            error_title = "Job Title";
            error_message = "Job title cannot be blank";
            edtJobTitle.requestFocus();
        }

        if(!successful)
        {
            system_error_obj = null;
            Show_Alert_Dialog(error_title, error_message);
        }
        else
        {
            /* reset to this if it fails */
            Merchant_Contact merchant_contact_obj_update = merchant_controller.getMerchantContactObj();

            Contact contact_obj = new Contact();
            contact_obj.setFirstName(first_name);
            contact_obj.setLastName(last_name);
            contact_obj.setEmail(email);
            contact_obj.setPhone(merchant_controller.getContactObj().getPhone());

            merchant_controller.getMerchantContactObj().setTitle(title);
            merchant_controller.setContactObj(contact_obj);

            update_merchant_contact_async = new Update_Merchant_Contact_Async();
            update_merchant_contact_async.execute((Void) null);
        }
    }

    public void Debug()
    {

    }

    private class Get_Merchant_Contact_Async extends AsyncTask<Void, Void, Boolean>
    {
        public Get_Merchant_Contact_Async()
        {
        }

        @Override
        protected void onPreExecute()
        {
        }

        @Override
        protected Boolean doInBackground(Void... login_log_data)
        {
            merchant_controller.Get_Merchant_Contact();
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
                if (successful)
                {
                    Load_Data();
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
            get_merchant_contact_async = null;
        }
    }

    private class Update_Merchant_Contact_Async extends AsyncTask<Void, Void, Boolean>
    {
        private ProgressDialog dialog;

        public Update_Merchant_Contact_Async()
        {
            dialog = new ProgressDialog(Contact_Edit.this);
        }

        @Override
        protected void onPreExecute()
        {
            dialog.setMessage("Updating");
            dialog.setCancelable(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... login_log_data)
        {
            merchant_controller.Update_Merchant_Contact();
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(Contact_Edit.this);
                    builder.setCancelable(false);
                    builder.setTitle("Successful");
                    builder.setMessage(system_successful_obj.getMessage())
                            .setNeutralButton("OK", new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    dialog.cancel();

                                    finish();
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
            update_merchant_contact_async = null;

            if (dialog.isShowing())
            {
                dialog.dismiss();
            }
        }
    }
}
