package com.gtsoft.meddyl.merchant.views.object;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.gtsoft.meddyl.merchant.R;
import com.gtsoft.meddyl.merchant.model.object.Certificate;
import com.gtsoft.meddyl.merchant.model.object.Contact;
import com.gtsoft.meddyl.merchant.system.gtsoft.GTTextView;
import com.gtsoft.meddyl.merchant.views.base.Tab_Controller;
import com.gtsoft.meddyl.merchant.views.base.View_Controller;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Certificate_Redeem extends View_Controller
{
    private Button btnRedeem;

    private Redeem_Certificate_Async redeem_certificate_async;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.certificate_redeem);

        if(debug)
            Debug();

        Intent intent = getIntent();
        system_controller = intent.getParcelableExtra("system_controller");
        merchant_controller = intent.getParcelableExtra("merchant_controller");
        deal_controller = intent.getParcelableExtra("deal_controller");

        screen_title = "REDEEM";
        left_button = "back";
        right_button = "";

        Set_Controller_Properties();
        getWindow().getDecorView().setBackgroundColor(Color.WHITE);

        btnRedeem = (Button) findViewById(R.id.btnRedeem);
        btnRedeem.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                redeem_certificate_async = new Redeem_Certificate_Async();
                redeem_certificate_async.execute((Void) null);
            }
        });

        Load_Data();
    }

    private void Load_Data()
    {
        Certificate certificate_obj = deal_controller.getCertificateObj();
        Contact contact_obj = deal_controller.getCustomerObj().getContactObj();

        Date entry_date = certificate_obj.getAssignedDate();
        SimpleDateFormat date_format =  new SimpleDateFormat("M/d/yyyy");

        View vw1 = (View)findViewById(R.id.vw1);
        vw1.setVisibility(View.VISIBLE);

        View vw2 = (View)findViewById(R.id.vw2);
        vw2.setVisibility(View.VISIBLE);

        GTTextView txvCertificateCode = (GTTextView) findViewById(R.id.txvCertificateCode);
        txvCertificateCode.setText(certificate_obj.getCertificateCode());

        GTTextView txvStatus = (GTTextView) findViewById(R.id.txvStatus);
        txvStatus.setText(certificate_obj.getStatusText1());

        GTTextView txvName = (GTTextView) findViewById(R.id.txvName);
        txvName.setText(contact_obj.getFirstName() + " " + contact_obj.getLastName());

        GTTextView txvEmail = (GTTextView) findViewById(R.id.txvEmail);
        txvEmail.setText(contact_obj.getEmail());

        GTTextView txvPurchaseDate = (GTTextView) findViewById(R.id.txvPurchaseDate);
        txvPurchaseDate.setText("Purchased on " + date_format.format(entry_date));

        GTTextView txvCreditCardNumber = (GTTextView) findViewById(R.id.txvCreditCardNumber);
        txvCreditCardNumber.setText(deal_controller.getCertificatePaymentObj().getCardNumber());

        GTTextView txvDeal = (GTTextView) findViewById(R.id.txvDeal);
        txvDeal.setText(certificate_obj.getDealObj().getDeal());

        GTTextView txvFinePrint = (GTTextView) findViewById(R.id.txvFinePrint);
        txvFinePrint.setText(certificate_obj.getDealObj().getFinePrintExt());

        btnRedeem = (Button) findViewById(R.id.btnRedeem);
        if(certificate_obj.getCertificateStatusObj().getStatus().equals("Active"))
            btnRedeem.setVisibility(View.VISIBLE);
        else
            btnRedeem.setVisibility(View.GONE);
    }

    public void Debug()
    {

    }

    public class Redeem_Certificate_Async extends AsyncTask<Void, Void, Boolean>
    {
        private ProgressDialog dialog;

        public Redeem_Certificate_Async()
        {
            dialog = new ProgressDialog(Certificate_Redeem.this);
        }

        @Override
        protected void onPreExecute()
        {
            dialog.setMessage("Validating");
            dialog.setCancelable(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... login_log_data)
        {
            deal_controller.Redeem_Certificate();
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(Certificate_Redeem.this);
                    builder.setCancelable(false);
                    builder.setTitle("Successful");
                    builder.setMessage(system_successful_obj.getMessage())
                            .setNeutralButton("OK", new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    dialog.cancel();

                                    finish();
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
            redeem_certificate_async = null;

            if (dialog.isShowing())
            {
                dialog.dismiss();
            }
        }
    }
}
