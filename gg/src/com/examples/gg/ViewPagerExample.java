package com.examples.gg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
 
public class ViewPagerExample extends FragmentActivity {
    private MyAdapter mAdapter;
    private ViewPager mPager;
    Button btn1;
    Button btn2;
    Button btn3;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.navigation_1);
        mAdapter = new MyAdapter(getSupportFragmentManager());
        btn1=(Button)findViewById(R.id.button1);
        btn2=(Button)findViewById(R.id.button2);
        btn3=(Button)findViewById(R.id.button3);
        
        setButton(btn1,"1",40,40);
        setButton(btn2,"",20,20);
        setButton(btn3,"",20,20);
        
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        mPager.setOnPageChangeListener(new OnPageChangeListener(){
        	 
            @Override
            public void onPageScrollStateChanged(int position) {}
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {}
            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                btnAction(position);
                
            }

        });
    }
    public void startsparknow(View v){
   	 Intent intent=new Intent();
   	 intent.setClass(ViewPagerExample.this, animation.class);
   	 startActivity(intent);
   	 this.finish();
    }
    
    public void startspark(){
      	 Intent intent=new Intent();
      	 intent.setClass(ViewPagerExample.this, animation.class);
      	 startActivity(intent);
      	 this.finish();
       }
    private void setButton(Button btn,String text,int h, int w){
        btn.setWidth(w);
        btn.setHeight(h);
        btn.setText(text);
    }
 
    private void btnAction(int action){
        switch(action){
          case 0: btn1.setBackgroundResource(R.drawable.d2_selected);btn2.setBackgroundResource(R.drawable.d2_unselected);btn3.setBackgroundResource(R.drawable.d2_unselected); break;
 
          case 1: btn1.setBackgroundResource(R.drawable.d2_unselected);btn2.setBackgroundResource(R.drawable.d2_selected);btn3.setBackgroundResource(R.drawable.d2_unselected); break;
          
          case 2: btn1.setBackgroundResource(R.drawable.d2_unselected);btn2.setBackgroundResource(R.drawable.d2_unselected);btn3.setBackgroundResource(R.drawable.d2_selected);break;
         
          case 3: setButton(btn3,"3",40,40); setButton(btn1,"",20,20);setButton(btn2,"",20,20); break;
          
          
        }
    }
    public class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }
 
        @Override
        public int getCount() {
            return 5;
        }
 
        @Override
        public Fragment getItem(int position) {
            switch (position) {
            case 0:
            System.out.println("0");
            	return new navigation_1();
                
            case 1:

                System.out.println("1");
                	
            	return new ImageFragment(R.drawable.baihumeimei);
            case 2:
//
//                System.out.println("2");
//                	
                return new ImageFragment(R.drawable.kaer);
            case 3:
                return new ImageFragment(R.drawable.shouwang);
            case 4:
            	startspark();
                return new ImageFragment(R.drawable.shouwang);
                
            default:
                return null;
            }
        }


    }
}