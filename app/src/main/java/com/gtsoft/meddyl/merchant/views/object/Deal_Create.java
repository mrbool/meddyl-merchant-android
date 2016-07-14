package com.gtsoft.meddyl.merchant.views.object;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gtsoft.meddyl.merchant.R;
import com.gtsoft.meddyl.merchant.model.object.Deal;
import com.gtsoft.meddyl.merchant.model.object.Promotion;
import com.gtsoft.meddyl.merchant.model.object.Promotion_Activity;
import com.gtsoft.meddyl.merchant.system.gtsoft.GTTextView;
import com.gtsoft.meddyl.merchant.system.gtsoft.Utils;
import com.gtsoft.meddyl.merchant.views.base.Tab_Controller;
import com.gtsoft.meddyl.merchant.views.base.View_Controller;

import org.droidparts.widget.ClearableEditText;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Deal_Create extends View_Controller
{
    private DatePickerDialog.OnDateSetListener date;
    final private Calendar myCalendar = Calendar.getInstance();

    private SeekBar skbPercentOff;
    private TextView txvPercentOff;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deal_create);

        Intent intent = getIntent();
        system_controller = intent.getParcelableExtra("system_controller");
        merchant_controller = intent.getParcelableExtra("merchant_controller");
        deal_controller = intent.getParcelableExtra("deal_controller");

        screen_title = "CREATE";
        left_button = "cancel";
        right_button = "next";

        Set_Controller_Properties();

        skbPercentOff = (SeekBar) findViewById(R.id.skbPercentOff);
        ShapeDrawable thumb = new ShapeDrawable(new android.graphics.drawable.shapes.OvalShape());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            thumb.setColorFilter(getResources().getColor(R.color.meddyl_blue, getTheme()), android.graphics.PorterDuff.Mode.SRC_IN);
        }
        else
        {
            thumb.setColorFilter(getResources().getColor(R.color.meddyl_blue), android.graphics.PorterDuff.Mode.SRC_IN);
        }
        thumb.setIntrinsicHeight(50);
        thumb.setIntrinsicWidth(50);
        skbPercentOff.setThumb(thumb);

        skbPercentOff.setMax(system_controller.getSystemSettingsObj().getPercentOffMax() - system_controller.getSystemSettingsObj().getPercentOffMin());
        skbPercentOff.setProgress(deal_controller.getDealObj().getPercentOff() - system_controller.getSystemSettingsObj().getPercentOffMin());
        skbPercentOff.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser)
            {
                txvPercentOff.setText(String.valueOf(system_controller.getSystemSettingsObj().getPercentOffMin() + progressValue) + "% Off");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
            }
        });

        txvPercentOff = (TextView) findViewById(R.id.txvPercentOff);
        txvPercentOff.setText(String.valueOf(deal_controller.getDealObj().getPercentOff()) + "% Off");

        ImageView imvQuestionMaxDollar = (ImageView) findViewById(R.id.imvQuestionMaxDollar);
        imvQuestionMaxDollar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                showPopup(view, system_controller.getSystemSettingsObj().getDollarValueInfo());
            }
        });

        ImageView imvQuestionCertQty = (ImageView) findViewById(R.id.imvQuestionCertQty);
        imvQuestionCertQty.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                showPopup(view, system_controller.getSystemSettingsObj().getCertificateQuantityInfo());
            }
        });

        ImageView imvQuestionExp = (ImageView) findViewById(R.id.imvQuestionExp);
        imvQuestionExp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                showPopup(view, system_controller.getSystemSettingsObj().getExpirationDaysInfo());
            }
        });

        date = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                Update_Expiration_Date();
            }
        };

        final ClearableEditText edtExpirationDate = (ClearableEditText) findViewById(R.id.edtExpirationDate);
        edtExpirationDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {
                    Show_Picker();
                    edtExpirationDate.setInputType(InputType.TYPE_NULL);
                }
            }
        });

        Date expiration_date = deal_controller.getDealObj().getExpirationDate();
        SimpleDateFormat date_format =  new SimpleDateFormat("M/d/yyyy");

        ((ClearableEditText) findViewById(R.id.edtMaxDollar)).setText(deal_controller.getDealObj().getMaxDollarAmount().toString().replace(".00",""));
        ((ClearableEditText) findViewById(R.id.edtCertQty)).setText(Integer.toString(deal_controller.getDealObj().getCertificateQuantity()));
        ((ClearableEditText) findViewById(R.id.edtExpirationDate)).setText(date_format.format(expiration_date));

        if(debug)
            Debug();
    }

    protected void Cancel()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(Deal_Create.this);
        builder.setCancelable(false);
        builder.setTitle("Cancel");
        builder.setMessage("Are you sure you want to cancel?")
                .setNegativeButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        finish();
                    }
                })
                .setPositiveButton("No", null).show();
    }

    public void Next()
    {
        successful = true;
        if(!Utils.isNumeric(((ClearableEditText) findViewById(R.id.edtMaxDollar)).getText().toString().trim()))
        {
            successful = false;
            error_title = "Max Dollar Amount";
            error_message = "You must enter a maxiumum dollar value";
            ((ClearableEditText) findViewById(R.id.edtMaxDollar)).requestFocus();
        }
        else if(!Utils.isNumeric(((ClearableEditText) findViewById(R.id.edtCertQty)).getText().toString().trim()))
        {
            successful = false;
            error_title = "Certificates";
            error_message = "You must enter the number of certificates";
            ((ClearableEditText) findViewById(R.id.edtCertQty)).requestFocus();
        }
        else if(!Utils.isDate(((ClearableEditText) findViewById(R.id.edtExpirationDate)).getText().toString(), "M/d/yyyy"))
        {
            successful = false;
            error_title = "Expiration Date";
            error_message = "You must enter an expiration date";
            ((ClearableEditText) findViewById(R.id.edtExpirationDate)).requestFocus();
        }

        if(!successful)
        {
            system_error_obj = null;
            Show_Alert_Dialog("Error", error_message);
        }
        else
        {
            Date expiration_date = null;
            SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy");
            try
            {
                expiration_date = sdf.parse(((EditText) findViewById(R.id.edtExpirationDate)).getText().toString());
            } catch (ParseException e)
            {
                e.printStackTrace();
            }

            int percent_off = system_controller.getSystemSettingsObj().getPercentOffMin() + skbPercentOff.getProgress();
            BigDecimal max_dollar_amount = new BigDecimal(((ClearableEditText) findViewById(R.id.edtMaxDollar)).getText().toString());
            int certificate_quantity = Integer.parseInt(((ClearableEditText) findViewById(R.id.edtCertQty)).getText().toString().trim());
            int expiration_date_days = Days.daysBetween(new DateTime(), new DateTime(expiration_date)).getDays();

            BigDecimal compare_min_dollar_value = system_controller.getSystemSettingsObj().getDollarValueMin();
            BigDecimal compare_max_dollar_value = system_controller.getSystemSettingsObj().getDollarValueMax();
            int compare_min_certificate_quantity = system_controller.getSystemSettingsObj().getCertificateQuantityMin();
            int compare_max_certificate_quantity = system_controller.getSystemSettingsObj().getCertificateQuantityMax();
            int expiration_days_min = system_controller.getSystemSettingsObj().getExpirationDaysMin();
            int expiration_days_max = system_controller.getSystemSettingsObj().getExpirationDaysMax();

            DateTimeFormatter dtfOut = DateTimeFormat.forPattern("M/d/yyyy");

            successful = true;
            if(max_dollar_amount.compareTo(compare_min_dollar_value) == -1)
            {
                successful = false;
                error_title = "Dollar Amount";
                error_message = "Maximum dollar amount is too low";
                ((ClearableEditText) findViewById(R.id.edtMaxDollar)).requestFocus();
            }
            else if(max_dollar_amount.compareTo(compare_max_dollar_value) == 1)
            {
                successful = false;
                error_title = "Dollar Amount";
                error_message = "Maximum dollar amount is too high";
                ((ClearableEditText) findViewById(R.id.edtMaxDollar)).requestFocus();
            }
            else if(certificate_quantity < compare_min_certificate_quantity)
            {
                successful = false;
                error_title = "Certificates";
                error_message = "Certificate quantity is too low";
                ((ClearableEditText) findViewById(R.id.edtCertQty)).requestFocus();
            }
            else if(certificate_quantity > compare_max_certificate_quantity)
            {
                successful = false;
                error_title = "Certificates";
                error_message = "Certificate quantity is too high";
                ((ClearableEditText) findViewById(R.id.edtCertQty)).requestFocus();
            }
            else if(expiration_date_days < expiration_days_min)
            {
                successful = false;
                error_title = "Expiration Date";
                error_message = "Expiration date must be after " + new DateTime().plus(expiration_days_min).toString(dtfOut);
                ((ClearableEditText) findViewById(R.id.edtCertQty)).requestFocus();
            }
            else if(expiration_date_days > expiration_days_max)
            {
                successful = false;
                error_title = "Expiration Date";
                error_message = "Expiration date must be before " + new DateTime().plus(expiration_days_max).toString(dtfOut);
                ((ClearableEditText) findViewById(R.id.edtCertQty)).requestFocus();
            }

            if(!successful)
            {
                system_error_obj = null;
                Show_Alert_Dialog("Error", error_message);
            }
            else
            {
                Promotion promotion_obj= new Promotion();
                Promotion_Activity promotion_activity_obj = new Promotion_Activity();
                promotion_activity_obj.setPromotionObj(promotion_obj);

                deal_controller.getDealObj().setDeal("");
                deal_controller.getDealObj().setPromotionActivityObj(promotion_activity_obj);
                deal_controller.getDealObj().setPercentOff(percent_off);
                deal_controller.getDealObj().setMaxDollarAmount(max_dollar_amount);
                deal_controller.getDealObj().setCertificateQuantity(certificate_quantity);
                deal_controller.getDealObj().setExpirationDate(expiration_date);

                Intent intent = new Intent(getApplicationContext(), Deal_Options.class);
                intent.putExtra("system_controller", system_controller);
                intent.putExtra("merchant_controller", merchant_controller);
                intent.putExtra("deal_controller", deal_controller);
                startActivityForResult(intent, 1);
            }
        }
    }

    public void Debug()
    {

    }

    public void Show_Picker()
    {
        InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        new DatePickerDialog(Deal_Create.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void Update_Expiration_Date()
    {
        String myFormat = "M/d/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        EditText edtExpirationDate = (EditText) findViewById(R.id.edtExpirationDate);
        edtExpirationDate.setText(sdf.format(myCalendar.getTime()));
    }

}
