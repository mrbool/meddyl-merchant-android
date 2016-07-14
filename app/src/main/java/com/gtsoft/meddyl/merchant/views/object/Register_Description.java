package com.gtsoft.meddyl.merchant.views.object;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.widget.EditText;

import com.gtsoft.meddyl.merchant.R;
import com.gtsoft.meddyl.merchant.model.object.Industry;
import com.gtsoft.meddyl.merchant.model.object.Merchant;
import com.gtsoft.meddyl.merchant.system.gtsoft.GTTextView;
import com.gtsoft.meddyl.merchant.views.base.View_Controller;

import org.droidparts.widget.ClearableEditText;

public class Register_Description extends View_Controller
{
    private ClearableEditText edtDescription;
    private GTTextView txvCharacterCount;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_description);

        Intent intent = getIntent();
        system_controller = intent.getParcelableExtra("system_controller");
        merchant_controller = intent.getParcelableExtra("merchant_controller");
        deal_controller = intent.getParcelableExtra("deal_controller");

        screen_title = "DESCRIPTION";
        left_button = "back";
        right_button = "next";

        Set_Controller_Properties();

//        InputFilter[] FilterArray = new InputFilter[1];
//        FilterArray[0] = new InputFilter.LengthFilter(system_controller.getSystemSettingsObj().getCustomerDescriptionCharacters());

        edtDescription = (ClearableEditText) findViewById(R.id.edtDescription);
//        edtDescription.setFilters(FilterArray);
//        edtDescription.addTextChangedListener(new TextWatcher() {
//
//            public void afterTextChanged(Editable s) {
//            }
//
//            public void beforeTextChanged(CharSequence s, int start,
//                                          int count, int after) {
//            }
//
//            public void onTextChanged(CharSequence s, int start,
//                                      int before, int count)
//            {
//                Set_Character_Count();
//            }
//        });

        txvCharacterCount = (GTTextView) findViewById(R.id.txvCharacterCount);
//        txvCharacterCount.setText(String.valueOf(system_controller.getSystemSettingsObj().getCustomerDescriptionCharacters()) + " characters left");

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
        edtDescription.setText(merchant_controller.getMerchantObj().getDescription());
    }

    private void Save_Data()
    {
        merchant_controller.getMerchantObj().setDescription(edtDescription.getText().toString().trim());
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
        String description = edtDescription.getText().toString().trim();

        successful = true;
        if(description.length() == 0)
        {
            successful = false;
            error_title = "Description";
            error_message = "Please add a company description";
            ((ClearableEditText) findViewById(R.id.edtDescription)).requestFocus();
        }

        if(!successful)
        {
            system_error_obj = null;
            Show_Alert_Dialog(error_title, error_message);
        }
        else
        {
            merchant_controller.getMerchantObj().setDescription(description);

            Intent intent = new Intent(getApplicationContext(), Register_Logo.class);
            intent.putExtra("system_controller", system_controller);
            intent.putExtra("merchant_controller", merchant_controller);
            intent.putExtra("deal_controller", deal_controller);
            startActivityForResult(intent, 0);
        }
    }

    public void Debug()
    {
        edtDescription.setText("This is a great restaurant");
    }

    public void Set_Character_Count()
    {
        int characters_left = system_controller.getSystemSettingsObj().getCustomerDescriptionCharacters() - edtDescription.getText().length();

        txvCharacterCount.setText(String.valueOf(characters_left) + " characters left");
    }

}
