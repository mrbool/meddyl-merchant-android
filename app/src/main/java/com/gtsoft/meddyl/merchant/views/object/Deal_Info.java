package com.gtsoft.meddyl.merchant.views.object;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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

import com.androidquery.AQuery;
import com.gtsoft.meddyl.merchant.R;
import com.gtsoft.meddyl.merchant.model.object.Deal;
import com.gtsoft.meddyl.merchant.system.gtsoft.GTTextView;
import com.gtsoft.meddyl.merchant.views.base.Tab_Controller;
import com.gtsoft.meddyl.merchant.views.base.View_Controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Deal_Info extends View_Controller
{
    private Send_Deal_Validation_Async send_deal_validation_async = null;
    private Cancel_Deal_Async cancel_deal_async = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deal_info);

        if(debug)
            Debug();

        Intent intent = getIntent();
        system_controller = intent.getParcelableExtra("system_controller");
        merchant_controller = intent.getParcelableExtra("merchant_controller");
        deal_controller = intent.getParcelableExtra("deal_controller");

        screen_title = "DEAL INFO";
        left_button = "back";
        right_button = "";

        Set_Controller_Properties();

        getWindow().getDecorView().setBackgroundColor(Color.WHITE);

        Deal deal_obj = deal_controller.getDealObj();

        Date entry_date = deal_obj.getEntryDateUtcStamp();
        SimpleDateFormat date_format =  new SimpleDateFormat("M/d/yyyy");

        Set_Logo(deal_obj.getImage());

        ((GTTextView) findViewById(R.id.txvDeal)).setText(deal_obj.getDeal());
        ((GTTextView) findViewById(R.id.txvExpiration)).setText(date_format.format(entry_date));
        ((GTTextView) findViewById(R.id.txvStatus)).setText(deal_obj.getDealStatusObj().getStatus());
        ((GTTextView) findViewById(R.id.txvCertificatesIssued)).setText(String.valueOf(deal_obj.getCertificateQuantity()) + " certificates issued");
        ((GTTextView) findViewById(R.id.txvCertificatesBought)).setText(String.valueOf(deal_obj.getCertificatesSold()) + " certificates bought");
        ((GTTextView) findViewById(R.id.txvCertificatesRedeemed)).setText(String.valueOf(deal_obj.getCertificatesRedeemed()) + " certificates redeemed");
        ((GTTextView) findViewById(R.id.txvFinePrint)).setText(deal_obj.getFinePrintExt());

        Button btnValidate = (Button) findViewById(R.id.btnValidate);
        btnValidate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                send_deal_validation_async = new Send_Deal_Validation_Async();
                send_deal_validation_async.execute((Void) null);
            }
        });

        Button btnModify = (Button) findViewById(R.id.btnModify);
        btnModify.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
               // Intent intent = new Intent(getApplicationContext(), Tab_Controller.class);
                Intent intent = new Intent(getApplicationContext(), Deal_Create.class);
                intent.putExtra("system_controller", system_controller);
                intent.putExtra("merchant_controller", merchant_controller);
                intent.putExtra("deal_controller", deal_controller);
                //intent.putExtra("selected_tab", 1);
                startActivity(intent);
            }
        });

        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Cancel_Deal();
            }
        });

        if(deal_obj.getDealStatusObj().getStatusId() != 5)
            btnValidate.setVisibility(View.GONE);

        if(deal_obj.getDealStatusObj().getStatusId() != 5 && deal_obj.getDealStatusObj().getStatusId() != 6)
            btnModify.setVisibility(View.GONE);

        if(deal_obj.getDealStatusObj().getStatusId() == 2 || deal_obj.getDealStatusObj().getStatusId() == 3)
            btnModify.setVisibility(View.GONE);
    }

    public void Set_Logo(String image)
    {
        ImageView imgDealImage = (ImageView) findViewById(R.id.imvDeal);
        AQuery aq = new AQuery(getApplicationContext());
        aq.id(imgDealImage).image(image);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        imgDealImage.getLayoutParams().width = size.x;
        imgDealImage.getLayoutParams().height = (int)Math.floor(size.x *.66);
        imgDealImage.requestLayout();
    }

    public void Cancel_Deal()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(Deal_Info.this);
        builder.setCancelable(false);
        builder.setTitle("Cancel Deal");
        builder.setMessage("Are you sure you want to cancel this deal?")
                .setPositiveButton("No", null)
                .setNegativeButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        cancel_deal_async = new Cancel_Deal_Async();
                        cancel_deal_async.execute((Void) null);
                    }
                }).show();
    }

    public void Debug()
    {

    }

    private class Send_Deal_Validation_Async extends AsyncTask<Void, Void, Boolean>
    {
        private ProgressDialog dialog;

        public Send_Deal_Validation_Async()
        {
            dialog = new ProgressDialog(Deal_Info.this);
        }

        @Override
        protected void onPreExecute()
        {
            dialog.setMessage("Sending Validation");
            dialog.setCancelable(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... login_log_data)
        {
            deal_controller.setMerchantContactObj(merchant_controller.getMerchantContactObj());
            deal_controller.Send_Deal_Validation();
            successful = deal_controller.getSuccessful();
            system_error_obj = deal_controller.getSystemErrorObj();
            system_successful_obj = deal_controller.getSystemSuccessfulObj();

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
                    AlertDialog.Builder builder = new AlertDialog.Builder(Deal_Info.this);
                    builder.setCancelable(false);
                    builder.setTitle("Validation");
                    builder.setMessage(system_successful_obj.getMessage())
                            .setNeutralButton("OK", new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    dialog.cancel();

                                    Intent intent = new Intent(getApplicationContext(), Deal_Validate.class);
                                    intent.putExtra("system_controller", system_controller);
                                    intent.putExtra("merchant_controller", merchant_controller);
                                    intent.putExtra("deal_controller", deal_controller);
                                    startActivity(intent);
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
            send_deal_validation_async = null;

            if (dialog.isShowing())
            {
                dialog.dismiss();
            }
        }
    }

    private class Cancel_Deal_Async extends AsyncTask<Void, Void, Boolean>
    {
        private ProgressDialog dialog;

        public Cancel_Deal_Async()
        {
            dialog = new ProgressDialog(Deal_Info.this);
        }

        @Override
        protected void onPreExecute()
        {
            dialog.setMessage("Cancelling");
            dialog.setCancelable(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... login_log_data)
        {
            deal_controller.setMerchantContactObj(merchant_controller.getMerchantContactObj());
            deal_controller.Cancel_Deal();
            successful = deal_controller.getSuccessful();
            system_error_obj = deal_controller.getSystemErrorObj();
            system_successful_obj = deal_controller.getSystemSuccessfulObj();

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
                    Intent intent = new Intent(getApplicationContext(), Tab_Controller.class);
                    intent.putExtra("system_controller", system_controller);
                    intent.putExtra("merchant_controller", merchant_controller);
                    intent.putExtra("deal_controller", deal_controller);
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
            send_deal_validation_async = null;

            if (dialog.isShowing())
            {
                dialog.dismiss();
            }
        }
    }
}
