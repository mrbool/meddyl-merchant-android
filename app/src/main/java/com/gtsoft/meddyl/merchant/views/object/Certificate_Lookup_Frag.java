package com.gtsoft.meddyl.merchant.views.object;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gtsoft.meddyl.merchant.R;
import com.gtsoft.meddyl.merchant.controller.object.Deal_Controller;
import com.gtsoft.meddyl.merchant.controller.object.Merchant_Controller;
import com.gtsoft.meddyl.merchant.controller.object.System_Controller;
import com.gtsoft.meddyl.merchant.model.object.Certificate;
import com.gtsoft.meddyl.merchant.model.object.Merchant_Contact_Validation;
import com.gtsoft.meddyl.merchant.model.object.System_Error;
import com.gtsoft.meddyl.merchant.model.object.System_Successful;
import com.gtsoft.meddyl.merchant.views.base.Fragment_Controller;
import com.gtsoft.meddyl.merchant.views.base.Tab_Controller;

import org.droidparts.widget.ClearableEditText;

public class Certificate_Lookup_Frag extends Fragment_Controller
{
    private Lookup_Certificate_Async lookup_certificate_async;

    private ClearableEditText edtCode;
    private String code;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.certificate_lookup_frag, container, false);

        system_controller = getArguments().getParcelable("system_controller");
        merchant_controller = getArguments().getParcelable("merchant_controller");
        deal_controller = getArguments().getParcelable("deal_controller");

        edtCode = (ClearableEditText) rootView.findViewById(R.id.edtCode);
        edtCode.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        edtCode.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable s)
            {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after)
            {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count)
            {
                if(start==5)
                    Check_Code();
            }
        });

        return rootView;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        edtCode.setText("");
    }

    private void Check_Code()
    {
        code = edtCode.getText().toString().trim();

        lookup_certificate_async = new Lookup_Certificate_Async();
        lookup_certificate_async.execute((Void) null);
    }

    private class Lookup_Certificate_Async extends AsyncTask<Void, Void, Boolean>
    {
        private ProgressDialog dialog;

        public Lookup_Certificate_Async()
        {
            dialog = new ProgressDialog(getActivity());
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
            Certificate certificate_obj = new Certificate();
            certificate_obj.setCertificateCode(code);

            deal_controller.setCertificateObj(certificate_obj);
            deal_controller.setMerchantContactObj(merchant_controller.getMerchantContactObj());
            deal_controller.Lookup_Certificate();
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
                    Intent intent = new Intent(getActivity(), Certificate_Redeem.class);
                    intent.putExtra("system_controller", system_controller);
                    intent.putExtra("merchant_controller", merchant_controller);
                    intent.putExtra("deal_controller", deal_controller);
                    startActivity(intent);
                }
                else
                {
                    edtCode.setText("");
                    ((Tab_Controller)getActivity()).Show_Alert_Dialog("Error", system_error_obj.getMessage());
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
            lookup_certificate_async = null;

            if (dialog.isShowing())
            {
                dialog.dismiss();
            }
        }
    }

}
