package com.gtsoft.meddyl.merchant.views.object;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.gtsoft.meddyl.merchant.R;
import com.gtsoft.meddyl.merchant.model.object.Industry;
import com.gtsoft.meddyl.merchant.model.object.Merchant;
import com.gtsoft.meddyl.merchant.model.object.Neighborhood;
import com.gtsoft.meddyl.merchant.model.object.Zip_Code;
import com.gtsoft.meddyl.merchant.system.gtsoft.ExifUtil;
import com.gtsoft.meddyl.merchant.system.gtsoft.TextWatcherPhone;
import com.gtsoft.meddyl.merchant.system.gtsoft.Utils;
import com.gtsoft.meddyl.merchant.views.adapter.Industry_Adapter;
import com.gtsoft.meddyl.merchant.views.adapter.Neighborhood_Adapter;
import com.gtsoft.meddyl.merchant.views.base.View_Controller;
import com.lyft.android.scissors.CropView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.droidparts.widget.ClearableEditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

import static android.graphics.Bitmap.CompressFormat.JPEG;
import static rx.android.schedulers.AndroidSchedulers.mainThread;
import static rx.schedulers.Schedulers.io;


public class Merchant_Edit extends View_Controller
{
    private ProgressDialog dialog;

    private ScrollView scrollView;

    ImageView imvLogo;
    static final int REQUEST_TAKE_PHOTO = 1;
    private static final int ACTION_TAKE_PHOTO = 1;
    private static final int ACTION_SELECT_FILE = 2;
    private String image;
    CropView cpvCropper;
    Button btnCancel;
    Button btnCropImage;
    CompositeSubscription subscriptions = new CompositeSubscription();

    private ClearableEditText edtCompanyName;
    private ClearableEditText edtDescription;
    private ClearableEditText edtAddress1;
    private ClearableEditText edtAddress2;
    private ClearableEditText edtZipCode;
    private ClearableEditText edtCompanyPhone;
    private ClearableEditText edtWebsite;

    private String zip_code;

    private Spinner spnIndustry;
    private Industry_Adapter adapter_industry;
    int industry_id;

    Spinner spnNeighborhood;
    private Neighborhood_Adapter adapter_neighborhood;
    int neighborhood_id;

