package com.gtsoft.meddyl.merchant.views.object;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.gtsoft.meddyl.merchant.R;
import com.gtsoft.meddyl.merchant.system.gtsoft.ExifUtil;
import com.gtsoft.meddyl.merchant.system.gtsoft.Utils;
import com.gtsoft.meddyl.merchant.views.base.View_Controller;

import org.droidparts.widget.ClearableEditText;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Deal_Image extends View_Controller
{
    private static final int ACTION_TAKE_PHOTO = 1;
    private static final int ACTION_SELECT_FILE = 2;

    private String image;

    ImageView imvDeal;
    int image_width;
    int image_height;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deal_image);

        if(debug)
            Debug();

        Intent intent = getIntent();
        system_controller = intent.getParcelableExtra("system_controller");
        merchant_controller = intent.getParcelableExtra("merchant_controller");
        deal_controller = intent.getParcelableExtra("deal_controller");

        screen_title = "IMAGE";
        left_button = "back";
        right_button = "next";

        Set_Controller_Properties();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        image_width = Double.valueOf(size.x * .95).intValue();
        image_height = Double.valueOf(image_width * .75).intValue();

        imvDeal = (ImageView)findViewById(R.id.imvDeal);

        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams)imvDeal.getLayoutParams();
        params.width = image_width;
        params.height = image_height;
        imvDeal.setLayoutParams(params);
        imvDeal.setX(30);

        imvDeal.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Select_Image();
            }
        });

        Load_Data();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK)
        {
            if(requestCode == ACTION_TAKE_PHOTO)
            {
                Handle_Selected_Camera();
            }
            else if(requestCode == ACTION_SELECT_FILE)
            {
                Handle_Selected_Photo(data);
            }
        }
    }

    private void Load_Data()
    {
        if(deal_controller.getDealObj().getDealId() != 0)
        {
            /* first try to load from file, else use url */
            File disk_file = new File(deal_controller.getDealObj().getImage());
            if (disk_file.exists())
            {
                image = deal_controller.getDealObj().getImage();
                Set_Picture();
            }
            else
            {
                AQuery aq_stars = new AQuery(getApplicationContext());
                File url_file = aq_stars.getCachedFile(deal_controller.getDealObj().getImage());
                if (url_file.exists())
                {
                    image = url_file.getAbsolutePath();
                    Set_Picture();
                }
            }
        }
        else
        {
            if (deal_controller.getDealObj().getImage() != null)
            {
                image = deal_controller.getDealObj().getImage();
                File imgFile = new File(deal_controller.getDealObj().getImage());

                if (imgFile.exists())
                {
                    Set_Picture();
                }
            }
        }
    }

    private void Save_Data()
    {
        if(image != null)
        {
            deal_controller.getDealObj().setImage(image);
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

    @Override
    protected void Next()
    {
        if(image == null)
        {
            Show_Alert_Dialog("Image", "Please select an image");
        }
        else
        {
            Save_Data();

            Intent intent = new Intent(getApplicationContext(), Deal_Review.class);
            intent.putExtra("system_controller", system_controller);
            intent.putExtra("merchant_controller", merchant_controller);
            intent.putExtra("deal_controller", deal_controller);
            startActivityForResult(intent, 1);
        }
    }

    public void Debug()
    {

    }

    private void Select_Image()
    {
        final CharSequence[] items = {"Take Photo", "Choose Photo",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Deal_Image.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int item)
            {
                if (items[item].equals("Take Photo"))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Deal_Image.this);
                    builder.setCancelable(false);
                    builder.setTitle("Take a Picture!");
                    builder.setMessage("For best results, rotate your camera sideways")
                            .setNeutralButton("OK", new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    dialog.cancel();

                                    Call_Camera();
                                }
                            }).create().show();

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
                startActivityForResult(takePictureIntent, ACTION_TAKE_PHOTO);
            }
        }
    }

    private File Create_Image_File() throws IOException
    {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
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
        Set_Picture();

        Add_Image_To_Gallery();
    }

    private void Handle_Selected_Photo(Intent data)
    {
        Uri selectedImage = data.getData();
        image = Get_Real_Path_From_URI(selectedImage);

        Set_Picture();
    }

    public String Get_Real_Path_From_URI(Uri contentUri)
    {
//        String [] proj={MediaStore.Images.Media.DATA};
//        Cursor cursor = managedQuery( contentUri,
//                proj, // Which columns to return
//                null,       // WHERE clause; which rows to return (all rows)
//                null,       // WHERE clause selection arguments (none)
//                null); // Order-by clause (ascending by name)
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//
//        return cursor.getString(column_index);

        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst())
        {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    private void Set_Picture()
    {
        int targetW = image_width;
        int targetH = image_height;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(image, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(image, bmOptions);
        imvDeal.setImageBitmap(bitmap);

        imvDeal.setFocusableInTouchMode(true);
        imvDeal.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(imvDeal, InputMethodManager.SHOW_IMPLICIT);
    }

    private void Add_Image_To_Gallery()
    {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(image);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
}
