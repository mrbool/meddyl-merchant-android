package com.gtsoft.meddyl.merchant.model.response;

import com.gtsoft.meddyl.merchant.model.object.System_Error;

public class JSONErrorResponse extends JSONResponse
{
    private System_Error system_error_obj;


    public System_Error getSystemError() {
        return this.system_error_obj;
    }

    public void setSystemError(System_Error system_error_obj) {
        this.system_error_obj = system_error_obj;
    }
}

