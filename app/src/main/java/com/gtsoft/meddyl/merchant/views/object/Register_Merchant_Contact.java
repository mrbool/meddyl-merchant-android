package com.gtsoft.meddyl.merchant.views.object;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import com.gtsoft.meddyl.merchant.R;
import com.gtsoft.meddyl.merchant.model.object.Contact;
import com.gtsoft.meddyl.merchant.model.object.Merchant_Contact;
import com.gtsoft.meddyl.merchant.system.gtsoft.GTTextView;
import com.gtsoft.meddyl.merchant.system.gtsoft.Utils;
import com.gtsoft.meddyl.merchant.views.base.View_Controller;

import org.droidparts.widget.ClearableEditText;


public class Register_Merchant_Contact extends View_Controller
{
    private ClearableEditText edtEmail;
    private ClearableEditText edtPassword;
    private ClearableEditText edtPasswordConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_merchant_contact);

        Intent intent = getIntent();
        system_controller = intent.getParcelableExtra("system_controller");
        merchant_controller = intent.getParcelableExtra("merchant_controller");
        deal_controller = intent.getParcelableExtra("deal_controller");

        screen_title = "CONTACT";
        left_button = "cancel";
        right_button = "next";

        Set_Controller_Properties();

        edtEmail = (ClearableEditText) findViewById(R.id.edtEmail);
        edtPassword = (ClearableEditText) findViewById(R.id.edtPassword);
        edtPasswordConfirm = (ClearableEditText) findViewById(R.id.edtPasswordConfirm);

        GTTextView txvTerms = (GTTextView) findViewById(R.id.txvTerms);
        txvTerms.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(), Terms_Of_Service.class);
                intent.putExtra("system_controller", system_controller);
                intent.putExtra("merchant_controller", merchant_controller);
                intent.putExtra("deal_controller", deal_controller);
                startActivity(intent);
            }
        });

        if(debug)
            Debug();

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
        ((GTTextView) findViewById(R.id.txvName)).setText(merchant_controller.getContactObj().getFirstName() + " " +  merchant_controller.getContactObj().getLastName());
        ((GTTextView) findViewById(R.id.txvPhone)).setText(Utils.Format_Phone(merchant_controller.getContactObj().getPhone()));
        edtEmail.setText(merchant_controller.getContactObj().getEmail());
        ((ClearableEditText) findViewById(R.id.edtPassword)).requestFocus();
    }

    private void Save_Data()
    {
        merchant_controller.getContactObj().setEmail(((ClearableEditText) findViewById(R.id.edtEmail)).getText().toString().trim());
        merchant_controller.getContactObj().setPassword(((ClearableEditText) findViewById(R.id.edtPassword)).getText().toString().trim());
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
        String email = ((ClearableEditText) findViewById(R.id.edtEmail)).getText().toString().trim();
        String password = ((ClearableEditText) findViewById(R.id.edtPassword)).getText().toString().trim();
        String password_confirm = ((ClearableEditText) findViewById(R.id.edtPasswordConfirm)).getText().toString().trim();

        successful = true;
        if(!Utils.isEmailValid(email))
        {
            successful = false;
            error_title = "Need Email";
            error_message = "You must a valid email address";
            ((ClearableEditText) findViewById(R.id.edtEmail)).requestFocus();
        }
        else if(password.length() < 5)
        {
            successful = false;
            error_title = "Password Incorrect";
            error_message = "Your password must be at least 5 characters";
            ((ClearableEditText) findViewById(R.id.edtPassword)).requestFocus();
        }
        else if(!password.equals(password_confirm))
        {
            successful = false;
            error_title = "Password Incorrect";
            error_message = "Your passwords do not match";
            ((ClearableEditText) findViewById(R.id.edtPassword)).requestFocus();
        }

        if(!successful)
        {
            system_error_obj = null;
            Show_Alert_Dialog(error_title, error_message);
        }
        else
        {
            merchant_controller.getContactObj().setEmail(email);
            merchant_controller.getContactObj().setPassword(password);

            Intent intent = new Intent(getApplicationContext(), Register_Merchant.class);
            intent.putExtra("system_controller", system_controller);
            intent.putExtra("merchant_controller", merchant_controller);
            intent.putExtra("deal_controller", deal_controller);
            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void Cancel()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(Register_Merchant_Contact.this);
        builder.setCancelable(false);
        builder.setTitle("Cancel");
        builder.setMessage("Are you sure you want to cancel?  Your info will be lost.")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        Intent intent = new Intent(getApplicationContext(), Main_View.class);
                        intent.putExtra("system_controller", system_controller);
                        intent.putExtra("merchant_controller", merchant_controller);
                        intent.putExtra("deal_controller", deal_controller);
                        startActivity(intent);
                    }
                }).show();
    }

    public void Debug()
    {
//        if(merchant_controller.getContactObj() == null)
//            merchant_controller.setContactObj(new Contact());
//
//        merchant_controller.getContactObj().setFirstName("George");
//        merchant_controller.getContactObj().setLastName("Triarhos");
//        merchant_controller.getContactObj().setPhone("8586990966");
//        merchant_controller.getContactObj().setEmail("gtriarhos@hotmail.com");
//
//        if(merchant_controller.getMerchantContactObj() == null)
//            merchant_controller.setMerchantContactObj(new Merchant_Contact());
//
//        merchant_controller.getMerchantContactObj().setTitle("TEST Title");

        edtEmail.setText("gtriarhos@hotmail.com");
        edtPassword.setText("test12");
        edtPasswordConfirm.setText("test12");
    }
}
