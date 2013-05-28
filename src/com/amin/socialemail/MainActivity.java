package com.amin.socialemail;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.ToggleButton;

import com.androidquery.AQuery;
import com.androidquery.callback.BitmapAjaxCallback;

public class MainActivity extends Activity {
	public static AQuery aq;
	private MiddleMan middle;
	private float width;
	public RelativeLayout view;
	public static RelativeLayout scroll;
	public List<ImageView> images;
	private MemoryInfo mi;
	private ActivityManager activityManager;
	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	    aq = new AQuery(this);
	    PictureCard.ID=0;
	    MiddleMan.position=0;
	    
	    mi = new MemoryInfo();
	    activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
	   
	    
	    final ListView listview = (ListView) findViewById(R.id.listview);
	    images = new ArrayList<ImageView>();
	    
	    view = (RelativeLayout) findViewById(R.id.mainview);
			  view.setOnTouchListener(new MainSlideListener(this, view));
	    scroll = (RelativeLayout) findViewById(R.id.scroll);
			 

		width= this.getResources().getDisplayMetrics().widthPixels;
	    view.setBackgroundColor(0xFFEE3333);

	    MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();
	    lp.leftMargin = (int) (width);
		   lp.rightMargin = (int) (-width);
		   view.requestLayout();

	    MyAdapter adapter = new MyAdapter(this);
	    listview.setAdapter(adapter);
	    middle = new MiddleMan(this, adapter);
	    
	  }
	  @Override
	  public void onBackPressed() {
		  Debug.out("Back");
		  MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();
		    lp.leftMargin = (int) (width);
			   lp.rightMargin = (int) (-width);
			   view.requestLayout();
			   NewImageView.cleanUp();

	  }
	  @Override
	  public boolean onKeyDown(int keyCode, KeyEvent event) {
	      if (keyCode == KeyEvent.KEYCODE_BACK) {
			  Debug.out("Back");

	    	  MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();
	  	    lp.leftMargin = (int) (width);
	  		   lp.rightMargin = (int) (-width);
			   view.requestLayout();
			   NewImageView.cleanUp();

	      }

	      return super.onKeyDown(keyCode, event);
	  }

	  public void updateCards(){
		  middle.feedMe();
	  }
	  public void memory(){
		  ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		  int memoryClass = am.getMemoryClass();
		  activityManager.getMemoryInfo(mi);
			long availableMegs = mi.availMem / 1048576L;
		    Debug.out("MEMORY UPDATE: "+availableMegs);
			  Debug.out("memoryClass:" + Integer.toString(memoryClass));
			// Get current size of heap in bytes
			  long heapSize = Runtime.getRuntime().totalMemory()/(1024*1024); 

			  // Get maximum size of heap in bytes. The heap cannot grow beyond this size.// Any attempt will result in an OutOfMemoryException.
			  long heapMaxSize = Runtime.getRuntime().maxMemory()/(1024*1024);
			  Debug.out("HeapSize :"+heapSize);
			//  onLowMemory();

			
	  }
	  @Override
	    public void onLowMemory(){  
Debug.out("CLEANING CLEANING CLEANING");
	        //clear all memory cached images when system is in low memory
	        //note that you can configure the max image cache count, see CONFIGURATION
	        BitmapAjaxCallback.clearCache();
	    }

	 
}
