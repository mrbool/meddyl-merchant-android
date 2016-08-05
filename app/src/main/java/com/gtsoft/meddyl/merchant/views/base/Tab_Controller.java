package com.gtsoft.meddyl.merchant.views.base;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;

import com.gtsoft.meddyl.merchant.R;
import com.gtsoft.meddyl.merchant.views.object.Account_Frag;
import com.gtsoft.meddyl.merchant.views.object.Certificate_Lookup_Frag;
import com.gtsoft.meddyl.merchant.views.object.Deal_Create_Frag;
import com.gtsoft.meddyl.merchant.views.object.Deals_Frag;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

//public class Tab_Controller extends View_Controller implements View.OnClickListener
public class Tab_Controller extends View_Controller
{
    int selected_tab;
    protected BottomBar mBottomBar;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    private DatePickerDialog.OnDateSetListener date;
    final private Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_controller);

        Intent intent = getIntent();
        system_controller = intent.getParcelableExtra("system_controller");
        merchant_controller = intent.getParcelableExtra("merchant_controller");
        deal_controller = intent.getParcelableExtra("deal_controller");
        selected_tab = intent.getIntExtra("selected_tab", 0);

        screen_title = "DEALS";
        left_button = "";
        right_button = "";

        Set_Controller_Properties();

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.useFixedMode();
        mBottomBar.noTabletGoodness();
        mBottomBar.setDefaultTabPosition(selected_tab);
        mBottomBar.setActiveTabColor(Color.BLUE);
        mBottomBar.setTextAppearance(R.style.bottom_bar_text);
        mBottomBar.setItemsFromMenu(R.menu.bottombar_menu, new OnMenuTabClickListener()
        {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId)
            {
                Select_Tab(menuItemId, false);
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId)
            {
            }
        });

        date = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth)
            {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                Update_Expiration_Date();
            }
        };
    }

    private void Update_Expiration_Date()
    {
        String myFormat = "M/d/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        Deal_Create_Frag fragment = (Deal_Create_Frag) getSupportFragmentManager().findFragmentById(R.id.mainLinearLayout);
        fragment.Set_Text(sdf.format(myCalendar.getTime()));
    }

    @Override
    protected void Cancel()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(Tab_Controller.this);
        builder.setCancelable(false);
        builder.setTitle("Cancel");
        builder.setMessage("Are you sure you want to cancel?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        //hideKeyboard();
                        mBottomBar.selectTabAtPosition(0, true);
                    }
                }).show();
    }

    @Override
    protected void Next()
    {
        selected_tab = mBottomBar.getCurrentTabPosition();
        if (selected_tab == 1)
        {
            Deal_Create_Frag fragment = (Deal_Create_Frag) getSupportFragmentManager().findFragmentById(R.id.mainLinearLayout);
            fragment.Next();
        }
    }

    protected void Select_Tab(int menuItemId, boolean isReselection)
    {
        if (menuItemId == R.id.bb_menu_deals)
        {
            screen_title = "YOUR DEALS";
            left_button = "";
            right_button = "";

            Set_Controller_Properties();

            Bundle bundle = new Bundle();
            bundle.putParcelable("system_controller", system_controller);
            bundle.putParcelable("merchant_controller", merchant_controller);
            bundle.putParcelable("deal_controller", deal_controller);

            mBottomBar.show();

            fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragment;
            fragment = new Deals_Frag();
            fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.mainLinearLayout, fragment);
            fragmentTransaction.commit();
        }
        else if (menuItemId == R.id.bb_menu_create)
        {
            screen_title = "CREATE";
            left_button = "cancel";
            right_button = "next";

            Set_Controller_Properties();

            Bundle bundle = new Bundle();
            bundle.putParcelable("system_controller", system_controller);
            bundle.putParcelable("merchant_controller", merchant_controller);
            bundle.putParcelable("deal_controller", deal_controller);

            mBottomBar.hide();

            fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragment;
            fragment = new Deal_Create_Frag();
            fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.mainLinearLayout, fragment);
            fragmentTransaction.commit();
        }
        else if (menuItemId == R.id.bb_menu_redeem)
        {
            screen_title = "LOOKUP";
            left_button = "";
            right_button = "";

            Set_Controller_Properties();

            mBottomBar.show();

            Bundle bundle = new Bundle();
            bundle.putParcelable("system_controller", system_controller);
            bundle.putParcelable("merchant_controller", merchant_controller);
            bundle.putParcelable("deal_controller", deal_controller);

            fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragment;
            fragment = new Certificate_Lookup_Frag();
            fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.mainLinearLayout, fragment);
            fragmentTransaction.commit();
        }
        else if (menuItemId == R.id.bb_menu_account)
        {
            screen_title = "ACCOUNT";
            left_button = "";
            right_button = "";

            Set_Controller_Properties();

            mBottomBar.show();

            Bundle bundle = new Bundle();
            bundle.putParcelable("system_controller", system_controller);
            bundle.putParcelable("merchant_controller", merchant_controller);
            bundle.putParcelable("deal_controller", deal_controller);

            fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragment;
            fragment = new Account_Frag();
            fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.mainLinearLayout, fragment);
            fragmentTransaction.commit();
        }
    }

    public void Show_Picker()
    {
        InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        new DatePickerDialog(Tab_Controller.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public void Show_Alert_Dialog(String title, String message)
    {
        super.Show_Alert_Dialog(title, message);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }
}
