package com.gtsoft.meddyl.merchant.views.base;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gtsoft.meddyl.merchant.R;
import com.gtsoft.meddyl.merchant.model.object.*;
import com.gtsoft.meddyl.merchant.controller.object.*;
import com.gtsoft.meddyl.merchant.system.gtsoft.GTTextView;

public class View_Controller extends AppCompatActivity
{
    protected static boolean debug = true;
    protected boolean successful;
    protected System_Successful system_successful_obj;
    protected System_Error system_error_obj;
    protected String error_title;
    protected String error_message;

    protected Merchant_Controller merchant_controller ;
    protected Deal_Controller deal_controller;
    protected System_Controller system_controller;

    protected String screen_title;
    protected String left_button;
    protected String right_button;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if(system_controller == null)
        {
            system_controller = new System_Controller(getApplicationContext());
        }

        if(merchant_controller == null)
        {
            merchant_controller = new Merchant_Controller(getApplicationContext());
        }

        if(deal_controller == null)
        {
            deal_controller = new Deal_Controller(getApplicationContext());
        }

        getWindow().getDecorView().setBackgroundResource(R.drawable.gradient);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed()
    {
        // Do Here what ever you want do on back press;
    }

    protected void Set_Controller_Properties()
    {
        final ViewGroup actionBarLayout;

        if(left_button == "cancel" || left_button == "later")
        {
            actionBarLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.action_bar_1, null);
        }
        else
        {
            actionBarLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.action_bar_2, null);
        }

        TextView txtHeaderTitle = (TextView) actionBarLayout.findViewById(R.id.txtHeaderTitle);
        txtHeaderTitle.setText(screen_title);

        if(right_button == "")
        {
            GTTextView txtRight = (GTTextView) actionBarLayout.findViewById(R.id.txtRightButton);
            txtRight.setVisibility(TextView.INVISIBLE);
        }
        else if (right_button == "next")
        {
            GTTextView txtRightButton = (GTTextView) actionBarLayout.findViewById(R.id.txtRightButton);
            txtRightButton.setText("NEXT");
            txtRightButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Next();
                }
            });
        }
        else if(right_button == "add")
        {
            GTTextView txtRightButton = (GTTextView) actionBarLayout.findViewById(R.id.txtRightButton);
            txtRightButton.setText("ADD");
            txtRightButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Add();
                }
            });
        }
        else if(right_button == "save")
        {
            GTTextView txtRightButton = (GTTextView) actionBarLayout.findViewById(R.id.txtRightButton);
            txtRightButton.setText("SAVE");
            txtRightButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Save();
                }
            });
        }

        if(left_button == "")
        {
            ImageView txtLeftButton = (ImageView) actionBarLayout.findViewById(R.id.txtLeftButton);
            txtLeftButton.setVisibility(TextView.INVISIBLE);
        }
        else if(left_button == "cancel")
        {
            GTTextView txtLeftButton = (GTTextView) actionBarLayout.findViewById(R.id.txtLeftButton);
            txtLeftButton.setText("CANCEL");
            txtLeftButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Cancel();
                }
            });
        }
        else if(left_button == "back")
        {
            ImageView txtLeftButton = (ImageView) actionBarLayout.findViewById(R.id.txtLeftButton);
            txtLeftButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Back();
                }
            });
        }
        else if(left_button == "later")
        {
            GTTextView txtLeftButton = (GTTextView) actionBarLayout.findViewById(R.id.txtLeftButton);
            txtLeftButton.setText("LATER");
            txtLeftButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Later();
                }
            });
        }

        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(actionBarLayout);
    }

    protected void Cancel()
    {
        finish();
    }

    protected void Back()
    {
        finish();
    }

    protected void Later()
    {
        finish();
    }

    protected void Save()
    {
    }

    protected void Add()
    {
    }

    protected void Next()
    {

    }

    protected void Clear_Login()
    {
//        Session fb_session = Session.getActiveSession();
//        if(fb_session!=null)
//        {
//            fb_session.closeAndClearTokenInformation();
//        }
//
//        SharedPreferences prefs = getApplicationContext().getSharedPreferences("com.gtsoft.visopa.visopa_customer", Context.MODE_PRIVATE);
//        prefs.edit().remove("customer_id").apply();
    }

    public void Save_Login(int customer_id)
    {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("com.gtsoft.visopa.visopa_customer", Context.MODE_PRIVATE);
        prefs.edit().putInt("customer_id", customer_id).apply();
    }

    public void Show_Alert_Dialog_Error(Context context)
    {
        String error_message;
        String button;
        String title;

        if(system_error_obj.getCode() == 6000)
        {
            title = "Software Upgrade Needed";
            error_message = system_error_obj.getMessage();
            button = "Upgrade";
        }
        else
        {
            title = "Oops!";
            error_message = system_error_obj.getMessage();
            button = "OK";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setMessage(error_message)
                .setTitle(title)
                .setNeutralButton(button, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.cancel();

                        if (system_error_obj.getCode() == 6000)
                        {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.gtsoft.visopa.visopa_customer"));

                            if (intent.resolveActivity(getPackageManager()) != null)
                            {
                                startActivity(intent);
                            }
                        }
//                        else
//                        {
//                            finish();
//                            //System.exit(0);
//                        }
                    }
                }).create().show();
    }

    public void Show_Alert_Dialog(String title, String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setTitle(title)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void showPopup(View anchorView, String text)
    {
        View popupView = getLayoutInflater().inflate(R.layout.popup, null);

        Display display = getWindowManager().getDefaultDisplay();
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

    protected void hideKeyboard()
    {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
}


