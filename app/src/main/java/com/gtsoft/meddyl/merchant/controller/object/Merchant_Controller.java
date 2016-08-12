package com.gtsoft.meddyl.merchant.controller.object;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.gtsoft.meddyl.merchant.controller.base.Base_Controller;
import com.gtsoft.meddyl.merchant.controller.rest_interface.REST_MerchantService;
import com.gtsoft.meddyl.merchant.model.object.*;
import com.gtsoft.meddyl.merchant.model.response.*;


public class Merchant_Controller extends Base_Controller implements Parcelable
{
	private Application_Type[] application_type_obj_array;
	private Certificate certificate_obj;
	private Certificate[] certificate_obj_array;
	private Certificate_Log certificate_log_obj;
	private Certificate_Log[] certificate_log_obj_array;
	private Certificate_Payment certificate_payment_obj;
	private Certificate_Payment[] certificate_payment_obj_array;
	private Certificate_Status certificate_status_obj;
	private Certificate_Status[] certificate_status_obj_array;
	private City city_obj;
	private City[] city_obj_array;
	private Contact contact_obj;
	private Contact[] contact_obj_array;
	private Contact_GPS_Log contact_gps_log_obj;
	private Contact_GPS_Log[] contact_gps_log_obj_array;
	private Credit_Card credit_card_obj;
	private Credit_Card[] credit_card_obj_array;
	private Credit_Card_Type credit_card_type_obj;
	private Credit_Card_Type[] credit_card_type_obj_array;
	private Customer customer_obj;
	private Customer[] customer_obj_array;
	private Customer_Deal_Log customer_deal_log_obj;
	private Customer_Deal_Log[] customer_deal_log_obj_array;
	private Customer_Search_Location_Type customer_search_location_type_obj;
	private Customer_Search_Location_Type[] customer_search_location_type_obj_array;
	private Customer_Status customer_status_obj;
	private Customer_Status[] customer_status_obj_array;
	private Deal deal_obj;
	private Deal[] deal_obj_array;
	private Deal_Fine_Print_Option deal_fine_print_option_obj;
	private Deal_Fine_Print_Option[] deal_fine_print_option_obj_array;
	private Deal_Log deal_log_obj;
	private Deal_Log[] deal_log_obj_array;
	private Deal_Payment deal_payment_obj;
	private Deal_Payment[] deal_payment_obj_array;
	private Deal_Status deal_status_obj;
	private Deal_Status[] deal_status_obj_array;
	private Deal_Validation deal_validation_obj;
	private Deal_Validation[] deal_validation_obj_array;
	private Email_Template email_template_obj;
	private Email_Template[] email_template_obj_array;
	private Facebook_Data_Friends facebook_data_friends_obj;
	private Facebook_Data_Friends[] facebook_data_friends_obj_array;
	private Facebook_Data_Hometown facebook_data_hometown_obj;
	private Facebook_Data_Hometown[] facebook_data_hometown_obj_array;
	private Facebook_Data_Location facebook_data_location_obj;
	private Facebook_Data_Location[] facebook_data_location_obj_array;
	private Facebook_Data_Profile facebook_data_profile_obj;
	private Facebook_Data_Profile[] facebook_data_profile_obj_array;
	private Fine_Print_Option fine_print_option_obj;
	private Fine_Print_Option[] fine_print_option_obj_array;
	private Industry industry_obj;
	private Industry[] industry_obj_array;
	private Login_Log[] login_log_obj_array;
	private Merchant merchant_obj;
	private Merchant[] merchant_obj_array;
	private Merchant_Contact merchant_contact_obj;
	private Merchant_Contact[] merchant_contact_obj_array;
	private Merchant_Contact_Status merchant_contact_status_obj;
	private Merchant_Contact_Status[] merchant_contact_status_obj_array;
	private Merchant_Contact_Validation merchant_contact_validation_obj;
	private Merchant_Contact_Validation[] merchant_contact_validation_obj_array;
	private Merchant_Rating merchant_rating_obj;
	private Merchant_Rating[] merchant_rating_obj_array;
	private Merchant_Status merchant_status_obj;
	private Merchant_Status[] merchant_status_obj_array;
	private Neighborhood neighborhood_obj;
	private Neighborhood[] neighborhood_obj_array;
	private Password_Reset password_reset_obj;
	private Password_Reset[] password_reset_obj_array;
	private Password_Reset_Status password_reset_status_obj;
	private Password_Reset_Status[] password_reset_status_obj_array;
	private Payment_Log payment_log_obj;
	private Payment_Log[] payment_log_obj_array;
	private Plivo_Phone_Number plivo_phone_number_obj;
	private Plivo_Phone_Number[] plivo_phone_number_obj_array;
	private Promotion promotion_obj;
	private Promotion[] promotion_obj_array;
	private Promotion_Activity promotion_activity_obj;
	private Promotion_Activity[] promotion_activity_obj_array;
	private Promotion_Type promotion_type_obj;
	private Promotion_Type[] promotion_type_obj_array;
	private SMS_Template sms_template_obj;
	private SMS_Template[] sms_template_obj_array;
	private State state_obj;
	private State[] state_obj_array;
	private System_Error system_error_obj;
	private System_Error[] system_error_obj_array;
	private System_Settings system_settings_obj;
	private System_Settings[] system_settings_obj_array;
	private System_Successful system_successful_obj;
	private System_Successful[] system_successful_obj_array;
	private System_Users system_users_obj;
	private System_Users[] system_users_obj_array;
	private Time_Zone time_zone_obj;
	private Time_Zone[] time_zone_obj_array;
	private Twilio_Phone_Number twilio_phone_number_obj;
	private Twilio_Phone_Number[] twilio_phone_number_obj_array;
	private Zip_Code zip_code_obj;
	private Zip_Code[] zip_code_obj_array;


