package com.gtsoft.meddyl.merchant.views.object;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gtsoft.meddyl.merchant.R;
import com.gtsoft.meddyl.merchant.model.object.Deal;
import com.gtsoft.meddyl.merchant.model.object.Promotion;
import com.gtsoft.meddyl.merchant.model.object.Promotion_Activity;
import com.gtsoft.meddyl.merchant.system.gtsoft.GTTextView;
import com.gtsoft.meddyl.merchant.system.gtsoft.Utils;
import com.gtsoft.meddyl.merchant.views.base.Fragment_Controller;
import com.gtsoft.meddyl.merchant.views.base.Tab_Controller;

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

public class Deal_Create_Frag extends Fragment_Controller
{
    final private Calendar myCalendar = Calendar.getInstance();

    private View rootView;
    private SeekBar skbPercentOff;
    private TextView txvPercentOff;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.deal_create_frag, container, false);

        system_controller = getArguments().getParcelable("system_controller");
        merchant_controller = getArguments().getParcelable("merchant_controller");
        deal_controller = getArguments().getParcelable("deal_controller");

        skbPercentOff = (SeekBar) rootView.findViewById(R.id.skbPercentOff);
        ShapeDrawable thumb = new ShapeDrawable(new android.graphics.drawable.shapes.OvalShape());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            thumb.setColorFilter(getResources().getColor(R.color.meddyl_blue, getActivity().getTheme()), android.graphics.PorterDuff.Mode.SRC_IN);
        }
        else
        {
            thumb.setColorFilter(getResources().getColor(R.color.meddyl_blue), android.graphics.PorterDuff.Mode.SRC_IN);
        }
        thumb.setIntrinsicHeight(50);
        thumb.setIntrinsicWidth(50);
        skbPercentOff.setThumb(thumb);

        skbPercentOff.setMax(system_controller.getSystemSettingsObj().getPercentOffMax() - system_controller.getSystemSettingsObj().getPercentOffMin());
        skbPercentOff.setProgress(system_controller.getSystemSettingsObj().getPercentOffDefault() - system_controller.getSystemSettingsObj().getPercentOffMin());
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

        txvPercentOff = (TextView) rootView.findViewById(R.id.txvPercentOff);
        txvPercentOff.setText(String.valueOf(system_controller.getSystemSettingsObj().getPercentOffDefault())+ "% Off");

        ImageView imvQuestionMaxDollar = (ImageView) rootView.findViewById(R.id.imvQuestionMaxDollar);
        imvQuestionMaxDollar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                showPopup(view, system_controller.getSystemSettingsObj().getDollarValueInfo());
            }
        });

        ImageView imvQuestionCertQty = (ImageView) rootView.findViewById(R.id.imvQuestionCertQty);
        imvQuestionCertQty.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                showPopup(view, system_controller.getSystemSettingsObj().getCertificateQuantityInfo());
            }
        });

        ImageView imvQuestionExp = (ImageView) rootView.findViewById(R.id.imvQuestionExp);
        imvQuestionExp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                showPopup(view, system_controller.getSystemSettingsObj().getExpirationDaysInfo());
            }
        });

        final ClearableEditText edtExpirationDate = (ClearableEditText) rootView.findViewById(R.id.edtExpirationDate);
        edtExpirationDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {
                    ((Tab_Controller)getActivity()).Show_Picker();
                    //int x = edtExpirationDate.getInputType();
                    edtExpirationDate.setInputType(InputType.TYPE_NULL);
                }
            }
        });

        deal_controller.setDealObj(new Deal());
        deal_controller.getDealObj().setUseDealImmediately(system_controller.getSystemSettingsObj().getDealUseImmediately());
        deal_controller.getDealObj().setIsValidNewCustomerOnly(system_controller.getSystemSettingsObj().getDealNewCustomerOnly());

        if(debug)
            Debug();

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        deal_controller =  data.getExtras().getParcelable("deal_controller");
        merchant_controller =  data.getExtras().getParcelable("merchant_controller");
        system_controller =  data.getExtras().getParcelable("system_controller");

        //Load_Data();
    }

    @Override
    public void onDestroyView()
    {
        Hide_Keyboard();

        super.onDestroyView();
    }

