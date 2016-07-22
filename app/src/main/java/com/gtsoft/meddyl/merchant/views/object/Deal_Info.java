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

import org.droidparts.widget.ClearableEditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Deal_Info extends View_Controller
{
    private ImageView imvDealImage;
    private GTTextView txvDeal;
    private GTTextView txvExpiration;
    private GTTextView txvStatus;
    private GTTextView txvCertificatesIssued;
    private GTTextView txvCertificatesBought;
    private GTTextView txvCertificatesRedeemed;
    private GTTextView txvCertificatesRemaining;
    private GTTextView txvFinePrint;

    private Button btnValidate;
    private Button btnModify;
    private Button btnCancel;

    private Get_Deal_Details_Async get_deal_details_async = null;
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

        imvDealImage = (ImageView) findViewById(R.id.imvDealImage);
        txvDeal = (GTTextView) findViewById(R.id.txvDeal);
        txvExpiration = (GTTextView) findViewById(R.id.txvExpiration);
        txvStatus = (GTTextView) findViewById(R.id.txvStatus);
        txvCertificatesIssued = (GTTextView) findViewById(R.id.txvCertificatesIssued);
        txvCertificatesBought = (GTTextView) findViewById(R.id.txvCertificatesBought);
        txvCertificatesRedeemed = (GTTextView) findViewById(R.id.txvCertificatesRedeemed);
        txvCertificatesRemaining = (GTTextView) findViewById(R.id.txvCertificatesRemaining);
        txvFinePrint = (GTTextView) findViewById(R.id.txvFinePrint);

        btnValidate = (Button) findViewById(R.id.btnValidate);
        btnValidate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                send_deal_validation_async = new Send_Deal_Validation_Async();
                send_deal_validation_async.execute((Void) null);
            }
        });

        btnModify = (Button) findViewById(R.id.btnModify);
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

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Cancel_Deal();
            }
        });

        get_deal_details_async = new Get_Deal_Details_Async();
        get_deal_details_async.execute((Void) null);
    }

    public void Load_Data()
    {
        Deal deal_obj = deal_controller.getDealObj();

        Date expiration_date = deal_obj.getExpirationDate();
        SimpleDateFormat date_format =  new SimpleDateFormat("M/d/yyyy");

        /* set logo */
        AQuery aq = new AQuery(getApplicationContext());
        aq.id(imvDealImage).image(deal_obj.getImage());

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        imvDealImage.getLayoutParams().width = size.x;
        imvDealImage.getLayoutParams().height = (int)Math.floor(size.x *.66);
        imvDealImage.requestLayout();

        /* set labels */
        txvDeal.setText(deal_obj.getDeal());
        txvExpiration.setText(date_format.format(expiration_date));
        txvStatus.setText(deal_obj.getDealStatusObj().getStatus());
        txvCertificatesIssued.setText(String.valueOf(deal_obj.getCertificateQuantity()) + " certificates issued");
        txvCertificatesBought.setText(String.valueOf(deal_obj.getCertificatesSold()) + " certificates bought");
        txvCertificatesRedeemed.setText(String.valueOf(deal_obj.getCertificatesRedeemed()) + " certificates redeemed");
        txvCertificatesRemaining.setText(String.valueOf(deal_obj.getCertificatesRemaining()) + " certificates remaining");
        txvFinePrint.setText(deal_obj.getFinePrintExt());

        imvDealImage.setVisibility(View.VISIBLE);
        ((GTTextView)findViewById(R.id.txvDealLabel)).setVisibility(View.VISIBLE);
        txvDeal.setVisibility(View.VISIBLE);
        ((View)findViewById(R.id.vw1)).setVisibility(View.VISIBLE);
        ((GTTextView)findViewById(R.id.txvExpirationLabel)).setVisibility(View.VISIBLE);
        txvExpiration.setVisibility(View.VISIBLE);
        ((View)findViewById(R.id.vw2)).setVisibility(View.VISIBLE);
        ((GTTextView)findViewById(R.id.txvStatusLabel)).setVisibility(View.VISIBLE);
        txvStatus.setVisibility(View.VISIBLE);
        ((View)findViewById(R.id.vw3)).setVisibility(View.VISIBLE);
        ((GTTextView)findViewById(R.id.txvStatsLabel)).setVisibility(View.VISIBLE);
        txvCertificatesIssued.setVisibility(View.VISIBLE);
        txvCertificatesBought.setVisibility(View.VISIBLE);
        txvCertificatesRedeemed.setVisibility(View.VISIBLE);
        txvCertificatesRemaining.setVisibility(View.VISIBLE);
        ((View)findViewById(R.id.vw4)).setVisibility(View.VISIBLE);
        ((GTTextView)findViewById(R.id.txvFinePrintLabel)).setVisibility(View.VISIBLE);
        txvFinePrint.setVisibility(View.VISIBLE);
        btnValidate.setVisibility(View.VISIBLE);
        btnModify.setVisibility(View.VISIBLE);
        btnCancel.setVisibility(View.VISIBLE);

        if(deal_obj.getDealStatusObj().getStatusId() != 5)
            btnValidate.setVisibility(View.GONE);

        if(deal_obj.getDealStatusObj().getStatusId() != 5 && deal_obj.getDealStatusObj().getStatusId() != 6)
            btnModify.setVisibility(View.GONE);

        if(deal_obj.getDealStatusObj().getStatusId() == 2 || deal_obj.getDealStatusObj().getStatusId() == 3)
            btnCancel.setVisibility(View.GONE);

    }

    public void Cancel_Deal()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(Deal_Info.this);
        builder.setCancelable(false);
        builder.setTitle("Cancel Deal");
        builder.setMessage("Are you sure you want to cancel this deal?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        cancel_deal_async = new Cancel_Deal_Async();
                        cancel_deal_async.execute((Void) null);
                    }
                })
                .setNegativeButton("No", null).show();
    }

    public void Debug()
    {

    }

    private class Get_Deal_Details_Async extends AsyncTask<Void, Void, Boolean>
    {
        private ProgressDialog dialog;

        public Get_Deal_Details_Async()
        {
            dialog = new ProgressDialog(Deal_Info.this);
        }

        @Override
        protected void onPreExecute()
        {
            dialog.setMessage("Loading");
            dialog.setCancelable(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... login_log_data)
        {
            deal_controller.setMerchantContactObj(merchant_controller.getMerchantContactObj());
            deal_controller.Get_Deal_Details();
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
                    Load_Data();
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
