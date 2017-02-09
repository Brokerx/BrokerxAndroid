package com.firstidea.android.brokerx.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.firstidea.android.brokerx.Constants;
import com.firstidea.android.brokerx.R;
import com.firstidea.android.brokerx.constants.AppConstants;
import com.firstidea.android.brokerx.fcm.MyFirebaseInstanceIDService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.regex.Pattern;

//import com.google.android.gcm.GCMRegistrar;
//import com.firstidea.android.garnet.Constants;
//import com.firstidea.android.garnet.R;

public class AppUtils {
	public final static boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			Pattern regex = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);;
			return regex.matcher(target).matches();
		}
	}

	public static String getDeviceId(Context context) {
        TelephonyManager mytelephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        Log.d("GK", "Device Id:" + mytelephonyManager.getDeviceId());
        return mytelephonyManager.getDeviceId();
    }

    public static String GetMimeType(Context context, Uri uriImage)
    {
        String strMimeType = null;

        Cursor cursor = context.getContentResolver().query(uriImage,
                new String[] { MediaStore.MediaColumns.MIME_TYPE },
                null, null, null);

        if (cursor != null && cursor.moveToNext())
        {
            strMimeType = cursor.getString(0);
        }

        return strMimeType;
    }

    public static String getDir(Context ctx){
        return Environment.getExternalStorageDirectory()+ File.separator+ctx.getString(R.string.app_name);
    }

    public static String getImagePath(String prefix, Context ctx){
        return Environment.getExternalStorageDirectory()+ File.separator+ctx.getString(R.string.app_name)+ File.separator+"IMAGE_"+prefix+".jpg";
    }

	public static String getDocumentFilePath(String fileName, Context ctx){
		String path = Environment.getExternalStorageDirectory()+ File.separator+ctx.getString(R.string.app_name)+ File.separator+"Documents";
		File file = new File(path);
		if(!file.exists()){
			file.mkdirs();
		}
        return Environment.getExternalStorageDirectory()+ File.separator+ctx.getString(R.string.app_name)+ File.separator+"Documents"+ File.separator+fileName;
    }

	public static boolean isFileAvailable(String fileName,Context ctx) {
		String filePathString = getDocumentFilePath(fileName,ctx);
		File f = new File(filePathString);
		if(f.exists() && !f.isDirectory()) {
			return true;
		}
		return false;
	}


/*
	public static String LoadGCMKey(Activity activity) {
		try {
			InstanceID instanceID = InstanceID.getInstance(activity);
			String token = instanceID.getToken(activity.getString(R.string.gcm_defaultSenderId),
					GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
			SharedPreferencesUtil.putSharedPreferencesString(activity, AppConstants.KEY_GCM_REG_TOKEN, token);
			return token;
		}catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}*/
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    public static boolean checkPlayServices(Activity activity) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(activity, resultCode, AppConstants.PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("AppUtile", "This device is not supported.");
            }
            return false;
        }
        return true;
    }

	public static void loadFCMid(final Activity context) {
		if (checkPlayServices(context)) {

			Intent intent = new Intent(context, MyFirebaseInstanceIDService.class);
			context.startService(intent);
			try {
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							String token = FirebaseInstanceId.getInstance().getToken();
							Log.i("TAG", "FCM Registration Token: " + token);
							String prevDeviceToken = SharedPreferencesUtil.getSharedPreferencesString(
									context, AppConstants.KEY_GCM_REG_TOKEN, null);
//                            Log.i("TAG", "PREVIOUS Token: " + prevDeviceToken);
							SharedPreferencesUtil.putSharedPreferencesString(context, AppConstants.KEY_GCM_REG_TOKEN, token);
							if (prevDeviceToken != null && !prevDeviceToken.equals(token)) {
								GcmUtils.registerGCMOnServer(context);
							}
							int prev_version = SharedPreferencesUtil.getSharedPreferencesInt(context, AppConstants.PREV_VERSION, 0);
							int current_version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;

//							if (Preferences.DEBUG) {
							Log.d("PREVIOUS VERSION", "" + prev_version);
							Log.d("CURRENT VERSION", "" + current_version);
//							}
							if (current_version > prev_version) {
//                                GCMRegistrar.register(context, GCMSenderID);
								SharedPreferencesUtil.putSharedPreferencesInt(context, AppConstants.PREV_VERSION, current_version);
								GcmUtils.registerGCMOnServer(context);
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}).start();

			} catch (Exception e) {
				e.printStackTrace();
			}


		}
	}
	public static Drawable getPressedStateDrawable(Context context){
		StateListDrawable states = new StateListDrawable();
		states.addState(new int[] {android.R.attr.state_pressed},
				context.getResources().getDrawable(R.color.main_color_lite));
		states.addState(new int[] {android.R.attr.state_focused},
				context.getResources().getDrawable(R.color.main_color_lite));
		states.addState(new int[] {android.R.attr.state_selected},
				context.getResources().getDrawable(R.color.main_color_lite));
		states.addState(new int[] { },context.getResources().getDrawable(R.drawable.listview_row_bg));

		return states;
	}

    public static Bitmap decodeFile(File f, int width, int height){
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);

            //Find the correct scale value. It should be the power of 2.
            int scale=1;
            while(o.outWidth/scale/2>=width && o.outHeight/scale/2>=height)
                scale*=2;

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
			e.printStackTrace();
		}
        return null;
    }

	public static String getRealPathFromURI(Uri contentURI, Context mContext) {
		Cursor cursor = mContext.getContentResolver().query(contentURI, null, null, null, null);
		if (cursor == null) { 
			return contentURI.getPath();
		} else { 
			cursor.moveToFirst(); 
			int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
			return cursor.getString(idx); 
		}
	}

	public static int getCameraPhotoOrientation(Context context, Uri imageUri, String imagePath){
		int rotate = 0;
		try {
			context.getContentResolver().notifyChange(imageUri, null);
			File imageFile = new File(imagePath);
			ExifInterface exif = new ExifInterface(
					imageFile.getAbsolutePath());
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_270:
				rotate = 270;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				rotate = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_90:
				rotate = 90;
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rotate;
	}
	
	public static byte[] toByteArray(Bitmap bmp) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
	    bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
	    byte[] byteArray = stream.toByteArray();
	    
		return byteArray;
	}
