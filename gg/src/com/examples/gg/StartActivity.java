package com.examples.gg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class StartActivity extends Activity {
	ImageButton youtube;
	ImageButton youku;  
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            
        addListenerOnButton();
        
        
//        Intent playIntent = new Intent(Intent.ACTION_VIEW);
//        playIntent.setData(Uri.parse("http://www.youtube.com/watch?v=sMM0R19IisI"));
//        playIntent.putExtra("force_fullscreen", true);
//        
//        
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=sMM0R19IisI"));
//        intent.putExtra("force_fullscreen",true); 
//        startActivity(intent);
       // Intent intent = new Intent(this, MainActivity.class);


        //MainActivity.this.startActivity(playIntent);
    }


	public void addListenerOnButton() {
		 
		youtube = (ImageButton) findViewById(R.id.imageButton1);
 
		youtube.setOnClickListener(new OnClickListener() {
 
		
			@Override
			public void onClick(View v) {
				Toast.makeText(StartActivity.this,"Youtube is selected", Toast.LENGTH_SHORT).show();
				   //Intent youtubeIntent = new Intent(StartActivity.this,TabActivity.class);
				Intent youtubeIntent = new Intent(StartActivity.this,SideMenuActivity.class);
				startActivity(youtubeIntent);

				   
			}
 
		});
		
		youku=(ImageButton)findViewById(R.id.imageButton2);
		youku.setOnClickListener(new OnClickListener() {
			 
			
			@Override
			public void onClick(View v) {
				Toast.makeText(StartActivity.this,"Youku is selected", Toast.LENGTH_SHORT).show();
				   Intent youtubeIntent = new Intent(StartActivity.this,SideMenuActivity.class);
				   startActivity(youtubeIntent);
		
			}
 
		});
 
	}
	
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
