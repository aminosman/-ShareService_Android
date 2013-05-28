package com.amin.socialemail;

import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ListView;



public class MainSlideListener implements View.OnTouchListener {

    private View view;
    private GestureDetector gestureDetector;
    private int leftM=0;
    private ListView list;
    private boolean touch = false;


    public MainSlideListener(MainActivity main, View view2) {
    	this.view=view2;
    //	Debug.out("Listner made");
    	//this.list=listview;
        gestureDetector = new GestureDetector(view2.getContext(), myGestureListener);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
    //	Debug.out("Listner touched");

    	if ( event.getActionMasked() == MotionEvent.ACTION_DOWN){
    		touch = true;

    	}
    	if ( event.getActionMasked() == MotionEvent.ACTION_UP)
         {
    		if(touch){
        		Debug.out("Tap");

    		}
             MarginLayoutParams lp = (MarginLayoutParams) v.getLayoutParams();
            Debug.out("Action Up");
    		   int width= v.getResources().getDisplayMetrics().widthPixels;   		   
    	        view.requestLayout();
    	        if(this.leftM==0){
    	        	if(lp.leftMargin>(width/4)*1){
    	    			   lp.leftMargin = (int) (width-(.1f*(float)width));
    	    			   lp.rightMargin = (int) (-width+(.1f*(float)width));
    	    			   view.requestLayout();
    	    		   }
    	    		   else if(lp.leftMargin<-(width/4)*1){ 
    	    			  // Debug.out("Left Mrg: "+(int) (-width+(.1f*(float)width)));
    	    			  // Debug.out("Right Mrg: "+(int) (width-(.1f*(float)width)));

    	    			   lp.leftMargin = (int) (-width+(.1f*(float)width));
    	    			   lp.rightMargin = (int) (width-(.1f*(float)width));
    	    			   view.requestLayout();
    	    		   }
    	    		   else{
    	    			   lp.leftMargin = 0;
    	    			   lp.rightMargin = 0;
    	    			   view.requestLayout();
    	    		   }
    	       }
    	        else{
    	        	if((lp.leftMargin<(width/4)*3 && lp.leftMargin>0) ||(lp.leftMargin>-(width/4)*3&&lp.leftMargin<-(width/4)*1)){
    	        		lp.leftMargin = 0;
 	    			   lp.rightMargin = 0;
 	    			   view.requestLayout();
    	    		   }
    	        	else{
    	        		lp.leftMargin=leftM;
    	        		if(leftM>0){
    	        			lp.rightMargin = (int) (-width+(.1f*(float)width));
    	        		}
    	        		else{
     	    			   lp.rightMargin = (int) (width-(.1f*(float)width));
    	        		}
    	        		 
    	        	}
    	    		   
    	        }
    		   this.leftM=lp.leftMargin;
    		  

         }
    	 view = v;
	      gestureDetector.onTouchEvent(event);
	        return true;


    }
   
    private SimpleOnGestureListener myGestureListener = new SimpleOnGestureListener() {

    private int origLeft, origRight;

    @Override
	public boolean onDown(MotionEvent e) {
        MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();
        origLeft = lp.leftMargin;
        origRight = lp.rightMargin;
   // 	Debug.out("Down");

        return true;
    };
    @Override
    public boolean onDoubleTap(MotionEvent e) {


        return true;
    };

    @Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

        //holder.card.requestDisallowInterceptTouchEvent(true);
       MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();
        lp.leftMargin = (int) (origLeft + (e2.getRawX() - e1.getRawX()));
        lp.rightMargin = (int) (origRight - (e2.getRawX() - e1.getRawX()));
        Debug.out (lp.leftMargin );
       view.requestLayout();
        return true;
    };
    };
}