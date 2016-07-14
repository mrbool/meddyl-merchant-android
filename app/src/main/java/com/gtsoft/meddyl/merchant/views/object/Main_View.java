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
import com.gtsoft.meddyl.merchant.views.base.Tab_Controller;
import com.gtsoft.meddyl.merchant.views.base.View_Controller;


public class Main_View extends View_Controller
{
    private String action="";
    private String user_name;
    private String password;

    private ProgressDialog dialog;

    private Load_System_Settings_Async load_system_settings_async = null;
    private Login_Async login_async = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);

        dialog = new ProgressDialog(Main_View.this);
        dialog.setCancelable(false);
        dialog.setMessage("Loading");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                action = "login";
                load_system_settings_async = new Load_System_Settings_Async();
                load_system_settings_async.execute((Void) null);
            }
        });

        Button btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                action = "register";
                load_system_settings_async = new Load_System_Settings_Async();
                load_system_settings_async.execute((Void) null);
            }
        });

        Auto_Login();
    }

    private void Auto_Login()
    {
        SharedPreferences prefs = getSharedPreferences("app", MODE_PRIVATE);
        user_name = prefs.getString("user_name", null);
        password = prefs.getString("password", null);

        if(user_name != null)
        {
            action = "auto_login";
            load_system_settings_async = new Load_System_Settings_Async();
            load_system_settings_async.execute((Void) null);
        }
        else
        {
            if (dialog.isShowing())
            {
                dialog.dismiss();
            }
        }
    }

    private class Load_System_Settings_Async extends AsyncTask<Void, Void, Boolean>
    {
        public Load_System_Settings_Async()
        {
        }

        @Override
        protected void onPreExecute()
        {
        }

        @Override
        protected Boolean doInBackground(Void... login_log_data)
        {
            system_controller.Get_System_Settings();
            successful = system_controller.getSuccessful();
            system_error_obj = system_controller.getSystemErrorObj();

            return successful;
        }

        @Override
        protected void onPostExecute(final Boolean success)
        {
            try
            {
                if (successful)
                {
                    if(action == "login")
                    {
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        intent.putExtra("system_controller", system_controller);
                        intent.putExtra("merchant_controller", merchant_controller);
                        intent.putExtra("deal_controller", deal_controller);
                        startActivity(intent);
                    }
                    else if (action == "register")
                    {
                        Intent intent = new Intent(getApplicationContext(), Register_Validation_Create.class);
                        intent.putExtra("system_controller", system_controller);
                        intent.putExtra("merchant_controller", merchant_controller);
                        intent.putExtra("deal_controller", deal_controller);
                        startActivity(intent);
                    }
                    else if(action == "auto_login")
                    {
                        login_async = new Login_Async();
                        login_async.execute((Void) null);
                    }
                }
                else
                {
                    Show_Alert_Dialog_Error(Main_View.this);
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
            load_system_settings_async = null;
        }
    }

    private class Login_Async extends AsyncTask<Void, Void, Boolean>
    {
        public Login_Async()
        {
        }

        @Override
        protected void onPreExecute()
        {
        }

        @Override
        protected Boolean doInBackground(Void... login_log_data)
        {
            Contact contact_obj = new Contact();
            contact_obj.setUserName(user_name);
            contact_obj.setPassword(password);

            merchant_controller.getLoginLogObj().setAutoLogin(true);
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