	public Merchant_Controller(Context context)
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
		out.writeParcelable(application_type_obj, flag);
		out.writeParcelable(certificate_obj, flag);
		out.writeParcelable(certificate_log_obj, flag);
		out.writeParcelable(certificate_payment_obj, flag);
		out.writeParcelable(certificate_status_obj, flag);
		out.writeParcelable(city_obj, flag);
		out.writeParcelable(contact_obj, flag);
		out.writeParcelable(contact_gps_log_obj, flag);
		out.writeParcelable(credit_card_obj, flag);
		out.writeParcelable(credit_card_type_obj, flag);
		out.writeParcelable(customer_obj, flag);
		out.writeParcelable(customer_deal_log_obj, flag);
		out.writeParcelable(customer_search_location_type_obj, flag);
		out.writeParcelable(customer_status_obj, flag);
		out.writeParcelable(deal_obj, flag);
		out.writeParcelable(deal_fine_print_option_obj, flag);
		out.writeParcelable(deal_log_obj, flag);
		out.writeParcelable(deal_payment_obj, flag);
		out.writeParcelable(deal_status_obj, flag);
		out.writeParcelable(deal_validation_obj, flag);
		out.writeParcelable(email_template_obj, flag);
		out.writeParcelable(facebook_data_friends_obj, flag);
		out.writeParcelable(facebook_data_hometown_obj, flag);
		out.writeParcelable(facebook_data_location_obj, flag);
		out.writeParcelable(facebook_data_profile_obj, flag);
		out.writeParcelable(fine_print_option_obj, flag);
		out.writeParcelable(industry_obj, flag);
		out.writeParcelable(login_log_obj, flag);
		out.writeParcelable(merchant_obj, flag);
		out.writeParcelable(merchant_contact_obj, flag);
		out.writeParcelable(merchant_contact_status_obj, flag);
		out.writeParcelable(merchant_contact_validation_obj, flag);
		out.writeParcelable(merchant_rating_obj, flag);
		out.writeParcelable(merchant_status_obj, flag);
		out.writeParcelable(neighborhood_obj, flag);
		out.writeParcelable(password_reset_obj, flag);
		out.writeParcelable(password_reset_status_obj, flag);
		out.writeParcelable(payment_log_obj, flag);
		out.writeParcelable(plivo_phone_number_obj, flag);
		out.writeParcelable(promotion_obj, flag);
		out.writeParcelable(promotion_activity_obj, flag);
		out.writeParcelable(promotion_type_obj, flag);
		out.writeParcelable(sms_template_obj, flag);
		out.writeParcelable(state_obj, flag);
		out.writeParcelable(system_error_obj, flag);
		out.writeParcelable(system_settings_obj, flag);
		out.writeParcelable(system_successful_obj, flag);
		out.writeParcelable(system_users_obj, flag);
		out.writeParcelable(time_zone_obj, flag);
		out.writeParcelable(twilio_phone_number_obj, flag);
		out.writeParcelable(zip_code_obj, flag);
	}

	private Merchant_Controller(Parcel in)
	{
		application_type_obj = in.readParcelable(Application_Type.class.getClassLoader());
		certificate_obj = in.readParcelable(Certificate.class.getClassLoader());
		certificate_log_obj = in.readParcelable(Certificate_Log.class.getClassLoader());
		certificate_payment_obj = in.readParcelable(Certificate_Payment.class.getClassLoader());
		certificate_status_obj = in.readParcelable(Certificate_Status.class.getClassLoader());
		city_obj = in.readParcelable(City.class.getClassLoader());
		contact_obj = in.readParcelable(Contact.class.getClassLoader());
		contact_gps_log_obj = in.readParcelable(Contact_GPS_Log.class.getClassLoader());
		credit_card_obj = in.readParcelable(Credit_Card.class.getClassLoader());
		credit_card_type_obj = in.readParcelable(Credit_Card_Type.class.getClassLoader());
		customer_obj = in.readParcelable(Customer.class.getClassLoader());
		customer_deal_log_obj = in.readParcelable(Customer_Deal_Log.class.getClassLoader());
		customer_search_location_type_obj = in.readParcelable(Customer_Search_Location_Type.class.getClassLoader());
		customer_status_obj = in.readParcelable(Customer_Status.class.getClassLoader());
		deal_obj = in.readParcelable(Deal.class.getClassLoader());
		deal_fine_print_option_obj = in.readParcelable(Deal_Fine_Print_Option.class.getClassLoader());
		deal_log_obj = in.readParcelable(Deal_Log.class.getClassLoader());
		deal_payment_obj = in.readParcelable(Deal_Payment.class.getClassLoader());
		deal_status_obj = in.readParcelable(Deal_Status.class.getClassLoader());
		deal_validation_obj = in.readParcelable(Deal_Validation.class.getClassLoader());
		email_template_obj = in.readParcelable(Email_Template.class.getClassLoader());
		facebook_data_friends_obj = in.readParcelable(Facebook_Data_Friends.class.getClassLoader());
		facebook_data_hometown_obj = in.readParcelable(Facebook_Data_Hometown.class.getClassLoader());
		facebook_data_location_obj = in.readParcelable(Facebook_Data_Location.class.getClassLoader());
		facebook_data_profile_obj = in.readParcelable(Facebook_Data_Profile.class.getClassLoader());
		fine_print_option_obj = in.readParcelable(Fine_Print_Option.class.getClassLoader());
		industry_obj = in.readParcelable(Industry.class.getClassLoader());
		login_log_obj = in.readParcelable(Login_Log.class.getClassLoader());
		merchant_obj = in.readParcelable(Merchant.class.getClassLoader());
		merchant_contact_obj = in.readParcelable(Merchant_Contact.class.getClassLoader());
		merchant_contact_status_obj = in.readParcelable(Merchant_Contact_Status.class.getClassLoader());
		merchant_contact_validation_obj = in.readParcelable(Merchant_Contact_Validation.class.getClassLoader());
		merchant_rating_obj = in.readParcelable(Merchant_Rating.class.getClassLoader());
		merchant_status_obj = in.readParcelable(Merchant_Status.class.getClassLoader());
		neighborhood_obj = in.readParcelable(Neighborhood.class.getClassLoader());
		password_reset_obj = in.readParcelable(Password_Reset.class.getClassLoader());
		password_reset_status_obj = in.readParcelable(Password_Reset_Status.class.getClassLoader());
		payment_log_obj = in.readParcelable(Payment_Log.class.getClassLoader());
		plivo_phone_number_obj = in.readParcelable(Plivo_Phone_Number.class.getClassLoader());
		promotion_obj = in.readParcelable(Promotion.class.getClassLoader());
		promotion_activity_obj = in.readParcelable(Promotion_Activity.class.getClassLoader());
		promotion_type_obj = in.readParcelable(Promotion_Type.class.getClassLoader());
		sms_template_obj = in.readParcelable(SMS_Template.class.getClassLoader());
		state_obj = in.readParcelable(State.class.getClassLoader());
		system_error_obj = in.readParcelable(System_Error.class.getClassLoader());
		system_settings_obj = in.readParcelable(System_Settings.class.getClassLoader());
		system_successful_obj = in.readParcelable(System_Successful.class.getClassLoader());
		system_users_obj = in.readParcelable(System_Users.class.getClassLoader());
		time_zone_obj = in.readParcelable(Time_Zone.class.getClassLoader());
		twilio_phone_number_obj = in.readParcelable(Twilio_Phone_Number.class.getClassLoader());
		zip_code_obj = in.readParcelable(Zip_Code.class.getClassLoader());
	}

	public static final Creator<Merchant_Controller> CREATOR = new Creator<Merchant_Controller>()
	{
		public Merchant_Controller createFromParcel(Parcel in) 
		{
			return new Merchant_Controller(in);
		}

		public Merchant_Controller[] newArray(int size) 
		{
			return new Merchant_Controller[size];
		}
	};

	public void Create_Validation()
	{
		system_error_obj = null;
		system_successful_obj = null;

		merchant_contact_obj = new Merchant_Contact();
		merchant_contact_obj.setLoginLogObj(login_log_obj);
		merchant_contact_obj.setContactObj(contact_obj);

		REST_MerchantService i_rest = new REST_MerchantService(merchant_service);
		i_rest.Create_Validation(merchant_contact_obj);

		JSONResponse json_response = i_rest.getJSONResponse();
		successful = json_response.getSuccessful();

		if (!successful)
		{
			system_error_obj = i_rest.getJSONErrorResponse().getSystemError();
		}
		else
		{
			system_successful_obj = i_rest.getJSONSuccessfulResponse().getSystemSuccessful();
		}
	}

	public void Validate()
	{
		system_error_obj = null;
		system_successful_obj = null;

		merchant_contact_obj = new Merchant_Contact();
		merchant_contact_obj.setLoginLogObj(login_log_obj);
		merchant_contact_obj.setContactObj(contact_obj);
		merchant_contact_obj.setMerchantContactValidationObj(merchant_contact_validation_obj);

		REST_MerchantService i_rest = new REST_MerchantService(merchant_service);
		i_rest.Validate(merchant_contact_obj);

		JSONResponse json_response = i_rest.getJSONResponse();
		successful = json_response.getSuccessful();

		if (!successful)
		{
			system_error_obj = i_rest.getJSONErrorResponse().getSystemError();
		}
		else
		{
			system_successful_obj = i_rest.getJSONSuccessfulResponse().getSystemSuccessful();
			merchant_contact_validation_obj = (Merchant_Contact_Validation)i_rest.getJSONSuccessfulResponse().getDataObj();
		}
	}

	public void Register()
	{
		system_error_obj = null;
		system_successful_obj = null;

		merchant_contact_obj.setLoginLogObj(login_log_obj);
		merchant_contact_obj.setContactObj(contact_obj);
		merchant_contact_obj.setMerchantObj(merchant_obj);
		merchant_contact_obj.setMerchantContactValidationObj(merchant_contact_validation_obj);

		REST_MerchantService i_rest = new REST_MerchantService(merchant_service);
		i_rest.Register(merchant_contact_obj);

		JSONResponse json_response = i_rest.getJSONResponse();
		successful = json_response.getSuccessful();

		if (!successful)
		{
			system_error_obj = i_rest.getJSONErrorResponse().getSystemError();
		}
		else
		{
			system_successful_obj = i_rest.getJSONSuccessfulResponse().getSystemSuccessful();
			merchant_contact_obj = (Merchant_Contact)i_rest.getJSONSuccessfulResponse().getDataObj();
		}
	}

	public void Login()
	{
		system_error_obj = null;
		system_successful_obj = null;

		merchant_contact_obj = new Merchant_Contact();
		merchant_contact_obj.setLoginLogObj(login_log_obj);
		merchant_contact_obj.setContactObj(contact_obj);

		String password = contact_obj.getPassword();

		REST_MerchantService i_rest = new REST_MerchantService(merchant_service);
		i_rest.Login(merchant_contact_obj);

		JSONResponse json_response = i_rest.getJSONResponse();
		successful = json_response.getSuccessful();

		if (!successful)
		{
			system_error_obj = i_rest.getJSONErrorResponse().getSystemError();
		}
		else
		{
			system_successful_obj = i_rest.getJSONSuccessfulResponse().getSystemSuccessful();

			merchant_contact_obj = (Merchant_Contact)i_rest.getJSONSuccessfulResponse().getDataObj();
			contact_obj = merchant_contact_obj.getContactObj();
			contact_obj.setPassword(password);
		}
	}

	public void Forgot_Password()
	{
		system_error_obj = null;
		system_successful_obj = null;

		merchant_contact_obj = new Merchant_Contact();
		merchant_contact_obj.setLoginLogObj(login_log_obj);
		merchant_contact_obj.setContactObj(contact_obj);

		REST_MerchantService i_rest = new REST_MerchantService(merchant_service);
		i_rest.Forgot_Password(merchant_contact_obj);

		JSONResponse json_response = i_rest.getJSONResponse();
		successful = json_response.getSuccessful();

		if (!successful)
		{
			system_error_obj = i_rest.getJSONErrorResponse().getSystemError();
		}
		else
		{
			system_successful_obj = i_rest.getJSONSuccessfulResponse().getSystemSuccessful();
		}
	}

	public void Get_Merchant_Contact()
	{
		system_error_obj = null;
		system_successful_obj = null;

		merchant_contact_obj.setLoginLogObj(login_log_obj);

		REST_MerchantService i_rest = new REST_MerchantService(merchant_service);
		i_rest.Get_Merchant_Contact(merchant_contact_obj);

		JSONResponse json_response = i_rest.getJSONResponse();
		successful = json_response.getSuccessful();

		if (!successful)
		{
			system_error_obj = i_rest.getJSONErrorResponse().getSystemError();
		}
		else
		{
			system_successful_obj = i_rest.getJSONSuccessfulResponse().getSystemSuccessful();
			merchant_contact_obj = (Merchant_Contact)i_rest.getJSONSuccessfulResponse().getDataObj();
		}
	}

	public void Update_Merchant_Contact()
	{
		system_error_obj = null;
		system_successful_obj = null;

		merchant_contact_obj.setLoginLogObj(login_log_obj);
		merchant_contact_obj.setContactObj(contact_obj);

		REST_MerchantService i_rest = new REST_MerchantService(merchant_service);
		i_rest.Update_Merchant_Contact(merchant_contact_obj);

		JSONResponse json_response = i_rest.getJSONResponse();
		successful = json_response.getSuccessful();

		if (!successful)
		{
			system_error_obj = i_rest.getJSONErrorResponse().getSystemError();
		}
		else
		{
			system_successful_obj = i_rest.getJSONSuccessfulResponse().getSystemSuccessful();
		}
	}

	public void Update_Merchant()
	{
		system_error_obj = null;
		system_successful_obj = null;

		merchant_contact_obj.setLoginLogObj(login_log_obj);
		merchant_contact_obj.setMerchantObj(merchant_obj);

		REST_MerchantService i_rest = new REST_MerchantService(merchant_service);
		i_rest.Update_Merchant(merchant_contact_obj);

		JSONResponse json_response = i_rest.getJSONResponse();
		successful = json_response.getSuccessful();

		if (!successful)
		{
			system_error_obj = i_rest.getJSONErrorResponse().getSystemError();
		}
		else
		{
			system_successful_obj = i_rest.getJSONSuccessfulResponse().getSystemSuccessful();
		}
	}

	public void Add_Credit_Card()
	{
		system_error_obj = null;
		system_successful_obj = null;

		credit_card_obj.setLoginLogObj(login_log_obj);
		credit_card_obj.setMerchantContactObj(merchant_contact_obj);

		REST_MerchantService i_rest = new REST_MerchantService(merchant_service);
		i_rest.Add_Credit_Card(credit_card_obj);

		JSONResponse json_response = i_rest.getJSONResponse();
		successful = json_response.getSuccessful();

		if (!successful)
		{
			system_error_obj = i_rest.getJSONErrorResponse().getSystemError();
		}
		else
		{
			system_successful_obj = i_rest.getJSONSuccessfulResponse().getSystemSuccessful();
			credit_card_obj = (Credit_Card)i_rest.getJSONSuccessfulResponse().getDataObj();
		}
	}

	public void Delete_Credit_Card()
	{
		system_error_obj = null;
		system_successful_obj = null;

		credit_card_obj.setLoginLogObj(login_log_obj);
		credit_card_obj.setMerchantContactObj(merchant_contact_obj);

		REST_MerchantService i_rest = new REST_MerchantService(merchant_service);
		i_rest.Delete_Credit_Card(credit_card_obj);

		JSONResponse json_response = i_rest.getJSONResponse();
		successful = json_response.getSuccessful();

		if (!successful)
		{
			system_error_obj = i_rest.getJSONErrorResponse().getSystemError();
		}
		else
		{
			system_successful_obj = i_rest.getJSONSuccessfulResponse().getSystemSuccessful();
		}
	}

	public void Get_Credit_Cards()
	{
		system_error_obj = null;
		system_successful_obj = null;

		merchant_contact_obj.setLoginLogObj(login_log_obj);

		REST_MerchantService i_rest = new REST_MerchantService(merchant_service);
		i_rest.Get_Credit_Cards(merchant_contact_obj);

		JSONResponse json_response = i_rest.getJSONResponse();
		successful = json_response.getSuccessful();

		if (!successful)
		{
			system_error_obj = i_rest.getJSONErrorResponse().getSystemError();
		}
		else
		{
			system_successful_obj = i_rest.getJSONSuccessfulResponse().getSystemSuccessful();
			credit_card_obj_array = (Credit_Card[])i_rest.getJSONSuccessfulResponse().getDataObj();
		}
	}

	public void Get_Default_Credit_Card()
	{
		system_error_obj = null;
		system_successful_obj = null;

		merchant_contact_obj = new Merchant_Contact();

		REST_MerchantService i_rest = new REST_MerchantService(merchant_service);
		i_rest.Get_Default_Credit_Card(merchant_contact_obj);

		JSONResponse json_response = i_rest.getJSONResponse();
		successful = json_response.getSuccessful();

		if (!successful)
		{
			system_error_obj = i_rest.getJSONErrorResponse().getSystemError();
		}
		else
		{
			system_successful_obj = i_rest.getJSONSuccessfulResponse().getSystemSuccessful();
			credit_card_obj = (Credit_Card)i_rest.getJSONSuccessfulResponse().getDataObj();
		}
	}

	public void Set_Default_Credit_Card()
	{
		system_error_obj = null;
		system_successful_obj = null;

		credit_card_obj.setLoginLogObj(login_log_obj);
		credit_card_obj.setMerchantContactObj(merchant_contact_obj);

		REST_MerchantService i_rest = new REST_MerchantService(merchant_service);
		i_rest.Set_Default_Credit_Card(credit_card_obj);

		JSONResponse json_response = i_rest.getJSONResponse();
		successful = json_response.getSuccessful();

		if (!successful)
		{
			system_error_obj = i_rest.getJSONErrorResponse().getSystemError();
		}
	}



	public Application_Type getApplicationTypeObj()
	{
		return this.application_type_obj;
	}

	public void setApplicationTypeObj(Application_Type application_type_obj)
	{
		this.application_type_obj = application_type_obj;
	}

	public Application_Type[] getApplicationTypeObjArray()
	{
		return this.application_type_obj_array;
	}

	public Certificate getCertificateObj()
	{
		return this.certificate_obj;
	}

	public void setCertificateObj(Certificate certificate_obj)
	{
		this.certificate_obj = certificate_obj;
	}

	public Certificate[] getCertificateObjArray()
	{
		return this.certificate_obj_array;
	}

	public Certificate_Log getCertificateLogObj()
	{
		return this.certificate_log_obj;
	}

	public void setCertificateLogObj(Certificate_Log certificate_log_obj)
	{
		this.certificate_log_obj = certificate_log_obj;
	}

	public Certificate_Log[] getCertificateLogObjArray()
	{
		return this.certificate_log_obj_array;
	}

	public Certificate_Payment getCertificatePaymentObj()
	{
		return this.certificate_payment_obj;
	}

	public void setCertificatePaymentObj(Certificate_Payment certificate_payment_obj)
	{
		this.certificate_payment_obj = certificate_payment_obj;
	}

	public Certificate_Payment[] getCertificatePaymentObjArray()
	{
		return this.certificate_payment_obj_array;
	}

	public Certificate_Status getCertificateStatusObj()
	{
		return this.certificate_status_obj;
	}

	public void setCertificateStatusObj(Certificate_Status certificate_status_obj)
	{
		this.certificate_status_obj = certificate_status_obj;
	}

	public Certificate_Status[] getCertificateStatusObjArray()
	{
		return this.certificate_status_obj_array;
	}

	public City getCityObj()
	{
		return this.city_obj;
	}

	public void setCityObj(City city_obj)
	{
		this.city_obj = city_obj;
	}

	public City[] getCityObjArray()
	{
		return this.city_obj_array;
	}

	public Contact getContactObj()
	{
		return this.contact_obj;
	}

	public void setContactObj(Contact contact_obj)
	{
		this.contact_obj = contact_obj;
	}

	public Contact[] getContactObjArray()
	{
		return this.contact_obj_array;
	}

	public Contact_GPS_Log getContactGPSLogObj()
	{
		return this.contact_gps_log_obj;
	}

	public void setContactGPSLogObj(Contact_GPS_Log contact_gps_log_obj)
	{
		this.contact_gps_log_obj = contact_gps_log_obj;
	}

	public Contact_GPS_Log[] getContactGPSLogObjArray()
	{
		return this.contact_gps_log_obj_array;
	}

	public Credit_Card getCreditCardObj()
	{
		return this.credit_card_obj;
	}

	public void setCreditCardObj(Credit_Card credit_card_obj)
	{
		this.credit_card_obj = credit_card_obj;
	}

	public Credit_Card[] getCreditCardObjArray()
	{
		return this.credit_card_obj_array;
	}

	public Credit_Card_Type getCreditCardTypeObj()
	{
		return this.credit_card_type_obj;
	}

	public void setCreditCardTypeObj(Credit_Card_Type credit_card_type_obj)
	{
		this.credit_card_type_obj = credit_card_type_obj;
	}

	public Credit_Card_Type[] getCreditCardTypeObjArray()
	{
		return this.credit_card_type_obj_array;
	}

	public Customer getCustomerObj()
	{
		return this.customer_obj;
	}

	public void setCustomerObj(Customer customer_obj)
	{
		this.customer_obj = customer_obj;
	}

	public Customer[] getCustomerObjArray()
	{
		return this.customer_obj_array;
	}

	public Customer_Deal_Log getCustomerDealLogObj()
	{
		return this.customer_deal_log_obj;
	}

	public void setCustomerDealLogObj(Customer_Deal_Log customer_deal_log_obj)
	{
		this.customer_deal_log_obj = customer_deal_log_obj;
	}

	public Customer_Deal_Log[] getCustomerDealLogObjArray()
	{
		return this.customer_deal_log_obj_array;
	}

	public Customer_Search_Location_Type getCustomerSearchLocationTypeObj()
	{
		return this.customer_search_location_type_obj;
	}

	public void setCustomerSearchLocationTypeObj(Customer_Search_Location_Type customer_search_location_type_obj)
	{
		this.customer_search_location_type_obj = customer_search_location_type_obj;
	}

	public Customer_Search_Location_Type[] getCustomerSearchLocationTypeObjArray()
	{
		return this.customer_search_location_type_obj_array;
	}

	public Customer_Status getCustomerStatusObj()
	{
		return this.customer_status_obj;
	}

	public void setCustomerStatusObj(Customer_Status customer_status_obj)
	{
		this.customer_status_obj = customer_status_obj;
	}

	public Customer_Status[] getCustomerStatusObjArray()
	{
		return this.customer_status_obj_array;
	}

	public Deal getDealObj()
	{
		return this.deal_obj;
	}

	public void setDealObj(Deal deal_obj)
	{
		this.deal_obj = deal_obj;
	}

	public Deal[] getDealObjArray()
	{
		return this.deal_obj_array;
	}

	public Deal_Fine_Print_Option getDealFinePrintOptionObj()
	{
		return this.deal_fine_print_option_obj;
	}

	public void setDealFinePrintOptionObj(Deal_Fine_Print_Option deal_fine_print_option_obj)
	{
		this.deal_fine_print_option_obj = deal_fine_print_option_obj;
	}

	public Deal_Fine_Print_Option[] getDealFinePrintOptionObjArray()
	{
		return this.deal_fine_print_option_obj_array;
	}

	public Deal_Log getDealLogObj()
	{
		return this.deal_log_obj;
	}

	public void setDealLogObj(Deal_Log deal_log_obj)
	{
		this.deal_log_obj = deal_log_obj;
	}

	public Deal_Log[] getDealLogObjArray()
	{
		return this.deal_log_obj_array;
	}

	public Deal_Payment getDealPaymentObj()
	{
		return this.deal_payment_obj;
	}

	public void setDealPaymentObj(Deal_Payment deal_payment_obj)
	{
		this.deal_payment_obj = deal_payment_obj;
	}

	public Deal_Payment[] getDealPaymentObjArray()
	{
		return this.deal_payment_obj_array;
	}

	public Deal_Status getDealStatusObj()
	{
		return this.deal_status_obj;
	}

	public void setDealStatusObj(Deal_Status deal_status_obj)
	{
		this.deal_status_obj = deal_status_obj;
	}

	public Deal_Status[] getDealStatusObjArray()
	{
		return this.deal_status_obj_array;
	}

	public Deal_Validation getDealValidationObj()
	{
		return this.deal_validation_obj;
	}

	public void setDealValidationObj(Deal_Validation deal_validation_obj)
	{
		this.deal_validation_obj = deal_validation_obj;
	}

	public Deal_Validation[] getDealValidationObjArray()
	{
		return this.deal_validation_obj_array;
	}

	public Email_Template getEmailTemplateObj()
	{
		return this.email_template_obj;
	}

	public void setEmailTemplateObj(Email_Template email_template_obj)
	{
		this.email_template_obj = email_template_obj;
	}

	public Email_Template[] getEmailTemplateObjArray()
	{
		return this.email_template_obj_array;
	}

	public Facebook_Data_Friends getFacebookDataFriendsObj()
	{
		return this.facebook_data_friends_obj;
	}

	public void setFacebookDataFriendsObj(Facebook_Data_Friends facebook_data_friends_obj)
	{
		this.facebook_data_friends_obj = facebook_data_friends_obj;
	}

	public Facebook_Data_Friends[] getFacebookDataFriendsObjArray()
	{
		return this.facebook_data_friends_obj_array;
	}

	public Facebook_Data_Hometown getFacebookDataHometownObj()
	{
		return this.facebook_data_hometown_obj;
	}

	public void setFacebookDataHometownObj(Facebook_Data_Hometown facebook_data_hometown_obj)
	{
		this.facebook_data_hometown_obj = facebook_data_hometown_obj;
	}

	public Facebook_Data_Hometown[] getFacebookDataHometownObjArray()
	{
		return this.facebook_data_hometown_obj_array;
	}

	public Facebook_Data_Location getFacebookDataLocationObj()
	{
		return this.facebook_data_location_obj;
	}

	public void setFacebookDataLocationObj(Facebook_Data_Location facebook_data_location_obj)
	{
		this.facebook_data_location_obj = facebook_data_location_obj;
	}

	public Facebook_Data_Location[] getFacebookDataLocationObjArray()
	{
		return this.facebook_data_location_obj_array;
	}

	public Facebook_Data_Profile getFacebookDataProfileObj()
	{
		return this.facebook_data_profile_obj;
	}

	public void setFacebookDataProfileObj(Facebook_Data_Profile facebook_data_profile_obj)
	{
		this.facebook_data_profile_obj = facebook_data_profile_obj;
	}

	public Facebook_Data_Profile[] getFacebookDataProfileObjArray()
	{
		return this.facebook_data_profile_obj_array;
	}

	public Fine_Print_Option getFinePrintOptionObj()
	{
		return this.fine_print_option_obj;
	}

	public void setFinePrintOptionObj(Fine_Print_Option fine_print_option_obj)
	{
		this.fine_print_option_obj = fine_print_option_obj;
	}

	public Fine_Print_Option[] getFinePrintOptionObjArray()
	{
		return this.fine_print_option_obj_array;
	}

	public Industry getIndustryObj()
	{
		return this.industry_obj;
	}

	public void setIndustryObj(Industry industry_obj)
	{
		this.industry_obj = industry_obj;
	}

	public Industry[] getIndustryObjArray()
	{
		return this.industry_obj_array;
	}

	public Login_Log getLoginLogObj()
	{
		return this.login_log_obj;
	}

	public void setLoginLogObj(Login_Log login_log_obj)
	{
		this.login_log_obj = login_log_obj;
	}

	public Login_Log[] getLoginLogObjArray()
	{
		return this.login_log_obj_array;
	}

	public Merchant getMerchantObj()
	{
		return this.merchant_obj;
	}

	public void setMerchantObj(Merchant merchant_obj)
	{
		this.merchant_obj = merchant_obj;
	}

	public Merchant[] getMerchantObjArray()
	{
		return this.merchant_obj_array;
	}

	public Merchant_Contact getMerchantContactObj()
	{
		return this.merchant_contact_obj;
	}

	public void setMerchantContactObj(Merchant_Contact merchant_contact_obj)
	{
		this.merchant_contact_obj = merchant_contact_obj;
	}

	public Merchant_Contact[] getMerchantContactObjArray()
	{
		return this.merchant_contact_obj_array;
	}

	public Merchant_Contact_Status getMerchantContactStatusObj()
	{
		return this.merchant_contact_status_obj;
	}

	public void setMerchantContactStatusObj(Merchant_Contact_Status merchant_contact_status_obj)
	{
		this.merchant_contact_status_obj = merchant_contact_status_obj;
	}

	public Merchant_Contact_Status[] getMerchantContactStatusObjArray()
	{
		return this.merchant_contact_status_obj_array;
	}

	public Merchant_Contact_Validation getMerchantContactValidationObj()
	{
		return this.merchant_contact_validation_obj;
	}

	public void setMerchantContactValidationObj(Merchant_Contact_Validation merchant_contact_validation_obj)
	{
		this.merchant_contact_validation_obj = merchant_contact_validation_obj;
	}

	public Merchant_Contact_Validation[] getMerchantContactValidationObjArray()
	{
		return this.merchant_contact_validation_obj_array;
	}

	public Merchant_Rating getMerchantRatingObj()
	{
		return this.merchant_rating_obj;
	}

	public void setMerchantRatingObj(Merchant_Rating merchant_rating_obj)
	{
		this.merchant_rating_obj = merchant_rating_obj;
	}

	public Merchant_Rating[] getMerchantRatingObjArray()
	{
		return this.merchant_rating_obj_array;
	}

	public Merchant_Status getMerchantStatusObj()
	{
		return this.merchant_status_obj;
	}

	public void setMerchantStatusObj(Merchant_Status merchant_status_obj)
	{
		this.merchant_status_obj = merchant_status_obj;
	}

	public Merchant_Status[] getMerchantStatusObjArray()
	{
		return this.merchant_status_obj_array;
	}

	public Neighborhood getNeighborhoodObj()
	{
		return this.neighborhood_obj;
	}

	public void setNeighborhoodObj(Neighborhood neighborhood_obj)
	{
		this.neighborhood_obj = neighborhood_obj;
	}

	public Neighborhood[] getNeighborhoodObjArray()
	{
		return this.neighborhood_obj_array;
	}

	public Password_Reset getPasswordResetObj()
	{
		return this.password_reset_obj;
	}

	public void setPasswordResetObj(Password_Reset password_reset_obj)
	{
		this.password_reset_obj = password_reset_obj;
	}

	public Password_Reset[] getPasswordResetObjArray()
	{
		return this.password_reset_obj_array;
	}

	public Password_Reset_Status getPasswordResetStatusObj()
	{
		return this.password_reset_status_obj;
	}

	public void setPasswordResetStatusObj(Password_Reset_Status password_reset_status_obj)
	{
		this.password_reset_status_obj = password_reset_status_obj;
	}

	public Password_Reset_Status[] getPasswordResetStatusObjArray()
	{
		return this.password_reset_status_obj_array;
	}

	public Payment_Log getPaymentLogObj()
	{
		return this.payment_log_obj;
	}

	public void setPaymentLogObj(Payment_Log payment_log_obj)
	{
		this.payment_log_obj = payment_log_obj;
	}

	public Payment_Log[] getPaymentLogObjArray()
	{
		return this.payment_log_obj_array;
	}

	public Plivo_Phone_Number getPlivoPhoneNumberObj()
	{
		return this.plivo_phone_number_obj;
	}

	public void setPlivoPhoneNumberObj(Plivo_Phone_Number plivo_phone_number_obj)
	{
		this.plivo_phone_number_obj = plivo_phone_number_obj;
	}

	public Plivo_Phone_Number[] getPlivoPhoneNumberObjArray()
	{
		return this.plivo_phone_number_obj_array;
	}

	public Promotion getPromotionObj()
	{
		return this.promotion_obj;
	}

	public void setPromotionObj(Promotion promotion_obj)
	{
		this.promotion_obj = promotion_obj;
	}

	public Promotion[] getPromotionObjArray()
	{
		return this.promotion_obj_array;
	}

	public Promotion_Activity getPromotionActivityObj()
	{
		return this.promotion_activity_obj;
	}

	public void setPromotionActivityObj(Promotion_Activity promotion_activity_obj)
	{
		this.promotion_activity_obj = promotion_activity_obj;
	}

	public Promotion_Activity[] getPromotionActivityObjArray()
	{
		return this.promotion_activity_obj_array;
	}

	public Promotion_Type getPromotionTypeObj()
	{
		return this.promotion_type_obj;
	}

	public void setPromotionTypeObj(Promotion_Type promotion_type_obj)
	{
		this.promotion_type_obj = promotion_type_obj;
	}

	public Promotion_Type[] getPromotionTypeObjArray()
	{
		return this.promotion_type_obj_array;
	}

	public SMS_Template getSMSTemplateObj()
	{
		return this.sms_template_obj;
	}

	public void setSMSTemplateObj(SMS_Template sms_template_obj)
	{
		this.sms_template_obj = sms_template_obj;
	}

	public SMS_Template[] getSMSTemplateObjArray()
	{
		return this.sms_template_obj_array;
	}

	public State getStateObj()
	{
		return this.state_obj;
	}

	public void setStateObj(State state_obj)
	{
		this.state_obj = state_obj;
	}

	public State[] getStateObjArray()
	{
		return this.state_obj_array;
	}

	public System_Error getSystemErrorObj()
	{
		return this.system_error_obj;
	}

	public void setSystemErrorObj(System_Error system_error_obj)
	{
		this.system_error_obj = system_error_obj;
	}

	public System_Error[] getSystemErrorObjArray()
	{
		return this.system_error_obj_array;
	}

	public System_Settings getSystemSettingsObj()
	{
		return this.system_settings_obj;
	}

	public void setSystemSettingsObj(System_Settings system_settings_obj)
	{
		this.system_settings_obj = system_settings_obj;
	}

	public System_Settings[] getSystemSettingsObjArray()
	{
		return this.system_settings_obj_array;
	}

	public System_Successful getSystemSuccessfulObj()
	{
		return this.system_successful_obj;
	}

	public void setSystemSuccessfulObj(System_Successful system_successful_obj)
	{
		this.system_successful_obj = system_successful_obj;
	}

	public System_Successful[] getSystemSuccessfulObjArray()
	{
		return this.system_successful_obj_array;
	}

	public System_Users getSystemUsersObj()
	{
		return this.system_users_obj;
	}

	public void setSystemUsersObj(System_Users system_users_obj)
	{
		this.system_users_obj = system_users_obj;
	}

	public System_Users[] getSystemUsersObjArray()
	{
		return this.system_users_obj_array;
	}

	public Time_Zone getTimeZoneObj()
	{
		return this.time_zone_obj;
	}

	public void setTimeZoneObj(Time_Zone time_zone_obj)
	{
		this.time_zone_obj = time_zone_obj;
	}

	public Time_Zone[] getTimeZoneObjArray()
	{
		return this.time_zone_obj_array;
	}

	public Twilio_Phone_Number getTwilioPhoneNumberObj()
	{
		return this.twilio_phone_number_obj;
	}

	public void setTwilioPhoneNumberObj(Twilio_Phone_Number twilio_phone_number_obj)
	{
		this.twilio_phone_number_obj = twilio_phone_number_obj;
	}

	public Twilio_Phone_Number[] getTwilioPhoneNumberObjArray()
	{
		return this.twilio_phone_number_obj_array;
	}

	public Zip_Code getZipCodeObj()
	{
		return this.zip_code_obj;
	}

	public void setZipCodeObj(Zip_Code zip_code_obj)
	{
		this.zip_code_obj = zip_code_obj;
	}

	public Zip_Code[] getZipCodeObjArray()
	{
		return this.zip_code_obj_array;
	}


}
