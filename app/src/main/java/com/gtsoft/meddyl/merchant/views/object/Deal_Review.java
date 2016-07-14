package com.gtsoft.meddyl.merchant.views.object;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.androidquery.AQuery;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gtsoft.meddyl.merchant.R;
import com.gtsoft.meddyl.merchant.model.object.Deal;
import com.gtsoft.meddyl.merchant.model.object.Fine_Print_Option;
import com.gtsoft.meddyl.merchant.model.object.Merchant;
import com.gtsoft.meddyl.merchant.system.gtsoft.GTTextView;
import com.gtsoft.meddyl.merchant.system.gtsoft.Utils;
import com.gtsoft.meddyl.merchant.views.base.Fragment_Controller;
import com.gtsoft.meddyl.merchant.views.base.Tab_Controller;
import com.gtsoft.meddyl.merchant.views.base.View_Controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Deal_Review extends View_Controller implements OnMapReadyCallback
{
    Bitmap bitmap;
    private GoogleMap mpvMap;

    String address_1, address_2, phone;

    private Get_Merchant_Contact_Async get_merchant_contact_async = null;
    private Verify_Deal_Async verify_deal_async = null;
    private Add_Deal_Async add_deal_async = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deal_review);

        if(debug)
            Debug();

        Intent intent = getIntent();
        system_controller = intent.getParcelableExtra("system_controller");
        merchant_controller = intent.getParcelableExtra("merchant_controller");
        deal_controller = intent.getParcelableExtra("deal_controller");

        screen_title = "REVIEW";
        left_button = "back";
        right_button = "";

        Set_Controller_Properties();

        get_merchant_contact_async = new Get_Merchant_Contact_Async();
        get_merchant_contact_async.execute((Void) null);
    }

    public void Debug()
    {

    }

    @Override
    public void onMapReady(GoogleMap map)
    {
        mpvMap = map;

        LatLng coordinates = Utils.Get_Location_From_Address(this, address_1 + ", " + address_2);
//        LatLng coordinates = Utils.Get_Location_From_Address(this, "2600 Torrey Pines Rd B19, La Jolla, CA 92037");

        mpvMap.addMarker(new MarkerOptions().position(coordinates).title(merchant_controller.getMerchantContactObj().getMerchantObj().getCompanyName()));

        CameraPosition cameraPosition = new CameraPosition.Builder().target(coordinates).zoom(12.0f).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        map.moveCamera(cameraUpdate);
    }

    public void Load_Merchant_Info()
    {
        final Merchant merchant_obj = merchant_controller.getMerchantContactObj().getMerchantObj();

        String neighborhood;
        if(merchant_obj.getNeighborhoodObj().getNeighborhoodId() == 0)
            neighborhood = merchant_obj.getZipCodeObj().getCityObj().getCity();
        else
            neighborhood = merchant_obj.getNeighborhoodObj().getNeighborhood();

        address_1 = merchant_obj.getAddress1() + " " + merchant_obj.getAddress2();
        address_2 = merchant_obj.getZipCodeObj().getCityObj().getCity() + ", " + merchant_obj.getZipCodeObj().getCityObj().getStateObj().getAbbreviation() + "  " + merchant_obj.getZipCodeObj().getZipCode();
        phone = Utils.Format_Phone(merchant_obj.getPhone());

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int image_width = Double.valueOf(size.x).intValue();
        int image_height = Double.valueOf(image_width * .75).intValue();

        ImageView imvStars = (ImageView) findViewById(R.id.imvStars);
        imvStars.getLayoutParams().width = size.x/3;
        imvStars.getLayoutParams().height = (size.x/3)/5;
        imvStars.requestLayout();
        imvStars.setVisibility(View.VISIBLE);

        ImageView imvLogo = (ImageView) findViewById(R.id.imvLogo);
        imvLogo.getLayoutParams().width = size.x/4;
        imvLogo.getLayoutParams().height = size.x/4;
        imvLogo.requestLayout();
        imvStars.setVisibility(View.VISIBLE);

        AQuery aq = new AQuery(getApplicationContext());
        aq.id(imvLogo).image(merchant_obj.getImage());

        if(merchant_obj.getMerchantRatingObj() == null)
        {
            imvStars.setVisibility(View.INVISIBLE);
        }
        else
        {
            AQuery aq_stars = new AQuery(getApplicationContext());
            aq_stars.id(imvStars).image(merchant_obj.getMerchantRatingObj().getImage());
        }

        imvLogo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                merchant_controller.setMerchantObj(merchant_controller.getMerchantContactObj().getMerchantObj());

                Intent intent = new Intent(getApplicationContext(), Merchant_Info.class);
                intent.putExtra("system_controller", system_controller);
                intent.putExtra("merchant_controller", merchant_controller);
                intent.putExtra("deal_controller", deal_controller);
                startActivity(intent);
            }
        });

        GTTextView txvCompanyName = (GTTextView) findViewById(R.id.txvCompanyName);
        txvCompanyName.getLayoutParams().width = Double.valueOf(size.x * .7).intValue();
        txvCompanyName.setText(merchant_obj.getCompanyName());

        GTTextView txvNeighborhood = (GTTextView) findViewById(R.id.txvNeighborhood);
        txvNeighborhood.setText(neighborhood);

        View vw1 = (View)findViewById(R.id.vw1);
        vw1.setVisibility(View.VISIBLE);

        View vw2 = (View)findViewById(R.id.vw2);
        vw2.setVisibility(View.VISIBLE);

        View vw3 = (View)findViewById(R.id.vw3);
        vw3.setVisibility(View.VISIBLE);

        View vw4 = (View)findViewById(R.id.vw4);
        vw4.setVisibility(View.VISIBLE);

        SupportMapFragment map_fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mpvMap);
        map_fragment.getMapAsync(this);

        RelativeLayout rloMapContainer = (RelativeLayout) findViewById(R.id.rloMapContainer);
        rloMapContainer.setVisibility(View.VISIBLE);

        GTTextView txvCompanyInfoLabel = (GTTextView) findViewById(R.id.txvCompanyInfoLabel);
        txvCompanyInfoLabel.setVisibility(View.VISIBLE);

        GTTextView txvAddress1 = (GTTextView) findViewById(R.id.txvAddress1);
        txvAddress1.setText(address_1);

        GTTextView txvAddress2 = (GTTextView) findViewById(R.id.txvAddress2);
        txvAddress2.setText(address_2);

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
                String url = merchant_controller.getMerchantContactObj().getMerchantObj().getWebsite();
                if (!url.startsWith("http://") && !url.startsWith("https://"))
                    url = "http://" + url;

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }

    private void Load_Deal_Info()
    {
        Deal deal_obj = deal_controller.getDealObj();

        Date expiration_date = deal_obj.getExpirationDate();
        SimpleDateFormat date_format =  new SimpleDateFormat("M/d/yyyy");

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int image_width = Double.valueOf(size.x).intValue();
        int image_height = Double.valueOf(image_width * .75).intValue();

        ImageView imvDealImage = (ImageView)findViewById(R.id.imvDealImage);
        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams)imvDealImage.getLayoutParams();
        params.width = image_width;
        params.height = image_height;
        imvDealImage.setLayoutParams(params);

        File imgFile = new File(deal_obj.getImage());
        if(imgFile.exists())
        {
            Set_Picture(imvDealImage);
        }

        GTTextView txvDeal = (GTTextView) findViewById(R.id.txvDeal);
        txvDeal.setText(deal_obj.getDeal());

        GTTextView txvExpirationDate = (GTTextView) findViewById(R.id.txvExpirationDate);
        txvExpirationDate.setText("Deal ends on " + date_format.format(expiration_date));

        GTTextView txvCertificatesRemaining = (GTTextView) findViewById(R.id.txvCertificatesRemaining);
        txvCertificatesRemaining.setText(deal_obj.getCertificateQuantity() + " certificates left!");

        GTTextView txvFinePrintLabel = (GTTextView) findViewById(R.id.txvFinePrintLabel);
        txvFinePrintLabel.setVisibility(View.VISIBLE);

        GTTextView txvFinePrint = (GTTextView) findViewById(R.id.txvFinePrint);
        txvFinePrint.setText(deal_obj.getFinePrintExt());

        Button btnCreateDeal = (Button) findViewById(R.id.btnCreateDeal);
        btnCreateDeal.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                add_deal_async = new Add_Deal_Async();
                add_deal_async.execute((Void) null);
            }
        });

        if(deal_obj.getDealId() == 0)
            btnCreateDeal.setText("Create Deal");
        else
            btnCreateDeal.setText("Update Deal");

        btnCreateDeal.setVisibility(View.VISIBLE);
    }

    private void Set_Picture(ImageView imvDealImage)
    {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int image_width = Double.valueOf(size.x).intValue();
        int image_height = Double.valueOf(image_width * .75).intValue();

        int targetW = image_width;
        int targetH = image_height;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(deal_controller.getDealObj().getImage(), bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        bitmap = BitmapFactory.decodeFile(deal_controller.getDealObj().getImage(), bmOptions);
        imvDealImage.setImageBitmap(bitmap);

        imvDealImage.setFocusableInTouchMode(true);
        imvDealImage.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(imvDealImage, InputMethodManager.SHOW_IMPLICIT);
    }

    private class Get_Merchant_Contact_Async extends AsyncTask<Void, Void, Boolean>
    {
        private ProgressDialog dialog;

        public Get_Merchant_Contact_Async()
        {
            dialog = new ProgressDialog(Deal_Review.this);
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
            merchant_controller.Get_Merchant_Contact();
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
                    verify_deal_async = new Verify_Deal_Async();
                    verify_deal_async.execute((Void) null);
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
            get_merchant_contact_async = null;

            if (dialog.isShowing())
            {
                dialog.dismiss();
            }
        }
    }

    private class Verify_Deal_Async extends AsyncTask<Void, Void, Boolean>
    {
        private ProgressDialog dialog;

        public Verify_Deal_Async()
        {
            dialog = new ProgressDialog(Deal_Review.this);
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
            deal_controller.setMerchantContactObj(merchant_controller.getMerchantContactObj());
            deal_controller.Verify_Deal();
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
                    Load_Merchant_Info();
                    Load_Deal_Info();
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
            verify_deal_async = null;

            if (dialog.isShowing())
            {
                dialog.dismiss();
            }
        }
    }

    private class Add_Deal_Async extends AsyncTask<Void, Void, Boolean>
    {
        private ProgressDialog dialog;

        public Add_Deal_Async()
        {
            dialog = new ProgressDialog(Deal_Review.this);
        }

        @Override
        protected void onPreExecute()
        {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
            byte[] b = stream.toByteArray();
            String imageEncoded = Base64.encodeToString(b,Base64.DEFAULT);

            deal_controller.getDealObj().setImageBase64(imageEncoded);


            if(deal_controller.getDealObj().getDealId() == 0)
                dialog.setMessage("Creating Deal");
            else
                dialog.setMessage("Updating Deal");

            dialog.setCancelable(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... login_log_data)
        {
            deal_controller.setMerchantContactObj(merchant_controller.getMerchantContactObj());
            deal_controller.Add_Deal();
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
                    Utils.deleteCache(getApplicationContext());

                    AlertDialog.Builder builder = new AlertDialog.Builder(Deal_Review.this);
                    builder.setCancelable(false);
                    builder.setTitle("Successful");
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
            verify_deal_async = null;

            if (dialog.isShowing())
            {
                dialog.dismiss();
            }
        }
    }
}
