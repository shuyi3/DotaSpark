package com.examples.gg;

import java.util.Random;

import com.examples.gg.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class openanimation extends Activity{
public static final String PREFS_NAME="first_night";
	 
@Override
public void onCreate(Bundle savedInstanceState){
	super.onCreate(savedInstanceState);
	setContentView(R.layout.open);
	
	ImageView image;
	image = (ImageView) findViewById(R.id.imageView1);
	
	int i1 = (int) (Math.random()*3);
	if(i1==1){
	image.setImageResource(R.drawable.sf);
	}else if(i1==0){
		image.setImageResource(R.drawable.xiaogou);
		
	}else {
		image.setImageResource(R.drawable.xiaohei);
		
		
	}
	
	 SharedPreferences preferences=getSharedPreferences(PREFS_NAME,0);
	 boolean fn = preferences.getBoolean("fn", true);
     if(fn){
	 
	 
	new Handler().postDelayed(new Runnable(){
	@Override
		public void run(){
	
		
		
		Intent intent=new Intent(openanimation.this,ViewPagerExample.class);
		startActivity(intent);
		openanimation.this.finish();
		
	}
	}, 1000);
     
	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
    SharedPreferences.Editor editor = settings.edit();
    editor.putBoolean("fn", false);
    editor.commit();
	
     }else{
    	 new Handler().postDelayed(new Runnable(){
    			@Override
    				public void run(){
    			
    				
    				
    				Intent intent=new Intent(openanimation.this,StartActivity.class);
    				startActivity(intent);
    				openanimation.this.finish();
    				
    			}
    			}, 2000);
    	 
     }
}
	
}
