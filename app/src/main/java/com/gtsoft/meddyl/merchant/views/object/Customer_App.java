package com.gtsoft.meddyl.merchant.views.object;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gtsoft.meddyl.merchant.R;
import com.gtsoft.meddyl.merchant.views.base.View_Controller;

public class Customer_App extends View_Controller
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_app);

        Intent intent = getIntent();
        system_controller = intent.getParcelableExtra("system_controller");
        merchant_controller = intent.getParcelableExtra("merchant_controller");
        deal_controller = intent.getParcelableExtra("deal_controller");

        screen_title = "MEDDYL";
        left_button = "back";
        right_button = "";

        Set_Controller_Properties();
        Button btnInstall = (Button) findViewById(R.id.btnInstall);
        btnInstall.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + system_controller.getSystemSettingsObj().getCustomerAppAndroidId())));
                }
                catch (android.content.ActivityNotFoundException anfe)
                {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="  + system_controller.getSystemSettingsObj().getCustomerAppAndroidId())));
                }
            }
        });
    }


}
