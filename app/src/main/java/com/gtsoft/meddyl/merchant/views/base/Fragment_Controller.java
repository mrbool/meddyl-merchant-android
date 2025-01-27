package com.gtsoft.meddyl.merchant.views.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.gtsoft.meddyl.merchant.controller.object.Deal_Controller;
import com.gtsoft.meddyl.merchant.controller.object.Merchant_Controller;
import com.gtsoft.meddyl.merchant.controller.object.System_Controller;
import com.gtsoft.meddyl.merchant.model.object.System_Error;
import com.gtsoft.meddyl.merchant.model.object.System_Successful;


public class Fragment_Controller extends Fragment
{
    protected static boolean debug = false;
    protected boolean successful;
    protected System_Successful system_successful_obj;
    protected System_Error system_error_obj;
    protected String error_title;
    protected String error_message;

    protected Merchant_Controller merchant_controller ;
    protected Deal_Controller deal_controller;
    protected System_Controller system_controller;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        return null;
    }

    protected void Hide_Keyboard()
    {
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }
}