/*

	public static boolean isConnectedToInternet(Context context, boolean isShowError) {
		ConnectivityManager cm =
				(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null &&
				activeNetwork.isConnectedOrConnecting();
		if (!isConnected && isShowError) {
			new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
					.setTitleText("No Internet Connection...")
					.setContentText("Please check your connection & try again.")
					.show();
		}
		return isConnected;
	}
*/

	/*
	public static boolean isConnectedToInternet(Context context, boolean isShowError) {
		ConnectivityManager cm =
		        (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		 
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null &&
		                      activeNetwork.isConnectedOrConnecting();
		if(!isConnected && isShowError) {
			Toast.makeText(context, "No Internet Connection.", Toast.LENGTH_SHORT).show();
			*//*new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
		    .setTitleText("No Internet Connection...")
		    .setContentText("Please check your connection & try again.")
		    .show();*//*
		}
		return isConnected;
	}
	
	public static boolean isDento(Context context) {
		String packageName = SharedPreferencesUtil.getSharedPreferencesString(context, "APP_TYPE", "");//context.getPackageName();
		return packageName.equalsIgnoreCase("dental");
	}
	
	public static String getGCMSenderID(Context context) {
		if(isDento(context)) {
			return Constants.DENTO_GCM_SENDER_ID;
		}
		return Constants.DOCTO_GCM_SENDER_ID;
	}*/
	/*
	public static void loadGCMid(Context context) {
		GCMRegistrar.checkDevice(context);
		GCMRegistrar.checkManifest(context);
		String GCMSenderID = getGCMSenderID(context);
		final String regId = GCMRegistrar.getRegistrationId(context);
		if (regId.equals("")) {
			GCMRegistrar.register(context, GCMSenderID);  //You will receive GCMID in "GCMIntentService.java"
		} 
		else{
			 SharedPreferencesUtil.putSharedPreferencesString(context, Constants.GCM_REGISTRATION_TOKEN, regId);
			 Log.v("GCM Registering", "GCM Already registered");
			 try {
				  int prev_version=SharedPreferencesUtil.getSharedPreferencesInt(context,Constants.PREV_VERSION,1);
				  int current_version=context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
				  if(prev_version <= 21) {
					  SharedPreferencesUtil.putSharedPreferencesBoolean(context, Constants.IS_FORCE_LOGOUT, true);
				  }
				  if(Preferences.DEBUG){
					  Log.d("PREVIOUS VERSION", ""+prev_version);
					  Log.d("CURRENT VERSION", ""+current_version);
				  }
				  if(current_version>prev_version){
					  GCMRegistrar.register(context, GCMSenderID); 
					  SharedPreferencesUtil.putSharedPreferencesInt(context, Constants.PREV_VERSION, current_version);
				  }
		    } 
		  catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		}
	}*/


	public static void showKeyboard(Context context, EditText edittext) {
		final InputMethodManager inputMethodManager = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.showSoftInput(edittext, InputMethodManager.SHOW_IMPLICIT);
	}

	public static void hideKeyboard(Context context, EditText edittext) {
		final InputMethodManager inputMethodManager = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(edittext.getWindowToken(), 0);
	}

	public static float convertDpToPixel(float dp, Context context){
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
		return px;
	}

	public static float convertPixelsToDp(float px, Context context){
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
		return dp;
	}
}
