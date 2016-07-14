package com.gtsoft.meddyl.merchant.views.object;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.gtsoft.meddyl.merchant.R;
import com.gtsoft.meddyl.merchant.system.gtsoft.GTTextView;
import com.gtsoft.meddyl.merchant.views.base.View_Controller;

import org.droidparts.widget.ClearableEditText;

public class Deal_Fine_Print_More extends View_Controller
{
    private ClearableEditText edtFinePrint;
    private GTTextView txvCharacterCount;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deal_fine_print_more);

        if(debug)
            Debug();

        Intent intent = getIntent();
        system_controller = intent.getParcelableExtra("system_controller");
        merchant_controller = intent.getParcelableExtra("merchant_controller");
        deal_controller = intent.getParcelableExtra("deal_controller");

        screen_title = "MORE FINE PRINT";
        left_button = "back";
        right_button = "next";

        Set_Controller_Properties();

        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(system_controller.getSystemSettingsObj().getCustomerDescriptionCharacters());

        edtFinePrint = (ClearableEditText) findViewById(R.id.edtFinePrint);
        edtFinePrint.setFilters(FilterArray);
        edtFinePrint.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count)
            {
                Set_Character_Count();
            }
        });

        edtFinePrint.setRawInputType(InputType.TYPE_CLASS_TEXT);
        edtFinePrint.setImeActionLabel(getResources().getString(R.string.done), EditorInfo.IME_ACTION_DONE);
        edtFinePrint.setImeOptions(EditorInfo.IME_ACTION_DONE);
        edtFinePrint.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event == null)
                {
                    if (actionId == EditorInfo.IME_ACTION_DONE)
                    {
                        // Capture soft enters in a singleLine EditText that is the last EditText
                        // This one is useful for the new list case, when there are no existing ListItems
                        edtFinePrint.clearFocus();
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    } else if (actionId == EditorInfo.IME_ACTION_NEXT) {
                        // Capture soft enters in other singleLine EditTexts
                    } else if (actionId == EditorInfo.IME_ACTION_GO) {
                    } else {
                        // Let the system handle all other null KeyEvents
                        return false;
                    }
                } else if (actionId == EditorInfo.IME_NULL) {
                    // Capture most soft enters in multi-line EditTexts and all hard enters;
                    // They supply a zero actionId and a valid keyEvent rather than
                    // a non-zero actionId and a null event like the previous cases.
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        // We capture the event when the key is first pressed.
                    } else {
                        // We consume the event when the key is released.
                        return true;
                    }
                } else {
                    // We let the system handle it when the listener is triggered by something that
                    // wasn't an enter.
                    return false;
                }
                return true;
            }
        });

        txvCharacterCount = (GTTextView) findViewById(R.id.txvCharacterCount);
        txvCharacterCount.setText(String.valueOf(system_controller.getSystemSettingsObj().getCustomerDescriptionCharacters()) + " characters left");

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

    public void Set_Character_Count()
    {
        int characters_left = system_controller.getSystemSettingsObj().getCustomerDescriptionCharacters() - edtFinePrint.getText().length();

        txvCharacterCount.setText(String.valueOf(characters_left) + " characters left");
    }

    private void Load_Data()
    {
        if(deal_controller.getDealObj().getFinePrint() != null)
        {
            ((ClearableEditText) findViewById(R.id.edtFinePrint)).setText(deal_controller.getDealObj().getFinePrint());
        }
    }

    private void Save_Data()
    {
        String fine_print = edtFinePrint.getText().toString().trim();
        deal_controller.getDealObj().setFinePrint(fine_print);
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

    @Override
    protected void Next()
    {
        Save_Data();

        Intent intent = new Intent(getApplicationContext(), Deal_Image.class);
        intent.putExtra("system_controller", system_controller);
        intent.putExtra("merchant_controller", merchant_controller);
        intent.putExtra("deal_controller", deal_controller);
        startActivityForResult(intent, 1);
    }

    public void Debug()
    {

    }
}
