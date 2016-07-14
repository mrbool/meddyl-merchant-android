package com.gtsoft.meddyl.merchant.views.object;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;

import com.gtsoft.meddyl.merchant.R;
import com.gtsoft.meddyl.merchant.views.base.View_Controller;

public class Deal_Options extends View_Controller
{
    private Switch swtUseImmediately;
    private Switch swtNewCustomers;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deal_options);

        if(debug)
            Debug();

        Intent intent = getIntent();
        system_controller = intent.getParcelableExtra("system_controller");
        merchant_controller = intent.getParcelableExtra("merchant_controller");
        deal_controller = intent.getParcelableExtra("deal_controller");

        screen_title = "OPTIONS";
        left_button = "back";
        right_button = "next";

        Set_Controller_Properties();

        swtUseImmediately = (Switch) findViewById(R.id.swtUseImmediately);
        swtNewCustomers = (Switch) findViewById(R.id.swtNewCustomers);

        ImageView imvUseImmediatelyQuestion = (ImageView) findViewById(R.id.imvUseImmediatelyQuestion);
        imvUseImmediatelyQuestion.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                showPopup(view, system_controller.getSystemSettingsObj().getDealUseImmediatelyInfo());
            }
        });

        ImageView imvNewCustomersQuestion = (ImageView) findViewById(R.id.imvNewCustomersQuestion);
        imvNewCustomersQuestion.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                showPopup(view, system_controller.getSystemSettingsObj().getDealNewCustomerOnlyInfo());
            }
        });

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
        swtUseImmediately.setChecked(deal_controller.getDealObj().getUseDealImmediately());
        swtNewCustomers.setChecked(deal_controller.getDealObj().getIsValidNewCustomerOnly());
    }

    private void Save_Data()
    {
        deal_controller.getDealObj().setUseDealImmediately(swtUseImmediately.isChecked());
        deal_controller.getDealObj().setIsValidNewCustomerOnly(swtNewCustomers.isChecked());
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
        Save_Data();

        Intent intent = new Intent(getApplicationContext(), Deal_Fine_Print.class);
        intent.putExtra("system_controller", system_controller);
        intent.putExtra("merchant_controller", merchant_controller);
        intent.putExtra("deal_controller", deal_controller);
        startActivityForResult(intent, 1);
    }

    public void Debug()
    {

    }
}
