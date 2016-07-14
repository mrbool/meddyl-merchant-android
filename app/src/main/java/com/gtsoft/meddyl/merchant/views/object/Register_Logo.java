package com.gtsoft.meddyl.merchant.views.object;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.gtsoft.meddyl.merchant.R;
import com.gtsoft.meddyl.merchant.system.gtsoft.ExifUtil;
import com.gtsoft.meddyl.merchant.system.gtsoft.GTTextView;
import com.gtsoft.meddyl.merchant.system.gtsoft.Utils;
import com.gtsoft.meddyl.merchant.views.base.View_Controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.lyft.android.scissors.CropView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import rx.Observable;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;
import static android.graphics.Bitmap.CompressFormat.JPEG;
import static rx.android.schedulers.AndroidSchedulers.mainThread;
import static rx.schedulers.Schedulers.io;


public class Register_Logo extends View_Controller
{
    ImageView imvLogo;
    GTTextView txvDescriptionLabel;

    static final int REQUEST_TAKE_PHOTO = 1;
    private static final int ACTION_TAKE_PHOTO = 1;
    private static final int ACTION_SELECT_FILE = 2;

    int image_width;
    int image_height;
    private String image;
    CropView cpvCropper;
    Button btnCancel;
    Button btnCropImage;
    CompositeSubscription subscriptions = new CompositeSubscription();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_logo);

        if(debug)
            Debug();

        Intent intent = getIntent();
        system_controller = intent.getParcelableExtra("system_controller");
        merchant_controller = intent.getParcelableExtra("merchant_controller");
        deal_controller = intent.getParcelableExtra("deal_controller");

        screen_title = "LOGO";
        left_button = "back";
        right_button = "next";

        Set_Controller_Properties();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        image_width = Double.valueOf(size.x * .95).intValue();
        image_height = image_width;

        cpvCropper = (CropView) findViewById(R.id.cpvCropper);
        cpvCropper.setViewportRatio(1);

        imvLogo = (ImageView)findViewById(R.id.imvLogo);
        txvDescriptionLabel = (GTTextView)findViewById(R.id.txvDescriptionLabel);

        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams)imvLogo.getLayoutParams();
        params.width = size.x;
        params.height = size.x;
        imvLogo.setLayoutParams(params);

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
                Show_Cropper(false);
            }
        });

        Show_Cropper(false);

        Load_Data();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK)
        {
            if(requestCode == ACTION_TAKE_PHOTO)
            {
                Handle_Selected_Camera();
            }
            else
            {
                Handle_Selected_Photo(data);
            }

            Show_Cropper(true);
        }
    }

    private void Load_Data()
    {
        if (merchant_controller.getMerchantObj().getImage() != null)
        {
            image = merchant_controller.getMerchantObj().getImage();
            File imgFile = new File(image);

            if (imgFile.exists())
            {
                Set_Picture();
            }
        }
    }

    private void Save_Data()
    {
        if(image != null)
        {
            merchant_controller.getMerchantObj().setImage(image);
        }
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

    protected void Next()
    {
        File company_file = new File(getCacheDir(), image);

        successful = true;
        if(!company_file.exists())
        {
            successful = false;
            error_title = "Need Logo";
            error_message = "Please take a picture or choose an image of your company logo";
        }

        if(!successful)
        {
            system_error_obj = null;
            Show_Alert_Dialog(error_title, error_message);
        }
        else
        {
            Save_Data();

            Intent intent = new Intent(getApplicationContext(), Merchant_Info.class);
            intent.putExtra("system_controller", system_controller);
            intent.putExtra("merchant_controller", merchant_controller);
            intent.putExtra("deal_controller", deal_controller);
            startActivityForResult(intent, 0);
        }
    }

    public void Debug()
    {

    }

    private void Select_Image()
    {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Register_Logo.this);
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
                else if (items[item].equals("Choose from Library"))
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
                photoFile = Create_Image_File();
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

    private File Create_Image_File() throws IOException
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

    private  void Handle_Selected_Camera()
    {
        Load_Cropper();

        Add_Image_To_Gallery();
    }

    private void Handle_Selected_Photo(Intent data)
    {
        Uri selectedImage = data.getData();
        image = Get_Real_Path_From_URI(selectedImage);

        Load_Cropper();
    }

    public String Get_Real_Path_From_URI(Uri contentUri)
    {
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

    private void Set_Picture()
    {
        File company_file = new File(getCacheDir(), merchant_controller.getMerchantObj().getImage());
        if(company_file.exists())
        {
            Bitmap myBitmap = BitmapFactory.decodeFile(company_file.getAbsolutePath());
            imvLogo.setImageBitmap(Utils.getRoundedCornerBitmap(myBitmap, 50));
        }
    }

    private void Add_Image_To_Gallery()
    {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(image);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void Load_Cropper()
    {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(image, bmOptions);
        bitmap = Bitmap.createScaledBitmap(bitmap, size.y, size.x, true);
        Bitmap orientedBitmap = ExifUtil.rotateBitmap(image, bitmap);
        cpvCropper.setImageBitmap(orientedBitmap);
    }

    private void Show_Cropper(Boolean show)
    {
        if(show)
        {
            txvDescriptionLabel.setVisibility(View.INVISIBLE);
            imvLogo.setVisibility(View.INVISIBLE);
            btnCropImage.setVisibility(View.VISIBLE);
            btnCancel.setVisibility(View.VISIBLE);
            cpvCropper.setVisibility(View.VISIBLE);
            getSupportActionBar().hide();
        }
        else
        {
            txvDescriptionLabel.setVisibility(View.VISIBLE);
            imvLogo.setVisibility(View.VISIBLE);
            btnCropImage.setVisibility(View.INVISIBLE);
            btnCancel.setVisibility(View.INVISIBLE);
            cpvCropper.setVisibility(View.INVISIBLE);
            getSupportActionBar().show();
        }
    }

    public void Crop_Picture()
    {
        final File croppedFile = new File(getCacheDir(), image);

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
                        Picasso.with(Register_Logo.this)
                                .load(croppedFile)
                                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                                .into(imvLogo);

                        Show_Cropper(false);
                    }
                }));
    }
}
