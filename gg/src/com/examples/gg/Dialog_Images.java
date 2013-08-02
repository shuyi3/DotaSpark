package com.examples.gg;

import android.app.AlertDialog;
import android.content.Context;
import android.preference.DialogPreference;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class Dialog_Images extends DialogPreference{

	public Dialog_Images(Context context, AttributeSet attrs) {
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
    		TextView mLink1 = (TextView) view.findViewById(R.id.open_source_link1);
    		mLink1.setText(Html.fromHtml(
    	            "<a href=\"http://www.pixelledesigns.com\">Icons and graphics elements by Happy Icon Studio</a>"));
    		mLink1.setMovementMethod(LinkMovementMethod.getInstance());
    		
    		TextView mLink2 = (TextView) view.findViewById(R.id.open_source_link2);
    		mLink2.setVisibility(View.GONE);
    		    		
    		TextView license = (TextView) view.findViewById(R.id.apache_license);
    		license.setText(Html.fromHtml(
    	            "This collection is licensed under the Creative Commons Attribution 3.0 United States License.<br><br>" + 
    				"To view a copy of this license, visit:<br><br>" + 
            		"<a href=\"http://creativecommons.org/licenses/by/3.0/us/\">http://creativecommons.org/licenses/by/3.0/us/</a><br>"));
    		license.setMovementMethod(LinkMovementMethod.getInstance());

            super.onBindDialogView(view);
    }
    

    
}
