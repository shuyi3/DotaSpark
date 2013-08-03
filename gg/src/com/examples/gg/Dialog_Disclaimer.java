package com.examples.gg;

import android.app.AlertDialog;
import android.content.Context;
import android.preference.DialogPreference;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class Dialog_Disclaimer extends DialogPreference{

	public Dialog_Disclaimer(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		this.setDialogLayoutResource(R.layout.open_source_preference);
		

	}
	
    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
            //builder.setTitle(R.string.pin_changepin_title);
            builder.setPositiveButton(null, null);
            builder.setNegativeButton(null, null);
            super.onPrepareDialogBuilder(builder);  
    }
	
    @Override
    public void onBindDialogView(View view){
    		TextView notice = (TextView) view.findViewById(R.id.notices);
    		notice.setVisibility(View.GONE);
    		
    		TextView mLink1 = (TextView) view.findViewById(R.id.open_source_link1);
    		mLink1.setVisibility(View.GONE);

    		
    		TextView mLink2 = (TextView) view.findViewById(R.id.open_source_link2);
    		mLink2.setVisibility(View.GONE);
    		    		
    		TextView license = (TextView) view.findViewById(R.id.apache_license);
    		license.setText(Html.fromHtml(
    	            "<p>The copyright of all Dota 2 wallpapers and icon are owned by Valve Corporation.</p>" + 
    				"<p>All the videos and match information are collected from Internet autonomously by programs. We only provide the links to other media. "+
    	            "This application does not generate any media contents.</p>" + 
            		"<p>If there are any contents that violate your rights, please contact us.</p>"));
    		license.setMovementMethod(LinkMovementMethod.getInstance());

            super.onBindDialogView(view);
    }
    

    
}
