package com.gtsoft.meddyl.merchant.model.object;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.Date;

public class Password_Reset_Status implements Parcelable
{
	private int status_id;
	private String status;

	public Password_Reset_Status()
	{

	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flag)
	{
		out.writeInt(status_id);
		out.writeString(status);
	}

	public static final Creator<Password_Reset_Status> CREATOR = new Creator<Password_Reset_Status>()
	{
		public Password_Reset_Status createFromParcel(Parcel in)
		{
			return new Password_Reset_Status(in);
		}

		public Password_Reset_Status[] newArray(int size)
		{
			return new Password_Reset_Status[size];
		}
	};

	private Password_Reset_Status(Parcel in)
	{
		status_id = in.readInt();
		status = in.readString();
	}

	public int getStatusId()
	{
		return this.status_id;
	}
	public void setStatusId(int status_id)
	{
		this.status_id = status_id;
	}

	public String getStatus()
	{
		return this.status;
	}
	public void setStatus(String status)
	{
		this.status = status;
	}
}