//    protected void Cancel()
//    {
//        AlertDialog.Builder builder = new AlertDialog.Builder(Tab_Controller.this);
//        builder.setCancelable(false);
//        builder.setTitle("Cancel");
//        builder.setMessage("Are you sure you want to cancel?")
//                .setNegativeButton("No", null)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
//                {
//                    @Override
//                    public void onClick(DialogInterface dialog, int whichButton)
//                    {
//                        //hideKeyboard();
//                        mBottomBar.selectTabAtPosition(0, true);
//                    }
//                }).show();
//    }

    public void Next()
    {
        successful = true;
        if(!Utils.isNumeric(((ClearableEditText) rootView.findViewById(R.id.edtMaxDollar)).getText().toString().trim()))
        {
            successful = false;
            error_title = "Max Dollar Amount";
            error_message = "You must enter a maxiumum dollar value";
            ((ClearableEditText) rootView.findViewById(R.id.edtMaxDollar)).requestFocus();
        }
        else if(!Utils.isNumeric(((ClearableEditText) rootView.findViewById(R.id.edtCertQty)).getText().toString().trim()))
        {
            successful = false;
            error_title = "Certificates";
            error_message = "You must enter the number of certificates";
            ((ClearableEditText) rootView.findViewById(R.id.edtCertQty)).requestFocus();
        }
        else if(!Utils.isDate(((ClearableEditText) rootView.findViewById(R.id.edtExpirationDate)).getText().toString(), "M/d/yyyy"))
        {
            successful = false;
            error_title = "Expiration Date";
            error_message = "You must enter an expiration date";
            ((ClearableEditText) rootView.findViewById(R.id.edtExpirationDate)).requestFocus();
        }

        if(!successful)
        {
            system_error_obj = null;
            ((Tab_Controller)getActivity()).Show_Alert_Dialog("Error", error_message);
        }
        else
        {
            Date expiration_date = null;
//            String timezone = Utils.Get_Time_Zone(merchant_controller.getMerchantContactObj().getMerchantObj().getZipCodeObj().getTimeZoneObj().getAbbreviation());
//            TimeZone tz = TimeZone.getTimeZone(timezone);
            SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy");
//            sdf.setTimeZone(tz);
            try
            {
                expiration_date = sdf.parse(((EditText) rootView.findViewById(R.id.edtExpirationDate)).getText().toString());
            } catch (ParseException e)
            {
                e.printStackTrace();
            }

            int percent_off = system_controller.getSystemSettingsObj().getPercentOffMin() + skbPercentOff.getProgress();
            BigDecimal max_dollar_amount = new BigDecimal(((ClearableEditText) rootView.findViewById(R.id.edtMaxDollar)).getText().toString());
            int certificate_quantity = Integer.parseInt(((ClearableEditText) rootView.findViewById(R.id.edtCertQty)).getText().toString().trim());
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
                error_message = "Maximum dollar value is too low\n" +
                        "\n" +
                        "Value must be between " + system_controller.getSystemSettingsObj().getDollarValueMin().toString() + " and " + system_controller.getSystemSettingsObj().getDollarValueMax().toString();

                ((ClearableEditText) rootView.findViewById(R.id.edtMaxDollar)).requestFocus();
            }
            else if(max_dollar_amount.compareTo(compare_max_dollar_value) == 1)
            {
                successful = false;
                error_title = "Dollar Amount";
                error_message = "Maximum dollar value is too high\n" +
                        "\n" +
                        "Value must be between " + system_controller.getSystemSettingsObj().getDollarValueMin().toString() + " and " + system_controller.getSystemSettingsObj().getDollarValueMax().toString();

                ((ClearableEditText) rootView.findViewById(R.id.edtMaxDollar)).requestFocus();
            }
            else if(certificate_quantity < compare_min_certificate_quantity)
            {
                successful = false;
                error_title = "Certificates";
                error_message = "Certificate quantity is too low\n" +
                        "\n" +
                        "Value must be between " + system_controller.getSystemSettingsObj().getCertificateQuantityMin() + " and " + system_controller.getSystemSettingsObj().getCertificateQuantityMax();

                ((ClearableEditText) rootView.findViewById(R.id.edtCertQty)).requestFocus();
            }
            else if(certificate_quantity > compare_max_certificate_quantity)
            {
                successful = false;
                error_title = "Certificates";
                error_message = "Certificate quantity is too high\n" +
                        "\n" +
                        "Value must be between " + system_controller.getSystemSettingsObj().getCertificateQuantityMin() + " and " + system_controller.getSystemSettingsObj().getCertificateQuantityMax();

                ((ClearableEditText) rootView.findViewById(R.id.edtCertQty)).requestFocus();
            }
            else if(expiration_date_days < expiration_days_min)
            {
                successful = false;
                error_title = "Expiration Date";
                error_message = "Expiration date must be after " + new DateTime().plusDays(expiration_days_min - 1).toString(dtfOut);

                ((ClearableEditText) rootView.findViewById(R.id.edtCertQty)).requestFocus();
            }
            else if(expiration_date_days > expiration_days_max)
            {
                successful = false;
                error_title = "Expiration Date";
                error_message = "Expiration date must be before " + new DateTime().plusDays(expiration_days_max - 1).toString(dtfOut);

                ((ClearableEditText) rootView.findViewById(R.id.edtCertQty)).requestFocus();
            }

            if(!successful)
            {
                system_error_obj = null;
                ((Tab_Controller)getActivity()).Show_Alert_Dialog("Error", error_message);
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

                Intent intent = new Intent(getActivity().getApplicationContext(), Deal_Options.class);
                intent.putExtra("system_controller", system_controller);
                intent.putExtra("merchant_controller", merchant_controller);
                intent.putExtra("deal_controller", deal_controller);
                startActivityForResult(intent, 1);
            }
        }
    }

    public void Debug()
    {
        ((ClearableEditText) rootView.findViewById(R.id.edtMaxDollar)).setText("25");
        ((ClearableEditText) rootView.findViewById(R.id.edtCertQty)).setText("15");
        ((ClearableEditText) rootView.findViewById(R.id.edtExpirationDate)).setText("7/25/2016");
    }

    private void Update_Expiration_Date()
    {
        String myFormat = "M/d/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        EditText edtExpirationDate = (EditText) rootView.findViewById(R.id.edtExpirationDate);
        edtExpirationDate.setText(sdf.format(myCalendar.getTime()));
    }

    public void showPopup(View anchorView, String text)
    {
        View popupView = getActivity().getLayoutInflater().inflate(R.layout.popup, null);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        final PopupWindow popupWindow = new PopupWindow(popupView, size.x, size.y);

        // Example: If you have a TextView inside `popup_layout.xml`

        FrameLayout llyMaster = (FrameLayout) popupView.findViewById(R.id.llyMaster);
        llyMaster.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                popupWindow.dismiss();
            }
        });

        GTTextView txvText = (GTTextView) popupView.findViewById(R.id.txvText);
        txvText.setText(text);

        FrameLayout rloInner = (FrameLayout) popupView.findViewById(R.id.rloInner);
        rloInner.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });

        ImageView imvCancel = (ImageView) popupView.findViewById(R.id.imvCancel);
        imvCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                popupWindow.dismiss();
            }
        });

        // If the PopupWindow should be focusable
        popupWindow.setFocusable(true);

        // If you need the PopupWindow to dismiss when when touched outside
        popupWindow.setBackgroundDrawable(new ColorDrawable());

        // Using location, the PopupWindow will be displayed right under anchorView
        popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0);
    }

    public void Set_Text(String test)
    {
        EditText edtExpirationDate = (EditText) rootView.findViewById(R.id.edtExpirationDate);
        edtExpirationDate.setText(test);

        edtExpirationDate.setInputType(1);
        edtExpirationDate.clearFocus();
    }

}