    private Load_Industries_Async load_industries_async = null;
    private Get_Merchant_Contact_Async get_merchant_contact_async = null;
    private Get_Neighborhood_By_Zip_Async get_neighborhood_by_zip_async = null;
    private Update_Merchant_Async update_merchant_async = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_edit);

        dialog = new ProgressDialog(Merchant_Edit.this);
        dialog.setMessage("Loading");
        dialog.setCancelable(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();

        if(debug)
            Debug();

        Intent intent = getIntent();
        system_controller = intent.getParcelableExtra("system_controller");
        merchant_controller = intent.getParcelableExtra("merchant_controller");
        deal_controller = intent.getParcelableExtra("deal_controller");

        screen_title = "EDIT";
        left_button = "back";
        right_button = "save";

        Set_Controller_Properties();

        image = "";

        scrollView = (ScrollView) findViewById(R.id.scrollView);
        edtCompanyName = (ClearableEditText) findViewById(R.id.edtCompanyName);
        edtDescription = (ClearableEditText) findViewById(R.id.edtDescription);
        edtAddress1 = (ClearableEditText) findViewById(R.id.edtAddress1);
        edtAddress2 = (ClearableEditText) findViewById(R.id.edtAddress2);
        edtZipCode = (ClearableEditText) findViewById(R.id.edtZipCode);
        edtCompanyPhone = (ClearableEditText) findViewById(R.id.edtCompanyPhone);
        edtWebsite = (ClearableEditText) findViewById(R.id.edtWebsite);
        spnIndustry = (Spinner) findViewById(R.id.spnIndustry);
        spnNeighborhood = (Spinner) findViewById(R.id.spnNeighborhood);

        edtZipCode.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                Get_Neighborhoods();
            }
        });

        edtDescription.setRawInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        edtDescription.setImeActionLabel(getResources().getString(R.string.done), EditorInfo.IME_ACTION_DONE);
        edtDescription.setImeOptions(EditorInfo.IME_ACTION_DONE);
        edtDescription.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event == null)
                {
                    if (actionId == EditorInfo.IME_ACTION_DONE)
                    {
                        // Capture soft enters in a singleLine EditText that is the last EditText
                        // This one is useful for the new list case, when there are no existing ListItems
                        edtDescription.clearFocus();
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

        load_industries_async = new Load_Industries_Async();
        load_industries_async.execute((Void) null);
    }

    private void Get_Neighborhoods()
    {
        zip_code = edtZipCode.getText().toString().trim();
        int length = zip_code.length();

        if(length == 5)
        {
            get_neighborhood_by_zip_async = new Get_Neighborhood_By_Zip_Async();
            get_neighborhood_by_zip_async.execute((Void) null);
        }
    }

    private void Load_Data()
    {
        Merchant merchant_obj = merchant_controller.getMerchantContactObj().getMerchantObj();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        cpvCropper = (CropView) findViewById(R.id.cpvCropper);
        cpvCropper.setViewportRatio(1);

        imvLogo = (ImageView) findViewById(R.id.imvLogo);
        imvLogo.getLayoutParams().width = size.x/2;
        imvLogo.getLayoutParams().height = size.x/2;
        imvLogo.requestLayout();
        imvLogo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Select_Image();
            }
        });

        btnCropImage = (Button) findViewById(R.id.btnCropImage);
        btnCropImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Crop_Picture();
            }
        });

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                image = "";
                Show_Cropper(false);
            }
        });

        Show_Cropper(false);

        AQuery aq = new AQuery(getApplicationContext());
        aq.id(imvLogo).image(merchant_obj.getImage());

        edtCompanyName.setText(merchant_obj.getCompanyName());
        spnIndustry.setSelection(adapter_industry.Get_Index_Of_Id(merchant_obj.getIndustryObj().getIndustryId()));
        edtDescription.setText(merchant_obj.getDescription());
        edtAddress1.setText(merchant_obj.getAddress1());
        edtAddress2.setText(merchant_obj.getAddress2());
        edtCompanyPhone.setText(Utils.Format_Phone(merchant_obj.getPhone()));
        edtWebsite.setText(merchant_obj.getWebsite());

        neighborhood_id = merchant_obj.getNeighborhoodObj().getNeighborhoodId();
        zip_code = merchant_obj.getZipCodeObj().getZipCode();

        imvLogo.setVisibility(View.VISIBLE);
        edtCompanyName.setVisibility(View.VISIBLE);
        spnIndustry.setVisibility(View.VISIBLE);
        edtDescription.setVisibility(View.VISIBLE);
        edtAddress1.setVisibility(View.VISIBLE);
        edtAddress2.setVisibility(View.VISIBLE);
        edtZipCode.setVisibility(View.VISIBLE);
        edtCompanyPhone.setVisibility(View.VISIBLE);
        edtWebsite.setVisibility(View.VISIBLE);

        edtZipCode.setText(merchant_obj.getZipCodeObj().getZipCode());

        edtCompanyPhone.addTextChangedListener(new TextWatcherPhone(edtCompanyPhone));

        if (dialog.isShowing())
        {
            dialog.dismiss();
        }

        scrollView.scrollTo(0, scrollView.getTop());
        hideKeyboard();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK)
        {

            if(requestCode == ACTION_TAKE_PHOTO)
            {
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);

                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeFile(image, bmOptions);
                bitmap = Bitmap.createScaledBitmap(bitmap, size.y, size.x, false);
                Bitmap orientedBitmap = ExifUtil.rotateBitmap(image, bitmap);
                cpvCropper.setImageBitmap(orientedBitmap);
            }
            else
            {
                Uri selectedImage = data.getData();
                image = getRealPathFromURI(selectedImage);

                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);

                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeFile(image, bmOptions);
                bitmap = Bitmap.createScaledBitmap(bitmap, size.y, size.x, true);
                Bitmap orientedBitmap = ExifUtil.rotateBitmap(image, bitmap);
                cpvCropper.setImageBitmap(orientedBitmap);

                //cpvCropper.extensions().load(galleryPictureUri);
            }

            Show_Cropper(true);
        }
        else
        {
            image = "";
        }
    }

    public String getRealPathFromURI(Uri contentUri) {

        // can post image
        String [] proj={MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery( contentUri,
                proj, // Which columns to return
                null,       // WHERE clause; which rows to return (all rows)
                null,       // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

    private void Show_Cropper(Boolean show)
    {
        if(show)
        {
            scrollView.setVisibility(View.GONE);
            btnCropImage.setVisibility(View.VISIBLE);
            btnCancel.setVisibility(View.VISIBLE);
            cpvCropper.setVisibility(View.VISIBLE);
            getSupportActionBar().hide();
        }
        else
        {
            scrollView.setVisibility(View.VISIBLE);
            btnCropImage.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
            cpvCropper.setVisibility(View.GONE);
            getSupportActionBar().show();
        }
    }

    private void Select_Image()
    {
        final CharSequence[] items = {"Take Photo", "Choose Photo", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Merchant_Edit.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int item)
            {
                if (items[item].equals("Take Photo"))
                {
                    Call_Camera();
                }
                else if (items[item].equals("Choose Photo"))
                {
                    Call_Photo_Library();
                }
                else if (items[item].equals("Cancel"))
                {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void Call_Photo_Library()
    {
        //cpvCropper.setVisibility(View.VISIBLE);
//        cpvCropper.extensions().pickUsing(this, 10001);

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, ACTION_SELECT_FILE);
    }

    private void Call_Camera()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
        {
            File photoFile = null;
            try
            {
                photoFile = createImageFile();
                image = photoFile.getAbsolutePath();
            }
            catch (IOException ex)
            {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null)
            {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException
    {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        //photo_path = "file:" + image.getAbsolutePath();
        return image;
    }

    public void Crop_Picture()
    {
        final File croppedFile = new File(getCacheDir(), "company_logo.jpg");

        Observable<Void> onSave = Observable.from(cpvCropper.extensions()
                .crop()
                .quality(100)
                .format(JPEG)
                .into(croppedFile))
                .subscribeOn(io())
                .observeOn(mainThread());

        subscriptions.add(onSave
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void nothing) {
                        Picasso.with(Merchant_Edit.this)
                                .load(croppedFile)
                                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                                .into(imvLogo);

                        Show_Cropper(false);
                    }
                }));
    }

    public void Save()
    {
        String company_name = edtCompanyName.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();
        String address_1 = edtAddress1.getText().toString().trim();
        String address_2 = edtAddress2.getText().toString().trim();
        zip_code = edtZipCode.getText().toString().trim();
        String company_phone = edtCompanyPhone.getText().toString().replaceAll("[^\\d]", "");
        String website = edtWebsite.getText().toString().trim();

        successful = true;
        if(company_name.length() == 0)
        {
            successful = false;
            error_title = "Need Company Name";
            error_message = "Company name cannot be blank";
            edtCompanyName.requestFocus();
        }
        else if(description.length() == 0)
        {
            successful = false;
            error_title = "Description";
            error_message = "Please add a company description";
            edtDescription.requestFocus();
        }
        else if(address_1.length() == 0)
        {
            successful = false;
            error_title = "Need Address";
            error_message = "Address cannot be blank";
            edtAddress1.requestFocus();
        }
        else if(zip_code.length() != 5)
        {
            successful = false;
            error_title = "Zip Code Incorrect";
            error_message = "Zip code is incorrect";
            edtZipCode.requestFocus();
        }
        else if(company_phone.length() == 0)
        {
            successful = false;
            error_title = "Phone Incorrect";
            error_message = "You must enter a valid phone number";
            edtCompanyPhone.requestFocus();
        }
        else if(website.length() == 0)
        {
            successful = false;
            error_title = "Need Website";
            error_message = "Please enter your company's website or yelp link";
            edtWebsite.requestFocus();
        }
        else if(industry_id == 0)
        {
            successful = false;
            error_title = "Need Industry";
            error_message = "Please select and industry";
            hideKeyboard();
            spnIndustry.setFocusable(true);
            spnIndustry.setFocusableInTouchMode(true);
            spnIndustry.requestFocus();
        }

        if(!successful)
        {
            system_error_obj = null;
            Show_Alert_Dialog(error_title, error_message);
        }
        else
        {
            String logo_base_64;
            if(image == "")
            {
                logo_base_64 = "";
            }
            else
            {
                File company_file = new File(getCacheDir(), "company_logo.jpg");
                Bitmap bitmap = BitmapFactory.decodeFile(company_file.getAbsolutePath());
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                byte[] b = stream.toByteArray();
                logo_base_64 = Base64.encodeToString(b, Base64.DEFAULT);
            }

            Industry industry_obj = new Industry();
            industry_obj.setIndustryId(industry_id);

            Neighborhood neighborhood_obj = new Neighborhood();
            neighborhood_obj.setNeighborhoodId(neighborhood_id);

            Zip_Code zip_code_obj = new Zip_Code();
            zip_code_obj.setZipCode(zip_code);

            Merchant merchant_obj = new Merchant();
            merchant_obj.setImageBase64(logo_base_64);
            merchant_obj.setCompanyName(company_name);
            merchant_obj.setDescription(description);
            merchant_obj.setAddress1(address_1);
            merchant_obj.setAddress2(address_2);
            merchant_obj.setPhone(company_phone);
            merchant_obj.setWebsite(website);
            merchant_obj.setIndustryObj(industry_obj);
            merchant_obj.setZipCodeObj(zip_code_obj);
            merchant_obj.setNeighborhoodObj(neighborhood_obj);

            merchant_controller.setMerchantObj(merchant_obj);

            update_merchant_async = new Update_Merchant_Async();
            update_merchant_async.execute((Void) null);
        }
    }

    public void Back()
    {
        edited = false;

        if(!image.equals(""))
        {
            edited = true;
        }
        else if(!edtCompanyName.getText().toString().equals(merchant_controller.getMerchantContactObj().getMerchantObj().getCompanyName()))
        {
            edited = true;
        }
        else if(industry_id != merchant_controller.getMerchantContactObj().getMerchantObj().getIndustryObj().getIndustryId())
        {
            edited = true;
        }
        else if(!edtDescription.getText().toString().equals(merchant_controller.getMerchantContactObj().getMerchantObj().getDescription()))
        {
            edited = true;
        }
        else if(!edtAddress1.getText().toString().equals(merchant_controller.getMerchantContactObj().getMerchantObj().getAddress1()))
        {
            edited = true;
        }
        else if(!edtAddress2.getText().toString().equals(merchant_controller.getMerchantContactObj().getMerchantObj().getAddress2()))
        {
            edited = true;
        }
        else if(!edtZipCode.getText().toString().equals(merchant_controller.getMerchantContactObj().getMerchantObj().getZipCodeObj().getZipCode()))
        {
            edited = true;
        }
        else if(neighborhood_id != merchant_controller.getMerchantContactObj().getMerchantObj().getNeighborhoodObj().getNeighborhoodId())
        {
            edited = true;
        }
        else if(!edtCompanyPhone.getText().toString().replaceAll("[^\\d]", "").equals(merchant_controller.getMerchantContactObj().getMerchantObj().getPhone()))
        {
            edited = true;
        }
        else if(!edtWebsite.getText().toString().equals(merchant_controller.getMerchantContactObj().getMerchantObj().getWebsite()))
        {
            edited = true;
        }

        if(edited)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(Merchant_Edit.this);
            builder.setCancelable(false);
            builder.setTitle("Cancel");
            builder.setMessage("You have unsaved changes, are you sure you want to cancel?")
                    .setNegativeButton("No", null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton)
                        {
                            finish();
                        }
                    }).show();
        }
        else
        {
            finish();
        }
    }

    public void Debug()
    {

    }

    private class Get_Neighborhood_By_Zip_Async extends AsyncTask<Void, Void, Boolean>
    {
        public Get_Neighborhood_By_Zip_Async()
        {
        }

        @Override
        protected void onPreExecute()
        {
        }

        @Override
        protected Boolean doInBackground(Void... login_log_data)
        {
            Zip_Code zip_code_obj = new Zip_Code();
            zip_code_obj.setZipCode(zip_code);

            system_controller.setZipCodeObj(zip_code_obj);
            system_controller.Get_Neighborhood_By_Zip();
            successful = system_controller.getSuccessful();
            system_error_obj = system_controller.getSystemErrorObj();
            system_successful_obj = system_controller.getSystemSuccessfulObj();

            return successful;
        }

        @Override
        protected void onPostExecute(final Boolean success)
        {
            try
            {
                if (!successful)
                {
                    Show_Alert_Dialog_Error(Merchant_Edit.this);
                }
                else
                {
                    if (system_controller.getZipCodeObj().getNeighborhoodObjArray().length == 0)
                    {
                        spnNeighborhood.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        spnNeighborhood.setVisibility(View.VISIBLE);

                        Neighborhood default_selection = new Neighborhood();
                        default_selection.setNeighborhood("Select Neighborhood");
                        default_selection.setNeighborhoodId(0);

                        List<Neighborhood> hold_list = new ArrayList<Neighborhood>(Arrays.asList(system_controller.getZipCodeObj().getNeighborhoodObjArray()));
                        hold_list.add(default_selection);

                        Neighborhood[] spinner_data = new Neighborhood[hold_list.size()];
                        for (int i = 0; i < hold_list.size(); i++) {
                            spinner_data[i] = hold_list.get(i);
                        }

                        adapter_neighborhood = new Neighborhood_Adapter(Merchant_Edit.this,
                                android.R.layout.simple_spinner_item,
                                spinner_data);
                        spnNeighborhood.setAdapter(adapter_neighborhood);
                        spnNeighborhood.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                        {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                                // Here you get the current item (a User object) that is selected by its position
                                Neighborhood neighborhood_obj = adapter_neighborhood.getItem(position);
                                neighborhood_id = neighborhood_obj.getNeighborhoodId();
                                //neighborhood = neighborhood_obj.getNeighborhood();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapter)
                            {
                            }
                        });

                        if(neighborhood_id == 0)
                        {
                            spnNeighborhood.setSelection(spinner_data.length - 1);
                            spnNeighborhood.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            spnNeighborhood.setSelection(adapter_neighborhood.Get_Index_Of_Id(neighborhood_id));
                        }
                    }
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
            get_neighborhood_by_zip_async = null;
        }
    }

    private class Load_Industries_Async extends AsyncTask<Void, Void, Boolean>
    {
        public Load_Industries_Async()
        {
        }

        @Override
        protected void onPreExecute()
        {
        }

        @Override
        protected Boolean doInBackground(Void... login_log_data)
        {
            Industry industry_obj = new Industry();
            industry_obj.setParentIndustryId(2);

            system_controller.setIndustryObj(industry_obj);
            system_controller.Get_Industry_Parent_Level();
            successful = system_controller.getSuccessful();
            system_error_obj = system_controller.getSystemErrorObj();

            return successful;
        }

        @Override
        protected void onPostExecute(final Boolean success)
        {
            try
            {
                if (!successful)
                {
                    Show_Alert_Dialog_Error(Merchant_Edit.this);
                }
                else
                {
                    adapter_industry = new Industry_Adapter(Merchant_Edit.this,
                            android.R.layout.simple_spinner_item,
                            system_controller.getIndustryObjArray());
                    spnIndustry.setAdapter(adapter_industry); // Set the custom adapter_industry to the spinner

                    // You can create an anonymous listener to handle the event when is selected an spinner item
                    spnIndustry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
                        {
                            // Here you get the current item (a User object) that is selected by its position
                            Industry industry_obj = adapter_industry.getItem(position);
                            industry_id = industry_obj.getIndustryId();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapter_industry) {  }
                    });

                    get_merchant_contact_async = new Get_Merchant_Contact_Async();
                    get_merchant_contact_async.execute((Void) null);
                }
            }
            catch (Exception ex)
            {
                Log.i("error in async", ex.getMessage());
            }
        }

        @Override
        protected void onCancelled()
        {
            load_industries_async = null;
        }
    }
    
    private class Get_Merchant_Contact_Async extends AsyncTask<Void, Void, Boolean>
    {

        public Get_Merchant_Contact_Async()
        {
        }

        @Override
        protected void onPreExecute()
        {
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
            get_merchant_contact_async = null;
        }
    }

    private class Update_Merchant_Async extends AsyncTask<Void, Void, Boolean>
    {
        private ProgressDialog dialog;

        public Update_Merchant_Async()
        {
            dialog = new ProgressDialog(Merchant_Edit.this);
        }

        @Override
        protected void onPreExecute()
        {
            dialog.setMessage("Updating");
            dialog.setCancelable(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... login_log_data)
        {
            merchant_controller.Update_Merchant();
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
                    if(image!="")
                        Utils.deleteCache(getApplicationContext());

                    AlertDialog.Builder builder = new AlertDialog.Builder(Merchant_Edit.this);
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
            update_merchant_async = null;

            if (dialog.isShowing())
            {
                dialog.dismiss();
            }
        }
    }
}
