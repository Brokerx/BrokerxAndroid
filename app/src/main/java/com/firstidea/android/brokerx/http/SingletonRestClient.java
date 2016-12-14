package com.firstidea.android.brokerx.http;

import android.content.Context;

import com.firstidea.android.brokerx.utils.SharedPreferencesUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.HttpURLConnection;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.Request;
import retrofit.client.UrlConnectionClient;
import retrofit.converter.GsonConverter;

public class SingletonRestClient {

	private static RestAdapter restAdapter = null;
//		public static String BASE_URL = "https://secure.reachx.in/";
		public static String BASE_URL = "http://reachx.in:8080/Brokerx/";
//	private static String BASE_URL = "http://192.168.0.100:8080/Brokerx/";
	public static String BASE_IMAGE_URL = BASE_URL + "Images/";
	public static String BASE_PROFILE_IMAGE_URL = BASE_URL + "Images/UserProfilePhotos/";
	public static String BASE_FILES_URL = BASE_URL + "files/";
	public static String ADVT_BASE_IMAGE_URL = BASE_IMAGE_URL + "Advertisements/";
	private static String BASE_API_URL = BASE_URL + "webresources/";
	private static void initRestAdapter(Context mContext) {
		if(restAdapter == null) {
	    	ReachxRequestInterceptor requestInterceptor = new ReachxRequestInterceptor(mContext);
			Gson gson = new GsonBuilder()
                .setDateFormat("yyyyy-MM-dd HH:mm:ss")
                .create();

			restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setRequestInterceptor(requestInterceptor)
                .setEndpoint(BASE_API_URL)
                .setConverter(new GsonConverter(gson))
                .build();
		}
	}
	
	/* A private Constructor prevents any other 
    * class from instantiating.
    */
    private SingletonRestClient(){}

	public static <S> S createService(Class<S> serviceClass, Context mContext) {
		initRestAdapter(mContext);
		return restAdapter.create(serviceClass);
	}

	public static <S> S createFileUploadService(Class<S> serviceClass, Context mContext) {
		ReachxRequestInterceptor requestInterceptor = new ReachxRequestInterceptor(mContext);
		Gson gson = new GsonBuilder()
				.setDateFormat("yyyyy-MM-dd HH:mm:ss")
				.create();
		RestAdapter uploadRestAdapter =  new RestAdapter.Builder()
				.setLogLevel(RestAdapter.LogLevel.FULL)
				.setRequestInterceptor(requestInterceptor)
				.setEndpoint(BASE_API_URL)
				.setConverter(new GsonConverter(gson))
				.setClient(new FileUploadClient())
			    .build();
		return restAdapter.create(serviceClass);
	}
	
	public static class ReachxRequestInterceptor implements RequestInterceptor {

		private Context mContext;
		
		public ReachxRequestInterceptor(Context mContext) {
			super();
			this.mContext = mContext;
		}

		/**
	     * Injects cookies to every request for session management
	     */
		@Override
		public void intercept(RequestFacade request) {
			String cookies = SharedPreferencesUtil.getSharedPreferencesString(mContext, "SESSION_COOKIES", "");

			if (null != cookies && cookies.length() > 0) {
                request.addHeader("Cookie", cookies);
            }
		}
		
	}

	public final static class FileUploadClient extends UrlConnectionClient {
		@Override
		protected HttpURLConnection openConnection(Request request) {
			HttpURLConnection connection = null;
			try {
				connection = super.openConnection(request);
			} catch (IOException e) {
				e.printStackTrace();
			}
			connection.setConnectTimeout((1 * 1000 ) * 60); // 1 minute
			connection.setReadTimeout((1 * 1000 ) * 60);
			return connection;
		}
	}
}
