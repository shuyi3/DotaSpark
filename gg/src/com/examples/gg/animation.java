package com.examples.gg;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class animation extends Activity {
@Override
protected void onCreate(Bundle savedInstanceState) {
// TODO Auto-generated method stub
super.onCreate(savedInstanceState); 
InputStream stream=null;

stream=this.getResources().openRawResource(R.drawable.jiguang);
if(stream==null){
	System.out.println("abc");
}else{
	System.out.println("notabc");
}
mygifview view = new mygifview(this, stream);

 setContentView(R.layout.viewgroup);
 ViewGroup parent = (ViewGroup) findViewById(R.id.vertical_container);
//
// RelativeLayout relativeLayout = new RelativeLayout(this); 

 LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
         LinearLayout.LayoutParams.MATCH_PARENT,
         LinearLayout.LayoutParams.MATCH_PARENT
         
        		 );

 view.setLayoutParams(lp);

parent.addView(view);

// parent.addView(view);
// 
//setContentView(parent);
 
//setContentView(R.layout.animation);
//img_left = (ImageView) findViewById(R.id.left);
//img_right = (ImageView) findViewById(R.id.right);
//AnimationSet animLeft = new AnimationSet(true);
//TranslateAnimation transLeft = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,-1f, Animation.RELATIVE_TO_SELF, 0f,Animation.RELATIVE_TO_SELF, 0f);
//transLeft.setDuration(2000);
//animLeft.addAnimation(transLeft);
//animLeft.setFillAfter(true);
//img_left.startAnimation(transLeft);
//transLeft.startNow();
//AnimationSet animRight = new AnimationSet(true);
//TranslateAnimation transRight = new TranslateAnimation(
//Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
//1f, Animation.RELATIVE_TO_SELF, 0f,
//Animation.RELATIVE_TO_SELF, 0f);
//transRight.setDuration(2000); 
// 
//animRight.addAnimation(transRight);
//animRight.setFillAfter(true);
//img_right.startAnimation(transRight);
//transRight.startNow();
new Handler().postDelayed(new Runnable() {
@Override
public void run() {
// TODO Auto-generated method stub
	System.out.println("jin le ");
Intent intent = new Intent(animation.this, StartActivity.class);
startActivity(intent);
animation.this.finish();
}
}, 3000);
}
}