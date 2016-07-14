package com.gtsoft.meddyl.merchant.controller.rest_interface;

import com.google.gson.*;
import com.gtsoft.meddyl.merchant.model.object.*;
import com.gtsoft.meddyl.merchant.model.response.*;
import com.gtsoft.meddyl.merchant.system.gtsoft.DotNetDateDeserializer;
import com.gtsoft.meddyl.merchant.system.gtsoft.ServiceHandler;
import java.util.Date;


public class REST_MerchantService
{
	private String web_service;
	private Gson gson;

	private JSONResponse json_response;
	private JSONSuccessfulResponse json_successful_response;
	private JSONErrorResponse json_error_response;


	public REST_MerchantService(String _web_service)
	{
		web_service = _web_service;

		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Date.class, new DotNetDateDeserializer());
		gson = builder.create();
	}

	public void Get_Application_Settings(Login_Log login_log_obj)
	{
		String uri = "system/application_settings";

		JsonElement je = gson.toJsonTree(login_log_obj);
		JsonObject jo = new JsonObject();
		jo.add("login_log_obj", je);
		String json_request_string = jo.toString();

		ServiceHandler sh = new ServiceHandler();
		String json_response_string = sh.makeServiceCall(web_service + uri, ServiceHandler.POST, json_request_string);

		if(json_response_string == null)
		{
			No_Internet_Connection();
		}
		else
		{
			JsonObject jsonObject = gson.fromJson(json_response_string, JsonObject.class);
			json_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONResponse.class);

			if(json_response.getSuccessful())
			{
				json_successful_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONSuccessfulResponse.class);
				json_successful_response.setDataObj(gson.fromJson(((JsonObject)jsonObject.get("JSONResponse")).get("data_obj"), Application_Type.class));
			}
			else
			{
				json_error_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONErrorResponse.class);
			}
		}
	}

	public void Get_System_Settings(System_Settings system_settings_obj)
	{
		String uri = "system/system_settings";

		JsonElement je = gson.toJsonTree(system_settings_obj);
		JsonObject jo = new JsonObject();
		jo.add("system_settings_obj", je);
		String json_request_string = jo.toString();

		ServiceHandler sh = new ServiceHandler();
		String json_response_string = sh.makeServiceCall(web_service + uri, ServiceHandler.POST, json_request_string);

		if(json_response_string == null)
		{
			No_Internet_Connection();
		}
		else
		{
			JsonObject jsonObject = gson.fromJson(json_response_string, JsonObject.class);
			json_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONResponse.class);

			if(json_response.getSuccessful())
			{
				json_successful_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONSuccessfulResponse.class);
				json_successful_response.setDataObj(gson.fromJson(((JsonObject)jsonObject.get("JSONResponse")).get("data_obj"), System_Settings.class));
			}
			else
			{
				json_error_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONErrorResponse.class);
			}
		}
	}

	public void Get_Industry_Parent_Level(Industry industry_obj)
	{
		String uri = "system/industry/parent";

		JsonElement je = gson.toJsonTree(industry_obj);
		JsonObject jo = new JsonObject();
		jo.add("industry_obj", je);
		String json_request_string = jo.toString();

		ServiceHandler sh = new ServiceHandler();
		String json_response_string = sh.makeServiceCall(web_service + uri, ServiceHandler.POST, json_request_string);

		if(json_response_string == null)
		{
			No_Internet_Connection();
		}
		else
		{
			JsonObject jsonObject = gson.fromJson(json_response_string, JsonObject.class);
			json_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONResponse.class);

			if(json_response.getSuccessful())
			{
				json_successful_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONSuccessfulResponse.class);
				json_successful_response.setDataObj(gson.fromJson(((JsonObject)jsonObject.get("JSONResponse")).get("data_obj"), Industry[].class));
			}
			else
			{
				json_error_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONErrorResponse.class);
			}
		}
	}

	public void Get_Neighborhood_By_Zip(Zip_Code zip_code_obj)
	{
		String uri = "system/neighborhood";

		JsonElement je = gson.toJsonTree(zip_code_obj);
		JsonObject jo = new JsonObject();
		jo.add("zip_code_obj", je);
		String json_request_string = jo.toString();

		ServiceHandler sh = new ServiceHandler();
		String json_response_string = sh.makeServiceCall(web_service + uri, ServiceHandler.POST, json_request_string);

		if(json_response_string == null)
		{
			No_Internet_Connection();
		}
		else
		{
			JsonObject jsonObject = gson.fromJson(json_response_string, JsonObject.class);
			json_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONResponse.class);

			if(json_response.getSuccessful())
			{
				json_successful_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONSuccessfulResponse.class);
				json_successful_response.setDataObj(gson.fromJson(((JsonObject)jsonObject.get("JSONResponse")).get("data_obj"), Zip_Code.class));
			}
			else
			{
				json_error_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONErrorResponse.class);
			}
		}
	}

	public void Get_Fine_Print_Options(Login_Log login_log_obj)
	{
		String uri = "system/fine_print_options";

		JsonElement je = gson.toJsonTree(login_log_obj);
		JsonObject jo = new JsonObject();
		jo.add("login_log_obj", je);
		String json_request_string = jo.toString();

		ServiceHandler sh = new ServiceHandler();
		String json_response_string = sh.makeServiceCall(web_service + uri, ServiceHandler.POST, json_request_string);

		if(json_response_string == null)
		{
			No_Internet_Connection();
		}
		else
		{
			JsonObject jsonObject = gson.fromJson(json_response_string, JsonObject.class);
			json_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONResponse.class);

			if(json_response.getSuccessful())
			{
				json_successful_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONSuccessfulResponse.class);
				json_successful_response.setDataObj(gson.fromJson(((JsonObject)jsonObject.get("JSONResponse")).get("data_obj"), Fine_Print_Option[].class));
			}
			else
			{
				json_error_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONErrorResponse.class);
			}
		}
	}

	public void Create_Validation(Merchant_Contact merchant_contact_obj)
	{
		String uri = "merchant/merchant_contact/create_validation";

		JsonElement je = gson.toJsonTree(merchant_contact_obj);
		JsonObject jo = new JsonObject();
		jo.add("merchant_contact_obj", je);
		String json_request_string = jo.toString();

		ServiceHandler sh = new ServiceHandler();
		String json_response_string = sh.makeServiceCall(web_service + uri, ServiceHandler.POST, json_request_string);

		if(json_response_string == null)
		{
			No_Internet_Connection();
		}
		else
		{
			JsonObject jsonObject = gson.fromJson(json_response_string, JsonObject.class);
			json_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONResponse.class);

			if(json_response.getSuccessful())
			{
				json_successful_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONSuccessfulResponse.class);
			}
			else
			{
				json_error_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONErrorResponse.class);
			}
		}
	}

	public void Validate(Merchant_Contact merchant_contact_obj)
	{
		String uri = "merchant/merchant_contact/validate";

		JsonElement je = gson.toJsonTree(merchant_contact_obj);
		JsonObject jo = new JsonObject();
		jo.add("merchant_contact_obj", je);
		String json_request_string = jo.toString();

		ServiceHandler sh = new ServiceHandler();
		String json_response_string = sh.makeServiceCall(web_service + uri, ServiceHandler.POST, json_request_string);

		if(json_response_string == null)
		{
			No_Internet_Connection();
		}
		else
		{
			JsonObject jsonObject = gson.fromJson(json_response_string, JsonObject.class);
			json_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONResponse.class);

			if(json_response.getSuccessful())
			{
				json_successful_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONSuccessfulResponse.class);
				json_successful_response.setDataObj(gson.fromJson(((JsonObject)jsonObject.get("JSONResponse")).get("data_obj"), Merchant_Contact_Validation.class));
			}
			else
			{
				json_error_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONErrorResponse.class);
			}
		}
	}

	public void Register(Merchant_Contact merchant_contact_obj)
	{
		String uri = "merchant/merchant_contact/register";

		JsonElement je = gson.toJsonTree(merchant_contact_obj);
		JsonObject jo = new JsonObject();
		jo.add("merchant_contact_obj", je);
		String json_request_string = jo.toString();

		ServiceHandler sh = new ServiceHandler();
		String json_response_string = sh.makeServiceCall(web_service + uri, ServiceHandler.POST, json_request_string);

		if(json_response_string == null)
		{
			No_Internet_Connection();
		}
		else
		{
			JsonObject jsonObject = gson.fromJson(json_response_string, JsonObject.class);
			json_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONResponse.class);

			if(json_response.getSuccessful())
			{
				json_successful_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONSuccessfulResponse.class);
				json_successful_response.setDataObj(gson.fromJson(((JsonObject)jsonObject.get("JSONResponse")).get("data_obj"), Merchant_Contact.class));
			}
			else
			{
				json_error_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONErrorResponse.class);
			}
		}
	}

	public void Login(Merchant_Contact merchant_contact_obj)
	{
		String uri = "merchant/merchant_contact/login";

		JsonElement je = gson.toJsonTree(merchant_contact_obj);
		JsonObject jo = new JsonObject();
		jo.add("merchant_contact_obj", je);
		String json_request_string = jo.toString();

		ServiceHandler sh = new ServiceHandler();
		String json_response_string = sh.makeServiceCall(web_service + uri, ServiceHandler.POST, json_request_string);

		if(json_response_string == null)
		{
			No_Internet_Connection();
		}
		else
		{
			JsonObject jsonObject = gson.fromJson(json_response_string, JsonObject.class);
			json_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONResponse.class);

			if(json_response.getSuccessful())
			{
				json_successful_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONSuccessfulResponse.class);
				json_successful_response.setDataObj(gson.fromJson(((JsonObject)jsonObject.get("JSONResponse")).get("data_obj"), Merchant_Contact.class));
			}
			else
			{
				json_error_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONErrorResponse.class);
			}
		}
	}

	public void Forgot_Password(Merchant_Contact merchant_contact_obj)
	{
		String uri = "merchant/merchant_contact/forgot_password";

		JsonElement je = gson.toJsonTree(merchant_contact_obj);
		JsonObject jo = new JsonObject();
		jo.add("merchant_contact_obj", je);
		String json_request_string = jo.toString();

		ServiceHandler sh = new ServiceHandler();
		String json_response_string = sh.makeServiceCall(web_service + uri, ServiceHandler.POST, json_request_string);

		if(json_response_string == null)
		{
			No_Internet_Connection();
		}
		else
		{
			JsonObject jsonObject = gson.fromJson(json_response_string, JsonObject.class);
			json_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONResponse.class);

			if(json_response.getSuccessful())
			{
				json_successful_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONSuccessfulResponse.class);
			}
			else
			{
				json_error_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONErrorResponse.class);
			}
		}
	}

	public void Get_Merchant_Contact(Merchant_Contact merchant_contact_obj)
	{
		String uri = "merchant/merchant_contact/details";

		JsonElement je = gson.toJsonTree(merchant_contact_obj);
		JsonObject jo = new JsonObject();
		jo.add("merchant_contact_obj", je);
		String json_request_string = jo.toString();

		ServiceHandler sh = new ServiceHandler();
		String json_response_string = sh.makeServiceCall(web_service + uri, ServiceHandler.POST, json_request_string);

		if(json_response_string == null)
		{
			No_Internet_Connection();
		}
		else
		{
			JsonObject jsonObject = gson.fromJson(json_response_string, JsonObject.class);
			json_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONResponse.class);

			if(json_response.getSuccessful())
			{
				json_successful_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONSuccessfulResponse.class);
				json_successful_response.setDataObj(gson.fromJson(((JsonObject)jsonObject.get("JSONResponse")).get("data_obj"), Merchant_Contact.class));
			}
			else
			{
				json_error_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONErrorResponse.class);
			}
		}
	}

	public void Update_Merchant(Merchant_Contact merchant_contact_obj)
	{
		String uri = "merchant/update";

		JsonElement je = gson.toJsonTree(merchant_contact_obj);
		JsonObject jo = new JsonObject();
		jo.add("merchant_contact_obj", je);
		String json_request_string = jo.toString();

		ServiceHandler sh = new ServiceHandler();
		String json_response_string = sh.makeServiceCall(web_service + uri, ServiceHandler.POST, json_request_string);

		if(json_response_string == null)
		{
			No_Internet_Connection();
		}
		else
		{
			JsonObject jsonObject = gson.fromJson(json_response_string, JsonObject.class);
			json_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONResponse.class);

			if(json_response.getSuccessful())
			{
				json_successful_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONSuccessfulResponse.class);
			}
			else
			{
				json_error_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONErrorResponse.class);
			}
		}
	}

	public void Update_Merchant_Contact(Merchant_Contact merchant_contact_obj)
	{
		String uri = "merchant/merchant_contact/update";

		JsonElement je = gson.toJsonTree(merchant_contact_obj);
		JsonObject jo = new JsonObject();
		jo.add("merchant_contact_obj", je);
		String json_request_string = jo.toString();

		ServiceHandler sh = new ServiceHandler();
		String json_response_string = sh.makeServiceCall(web_service + uri, ServiceHandler.POST, json_request_string);

		if(json_response_string == null)
		{
			No_Internet_Connection();
		}
		else
		{
			JsonObject jsonObject = gson.fromJson(json_response_string, JsonObject.class);
			json_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONResponse.class);

			if(json_response.getSuccessful())
			{
				json_successful_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONSuccessfulResponse.class);
			}
			else
			{
				json_error_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONErrorResponse.class);
			}
		}
	}

	public void Add_Credit_Card(Credit_Card credit_card_obj)
	{
		String uri = "merchant/merchant_contact/credit_card/add";

		JsonElement je = gson.toJsonTree(credit_card_obj);
		JsonObject jo = new JsonObject();
		jo.add("credit_card_obj", je);
		String json_request_string = jo.toString();

		ServiceHandler sh = new ServiceHandler();
		String json_response_string = sh.makeServiceCall(web_service + uri, ServiceHandler.POST, json_request_string);

		if(json_response_string == null)
		{
			No_Internet_Connection();
		}
		else
		{
			JsonObject jsonObject = gson.fromJson(json_response_string, JsonObject.class);
			json_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONResponse.class);

			if(json_response.getSuccessful())
			{
				json_successful_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONSuccessfulResponse.class);
				json_successful_response.setDataObj(gson.fromJson(((JsonObject)jsonObject.get("JSONResponse")).get("data_obj"), Credit_Card.class));
			}
			else
			{
				json_error_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONErrorResponse.class);
			}
		}
	}

	public void Delete_Credit_Card(Credit_Card credit_card_obj)
	{
		String uri = "merchant/merchant_contact/credit_card/delete";

		JsonElement je = gson.toJsonTree(credit_card_obj);
		JsonObject jo = new JsonObject();
		jo.add("credit_card_obj", je);
		String json_request_string = jo.toString();

		ServiceHandler sh = new ServiceHandler();
		String json_response_string = sh.makeServiceCall(web_service + uri, ServiceHandler.POST, json_request_string);

		if(json_response_string == null)
		{
			No_Internet_Connection();
		}
		else
		{
			JsonObject jsonObject = gson.fromJson(json_response_string, JsonObject.class);
			json_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONResponse.class);

			if(json_response.getSuccessful())
			{
				json_successful_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONSuccessfulResponse.class);
			}
			else
			{
				json_error_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONErrorResponse.class);
			}
		}
	}

	public void Set_Default_Credit_Card(Credit_Card credit_card_obj)
	{
		String uri = "merchant/merchant_contact/credit_card/set_default";

		JsonElement je = gson.toJsonTree(credit_card_obj);
		JsonObject jo = new JsonObject();
		jo.add("credit_card_obj", je);
		String json_request_string = jo.toString();

		ServiceHandler sh = new ServiceHandler();
		String json_response_string = sh.makeServiceCall(web_service + uri, ServiceHandler.POST, json_request_string);

		if(json_response_string == null)
		{
			No_Internet_Connection();
		}
		else
		{
			JsonObject jsonObject = gson.fromJson(json_response_string, JsonObject.class);
			json_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONResponse.class);

			if(json_response.getSuccessful())
			{
				json_successful_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONSuccessfulResponse.class);
			}
			else
			{
				json_error_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONErrorResponse.class);
			}
		}
	}

	public void Get_Credit_Cards(Merchant_Contact merchant_contact_obj)
	{
		String uri = "merchant/merchant_contact/credit_card/get_all";

		JsonElement je = gson.toJsonTree(merchant_contact_obj);
		JsonObject jo = new JsonObject();
		jo.add("merchant_contact_obj", je);
		String json_request_string = jo.toString();

		ServiceHandler sh = new ServiceHandler();
		String json_response_string = sh.makeServiceCall(web_service + uri, ServiceHandler.POST, json_request_string);

		if(json_response_string == null)
		{
			No_Internet_Connection();
		}
		else
		{
			JsonObject jsonObject = gson.fromJson(json_response_string, JsonObject.class);
			json_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONResponse.class);

			if(json_response.getSuccessful())
			{
				json_successful_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONSuccessfulResponse.class);
				json_successful_response.setDataObj(gson.fromJson(((JsonObject)jsonObject.get("JSONResponse")).get("data_obj"), Credit_Card[].class));
			}
			else
			{
				json_error_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONErrorResponse.class);
			}
		}
	}

	public void Get_Default_Credit_Card(Merchant_Contact merchant_contact_obj)
	{
		String uri = "merchant/merchant_contact/credit_card/get_default";

		JsonElement je = gson.toJsonTree(merchant_contact_obj);
		JsonObject jo = new JsonObject();
		jo.add("merchant_contact_obj", je);
		String json_request_string = jo.toString();

		ServiceHandler sh = new ServiceHandler();
		String json_response_string = sh.makeServiceCall(web_service + uri, ServiceHandler.POST, json_request_string);

		if(json_response_string == null)
		{
			No_Internet_Connection();
		}
		else
		{
			JsonObject jsonObject = gson.fromJson(json_response_string, JsonObject.class);
			json_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONResponse.class);

			if(json_response.getSuccessful())
			{
				json_successful_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONSuccessfulResponse.class);
				json_successful_response.setDataObj(gson.fromJson(((JsonObject)jsonObject.get("JSONResponse")).get("data_obj"), Credit_Card.class));
			}
			else
			{
				json_error_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONErrorResponse.class);
			}
		}
	}

	public void Verify_Deal(Deal deal_obj)
	{
		String uri = "deal/verify";

		JsonElement je = gson.toJsonTree(deal_obj);
		JsonObject jo = new JsonObject();
		jo.add("deal_obj", je);
		String json_request_string = jo.toString();

		ServiceHandler sh = new ServiceHandler();
		String json_response_string = sh.makeServiceCall(web_service + uri, ServiceHandler.POST, json_request_string);

		if(json_response_string == null)
		{
			No_Internet_Connection();
		}
		else
		{
			JsonObject jsonObject = gson.fromJson(json_response_string, JsonObject.class);
			json_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONResponse.class);

			if(json_response.getSuccessful())
			{
				json_successful_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONSuccessfulResponse.class);
				json_successful_response.setDataObj(gson.fromJson(((JsonObject)jsonObject.get("JSONResponse")).get("data_obj"), Deal.class));
			}
			else
			{
				json_error_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONErrorResponse.class);
			}
		}
	}

	public void Add_Deal(Deal deal_obj)
	{
		String uri = "deal/add";

		JsonElement je = gson.toJsonTree(deal_obj);
		JsonObject jo = new JsonObject();
		jo.add("deal_obj", je);
		String json_request_string = jo.toString();

		ServiceHandler sh = new ServiceHandler();
		String json_response_string = sh.makeServiceCall(web_service + uri, ServiceHandler.POST, json_request_string);

		if(json_response_string == null)
		{
			No_Internet_Connection();
		}
		else
		{
			JsonObject jsonObject = gson.fromJson(json_response_string, JsonObject.class);
			json_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONResponse.class);

			if(json_response.getSuccessful())
			{
				json_successful_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONSuccessfulResponse.class);
				json_successful_response.setDataObj(gson.fromJson(((JsonObject)jsonObject.get("JSONResponse")).get("data_obj"), Deal.class));
			}
			else
			{
				json_error_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONErrorResponse.class);
			}
		}
	}

	public void Send_Deal_Validation(Deal deal_obj)
	{
		String uri = "deal/send_validation_code";

		JsonElement je = gson.toJsonTree(deal_obj);
		JsonObject jo = new JsonObject();
		jo.add("deal_obj", je);
		String json_request_string = jo.toString();

		ServiceHandler sh = new ServiceHandler();
		String json_response_string = sh.makeServiceCall(web_service + uri, ServiceHandler.POST, json_request_string);

		if(json_response_string == null)
		{
			No_Internet_Connection();
		}
		else
		{
			JsonObject jsonObject = gson.fromJson(json_response_string, JsonObject.class);
			json_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONResponse.class);

			if(json_response.getSuccessful())
			{
				json_successful_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONSuccessfulResponse.class);
			}
			else
			{
				json_error_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONErrorResponse.class);
			}
		}
	}

	public void Validate_Deal(Deal deal_obj)
	{
		String uri = "deal/validate";

		JsonElement je = gson.toJsonTree(deal_obj);
		JsonObject jo = new JsonObject();
		jo.add("deal_obj", je);
		String json_request_string = jo.toString();

		ServiceHandler sh = new ServiceHandler();
		String json_response_string = sh.makeServiceCall(web_service + uri, ServiceHandler.POST, json_request_string);

		if(json_response_string == null)
		{
			No_Internet_Connection();
		}
		else
		{
			JsonObject jsonObject = gson.fromJson(json_response_string, JsonObject.class);
			json_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONResponse.class);

			if(json_response.getSuccessful())
			{
				json_successful_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONSuccessfulResponse.class);
			}
			else
			{
				json_error_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONErrorResponse.class);
			}
		}
	}

	public void Get_Deals(Merchant_Contact merchant_contact_obj)
	{
		String uri = "deal/deals";

		JsonElement je = gson.toJsonTree(merchant_contact_obj);
		JsonObject jo = new JsonObject();
		jo.add("merchant_contact_obj", je);
		String json_request_string = jo.toString();

		ServiceHandler sh = new ServiceHandler();
		String json_response_string = sh.makeServiceCall(web_service + uri, ServiceHandler.POST, json_request_string);

		if(json_response_string == null)
		{
			No_Internet_Connection();
		}
		else
		{
			JsonObject jsonObject = gson.fromJson(json_response_string, JsonObject.class);
			json_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONResponse.class);

			if(json_response.getSuccessful())
			{
				json_successful_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONSuccessfulResponse.class);
				json_successful_response.setDataObj(gson.fromJson(((JsonObject)jsonObject.get("JSONResponse")).get("data_obj"), Deal[].class));
			}
			else
			{
				json_error_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONErrorResponse.class);
			}
		}
	}

	public void Get_Deal_Details(Deal deal_obj)
	{
		String uri = "deal/detail";

		JsonElement je = gson.toJsonTree(deal_obj);
		JsonObject jo = new JsonObject();
		jo.add("deal_obj", je);
		String json_request_string = jo.toString();

		ServiceHandler sh = new ServiceHandler();
		String json_response_string = sh.makeServiceCall(web_service + uri, ServiceHandler.POST, json_request_string);

		if(json_response_string == null)
		{
			No_Internet_Connection();
		}
		else
		{
			JsonObject jsonObject = gson.fromJson(json_response_string, JsonObject.class);
			json_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONResponse.class);

			if(json_response.getSuccessful())
			{
				json_successful_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONSuccessfulResponse.class);
				json_successful_response.setDataObj(gson.fromJson(((JsonObject)jsonObject.get("JSONResponse")).get("data_obj"), Fine_Print_Option[].class));
			}
			else
			{
				json_error_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONErrorResponse.class);
			}
		}
	}

	public void Cancel_Deal(Deal deal_obj)
	{
		String uri = "deal/cancel";

		JsonElement je = gson.toJsonTree(deal_obj);
		JsonObject jo = new JsonObject();
		jo.add("deal_obj", je);
		String json_request_string = jo.toString();

		ServiceHandler sh = new ServiceHandler();
		String json_response_string = sh.makeServiceCall(web_service + uri, ServiceHandler.POST, json_request_string);

		if(json_response_string == null)
		{
			No_Internet_Connection();
		}
		else
		{
			JsonObject jsonObject = gson.fromJson(json_response_string, JsonObject.class);
			json_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONResponse.class);

			if(json_response.getSuccessful())
			{
				json_successful_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONSuccessfulResponse.class);
			}
			else
			{
				json_error_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONErrorResponse.class);
			}
		}
	}

	public void Lookup_Certificate(Certificate certificate_obj)
	{
		String uri = "deal/certificate/lookup";

		JsonElement je = gson.toJsonTree(certificate_obj);
		JsonObject jo = new JsonObject();
		jo.add("certificate_obj", je);
		String json_request_string = jo.toString();

		ServiceHandler sh = new ServiceHandler();
		String json_response_string = sh.makeServiceCall(web_service + uri, ServiceHandler.POST, json_request_string);

		if(json_response_string == null)
		{
			No_Internet_Connection();
		}
		else
		{
			JsonObject jsonObject = gson.fromJson(json_response_string, JsonObject.class);
			json_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONResponse.class);

			if(json_response.getSuccessful())
			{
				json_successful_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONSuccessfulResponse.class);
				json_successful_response.setDataObj(gson.fromJson(((JsonObject)jsonObject.get("JSONResponse")).get("data_obj"), Certificate_Payment.class));
			}
			else
			{
				json_error_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONErrorResponse.class);
			}
		}
	}

	public void Redeem_Certificate(Certificate certificate_obj)
	{
		String uri = "deal/certificate/redeem";

		JsonElement je = gson.toJsonTree(certificate_obj);
		JsonObject jo = new JsonObject();
		jo.add("certificate_obj", je);
		String json_request_string = jo.toString();

		ServiceHandler sh = new ServiceHandler();
		String json_response_string = sh.makeServiceCall(web_service + uri, ServiceHandler.POST, json_request_string);

		if(json_response_string == null)
		{
			No_Internet_Connection();
		}
		else
		{
			JsonObject jsonObject = gson.fromJson(json_response_string, JsonObject.class);
			json_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONResponse.class);

			if(json_response.getSuccessful())
			{
				json_successful_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONSuccessfulResponse.class);
			}
			else
			{
				json_error_response = gson.fromJson(jsonObject.get("JSONResponse"), JSONErrorResponse.class);
			}
		}
	}


	private void No_Internet_Connection()
	{
		json_response = new JSONResponse();
		json_response.setSuccessful(false);

		System_Error system_error_obj = new System_Error();
		system_error_obj.setCode(500);
		system_error_obj.setMessage("No Internet Connectin");

		json_error_response = new JSONErrorResponse();
		json_error_response.setSuccessful(json_response.getSuccessful());
		json_error_response.setSystemError(system_error_obj);
	}


	public JSONResponse getJSONResponse()
	{
		return this.json_response;
	}

	public JSONSuccessfulResponse getJSONSuccessfulResponse()
	{
		return this.json_successful_response;
	}

	public JSONErrorResponse getJSONErrorResponse()
	{
		return this.json_error_response;
	}
}

