package com.gtsoft.meddyl.merchant.views.object;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.gtsoft.meddyl.merchant.R;
import com.gtsoft.meddyl.merchant.model.object.Contact;
import com.gtsoft.meddyl.merchant.system.gtsoft.GTTextView;
import com.gtsoft.meddyl.merchant.views.base.Tab_Controller;
import com.gtsoft.meddyl.merchant.views.base.View_Controller;

import org.droidparts.widget.ClearableEditText;

public class Login extends View_Controller
{
    String user_name;
    String password;

    private Login_Async login_async = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        if(debug)
            Debug();

        Intent intent = getIntent();
        system_controller = intent.getParcelableExtra("system_controller");
        merchant_controller = intent.getParcelableExtra("merchant_controller");
        deal_controller = intent.getParcelableExtra("deal_controller");

        screen_title = "LOGIN";
        left_button = "back";
        right_button = "";

        Set_Controller_Properties();

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Login();
            }
        });

        GTTextView txvForgotPassword = (GTTextView) findViewById(R.id.txvForgotPassword);
        txvForgotPassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(), Forgot_Password.class);
                intent.putExtra("system_controller", system_controller);
                intent.putExtra("merchant_controller", merchant_controller);
                intent.putExtra("deal_controller", deal_controller);
                startActivity(intent);
            }
        });
    }

    private void Login()
    {
        user_name = ((ClearableEditText) findViewById(R.id.edtEmail)).getText().toString().trim();
        password = ((ClearableEditText) findViewById(R.id.edtPassword)).getText().toString().trim();

        successful = true;
        if(user_name.trim().length() == 0)
        {
            successful = false;
            error_message = "Please enter your email";
            error_title = "Need Email";
            ((ClearableEditText) findViewById(R.id.edtEmail)).requestFocus();
        }
        else if(password.trim().length() == 0)
        {
            successful = false;
            error_message = "Please enter your password";
            error_title = "Need Password";
            ((ClearableEditText) findViewById(R.id.edtPassword)).requestFocus();
        }

        if(!successful)
        {
            system_error_obj = null;
            Show_Alert_Dialog(error_title, error_message);
        }
        else
        {
            login_async = new Login_Async();
            login_async.execute((Void) null);
        }
    }

    public void Debug()
    {
        ((ClearableEditText) findViewById(R.id.edtEmail)).setText("gtriarhos@gmail.com");
        ((ClearableEditText) findViewById(R.id.edtPassword)).setText("test12");
    }

    private class Login_Async extends AsyncTask<Void, Void, Boolean>
    {
        private ProgressDialog dialog;

        public Login_Async()
        {
            dialog = new ProgressDialog(Login.this);
        }

        @Override
        protected void onPreExecute()
        {
            dialog.setMessage("Logging In");
            dialog.setCancelable(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... login_log_data)
        {
            Contact contact_obj = new Contact();
            contact_obj.setUserName(user_name);
            contact_obj.setPassword(password);

            merchant_controller.getLoginLogObj().setAutoLogin(false);
            merchant_controller.setContactObj(contact_obj);
            merchant_controller.Login();
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
                    SharedPreferences.Editor editor = getSharedPreferences("app", MODE_PRIVATE).edit();
                    editor.putString("user_name", user_name);
                    editor.putString("password", password);
                    editor.apply();

                    system_controller.getLoginLogObj().setMerchantContactId(merchant_controller.getMerchantContactObj().getMerchantContactId());
                    merchant_controller.getLoginLogObj().setMerchantContactId(merchant_controller.getMerchantContactObj().getMerchantContactId());
                    deal_controller.getLoginLogObj().setMerchantContactId(merchant_controller.getMerchantContactObj().getMerchantContactId());

                    Intent intent = new Intent(getApplicationContext(), Tab_Controller.class);
                    intent.putExtra("system_controller", system_controller);
                    intent.putExtra("merchant_controller", merchant_controller);
                    intent.putExtra("deal_controller", deal_controller);
                    intent.putExtra("selected_tab", 0);
                    startActivity(intent);
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
            login_async = null;

            if (dialog.isShowing())
            {
                dialog.dismiss();
            }
        }
    }

}
