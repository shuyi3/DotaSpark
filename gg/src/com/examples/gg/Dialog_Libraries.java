package com.examples.gg;

import android.app.AlertDialog;
import android.content.Context;
import android.preference.DialogPreference;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class Dialog_Libraries extends DialogPreference{

	public Dialog_Libraries(Context context, AttributeSet attrs) {
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
    	            "<a href=\"https://github.com/nostra13/Android-Universal-Image-Loader\">Using Universal Image Loader (c) 2011-2013, Sergey Tarasevich</a>"));
    		mLink1.setMovementMethod(LinkMovementMethod.getInstance());
    		    		
    		TextView mLink2 = (TextView) view.findViewById(R.id.open_source_link2);
    		mLink2.setText(Html.fromHtml(
    	            "<a href=\"https://github.com/shontauro/android-pulltorefresh-and-loadmore\">Load More Listview (c) 2012 Fabian Leon</a>"));
    		mLink2.setMovementMethod(LinkMovementMethod.getInstance());
    		
    		TextView license = (TextView) view.findViewById(R.id.apache_license);
    		license.setText(Html.fromHtml(
    	            "These projects are licensed under the Apache License v2.0.<br><br>" + 
    				"You may obtain a copy of the License at:<br><br>" + 
            		"<a href=\"http://www.apache.org/licenses/\">http://www.apache.org/licenses</a><br>"));
    		license.setMovementMethod(LinkMovementMethod.getInstance());

            super.onBindDialogView(view);
    }
    

    
}
