package com.firstidea.android.brokerx.widget;


import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.firstidea.android.brokerx.R;


public class AppProgressDialog {
	
	public static Dialog show(Context ctx){
		final Dialog dialog = new Dialog(ctx, R.style.DialogFullScreen);
		dialog.setContentView(R.layout.app_progess_dialog);
	    dialog.setCancelable(false);
	    dialog.show();
	    return dialog;
	}

}
