package com.gtsoft.meddyl.merchant.views.object;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gtsoft.meddyl.merchant.R;

import com.gtsoft.meddyl.merchant.system.gtsoft.GTTextView;
import com.gtsoft.meddyl.merchant.system.gtsoft.Utils;
import com.gtsoft.meddyl.merchant.views.base.Tab_Controller;
import com.gtsoft.meddyl.merchant.views.base.View_Controller;

import java.io.ByteArrayOutputStream;
import java.io.File;


public class Merchant_Info extends View_Controller implements OnMapReadyCallback
{
    String address_1, address_2, phone;
    private GoogleMap mpvMap;
    private Register_Async register_async = null;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_info);

        if(debug)
            Debug();

        Intent intent = getIntent();
        system_controller = intent.getParcelableExtra("system_controller");
        merchant_controller = intent.getParcelableExtra("merchant_controller");
        deal_controller = intent.getParcelableExtra("deal_controller");

        screen_title = "INFO";
        left_button = "back";
        right_button = "";

        Set_Controller_Properties();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        address_1 = merchant_controller.getMerchantObj().getAddress1() + " " + merchant_controller.getMerchantObj().getAddress2();
        address_2 = merchant_controller.getMerchantObj().getZipCodeObj().getCityObj().getCity() + ", " + merchant_controller.getMerchantObj().getZipCodeObj().getCityObj().getStateObj().getAbbreviation() + "  " + merchant_controller.getMerchantObj().getZipCodeObj().getZipCode();
        phone = Utils.Format_Phone(merchant_controller.getMerchantObj().getPhone());

        String neighborhood = "";
        if(merchant_controller.getMerchantObj().getNeighborhoodObj().getNeighborhoodId() == 0)
        {
            neighborhood = merchant_controller.getMerchantObj().getZipCodeObj().getCityObj().getCity();
        }
        else
        {
            neighborhood = merchant_controller.getMerchantObj().getNeighborhoodObj().getNeighborhood();
        }

        ((GTTextView) findViewById(R.id.txvCompanyName)).setText(merchant_controller.getMerchantObj().getCompanyName());
        ((GTTextView) findViewById(R.id.txvNeighborhood)).setText(neighborhood);
        ((GTTextView) findViewById(R.id.txvDescription)).setText(merchant_controller.getMerchantObj().getDescription());
        ((GTTextView) findViewById(R.id.txvAddress1)).setText(address_1);
        ((GTTextView) findViewById(R.id.txvAddress2)).setText(address_2);

        Set_Logo_And_Stars();

        SupportMapFragment map_fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mpvMap);
        map_fragment.getMapAsync(this);

        GTTextView txvDirections = (GTTextView) findViewById(R.id.txvDirections);
        txvDirections.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String uriString = "geo:0,0?q=" + address_1 + address_2;
                Uri uri = Uri.parse("google.navigation:q=" + address_1 + address_2);
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        GTTextView txvPhone = (GTTextView) findViewById(R.id.txvPhone);
        txvPhone.setText(phone);
        txvPhone.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phone));
                startActivity(callIntent);
            }
        });

        GTTextView txvWebSite = (GTTextView) findViewById(R.id.txvWebSite);
        txvWebSite.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String url = merchant_controller.getMerchantObj().getWebsite();
                if (!url.startsWith("http://") && !url.startsWith("https://"))
                    url = "http://" + url;

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        Button btnRegister = (Button) findViewById(R.id.btnRegister);

        if(merchant_controller.getMerchantObj().getMerchantId() == 0)
        {
            btnRegister.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    register_async = new Register_Async();
                    register_async.execute((Void) null);
                }
            });
        }
        else
        {
            btnRegister.setVisibility(View.GONE);
        }
    }

    @Override
    public void onMapReady(GoogleMap map)
    {
        mpvMap = map;

        LatLng coordinates = Utils.Get_Location_From_Address(this, address_1 + " " + address_2);
        mpvMap.addMarker(new MarkerOptions().position(coordinates).title(merchant_controller.getMerchantObj().getCompanyName()));

        CameraPosition cameraPosition = new CameraPosition.Builder().target(coordinates).zoom(12.0f).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        map.moveCamera(cameraUpdate);
    }

    public void Set_Logo_And_Stars()
    {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        ImageView imvLogo = (ImageView) findViewById(R.id.imvLogo);
        imvLogo.getLayoutParams().width = size.x/2;
        imvLogo.getLayoutParams().height = size.x/2;
        imvLogo.requestLayout();

        ImageView imvStars = (ImageView) findViewById(R.id.imvStars);
        imvStars.getLayoutParams().width = size.x/2;
        imvStars.getLayoutParams().height = (size.x/2)/5;
        imvStars.requestLayout();

        if(merchant_controller.getMerchantObj().getMerchantId() == 0)
        {
            File company_file = new File(getCacheDir(), merchant_controller.getMerchantObj().getImage());
            if(company_file.exists())
            {
                Bitmap myBitmap = BitmapFactory.decodeFile(company_file.getAbsolutePath());
                imvLogo.setImageBitmap(Utils.getRoundedCornerBitmap(myBitmap, 50));
            }
        }
        else
        {
            AQuery aq = new AQuery(getApplicationContext());
            aq.id(imvLogo).image(merchant_controller.getMerchantObj().getImage());

            if(merchant_controller.getMerchantObj().getMerchantRatingObj() == null)
            {
                imvStars.setVisibility(View.GONE);
            }
            else
            {
                AQuery aq_stars = new AQuery(getApplicationContext());
                aq_stars.id(imvStars).image(merchant_controller.getMerchantObj().getMerchantRatingObj().getImage());
            }
        }
    }

    public void Debug()
    {

    }

    private class Register_Async extends AsyncTask<Void, Void, Boolean>
    {
        private ProgressDialog dialog;

        public Register_Async()
        {
            dialog = new ProgressDialog(Merchant_Info.this);
        }

        @Override
        protected void onPreExecute()
        {
            dialog.setMessage("Registering");
            dialog.setCancelable(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... login_log_data)
        {
            File company_file = new File(getCacheDir(), merchant_controller.getMerchantObj().getImage());
            Bitmap bitmap = BitmapFactory.decodeFile(company_file.getAbsolutePath());
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
            byte[] b = stream.toByteArray();
            String imageEncoded = Base64.encodeToString(b,Base64.DEFAULT);

            merchant_controller.getMerchantObj().setImageBase64(imageEncoded);
            merchant_controller.Register();
            successful = merchant_controller.getSuccessful();
            system_error_obj = merchant_controller.getSystemErrorObj();
            system_successful_obj = merchant_controller.getSystemSuccessfulObj();

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
                    SharedPreferences.Editor editor = getSharedPreferences("app", MODE_PRIVATE).edit();
                    editor.putString("user_name", merchant_controller.getContactObj().getEmail());
                    editor.putString("password", merchant_controller.getContactObj().getPassword());
                    editor.apply();

                    system_controller.getLoginLogObj().setMerchantContactId(merchant_controller.getMerchantContactObj().getMerchantContactId());
                    merchant_controller.getLoginLogObj().setMerchantContactId(merchant_controller.getMerchantContactObj().getMerchantContactId());
                    deal_controller.getLoginLogObj().setMerchantContactId(merchant_controller.getMerchantContactObj().getMerchantContactId());

                    Intent intent = new Intent(getApplicationContext(), Tab_Controller.class);
                    intent.putExtra("system_controller", system_controller);
                    intent.putExtra("merchant_controller", merchant_controller);
                    intent.putExtra("deal_controller", deal_controller);
                    intent.putExtra("selected_tab", 0);
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
            register_async = null;

            if (dialog.isShowing())
            {
                dialog.dismiss();
            }
        }
    }
}
